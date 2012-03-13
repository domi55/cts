/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.cts.tradefed.testtype;

import com.android.cts.tradefed.build.CtsBuildHelper;
import com.android.tradefed.build.IBuildInfo;
import com.android.tradefed.device.DeviceNotAvailableException;
import com.android.tradefed.result.ITestInvocationListener;
import com.android.tradefed.util.FileUtil;

import junit.framework.TestCase;

import java.io.File;

/**
 * Running the accessibility tests requires modification of secure
 * settings. Secure settings cannot be changed from device CTS tests
 * since system signature permission is required. Such settings can
 * be modified by the shell user, so a host side test is used for
 * installing a package with a delegating accessibility service, enabling
 * this service, running these tests, disabling the service, and removing
 * the delegating accessibility service package.
 */
public class AccessibilityServiceTestRunner extends InstrumentationApkTest {

    private static final String DELEGATING_ACCESSIBLITY_SERVICE_PACKAGE_NAME =
        "android.accessibilityservice.delegate";

    private static final String DELEGATING_ACCESSIBLITY_SERVICE_NAME =
        "android.accessibilityservice.delegate.DelegatingAccessibilityService";

    private static final String DELEGATING_ACCESSIBLITY_SERVICE_APK =
        "CtsDelegatingAccessibilityService.apk";

    private CtsBuildHelper mCtsBuild;

    @Override
    public void setBuild(IBuildInfo build) {
        super.setBuild(build);
        mCtsBuild  = CtsBuildHelper.createBuildHelper(build);
    }

    @Override
    public void run(ITestInvocationListener listener) throws DeviceNotAvailableException {
        beforeTest();
        super.run(listener);
        afterTest();
    }

    private void beforeTest() throws DeviceNotAvailableException {
        installApkAndAssert(DELEGATING_ACCESSIBLITY_SERVICE_APK);
        enableAccessibilityAndDelegatingService();
    }

    private void afterTest() throws DeviceNotAvailableException {
        disableAccessibilityAndDelegatingService();
        uninstallAndAssert(DELEGATING_ACCESSIBLITY_SERVICE_PACKAGE_NAME);
    }

    private void installApkAndAssert(String apkName) throws DeviceNotAvailableException {
        File file = FileUtil.getFileForPath(mCtsBuild.getTestCasesDir(), apkName);
        String errorMessage = getDevice().installPackage(file, true);
        TestCase.assertNull("Error installing: " + apkName, errorMessage);
    }

    private void uninstallAndAssert(String packageName) throws DeviceNotAvailableException {
        String errorMessage = getDevice().uninstallPackage(packageName);
        TestCase.assertNull("Error uninstalling: " + packageName, errorMessage);
    }

    private void enableAccessibilityAndDelegatingService() throws DeviceNotAvailableException {
        // The properties may not be in the database, therefore they are first removed
        // and then added with the right value. This avoid inserting the same setting
        // more than once and also avoid parsing the result of a query shell command.
        String componentName = DELEGATING_ACCESSIBLITY_SERVICE_PACKAGE_NAME + "/"
            + DELEGATING_ACCESSIBLITY_SERVICE_NAME;
        getDevice().executeShellCommand(
                "content delete"
                + " --uri content://settings/secure"
                + " --where \"name='enabled_accessibility_services'\"");
        getDevice().executeShellCommand(
                "content insert"
                + " --uri content://settings/secure"
                + " --bind name:s:enabled_accessibility_services"
                + " --bind value:s:" + componentName);
        getDevice().executeShellCommand(
                "content delete"
                + " --uri content://settings/secure"
                + " --where \"name='accessibility_enabled'\"");
        getDevice().executeShellCommand(
                "content insert"
                + " --uri content://settings/secure"
                + " --bind name:s:accessibility_enabled"
                + " --bind value:i:1");
    }

    private void disableAccessibilityAndDelegatingService() throws DeviceNotAvailableException {
        getDevice().executeShellCommand(
                "content update"
                + " --uri content://settings/secure"
                + " --bind value:s:"
                + " --where \"name='enabled_accessibility_services'\"");
        getDevice().executeShellCommand(
                "content update"
                + " --uri content://settings/secure"
                + " --bind value:s:0"
                + " --where \"name='accessibility_enabled'\"");
    }
}

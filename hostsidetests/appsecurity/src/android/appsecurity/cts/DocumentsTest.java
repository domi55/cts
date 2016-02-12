/*
 * Copyright (C) 2014 The Android Open Source Project
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

package android.appsecurity.cts;

import com.android.cts.migration.MigrationHelper;
import com.android.tradefed.build.IBuildInfo;
import com.android.tradefed.device.DeviceNotAvailableException;
import com.android.tradefed.testtype.DeviceTestCase;
import com.android.tradefed.testtype.IAbi;
import com.android.tradefed.testtype.IAbiReceiver;
import com.android.tradefed.testtype.IBuildReceiver;

/**
 * Set of tests that verify behavior of
 * {@link android.provider.DocumentsContract} and related intents.
 */
public class DocumentsTest extends DeviceTestCase implements IAbiReceiver, IBuildReceiver {
    private static final String PROVIDER_PKG = "com.android.cts.documentprovider";
    private static final String PROVIDER_APK = "CtsDocumentProvider.apk";

    private static final String CLIENT_PKG = "com.android.cts.documentclient";
    private static final String CLIENT_APK = "CtsDocumentClient.apk";

    private IAbi mAbi;
    private IBuildInfo mCtsBuild;

    @Override
    public void setAbi(IAbi abi) {
        mAbi = abi;
    }

    @Override
    public void setBuild(IBuildInfo buildInfo) {
        mCtsBuild = buildInfo;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        assertNotNull(mAbi);
        assertNotNull(mCtsBuild);

        getDevice().uninstallPackage(PROVIDER_PKG);
        getDevice().uninstallPackage(CLIENT_PKG);

        assertNull(getDevice().installPackage(
                MigrationHelper.getTestFile(mCtsBuild, PROVIDER_APK), false));
        assertNull(getDevice().installPackage(
                MigrationHelper.getTestFile(mCtsBuild, CLIENT_APK), false));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        getDevice().uninstallPackage(PROVIDER_PKG);
        getDevice().uninstallPackage(CLIENT_PKG);
    }

    public void testOpenSimple() throws Exception {
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testOpenSimple");
    }

    public void testOpenVirtual() throws Exception {
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testOpenVirtual");
    }

    public void testCreateNew() throws Exception {
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testCreateNew");
    }

    public void testCreateExisting() throws Exception {
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testCreateExisting");
    }

    public void testTree() throws Exception {
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testTree");
    }

    public void testOpenExternalDirectory_invalidPath() throws Exception {
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testOpenExternalDirectory_invalidPath");
    }

    public void testOpenExternalDirectory_userRejects() throws Exception {
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testOpenExternalDirectory_userRejects");
    }

    public void testOpenExternalDirectory_userAccepts() throws Exception {
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testOpenExternalDirectory_userAccepts");
    }

    public void testOpenExternalDirectory_userAcceptsNewDirectory() throws Exception {
        // TODO: figure out a better way to remove the directory.
        final String command = "rm -rf /sdcard/Pictures";
        final String output = getDevice().executeShellCommand(command);
        if (!output.trim().isEmpty()) {
            fail("Command '" + command + "' failed: '" + output + "'");
        }
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testOpenExternalDirectory_userAccepts");
    }

    public void testGetContent() throws Exception {
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testGetContent");
    }

    public void testTransferDocument() throws Exception {
        runDeviceTests(CLIENT_PKG, ".DocumentsClientTest", "testTransferDocument");
    }

    public void runDeviceTests(String packageName, String testClassName, String testMethodName)
            throws DeviceNotAvailableException {
        Utils.runDeviceTests(getDevice(), packageName, testClassName, testMethodName);
    }
}

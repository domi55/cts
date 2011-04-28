/*
 * Copyright (C) 2011 The Android Open Source Project
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
import com.android.ddmlib.Log;
import com.android.ddmlib.testrunner.TestIdentifier;
import com.android.tradefed.device.DeviceNotAvailableException;
import com.android.tradefed.device.ITestDevice;
import com.android.tradefed.result.ITestInvocationListener;
import com.android.tradefed.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * A wrapper around {@link JarHostTest} that includes additional device setup and clean up.
 *
 */
public class VMHostTest extends JarHostTest {

    private static final String LOG_TAG = "VMHostTest";
    private static final String VM_TEST_TEMP_DIR = "/data/local/tmp/vm-tests";

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void run(ITestInvocationListener listener) throws DeviceNotAvailableException {
        if (!installVmPrereqs(getDevice(), getBuildHelper())) {
            throw new RuntimeException(String.format(
                    "Failed to install vm-tests prereqs on device %s",
                    getDevice().getSerialNumber()));
        }
        super.run(listener);
    }

    /**
     * Install pre-requisite jars for running vm-tests, creates temp directories for test.
     *
     * @param device the {@link ITestDevice}
     * @param ctsBuild the {@link CtsBuildHelper}
     * @throws DeviceNotAvailableException
     * @return true if test jar files are extracted and pushed to device successfully
     */
    private boolean installVmPrereqs(ITestDevice device, CtsBuildHelper ctsBuild)
            throws DeviceNotAvailableException {
        if (device.doesFileExist(VM_TEST_TEMP_DIR)) {
            Log.d(LOG_TAG, String.format("Removing device's temp dir %s from previous runs.",
                    VM_TEST_TEMP_DIR));
            device.executeShellCommand(String.format("rm -r %s", VM_TEST_TEMP_DIR));
        }
        // Creates temp directory recursively. We also need to create the dalvik-cache directory
        // which is used by the dalvikvm to optimize things. Without the dalvik-cache, there will be
        // a sigsev thrown by the vm.
        Log.d(LOG_TAG, "Creating device temp directory, including dalvik-cache.");
        createRemoteDir(device, VM_TEST_TEMP_DIR + "/dalvik-cache" );
        try {
            File localTmpDir = FileUtil.createTempDir("cts-vm", new File("/tmp/"));
            Log.d(LOG_TAG, String.format("Creating host temp dir %s", localTmpDir.getPath()));
            File jarFile = new File(ctsBuild.getTestCasesDir(), getJarFileName());
            if (!jarFile.exists()) {
                Log.e(LOG_TAG, String.format("Missing jar file %s", jarFile.getPath()));
                return false;
            }
            Log.d(LOG_TAG, String.format("Extracting jar file %s to host temp directory %s.",
                    jarFile.getPath(), localTmpDir.getPath()));
            ZipFile zipFile = new ZipFile(jarFile);
            FileUtil.extractZip(zipFile, localTmpDir);
            File localTestTmpDir = new File(localTmpDir, "tests/dot");
            Log.d(LOG_TAG, String.format("Syncing host dir %s to device dir %s",
                    localTestTmpDir.getPath(), VM_TEST_TEMP_DIR));
            device.syncFiles(localTestTmpDir, VM_TEST_TEMP_DIR);
            Log.d(LOG_TAG, String.format("Cleaning up host temp dir %s", localTmpDir.getPath()));
            FileUtil.recursiveDelete(localTmpDir);
        } catch (IOException e) {
            Log.e(LOG_TAG, String.format("Failed to extract jar file %s and sync it to device %s.",
                    getJarFileName(), device.getSerialNumber()));
            return false;
        }
        return true;
    }

    /**
     * Creates the file directory recursively in the device.
     *
     * @param device the {@link ITestDevice}
     * @param remoteFilePath the absolute path.
     * @throws DeviceNotAvailableException
     */
    private void createRemoteDir(ITestDevice device, String remoteFilePath)
            throws DeviceNotAvailableException {
        if (device.doesFileExist(remoteFilePath)) {
            return;
        }
        File f = new File(remoteFilePath);
        String parentPath = f.getParent();
        if (parentPath != null) {
            createRemoteDir(device, parentPath);
        }
        device.executeShellCommand(String.format("mkdir %s", remoteFilePath));
    }
}

/*
 * Copyright (C) 2017 The Android Open Source Project
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
package com.android.server.cts;

import com.android.tradefed.log.LogUtil;

/**
 * Test for "dumpsys batterystats -c
 *
 * Validates reporting of battery stats based on different events
 */
public class BatteryStatsValidationTest extends ProtoDumpTestCase {
    private static final String TAG = "BatteryStatsValidationTest";

    private static final String DEVICE_SIDE_TEST_APK = "CtsBatteryStatsApp.apk";
    private static final String DEVICE_SIDE_TEST_PACKAGE
            = "com.android.server.cts.device.batterystats";
    private static final String DEVICE_SIDE_BG_SERVICE_COMPONENT
            = "com.android.server.cts.device.batterystats/.BatteryStatsBackgroundService";
    private static final String DEVICE_SIDE_FG_ACTIVITY_COMPONENT
            = "com.android.server.cts.device.batterystats/.BatteryStatsForegroundActivity";
    private static final String DEVICE_SIDE_JOB_COMPONENT
            = "com.android.server.cts.device.batterystats/.SimpleJobService";
    private static final String DEVICE_SIDE_SYNC_COMPONENT
            = "com.android.server.cts.device.batterystats.provider/"
            + "com.android.server.cts.device.batterystats";

    // Low end of packet size. TODO: Get exact packet size
    private static final int LOW_MTU = 1500;
    // High end of packet size. TODO: Get exact packet size
    private static final int HIGH_MTU = 2500;

    // Constants from BatteryStatsBgVsFgActions.java (not directly accessible here).
    public static final String KEY_ACTION = "action";
    public static final String ACTION_BLE_SCAN = "action.ble_scan";
    public static final String ACTION_JOB_SCHEDULE = "action.jobs";
    public static final String ACTION_SYNC = "action.sync";
    public static final String ACTION_WIFI_SCAN = "action.wifi_scan";
    public static final String ACTION_WIFI_DOWNLOAD = "action.wifi_download";
    public static final String ACTION_WIFI_UPLOAD = "action.wifi_upload";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Uninstall to clear the history in case it's still on the device.
        getDevice().uninstallPackage(DEVICE_SIDE_TEST_PACKAGE);
    }

    /** Smallest possible HTTP header. */
    private static final int MIN_HTTP_HEADER_BYTES = 26;

    @Override
    protected void tearDown() throws Exception {
        getDevice().uninstallPackage(DEVICE_SIDE_TEST_PACKAGE);

        batteryOffScreenOn();
        super.tearDown();
    }

    protected void batteryOnScreenOff() throws Exception {
        getDevice().executeShellCommand("dumpsys battery unplug");
        getDevice().executeShellCommand("dumpsys batterystats enable pretend-screen-off");
    }

    protected void batteryOffScreenOn() throws Exception {
        getDevice().executeShellCommand("dumpsys battery reset");
        getDevice().executeShellCommand("dumpsys batterystats disable pretend-screen-off");
    }

    public void testAlarms() throws Exception {
        batteryOnScreenOff();

        installPackage(DEVICE_SIDE_TEST_APK, /* grantPermissions= */ true);

        runDeviceTests(DEVICE_SIDE_TEST_PACKAGE, ".BatteryStatsAlarmTest", "testAlarms");

        assertValueRange("wua", "*walarm*:com.android.server.cts.device.batterystats.ALARM",
                5, 3, 3);

        batteryOffScreenOn();
    }

    public void testWakeLockDuration() throws Exception {
        batteryOnScreenOff();

        installPackage(DEVICE_SIDE_TEST_APK, /* grantPermissions= */ true);

        runDeviceTests(DEVICE_SIDE_TEST_PACKAGE, ".BatteryStatsWakeLockTests",
                "testHoldShortWakeLock");

        runDeviceTests(DEVICE_SIDE_TEST_PACKAGE, ".BatteryStatsWakeLockTests",
                "testHoldLongWakeLock");

        assertValueRange("wl", "BSShortWakeLock", 15, (long) (500 * 0.9), 500 * 2); // partial max duration
        assertValueRange("wl", "BSLongWakeLock", 15, (long) (3000 * 0.9), 3000 * 2);  // partial max duration

        batteryOffScreenOn();
    }

    public void testServiceForegroundDuration() throws Exception {
        batteryOnScreenOff();
        installPackage(DEVICE_SIDE_TEST_APK, true);

        getDevice().executeShellCommand(
                "am start -n com.android.server.cts.device.batterystats/.SimpleActivity");
        assertValueRange("st", "", 5, 0, 0); // No foreground service time before test
        runDeviceTests(DEVICE_SIDE_TEST_PACKAGE, ".BatteryStatsProcessStateTests",
                "testForegroundService");
        assertValueRange("st", "", 5, (long) (2000 * 0.8), 4000);

        batteryOffScreenOn();
    }

    public void testBleScans() throws Exception {
        batteryOnScreenOff();
        installPackage(DEVICE_SIDE_TEST_APK, true);

        // Foreground test.
        executeForeground(ACTION_BLE_SCAN);
        Thread.sleep(2_500);
        assertValueRange("blem", "", 5, 1, 1); // ble_scan_count
        assertValueRange("blem", "", 6, 0, 0); // ble_scan_count_bg

        // Background test.
        executeBackground(ACTION_BLE_SCAN);
        Thread.sleep(2_500);
        assertValueRange("blem", "", 5, 2, 2); // ble_scan_count
        assertValueRange("blem", "", 6, 1, 1); // ble_scan_count_bg

        batteryOffScreenOn();
    }

    public void testJobBgVsFg() throws Exception {
        batteryOnScreenOff();
        installPackage(DEVICE_SIDE_TEST_APK, true);

        // Foreground test.
        executeForeground(ACTION_JOB_SCHEDULE);
        Thread.sleep(4_000);
        assertValueRange("jb", "", 6, 1, 1); // count
        assertValueRange("jb", "", 8, 0, 0); // background_count

        // Background test.
        executeBackground(ACTION_JOB_SCHEDULE);
        Thread.sleep(4_000);
        assertValueRange("jb", "", 6, 2, 2); // count
        assertValueRange("jb", "", 8, 1, 1); // background_count

        batteryOffScreenOn();
    }

    public void testSyncBgVsFg() throws Exception {
        batteryOnScreenOff();
        installPackage(DEVICE_SIDE_TEST_APK, true);

        // Foreground test.
        executeForeground(ACTION_SYNC);
        Thread.sleep(3_000);
        // Allow one or two syncs in this time frame (not just one) due to unpredictable syncs.
        assertValueRange("sy", DEVICE_SIDE_SYNC_COMPONENT, 6, 1, 2); // count
        assertValueRange("sy", DEVICE_SIDE_SYNC_COMPONENT, 8, 0, 0); // background_count

        // Background test.
        executeBackground(ACTION_SYNC);
        Thread.sleep(3_000);
        assertValueRange("sy", DEVICE_SIDE_SYNC_COMPONENT, 6, 2, 4); // count
        assertValueRange("sy", DEVICE_SIDE_SYNC_COMPONENT, 8, 1, 2); // background_count

        batteryOffScreenOn();
    }

    public void testWifiScans() throws Exception {
        batteryOnScreenOff();
        installPackage(DEVICE_SIDE_TEST_APK, true);

        // Foreground count test.
        executeForeground(ACTION_WIFI_SCAN);
        Thread.sleep(4_000);
        assertValueRange("wfl", "", 7, 1, 1); // scan_count
        assertValueRange("wfl", "", 11, 0, 0); // scan_count_bg

        // Background count test.
        executeBackground(ACTION_WIFI_SCAN);
        Thread.sleep(6_000);
        assertValueRange("wfl", "", 7, 2, 2); // scan_count
        assertValueRange("wfl", "", 11, 1, 1); // scan_count_bg

        batteryOffScreenOn();
    }

    /**
     * Tests whether the on-battery realtime and total realtime values
     * are properly updated in battery stats.
     */
    public void testRealtime() throws Exception {
        batteryOnScreenOff();
        long startingValueRealtime = getLongValue(0, "bt", "", 7);
        long startingValueBatteryRealtime = getLongValue(0, "bt", "", 5);
        // After going on battery
        Thread.sleep(2000);
        batteryOffScreenOn();
        // After going off battery
        Thread.sleep(2000);

        long currentValueRealtime = getLongValue(0, "bt", "", 7);
        long currentValueBatteryRealtime = getLongValue(0, "bt", "", 5);

        // Total realtime increase should be 4000ms at least
        assertTrue(currentValueRealtime >= startingValueRealtime + 4000);
        // But not too much more
        assertTrue(currentValueRealtime < startingValueRealtime + 6000);
        // Battery on realtime should be more than 2000 but less than 4000
        assertTrue(currentValueBatteryRealtime >= startingValueBatteryRealtime + 2000);
        assertTrue(currentValueBatteryRealtime < startingValueBatteryRealtime + 4000);
    }

    /**
     * Tests the total duration reported for jobs run on the job scheduler.
     */
    public void testJobDuration() throws Exception {
        batteryOnScreenOff();

        installPackage(DEVICE_SIDE_TEST_APK, true);

        runDeviceTests(DEVICE_SIDE_TEST_PACKAGE, ".BatteryStatsJobDurationTests",
                "testJobDuration");

        // Should be approximately 3000 ms. Use 0.8x and 2x as the lower and upper
        // bounds to account for possible errors due to thread scheduling and cpu load.
        assertValueRange("jb", DEVICE_SIDE_JOB_COMPONENT, 5, (long) (3000 * 0.8), 3000 * 2);
        batteryOffScreenOn();
    }

    /**
     * Tests the total duration and # of syncs reported for sync activities.
     */
    public void testSyncs() throws Exception {
        batteryOnScreenOff();

        installPackage(DEVICE_SIDE_TEST_APK, true);

        runDeviceTests(DEVICE_SIDE_TEST_PACKAGE, ".BatteryStatsSyncTest", "testRunSyncs");

        // First, check the count, which should be 10.
        // (It could be 11, if the initial sync actually happened before getting cancelled.)
        assertValueRange("sy", DEVICE_SIDE_SYNC_COMPONENT, 6, 10L, 11L);

        // Should be approximately, but at least 10 seconds. Use 2x as the upper
        // bounds to account for possible errors due to thread scheduling and cpu load.
        assertValueRange("sy", DEVICE_SIDE_SYNC_COMPONENT, 5, 10000, 10000 * 2);
    }

    /**
     * Tests the total bytes reported for downloading over wifi.
     */
    public void testWifiDownload() throws Exception {
        batteryOnScreenOff();
        installPackage(DEVICE_SIDE_TEST_APK, true);

        final long FUZZ = 50 * 1024;

        long prevBytes = getLongValue(getUid(), "nt", "", 6);

        executeForeground(ACTION_WIFI_DOWNLOAD);
        long downloadedBytes = getDownloadedBytes();
        assertTrue(downloadedBytes > 0);
        long min = prevBytes + downloadedBytes + MIN_HTTP_HEADER_BYTES;
        long max = prevBytes + downloadedBytes + FUZZ; // Add some fuzzing.
        assertValueRange("nt", "", 6, min, max); // wifi_bytes_rx
        assertValueRange("nt", "", 10, min / HIGH_MTU, max / LOW_MTU); // wifi_packets_rx

        // Do the background download
        long prevBgBytes = getLongValue(getUid(), "nt", "", 20);
        executeBackground(ACTION_WIFI_DOWNLOAD);
        Thread.sleep(4000);
        downloadedBytes = getDownloadedBytes();

        long minBg = prevBgBytes + downloadedBytes + MIN_HTTP_HEADER_BYTES;
        long maxBg = prevBgBytes + downloadedBytes + FUZZ;
        assertValueRange("nt", "", 20, minBg, maxBg); // wifi_bytes_bg_rx
        assertValueRange("nt", "", 24, minBg / HIGH_MTU, maxBg / LOW_MTU); // wifi_packets_bg_rx

        // Also increases total wifi counts.
        min += downloadedBytes + MIN_HTTP_HEADER_BYTES;
        max += downloadedBytes + FUZZ;
        assertValueRange("nt", "", 6, min, max); // wifi_bytes_rx
        assertValueRange("nt", "", 10, min / HIGH_MTU, max / LOW_MTU); // wifi_packets_rx

        batteryOffScreenOn();
    }

    /**
     * Tests the total bytes reported for uploading over wifi.
     */
    public void testWifiUpload() throws Exception {
        batteryOnScreenOff();
        installPackage(DEVICE_SIDE_TEST_APK, true);

        executeForeground(ACTION_WIFI_UPLOAD);
        Thread.sleep(2000);
        int min = MIN_HTTP_HEADER_BYTES + (2 * 1024);
        int max = min + (6 * 1024); // Add some fuzzing.
        assertValueRange("nt", "", 7, min, max); // wifi_bytes_tx

        executeBackground(ACTION_WIFI_UPLOAD);
        Thread.sleep(4000);
        assertValueRange("nt", "", 21, min * 2, max * 2); // wifi_bytes_bg_tx

        batteryOffScreenOn();
    }

    private int getUid() throws Exception {
        String uidLine = getDevice().executeShellCommand("cmd package list packages -U "
                + DEVICE_SIDE_TEST_PACKAGE);
        String[] uidLineParts = uidLine.split(":");
        // 3rd entry is package uid
        assertTrue(uidLineParts.length > 2);
        int uid = Integer.parseInt(uidLineParts[2].trim());
        assertTrue(uid > 10000);
        return uid;
    }

    /**
     * Verifies that the recorded time for the specified tag and name in the test package
     * is within the specified range.
     */
    private void assertValueRange(String tag, String optionalAfterTag,
            int index, long min, long max) throws Exception {
        int uid = getUid();
        long value = getLongValue(uid, tag, optionalAfterTag, index);

        assertTrue("Value " + value + " is less than min " + min, value >= min);
        assertTrue("Value " + value + " is greater than max " + max, value <= max);
    }

    /**
     * Returns a particular long value from a line matched by uid, tag and the optionalAfterTag.
     */
    private long getLongValue(int uid, String tag, String optionalAfterTag, int index)
            throws Exception {
        String dumpsys = getDevice().executeShellCommand("dumpsys batterystats --checkin");
        String[] lines = dumpsys.split("\n");
        long value = 0;
        if (optionalAfterTag == null) {
            optionalAfterTag = "";
        }
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i];
            if (line.contains(uid + ",l," + tag + "," + optionalAfterTag)
                    || (!optionalAfterTag.equals("") &&
                        line.contains(uid + ",l," + tag + ",\"" + optionalAfterTag))) {
                String[] wlParts = line.split(",");
                value = Long.parseLong(wlParts[index]);
            }
        }
        return value;
    }

    /**
     * Runs a (background) service to perform the given action.
     * @param actionValue one of the constants in BatteryStatsBgVsFgActions indicating the desired
     *                    action to perform.
     */
    private void executeBackground(String actionValue) throws Exception {
        allowBackgroundServices();
        getDevice().executeShellCommand(String.format(
                "am startservice -n '%s' -e %s %s",
                DEVICE_SIDE_BG_SERVICE_COMPONENT, KEY_ACTION, actionValue));
    }

    /** Required to successfully start a background service from adb in O. */
    private void allowBackgroundServices() throws Exception {
        getDevice().executeShellCommand(String.format(
                "cmd deviceidle tempwhitelist %s", DEVICE_SIDE_TEST_PACKAGE));
    }

    /**
     * Runs an activity (in the foreground) to perform the given action.
     * @param actionValue one of the constants in BatteryStatsBgVsFgActions indicating the desired
     *                    action to perform.
     */
    private void executeForeground(String actionValue) throws Exception {
        getDevice().executeShellCommand(String.format(
                "am start -n '%s' -e %s %s",
                DEVICE_SIDE_FG_ACTIVITY_COMPONENT, KEY_ACTION, actionValue));
    }

    /**
     * Returns the bytes downloaded for the wifi transfer download tests.
     */
    private long getDownloadedBytes() throws Exception {
        String log = getDevice().executeShellCommand(
                "logcat -d -s BatteryStatsWifiTransferTests -e '\\d+'");
        String[] lines = log.split("\n");
        long size = 0;
        for (int i = lines.length - 1; i >= 0; i--) {
            String[] parts = lines[i].split(":");
            String num = parts[parts.length - 1].trim();
            if (num.matches("\\d+")) {
                size = Integer.parseInt(num);
            }
        }
        return size;
    }
}

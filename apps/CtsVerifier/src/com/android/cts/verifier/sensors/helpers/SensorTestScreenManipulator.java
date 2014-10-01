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
 * limitations under the License
 */

package com.android.cts.verifier.sensors.helpers;

import com.android.cts.verifier.sensors.base.BaseSensorTestActivity;
import com.android.cts.verifier.sensors.base.ISensorTestStateContainer;

import android.app.Activity;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.text.TextUtils;

/**
 * A class that provides functionality to manipulate the state of the device's screen.
 *
 * The implementation uses a simple state machine with 3 states: keep-screen-off, keep-screen-on,
 * and a free-state where the class does not affect the system's state.
 *
 * The list of transitions and their handlers are:
 *      keep-screen-on --(turnScreenOff)--> keep-screen-off
 *      keep-screen-on --(releaseScreenOn)--> free-state
 *
 *      keep-screen-off --(turnScreenOn)--> keep-screen-on
 *      keep-screen-off --(wakeUpScreen)--> free-state
 *
 *      free-state --(turnScreenOff)--> keep-screen-off
 *      free-state --(turnScreenOn)--> keep-screen-on
 *
 * NOTES:
 * - the operator still can turn on/off the screen by pressing the power button
 * - this class must be used by a single client, that can manage the state of the instance, likely
 * - in a single-threaded environment
 */
public class SensorTestScreenManipulator {

    private final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private final ComponentName mComponentName;
    private final PowerManager.WakeLock mWakeUpScreenWakeLock;
    private final PowerManager.WakeLock mKeepScreenWakeLock;

    private InternalBroadcastReceiver mBroadcastReceiver;
    private boolean mTurnOffScreenOnPowerDisconnected;

    public SensorTestScreenManipulator(Context context) {
        mContext = context;
        mComponentName = SensorDeviceAdminReceiver.getComponentName(context);
        mDevicePolicyManager =
                (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        int levelAndFlags = PowerManager.FULL_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE
                | PowerManager.ACQUIRE_CAUSES_WAKEUP;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeUpScreenWakeLock = powerManager.newWakeLock(levelAndFlags, "SensorTestWakeUpScreen");
        mWakeUpScreenWakeLock.setReferenceCounted(false);
        mKeepScreenWakeLock = powerManager.newWakeLock(levelAndFlags, "SensorTestKeepScreenOn");
        mWakeUpScreenWakeLock.setReferenceCounted(false);
    }

    /**
     * Initializes the current instance.
     * Initialization should usually happen inside {@link BaseSensorTestActivity#activitySetUp}.
     *
     * NOTE: Initialization will bring up an Activity to let the user activate the Device Admin,
     * this method will block until the user completes the operation.
     */
    public synchronized void initialize(ISensorTestStateContainer stateContainer) {
        if (!isDeviceAdminInitialized()) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
            int resultCode = stateContainer.executeActivity(intent);
            if (resultCode != Activity.RESULT_OK) {
                throw new IllegalStateException(
                        "Test cannot execute without Activating the Device Administrator.");
            }
        }

        if (mBroadcastReceiver == null) {
            mBroadcastReceiver = new InternalBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            mContext.registerReceiver(mBroadcastReceiver, intentFilter);
        }
    }

    /**
     * Closes the current instance.
     * This operation should usually happen inside {@link BaseSensorTestActivity#activityCleanUp}.
     */
    public synchronized  void close() {
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }

    /**
     * Instruct the device to turn off the screen immediately.
     */
    public synchronized void turnScreenOff() {
        ensureDeviceAdminInitialized();
        releaseScreenOn();
        mDevicePolicyManager.lockNow();
    }

    /**
     * Instruct the device to wake up the screen immediately, the screen will remain on for a bit,
     * but the system might turn the screen off in the near future.
     */
    public synchronized void wakeUpScreen() {
        mWakeUpScreenWakeLock.acquire();
        // release right away, the screen still remains on for a bit, but not indefinitely
        mWakeUpScreenWakeLock.release();
    }

    /**
     * Instructs the device to turn on the screen immediately.
     *
     * The screen will remain on until the client invokes {@link #releaseScreenOn()}, or the user
     * presses the device's power button.
     */
    public synchronized void turnScreenOn() {
        if (mKeepScreenWakeLock.isHeld()) {
            // recover from cases when we could get out of sync, this can happen because the user
            // can press the power button, and other wake-locks can prevent intents to be received
            mKeepScreenWakeLock.release();
        }
        mKeepScreenWakeLock.acquire();
    }

    /**
     * Indicates that the client does not require the screen to remain on anymore.
     *
     * See {@link #turnScreenOn()} for more information.
     */
    public synchronized void releaseScreenOn() {
        if (!mKeepScreenWakeLock.isHeld()) {
            return;
        }
        mKeepScreenWakeLock.release();
    }

    /**
     * Queues a request to turn off the screen off when the device has been disconnected from a
     * power source (usually upon USB disconnected).
     *
     * (It is useful for Sensor Power Tests, as the Power Monitor usually detaches itself from the
     * device before beginning to sample data).
     */
    public synchronized void turnScreenOffOnNextPowerDisconnect() {
        ensureDeviceAdminInitialized();
        mTurnOffScreenOnPowerDisconnected = true;
    }

    private void ensureDeviceAdminInitialized() throws IllegalStateException {
        if (!isDeviceAdminInitialized()) {
            throw new IllegalStateException("Component must be initialized before it can be used.");
        }
    }

    private boolean isDeviceAdminInitialized() {
        if (!mDevicePolicyManager.isAdminActive(mComponentName)) {
            return false;
        }
        return mDevicePolicyManager
                .hasGrantedPolicy(mComponentName, DeviceAdminInfo.USES_POLICY_FORCE_LOCK);
    }

    private class InternalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (TextUtils.equals(action, Intent.ACTION_POWER_DISCONNECTED)) {
                if (mTurnOffScreenOnPowerDisconnected) {
                    turnScreenOff();
                    // reset the flag after it has triggered once, we try to avoid cases when the test
                    // might leave the receiver enabled after itself,
                    // this approach still provides a way to multiplex one time requests
                    mTurnOffScreenOnPowerDisconnected = false;
                }
            }
        }
    }
}

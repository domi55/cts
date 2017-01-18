/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.android.cts.verifier.managedprovisioning;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.BitmapFactory;
import android.net.ProxyInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;

import com.android.cts.verifier.R;
import com.android.cts.verifier.managedprovisioning.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandReceiverActivity extends Activity {
    private static final String TAG = "CommandReceiverActivity";

    public static final String ACTION_EXECUTE_COMMAND =
            "com.android.cts.verifier.managedprovisioning.action.EXECUTE_COMMAND";
    public static final String EXTRA_COMMAND =
            "com.android.cts.verifier.managedprovisioning.extra.COMMAND";

    public static final String COMMAND_SET_USER_RESTRICTION = "set-user_restriction";
    public static final String COMMAND_DISALLOW_KEYGUARD_UNREDACTED_NOTIFICATIONS =
            "disallow-keyguard-unredacted-notifications";
    public static final String COMMAND_SET_AUTO_TIME_REQUIRED = "set-auto-time-required";
    public static final String COMMAND_SET_GLOBAL_SETTING =
            "set-global-setting";
    public static final String COMMAND_SET_MAXIMUM_TO_LOCK = "set-maximum-time-to-lock";
    public static final String COMMAND_SET_PASSWORD_QUALITY = "set-password-quality";
    public static final String COMMAND_SET_KEYGUARD_DISABLED = "set-keyguard-disabled";
    public static final String COMMAND_SET_LOCK_SCREEN_INFO = "set-lock-screen-info";
    public static final String COMMAND_SET_STATUSBAR_DISABLED = "set-statusbar-disabled";
    public static final String COMMAND_ALLOW_ONLY_SYSTEM_INPUT_METHODS =
            "allow-only-system-input-methods";
    public static final String COMMAND_ALLOW_ONLY_SYSTEM_ACCESSIBILITY_SERVICES =
            "allow-only-system-accessibility-services";
    public static final String COMMAND_DEVICE_OWNER_CLEAR_POLICIES = "do-clear-policies";
    public static final String COMMAND_PROFILE_OWNER_CLEAR_POLICIES = "po-clear-policies";
    public static final String COMMAND_REMOVE_DEVICE_OWNER = "remove-device-owner";
    public static final String COMMAND_REQUEST_BUGREPORT = "request-bugreport";
    public static final String COMMAND_SET_USER_ICON = "set-user-icon";
    public static final String COMMAND_RETRIEVE_NETWORK_LOGS = "retrieve-network-logs";
    public static final String COMMAND_RETRIEVE_SECURITY_LOGS = "retrieve-security-logs";
    public static final String COMMAND_SET_ORGANIZATION_NAME = "set-organization-name";
    public static final String COMMAND_ENABLE_NETWORK_LOGGING = "enable-network-logging";
    public static final String COMMAND_DISABLE_NETWORK_LOGGING = "disable-network-logging";
    public static final String COMMAND_INSTALL_HELPER_PACKAGE = "install-helper-package";
    public static final String COMMAND_UNINSTALL_HELPER_PACKAGE = "uninstall-helper-package";
    public static final String COMMAND_CREATE_MANAGED_PROFILE = "create-managed-profile";
    public static final String COMMAND_REMOVE_MANAGED_PROFILE = "remove-managed-profile";
    public static final String COMMAND_SET_ALWAYS_ON_VPN = "set-always-on-vpn";
    public static final String COMMAND_CLEAR_ALWAYS_ON_VPN = "clear-always-on-vpn";
    public static final String COMMAND_SET_GLOBAL_HTTP_PROXY = "set-global-http-proxy";
    public static final String COMMAND_CLEAR_GLOBAL_HTTP_PROXY = "clear-global-http-proxy";

    public static final String EXTRA_USER_RESTRICTION =
            "com.android.cts.verifier.managedprovisioning.extra.USER_RESTRICTION";
    public static final String EXTRA_SETTING =
            "com.android.cts.verifier.managedprovisioning.extra.SETTING";
    // This extra can be used along with a command extra to set policy to
    // specify if that policy is enforced or not.
    public static final String EXTRA_ENFORCED =
            "com.android.cts.verifier.managedprovisioning.extra.ENFORCED";
    public static final String EXTRA_VALUE =
            "com.android.cts.verifier.managedprovisioning.extra.VALUE";
    public static final String EXTRA_ORGANIZATION_NAME =
            "com.android.cts.verifier.managedprovisioning.extra.ORGANIZATION_NAME";

    // We care about installing and uninstalling only. It does not matter what apk is used.
    // NotificationBot.apk is a good choice because it comes bundled with the CTS verifier.
    protected static final String HELPER_APP_LOCATION = "/sdcard/NotificationBot.apk";
    protected static final String HELPER_APP_PKG = "com.android.cts.robot";

    public static final String ACTION_INSTALL_COMPLETE =
            "com.android.cts.verifier.managedprovisioning.action.ACTION_INSTALL_COMPLETE";
    public static final String ACTION_UNINSTALL_COMPLETE =
            "com.android.cts.verifier.managedprovisioning.action.ACTION_UNINSTALL_COMPLETE";

    private ComponentName mAdmin;
    private DevicePolicyManager mDpm;
    private UserManager mUm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        try {
            mDpm = (DevicePolicyManager) getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
            mUm = (UserManager) getSystemService(Context.USER_SERVICE);
            mAdmin = DeviceAdminTestReceiver.getReceiverComponentName();
            Log.i(TAG, "Command: " + intent);

            final String command = getIntent().getStringExtra(EXTRA_COMMAND);
            switch (command) {
                case COMMAND_SET_USER_RESTRICTION: {
                    String restrictionKey = intent.getStringExtra(EXTRA_USER_RESTRICTION);
                    boolean enforced = intent.getBooleanExtra(EXTRA_ENFORCED, false);
                    if (enforced) {
                        mDpm.addUserRestriction(mAdmin, restrictionKey);
                    } else {
                        mDpm.clearUserRestriction(mAdmin, restrictionKey);
                    }
                } break;
                case COMMAND_DISALLOW_KEYGUARD_UNREDACTED_NOTIFICATIONS: {
                    boolean enforced = intent.getBooleanExtra(EXTRA_ENFORCED, false);
                    mDpm.setKeyguardDisabledFeatures(mAdmin, enforced
                            ? DevicePolicyManager.KEYGUARD_DISABLE_UNREDACTED_NOTIFICATIONS
                            : 0);
                } break;
                case COMMAND_SET_AUTO_TIME_REQUIRED: {
                    mDpm.setAutoTimeRequired(mAdmin,
                            intent.getBooleanExtra(EXTRA_ENFORCED, false));
                }
                case COMMAND_SET_LOCK_SCREEN_INFO: {
                    mDpm.setDeviceOwnerLockScreenInfo(mAdmin, intent.getStringExtra(EXTRA_VALUE));
                }
                case COMMAND_SET_MAXIMUM_TO_LOCK: {
                    final long timeInSeconds = Long.parseLong(intent.getStringExtra(EXTRA_VALUE));
                    mDpm.setMaximumTimeToLock(mAdmin,
                            TimeUnit.SECONDS.toMillis(timeInSeconds) /* in milliseconds */);
                } break;
                case COMMAND_SET_PASSWORD_QUALITY: {
                    int quality = intent.getIntExtra(EXTRA_VALUE, 0);
                    mDpm.setPasswordQuality(mAdmin, quality);
                } break;
                case COMMAND_SET_KEYGUARD_DISABLED: {
                    boolean enforced = intent.getBooleanExtra(EXTRA_ENFORCED, false);
                    if (enforced) {
                        mDpm.resetPassword(null, 0);
                    }
                    mDpm.setKeyguardDisabled(mAdmin, enforced);
                } break;
                case COMMAND_SET_STATUSBAR_DISABLED: {
                    boolean enforced = intent.getBooleanExtra(EXTRA_ENFORCED, false);
                    mDpm.setStatusBarDisabled(mAdmin, enforced);
                } break;
                case COMMAND_ALLOW_ONLY_SYSTEM_INPUT_METHODS: {
                    boolean enforced = intent.getBooleanExtra(EXTRA_ENFORCED, false);
                    mDpm.setPermittedInputMethods(mAdmin, enforced ? new ArrayList() : null);
                } break;
                case COMMAND_ALLOW_ONLY_SYSTEM_ACCESSIBILITY_SERVICES: {
                    boolean enforced = intent.getBooleanExtra(EXTRA_ENFORCED, false);
                    mDpm.setPermittedAccessibilityServices(mAdmin,
                            enforced ? new ArrayList() : null);
                } break;
                case COMMAND_SET_GLOBAL_SETTING: {
                    final String setting = intent.getStringExtra(EXTRA_SETTING);
                    final String value = intent.getStringExtra(EXTRA_VALUE);
                    mDpm.setGlobalSetting(mAdmin, setting, value);
                } break;
                case COMMAND_REMOVE_DEVICE_OWNER: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    clearAllPolicies();
                    mDpm.clearDeviceOwnerApp(getPackageName());
                } break;
                case COMMAND_REQUEST_BUGREPORT: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    final boolean bugreportStarted = mDpm.requestBugreport(mAdmin);
                    if (!bugreportStarted) {
                        Utils.showBugreportNotification(this, getString(
                                R.string.bugreport_already_in_progress),
                                Utils.BUGREPORT_NOTIFICATION_ID);
                    }
                } break;
                case COMMAND_DEVICE_OWNER_CLEAR_POLICIES: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    clearAllPolicies();
                } break;
                case COMMAND_PROFILE_OWNER_CLEAR_POLICIES: {
                    if (!mDpm.isProfileOwnerApp(getPackageName())) {
                        return;
                    }
                    clearProfileOwnerRelatedPolicies();
                } break;
                case COMMAND_SET_USER_ICON: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    mDpm.setUserIcon(mAdmin, BitmapFactory.decodeResource(getResources(),
                            com.android.cts.verifier.R.drawable.icon));
                } break;
                case COMMAND_RETRIEVE_NETWORK_LOGS: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    // STOPSHIP(b/33068581): Network logging will be un-hidden for O. Remove
                    // reflection when the un-hiding happens.
                    final Method setNetworkLoggingEnabledMethod =
                            DevicePolicyManager.class.getDeclaredMethod(
                                    "setNetworkLoggingEnabled", ComponentName.class, boolean.class);
                    final Method retrieveNetworkLogsMethod =
                            DevicePolicyManager.class.getDeclaredMethod(
                                    "retrieveNetworkLogs", ComponentName.class, long.class);
                    setNetworkLoggingEnabledMethod.invoke(mDpm, mAdmin, true);
                    retrieveNetworkLogsMethod.invoke(mDpm, mAdmin, 0 /* batchToken */);
                    setNetworkLoggingEnabledMethod.invoke(mDpm, mAdmin, false);
                } break;
                case COMMAND_RETRIEVE_SECURITY_LOGS: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    mDpm.retrieveSecurityLogs(mAdmin);
                } break;
                case COMMAND_SET_ORGANIZATION_NAME: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    mDpm.setOrganizationName(mAdmin,
                            intent.getStringExtra(EXTRA_ORGANIZATION_NAME));
                } break;
                case COMMAND_ENABLE_NETWORK_LOGGING: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    mDpm.setNetworkLoggingEnabled(mAdmin, true);
                } break;
                case COMMAND_DISABLE_NETWORK_LOGGING: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    mDpm.setNetworkLoggingEnabled(mAdmin, false);
                } break;
                case COMMAND_INSTALL_HELPER_PACKAGE: {
                    installHelperPackage();
                } break;
                case COMMAND_UNINSTALL_HELPER_PACKAGE: {
                    getPackageManager().getPackageInstaller().uninstall(HELPER_APP_PKG,
                            PendingIntent.getBroadcast(this, 0,
                                    new Intent(ACTION_UNINSTALL_COMPLETE), 0).getIntentSender());
                } break;
                case COMMAND_CREATE_MANAGED_PROFILE: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    if (mUm.getUserProfiles().size() > 1) {
                        return;
                    }
                    startActivityForResult(new Intent(
                            DevicePolicyManager.ACTION_PROVISION_MANAGED_PROFILE)
                            .putExtra(DevicePolicyManager
                                    .EXTRA_PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME,
                                    CompDeviceAdminTestReceiver.getReceiverComponentName())
                            .putExtra(DevicePolicyManager.EXTRA_PROVISIONING_SKIP_ENCRYPTION, true)
                            .putExtra(DevicePolicyManager.EXTRA_PROVISIONING_SKIP_USER_CONSENT,
                                true), 0);
                } break;
                case COMMAND_REMOVE_MANAGED_PROFILE: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    removeManagedProfile();
                } break;
                case COMMAND_SET_ALWAYS_ON_VPN: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    mDpm.setAlwaysOnVpnPackage(mAdmin, getPackageName(),
                            false /* lockdownEnabled */);
                } break;
                case COMMAND_CLEAR_ALWAYS_ON_VPN: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    mDpm.setAlwaysOnVpnPackage(mAdmin, null /* vpnPackage */,
                            false /* lockdownEnabled */);
                } break;
                case COMMAND_SET_GLOBAL_HTTP_PROXY: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    mDpm.setRecommendedGlobalProxy(mAdmin,
                            ProxyInfo.buildDirectProxy("example.com", 123));
                } break;
                case COMMAND_CLEAR_GLOBAL_HTTP_PROXY: {
                    if (!mDpm.isDeviceOwnerApp(getPackageName())) {
                        return;
                    }
                    mDpm.setRecommendedGlobalProxy(mAdmin, null);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to execute command: " + intent, e);
        } finally {
            finish();
        }
    }

    private void installHelperPackage() throws Exception {
        final PackageInstaller packageInstaller = getPackageManager().getPackageInstaller();
        final PackageInstaller.Session session = packageInstaller.openSession(
                packageInstaller.createSession(new PackageInstaller.SessionParams(
                        PackageInstaller.SessionParams.MODE_FULL_INSTALL)));
        final File file = new File(HELPER_APP_LOCATION);
        final InputStream in = new FileInputStream(file);
        final OutputStream out = session.openWrite("CommandReceiverActivity", 0, file.length());
        final byte[] buffer = new byte[65536];
        int count;
        while ((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
        }
        session.fsync(out);
        in.close();
        out.close();
        session.commit(PendingIntent.getBroadcast(this, 0, new Intent(ACTION_INSTALL_COMPLETE), 0)
                .getIntentSender());
    }

    private void clearAllPolicies() throws Exception {
        clearProfileOwnerRelatedPolicies();

        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_ADD_USER);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_ADJUST_VOLUME);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_CONFIG_BLUETOOTH);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_CONFIG_CELL_BROADCASTS);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_CONFIG_MOBILE_NETWORKS);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_CONFIG_TETHERING);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_CONFIG_VPN);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_CONFIG_WIFI);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_DATA_ROAMING);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_DEBUGGING_FEATURES);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_FACTORY_RESET);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_FUN);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_INSTALL_UNKNOWN_SOURCES);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_NETWORK_RESET);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_OUTGOING_BEAM);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_REMOVE_USER);

        mDpm.setDeviceOwnerLockScreenInfo(mAdmin, null);
        mDpm.setKeyguardDisabled(mAdmin, false);
        mDpm.setAutoTimeRequired(mAdmin, false);
        mDpm.setStatusBarDisabled(mAdmin, false);
        // STOPSHIP(b/33068581): Network logging will be un-hidden for O. Remove reflection when the
        // un-hiding happens.
        final Method setNetworkLoggingEnabledMethod = DevicePolicyManager.class.getDeclaredMethod(
                "setNetworkLoggingEnabled", ComponentName.class, boolean.class);
        setNetworkLoggingEnabledMethod.invoke(mDpm, mAdmin, false);
        mDpm.setOrganizationName(mAdmin, null);
        mDpm.setRecommendedGlobalProxy(mAdmin, null);
    }

    private void clearProfileOwnerRelatedPolicies() {
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_APPS_CONTROL);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_CONFIG_CREDENTIALS);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_MODIFY_ACCOUNTS);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_SHARE_LOCATION);
        mDpm.clearUserRestriction(mAdmin, UserManager.DISALLOW_UNINSTALL_APPS);

        mDpm.setKeyguardDisabledFeatures(mAdmin, 0);
        mDpm.setPasswordQuality(mAdmin, 0);
        mDpm.setMaximumTimeToLock(mAdmin, 0);
        mDpm.setPermittedAccessibilityServices(mAdmin, null);
        mDpm.setPermittedInputMethods(mAdmin, null);
    }

    private void removeManagedProfile() {
        for (final UserHandle userHandle : mUm.getUserProfiles()) {
            mDpm.removeUser(mAdmin, userHandle);
        }
    }
}

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

package com.android.cts.deviceowner;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Test {@link DevicePolicyManager#getForceEphemeralUser} and
 * {@link DevicePolicyManager#setForceEphemeralUser}.
 *
 * <p>The test toggles force-ephemeral-user policy on and leaves it that way which enables
 * combining it with additional host-side tests.
 */
public class ForceEphemeralUsersTest extends BaseDeviceOwnerTest {

    /**
     * Test setting and subsequently getting the force-ephemeral-user policy.
     */
    public void testSetForceEphemeralUsers() throws Exception {
        Method setForceEphemeralUsersMethod = DevicePolicyManager.class.getDeclaredMethod(
                "setForceEphemeralUsers", ComponentName.class, boolean.class);
        setForceEphemeralUsersMethod.invoke(mDevicePolicyManager, getWho(), true);

        Method getForceEphemeralUsersMethod = DevicePolicyManager.class.getDeclaredMethod(
                "getForceEphemeralUsers", ComponentName.class);
        assertTrue((boolean) getForceEphemeralUsersMethod.invoke(mDevicePolicyManager, getWho()));
    }

    /**
     * Setting force-ephemeral-user policy should fail if not on system with split system user.
     *
     * <p>To be run on systems without split system user.
     */
    public void testSetForceEphemeralUsersFails() throws Exception {
        Method setForceEphemeralUsersMethod = DevicePolicyManager.class.getDeclaredMethod(
                "setForceEphemeralUsers", ComponentName.class, boolean.class);
        try {
            setForceEphemeralUsersMethod.invoke(mDevicePolicyManager, getWho(), true);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof UnsupportedOperationException) {
                // Test passed, the exception was thrown as expected.
                return;
            }
        }
        fail("UnsupportedOperationException should have been thrown by setForceEphemeralUsers");
    }

}

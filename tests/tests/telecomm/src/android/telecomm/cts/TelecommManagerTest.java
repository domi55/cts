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

package android.telecomm.cts;

import android.content.ComponentName;
import android.net.Uri;
import android.telecomm.PhoneAccount;
import android.telecomm.PhoneAccountHandle;
import android.telecomm.PhoneCapabilities;
import android.telecomm.TelecommManager;
import android.test.AndroidTestCase;

import java.util.List;

public class TelecommManagerTest extends AndroidTestCase {
    public void testRegisterAccountsBlocked() {
        PhoneAccount phoneAccount = new PhoneAccount.Builder(
                new PhoneAccountHandle(
                        new ComponentName(getContext(), TelecommManagerTest.class),
                        "testRegisterAccountsBlocked"),
                "Mock PhoneAccount")
                .setAddress(Uri.parse("tel:6502637643"))
                .setSubscriptionAddress(Uri.parse("tel:650-263-7643"))
                .setCapabilities(PhoneCapabilities.ALL)
                .setIconResId(0)
                .setShortDescription("PhoneAccount used in TelecommManagerTest")
                .build();

        TelecommManager tm = TelecommManager.from(getContext());
        List<PhoneAccountHandle> handles = tm.getEnabledPhoneAccounts();

        try {
            tm.registerPhoneAccount(phoneAccount);
            fail("This should have failed (CTS can't get the permission)");
        } catch (SecurityException e) {
            assertEquals(handles, tm.getEnabledPhoneAccounts());
        }
    }
}

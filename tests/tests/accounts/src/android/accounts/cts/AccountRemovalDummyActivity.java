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

package android.accounts.cts;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * An activity.
 */
public class AccountRemovalDummyActivity extends Activity {

    public static Intent createIntent(Context context) {
        return new Intent(context, AccountRemovalDummyActivity.class);
    }

    private AccountAuthenticatorResponse mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle input = (savedInstanceState == null) ? getIntent().getExtras() : savedInstanceState;
        mResponse = input
                .getParcelable(MockAccountAuthenticator.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        // Do verification and then remove the account
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
        mResponse.onResult(result);
        finish();
    }
}

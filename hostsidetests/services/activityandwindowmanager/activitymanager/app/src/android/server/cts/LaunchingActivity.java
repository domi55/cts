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
 * limitations under the License
 */

package android.server.cts;

import static android.content.Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT;
import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;

/**
 * Activity that launches another activities when new intent is received.
 */
public class LaunchingActivity extends Activity {
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }

        Intent newIntent = new Intent();
        String targetActivity = extras.getString("target_activity");
        if (targetActivity != null) {
            String packageName = getApplicationContext().getPackageName();
            newIntent.setComponent(new ComponentName(packageName,
                    packageName + "." + targetActivity));
        } else {
            newIntent.setClass(this, TestActivity.class);
        }

        if (extras.getBoolean("launch_to_the_side")) {
            newIntent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_LAUNCH_ADJACENT);
            if (extras.getBoolean("multiple_task")) {
                newIntent.addFlags(FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            if (extras.getBoolean("random_data")) {
                Uri data = new Uri.Builder()
                        .path(String.valueOf(System.currentTimeMillis()))
                        .build();
                newIntent.setData(data);
            }
        }

        ActivityOptions options = null;
        final int displayId = extras.getInt("display_id", -1);
        if (displayId != -1) {
            options = ActivityOptions.makeBasic();
            options.setLaunchDisplayId(displayId);
            newIntent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_MULTIPLE_TASK);
        }

        startActivity(newIntent, options != null ? options.toBundle() : null);
    }
}

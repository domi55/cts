/*
 * Copyright (C) 2008 The Android Open Source Project
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

package android.app.stubs;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;

public class MockService extends Service {
    public static boolean result = false;
    private final IBinder mBinder = new MockBinder();

    private static boolean sStarted = false;
    private static Object sBlocker = new Object();

    public class MockBinder extends Binder {
        MockService getService() {
            return MockService.this;
        }
    }

    /**
     * set the result as true when service bind
     */
    @Override
    public IBinder onBind(Intent intent) {
        synchronized (sBlocker) {
            result = true;
            sStarted = true;
            sBlocker.notifyAll();
        }
        return mBinder;
    }

    /**
     * set the result as true when service start
     */
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        synchronized (sBlocker) {
            stopSelf(startId);
            result = true;
            sStarted = true;
            sBlocker.notifyAll();
        }
    }

    public static void prepareStart() {
        synchronized (sBlocker) {
            sStarted = false;
            result = false;
        }
    }

    public static boolean waitForStart(long timeout) {
        long now = SystemClock.elapsedRealtime();
        final long endTime = now + timeout;
        synchronized (sBlocker) {
            while (!sStarted && now < endTime) {
                try {
                    sBlocker.wait(endTime - now);
                } catch (InterruptedException e) {
                }
                now = SystemClock.elapsedRealtime();
            }
            return sStarted;
        }
    }
}


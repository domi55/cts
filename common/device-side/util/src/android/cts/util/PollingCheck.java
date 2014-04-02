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

package android.cts.util;

import java.util.concurrent.Callable;

import junit.framework.Assert;

public abstract class PollingCheck {
    private static final long TIME_SLICE = 50;
    private long mTimeoutMs = 3000;

    public PollingCheck() {
    }

    public PollingCheck(long timeoutMs) {
        mTimeoutMs = timeoutMs;
    }

    protected abstract boolean check();

    public void run() {
        if (check()) {
            return;
        }

        long timeoutMs = mTimeoutMs;
        while (timeoutMs > 0) {
            try {
                Thread.sleep(TIME_SLICE);
            } catch (InterruptedException e) {
                Assert.fail("unexpected InterruptedException");
            }

            if (check()) {
                return;
            }

            timeoutMs -= TIME_SLICE;
        }

        Assert.fail("unexpected timeout");
    }

    public static void check(CharSequence message, long timeoutMs, Callable<Boolean> condition)
            throws Exception {
        while (timeoutMs > 0) {
            if (condition.call()) {
                return;
            }

            Thread.sleep(TIME_SLICE);
            timeoutMs -= TIME_SLICE;
        }

        Assert.fail(message.toString());
    }
}

/*
 * Copyright (C) 2012 The Android Open Source Project
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

package android.view.cts;

import android.test.AndroidTestCase;
import android.view.Choreographer;

public class ChoreographerTest extends AndroidTestCase {
    private static final long NOMINAL_VSYNC_PERIOD = 16;
    private static final long DELAY_PERIOD = NOMINAL_VSYNC_PERIOD * 5;
    private static final Object TOKEN = new Object();

    private Choreographer mChoreographer = Choreographer.getInstance();

    public void testFrameDelay() {
        assertTrue(Choreographer.getFrameDelay() > 0);

        long oldFrameDelay = Choreographer.getFrameDelay();
        long newFrameDelay = oldFrameDelay * 2;
        Choreographer.setFrameDelay(newFrameDelay);
        assertEquals(newFrameDelay, Choreographer.getFrameDelay());

        Choreographer.setFrameDelay(oldFrameDelay);
    }

    public void testPostCallbackWithoutDelayEventuallyRunsCallbacks() {
        MockRunnable addedCallback1 = new MockRunnable();
        MockRunnable addedCallback2 = new MockRunnable();
        MockRunnable removedCallback = new MockRunnable();
        try {
            // Add and remove a few callbacks.
            mChoreographer.postCallback(
                    Choreographer.CALLBACK_ANIMATION, addedCallback1, null);
            mChoreographer.postCallbackDelayed(
                    Choreographer.CALLBACK_ANIMATION, addedCallback2, null, 0);
            mChoreographer.postCallback(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, null);
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, null);

            // Sleep for a couple of frames.
            sleep(NOMINAL_VSYNC_PERIOD * 3);

            // We expect the remaining callbacks to have been invoked once.
            assertEquals(1, addedCallback1.invocationCount);
            assertEquals(1, addedCallback2.invocationCount);
            assertEquals(0, removedCallback.invocationCount);

            // If we post a callback again, then it should be invoked again.
            mChoreographer.postCallback(
                    Choreographer.CALLBACK_ANIMATION, addedCallback1, null);
            sleep(NOMINAL_VSYNC_PERIOD * 3);

            assertEquals(2, addedCallback1.invocationCount);
            assertEquals(1, addedCallback2.invocationCount);
            assertEquals(0, removedCallback.invocationCount);

            // If the token matches, the the callback should be removed.
            mChoreographer.postCallback(
                    Choreographer.CALLBACK_ANIMATION, addedCallback1, null);
            mChoreographer.postCallback(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, TOKEN);
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, null, TOKEN);
            sleep(NOMINAL_VSYNC_PERIOD * 3);
            assertEquals(3, addedCallback1.invocationCount);
            assertEquals(0, removedCallback.invocationCount);

            // If the action and token matches, then the callback should be removed.
            // If only the token matches, then the callback should not be removed.
            mChoreographer.postCallback(
                    Choreographer.CALLBACK_ANIMATION, addedCallback1, TOKEN);
            mChoreographer.postCallback(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, TOKEN);
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, TOKEN);
            sleep(NOMINAL_VSYNC_PERIOD * 3);
            assertEquals(4, addedCallback1.invocationCount);
            assertEquals(0, removedCallback.invocationCount);
        } finally {
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, addedCallback1, null);
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, addedCallback2, null);
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, null);
        }
    }

    public void testPostCallbackWithDelayEventuallyRunsCallbacksAfterDelay() {
        MockRunnable addedCallback = new MockRunnable();
        MockRunnable removedCallback = new MockRunnable();
        try {
            // Add and remove a few callbacks.
            mChoreographer.postCallbackDelayed(
                    Choreographer.CALLBACK_ANIMATION, addedCallback, null, DELAY_PERIOD);
            mChoreographer.postCallbackDelayed(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, null, DELAY_PERIOD);
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, null);

            // Sleep for a couple of frames.
            sleep(NOMINAL_VSYNC_PERIOD * 3);

            // The callbacks should not have been invoked yet because of the delay.
            assertEquals(0, addedCallback.invocationCount);
            assertEquals(0, removedCallback.invocationCount);

            // Sleep for the rest of the delay time.
            sleep(DELAY_PERIOD);

            // We expect the remaining callbacks to have been invoked.
            assertEquals(1, addedCallback.invocationCount);
            assertEquals(0, removedCallback.invocationCount);

            // If the token matches, the the callback should be removed.
            mChoreographer.postCallbackDelayed(
                    Choreographer.CALLBACK_ANIMATION, addedCallback, null, DELAY_PERIOD);
            mChoreographer.postCallbackDelayed(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, TOKEN, DELAY_PERIOD);
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, null, TOKEN);
            sleep(NOMINAL_VSYNC_PERIOD * 3 + DELAY_PERIOD);
            assertEquals(2, addedCallback.invocationCount);
            assertEquals(0, removedCallback.invocationCount);

            // If the action and token matches, then the callback should be removed.
            // If only the token matches, then the callback should not be removed.
            mChoreographer.postCallbackDelayed(
                    Choreographer.CALLBACK_ANIMATION, addedCallback, TOKEN, DELAY_PERIOD);
            mChoreographer.postCallbackDelayed(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, TOKEN, DELAY_PERIOD);
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, TOKEN);
            sleep(NOMINAL_VSYNC_PERIOD * 3 + DELAY_PERIOD);
            assertEquals(3, addedCallback.invocationCount);
            assertEquals(0, removedCallback.invocationCount);
        } finally {
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, addedCallback, null);
            mChoreographer.removeCallbacks(
                    Choreographer.CALLBACK_ANIMATION, removedCallback, null);
        }
    }

    public void testPostCallbackThrowsIfRunnableIsNull() {
        try {
            mChoreographer.postCallback(
                    Choreographer.CALLBACK_ANIMATION, null, TOKEN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testPostCallbackDelayedThrowsIfRunnableIsNull() {
        try {
            mChoreographer.postCallbackDelayed(
                    Choreographer.CALLBACK_ANIMATION, null, TOKEN, DELAY_PERIOD);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }

    private static final class MockRunnable implements Runnable {
        public int invocationCount;

        @Override
        public void run() {
            invocationCount += 1;
        }
    }
}

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
package android.animation.cts;

import android.animation.AnimatorListenerAdapter;
import android.support.test.filters.SmallTest;

import org.junit.Test;

@SmallTest
public class AnimatorListenerAdapterTest {
    /**
     * AnimatorListenerAdapter has a noop implementation of the AnimatorListener interface.
     * It should do nothing, including when nulls are passed to it.
     * <p>
     * Mostly this test pokes the implementation so that it is counted as tested. There isn't
     * much to test here since it has no implementation.
     */
    @Test
    public void testNullOk() throws Throwable {
        AnimatorListenerAdapter adapter = new MyAdapter();
        adapter.onAnimationStart(null);
        adapter.onAnimationEnd(null);
        adapter.onAnimationRepeat(null);
        adapter.onAnimationCancel(null);
        adapter.onAnimationPause(null);
        adapter.onAnimationResume(null);
    }

    private static class MyAdapter extends AnimatorListenerAdapter {
    }
}

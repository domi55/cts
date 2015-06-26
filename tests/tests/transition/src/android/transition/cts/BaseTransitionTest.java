/*
 * Copyright (C) 2015 The Android Open Source Project
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
package android.transition.cts;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.test.ActivityInstrumentationTestCase2;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import com.android.cts.transition.R;

public class BaseTransitionTest extends ActivityInstrumentationTestCase2<TransitionActivity> {
    protected TransitionActivity mActivity;
    protected FrameLayout mSceneRoot;
    public float mAnimatedValue;
    protected ArrayList<View> mTargets = new ArrayList<>();
    protected TestTransition mTransition;

    public BaseTransitionTest() {
        super(TransitionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        mActivity = getActivity();
        mSceneRoot = (FrameLayout) mActivity.findViewById(R.id.container);
        mTargets.clear();
        mTransition = new TestTransition();
    }

    protected void waitForStart() throws InterruptedException {
        waitForStart(mTransition.listener);
    }

    protected static void waitForStart(SimpleTransitionListener listener) throws InterruptedException {
        long endTime = System.currentTimeMillis() + 50;
        synchronized (listener) {
            while (!listener.started) {
                long now = System.currentTimeMillis();
                long waitTime = endTime - now;
                if (waitTime <= 0) {
                    throw new InterruptedException();
                }
                listener.wait(waitTime);
            }
        }
    }

    protected void waitForEnd(long waitMillis) throws InterruptedException {
        waitForEnd(mTransition.listener, waitMillis);
    }

    protected static void waitForEnd(SimpleTransitionListener listener, long waitMillis)
            throws InterruptedException {
        long endTime = System.currentTimeMillis() + waitMillis;
        synchronized (listener) {
            while (!listener.ended) {
                long now = System.currentTimeMillis();
                long waitTime = endTime - now;
                if (waitTime <= 0) {
                    throw new InterruptedException();
                }
                listener.wait(waitTime);
            }
        }
    }

    protected void startTransition(final int layoutId) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Scene scene = Scene.getSceneForLayout(mSceneRoot, layoutId, mActivity);
                TransitionManager.go(scene, mTransition);
            }
        });
        waitForStart();
    }

    protected void endTransition() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                TransitionManager.endTransitions(mSceneRoot);
            }
        });
    }

    protected void enterScene(final int layoutId) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Scene scene = Scene.getSceneForLayout(mSceneRoot, layoutId, mActivity);
                scene.enter();
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public class TestTransition extends Visibility {
        public final SimpleTransitionListener listener = new SimpleTransitionListener();

        public TestTransition() {
            addListener(listener);
            setDuration(100);
        }

        @Override
        public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues,
                TransitionValues endValues) {
            mTargets.add(endValues.view);
            return ObjectAnimator.ofFloat(BaseTransitionTest.this, "mAnimatedValue", 0, 1);
        }

        @Override
        public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues,
                TransitionValues endValues) {
            mTargets.add(startValues.view);
            return ObjectAnimator.ofFloat(BaseTransitionTest.this, "mAnimatedValue", 1, 0);
        }
    }
}

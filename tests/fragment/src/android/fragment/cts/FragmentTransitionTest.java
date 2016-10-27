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
package android.fragment.cts;

import static junit.framework.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Instrumentation;
import android.app.SharedElementCallback;
import android.cts.util.transition.TargetTracking;
import android.cts.util.transition.TrackingTransition;
import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.transition.TransitionSet;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@MediumTest
@RunWith(Parameterized.class)
public class FragmentTransitionTest {
    private final boolean mOptimize;

    @Parameterized.Parameters
    public static Object[] data() {
        return new Boolean[] {
                false, true
        };
    }

    @Rule
    public ActivityTestRule<FragmentTestActivity> mActivityRule =
            new ActivityTestRule<FragmentTestActivity>(FragmentTestActivity.class);

    private Instrumentation mInstrumentation;
    private FragmentManager mFragmentManager;

    public FragmentTransitionTest(final boolean optimize) {
        mOptimize = optimize;
    }

    @Before
    public void setup() throws Throwable {
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mFragmentManager = mActivityRule.getActivity().getFragmentManager();
        FragmentTestUtil.setContentView(mActivityRule, R.layout.simple_container);
    }

    // Test that normal view transitions (enter, exit, reenter, return) run with
    // a single fragment.
    @Test
    public void enterExitTransitions() throws Throwable {
        // enter transition
        TransitionFragment fragment = setupInitialFragment();
        final View blue = findBlue();
        final View green = findBlue();

        // exit transition
        mFragmentManager.beginTransaction()
                .setAllowOptimization(mOptimize)
                .remove(fragment)
                .addToBackStack(null)
                .commit();

        fragment.waitForTransition();
        verifyAndClearTransition(fragment.exitTransition, null, green, blue);
        verifyNoOtherTransitions(fragment);

        // reenter transition
        FragmentTestUtil.popBackStackImmediate(mActivityRule);
        fragment.waitForTransition();
        final View green2 = findGreen();
        final View blue2 = findBlue();
        verifyAndClearTransition(fragment.reenterTransition, null, green2, blue2);
        verifyNoOtherTransitions(fragment);

        // return transition
        FragmentTestUtil.popBackStackImmediate(mActivityRule);
        fragment.waitForTransition();
        verifyAndClearTransition(fragment.returnTransition, null, green2, blue2);
        verifyNoOtherTransitions(fragment);
    }

    // Test that shared elements transition from one fragment to the next
    // and back during pop.
    @Test
    public void sharedElement() throws Throwable {
        TransitionFragment fragment1 = setupInitialFragment();

        // Now do a transition to scene2
        TransitionFragment fragment2 = new TransitionFragment();
        fragment2.setLayoutId(R.layout.scene2);

        verifyTransition(fragment1, fragment2, "blueSquare");

        // Now pop the back stack
        verifyPopTransition(1, fragment2, fragment1);
    }

    // Test that shared element transitions through multiple fragments work together
    @Test
    public void intermediateFragment() throws Throwable {
        TransitionFragment fragment1 = setupInitialFragment();

        final TransitionFragment fragment2 = new TransitionFragment();
        fragment2.setLayoutId(R.layout.scene3);

        verifyTransition(fragment1, fragment2, "shared");

        final TransitionFragment fragment3 = new TransitionFragment();
        fragment3.setLayoutId(R.layout.scene2);

        verifyTransition(fragment2, fragment3, "blueSquare");

        // Should transfer backwards when popping multiple:
        verifyPopTransition(2, fragment3, fragment1, fragment2);
    }

    // Adding/removing the same fragment multiple times shouldn't mess anything up
    @Test
    public void removeAdded() throws Throwable {
        final TransitionFragment fragment1 = setupInitialFragment();

        final View startBlue = findBlue();
        final View startGreen = findGreen();

        final TransitionFragment fragment2 = new TransitionFragment();
        fragment2.setLayoutId(R.layout.scene2);

        mInstrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mFragmentManager.beginTransaction()
                        .setAllowOptimization(mOptimize)
                        .replace(R.id.fragmentContainer, fragment2)
                        .replace(R.id.fragmentContainer, fragment1)
                        .replace(R.id.fragmentContainer, fragment2)
                        .addToBackStack(null)
                        .commit();
            }
        });
        FragmentTestUtil.waitForExecution(mActivityRule);

        // should be a normal transition from fragment1 to fragment2
        fragment2.waitForTransition();
        final View endBlue = findBlue();
        final View endGreen = findGreen();
        verifyAndClearTransition(fragment1.exitTransition, null, startBlue, startGreen);
        verifyAndClearTransition(fragment2.enterTransition, null, endBlue, endGreen);
        verifyNoOtherTransitions(fragment1);
        verifyNoOtherTransitions(fragment2);

        // Pop should also do the same thing
        FragmentTestUtil.popBackStackImmediate(mActivityRule);

        fragment1.waitForTransition();
        final View popBlue = findBlue();
        final View popGreen = findGreen();
        verifyAndClearTransition(fragment1.reenterTransition, null, popBlue, popGreen);
        verifyAndClearTransition(fragment2.returnTransition, null, endBlue, endGreen);
        verifyNoOtherTransitions(fragment1);
        verifyNoOtherTransitions(fragment2);
    }

    // Make sure that shared elements on two different fragment containers don't interact
    @Test
    public void crossContainer() throws Throwable {
        FragmentTestUtil.setContentView(mActivityRule, R.layout.double_container);
        TransitionFragment fragment1 = new TransitionFragment();
        fragment1.setLayoutId(R.layout.scene1);
        TransitionFragment fragment2 = new TransitionFragment();
        fragment2.setLayoutId(R.layout.scene1);
        mFragmentManager.beginTransaction()
                .setAllowOptimization(mOptimize)
                .add(R.id.fragmentContainer1, fragment1)
                .add(R.id.fragmentContainer2, fragment2)
                .addToBackStack(null)
                .commit();
        FragmentTestUtil.waitForExecution(mActivityRule);

        fragment1.waitForTransition();
        final View greenSquare1 = findViewById(fragment1, R.id.greenSquare);
        final View blueSquare1 = findViewById(fragment1, R.id.blueSquare);
        verifyAndClearTransition(fragment1.enterTransition, null, greenSquare1, blueSquare1);
        verifyNoOtherTransitions(fragment1);
        fragment2.waitForTransition();
        final View greenSquare2 = findViewById(fragment2, R.id.greenSquare);
        final View blueSquare2 = findViewById(fragment2, R.id.blueSquare);
        verifyAndClearTransition(fragment2.enterTransition, null, greenSquare2, blueSquare2);
        verifyNoOtherTransitions(fragment2);

        // Make sure the correct transitions are run when the target names
        // are different in both shared elements. We may fool the system.
        verifyCrossTransition(false, fragment1, fragment2);

        // Make sure the correct transitions are run when the source names
        // are different in both shared elements. We may fool the system.
        verifyCrossTransition(true, fragment1, fragment2);
    }

    // Make sure that onSharedElementStart and onSharedElementEnd are called
    @Test
    public void callStartEndWithSharedElements() throws Throwable {
        TransitionFragment fragment1 = setupInitialFragment();

        // Now do a transition to scene2
        TransitionFragment fragment2 = new TransitionFragment();
        fragment2.setLayoutId(R.layout.scene2);

        SharedElementCallback enterCallback = mock(SharedElementCallback.class);
        fragment2.setEnterSharedElementCallback(enterCallback);

        final View startBlue = findBlue();

        verifyTransition(fragment1, fragment2, "blueSquare");

        ArgumentCaptor<List> names = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List> views = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List> snapshots = ArgumentCaptor.forClass(List.class);
        verify(enterCallback).onSharedElementStart(names.capture(), views.capture(),
                snapshots.capture());
        assertEquals(1, names.getValue().size());
        assertEquals(1, views.getValue().size());
        assertNull(snapshots.getValue());
        assertEquals("blueSquare", names.getValue().get(0));
        assertEquals(startBlue, views.getValue().get(0));

        final View endBlue = findBlue();

        verify(enterCallback).onSharedElementEnd(names.capture(), views.capture(),
                snapshots.capture());
        assertEquals(1, names.getValue().size());
        assertEquals(1, views.getValue().size());
        assertNull(snapshots.getValue());
        assertEquals("blueSquare", names.getValue().get(0));
        assertEquals(endBlue, views.getValue().get(0));

        // Now pop the back stack
        reset(enterCallback);
        verifyPopTransition(1, fragment2, fragment1);

        verify(enterCallback).onSharedElementStart(names.capture(), views.capture(),
                snapshots.capture());
        assertEquals(1, names.getValue().size());
        assertEquals(1, views.getValue().size());
        assertNull(snapshots.getValue());
        assertEquals("blueSquare", names.getValue().get(0));
        assertEquals(endBlue, views.getValue().get(0));

        final View reenterBlue = findBlue();

        verify(enterCallback).onSharedElementEnd(names.capture(), views.capture(),
                snapshots.capture());
        assertEquals(1, names.getValue().size());
        assertEquals(1, views.getValue().size());
        assertNull(snapshots.getValue());
        assertEquals("blueSquare", names.getValue().get(0));
        assertEquals(reenterBlue, views.getValue().get(0));
    }

    // Make sure that onMapSharedElement works to change the shared element going out
    @Test
    public void onMapSharedElementOut() throws Throwable {
        TransitionFragment fragment1 = setupInitialFragment();

        // Now do a transition to scene2
        TransitionFragment fragment2 = new TransitionFragment();
        fragment2.setLayoutId(R.layout.scene2);

        final View startBlue = findBlue();
        final View startGreen = findGreen();

        final Rect startGreenBounds = getBoundsOnScreen(startGreen);

        SharedElementCallback mapOut = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                assertEquals(1, names.size());
                assertEquals("blueSquare", names.get(0));
                assertEquals(1, sharedElements.size());
                assertEquals(startBlue, sharedElements.get("blueSquare"));
                sharedElements.put("blueSquare", startGreen);
            }
        };
        fragment1.setExitSharedElementCallback(mapOut);

        mFragmentManager.beginTransaction()
                .addSharedElement(startBlue, "blueSquare")
                .replace(R.id.fragmentContainer, fragment2)
                .setAllowOptimization(mOptimize)
                .addToBackStack(null)
                .commit();
        FragmentTestUtil.waitForExecution(mActivityRule);

        fragment1.waitForTransition();
        fragment2.waitForTransition();

        final View endBlue = findBlue();
        final Rect endBlueBounds = getBoundsOnScreen(endBlue);

        verifyAndClearTransition(fragment2.sharedElementEnter, startGreenBounds, startGreen,
                endBlue);

        SharedElementCallback mapBack = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                assertEquals(1, names.size());
                assertEquals("blueSquare", names.get(0));
                assertEquals(1, sharedElements.size());
                final View expectedBlue = findViewById(fragment1, R.id.blueSquare);
                assertEquals(expectedBlue, sharedElements.get("blueSquare"));
                final View greenSquare = findViewById(fragment1, R.id.greenSquare);
                sharedElements.put("blueSquare", greenSquare);
            }
        };
        fragment1.setExitSharedElementCallback(mapBack);

        FragmentTestUtil.popBackStackImmediate(mActivityRule);

        fragment1.waitForTransition();
        fragment2.waitForTransition();

        final View reenterGreen = findGreen();
        verifyAndClearTransition(fragment2.sharedElementReturn, endBlueBounds, endBlue,
                reenterGreen);
    }

    // Make sure that onMapSharedElement works to change the shared element target
    @Test
    public void onMapSharedElementIn() throws Throwable {
        TransitionFragment fragment1 = setupInitialFragment();

        // Now do a transition to scene2
        TransitionFragment fragment2 = new TransitionFragment();
        fragment2.setLayoutId(R.layout.scene2);

        final View startBlue = findBlue();
        final Rect startBlueBounds = getBoundsOnScreen(startBlue);

        SharedElementCallback mapIn = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                assertEquals(1, names.size());
                assertEquals("blueSquare", names.get(0));
                assertEquals(1, sharedElements.size());
                final View blueSquare = findViewById(fragment2, R.id.blueSquare);
                assertEquals(blueSquare, sharedElements.get("blueSquare"));
                final View greenSquare = findViewById(fragment2, R.id.greenSquare);
                sharedElements.put("blueSquare", greenSquare);
            }
        };
        fragment2.setEnterSharedElementCallback(mapIn);

        mFragmentManager.beginTransaction()
                .addSharedElement(startBlue, "blueSquare")
                .replace(R.id.fragmentContainer, fragment2)
                .setAllowOptimization(mOptimize)
                .addToBackStack(null)
                .commit();
        FragmentTestUtil.waitForExecution(mActivityRule);

        fragment1.waitForTransition();
        fragment2.waitForTransition();

        final View endGreen = findGreen();
        final View endBlue = findBlue();
        final Rect endGreenBounds = getBoundsOnScreen(endGreen);

        verifyAndClearTransition(fragment2.sharedElementEnter, startBlueBounds, startBlue,
                endGreen);

        SharedElementCallback mapBack = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                assertEquals(1, names.size());
                assertEquals("blueSquare", names.get(0));
                assertEquals(1, sharedElements.size());
                assertEquals(endBlue, sharedElements.get("blueSquare"));
                sharedElements.put("blueSquare", endGreen);
            }
        };
        fragment2.setEnterSharedElementCallback(mapBack);

        FragmentTestUtil.popBackStackImmediate(mActivityRule);

        fragment1.waitForTransition();
        fragment2.waitForTransition();

        final View reenterBlue = findBlue();
        verifyAndClearTransition(fragment2.sharedElementReturn, endGreenBounds, endGreen,
                reenterBlue);
    }

    // Ensure that shared element transitions that have targets properly target the views
    @Test
    public void complexSharedElementTransition() throws Throwable {
        TransitionFragment fragment1 = setupInitialFragment();

        // Now do a transition to scene2
        ComplexTransitionFragment fragment2 = new ComplexTransitionFragment();
        fragment2.setLayoutId(R.layout.scene2);

        final View startBlue = findBlue();
        final View startGreen = findGreen();
        final Rect startBlueBounds = getBoundsOnScreen(startBlue);

        mFragmentManager.beginTransaction()
                .addSharedElement(startBlue, "blueSquare")
                .addSharedElement(startGreen, "greenSquare")
                .replace(R.id.fragmentContainer, fragment2)
                .addToBackStack(null)
                .commit();
        FragmentTestUtil.waitForExecution(mActivityRule);

        fragment1.waitForTransition();
        fragment2.waitForTransition();

        final View endBlue = findBlue();
        final View endGreen = findGreen();
        final Rect endBlueBounds = getBoundsOnScreen(endBlue);

        verifyAndClearTransition(fragment2.sharedElementEnterTransition1, startBlueBounds,
                startBlue, endBlue);
        verifyAndClearTransition(fragment2.sharedElementEnterTransition2, startBlueBounds,
                startGreen, endGreen);

        // Now see if it works when popped
        FragmentTestUtil.popBackStackImmediate(mActivityRule);

        fragment1.waitForTransition();
        fragment2.waitForTransition();

        final View reenterBlue = findBlue();
        final View reenterGreen = findGreen();

        verifyAndClearTransition(fragment2.sharedElementReturnTransition1, endBlueBounds,
                endBlue, reenterBlue);
        verifyAndClearTransition(fragment2.sharedElementReturnTransition2, endBlueBounds,
                endGreen, reenterGreen);
    }

    // Ensure that after transitions have executed that they don't have any targets or other
    // unfortunate modifications.
    @Test
    public void transitionsEndUnchanged() throws Throwable {
        TransitionFragment fragment1 = setupInitialFragment();

        // Now do a transition to scene2
        TransitionFragment fragment2 = new TransitionFragment();
        fragment2.setLayoutId(R.layout.scene2);

        verifyTransition(fragment1, fragment2, "blueSquare");
        assertEquals(0, fragment1.exitTransition.getTargets().size());
        assertEquals(0, fragment2.sharedElementEnter.getTargets().size());
        assertEquals(0, fragment2.enterTransition.getTargets().size());
        assertNull(fragment1.exitTransition.getEpicenterCallback());
        assertNull(fragment2.enterTransition.getEpicenterCallback());
        assertNull(fragment2.sharedElementEnter.getEpicenterCallback());

        // Now pop the back stack
        verifyPopTransition(1, fragment2, fragment1);

        assertEquals(0, fragment2.returnTransition.getTargets().size());
        assertEquals(0, fragment2.sharedElementReturn.getTargets().size());
        assertEquals(0, fragment1.reenterTransition.getTargets().size());
        assertNull(fragment2.returnTransition.getEpicenterCallback());
        assertNull(fragment2.sharedElementReturn.getEpicenterCallback());
        assertNull(fragment2.reenterTransition.getEpicenterCallback());
    }

    // Ensure that transitions are done when a fragment is shown and hidden
    @Test
    public void showHideTransition() throws Throwable {
        TransitionFragment fragment1 = setupInitialFragment();
        TransitionFragment fragment2 = new TransitionFragment();
        fragment2.setLayoutId(R.layout.scene2);

        final View startBlue = findBlue();
        final View startGreen = findGreen();

        mFragmentManager.beginTransaction()
                .setAllowOptimization(mOptimize)
                .add(R.id.fragmentContainer, fragment2)
                .hide(fragment1)
                .addToBackStack(null)
                .commit();

        FragmentTestUtil.waitForExecution(mActivityRule);
        fragment1.waitForTransition();
        fragment2.waitForTransition();

        final View endGreen = findViewById(fragment2, R.id.greenSquare);
        final View endBlue = findViewById(fragment2, R.id.blueSquare);

        assertEquals(View.GONE, fragment1.getView().getVisibility());
        assertEquals(View.VISIBLE, startGreen.getVisibility());
        assertEquals(View.VISIBLE, startBlue.getVisibility());

        verifyAndClearTransition(fragment1.exitTransition, null, startGreen, startBlue);
        verifyNoOtherTransitions(fragment1);

        verifyAndClearTransition(fragment2.enterTransition, null, endGreen, endBlue);
        verifyNoOtherTransitions(fragment2);

        FragmentTestUtil.popBackStackImmediate(mActivityRule);

        FragmentTestUtil.waitForExecution(mActivityRule);
        fragment1.waitForTransition();
        fragment2.waitForTransition();

        verifyAndClearTransition(fragment1.reenterTransition, null, startGreen, startBlue);
        verifyNoOtherTransitions(fragment1);

        assertEquals(View.VISIBLE, fragment1.getView().getVisibility());
        assertEquals(View.VISIBLE, startGreen.getVisibility());
        assertEquals(View.VISIBLE, startBlue.getVisibility());

        verifyAndClearTransition(fragment2.returnTransition, null, endGreen, endBlue);
        verifyNoOtherTransitions(fragment2);
    }

    // Ensure that transitions are done when a fragment is attached and detached
    @Test
    public void attachDetachTransition() throws Throwable {
        TransitionFragment fragment1 = setupInitialFragment();
        TransitionFragment fragment2 = new TransitionFragment();
        fragment2.setLayoutId(R.layout.scene2);

        final View startBlue = findBlue();
        final View startGreen = findGreen();

        mFragmentManager.beginTransaction()
                .setAllowOptimization(mOptimize)
                .add(R.id.fragmentContainer, fragment2)
                .detach(fragment1)
                .addToBackStack(null)
                .commit();

        FragmentTestUtil.waitForExecution(mActivityRule);

        final View endGreen = findViewById(fragment2, R.id.greenSquare);
        final View endBlue = findViewById(fragment2, R.id.blueSquare);

        verifyAndClearTransition(fragment1.exitTransition, null, startGreen, startBlue);
        verifyNoOtherTransitions(fragment1);

        verifyAndClearTransition(fragment2.enterTransition, null, endGreen, endBlue);
        verifyNoOtherTransitions(fragment2);

        FragmentTestUtil.popBackStackImmediate(mActivityRule);

        FragmentTestUtil.waitForExecution(mActivityRule);

        final View reenterBlue = findBlue();
        final View reenterGreen = findGreen();

        verifyAndClearTransition(fragment1.reenterTransition, null, reenterGreen, reenterBlue);
        verifyNoOtherTransitions(fragment1);

        verifyAndClearTransition(fragment2.returnTransition, null, endGreen, endBlue);
        verifyNoOtherTransitions(fragment2);
    }

    private TransitionFragment setupInitialFragment() throws Throwable {
        TransitionFragment fragment1 = new TransitionFragment();
        fragment1.setLayoutId(R.layout.scene1);
        mFragmentManager.beginTransaction()
                .setAllowOptimization(mOptimize)
                .add(R.id.fragmentContainer, fragment1)
                .addToBackStack(null)
                .commit();
        FragmentTestUtil.waitForExecution(mActivityRule);
        fragment1.waitForTransition();
        final View blueSquare1 = findBlue();
        final View greenSquare1 = findGreen();
        verifyAndClearTransition(fragment1.enterTransition, null, blueSquare1, greenSquare1);
        verifyNoOtherTransitions(fragment1);
        return fragment1;
    }

    private View findViewById(Fragment fragment, int id) {
        return fragment.getView().findViewById(id);
    }

    private View findGreen() {
        return mActivityRule.getActivity().findViewById(R.id.greenSquare);
    }

    private View findBlue() {
        return mActivityRule.getActivity().findViewById(R.id.blueSquare);
    }

    private View findRed() {
        return mActivityRule.getActivity().findViewById(R.id.redSquare);
    }

    private void verifyAndClearTransition(TargetTracking transition, Rect epicenter,
            View... expected) {
        if (epicenter == null) {
            assertNull(transition.getCapturedEpicenter());
        } else {
            assertEquals(epicenter, transition.getCapturedEpicenter());
        }
        ArrayList<View> targets = transition.getTrackedTargets();
        String errorMessage = "Expected: [" + expected.length + "] {" +
                Arrays.stream(expected).map(v -> v.toString()).collect(Collectors.joining(", ")) +
                "}, but got: [" + targets.size() + "] {" +
                targets.stream().map(v -> v.toString()).collect(Collectors.joining(", ")) +
                "}";
        assertEquals(errorMessage, expected.length, targets.size());
        for (View view : expected) {
            assertTrue(errorMessage, targets.contains(view));
        }
        transition.clearTargets();
    }

    private void verifyNoOtherTransitions(TransitionFragment fragment) {
        assertEquals(0, fragment.enterTransition.targets.size());
        assertEquals(0, fragment.exitTransition.targets.size());
        assertEquals(0, fragment.reenterTransition.targets.size());
        assertEquals(0, fragment.returnTransition.targets.size());
        assertEquals(0, fragment.sharedElementEnter.targets.size());
        assertEquals(0, fragment.sharedElementReturn.targets.size());
    }

    private void verifyTransition(TransitionFragment from, TransitionFragment to,
            String sharedElementName) throws Throwable {
        final View startBlue = findBlue();
        final View startGreen = findGreen();
        final View startRed = findRed();

        final Rect startBlueRect = getBoundsOnScreen(startBlue);

        mFragmentManager.beginTransaction()
                .setAllowOptimization(mOptimize)
                .addSharedElement(startBlue, sharedElementName)
                .replace(R.id.fragmentContainer, to)
                .addToBackStack(null)
                .commit();

        FragmentTestUtil.waitForExecution(mActivityRule);

        to.waitForTransition();
        final View endGreen = findGreen();
        final View endBlue = findBlue();
        final View endRed = findRed();
        final Rect endBlueRect = getBoundsOnScreen(endBlue);

        if (startRed != null) {
            verifyAndClearTransition(from.exitTransition, startBlueRect, startGreen, startRed);
        } else {
            verifyAndClearTransition(from.exitTransition, startBlueRect, startGreen);
        }
        verifyNoOtherTransitions(from);

        if (endRed != null) {
            verifyAndClearTransition(to.enterTransition, endBlueRect, endGreen, endRed);
        } else {
            verifyAndClearTransition(to.enterTransition, endBlueRect, endGreen);
        }
        verifyAndClearTransition(to.sharedElementEnter, startBlueRect, startBlue, endBlue);
        verifyNoOtherTransitions(to);
    }

    private void verifyCrossTransition(boolean swapSource,
            TransitionFragment from1, TransitionFragment from2) throws Throwable {

        final TransitionFragment to1 = new TransitionFragment();
        to1.setLayoutId(R.layout.scene2);
        final TransitionFragment to2 = new TransitionFragment();
        to2.setLayoutId(R.layout.scene2);

        final View fromExit1 = findViewById(from1, R.id.greenSquare);
        final View fromShared1 = findViewById(from1, R.id.blueSquare);
        final Rect fromSharedRect1 = getBoundsOnScreen(fromShared1);

        final int fromExitId2 = swapSource ? R.id.blueSquare : R.id.greenSquare;
        final int fromSharedId2 = swapSource ? R.id.greenSquare : R.id.blueSquare;
        final View fromExit2 = findViewById(from2, fromExitId2);
        final View fromShared2 = findViewById(from2, fromSharedId2);
        final Rect fromSharedRect2 = getBoundsOnScreen(fromShared2);

        final String sharedElementName = swapSource ? "blueSquare" : "greenSquare";

        mActivityRule.runOnUiThread(() -> {
            mFragmentManager.beginTransaction()
                    .setAllowOptimization(mOptimize)
                    .addSharedElement(fromShared1, "blueSquare")
                    .replace(R.id.fragmentContainer1, to1)
                    .addToBackStack(null)
                    .commit();
            mFragmentManager.beginTransaction()
                    .setAllowOptimization(mOptimize)
                    .addSharedElement(fromShared2, sharedElementName)
                    .replace(R.id.fragmentContainer2, to2)
                    .addToBackStack(null)
                    .commit();
        });
        FragmentTestUtil.waitForExecution(mActivityRule);

        from1.waitForTransition();
        from2.waitForTransition();
        to1.waitForTransition();
        to2.waitForTransition();

        final View toEnter1 = findViewById(to1, R.id.greenSquare);
        final View toShared1 = findViewById(to1, R.id.blueSquare);
        final Rect toSharedRect1 = getBoundsOnScreen(toShared1);

        final View toEnter2 = findViewById(to2, fromSharedId2);
        final View toShared2 = findViewById(to2, fromExitId2);
        final Rect toSharedRect2 = getBoundsOnScreen(toShared2);

        verifyAndClearTransition(from1.exitTransition, fromSharedRect1, fromExit1);
        verifyAndClearTransition(from2.exitTransition, fromSharedRect2, fromExit2);
        verifyNoOtherTransitions(from1);
        verifyNoOtherTransitions(from2);

        verifyAndClearTransition(to1.enterTransition, toSharedRect1, toEnter1);
        verifyAndClearTransition(to2.enterTransition, toSharedRect2, toEnter2);
        verifyAndClearTransition(to1.sharedElementEnter, fromSharedRect1, fromShared1, toShared1);
        verifyAndClearTransition(to2.sharedElementEnter, fromSharedRect2, fromShared2, toShared2);
        verifyNoOtherTransitions(to1);
        verifyNoOtherTransitions(to2);

        // Now pop it back
        mActivityRule.runOnUiThread(() -> {
            mFragmentManager.popBackStack();
            mFragmentManager.popBackStack();
        });
        FragmentTestUtil.waitForExecution(mActivityRule);

        from1.waitForTransition();
        from2.waitForTransition();
        to1.waitForTransition();
        to2.waitForTransition();

        final View returnEnter1 = findViewById(from1, R.id.greenSquare);
        final View returnShared1 = findViewById(from1, R.id.blueSquare);

        final View returnEnter2 = findViewById(from2, fromExitId2);
        final View returnShared2 = findViewById(from2, fromSharedId2);

        verifyAndClearTransition(to1.returnTransition, toSharedRect1, toEnter1);
        verifyAndClearTransition(to2.returnTransition, toSharedRect2, toEnter2);
        verifyAndClearTransition(to1.sharedElementReturn, toSharedRect1, toShared1, returnShared1);
        verifyAndClearTransition(to2.sharedElementReturn, toSharedRect2, toShared2, returnShared2);
        verifyNoOtherTransitions(to1);
        verifyNoOtherTransitions(to2);

        verifyAndClearTransition(from1.reenterTransition, fromSharedRect1, returnEnter1);
        verifyAndClearTransition(from2.reenterTransition, fromSharedRect2, returnEnter2);
        verifyNoOtherTransitions(from1);
        verifyNoOtherTransitions(from2);
    }

    private void verifyPopTransition(final int numPops, TransitionFragment from,
            TransitionFragment to, TransitionFragment... others) throws Throwable {
        final View startBlue = findBlue();
        final View startGreen = findGreen();
        final View startRed = findRed();
        final Rect startSharedRect = getBoundsOnScreen(startBlue);

        mInstrumentation.runOnMainSync(() -> {
            for (int i = 0; i < numPops; i++) {
                mFragmentManager.popBackStack();
            }
        });
        FragmentTestUtil.waitForExecution(mActivityRule);

        to.waitForTransition();
        final View endGreen = findGreen();
        final View endBlue = findBlue();
        final View endRed = findRed();
        final Rect endSharedRect = getBoundsOnScreen(endBlue);

        if (startRed != null) {
            verifyAndClearTransition(from.returnTransition, startSharedRect, startGreen, startRed);
        } else {
            verifyAndClearTransition(from.returnTransition, startSharedRect, startGreen);
        }
        verifyAndClearTransition(from.sharedElementReturn, startSharedRect, startBlue, endBlue);
        verifyNoOtherTransitions(from);

        if (endRed != null) {
            verifyAndClearTransition(to.reenterTransition, endSharedRect, endGreen, endRed);
        } else {
            verifyAndClearTransition(to.reenterTransition, endSharedRect, endGreen);
        }
        verifyNoOtherTransitions(to);

        if (others != null) {
            for (TransitionFragment fragment : others) {
                verifyNoOtherTransitions(fragment);
            }
        }
    }

    private static Rect getBoundsOnScreen(View view) {
        final int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        return new Rect(loc[0], loc[1], loc[0] + view.getWidth(), loc[1] + view.getHeight());
    }

    public static class ComplexTransitionFragment extends TransitionFragment {
        public final TrackingTransition sharedElementEnterTransition1 = new TrackingTransition();
        public final TrackingTransition sharedElementEnterTransition2 = new TrackingTransition();
        public final TrackingTransition sharedElementReturnTransition1 = new TrackingTransition();
        public final TrackingTransition sharedElementReturnTransition2 = new TrackingTransition();

        public final TransitionSet sharedElementEnterTransition = new TransitionSet()
                .addTransition(sharedElementEnterTransition1)
                .addTransition(sharedElementEnterTransition2);
        public final TransitionSet sharedElementReturnTransition = new TransitionSet()
                .addTransition(sharedElementReturnTransition1)
                .addTransition(sharedElementReturnTransition2);

        public ComplexTransitionFragment() {
            sharedElementEnterTransition1.addTarget(R.id.blueSquare);
            sharedElementEnterTransition2.addTarget(R.id.greenSquare);
            sharedElementReturnTransition1.addTarget(R.id.blueSquare);
            sharedElementReturnTransition2.addTarget(R.id.greenSquare);
            setSharedElementEnterTransition(sharedElementEnterTransition);
            setSharedElementReturnTransition(sharedElementReturnTransition);
        }

    }
}

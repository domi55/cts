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

package android.widget.cts;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.cts.util.PollingCheck;
import android.cts.util.WidgetTestUtils;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.ActionMode;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.cts.util.TestUtils;
import android.widget.cts.util.TestUtilsMatchers;
import android.widget.cts.util.ViewTestUtils;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class AbsListViewTest {
    private static final String[] SHORT_LIST = new String[] { "This", "is", "short", "!" };

    private static final String[] COUNTRY_LIST = new String[] {
            "Argentina", "Armenia", "Aruba", "Australia", "Belarus", "Belgium", "Belize", "Benin",
            "Botswana", "Brazil", "Cameroon", "China", "Colombia", "Costa Rica", "Cyprus",
            "Denmark", "Djibouti", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Germany",
            "Ghana", "Haiti", "Honduras", "Iceland", "India", "Indonesia", "Ireland", "Italy",
            "Japan", "Kiribati", "Laos", "Lesotho", "Liberia", "Malaysia", "Mongolia", "Myanmar",
            "Nauru", "Norway", "Oman", "Pakistan", "Philippines", "Portugal", "Romania", "Russia",
            "Rwanda", "Singapore", "Slovakia", "Slovenia", "Somalia", "Swaziland", "Togo", "Tuvalu",
            "Uganda", "Ukraine", "United States", "Vanuatu", "Venezuela", "Zimbabwe"
    };

    @Rule
    public ActivityTestRule<ListViewCtsActivity> mActivityRule
            = new ActivityTestRule<>(ListViewCtsActivity.class);

    private AbsListView mListView;
    private Context mContext;
    private Instrumentation mInstrumentation;
    private AttributeSet mAttributeSet;
    private ArrayAdapter<String> mShortAdapter;
    private ArrayAdapter<String> mCountriesAdapter;

    private static final float DELTA = 0.001f;

    @Before
    public void setup() throws Exception {

        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mContext = mInstrumentation.getTargetContext();

        final Activity activity = mActivityRule.getActivity();

        PollingCheck.waitFor(() -> activity.hasWindowFocus());

        XmlPullParser parser = mContext.getResources().getXml(R.layout.listview_layout);
        WidgetTestUtils.beginDocument(parser, "FrameLayout");
        mAttributeSet = Xml.asAttributeSet(parser);

        mShortAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, SHORT_LIST);
        mCountriesAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, COUNTRY_LIST);

        mListView = (ListView) activity.findViewById(R.id.listview_default);
    }

    @Test
    public void testAccessFastScrollEnabled() {
        mListView.setFastScrollEnabled(false);
        assertFalse(mListView.isFastScrollEnabled());

        mListView.setFastScrollEnabled(true);
        assertTrue(mListView.isFastScrollEnabled());
    }

    @Test
    public void testAccessSmoothScrollbarEnabled() {
        mListView.setSmoothScrollbarEnabled(false);
        assertFalse(mListView.isSmoothScrollbarEnabled());

        mListView.setSmoothScrollbarEnabled(true);
        assertTrue(mListView.isSmoothScrollbarEnabled());
    }

    @Test
    public void testAccessScrollingCacheEnabled() {
        mListView.setScrollingCacheEnabled(false);
        assertFalse(mListView.isScrollingCacheEnabled());

        mListView.setScrollingCacheEnabled(true);
        assertTrue(mListView.isScrollingCacheEnabled());
    }

    private void setAdapter() {
        setAdapter(mCountriesAdapter);
    }

    private void setAdapter(final ListAdapter adapter) {
        mInstrumentation.runOnMainSync(() -> mListView.setAdapter(adapter));
        mInstrumentation.waitForIdleSync();
    }

    private void setListSelection(int index) {
        mInstrumentation.runOnMainSync(() -> mListView.setSelection(index));
        mInstrumentation.waitForIdleSync();
    }

    @LargeTest
    @Test
    public void testSetOnScrollListener() {
        AbsListView.OnScrollListener mockScrollListener =
                mock(AbsListView.OnScrollListener.class);

        verifyZeroInteractions(mockScrollListener);

        mListView.setOnScrollListener(mockScrollListener);
        verify(mockScrollListener, times(1)).onScroll(mListView, 0, 0, 0);
        verifyNoMoreInteractions(mockScrollListener);

        reset(mockScrollListener);

        setAdapter();
        verify(mockScrollListener, times(1)).onScroll(mListView, 0, mListView.getChildCount(),
                COUNTRY_LIST.length);
        verifyNoMoreInteractions(mockScrollListener);

        reset(mockScrollListener);

        ViewTestUtils.emulateScrollToBottom(mInstrumentation, mListView);

        ArgumentCaptor<Integer> firstVisibleItemCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> visibleItemCountCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mockScrollListener, atLeastOnce()).onScroll(eq(mListView),
                firstVisibleItemCaptor.capture(), visibleItemCountCaptor.capture(),
                eq(COUNTRY_LIST.length));

        // We expect the first visible item values to be increasing
        MatcherAssert.assertThat(firstVisibleItemCaptor.getAllValues(),
                TestUtilsMatchers.inAscendingOrder());
        // The number of visible items during scrolling may change depending on the specific
        // scroll position. As such we only test this number at the very end
        final List<Integer> capturedVisibleItemCounts = visibleItemCountCaptor.getAllValues();
        assertEquals(mListView.getChildCount(),
                (int) capturedVisibleItemCounts.get(capturedVisibleItemCounts.size() - 1));

        ArgumentCaptor<Integer> scrollStateCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mockScrollListener, atLeastOnce()).onScrollStateChanged(eq(mListView),
                scrollStateCaptor.capture());

        // Verify that the last scroll state is IDLE
        final List<Integer> capturedScrollStates = scrollStateCaptor.getAllValues();
        assertEquals(AbsListView.OnScrollListener.SCROLL_STATE_IDLE,
                (int) capturedScrollStates.get(capturedScrollStates.size() - 1));
    }

    @LargeTest
    @Test
    public void testFling() {
        AbsListView.OnScrollListener mockScrollListener = mock(AbsListView.OnScrollListener.class);
        mListView.setOnScrollListener(mockScrollListener);

        setAdapter();

        // Fling down from top, expect a scroll.
        fling(10000, mockScrollListener);
        ArgumentCaptor<Integer> firstVisibleItemCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mockScrollListener, atLeastOnce()).onScroll(eq(mListView),
                firstVisibleItemCaptor.capture(), anyInt(), eq(COUNTRY_LIST.length));
        List<Integer> capturedFirstVisibleItems = firstVisibleItemCaptor.getAllValues();
        assertTrue(capturedFirstVisibleItems.get(capturedFirstVisibleItems.size() - 1) > 0);

        // Fling up the same amount, expect a scroll to the original position.
        fling(-10000, mockScrollListener);
        firstVisibleItemCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mockScrollListener, atLeastOnce()).onScroll(eq(mListView),
                firstVisibleItemCaptor.capture(), anyInt(), eq(COUNTRY_LIST.length));
        capturedFirstVisibleItems = firstVisibleItemCaptor.getAllValues();
        assertTrue(capturedFirstVisibleItems.get(capturedFirstVisibleItems.size() - 1) == 0);

        // Fling up again, expect no scroll, as the viewport is already at top.
        fling(-10000, mockScrollListener);
        verify(mockScrollListener, never()).onScroll(any(AbsListView.class), anyInt(), anyInt(),
                anyInt());

        // Fling up again with a huge velocity, expect no scroll.
        fling(-50000, mockScrollListener);
        verify(mockScrollListener, never()).onScroll(any(AbsListView.class), anyInt(), anyInt(),
                anyInt());
    }

    private void fling(int velocityY, OnScrollListener mockScrollListener) {
        reset(mockScrollListener);

        // Create a count down latch and configure it to be counted down when our mock
        // listener is invoked with IDLE state
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        doAnswer((InvocationOnMock invocation) -> {
             countDownLatch.countDown();
             return null;
        }).when(mockScrollListener).onScrollStateChanged(
                mListView, OnScrollListener.SCROLL_STATE_IDLE);

        // Now fling the list view
        mInstrumentation.runOnMainSync(() -> mListView.fling(velocityY));

        // And wait for the latch to be triggered
        try {
            assertTrue(countDownLatch.await(20L, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
        }
    }

    private class ScrollListListener implements AbsListView.OnScrollListener {
        private CountDownLatch mLatchToNotify;
        private int mTargetPosition;

        public ScrollListListener(CountDownLatch latchToNotify, int targetPosition) {
            mLatchToNotify = latchToNotify;
            mTargetPosition = targetPosition;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                int totalItemCount) {
            // Is our target in current visible range?
            int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
            boolean isInRange = (mTargetPosition >= firstVisibleItem) &&
                    (mTargetPosition <= lastVisibleItem);
            if (!isInRange) {
                return;
            }

            // Is our target also fully visible?
            int visibleIndexOfTarget = mTargetPosition - firstVisibleItem;
            View targetChild = mListView.getChildAt(visibleIndexOfTarget);
            boolean isTargetFullyVisible = (targetChild.getTop() >= 0) &&
                    (targetChild.getBottom() <= mListView.getHeight());
            if (isTargetFullyVisible) {
                mListView.setOnScrollListener(null);
                mLatchToNotify.countDown();
            }
        }
    }

    private void verifyScrollToPosition(int positionToScrollTo) {
        final int firstVisiblePosition = mListView.getFirstVisiblePosition();
        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        // Is it already on the screen?
        if ((positionToScrollTo >= firstVisiblePosition) &&
                (positionToScrollTo <= lastVisiblePosition)) {
            return;
        }

        // Register a scroll listener on our ListView. The listener will notify our latch
        // when the "target" item comes into view. If that never happens, the latch will
        // time out and fail the test.
        final CountDownLatch latch = new CountDownLatch(1);
        mListView.setOnScrollListener(new ScrollListListener(latch, positionToScrollTo));
        mInstrumentation.runOnMainSync(() -> mListView.smoothScrollToPosition(
                positionToScrollTo));

        boolean result = false;
        try {
            result = latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }
        assertTrue("Timed out while waiting for the target view to be scrolled into view", result);

        // Verify that the position we've been asked to scroll to is visible
        assertTrue("Asked to scroll to " + positionToScrollTo + " and first visible is " +
                        mListView.getFirstVisiblePosition(),
                positionToScrollTo >= mListView.getFirstVisiblePosition());
        assertTrue("Asked to scroll to " + positionToScrollTo + " and last visible is " +
                        mListView.getLastVisiblePosition(),
                positionToScrollTo <= mListView.getLastVisiblePosition());
    }

    @LargeTest
    @Test
    public void testSmoothScrollToPosition() {
        final int itemCount = COUNTRY_LIST.length;

        setAdapter();

        // Scroll closer to the end of the list
        verifyScrollToPosition(itemCount - 10);
        // Scroll back towards the beginning of the list
        verifyScrollToPosition(5);
        // And then towards the end of the list again
        verifyScrollToPosition(itemCount - 1);
    }

    @LargeTest
    @Test
    public void testCanScrollList() {
        final int itemCount = COUNTRY_LIST.length;

        setAdapter();

        assertEquals(0, mListView.getFirstVisiblePosition());

        // Verify that when we're at the top of the list, we can't scroll up but we can scroll
        // down (unless we're on a huge screen and everything fits).
        assertFalse(mListView.canScrollList(-1));
        if (mListView.getLastVisiblePosition() < (itemCount - 1)) {
            assertTrue(mListView.canScrollList(1));
        }

        // Scroll down to the very end of the list
        verifyScrollToPosition(itemCount - 1);
        assertEquals(itemCount - 1, mListView.getLastVisiblePosition());

        // Verify that when we're at the bottom of the list, we can't scroll down but we can scroll
        // up (unless we're on a huge screen and everything fits).
        assertFalse(mListView.canScrollList(1));
        if (mListView.getFirstVisiblePosition() > 0) {
            assertTrue(mListView.canScrollList(-1));
        }

        // Scroll up to the middle of the list
        final int itemInTheMiddle = itemCount / 2;
        verifyScrollToPosition(itemInTheMiddle);

        // Verify that when we're in the middle of the list, we can scroll both up and down (unless
        // we're on a huge screen and everything fits).
        if (mListView.getFirstVisiblePosition() > 0) {
            assertTrue(mListView.canScrollList(-1));
        }
        if (mListView.getLastVisiblePosition() < (itemCount - 1)) {
            assertTrue(mListView.canScrollList(1));
        }
    }

    @Test
    public void testGetFocusedRect() {
        setAdapter(mShortAdapter);
        setListSelection(0);

        Rect r1 = new Rect();
        mListView.getFocusedRect(r1);

        assertEquals(0, r1.top);
        assertTrue(r1.bottom > 0);
        assertEquals(0, r1.left);
        assertTrue(r1.right > 0);

        setListSelection(3);
        Rect r2 = new Rect();
        mListView.getFocusedRect(r2);
        assertTrue(r2.top > 0);
        assertTrue(r2.bottom > 0);
        assertEquals(0, r2.left);
        assertTrue(r2.right > 0);

        assertTrue(r2.top > r1.top);
        assertEquals(r1.bottom - r1.top, r2.bottom - r2.top);
        assertEquals(r1.right, r2.right);
    }

    @Test
    public void testAccessStackFromBottom() {
        setAdapter();

        mInstrumentation.runOnMainSync(() -> mListView.setStackFromBottom(false));
        assertFalse(mListView.isStackFromBottom());
        assertEquals(0, mListView.getSelectedItemPosition());

        mInstrumentation.runOnMainSync(() -> mListView.setStackFromBottom(true));

        mInstrumentation.waitForIdleSync();
        assertTrue(mListView.isStackFromBottom());
        // ensure last item in list is selected
        assertEquals(COUNTRY_LIST.length-1, mListView.getSelectedItemPosition());
    }

    @Test
    public void testAccessSelectedItem() {
        assertNull(mListView.getSelectedView());

        setAdapter();

        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        TextView tv = (TextView) mListView.getSelectedView();
        assertEquals(COUNTRY_LIST[0], tv.getText().toString());

        if (lastVisiblePosition >= 5) {
            setListSelection(5);
            tv = (TextView) mListView.getSelectedView();
            assertEquals(COUNTRY_LIST[5], tv.getText().toString());
        }

        if (lastVisiblePosition >= 2) {
            setListSelection(2);
            tv = (TextView) mListView.getSelectedView();
            assertEquals(COUNTRY_LIST[2], tv.getText().toString());
        }
    }

    @Test
    public void testAccessListPadding() {
        setAdapter();

        assertEquals(0, mListView.getListPaddingLeft());
        assertEquals(0, mListView.getListPaddingTop());
        assertEquals(0, mListView.getListPaddingRight());
        assertEquals(0, mListView.getListPaddingBottom());

        final Rect r = new Rect(0, 0, 40, 60);
        mInstrumentation.runOnMainSync(
                () -> mListView.setPadding(r.left, r.top, r.right, r.bottom));
        mInstrumentation.waitForIdleSync();

        assertEquals(r.left, mListView.getListPaddingLeft());
        assertEquals(r.top, mListView.getListPaddingTop());
        assertEquals(r.right, mListView.getListPaddingRight());
        assertEquals(r.bottom, mListView.getListPaddingBottom());
    }

    @Test
    public void testAccessSelector() {
        setAdapter();

        final Drawable d = mContext.getDrawable(R.drawable.pass);
        mListView.setSelector(d);

        mInstrumentation.runOnMainSync(() -> mListView.requestLayout());
        mInstrumentation.waitForIdleSync();
        assertSame(d, mListView.getSelector());
        assertTrue(mListView.verifyDrawable(d));

        mListView.setSelector(R.drawable.failed);
        mListView.setDrawSelectorOnTop(true);

        mInstrumentation.runOnMainSync(() -> mListView.requestLayout());
        mInstrumentation.waitForIdleSync();

        Drawable drawable = mListView.getSelector();
        assertNotNull(drawable);
        final Rect r = drawable.getBounds();

        final TextView v = (TextView) mListView.getSelectedView();
        PollingCheck.waitFor(() -> v.getRight() == r.right);
        assertEquals(v.getLeft(), r.left);
        assertEquals(v.getTop(), r.top);
        assertEquals(v.getBottom(), r.bottom);
    }

    @Test
    public void testSetScrollIndicators() {
        final Activity activity = mActivityRule.getActivity();
        TextView tv1 = (TextView) activity.findViewById(R.id.headerview1);
        TextView tv2 = (TextView) activity.findViewById(R.id.footerview1);

        setAdapter();

        mListView.setScrollIndicators(tv1, tv2);

        mInstrumentation.runOnMainSync(() -> mListView.requestLayout());
        mInstrumentation.waitForIdleSync();
    }

    @Test
    public void testShowContextMenuForChild() {
        setAdapter();
        setListSelection(1);

        TextView tv = (TextView) mListView.getSelectedView();
        assertFalse(mListView.showContextMenuForChild(tv));

        // TODO: how to show the contextMenu success
    }

    @Test
    public void testPointToPosition() {
        assertEquals(AbsListView.INVALID_POSITION, mListView.pointToPosition(-1, -1));
        assertEquals(AbsListView.INVALID_ROW_ID, mListView.pointToRowId(-1, -1));

        setAdapter();

        View row = mListView.getChildAt(0);
        int rowHeight = row.getHeight();
        int middleOfSecondRow = rowHeight + rowHeight/2;

        int position1 = mListView.pointToPosition(0, 0);
        int position2 = mListView.pointToPosition(50, middleOfSecondRow);

        assertEquals(mCountriesAdapter.getItemId(position1), mListView.pointToRowId(0, 0));
        assertEquals(mCountriesAdapter.getItemId(position2),
                mListView.pointToRowId(50, middleOfSecondRow));

        assertTrue(position2 > position1);
    }

    @Test
    public void testSetRecyclerListener() {
        setAdapter();

        AbsListView.RecyclerListener mockRecyclerListener =
                mock(AbsListView.RecyclerListener.class);
        verifyZeroInteractions(mockRecyclerListener);

        mListView.setRecyclerListener(mockRecyclerListener);
        List<View> views = new ArrayList<>();
        mListView.reclaimViews(views);

        assertTrue(views.size() > 0);

        // Verify that onMovedToScrapHeap was called on each view in the order that they were
        // put in the list that we passed to reclaimViews
        final InOrder reclaimedOrder = inOrder(mockRecyclerListener);
        for (View reclaimed : views) {
            reclaimedOrder.verify(mockRecyclerListener, times(1)).onMovedToScrapHeap(reclaimed);
        }
        verifyNoMoreInteractions(mockRecyclerListener);
    }

    @Test
    public void testAccessCacheColorHint() {
        mListView.setCacheColorHint(Color.RED);
        assertEquals(Color.RED, mListView.getCacheColorHint());
        assertEquals(Color.RED, mListView.getSolidColor());

        mListView.setCacheColorHint(Color.LTGRAY);
        assertEquals(Color.LTGRAY, mListView.getCacheColorHint());
        assertEquals(Color.LTGRAY, mListView.getSolidColor());

        mListView.setCacheColorHint(Color.GRAY);
        assertEquals(Color.GRAY, mListView.getCacheColorHint());
        assertEquals(Color.GRAY, mListView.getSolidColor());
    }

    @Test
    public void testAccessTranscriptMode() {
        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        assertEquals(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL, mListView.getTranscriptMode());

        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
        assertEquals(AbsListView.TRANSCRIPT_MODE_DISABLED, mListView.getTranscriptMode());

        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        assertEquals(AbsListView.TRANSCRIPT_MODE_NORMAL, mListView.getTranscriptMode());
    }

    @Test
    public void testCheckLayoutParams() {
        MyListView listView = new MyListView(mContext);

        AbsListView.LayoutParams param1 = new AbsListView.LayoutParams(10, 10);
        assertTrue(listView.checkLayoutParams(param1));

        ViewGroup.LayoutParams param2 = new ViewGroup.LayoutParams(10, 10);
        assertFalse(listView.checkLayoutParams(param2));
    }

    @Test
    public void testComputeVerticalScrollValues() {
        MyListView listView = new MyListView(mContext);
        assertEquals(0, listView.computeVerticalScrollRange());
        assertEquals(0, listView.computeVerticalScrollOffset());
        assertEquals(0, listView.computeVerticalScrollExtent());

        listView.setAdapter(mCountriesAdapter);
        listView.setSmoothScrollbarEnabled(false);
        assertEquals(mCountriesAdapter.getCount(), listView.computeVerticalScrollRange());
        assertEquals(0, listView.computeVerticalScrollOffset());
        assertEquals(0, listView.computeVerticalScrollExtent());

        listView.setSmoothScrollbarEnabled(true);
        assertEquals(0, listView.computeVerticalScrollOffset());
        assertEquals(0, listView.computeVerticalScrollExtent());
    }

    @Test
    public void testGenerateLayoutParams() throws XmlPullParserException, IOException {
        ViewGroup.LayoutParams res = mListView.generateLayoutParams(mAttributeSet);
        assertNotNull(res);
        assertTrue(res instanceof AbsListView.LayoutParams);

        MyListView listView = new MyListView(mContext);
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        res = listView.generateLayoutParams(p);
        assertNotNull(res);
        assertTrue(res instanceof AbsListView.LayoutParams);
        assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, res.width);
        assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, res.height);
    }

    @Test
    public void testBeforeAndAfterTextChanged() {
        // The java doc says these two methods do nothing
        CharSequence str = "test";
        SpannableStringBuilder sb = new SpannableStringBuilder();

        mListView.beforeTextChanged(str, 0, str.length(), str.length());
        mListView.afterTextChanged(sb);

        // test callback
        MyListView listView = new MyListView(mContext);
        TextView tv = new TextView(mContext);

        assertFalse(listView.isBeforeTextChangedCalled());
        assertFalse(listView.isOnTextChangedCalled());
        assertFalse(listView.isAfterTextChangedCalled());

        tv.addTextChangedListener(listView);
        assertFalse(listView.isBeforeTextChangedCalled());
        assertFalse(listView.isOnTextChangedCalled());
        assertFalse(listView.isAfterTextChangedCalled());

        tv.setText("abc");
        assertTrue(listView.isBeforeTextChangedCalled());
        assertTrue(listView.isOnTextChangedCalled());
        assertTrue(listView.isAfterTextChangedCalled());
    }

    @Test
    public void testAddTouchables() {
        ArrayList<View> views = new ArrayList<>();
        assertEquals(0, views.size());

        setAdapter();

        mListView.addTouchables(views);
        assertEquals(mListView.getChildCount(), views.size());
    }

    @Test
    public void testInvalidateViews() {
        final Activity activity = mActivityRule.getActivity();
        TextView tv1 = (TextView) activity.findViewById(R.id.headerview1);
        TextView tv2 = (TextView) activity.findViewById(R.id.footerview1);

        setAdapter();

        mListView.setScrollIndicators(tv1, tv2);

        mInstrumentation.runOnMainSync(() -> mListView.invalidateViews());
        mInstrumentation.waitForIdleSync();
    }

    @Test
    public void testGetContextMenuInfo() {
        final MyListView listView = new MyListView(mContext, mAttributeSet);

        mInstrumentation.runOnMainSync(() ->  {
            mActivityRule.getActivity().setContentView(listView);
            listView.setAdapter(mCountriesAdapter);
            listView.setSelection(2);
        });
        mInstrumentation.waitForIdleSync();

        final TextView v = (TextView) listView.getSelectedView();
        assertNull(listView.getContextMenuInfo());

        final AbsListView.OnItemLongClickListener mockOnItemLongClickListener =
                mock(AbsListView.OnItemLongClickListener.class);
        listView.setOnItemLongClickListener(mockOnItemLongClickListener);

        verifyZeroInteractions(mockOnItemLongClickListener);

        // Create a count down latch and configure it to be counted down when our mock
        // listener is invoked with the expected view
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        doAnswer(new Answer<Void>() {
             @Override
             public Void answer(InvocationOnMock invocation) {
                 countDownLatch.countDown();
                 return null;
             }
         }).when(mockOnItemLongClickListener).onItemLongClick(listView, v, 2,
                listView.getItemIdAtPosition(2));

        mInstrumentation.waitForIdleSync();

        // Now long click our view
        ViewTestUtils.emulateLongClick(mInstrumentation, v, 500);

        // And wait for the latch to be triggered
        try {
            assertTrue(countDownLatch.await(20L, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
        }

        verify(mockOnItemLongClickListener, times(1)).onItemLongClick(listView, v, 2,
                listView.getItemIdAtPosition(2));

        ContextMenuInfo cmi = listView.getContextMenuInfo();
        assertNotNull(cmi);
    }

    @Test
    public void testGetTopBottomFadingEdgeStrength() {
        MyListView listView = new MyListView(mContext);

        assertEquals(0.0f, listView.getTopFadingEdgeStrength(), DELTA);
        assertEquals(0.0f, listView.getBottomFadingEdgeStrength(), DELTA);
    }

    @Test
    public void testHandleDataChanged() {
        MyListView listView = new MyListView(mContext, mAttributeSet, 0);
        listView.handleDataChanged();
        // TODO: how to check?
    }

    @Test
    public void testSetFilterText() {
        MyListView listView = new MyListView(mContext, mAttributeSet, 0);
        String filterText = "xyz";

        assertFalse(listView.isTextFilterEnabled());
        assertFalse(listView.hasTextFilter());
        assertFalse(listView.isInFilterMode());
        assertTrue(mListView.checkInputConnectionProxy(null));

        listView.setTextFilterEnabled(false);
        listView.setFilterText(filterText);
        assertFalse(listView.isTextFilterEnabled());
        assertFalse(listView.hasTextFilter());
        assertFalse(listView.isInFilterMode());

        listView.setTextFilterEnabled(true);
        listView.setFilterText(null);
        assertTrue(listView.isTextFilterEnabled());
        assertFalse(listView.hasTextFilter());
        assertFalse(listView.isInFilterMode());

        listView.setTextFilterEnabled(true);
        listView.setFilterText(filterText);
        assertTrue(listView.isTextFilterEnabled());
        assertTrue(listView.hasTextFilter());
        assertTrue(listView.isInFilterMode());

        listView.clearTextFilter();
        assertTrue(listView.isTextFilterEnabled());
        assertFalse(listView.hasTextFilter());
        assertFalse(listView.isInFilterMode());
    }

    @MediumTest
    @Test
    public void testSetItemChecked_multipleModeSameValue() {
        // Calling setItemChecked with the same value in multiple choice mode should not cause
        // requestLayout
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(0, false));
        mInstrumentation.waitForIdleSync();
        assertFalse(mListView.isLayoutRequested());
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(0, false));
        assertFalse(mListView.isLayoutRequested());
    }

    @MediumTest
    @Test
    public void testSetItemChecked_singleModeSameValue() {
        // Calling setItemChecked with the same value in single choice mode should not cause
        // requestLayout
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(0, false));
        mInstrumentation.waitForIdleSync();
        assertFalse(mListView.isLayoutRequested());
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(0, false));
        assertFalse(mListView.isLayoutRequested());
    }

    @MediumTest
    @Test
    public void testSetItemChecked_multipleModeDifferentValue() {
        // Calling setItemChecked with a different value in multiple choice mode should cause
        // requestLayout
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(0, false));
        mInstrumentation.waitForIdleSync();
        assertFalse(mListView.isLayoutRequested());
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(0, true));
        assertTrue(mListView.isLayoutRequested());
    }

    @MediumTest
    @Test
    public void testSetItemChecked_singleModeDifferentValue() {
        // Calling setItemChecked with a different value in single choice mode should cause
        // requestLayout
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(0, false));
        mInstrumentation.waitForIdleSync();
        assertFalse(mListView.isLayoutRequested());
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(0, true));
        assertTrue(mListView.isLayoutRequested());
    }

    @LargeTest
    @Test
    public void testTextFilter() {
        setAdapter();

        // Default state - no text filter
        assertFalse(mListView.isTextFilterEnabled());
        assertFalse(mListView.hasTextFilter());
        assertTrue(TextUtils.isEmpty(mListView.getTextFilter()));

        // Enable text filter and verify that while it's enabled, the filtering is
        // still no on
        mInstrumentation.runOnMainSync(() -> mListView.setTextFilterEnabled(true));
        assertTrue(mListView.isTextFilterEnabled());
        assertFalse(mListView.hasTextFilter());
        assertTrue(TextUtils.isEmpty(mListView.getTextFilter()));

        // Verify the initial content of the list
        assertEquals(COUNTRY_LIST.length, mListView.getCount());

        // Set text filter to A - we expect four entries to be left displayed in the list
        mInstrumentation.runOnMainSync(() -> mListView.setFilterText("A"));
        PollingCheck.waitFor(() -> mListView.getCount() == 4);
        assertTrue(mListView.isTextFilterEnabled());
        assertTrue(mListView.hasTextFilter());
        assertTrue(TextUtils.equals("A", mListView.getTextFilter()));

        // Set text filter to Ar - we expect three entries to be left displayed in the list
        mInstrumentation.runOnMainSync(() -> mListView.setFilterText("Ar"));
        PollingCheck.waitFor(() -> mListView.getCount() == 3);
        assertTrue(mListView.isTextFilterEnabled());
        assertTrue(mListView.hasTextFilter());
        assertTrue(TextUtils.equals("Ar", mListView.getTextFilter()));

        // Clear text filter - we expect to go back to the initial content
        mInstrumentation.runOnMainSync(() -> mListView.clearTextFilter());
        PollingCheck.waitFor(() -> mListView.getCount() == COUNTRY_LIST.length);
        assertTrue(mListView.isTextFilterEnabled());
        assertFalse(mListView.hasTextFilter());
        assertTrue(TextUtils.isEmpty(mListView.getTextFilter()));

        // Set text filter to Be - we expect four entries to be left displayed in the list
        mInstrumentation.runOnMainSync(() -> mListView.setFilterText("Be"));
        PollingCheck.waitFor(() -> mListView.getCount() == 4);
        assertTrue(mListView.isTextFilterEnabled());
        assertTrue(mListView.hasTextFilter());
        assertTrue(TextUtils.equals("Be", mListView.getTextFilter()));

        // Set text filter to Q - we no entries displayed in the list
        mInstrumentation.runOnMainSync(() -> mListView.setFilterText("Q"));
        PollingCheck.waitFor(() -> mListView.getCount() == 0);
        assertTrue(mListView.isTextFilterEnabled());
        assertTrue(mListView.hasTextFilter());
        assertTrue(TextUtils.equals("Q", mListView.getTextFilter()));
    }

    @Test
    public void testOnFilterComplete() {
        // Note that we're not using spy() due to Mockito not being able to spy on ListView,
        // at least yet.
        final MyListView listView = new MyListView(mContext, mAttributeSet);

        mInstrumentation.runOnMainSync(() -> {
            mActivityRule.getActivity().setContentView(listView);
            listView.setAdapter(mCountriesAdapter);
            listView.setTextFilterEnabled(true);
        });
        mInstrumentation.waitForIdleSync();

        // Set text filter to A - we expect four entries to be left displayed in the list
        mInstrumentation.runOnMainSync(() -> listView.setFilterText("A"));
        PollingCheck.waitFor(() -> listView.getCount() == 4);
        assertTrue(listView.isTextFilterEnabled());
        assertTrue(listView.hasTextFilter());
        assertTrue(TextUtils.equals("A", listView.getTextFilter()));

        assertEquals(4, listView.getOnFilterCompleteCount());
    }

    private static class PositionArrayAdapter<T> extends ArrayAdapter<T> {
        public PositionArrayAdapter(Context context, int resource, List<T> objects) {
            super(context, resource, objects);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private void verifyCheckedState(final long[] expectedCheckedItems) {
        TestUtils.assertIdentical(expectedCheckedItems, mListView.getCheckedItemIds());

        assertEquals(expectedCheckedItems.length, mListView.getCheckedItemCount());

        final long expectedCheckedItemPosition =
                (mListView.getChoiceMode() == AbsListView.CHOICE_MODE_SINGLE) &&
                        (expectedCheckedItems.length == 1)
                        ? expectedCheckedItems[0]
                        : AbsListView.INVALID_POSITION;
        assertEquals(expectedCheckedItemPosition, mListView.getCheckedItemPosition());

        // Note that getCheckedItemPositions doesn't have a guarantee that it only holds
        // true values, which is why we're not doing the size() == 0 check even in the initial
        // state
        TestUtils.assertTrueValuesAtPositions(
                expectedCheckedItems, mListView.getCheckedItemPositions());
    }

    @MediumTest
    @Test
    public void testCheckedItemsUnderNoneChoiceMode() {
        final ArrayList<String> items = new ArrayList<>(Arrays.asList(COUNTRY_LIST));
        final ArrayAdapter<String> adapter = new PositionArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, items);
        WidgetTestUtils.runOnMainAndDrawSync(mInstrumentation, mListView,
                () -> mListView.setAdapter(adapter));

        mInstrumentation.runOnMainSync(
                () -> mListView.setChoiceMode(AbsListView.CHOICE_MODE_NONE));
        verifyCheckedState(new long[] {});

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(2, true));
        verifyCheckedState(new long[] {});

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(4, true));
        verifyCheckedState(new long[] {});

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(2, false));
        verifyCheckedState(new long[] {});

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(4, false));
        verifyCheckedState(new long[] {});
    }

    @MediumTest
    @Test
    public void testCheckedItemsUnderSingleChoiceMode() {
        final ArrayList<String> items = new ArrayList<>(Arrays.asList(COUNTRY_LIST));
        final ArrayAdapter<String> adapter = new PositionArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, items);
        WidgetTestUtils.runOnMainAndDrawSync(mInstrumentation, mListView,
                () -> mListView.setAdapter(adapter));

        mInstrumentation.runOnMainSync(
                () -> mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE));
        verifyCheckedState(new long[] {});

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(2, true));
        verifyCheckedState(new long[] { 2 });

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(4, true));
        verifyCheckedState(new long[] { 4 });

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(2, false));
        verifyCheckedState(new long[] { 4 });

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(4, false));
        verifyCheckedState(new long[] {});
    }

    @MediumTest
    @Test
    public void testCheckedItemsUnderMultipleChoiceMode() {
        final ArrayList<String> items = new ArrayList<>(Arrays.asList(COUNTRY_LIST));
        final ArrayAdapter<String> adapter = new PositionArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, items);
        WidgetTestUtils.runOnMainAndDrawSync(mInstrumentation, mListView,
                () -> mListView.setAdapter(adapter));

        mInstrumentation.runOnMainSync(
                () -> mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE));
        verifyCheckedState(new long[] {});

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(2, true));
        verifyCheckedState(new long[] { 2 });

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(4, true));
        verifyCheckedState(new long[] { 2, 4 });

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(2, false));
        verifyCheckedState(new long[] { 4 });

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(4, false));
        verifyCheckedState(new long[] {});
    }

    @MediumTest
    @Test
    public void testCheckedItemsUnderMultipleModalChoiceMode() {
        final ArrayList<String> items = new ArrayList<>(Arrays.asList(COUNTRY_LIST));
        final ArrayAdapter<String> adapter = new PositionArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, items);
        WidgetTestUtils.runOnMainAndDrawSync(mInstrumentation, mListView,
                () -> mListView.setAdapter(adapter));

        // Configure a multi-choice mode listener to configure our test contextual action bar
        // content. We will subsequently query that listener for calls to its
        // onItemCheckedStateChanged method
        final AbsListView.MultiChoiceModeListener mockMultiChoiceModeListener =
                mock(AbsListView.MultiChoiceModeListener.class);
        doAnswer((InvocationOnMock invocation) -> {
            final ActionMode actionMode = (ActionMode) invocation.getArguments() [0];
            final Menu menu = (Menu) invocation.getArguments() [1];
            actionMode.getMenuInflater().inflate(R.menu.cab_menu, menu);
            return true;
        }).when(mockMultiChoiceModeListener).onCreateActionMode(
                any(ActionMode.class), any(Menu.class));
        mListView.setMultiChoiceModeListener(mockMultiChoiceModeListener);

        mInstrumentation.runOnMainSync(
                () -> mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL));
        verifyCheckedState(new long[] {});

        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(2, true));
        verifyCheckedState(new long[] { 2 });
        verify(mockMultiChoiceModeListener, times(1)).onItemCheckedStateChanged(
                any(ActionMode.class), eq(2), eq(2L), eq(true));

        reset(mockMultiChoiceModeListener);
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(4, true));
        verifyCheckedState(new long[] { 2, 4 });
        verify(mockMultiChoiceModeListener, times(1)).onItemCheckedStateChanged(
                any(ActionMode.class), eq(4), eq(4L), eq(true));

        reset(mockMultiChoiceModeListener);
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(2, false));
        verifyCheckedState(new long[] { 4 });
        verify(mockMultiChoiceModeListener, times(1)).onItemCheckedStateChanged(
                any(ActionMode.class), eq(2), eq(2L), eq(false));

        reset(mockMultiChoiceModeListener);
        mInstrumentation.runOnMainSync(() -> mListView.setItemChecked(4, false));
        verifyCheckedState(new long[] {});
        mListView.setMultiChoiceModeListener(mockMultiChoiceModeListener);
        verify(mockMultiChoiceModeListener, times(1)).onItemCheckedStateChanged(
                any(ActionMode.class), eq(4), eq(4L), eq(false));
    }

    /**
     * MyListView for test.
     */
    private static class MyListView extends ListView {
        public MyListView(Context context) {
            super(context);
        }

        public MyListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
            return super.checkLayoutParams(p);
        }

        @Override
        protected int computeVerticalScrollExtent() {
            return super.computeVerticalScrollExtent();
        }

        @Override
        protected int computeVerticalScrollOffset() {
            return super.computeVerticalScrollOffset();
        }

        @Override
        protected int computeVerticalScrollRange() {
            return super.computeVerticalScrollRange();
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
        }

        @Override
        protected void dispatchSetPressed(boolean pressed) {
            super.dispatchSetPressed(pressed);
        }

        @Override
        protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
            return super.generateLayoutParams(p);
        }

        @Override
        protected float getBottomFadingEdgeStrength() {
            return super.getBottomFadingEdgeStrength();
        }

        @Override
        protected ContextMenuInfo getContextMenuInfo() {
            return super.getContextMenuInfo();
        }

        @Override
        protected float getTopFadingEdgeStrength() {
            return super.getTopFadingEdgeStrength();
        }

        @Override
        protected void handleDataChanged() {
            super.handleDataChanged();
        }

        @Override
        protected boolean isInFilterMode() {
            return super.isInFilterMode();
        }

        private boolean mIsBeforeTextChangedCalled;
        private boolean mIsOnTextChangedCalled;
        private boolean mIsAfterTextChangedCalled;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mIsBeforeTextChangedCalled = true;
            super.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mIsOnTextChangedCalled = true;
            super.onTextChanged(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            mIsAfterTextChangedCalled = true;
            super.afterTextChanged(s);
        }

        public boolean isBeforeTextChangedCalled() {
            return mIsBeforeTextChangedCalled;
        }

        public boolean isOnTextChangedCalled() {
            return mIsOnTextChangedCalled;
        }

        public boolean isAfterTextChangedCalled() {
            return mIsAfterTextChangedCalled;
        }

        private int mOnFilterCompleteCount = -1;

        @Override
        public void onFilterComplete(int count) {
            super.onFilterComplete(count);
            mOnFilterCompleteCount = count;
        }

        public int getOnFilterCompleteCount() {
            return mOnFilterCompleteCount;
        }
    }
}

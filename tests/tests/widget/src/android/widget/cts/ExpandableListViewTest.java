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

import static org.mockito.Mockito.*;

import android.app.Instrumentation;
import android.content.Context;
import android.cts.util.PollingCheck;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.cts.util.ExpandableListScenario;

import org.xmlpull.v1.XmlPullParser;

@MediumTest
public class ExpandableListViewTest extends ActivityInstrumentationTestCase2<ExpandableList> {
    private Instrumentation mInstrumentation;
    private ExpandableListScenario mActivity;
    private ExpandableListView mExpandableListView;

    public ExpandableListViewTest() {
        super(ExpandableList.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mInstrumentation = getInstrumentation();
        mActivity = getActivity();
        PollingCheck.waitFor(() -> mActivity.hasWindowFocus());
        mExpandableListView = mActivity.getExpandableListView();
    }

    public void testConstructor() {
        new ExpandableListView(mActivity);

        new ExpandableListView(mActivity, null);

        new ExpandableListView(mActivity, null, android.R.attr.expandableListViewStyle);

        new ExpandableListView(mActivity, null, 0,
                android.R.style.Widget_DeviceDefault_ExpandableListView);

        new ExpandableListView(mActivity, null, 0,
                android.R.style.Widget_DeviceDefault_Light_ExpandableListView);

        new ExpandableListView(mActivity, null, 0,
                android.R.style.Widget_Material_ExpandableListView);

        new ExpandableListView(mActivity, null, 0,
                android.R.style.Widget_Material_Light_ExpandableListView);

        XmlPullParser parser =
                mActivity.getResources().getXml(R.layout.expandablelistview_layout);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        new ExpandableListView(mActivity, attrs);
        new ExpandableListView(mActivity, attrs, 0);

        try {
            new ExpandableListView(null);
            fail("should throw NullPointerException.");
        } catch (NullPointerException e) {
        }

        try {
            new ExpandableListView(null, null);
            fail("should throw NullPointerException.");
        } catch (NullPointerException e) {
        }

        try {
            new ExpandableListView(null, null, 0);
            fail("should throw NullPointerException.");
        } catch (NullPointerException e) {
        }
    }

    public void testSetChildDivider() {
        Drawable drawable = mActivity.getResources().getDrawable(R.drawable.scenery);
        mExpandableListView.setChildDivider(drawable);
    }

    public void testSetAdapter() {
        try {
            mExpandableListView.setAdapter((ListAdapter) null);
            fail("setAdapter(ListAdapter) should throw RuntimeException here.");
        } catch (RuntimeException e) {
        }
    }

    @UiThreadTest
    public void testGetAdapter() {
        assertNull(mExpandableListView.getAdapter());

        ExpandableListAdapter expandableAdapter = new MockExpandableListAdapter();
        mExpandableListView.setAdapter(expandableAdapter);
        assertNotNull(mExpandableListView.getAdapter());
    }

    @UiThreadTest
    public void testAccessExpandableListAdapter() {
        ExpandableListAdapter expandableAdapter = new MockExpandableListAdapter();

        assertNull(mExpandableListView.getExpandableListAdapter());
        mExpandableListView.setAdapter(expandableAdapter);
        assertSame(expandableAdapter, mExpandableListView.getExpandableListAdapter());
    }

    @UiThreadTest
    public void testPerformItemClick() {
        assertFalse(mExpandableListView.performItemClick(null, 100, 99));

        ExpandableListView.OnItemClickListener mockOnItemClickListener =
                mock(ExpandableListView.OnItemClickListener.class);
        mExpandableListView.setOnItemClickListener(mockOnItemClickListener);
        assertTrue(mExpandableListView.performItemClick(null, 100, 99));
        verify(mockOnItemClickListener, times(1)).onItemClick(eq(mExpandableListView),
                any(View.class), eq(100), eq(99L));
    }

    public void testSetOnItemClickListener() {
        ExpandableListView.OnItemClickListener mockOnItemClickListener =
                mock(ExpandableListView.OnItemClickListener.class);

        assertNull(mExpandableListView.getOnItemClickListener());
        mExpandableListView.setOnItemClickListener(mockOnItemClickListener);
        assertSame(mockOnItemClickListener, mExpandableListView.getOnItemClickListener());
    }

    @UiThreadTest
    public void testExpandGroup() {
        ExpandableListAdapter expandableAdapter = new MockExpandableListAdapter();
        mExpandableListView.setAdapter(expandableAdapter);

        ExpandableListView.OnGroupExpandListener mockOnGroupExpandListener =
                mock(ExpandableListView.OnGroupExpandListener.class);
        mExpandableListView.setOnGroupExpandListener(mockOnGroupExpandListener);

        verifyZeroInteractions(mockOnGroupExpandListener);

        assertTrue(mExpandableListView.expandGroup(0));
        verify(mockOnGroupExpandListener, times(1)).onGroupExpand(0);
        assertTrue(mExpandableListView.isGroupExpanded(0));

        reset(mockOnGroupExpandListener);
        assertFalse(mExpandableListView.expandGroup(0));
        verify(mockOnGroupExpandListener, times(1)).onGroupExpand(0);
        assertTrue(mExpandableListView.isGroupExpanded(0));

        reset(mockOnGroupExpandListener);
        assertTrue(mExpandableListView.expandGroup(1));
        verify(mockOnGroupExpandListener, times(1)).onGroupExpand(1);
        assertTrue(mExpandableListView.isGroupExpanded(1));

        reset(mockOnGroupExpandListener);
        assertFalse(mExpandableListView.expandGroup(1));
        verify(mockOnGroupExpandListener, times(1)).onGroupExpand(1);
        assertTrue(mExpandableListView.isGroupExpanded(1));

        reset(mockOnGroupExpandListener);
        mExpandableListView.setAdapter((ExpandableListAdapter) null);
        try {
            mExpandableListView.expandGroup(0);
            fail("should throw NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void testExpandGroupSmooth() {
        mInstrumentation.runOnMainSync(
                () -> mExpandableListView.setAdapter(new MockExpandableListAdapter()));

        ExpandableListView.OnGroupExpandListener mockOnGroupExpandListener =
                mock(ExpandableListView.OnGroupExpandListener.class);
        mExpandableListView.setOnGroupExpandListener(mockOnGroupExpandListener);

        verifyZeroInteractions(mockOnGroupExpandListener);
        mInstrumentation.runOnMainSync(() -> assertTrue(mExpandableListView.expandGroup(0, true)));
        mInstrumentation.waitForIdleSync();
        verify(mockOnGroupExpandListener, times(1)).onGroupExpand(0);
        assertTrue(mExpandableListView.isGroupExpanded(0));

        reset(mockOnGroupExpandListener);
        mInstrumentation.runOnMainSync(() -> assertFalse(mExpandableListView.expandGroup(0, true)));
        mInstrumentation.waitForIdleSync();
        verify(mockOnGroupExpandListener, times(1)).onGroupExpand(0);
        assertTrue(mExpandableListView.isGroupExpanded(0));

        reset(mockOnGroupExpandListener);
        mInstrumentation.runOnMainSync(() -> assertTrue(mExpandableListView.expandGroup(1, true)));
        mInstrumentation.waitForIdleSync();
        verify(mockOnGroupExpandListener, times(1)).onGroupExpand(1);
        assertTrue(mExpandableListView.isGroupExpanded(1));

        reset(mockOnGroupExpandListener);
        mInstrumentation.runOnMainSync(() -> assertFalse(mExpandableListView.expandGroup(1, true)));
        mInstrumentation.waitForIdleSync();
        verify(mockOnGroupExpandListener, times(1)).onGroupExpand(1);
        assertTrue(mExpandableListView.isGroupExpanded(1));

        reset(mockOnGroupExpandListener);
        mInstrumentation.runOnMainSync(() -> {
            mExpandableListView.setAdapter((ExpandableListAdapter) null);
            try {
                mExpandableListView.expandGroup(0);
                fail("should throw NullPointerException");
            } catch (NullPointerException e) {
            }
        });
    }

    @UiThreadTest
    public void testCollapseGroup() {
        ExpandableListAdapter expandableAdapter = new MockExpandableListAdapter();
        mExpandableListView.setAdapter(expandableAdapter);

        ExpandableListView.OnGroupCollapseListener mockOnGroupCollapseListener =
                mock(ExpandableListView.OnGroupCollapseListener.class);
        mExpandableListView.setOnGroupCollapseListener(mockOnGroupCollapseListener);

        verifyZeroInteractions(mockOnGroupCollapseListener);
        assertFalse(mExpandableListView.collapseGroup(0));
        verify(mockOnGroupCollapseListener, times(1)).onGroupCollapse(0);
        assertFalse(mExpandableListView.isGroupExpanded(0));

        reset(mockOnGroupCollapseListener);
        mExpandableListView.expandGroup(0);
        assertTrue(mExpandableListView.collapseGroup(0));
        verify(mockOnGroupCollapseListener, times(1)).onGroupCollapse(0);
        assertFalse(mExpandableListView.isGroupExpanded(0));

        reset(mockOnGroupCollapseListener);
        assertFalse(mExpandableListView.collapseGroup(1));
        verify(mockOnGroupCollapseListener, times(1)).onGroupCollapse(1);
        assertFalse(mExpandableListView.isGroupExpanded(1));

        reset(mockOnGroupCollapseListener);
        mExpandableListView.setAdapter((ExpandableListAdapter) null);
        try {
            mExpandableListView.collapseGroup(0);
            fail("should throw NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @UiThreadTest
    public void testSetOnGroupClickListener() {
        mExpandableListView.setAdapter(new MockExpandableListAdapter());

        ExpandableListView.OnGroupClickListener mockOnGroupClickListener =
                mock(ExpandableListView.OnGroupClickListener.class);

        mExpandableListView.setOnGroupClickListener(mockOnGroupClickListener);
        verifyZeroInteractions(mockOnGroupClickListener);

        mExpandableListView.performItemClick(null, 0, 0);
        verify(mockOnGroupClickListener, times(1)).onGroupClick(eq(mExpandableListView),
                any(View.class), eq(0), eq(0L));
    }

    @UiThreadTest
    public void testSetOnChildClickListener() {
        mExpandableListView.setAdapter(new MockExpandableListAdapter());

        ExpandableListView.OnChildClickListener mockOnChildClickListener =
                mock(ExpandableListView.OnChildClickListener.class);

        mExpandableListView.setOnChildClickListener(mockOnChildClickListener);
        verifyZeroInteractions(mockOnChildClickListener);

        // first let the list expand
        mExpandableListView.expandGroup(0);
        // click on the child list of the first group
        mExpandableListView.performItemClick(null, 1, 0);
        verify(mockOnChildClickListener, times(1)).onChildClick(eq(mExpandableListView),
                any(View.class), eq(0), eq(0), eq(0L));
    }

    @UiThreadTest
    public void testGetExpandableListPosition() {
        mExpandableListView.setAdapter(new MockExpandableListAdapter());

        assertEquals(0, mExpandableListView.getExpandableListPosition(0));

        // Group 0 is not expanded, position 1 is invalid
        assertEquals(ExpandableListView.PACKED_POSITION_VALUE_NULL,
                mExpandableListView.getExpandableListPosition(1));

        // Position 1 becomes valid when group 0 is expanded
        mExpandableListView.expandGroup(0);
        assertEquals(ExpandableListView.getPackedPositionForChild(0, 0),
                mExpandableListView.getExpandableListPosition(1));

        // Position 2 is still invalid (only one child).
        assertEquals(ExpandableListView.PACKED_POSITION_VALUE_NULL,
                mExpandableListView.getExpandableListPosition(2));
    }

    @UiThreadTest
    public void testGetFlatListPosition() {
        mExpandableListView.setAdapter(new MockExpandableListAdapter());

        try {
            mExpandableListView.getFlatListPosition(ExpandableListView.PACKED_POSITION_VALUE_NULL);
        } catch (NullPointerException e) {
        }
        assertEquals(0, mExpandableListView.getFlatListPosition(
                ExpandableListView.PACKED_POSITION_TYPE_CHILD<<32));
        // 0x8000000100000000L means this is a child and group position is 1.
        assertEquals(1, mExpandableListView.getFlatListPosition(0x8000000100000000L));
    }

    @UiThreadTest
    public void testGetSelectedPosition() {
        assertEquals(ExpandableListView.PACKED_POSITION_VALUE_NULL,
                mExpandableListView.getSelectedPosition());

        mExpandableListView.setAdapter(new MockExpandableListAdapter());

        mExpandableListView.setSelectedGroup(0);
        assertEquals(0, mExpandableListView.getSelectedPosition());

        mExpandableListView.setSelectedGroup(1);
        assertEquals(0, mExpandableListView.getSelectedPosition());
    }

    @UiThreadTest
    public void testGetSelectedId() {
        assertEquals(-1, mExpandableListView.getSelectedId());
        mExpandableListView.setAdapter(new MockExpandableListAdapter());

        mExpandableListView.setSelectedGroup(0);
        assertEquals(0, mExpandableListView.getSelectedId());

        mExpandableListView.setSelectedGroup(1);
        assertEquals(0, mExpandableListView.getSelectedId());
    }

    @UiThreadTest
    public void testSetSelectedGroup() {
        mExpandableListView.setAdapter(new MockExpandableListAdapter());

        mExpandableListView.setSelectedGroup(0);
        assertEquals(0, mExpandableListView.getSelectedPosition());

        mExpandableListView.setSelectedGroup(1);
        assertEquals(0, mExpandableListView.getSelectedPosition());
    }

    @UiThreadTest
    public void testSetSelectedChild() {
        mExpandableListView.setAdapter(new MockExpandableListAdapter());

        assertTrue(mExpandableListView.setSelectedChild(0, 0, false));
        assertTrue(mExpandableListView.setSelectedChild(0, 1, true));
    }

    @UiThreadTest
    public void testIsGroupExpanded() {
        mExpandableListView.setAdapter(new MockExpandableListAdapter());

        mExpandableListView.expandGroup(1);
        assertFalse(mExpandableListView.isGroupExpanded(0));
        assertTrue(mExpandableListView.isGroupExpanded(1));
    }

    public void testGetPackedPositionType() {
        assertEquals(ExpandableListView.PACKED_POSITION_TYPE_NULL,
                ExpandableListView.getPackedPositionType(
                        ExpandableListView.PACKED_POSITION_VALUE_NULL));

        assertEquals(ExpandableListView.PACKED_POSITION_TYPE_GROUP,
                ExpandableListView.getPackedPositionType(0));

        // 0x8000000000000000L is PACKED_POSITION_MASK_TYPE, but it is private,
        // so we just use its value.
        assertEquals(ExpandableListView.PACKED_POSITION_TYPE_CHILD,
                ExpandableListView.getPackedPositionType(0x8000000000000000L));
    }

    public void testGetPackedPositionGroup() {
        assertEquals(-1, ExpandableListView.getPackedPositionGroup(
                ExpandableListView.PACKED_POSITION_VALUE_NULL));

        assertEquals(0, ExpandableListView.getPackedPositionGroup(0));

        // 0x123400000000L means its group position is 0x1234
        assertEquals(0x1234, ExpandableListView.getPackedPositionGroup(0x123400000000L));

        // 0x7FFFFFFF00000000L means its group position is 0x7FFFFFFF
        assertEquals(0x7FFFFFFF, ExpandableListView.getPackedPositionGroup(0x7FFFFFFF00000000L));
    }

    public void testGetPackedPositionChild() {
        assertEquals(-1, ExpandableListView.getPackedPositionChild(
                ExpandableListView.PACKED_POSITION_VALUE_NULL));

        assertEquals(-1, ExpandableListView.getPackedPositionChild(1));

        // 0x8000000000000000L means its child position is 0
        assertEquals(0, ExpandableListView.getPackedPositionChild(0x8000000000000000L));

        // 0x80000000ffffffffL means its child position is 0xffffffff
        assertEquals(0xffffffff, ExpandableListView.getPackedPositionChild(0x80000000ffffffffL));
    }

    public void testGetPackedPositionForChild() {
        assertEquals(0x8000000000000000L,
                ExpandableListView.getPackedPositionForChild(0, 0));

        assertEquals(0xffffffffffffffffL,
                ExpandableListView.getPackedPositionForChild(Integer.MAX_VALUE, 0xffffffff));
    }

    public void testGetPackedPositionForGroup() {
        assertEquals(0, ExpandableListView.getPackedPositionForGroup(0));

        assertEquals(0x7fffffff00000000L,
                ExpandableListView.getPackedPositionForGroup(Integer.MAX_VALUE));
    }

    public void testSetChildIndicator() {
        mExpandableListView.setChildIndicator(null);
    }

    public void testSetChildIndicatorBounds() {
        mExpandableListView.setChildIndicatorBounds(10, 20);
    }

    public void testSetChildIndicatorBoundsRelative() {
        mExpandableListView.setChildIndicatorBoundsRelative(10, 20);
    }

    public void testSetGroupIndicator() {
        Drawable drawable = new BitmapDrawable();
        mExpandableListView.setGroupIndicator(drawable);
    }

    public void testSetIndicatorBounds() {
        mExpandableListView.setIndicatorBounds(10, 30);
    }

    public void testSetIndicatorBoundsRelative() {
        mExpandableListView.setIndicatorBoundsRelative(10, 30);
    }

    public void testOnSaveInstanceState() {
        ExpandableListView src = new ExpandableListView(mActivity);
        Parcelable p1 = src.onSaveInstanceState();

        ExpandableListView dest = new ExpandableListView(mActivity);
        dest.onRestoreInstanceState(p1);
        Parcelable p2 = dest.onSaveInstanceState();

        assertNotNull(p1);
        assertNotNull(p2);
    }

    public void testDispatchDraw() {
        MockExpandableListView expandableListView = new MockExpandableListView(mActivity);
        expandableListView.dispatchDraw(new Canvas());
    }

    private class MockExpandableListAdapter implements ExpandableListAdapter {
        private final LayoutInflater mLayoutInflater;

        public MockExpandableListAdapter() {
            mLayoutInflater = LayoutInflater.from(mActivity);
        }

        public void registerDataSetObserver(DataSetObserver observer) {
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

        public int getGroupCount() {
            return 1;
        }

        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
            case 0:
                return 1;
            default:
                return 0;
            }
        }

        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
            case 0:
                return "Data";
            default:
                return null;
            }
        }

        public Object getChild(int groupPosition, int childPosition) {
            if (groupPosition == 0 && childPosition == 0)
                return "child data";
            else
                return null;
        }

        public long getGroupId(int groupPosition) {
            return 0;
        }

        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        public boolean hasStableIds() {
            return true;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {
            TextView result = (TextView) convertView;
            if (result == null) {
                result = (TextView) mLayoutInflater.inflate(
                        R.layout.expandablelistview_group, parent, false);
            }
            result.setText("Group " + groupPosition);
            return result;
        }

        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
            TextView result = (TextView) convertView;
            if (result == null) {
                result = (TextView) mLayoutInflater.inflate(
                        R.layout.expandablelistview_child, parent, false);
            }
            result.setText("Child " + childPosition);
            return result;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean areAllItemsEnabled() {
            return true;
        }

        public boolean isEmpty() {
            return true;
        }

        public void onGroupExpanded(int groupPosition) {
        }

        public void onGroupCollapsed(int groupPosition) {
        }

        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        public long getCombinedGroupId(long groupId) {
            return 0;
        }
    }

    private class MockExpandableListView extends ExpandableListView {
        public MockExpandableListView(Context context) {
            super(context);
        }

        public MockExpandableListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MockExpandableListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
        }
    }
}

/*
 * Copyright (C) 2008 Esmertec AG
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

import android.test.AndroidTestCase;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ListView;

/**
 * Test {@link ExpandableListContextMenuInfo}.
 */
public class ExpandableListView_ExpandableListContextMenuInfoTest extends AndroidTestCase {
    public void testConstructor() {
        ListView listview = new ListView(getContext());
        ExpandableListContextMenuInfo expandableListContextMenuInfo =
            new ExpandableListView.ExpandableListContextMenuInfo(listview, 100L, 80L);
        assertNotNull(expandableListContextMenuInfo);
        assertSame(listview, expandableListContextMenuInfo.targetView);
        assertEquals(100L, expandableListContextMenuInfo.packedPosition);
        assertEquals(80L, expandableListContextMenuInfo.id);
    }
}

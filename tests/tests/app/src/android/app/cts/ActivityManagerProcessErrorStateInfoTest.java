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
package android.app.cts;

import android.app.ActivityManager;
import android.os.Parcel;
import android.test.AndroidTestCase;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

@TestTargetClass(ActivityManager.ProcessErrorStateInfo.class)
public class ActivityManagerProcessErrorStateInfoTest extends AndroidTestCase {
    protected ActivityManager.ProcessErrorStateInfo mErrorStateInfo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mErrorStateInfo = new ActivityManager.ProcessErrorStateInfo();
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        notes = "Test constructor",
        method = "ActivityManager.ProcessErrorStateInfo",
        args = {}
    )
    public void testConstructor() {
        new ActivityManager.ProcessErrorStateInfo();
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "describeContents",
        args = {}
    )
    public void testDescribeContents() {
        assertEquals(0, mErrorStateInfo.describeContents());
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "writeToParcel",
        args = {android.os.Parcel.class, int.class}
    )
    public void testWriteToParcel() throws Exception {
        int condition = 1;
        String processName = "processName";
        int pid = 2;
        int uid = 3;
        String tag = "tag";
        String shortMsg = "shortMsg";
        String longMsg = "longMsg";
        byte[] crashData = { 1, 2, 3 };

        mErrorStateInfo.condition = condition;
        mErrorStateInfo.processName = processName;
        mErrorStateInfo.pid = pid;
        mErrorStateInfo.uid = uid;
        mErrorStateInfo.tag = tag;
        mErrorStateInfo.shortMsg = shortMsg;
        mErrorStateInfo.longMsg = longMsg;
        // test crashData is not null
        mErrorStateInfo.crashData = crashData;

        Parcel parcel = Parcel.obtain();
        mErrorStateInfo.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        ActivityManager.ProcessErrorStateInfo values =
            ActivityManager.ProcessErrorStateInfo.CREATOR.createFromParcel(parcel);

        assertEquals(condition, values.condition);
        assertEquals(processName, values.processName);
        assertEquals(pid, values.pid);
        assertEquals(uid, values.uid);
        assertEquals(tag, values.tag);
        // null?
        assertEquals(shortMsg, values.shortMsg);
        assertEquals(longMsg, values.longMsg);
        assertEquals(1, values.crashData[0]);
        assertEquals(2, values.crashData[1]);
        assertEquals(3, values.crashData[2]);
        // test crashData is null
        mErrorStateInfo.crashData = null;
        parcel = Parcel.obtain();
        mErrorStateInfo.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        values = ActivityManager.ProcessErrorStateInfo.CREATOR.createFromParcel(parcel);
        assertNull(values.crashData);

    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        notes = "Test readFromParcel method",
        method = "readFromParcel",
        args = {android.os.Parcel.class}
    )
    public void testReadFromParcel() throws Exception {
        int condition = 1;
        String processName = "processName";
        int pid = 2;
        int uid = 3;
        String tag = "tag";
        String shortMsg = "shortMsg";
        String longMsg = "longMsg";
        byte[] crashData = { 1, 2, 3 };

        mErrorStateInfo.condition = condition;
        mErrorStateInfo.processName = processName;
        mErrorStateInfo.pid = pid;
        mErrorStateInfo.uid = uid;
        mErrorStateInfo.tag = tag;
        mErrorStateInfo.shortMsg = shortMsg;
        mErrorStateInfo.longMsg = longMsg;
        // test crashData is not null
        mErrorStateInfo.crashData = crashData;

        Parcel parcel = Parcel.obtain();
        mErrorStateInfo.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        ActivityManager.ProcessErrorStateInfo values = new ActivityManager.ProcessErrorStateInfo();
        values.readFromParcel(parcel);

        assertEquals(condition, values.condition);
        assertEquals(processName, values.processName);
        assertEquals(pid, values.pid);
        assertEquals(uid, values.uid);
        assertEquals(tag, values.tag);
        assertEquals(shortMsg, values.shortMsg);
        assertEquals(longMsg, values.longMsg);
        assertEquals(1, values.crashData[0]);
        assertEquals(2, values.crashData[1]);
        assertEquals(3, values.crashData[2]);

        // test crashData is null
        mErrorStateInfo.crashData = null;
        parcel = Parcel.obtain();
        mErrorStateInfo.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        values = new ActivityManager.ProcessErrorStateInfo();
        values.readFromParcel(parcel);
        assertNull(values.crashData);
    }

}

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

package android.content.cts;

import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.test.AndroidTestCase;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(MalformedMimeTypeException.class)
public class IntentFilter_MalformedMimeTypeExceptionTest extends
        AndroidTestCase {

    private MalformedMimeTypeException mMalformedMimeTypeException;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMalformedMimeTypeException = null;
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "IntentFilter.MalformedMimeTypeException",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "IntentFilter.MalformedMimeTypeException",
            args = {java.lang.String.class}
        )
    })
    public void testMalformedMimeTypeException() {
        mMalformedMimeTypeException = new IntentFilter.MalformedMimeTypeException();
        assertNotNull(mMalformedMimeTypeException);
        try {
            throw mMalformedMimeTypeException;
        } catch (MalformedMimeTypeException e) {
            // expected
        }
        final String message = "testException";
        mMalformedMimeTypeException = new IntentFilter.MalformedMimeTypeException(
                message);
        assertNotNull(mMalformedMimeTypeException);
        assertEquals(message, mMalformedMimeTypeException.getMessage());

        try {
            throw mMalformedMimeTypeException;
        } catch (MalformedMimeTypeException e) {
            // expected
            assertEquals(message, e.getMessage());
        }
    }

}


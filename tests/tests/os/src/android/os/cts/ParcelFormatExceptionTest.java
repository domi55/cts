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
package android.os.cts;

import junit.framework.TestCase;
import android.os.ParcelFormatException;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(ParcelFormatException.class)
public class ParcelFormatExceptionTest extends TestCase{
    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "test method: ParcelFormatException",
            method = "ParcelFormatException",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "test method: ParcelFormatException",
            method = "ParcelFormatException",
            args = {java.lang.String.class}
        )
    })
    public void testParcelFormatException(){
        ParcelFormatException ne = null;
        boolean isThrowed = false;

        try {
            ne = new ParcelFormatException();
            throw ne;
        } catch (ParcelFormatException e) {
            assertSame(ne, e);
            isThrowed = true;
        } finally {
            if (!isThrowed) {
                fail("should throw out ParcelFormatException");
            }
        }

        isThrowed = false;

        try {
            ne = new ParcelFormatException("ParcelFormatException");
            throw ne;
        } catch (ParcelFormatException e) {
            assertSame(ne, e);
            isThrowed = true;
        } finally {
            if (!isThrowed) {
                fail("should throw out ParcelFormatException");
            }
        }
    }

}

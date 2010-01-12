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

package android.database.cts;

import android.database.StaleDataException;
import android.test.AndroidTestCase;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(android.database.StaleDataException.class)
public class StaleDataExceptionTest extends AndroidTestCase {
    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test constructors of StaleDataException.",
            method = "StaleDataException",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test constructors of StaleDataException.",
            method = "StaleDataException",
            args = {java.lang.String.class}
        )
    })
    public void testConstructors() {
        String expected1 = "Expected exception message";

        // Test StaleDataException()
        try {
            throw new StaleDataException();
        } catch (StaleDataException e) {
            assertNull(e.getMessage());
        }

        // Test StaleDataException(String)
        try {
            throw new StaleDataException(expected1);
        } catch (StaleDataException e) {
            assertEquals(expected1, e.getMessage());
        }
    }
}

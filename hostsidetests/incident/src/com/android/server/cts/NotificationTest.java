/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.android.server.cts;

import android.service.notification.NotificationRecordProto;
import android.service.notification.NotificationServiceDumpProto;
import android.service.notification.State;

/**
 * Test to check that the notification service properly outputs its dump state.
 */
public class NotificationTest extends ProtoDumpTestCase {
    /**
     * Tests that at least one notification is posted, and verify its properties are plausible.
     */
    public void testNotificationRecords() throws Exception {
        final NotificationServiceDumpProto dump = getDump(NotificationServiceDumpProto.parser(),
                "dumpsys notification --proto");

        assertTrue(dump.getRecordsCount() > 0);
        boolean found = false;
        for (NotificationRecordProto record : dump.getRecordsList()) {
            if (record.getKey().contains("android")) {
                found = true;
                assertEquals(State.POSTED, record.getState());
                assertTrue(record.getImportance() > 0 /* NotificationManager.IMPORTANCE_NONE */);
                assertEquals(record.getKey(), record.getGroupKey());

                // Ensure these fields exist, at least
                record.getFlags();
                record.getChannelId();
                record.getSound();
                record.getSoundUsage();
                record.getCanVibrate();
                record.getCanShowLight();
                record.getGroupKey();
            }
        }

        assertTrue(found);
    }
}


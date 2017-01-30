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

package android.app.cts;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Parcel;
import android.test.AndroidTestCase;

public class NotificationChannelTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testDescribeContents() {
        final int expected = 0;
        NotificationChannel channel =
                new NotificationChannel("1", "1", NotificationManager.IMPORTANCE_DEFAULT);
        assertEquals(expected, channel.describeContents());
    }

    public void testConstructor() {
        NotificationChannel channel =
                new NotificationChannel("1", "one", NotificationManager.IMPORTANCE_DEFAULT);
        assertEquals("1", channel.getId());
        assertEquals("one", channel.getName());
        assertEquals(false, channel.canBypassDnd());
        assertEquals(false, channel.shouldShowLights());
        assertEquals(false, channel.shouldVibrate());
        assertEquals(null, channel.getVibrationPattern());
        assertEquals(NotificationManager.IMPORTANCE_DEFAULT, channel.getImportance());
        assertEquals(null, channel.getSound());
        assertTrue(channel.canShowBadge());
        assertEquals(Notification.AUDIO_ATTRIBUTES_DEFAULT, channel.getAudioAttributes());
        assertEquals(null, channel.getGroup());
    }

    public void testWriteToParcel() {
        NotificationChannel channel =
                new NotificationChannel("1", "one", NotificationManager.IMPORTANCE_DEFAULT);
        Parcel parcel = Parcel.obtain();
        channel.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        NotificationChannel channel1 = NotificationChannel.CREATOR.createFromParcel(parcel);
        assertEquals(channel, channel1);
    }

    public void testLights() {
        NotificationChannel channel =
                new NotificationChannel("1", "one", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setLights(true);
        assertTrue(channel.shouldShowLights());
        channel.setLights(false);
        assertFalse(channel.shouldShowLights());
    }

    public void testVibration() {
        NotificationChannel channel =
                new NotificationChannel("1", "one", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        assertTrue(channel.shouldVibrate());
        channel.enableVibration(false);
        assertFalse(channel.shouldVibrate());
    }

    public void testVibrationPattern() {
        final long[] pattern = new long[] {1, 7, 1, 7, 3};
        NotificationChannel channel =
                new NotificationChannel("1", "one", NotificationManager.IMPORTANCE_DEFAULT);
        assertNull(channel.getVibrationPattern());
        channel.setVibrationPattern(pattern);
        assertEquals(pattern, channel.getVibrationPattern());
        channel.enableVibration(true);
        assertTrue(channel.shouldVibrate());
    }

    public void testSound() {
        Uri expected = new Uri.Builder().scheme("fruit").appendQueryParameter("favorite", "bananas")
                .build();
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .build();
        NotificationChannel channel =
                new NotificationChannel("1", "one", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setSound(expected, attributes);
        assertEquals(expected, channel.getSound());
        assertEquals(attributes, channel.getAudioAttributes());
    }

    public void testShowBadge() {
        NotificationChannel channel =
                new NotificationChannel("1", "one", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setShowBadge(true);
        assertTrue(channel.canShowBadge());
    }

    public void testGroup() {
        NotificationChannel channel =
                new NotificationChannel("1", "one", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setGroup("banana");
        assertEquals("banana", channel.getGroup());
    }
}

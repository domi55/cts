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

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony.Threads;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.test.AndroidTestCase;
import android.util.Log;

import android.app.stubs.R;

public class NotificationManagerTest extends AndroidTestCase {
    final String TAG = NotificationManagerTest.class.getSimpleName();
    final boolean DEBUG = false;

    private NotificationManager mNotificationManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mNotificationManager = (NotificationManager) mContext.getSystemService(
                Context.NOTIFICATION_SERVICE);
        // clear the deck so that our getActiveNotifications results are predictable
        mNotificationManager.cancelAll();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mNotificationManager.cancelAll();
    }

    public void testCreateChannel() {
        NotificationChannel channel = new NotificationChannel("id", "name");
        try {
            mNotificationManager.createNotificationChannel(channel);
            assertEquals(channel, mNotificationManager.getNotificationChannel(channel.getId()));
        } finally {
            mNotificationManager.deleteNotificationChannel(channel.getId());
        }
    }

    public void testCreateChannelAlreadyExists() {
        NotificationChannel channel = new NotificationChannel("id", "name");
        try {
            mNotificationManager.createNotificationChannel(channel);
            assertEquals(channel, mNotificationManager.getNotificationChannel(channel.getId()));
            try {
                mNotificationManager.createNotificationChannel(channel);
                fail("Created channel with duplicate id");
            } catch (IllegalArgumentException e) {
                // success
            }
        } finally {
            mNotificationManager.deleteNotificationChannel(channel.getId());
        }
    }

    public void testDeleteChannel() {
        NotificationChannel channel = new NotificationChannel("id", "name");
        mNotificationManager.createNotificationChannel(channel);
        assertEquals(channel, mNotificationManager.getNotificationChannel(channel.getId()));
        mNotificationManager.deleteNotificationChannel(channel.getId());
        assertNull(mNotificationManager.getNotificationChannel(channel.getId()));
    }

    public void testCannotDeleteDefaultChannel() {
        NotificationChannel channel =
                new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID, "name");
        try {
            mNotificationManager.deleteNotificationChannel(channel.getId());
            fail("Deleted default channel");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    public void testGetChannel() {
        NotificationChannel channel1 = new NotificationChannel("id", "name");
        NotificationChannel channel2 = new NotificationChannel("id2", "name2");
        NotificationChannel channel3 = new NotificationChannel("id3", "name3");
        NotificationChannel channel4 = new NotificationChannel("id4", "name4");
        try {
            mNotificationManager.createNotificationChannel(channel1);
            mNotificationManager.createNotificationChannel(channel2);
            mNotificationManager.createNotificationChannel(channel3);
            mNotificationManager.createNotificationChannel(channel4);

            assertEquals(channel2, mNotificationManager.getNotificationChannel(channel2.getId()));
            assertEquals(channel3, mNotificationManager.getNotificationChannel(channel3.getId()));
            assertEquals(channel1, mNotificationManager.getNotificationChannel(channel1.getId()));
            assertEquals(channel4, mNotificationManager.getNotificationChannel(channel4.getId()));
        } finally {
            mNotificationManager.deleteNotificationChannel(channel1.getId());
            mNotificationManager.deleteNotificationChannel(channel2.getId());
            mNotificationManager.deleteNotificationChannel(channel3.getId());
            mNotificationManager.deleteNotificationChannel(channel4.getId());
        }
    }

    public void testUpdateChannel() {
        NotificationChannel channel = new NotificationChannel("id", "name");
        try {
            mNotificationManager.createNotificationChannel(channel);
            channel.setLights(true);
            mNotificationManager.updateNotificationChannel(channel);
            assertEquals(channel, mNotificationManager.getNotificationChannel(channel.getId()));
        } finally {
            mNotificationManager.deleteNotificationChannel(channel.getId());
        }
    }

    public void testUpdateChannelDoesNotExist() {
        try {
            mNotificationManager.updateNotificationChannel(
                    new NotificationChannel("blah", "blahname"));
            fail("Update on non existent channel succeeded");
        } catch (IllegalArgumentException e){
            // pass
        }
    }

    public void testNotify() {
        mNotificationManager.cancelAll();

        final int id = 1;
        sendNotification(id, R.drawable.black);
        // test updating the same notification
        sendNotification(id, R.drawable.blue);
        sendNotification(id, R.drawable.yellow);

        // assume that sendNotification tested to make sure individual notifications were present
        StatusBarNotification[] sbns = mNotificationManager.getActiveNotifications();
        for (StatusBarNotification sbn : sbns) {
            if (sbn.getId() != id) {
                fail("we got back other notifications besides the one we posted: "
                        + sbn.getKey());
            }
        }
    }

    public void testCancel() {
        final int id = 9;
        sendNotification(id, R.drawable.black);
        mNotificationManager.cancel(id);

        if (!checkNotificationExistence(id, /*shouldExist=*/ false)) {
            fail("canceled notification was still alive, id=" + id);
        }
    }

    public void testCancelAll() {
        sendNotification(1, R.drawable.black);
        sendNotification(2, R.drawable.blue);
        sendNotification(3, R.drawable.yellow);

        if (DEBUG) {
            Log.d(TAG, "posted 3 notifications, here they are: ");
            StatusBarNotification[] sbns = mNotificationManager.getActiveNotifications();
            for (StatusBarNotification sbn : sbns) {
                Log.d(TAG, "  " + sbn);
            }
            Log.d(TAG, "about to cancel...");
        }
        mNotificationManager.cancelAll();

        StatusBarNotification[] sbns = mNotificationManager.getActiveNotifications();
        assertTrue("notification list was not empty after cancelAll", sbns.length == 0);
    }

    private void sendNotification(final int id, final int icon) {
        final Intent intent = new Intent(Intent.ACTION_MAIN, Threads.CONTENT_URI);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Intent.ACTION_MAIN);

        final PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        final Notification notification = new Notification.Builder(mContext)
                .setSmallIcon(icon)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("notify#" + id)
                .setContentText("This is #" + id + "notification  ")
                .setContentIntent(pendingIntent)
                .build();
        mNotificationManager.notify(id, notification);


        if (!checkNotificationExistence(id, /*shouldExist=*/ true)) {
            fail("couldn't find posted notification id=" + id);
        }
    }

    private boolean checkNotificationExistence(int id, boolean shouldExist) {
        // notification is a bit asynchronous so it may take a few ms to appear in getActiveNotifications()
        // we will check for it for up to 200ms before giving up
        boolean found = false;
        for (int tries=3; tries-->0;) {
            // Need reset flag.
            found = false;
            final StatusBarNotification[] sbns = mNotificationManager.getActiveNotifications();
            for (StatusBarNotification sbn : sbns) {
                if (sbn.getId() == id) {
                    found = true;
                    break;
                }
            }
            if (found == shouldExist) break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                // pass
            }
        }
        return found == shouldExist;
    }
}

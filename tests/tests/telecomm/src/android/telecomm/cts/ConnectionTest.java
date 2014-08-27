/*
 * Copyright (C) 2014 The Android Open Source Project
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

package android.telecomm.cts;

import android.telecomm.Connection;
import android.telephony.DisconnectCause;
import android.test.AndroidTestCase;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ConnectionTest extends AndroidTestCase {
    public void testStateCallbacks() {
        final Semaphore lock = new Semaphore(0);
        Connection connection = createConnection(lock);

        waitForStateChange(lock);
        assertEquals(Connection.STATE_NEW, connection.getState());

        connection.setInitializing();
        waitForStateChange(lock);
        assertEquals(Connection.STATE_INITIALIZING, connection.getState());

        connection.setInitialized();
        waitForStateChange(lock);
        assertEquals(Connection.STATE_NEW, connection.getState());

        connection.setRinging();
        waitForStateChange(lock);
        assertEquals(Connection.STATE_RINGING, connection.getState());

        connection.setDialing();
        waitForStateChange(lock);
        assertEquals(Connection.STATE_DIALING, connection.getState());

        connection.setActive();
        waitForStateChange(lock);
        assertEquals(Connection.STATE_ACTIVE, connection.getState());

        connection.setOnHold();
        waitForStateChange(lock);
        assertEquals(Connection.STATE_HOLDING, connection.getState());

        connection.setDisconnected(DisconnectCause.LOCAL, "Test call");
        waitForStateChange(lock);
        assertEquals(Connection.STATE_DISCONNECTED, connection.getState());

        connection.setRinging();
        waitForStateChange(lock);
        assertEquals("Connection should not move out of STATE_DISCONNECTED.",
                Connection.STATE_DISCONNECTED, connection.getState());
    }

    public void testFailedState() {
        Connection connection =
                Connection.createFailedConnection(DisconnectCause.LOCAL, "Test call");
        assertEquals(Connection.STATE_DISCONNECTED, connection.getState());

        connection.setRinging();
        assertEquals("Connection should not move out of STATE_DISCONNECTED.",
                Connection.STATE_DISCONNECTED, connection.getState());
    }

    public void testCanceledState() {
        Connection connection = Connection.createCanceledConnection();
        assertEquals(Connection.STATE_DISCONNECTED, connection.getState());

        connection.setDialing();
        assertEquals("Connection should not move out of STATE_DISCONNECTED",
                Connection.STATE_DISCONNECTED, connection.getState());
    }

    private static Connection createConnection(final Semaphore lock) {
        Connection.Listener listener = new Connection.Listener() {
            @Override
            public void onStateChanged(Connection c, int state) {
                lock.release();
            }
        };

        Connection connection = new BasicConnection();
        connection.addConnectionListener(listener);
        return connection;
    }

    private static void waitForStateChange(Semaphore lock) {
        try {
            lock.tryAcquire(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail("State transition timed out");
        }
    }

    private static final class BasicConnection extends Connection {
    }
}

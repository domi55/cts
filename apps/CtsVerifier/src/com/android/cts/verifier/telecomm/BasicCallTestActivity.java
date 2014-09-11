package com.android.cts.verifier.telecomm;

import com.android.cts.verifier.R;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.telecomm.Connection;
import android.telecomm.ConnectionRequest;
import android.telecomm.PhoneAccountHandle;
import android.telecomm.RemoteConnection;
import android.telecomm.StatusHints;
import android.util.Log;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Tests that a basic call with RemoteConnections will go through. When this ConnectionService is
 * notified of an outgoing call, it will create a Connection which wraps a RemoteConnection. Then,
 * once the RemoteConnection starts dialing, it will disconnect the call and the test will pass.
 */
public class BasicCallTestActivity extends TelecommBaseTestActivity {
    private static final Semaphore sLock = new Semaphore(0);

    @Override
    protected int getTestTitleResource() {
        return R.string.telecomm_basic_call_title;
    }

    @Override
    protected int getTestInfoResource() {
        return R.string.telecomm_basic_call_info;
    }

    @Override
    protected Class<? extends android.telecomm.ConnectionService> getConnectionService() {
        return ConnectionService.class;
    }

    @Override
    protected String getConnectionServiceLabel() {
        return "Basic Call Manager";
    }

    @Override
    protected boolean onCallPlacedBackgroundThread() {
        try {
            if (!sLock.tryAcquire(5000, TimeUnit.MILLISECONDS)) {
                return false;
            }

            // Wait for the listeners to be fired so the call is cleaned up.
            SystemClock.sleep(1000);

            return !getTelecommManager().isInCall();
        } catch (Exception e) {
            return false;
        }
    }

    public static class ConnectionService extends android.telecomm.ConnectionService {
        @Override
        public Connection onCreateOutgoingConnection(
                PhoneAccountHandle connectionManagerPhoneAccount,
                ConnectionRequest request) {
            RemoteConnection remoteConnection =
                    createRemoteOutgoingConnection(connectionManagerPhoneAccount, request);
            return new ProxyConnection(remoteConnection);
        }
    }

    private static class ProxyConnection extends Connection {
        private final RemoteConnection mRemoteConnection;

        private final RemoteConnection.Listener mListener = new RemoteConnection.Listener() {
            @Override
            public void onStateChanged(RemoteConnection connection, int state) {
                switch (state) {
                    case Connection.STATE_ACTIVE:
                        setActive();
                        break;
                    case Connection.STATE_DIALING:
                        setDialing();
                        break;
                    case Connection.STATE_DISCONNECTED:
                        sLock.release();
                        break;
                    case Connection.STATE_HOLDING:
                        setOnHold();
                        break;
                    case Connection.STATE_INITIALIZING:
                        setInitializing();
                        break;
                    case Connection.STATE_NEW:
                        setInitialized();
                        break;
                    case Connection.STATE_RINGING:
                        setRinging();
                        break;
                }
            }

            @Override
            public void onDisconnected(RemoteConnection connection, int disconnectCauseCode,
                    String disconnectCauseMessage) {
                setDisconnected(disconnectCauseCode, disconnectCauseMessage);
            }

            @Override
            public void onStatusHintsChanged(RemoteConnection connection, StatusHints statusHints) {
                setStatusHints(statusHints);
            }

            @Override
            public void onHandleChanged(RemoteConnection connection, Uri handle, int presentation) {
                setHandle(handle, presentation);
            }

            @Override
            public void onCallerDisplayNameChanged(RemoteConnection connection,
                    String callerDisplayName,
                    int presentation) {
                setCallerDisplayName(callerDisplayName, presentation);
            }

            @Override
            public void onDestroyed(RemoteConnection connection) {
                destroy();
            }
        };

        public ProxyConnection(RemoteConnection connection) {
            mRemoteConnection = connection;
            if (connection.getState() == Connection.STATE_DISCONNECTED) {
                sLock.release();
            } else {
                mRemoteConnection.addListener(mListener);
            }
        }

        @Override
        public void onSetState(int state) {
            if (state == Connection.STATE_DIALING) {
                // Good enough; let's disconnect this call.
                mRemoteConnection.disconnect();
                sLock.release();
            }
        }
    }
}

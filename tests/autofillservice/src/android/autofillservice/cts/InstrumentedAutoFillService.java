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

package android.autofillservice.cts;

import static android.autofillservice.cts.CannedFillResponse.NO_RESPONSE;
import static android.autofillservice.cts.Helper.CONNECTION_TIMEOUT_MS;
import static android.autofillservice.cts.Helper.FILL_TIMEOUT_MS;
import static android.autofillservice.cts.Helper.SAVE_TIMEOUT_MS;
import static android.autofillservice.cts.Helper.IDLE_UNBIND_TIMEOUT_MS;
import static android.autofillservice.cts.Helper.dumpAutofillService;
import static android.autofillservice.cts.Helper.dumpStructure;

import static com.google.common.truth.Truth.assertWithMessage;

import android.app.assist.AssistStructure;
import android.autofillservice.cts.CannedFillResponse.CannedDataset;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.List;

/**
 * Implementation of {@link AutofillService} used in the tests.
 */
public class InstrumentedAutoFillService extends AutofillService {

    private static final String TAG = "InstrumentedAutoFillService";

    private static final boolean DUMP_FILL_REQUESTS = false;
    private static final boolean DUMP_SAVE_REQUESTS = false;

    private static final String STATE_CONNECTED = "CONNECTED";
    private static final String STATE_DISCONNECTED = "DISCONNECTED";

    private static final AtomicReference<InstrumentedAutoFillService> sInstance =
            new AtomicReference<>();
    private static final Replier sReplier = new Replier();
    private static final BlockingQueue<String> sConnectionStates = new LinkedBlockingQueue<>();

    public InstrumentedAutoFillService() {
        sInstance.set(this);
    }

    public static AutofillService peekInstance() {
        return sInstance.get();
    }

    @Override
    public void onConnected() {
        Log.v(TAG, "onConnected(): " + sConnectionStates);
        sConnectionStates.offer(STATE_CONNECTED);
    }

    @Override
    public void onDisconnected() {
        Log.v(TAG, "onDisconnected(): " + sConnectionStates);
        sConnectionStates.offer(STATE_DISCONNECTED);
    }

    @Override
    public void onFillRequest(android.service.autofill.FillRequest request,
            CancellationSignal cancellationSignal, FillCallback callback) {
        if (DUMP_FILL_REQUESTS) dumpStructure("onFillRequest()", request.getFillContexts());

        sReplier.onFillRequest(request.getFillContexts(), request.getClientState(),
                cancellationSignal, callback, request.getFlags());
    }

    @Override
    public void onSaveRequest(android.service.autofill.SaveRequest request,
            SaveCallback callback) {
        if (DUMP_SAVE_REQUESTS) dumpStructure("onSaveRequest()", request.getFillContexts());
        sReplier.onSaveRequest(request.getFillContexts(), request.getClientState(), callback);
    }

    /**
     * Waits until {@link #onConnected()} is called, or fails if it times out.
     *
     * <p>This method is useful on tests that explicitly verifies the connection, but should be
     * avoided in other tests, as it adds extra time to the test execution - if a text needs to
     * block until the service receives a callback, it should use
     * {@link Replier#getNextFillRequest()} instead.
     */
    static void waitUntilConnected() throws InterruptedException {
        final String state = sConnectionStates.poll(CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        if (state == null) {
            dumpAutofillService();
            throw new RetryableException("not connected in %d ms", CONNECTION_TIMEOUT_MS);
        }
        assertWithMessage("Invalid connection state").that(state).isEqualTo(STATE_CONNECTED);
    }

    /**
     * Waits until {@link #onDisconnected()} is called, or fails if it times out.
     *
     * <p>This method is useful on tests that explicitly verifies the connection, but should be
     * avoided in other tests, as it adds extra time to the test execution.
     */
    static void waitUntilDisconnected() throws InterruptedException {
        final String state = sConnectionStates.poll(2 * IDLE_UNBIND_TIMEOUT_MS,
                TimeUnit.MILLISECONDS);
        if (state == null) {
            throw new RetryableException("not disconnected in %d ms", IDLE_UNBIND_TIMEOUT_MS);
        }
        assertWithMessage("Invalid connection state").that(state).isEqualTo(STATE_DISCONNECTED);
    }

    /**
     * Gets the {@link Replier} singleton.
     */
    static Replier getReplier() {
        return sReplier;
    }

    static void resetStaticState() {
        sConnectionStates.clear();
    }

    /**
     * POJO representation of the contents of a
     * {@link AutofillService#onFillRequest(android.service.autofill.FillRequest,
     * CancellationSignal, FillCallback)} that can be asserted at the end of a test case.
     */
    static final class FillRequest {
        final AssistStructure structure;
        final List<FillContext> contexts;
        final Bundle data;
        final CancellationSignal cancellationSignal;
        final FillCallback callback;
        final int flags;

        private FillRequest(List<FillContext> contexts, Bundle data,
                CancellationSignal cancellationSignal, FillCallback callback, int flags) {
            this.contexts = contexts;
            this.data = data;
            this.cancellationSignal = cancellationSignal;
            this.callback = callback;
            this.flags = flags;
            structure = contexts.get(contexts.size() - 1).getStructure();
        }
    }

    /**
     * POJO representation of the contents of a
     * {@link AutofillService#onSaveRequest(android.service.autofill.SaveRequest, SaveCallback)}
     * that can be asserted at the end of a test case.
     */
    static final class SaveRequest {
        final List<FillContext> contexts;
        final AssistStructure structure;
        final Bundle data;
        final SaveCallback callback;

        private SaveRequest(List<FillContext> contexts, Bundle data, SaveCallback callback) {
            if (contexts != null && contexts.size() > 0) {
                structure = contexts.get(contexts.size() - 1).getStructure();
            } else {
                structure = null;
            }
            this.contexts = contexts;
            this.data = data;
            this.callback = callback;
        }
    }

    /**
     * Object used to answer a
     * {@link AutofillService#onFillRequest(android.service.autofill.FillRequest,
     * CancellationSignal, FillCallback)}
     * on behalf of a unit test method.
     */
    static final class Replier {

        private final BlockingQueue<CannedFillResponse> mResponses = new LinkedBlockingQueue<>();
        private final BlockingQueue<FillRequest> mFillRequests = new LinkedBlockingQueue<>();
        private final BlockingQueue<SaveRequest> mSaveRequests = new LinkedBlockingQueue<>();

        private Replier() {
        }

        /**
         * Sets the expectation for the next {@code onFillRequest} as {@link FillResponse} with just
         * one {@link Dataset}.
         */
        Replier addResponse(CannedDataset dataset) {
            return addResponse(new CannedFillResponse.Builder()
                    .addDataset(dataset)
                    .build());
        }

        /**
         * Sets the expectation for the next {@code onFillRequest}.
         */
        Replier addResponse(CannedFillResponse response) {
            if (response == null) {
                throw new IllegalArgumentException("Cannot be null - use NO_RESPONSE instead");
            }
            mResponses.add(response);
            return this;
        }

        /**
         * Gets the next fill request, in the order received.
         *
         * <p>Typically called at the end of a test case, to assert the initial request.
         */
        FillRequest getNextFillRequest() throws InterruptedException {
            final FillRequest request = mFillRequests.poll(FILL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (request == null) {
                throw new RetryableException("onFillRequest() not called in %s ms",
                        FILL_TIMEOUT_MS);
            }
            return request;
        }

        /**
         * Asserts the total number of {@link AutofillService#onFillRequest(
         * android.service.autofill.FillRequest,  CancellationSignal, FillCallback)}, minus those
         * returned by {@link #getNextFillRequest()}.
         */
        void assertNumberUnhandledFillRequests(int expected) {
            assertWithMessage("Invalid number of fill requests").that(mFillRequests.size())
                    .isEqualTo(expected);
        }

        /**
         * Gets the next save request, in the order received.
         *
         * <p>Typically called at the end of a test case, to assert the initial request.
         */
        SaveRequest getNextSaveRequest() throws InterruptedException {
            final SaveRequest request = mSaveRequests.poll(SAVE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (request == null) {
                throw new RetryableException(
                        "onSaveRequest() not called in %d ms", SAVE_TIMEOUT_MS);
            }
            return request;
        }

        /**
         * Asserts the total number of
         * {@link AutofillService#onSaveRequest(android.service.autofill.SaveRequest, SaveCallback)}
         * minus those returned by {@link #getNextSaveRequest()}.
         */
        void assertNumberUnhandledSaveRequests(int expected) {
            assertWithMessage("Invalid number of save requests").that(mSaveRequests.size())
                    .isEqualTo(expected);
        }

        /**
         * Resets its internal state.
         */
        void reset() {
            mResponses.clear();
            mFillRequests.clear();
            mSaveRequests.clear();
        }

        private void onFillRequest(List<FillContext> contexts, Bundle data,
                CancellationSignal cancellationSignal, FillCallback callback, int flags) {
            try {
                CannedFillResponse response = null;
                try {
                    response = mResponses.poll(CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.w(TAG, "Interrupted getting CannedResponse: " + e);
                    Thread.currentThread().interrupt();
                }
                if (response == null) {
                    dumpStructure("onFillRequest() without response", contexts);
                    throw new IllegalStateException("No CannedResponse");
                }
                if (response == NO_RESPONSE) {
                    Log.d(TAG, "onFillRequest(): replying with null");
                    callback.onSuccess(null);
                    return;
                }

                final FillResponse fillResponse = response.asFillResponse(
                        (id) -> Helper.findNodeByResourceId(contexts, id));

                Log.v(TAG, "onFillRequest(): fillResponse = " + fillResponse);
                callback.onSuccess(fillResponse);
            } finally {
                mFillRequests.offer(new FillRequest(contexts, data, cancellationSignal, callback,
                        flags));
            }
        }

        private void onSaveRequest(List<FillContext> contexts, Bundle data, SaveCallback callback) {
            Log.d(TAG, "onSaveRequest()");
            mSaveRequests.offer(new SaveRequest(contexts, data, callback));
            callback.onSuccess();
        }
    }
}

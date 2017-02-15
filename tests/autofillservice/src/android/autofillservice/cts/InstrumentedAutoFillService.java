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

import static android.autofillservice.cts.Helper.FILL_TIMEOUT_MS;
import static android.autofillservice.cts.Helper.SAVE_TIMEOUT_MS;
import static android.autofillservice.cts.Helper.findNodeByResourceId;

import static com.google.common.truth.Truth.assertWithMessage;

import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.autofillservice.cts.CannedFillResponse.CannedDataset;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.service.autofill.AutoFillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.util.Log;
import android.view.autofill.AutoFillId;
import android.view.autofill.AutoFillValue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of {@link AutoFillService} used in the tests.
 */
public class InstrumentedAutoFillService extends AutoFillService {

    private static final String TAG = "InstrumentedAutoFillService";

    private static final AtomicReference<Replier> sReplier = new AtomicReference<>();

    // TODO(b/33197203, b/33802548): add tests for onConnected() / onDisconnected() and/or remove
    // overriden methods below that are only logging their calls.

    @Override
    public void onConnected() {
        Log.v(TAG, "onConnected()");
    }

    @Override
    public void onDisconnected() {
        Log.v(TAG, "onDisconnected()");
    }

    @Override
    public void onFillRequest(AssistStructure structure, Bundle data,
            CancellationSignal cancellationSignal, FillCallback callback) {
        final Replier replier = sReplier.getAndSet(null);
        assertWithMessage("Replier not set").that(replier).isNotNull();
        replier.onFillRequest(structure, data, cancellationSignal, callback);
    }

    @Override
    public void onSaveRequest(AssistStructure structure, Bundle data, SaveCallback callback) {
        final Replier replier = sReplier.getAndSet(null);
        assertWithMessage("Replier not set").that(replier).isNotNull();
        replier.onSaveRequest(structure, data, callback);
    }

    /**
     * Sets the {@link Replier} for the
     * {@link #onFillRequest(AssistStructure, Bundle, CancellationSignal, FillCallback)} and
     * {@link #onSaveRequest(AssistStructure, Bundle, SaveCallback)} calls.
     */
    public static void setReplier(Replier replier) {
        final boolean ok = sReplier.compareAndSet(null, replier);
        if (!ok) {
            throw new IllegalStateException("already set: " + sReplier.get());
        }
    }

    public static void resetFillReplier() {
        sReplier.set(null);
    }

    /**
     * POJO representation of the contents of a
     * {@link AutoFillService#onFillRequest(android.app.assist.AssistStructure, android.os.Bundle,
     * android.os.CancellationSignal, android.service.autofill.FillCallback)}
     * that can be asserted at the end of a test case.
     */
    static final class FillRequest {
        final AssistStructure structure;
        final Bundle data;
        final CancellationSignal cancellationSignal;
        final FillCallback callback;

        private FillRequest(AssistStructure structure, Bundle data,
                CancellationSignal cancellationSignal, FillCallback callback) {
            this.structure = structure;
            this.data = data;
            this.cancellationSignal = cancellationSignal;
            this.callback = callback;
        }
    }

    /**
     * POJO representation of the contents of a
     * {@link AutoFillService#onSaveRequest(AssistStructure, Bundle, SaveCallback)}
     * that can be asserted at the end of a test case.
     */
    static final class SaveRequest {
        final AssistStructure structure;
        final Bundle data;
        final SaveCallback callback;

        private SaveRequest(AssistStructure structure, Bundle data, SaveCallback callback) {
            this.structure = structure;
            this.data = data;
            this.callback = callback;
        }
    }

    /**
     * Object used to answer a
     * {@link AutoFillService#onFillRequest(android.app.assist.AssistStructure, android.os.Bundle,
     * android.os.CancellationSignal, android.service.autofill.FillCallback)}
     * on behalf of a unit test method.
     */
    static final class Replier {

        private final Queue<CannedFillResponse> mResponses = new LinkedList<>();
        private final BlockingQueue<FillRequest> mFillRequests = new LinkedBlockingQueue<>();
        private final BlockingQueue<SaveRequest> mSaveRequests = new LinkedBlockingQueue<>();

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
                throw new AssertionError("onFill() not called in " + FILL_TIMEOUT_MS + " ms");
            }
            return request;
        }

        /**
         * Asserts the total number of {@link AutoFillService#onFillRequest(AssistStructure, Bundle,
         * CancellationSignal, FillCallback)}, minus those returned by
         * {@link #getNextFillRequest()}.
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
                throw new AssertionError("onSave() not called in " + SAVE_TIMEOUT_MS + " ms");
            }
            return request;
        }

        /**
         * Asserts the total number of {@link AutoFillService#onSaveRequest(AssistStructure,
         * Bundle, SaveCallback)} minus those returned by {@link #getNextSaveRequest()}.
         */
        void assertNumberUnhandledSaveRequests(int expected) {
            assertWithMessage("Invalid number of save requests").that(mSaveRequests.size())
                    .isEqualTo(expected);
        }

        private void onFillRequest(AssistStructure structure,
                @SuppressWarnings("unused") Bundle data, CancellationSignal cancellationSignal,
                FillCallback callback) {

            mFillRequests.offer(new FillRequest(structure, data, cancellationSignal, callback));

            final CannedFillResponse response = mResponses.remove();

            if (response == null) {
                callback.onSuccess(null);
                return;
            }
            final FillResponse.Builder responseBuilder = new FillResponse.Builder();
            final List<CannedDataset> datasets = response.datasets;
            final String[] savableIds = response.savableIds;

            if (datasets.isEmpty() && savableIds == null) {
                callback.onSuccess(responseBuilder.build());
                return;
            }

            if (!datasets.isEmpty()) {
                assertWithMessage("multiple datasets not supported yet").that(datasets).hasSize(1);

                final CannedDataset dataset = datasets.get(0);

                final Map<String, AutoFillValue> fields = dataset.fields;
                if (fields.isEmpty()) {
                    callback.onSuccess(responseBuilder.build());
                    return;
                }

                final Dataset.Builder datasetBuilder = new Dataset.Builder(dataset.name);
                for (Map.Entry<String, AutoFillValue> entry : fields.entrySet()) {
                    final String resourceId = entry.getKey();
                    final ViewNode node = findNodeByResourceId(structure, resourceId);
                    assertWithMessage("no ViewNode with id %s", resourceId).that(node).isNotNull();
                    final AutoFillId id = node.getAutoFillId();
                    final AutoFillValue value = entry.getValue();
                    Log.d(TAG, "setting '" + resourceId + "' (" + id + ") to " + value);
                    datasetBuilder.setValue(id, value);
                }
                responseBuilder.addDataset(datasetBuilder.build());
            }

            if (savableIds != null) {
                for (String resourceId : savableIds) {
                    final ViewNode node = findNodeByResourceId(structure, resourceId);
                    final AutoFillId id = node.getAutoFillId();
                    Log.d(TAG, "mapping savable id: '" + resourceId + "' to " + id);
                    responseBuilder.addSavableFields(id);
                }
            }

            final FillResponse fillResponse = responseBuilder.build();
            Log.v(TAG, "onFillRequest(): fillResponse = " + fillResponse);
            callback.onSuccess(fillResponse);
        }

        private void onSaveRequest(AssistStructure structure, Bundle data, SaveCallback callback) {
            Log.d(TAG, "onSaveRequest()");
            mSaveRequests.offer(new SaveRequest(structure, data, callback));
        }
    }
}

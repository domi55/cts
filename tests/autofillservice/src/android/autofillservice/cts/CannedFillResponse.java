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

import static com.google.common.truth.Truth.assertWithMessage;

import static android.autofillservice.cts.Helper.dumpStructure;
import static android.autofillservice.cts.Helper.findNodeByResourceId;
import static android.autofillservice.cts.Helper.getAutofillIds;
import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.content.IntentSender;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveInfo;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class used to produce a {@link FillResponse} based on expected fields that should be
 * present in the {@link AssistStructure}.
 *
 * <p>Typical usage:
 *
 * <pre class="prettyprint">
 * InstrumentedAutoFillService.setFillResponse(new CannedFillResponse.Builder()
 *               .addDataset(new CannedDataset.Builder("dataset_name")
 *                   .setField("resource_id1", AutofillValue.forText("value1"))
 *                   .setField("resource_id2", AutofillValue.forText("value2"))
 *                   .build())
 *               .build());
 * </pre class="prettyprint">
 */
final class CannedFillResponse {

    private final List<CannedDataset> mDatasets;
    private final int mSaveType;
    private final String[] mRequiredSavableIds;
    private final String[] mOptionalSavableIds;
    private final String mSaveDescription;
    private final Bundle mExtras;
    private final RemoteViews mPresentation;
    private final IntentSender mAuthentication;
    private final String[] mAuthenticationIds;
    private final CharSequence mNegativeActionLabel;
    private final IntentSender mNegativeActionListener;
    private final int mFlags;

    private CannedFillResponse(Builder builder) {
        mDatasets = builder.mDatasets;
        mRequiredSavableIds = builder.mRequiredSavableIds;
        mOptionalSavableIds = builder.mOptionalSavableIds;
        mSaveDescription = builder.mSaveDescription;
        mSaveType = builder.mSaveType;
        mExtras = builder.mExtras;
        mPresentation = builder.mPresentation;
        mAuthentication = builder.mAuthentication;
        mAuthenticationIds = builder.mAuthenticationIds;
        mNegativeActionLabel = builder.mNegativeActionLabel;
        mNegativeActionListener = builder.mNegativeActionListener;
        mFlags = builder.mFlags;
    }

    /**
     * Constant used to pass a {@code null} response to the
     * {@link FillCallback#onSuccess(FillResponse)} method.
     */
    static final CannedFillResponse NO_RESPONSE = new Builder().build();

    /**
     * Creates a new response, replacing the dataset field ids by the real ids from the assist
     * structure.
     */
    FillResponse asFillResponse(AssistStructure structure) {
        final FillResponse.Builder builder = new FillResponse.Builder();
        if (mDatasets != null) {
            for (CannedDataset cannedDataset : mDatasets) {
                final Dataset dataset = cannedDataset.asDataset(structure);
                assertWithMessage("Cannot create datase").that(dataset).isNotNull();
                builder.addDataset(dataset);
            }
        }
        if (mRequiredSavableIds != null) {
            final SaveInfo.Builder saveInfo;

            if (mRequiredSavableIds == null) {
                saveInfo = new SaveInfo.Builder(mSaveType, null);
            } else {
                saveInfo = new SaveInfo.Builder(mSaveType,
                        getAutofillIds(structure, mRequiredSavableIds));
            }

            saveInfo.setFlags(mFlags);

            if (mOptionalSavableIds != null) {
                saveInfo.setOptionalIds(getAutofillIds(structure, mOptionalSavableIds));
            }
            if (mSaveDescription != null) {
                saveInfo.setDescription(mSaveDescription);
            }
            if (mNegativeActionLabel != null) {
                saveInfo.setNegativeAction(mNegativeActionLabel, mNegativeActionListener);
            }
            builder.setSaveInfo(saveInfo.build());
        }
        return builder
                .setExtras(mExtras)
                .setAuthentication(getAutofillIds(structure, mAuthenticationIds), mAuthentication,
                        mPresentation)
                .build();
    }

    @Override
    public String toString() {
        return "CannedFillResponse: [datasets=" + mDatasets
                + ", requiredSavableIds=" + Arrays.toString(mRequiredSavableIds)
                + ", optionalSavableIds=" + Arrays.toString(mOptionalSavableIds)
                + ", mFlags=" + mFlags
                + ", saveDescription=" + mSaveDescription
                + ", hasPresentation=" + (mPresentation != null)
                + ", hasAuthentication=" + (mAuthentication != null)
                + ", authenticationIds=" + Arrays.toString(mAuthenticationIds)
                + "]";
    }

    static class Builder {
        private final List<CannedDataset> mDatasets = new ArrayList<>();
        private String[] mRequiredSavableIds;
        private String[] mOptionalSavableIds;
        private String mSaveDescription;
        public int mSaveType = -1;
        private Bundle mExtras;
        private RemoteViews mPresentation;
        private IntentSender mAuthentication;
        private String[] mAuthenticationIds;
        private CharSequence mNegativeActionLabel;
        private IntentSender mNegativeActionListener;
        private int mFlags;

        public Builder addDataset(CannedDataset dataset) {
            mDatasets.add(dataset);
            return this;
        }

        /**
         * Sets the required savable ids based on they {@code resourceId}.
         */
        public Builder setRequiredSavableIds(int type, String... ids) {
            mSaveType = type;
            mRequiredSavableIds = ids;
            return this;
        }

        public Builder setFlags(int flags) {
            mFlags = flags;
            return this;
        }

        /**
         * Sets the optional savable ids based on they {@code resourceId}.
         */
        public Builder setOptionalSavableIds(String... ids) {
            mOptionalSavableIds = ids;
            return this;
        }

        /**
         * Sets the description passed to the {@link SaveInfo}.
         */
        public Builder setSaveDescription(String description) {
            mSaveDescription = description;
            return this;
        }

        /**
         * Sets the extra passed to {@link
         * android.service.autofill.FillResponse.Builder#setExtras(Bundle)}.
         */
        public Builder setExtras(Bundle data) {
            mExtras = data;
            return this;
        }

        /**
         * Sets the view to present the response in the UI.
         */
        public Builder setPresentation(RemoteViews presentation) {
            mPresentation = presentation;
            return this;
        }

        /**
         * Sets the authentication intent.
         */
        public Builder setAuthentication(IntentSender authentication) {
            mAuthentication = authentication;
            return this;
        }

        /**
         * Sets the authentication ids.
         */
        public Builder setAuthenticationIds(String... ids) {
            mAuthenticationIds = ids;
            return this;
        }

        /**
         * Sets the negative action spec.
         */
        public Builder setNegativeAction(CharSequence label,
                IntentSender listener) {
            mNegativeActionLabel = label;
            mNegativeActionListener = listener;
            return this;
        }

        public CannedFillResponse build() {
            return new CannedFillResponse(this);
        }
    }

    /**
     * Helper class used to produce a {@link Dataset} based on expected fields that should be
     * present in the {@link AssistStructure}.
     *
     * <p>Typical usage:
     *
     * <pre class="prettyprint">
     * InstrumentedAutoFillService.setFillResponse(new CannedFillResponse.Builder()
     *               .addDataset(new CannedDataset.Builder("dataset_name")
     *                   .setField("resource_id1", AutofillValue.forText("value1"))
     *                   .setField("resource_id2", AutofillValue.forText("value2"))
     *                   .build())
     *               .build());
     * </pre class="prettyprint">
     */
    static class CannedDataset {
        private final Map<String, AutofillValue> mFieldValues;
        private final Map<String, RemoteViews> mFieldPresentations;
        private final RemoteViews mPresentation;
        private final IntentSender mAuthentication;
        private final String mId;

        private CannedDataset(Builder builder) {
            mFieldValues = builder.mFieldValues;
            mFieldPresentations = builder.mFieldPresentations;
            mPresentation = builder.mPresentation;
            mAuthentication = builder.mAuthentication;
            mId = builder.mId;
        }

        /**
         * Creates a new dataset, replacing the field ids by the real ids from the assist structure.
         */
        Dataset asDataset(AssistStructure structure) {
            final Dataset.Builder builder = (mPresentation == null)
                    ? new Dataset.Builder()
                    : new Dataset.Builder(mPresentation);

            if (mFieldValues != null) {
                for (Map.Entry<String, AutofillValue> entry : mFieldValues.entrySet()) {
                    final String resourceId = entry.getKey();
                    final ViewNode node = findNodeByResourceId(structure, resourceId);
                    if (node == null) {
                        dumpStructure("asDataset()", structure);
                        throw new AssertionError("No node with resource id " + resourceId);
                    }
                    final AutofillId id = node.getAutofillId();
                    final AutofillValue value = entry.getValue();
                    final RemoteViews presentation = mFieldPresentations.get(resourceId);
                    if (presentation != null) {
                        builder.setValue(id, value, presentation);
                    } else {
                        builder.setValue(id, value);
                    }
                }
            }
            builder.setId(mId).setAuthentication(mAuthentication);
            return builder.build();
        }

        @Override
        public String toString() {
            return "CannedDataset " + mId + " : [hasPresentation=" + (mPresentation != null)
                    + ", fieldPresentations=" + (mFieldPresentations)
                    + ", hasAuthentication=" + (mAuthentication != null)
                    + ", fieldValuess=" + mFieldValues + "]";
        }

        static class Builder {
            private final Map<String, AutofillValue> mFieldValues = new HashMap<>();
            private final Map<String, RemoteViews> mFieldPresentations = new HashMap<>();
            private RemoteViews mPresentation;
            private IntentSender mAuthentication;
            private String mId;

            public Builder() {

            }

            public Builder(RemoteViews presentation) {
                mPresentation = presentation;
            }

            /**
             * Sets the canned value of a text field based on its {@code resourceId}.
             */
            public Builder setField(String resourceId, String text) {
                return setField(resourceId, AutofillValue.forText(text));
            }

            /**
             * Sets the canned value of a list field based on its {@code resourceId}.
             */
            public Builder setField(String resourceId, int index) {
                return setField(resourceId, AutofillValue.forList(index));
            }

            /**
             * Sets the canned value of a toggle field based on its {@code resourceId}.
             */
            public Builder setField(String resourceId, boolean toggled) {
                return setField(resourceId, AutofillValue.forToggle(toggled));
            }

            /**
             * Sets the canned value of a date field based on its {@code resourceId}.
             */
            public Builder setField(String resourceId, long date) {
                return setField(resourceId, AutofillValue.forDate(date));
            }

            /**
             * Sets the canned value of a date field based on its {@code resourceId}.
             */
            public Builder setField(String resourceId, AutofillValue value) {
                mFieldValues.put(resourceId, value);
                return this;
            }

            /**
             * Sets the canned value of a field based on its {@code resourceId}.
             */
            public Builder setField(String resourceId, String text, RemoteViews presentation) {
                setField(resourceId, text);
                mFieldPresentations.put(resourceId, presentation);
                return this;
            }

            /**
             * Sets the view to present the response in the UI.
             */
            public Builder setPresentation(RemoteViews presentation) {
                mPresentation = presentation;
                return this;
            }

            /**
             * Sets the authentication intent.
             */
            public Builder setAuthentication(IntentSender authentication) {
                mAuthentication = authentication;
                return this;
            }

            /**
             * Sets the name.
             */
            public Builder setId(String id) {
                mId = id;
                return this;
            }

            public CannedDataset build() {
                return new CannedDataset(this);
            }
        }
    }
}

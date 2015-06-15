/*
 * Copyright (C) 2015 The Android Open Source Project
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

package android.telecom.cts;

import static android.telecom.cts.TestUtils.shouldTestTelecom;

import android.os.Bundle;
import android.telecom.CallAudioState;
import android.telecom.TelecomManager;

/**
 * Verifies the behavior of Telecom during various outgoing call flows.
 */
public class OutgoingCallTest extends BaseTelecomTestWithMockServices {

    // TODO: Need to send some commands to the UserManager via adb to do setup
    public void testDisallowOutgoingCallsForSecondaryUser() {

    }

    // TODO: Need to figure out a way to mock emergency calls without adb root
    public void testOutgoingCallBroadcast_isSentForAllCalls() {

    }

    /**
     * Verifies that providing the EXTRA_START_CALL_WITH_SPEAKERPHONE extra starts the call with
     * speakerphone automatically enabled.
     *
     * @see {@link TelecomManager#EXTRA_START_CALL_WITH_SPEAKERPHONE}
     */
    public void testStartCallWithSpeakerphoneTrue_SpeakerphoneOnInCall() {
        final Bundle extras = new Bundle();
        extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true);
        placeAndVerifyCall(extras);
        verifyConnectionForOutgoingCall();
        assertAudioRoute(mInCallCallbacks.getService(), CallAudioState.ROUTE_SPEAKER);
    }

    public void testStartCallWithSpeakerphoneFalse_SpeakerphoneOffInCall() {
        final Bundle extras = new Bundle();
        extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false);
        placeAndVerifyCall(extras);
        verifyConnectionForOutgoingCall();
        assertAudioRoute(mInCallCallbacks.getService(), CallAudioState.ROUTE_EARPIECE);
    }

    public void testStartCallWithSpeakerphoneNotProvided_SpeakerphoneOffByDefault() {
        placeAndVerifyCall();
        verifyConnectionForOutgoingCall();
        assertAudioRoute(mInCallCallbacks.getService(), CallAudioState.ROUTE_EARPIECE);
    }
}

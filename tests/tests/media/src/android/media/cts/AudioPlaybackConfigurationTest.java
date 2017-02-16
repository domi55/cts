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

package android.media.cts;

import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.media.AudioPlaybackConfiguration;

import com.android.compatibility.common.util.CtsAndroidTestCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AudioPlaybackConfigurationTest extends CtsAndroidTestCase {
    private final static String TAG = "AudioPlaybackConfigurationTest";

    private final static int TEST_TIMING_TOLERANCE_MS = 50;

    // not declared inside test so it can be released in case of failure
    private MediaPlayer mMp;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        if (mMp != null) {
            mMp.stop();
            mMp.release();
        }
    }

    private final static int TEST_USAGE = AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED;
    private final static int TEST_CONTENT = AudioAttributes.CONTENT_TYPE_SPEECH;

    public void testGetterMediaPlayer() throws Exception {
        if (!isValidPlatform("testGetterMediaPlayer")) return;

        AudioManager am = new AudioManager(getContext());
        assertNotNull("Could not create AudioManager", am);

        final AudioAttributes aa = (new AudioAttributes.Builder())
                .setUsage(TEST_USAGE)
                .setContentType(TEST_CONTENT)
                .build();

        List<AudioPlaybackConfiguration> configs = am.getActivePlaybackConfigurations();
        final int nbActivePlayersBeforeStart = configs.size();

        mMp = MediaPlayer.create(getContext(), R.raw.sine1khzs40dblong,
                aa, am.generateAudioSessionId());
        configs = am.getActivePlaybackConfigurations();
        assertEquals("inactive MediaPlayer, number of configs shouldn't have changed",
                nbActivePlayersBeforeStart /*expected*/, configs.size());

        mMp.start();
        Thread.sleep(2*TEST_TIMING_TOLERANCE_MS);// waiting for playback to start
        configs = am.getActivePlaybackConfigurations();
        assertEquals("active MediaPlayer, number of configs should have increased",
                nbActivePlayersBeforeStart + 1 /*expected*/,
                configs.size());
        assertTrue("Active player, attributes not found", hasAttr(configs, aa));

        // verify "privileged" fields aren't available through reflection
        final AudioPlaybackConfiguration config = configs.get(0);
        final Class<?> confClass = config.getClass();
        final Method getClientUidMethod = confClass.getDeclaredMethod("getClientUid");
        final Method getClientPidMethod = confClass.getDeclaredMethod("getClientPid");
        final Method getPlayerTypeMethod = confClass.getDeclaredMethod("getPlayerType");
        try {
            Integer uid = (Integer) getClientUidMethod.invoke(config, null);
            assertEquals("uid isn't protected", -1 /*expected*/, uid.intValue());
            Integer pid = (Integer) getClientPidMethod.invoke(config, null);
            assertEquals("pid isn't protected", -1 /*expected*/, pid.intValue());
            Integer type = (Integer) getPlayerTypeMethod.invoke(config, null);
            assertEquals("player type isn't protected", -1 /*expected*/, type.intValue());
        } catch (Exception e) {
            fail("Exception thrown during reflection on config privileged fields"+ e);
        }
    }

    public void testCallbackMediaPlayer() throws Exception {
        if (!isValidPlatform("testCallbackMediaPlayer")) return;

        AudioManager am = new AudioManager(getContext());
        assertNotNull("Could not create AudioManager", am);

        final AudioAttributes aa = (new AudioAttributes.Builder())
                .setUsage(TEST_USAGE)
                .setContentType(TEST_CONTENT)
                .build();

        mMp = MediaPlayer.create(getContext(), R.raw.sine1khzs40dblong,
                aa, am.generateAudioSessionId());

        MyAudioPlaybackCallback callback = new MyAudioPlaybackCallback();
        am.registerAudioPlaybackCallback(callback, null /*handler*/);

        // query how many active players before starting the MediaPlayer
        List<AudioPlaybackConfiguration> configs = am.getActivePlaybackConfigurations();
        final int nbActivePlayersBeforeStart = configs.size();

        mMp.start();
        Thread.sleep(TEST_TIMING_TOLERANCE_MS);

        assertEquals("onPlaybackConfigChanged call count not expected",
                1/*expected*/, callback.getCbInvocationNumber()); //only one start call
        assertEquals("number of active players not expected",
                // one more player active
                nbActivePlayersBeforeStart + 1/*expected*/, callback.getNbConfigs());
        assertTrue("Active player, attributes not found", hasAttr(callback.getConfigs(), aa));

        // stopping recording: callback is called with no match
        callback.reset();
        mMp.pause();
        Thread.sleep(TEST_TIMING_TOLERANCE_MS);

        assertEquals("onPlaybackConfigChanged call count not expected after pause",
                1/*expected*/, callback.getCbInvocationNumber()); //only one pause call since reset
        assertEquals("number of active players not expected after pause",
                nbActivePlayersBeforeStart/*expected*/, callback.getNbConfigs());

        // unregister callback and start recording again
        am.unregisterAudioPlaybackCallback(callback);
        Thread.sleep(TEST_TIMING_TOLERANCE_MS);
        callback.reset();
        mMp.start();
        Thread.sleep(TEST_TIMING_TOLERANCE_MS);
        assertEquals("onPlaybackConfigChanged call count not expected after unregister",
                0/*expected*/, callback.getCbInvocationNumber()); //callback is unregistered

        // just call the callback once directly so it's marked as tested
        final AudioManager.AudioPlaybackCallback apc =
                (AudioManager.AudioPlaybackCallback) callback;
        apc.onPlaybackConfigChanged(new ArrayList<AudioPlaybackConfiguration>());
    }


    private static class MyAudioPlaybackCallback extends AudioManager.AudioPlaybackCallback {
        private int mCalled = 0;
        private int mNbConfigs = 0;
        private List<AudioPlaybackConfiguration> mConfigs;

        void reset() {
            mCalled = 0;
            mNbConfigs = 0;
            mConfigs.clear();
        }

        int getCbInvocationNumber() { return mCalled; }
        int getNbConfigs() { return mNbConfigs; }
        List<AudioPlaybackConfiguration> getConfigs() { return mConfigs; }

        MyAudioPlaybackCallback() {
        }

        @Override
        public void onPlaybackConfigChanged(List<AudioPlaybackConfiguration> configs) {
            mCalled++;
            mNbConfigs = configs.size();
            mConfigs = configs;
        }
    }

    private static boolean hasAttr(List<AudioPlaybackConfiguration> configs, AudioAttributes aa) {
        Iterator<AudioPlaybackConfiguration> it = configs.iterator();
        while (it.hasNext()) {
            final AudioPlaybackConfiguration apc = it.next();
            if (apc.getAudioAttributes().getContentType() == aa.getContentType()
                    && apc.getAudioAttributes().getUsage() == aa.getUsage()) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidPlatform(String testName) {
        if (!getContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
            Log.w(TAG,"AUDIO_OUTPUT feature not found. This system might not have a valid "
                    + "audio output HAL, skipping test " + testName);
            return false;
        }
        return true;
    }
}

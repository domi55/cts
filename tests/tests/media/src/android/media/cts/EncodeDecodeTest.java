/*
 * Copyright (C) 2013 The Android Open Source Project
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

import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLES11Ext;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.test.AndroidTestCase;
import android.util.Log;
import android.view.Surface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;


/**
 * Generates a series of video frames, encodes them, decodes them, and tests for significant
 * divergence from the original.
 * <p>
 * There are two ways to connect an encoder to a decoder.  The first is to pass the output
 * buffers from the encoder to the input buffers of the decoder, using ByteBuffer.put() to
 * copy the bytes.  With this approach, we need to watch for BUFFER_FLAG_CODEC_CONFIG, and
 * if seen we use format.setByteBuffer("csd-0") followed by decoder.configure() to pass the
 * meta-data through.
 * <p>
 * The second way is to write the buffers to a file and then stream it back in.  With this
 * approach it is necessary to use a MediaExtractor to retrieve the format info and skip past
 * the meta-data.
 * <p>
 * The former can be done entirely in memory, but requires that the encoder and decoder
 * operate simultaneously (the I/O buffers are owned by MediaCodec).  The latter requires
 * writing to disk, because MediaExtractor can only accept a file or URL as a source.
 * <p>
 * The direct encoder-to-decoder approach isn't currently tested elsewhere in this CTS
 * package, so we use that here.
 */
public class EncodeDecodeTest extends AndroidTestCase {
    private static final String TAG = "EncodeDecodeTest";
    private static final boolean VERBOSE = false;           // lots of logging
    private static final boolean DEBUG_SAVE_FILE = false;   // save copy of encoded movie
    private static final String DEBUG_FILE_NAME_BASE = "/sdcard/test.";

    // parameters for the encoder
    private static final String MIME_TYPE = "video/avc";    // H.264 Advanced Video Coding
    private static final int BIT_RATE = 1000000;            // 1Mbps
    private static final int FRAME_RATE = 15;               // 15fps
    private static final int IFRAME_INTERVAL = 10;          // 10 seconds between I-frames

    // movie length, in frames
    private static final int NUM_FRAMES = 30;               // two seconds of video

    private static final int TEST_Y = 240;                  // YUV values for colored rect
    private static final int TEST_U = 220;
    private static final int TEST_V = 200;
    private static final int TEST_R0 = 0;                   // RGB eqivalent of {0,0,0}
    private static final int TEST_G0 = 136;
    private static final int TEST_B0 = 0;
    private static final int TEST_R1 = 255;                 // RGB equivalent of {240,220,200}
    private static final int TEST_G1 = 166;
    private static final int TEST_B1 = 255;

    // size of a frame, in pixels
    private int mWidth = -1;
    private int mHeight = -1;


    /**
     * Tests streaming of AVC video through the encoder and decoder.  Data is encoded from
     * a series of byte[] buffers and decoded into ByteBuffers.  The output is checked for
     * validity.
     */
    public void testEncodeDecodeVideoFromBufferToBufferQCIF() throws Exception {
        setSize(176, 144);
        testEncodeDecodeVideoFromBuffer(false);
    }
    public void testEncodeDecodeVideoFromBufferToBufferQVGA() throws Exception {
        setSize(320, 240);
        testEncodeDecodeVideoFromBuffer(false);
    }
    public void testEncodeDecodeVideoFromBufferToBuffer720p() throws Exception {
        setSize(1280, 720);
        testEncodeDecodeVideoFromBuffer(false);
    }

    /**
     * Tests streaming of AVC video through the encoder and decoder.  Data is encoded from
     * a series of byte[] buffers and decoded into Surfaces.  The output is checked for
     * validity but some frames may be dropped.
     * <p>
     * Because of the way SurfaceTexture.OnFrameAvailableListener works, we need to run this
     * test on a thread that doesn't have a Looper configured.  If we don't, the test will
     * pass, but we won't actually test the output because we'll never receive the "frame
     * available" notifications".  The CTS test framework seems to be configuring a Looper on
     * the test thread, so we have to hand control off to a new thread for the duration of
     * the test.
     */
    public void testEncodeDecodeVideoFromBufferToSurfaceQCIF() throws Throwable {
        setSize(176, 144);
        BufferToSurfaceWrapper.runTest(this);
    }
    public void testEncodeDecodeVideoFromBufferToSurfaceQVGA() throws Throwable {
        setSize(320, 240);
        BufferToSurfaceWrapper.runTest(this);
    }
    public void testEncodeDecodeVideoFromBufferToSurface720p() throws Throwable {
        setSize(1280, 720);
        BufferToSurfaceWrapper.runTest(this);

    }

    /** Wraps testEncodeDecodeVideoFromBuffer(true) */
    private static class BufferToSurfaceWrapper implements Runnable {
        private Throwable mThrowable;
        private EncodeDecodeTest mTest;

        private BufferToSurfaceWrapper(EncodeDecodeTest test) {
            mTest = test;
        }

        public void run() {
            try {
                mTest.testEncodeDecodeVideoFromBuffer(true);
            } catch (Throwable th) {
                mThrowable = th;
            }
        }

        /**
         * Entry point.
         */
        public static void runTest(EncodeDecodeTest obj) throws Throwable {
            BufferToSurfaceWrapper wrapper = new BufferToSurfaceWrapper(obj);
            Thread th = new Thread(wrapper, "codec test");
            th.start();
            th.join();
            if (wrapper.mThrowable != null) {
                throw wrapper.mThrowable;
            }
        }
    }

    /**
     * Sets the desired frame size.
     */
    private void setSize(int width, int height) {
        if ((width % 16) != 0 || (height % 16) != 0) {
            Log.w(TAG, "WARNING: width or height not multiple of 16");
        }
        mWidth = width;
        mHeight = height;
    }

    /**
     * Tests encoding and subsequently decoding video from frames generated into a buffer.
     * <p>
     * We encode several frames of a video test pattern using MediaCodec, then decode the
     * output with MediaCodec and do some simple checks.
     * <p>
     * See http://b.android.com/37769 for a discussion of input format pitfalls.
     */
    private void testEncodeDecodeVideoFromBuffer(boolean toSurface) throws Exception {
        MediaCodecInfo codecInfo = selectCodec(MIME_TYPE);
        if (codecInfo == null) {
            // Don't fail CTS if they don't have an AVC codec (not here, anyway).
            Log.e(TAG, "Unable to find an appropriate codec for " + MIME_TYPE);
            return;
        }
        if (VERBOSE) Log.d(TAG, "found codec: " + codecInfo.getName());

        int colorFormat = selectColorFormat(codecInfo, MIME_TYPE);
        if (VERBOSE) Log.d(TAG, "found colorFormat: " + colorFormat);

        // We avoid the device-specific limitations on width and height by using values that
        // are multiples of 16, which all tested devices seem to be able to handle.
        MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);

        // Set some properties.  Failing to specify some of these can cause the MediaCodec
        // configure() call to throw an unhelpful exception.
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
        format.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);
        if (VERBOSE) Log.d(TAG, "format: " + format);

        // Create a MediaCodec for the desired codec, then configure it as an encoder with
        // our desired properties.
        MediaCodec encoder = MediaCodec.createByCodecName(codecInfo.getName());
        encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        encoder.start();

        // Create a MediaCodec for the decoder, just based on the MIME type.  The various
        // format details will be passed through the csd-0 meta-data later on.
        MediaCodec decoder = MediaCodec.createDecoderByType(MIME_TYPE);

        try {
            encodeDecodeVideoFromBuffer(encoder, colorFormat, decoder, toSurface);
        } finally {
            if (VERBOSE) Log.d(TAG, "releasing codecs");
            encoder.stop();
            decoder.stop();
            encoder.release();
            decoder.release();
        }
    }

    /**
     * Returns the first codec capable of encoding the specified MIME type, or null if no
     * match was found.
     */
    private static MediaCodecInfo selectCodec(String mimeType) {
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

            if (!codecInfo.isEncoder()) {
                continue;
            }

            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(mimeType)) {
                    return codecInfo;
                }
            }
        }
        return null;
    }

    /**
     * Returns a color format that is supported by the codec and by this test code.  If no
     * match is found, this throws a test failure -- the set of formats known to the test
     * should be expanded for new platforms.
     */
    private static int selectColorFormat(MediaCodecInfo codecInfo, String mimeType) {
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
        for (int i = 0; i < capabilities.colorFormats.length; i++) {
            int colorFormat = capabilities.colorFormats[i];
            if (isRecognizedFormat(colorFormat)) {
                return colorFormat;
            }
        }
        fail("couldn't find a good color format for " + codecInfo.getName() + " / " + mimeType);
        return 0;   // not reached
    }

    /**
     * Returns true if this is a color format that this test code understands (i.e. we know how
     * to read and generate frames in this format).
     */
    private static boolean isRecognizedFormat(int colorFormat) {
        switch (colorFormat) {
            // these are the formats we know how to handle for this test
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns true if the specified color format is semi-planar YUV.  Throws an exception
     * if the color format is not recognized (e.g. not YUV).
     */
    private static boolean isSemiPlanarYUV(int colorFormat) {
        switch (colorFormat) {
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
                return false;
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
                return true;
            default:
                throw new RuntimeException("unknown format " + colorFormat);
        }
    }

    /**
     * Does the actual work for encoding frames from buffers of byte[].
     */
    private void encodeDecodeVideoFromBuffer(MediaCodec encoder, int encoderColorFormat,
            MediaCodec decoder, boolean toSurface) {
        final int TIMEOUT_USEC = 10000;
        ByteBuffer[] encoderInputBuffers = encoder.getInputBuffers();
        ByteBuffer[] encoderOutputBuffers = encoder.getOutputBuffers();
        ByteBuffer[] decoderInputBuffers = null;
        ByteBuffer[] decoderOutputBuffers = null;
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int decoderColorFormat = -12345;    // init to invalid value
        int generateIndex = 0;
        int checkIndex = 0;
        boolean decoderConfigured = false;
        SurfaceStuff surfaceStuff = null;

        // The size of a frame of video data, in the formats we handle, is stride*sliceHeight
        // for Y, and (stride/2)*(sliceHeight/2) for each of the Cb and Cr channels.  Application
        // of algebra and assuming that stride==width and sliceHeight==height yields:
        byte[] frameData = new byte[mWidth * mHeight * 3 / 2];

        // Just out of curiosity.
        long rawSize = 0;
        long encodedSize = 0;

        // Save a copy to disk.  Useful for debugging the test.
        FileOutputStream outputStream = null;
        if (DEBUG_SAVE_FILE) {
            String fileName = DEBUG_FILE_NAME_BASE + mWidth + "x" + mHeight + ".mp4";
            try {
                outputStream = new FileOutputStream(fileName);
                Log.d(TAG, "encoded output will be saved as " + fileName);
            } catch (IOException ioe) {
                Log.w(TAG, "Unable to create debug output file " + fileName);
                throw new RuntimeException(ioe);
            }
        }

        if (toSurface) {
            surfaceStuff = new SurfaceStuff(mWidth, mHeight);
        }

        // Loop until the output side is done.
        boolean inputDone = false;
        boolean encoderDone = false;
        boolean outputDone = false;
        while (!outputDone) {
            if (VERBOSE) Log.d(TAG, "loop");

            // If we're not done submitting frames, generate a new one and submit it.  By
            // doing this on every loop we're working to ensure that the encoder always has
            // work to do.
            //
            // We don't really want a timeout here, but sometimes there's a delay opening
            // the encoder device, so a short timeout can keep us from spinning hard.
            if (!inputDone) {
                int inputBufIndex = encoder.dequeueInputBuffer(TIMEOUT_USEC);
                if (VERBOSE) Log.d(TAG, "inputBufIndex=" + inputBufIndex);
                if (inputBufIndex >= 0) {
                    long ptsUsec = generateIndex * 1000000 / FRAME_RATE;
                    if (generateIndex == NUM_FRAMES) {
                        // Send an empty frame with the end-of-stream flag set.  If we set EOS
                        // on a frame with data, that frame data will be ignored, and the
                        // output will be short one frame.
                        encoder.queueInputBuffer(inputBufIndex, 0, 0, ptsUsec,
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        inputDone = true;
                        if (VERBOSE) Log.d(TAG, "sent input EOS (with zero-length frame)");
                    } else {
                        generateFrame(generateIndex, encoderColorFormat, frameData);

                        ByteBuffer inputBuf = encoderInputBuffers[inputBufIndex];
                        // the buffer should be sized to hold one full frame
                        assertTrue(inputBuf.capacity() >= frameData.length);
                        inputBuf.clear();
                        inputBuf.put(frameData);

                        encoder.queueInputBuffer(inputBufIndex, 0, frameData.length, ptsUsec, 0);
                        if (VERBOSE) Log.d(TAG, "submitted frame " + generateIndex + " to enc");
                    }
                    generateIndex++;
                } else {
                    // either all in use, or we timed out during initial setup
                    if (VERBOSE) Log.d(TAG, "input buffer not available");
                }
            }

            // Check for output from the encoder.  If there's no output yet, we either need to
            // provide more input, or we need to wait for the encoder to work its magic.  We
            // can't actually tell which is the case, so if we can't get an output buffer right
            // away we loop around and see if it wants more input.
            //
            // Once we get EOS from the encoder, we don't need to do this anymore.
            if (!encoderDone) {
                int encoderStatus = encoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    if (VERBOSE) Log.d(TAG, "no output from encoder available");
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    // not expected for an encoder
                    encoderOutputBuffers = encoder.getOutputBuffers();
                    if (VERBOSE) Log.d(TAG, "encoder output buffers changed");
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    // not expected for an encoder
                    MediaFormat newFormat = encoder.getOutputFormat();
                    if (VERBOSE) Log.d(TAG, "encoder output format changed: " + newFormat);
                } else if (encoderStatus < 0) {
                    fail("unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
                } else { // encoderStatus >= 0
                    ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                    if (encodedData == null) {
                        fail("encoderOutputBuffer " + encoderStatus + " was null");
                    }

                    // It's usually necessary to adjust the ByteBuffer values to match BufferInfo.
                    encodedData.position(info.offset);
                    encodedData.limit(info.offset + info.size);

                    encodedSize += info.size;
                    if (outputStream != null) {
                        byte[] data = new byte[info.size];
                        encodedData.get(data);
                        encodedData.position(info.offset);
                        try {
                            outputStream.write(data);
                        } catch (IOException ioe) {
                            Log.w(TAG, "failed writing debug data to file");
                            throw new RuntimeException(ioe);
                        }
                    }
                    if ((info.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                        // Codec config info.  Only expected on first packet.
                        assertFalse(decoderConfigured);
                        MediaFormat format =
                                MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);
                        format.setByteBuffer("csd-0", encodedData);
                        decoder.configure(format, toSurface ? surfaceStuff.getSurface() : null,
                                null, 0);
                        decoder.start();
                        decoderInputBuffers = decoder.getInputBuffers();
                        decoderOutputBuffers = decoder.getOutputBuffers();
                        decoderConfigured = true;
                        if (VERBOSE) Log.d(TAG, "decoder configured (" + info.size + " bytes)");
                    } else {
                        // Get a decoder input buffer, blocking until it's available.
                        assertTrue(decoderConfigured);
                        int inputBufIndex = decoder.dequeueInputBuffer(-1);
                        ByteBuffer inputBuf = decoderInputBuffers[inputBufIndex];
                        inputBuf.clear();
                        inputBuf.put(encodedData);
                        decoder.queueInputBuffer(inputBufIndex, 0, info.size, info.presentationTimeUs,
                                info.flags);

                        encoderDone = (info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;
                        if (VERBOSE) Log.d(TAG, "passed " + info.size + " bytes to decoder"
                                + (encoderDone ? " (EOS)" : ""));
                    }

                    encoder.releaseOutputBuffer(encoderStatus, false);
                }
            }

            // Check for output from the decoder.  We want to do this on every loop to avoid
            // the possibility of stalling the pipeline.  We use a short timeout to avoid
            // burning CPU if the decoder is hard at work but the next frame isn't quite ready.
            //
            // If we're decoding to a Surface, we'll get notified here as usual but the
            // ByteBuffer references will be null.  The data is sent to Surface instead.
            if (decoderConfigured) {
                int decoderStatus = decoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                if (decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    if (VERBOSE) Log.d(TAG, "no output from decoder available");
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    if (VERBOSE) Log.d(TAG, "decoder output buffers changed");
                    decoderOutputBuffers = decoder.getOutputBuffers();
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    // this happens before the first frame is returned
                    MediaFormat decoderOutputFormat = decoder.getOutputFormat();
                    decoderColorFormat =
                            decoderOutputFormat.getInteger(MediaFormat.KEY_COLOR_FORMAT);
                    if (VERBOSE) Log.d(TAG, "decoder output format changed: " +
                            decoderOutputFormat);
                } else if (decoderStatus < 0) {
                    fail("unexpected result from deocder.dequeueOutputBuffer: " + decoderStatus);
                } else {  // decoderStatus >= 0
                    if (!toSurface) {
                        ByteBuffer outputFrame = decoderOutputBuffers[decoderStatus];

                        outputFrame.position(info.offset);
                        outputFrame.limit(info.offset + info.size);

                        rawSize += info.size;
                        if (info.size == 0) {
                            if (VERBOSE) Log.d(TAG, "got empty frame");
                        } else {
                            if (VERBOSE) Log.d(TAG, "decoded, checking frame " + checkIndex);
                            checkFrame(checkIndex++, decoderColorFormat, outputFrame);
                        }

                        if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            if (VERBOSE) Log.d(TAG, "output EOS");
                            outputDone = true;
                        }
                        decoder.releaseOutputBuffer(decoderStatus, false /*render*/);
                    } else {
                        if (VERBOSE) Log.d(TAG, "surface decoder given buffer " + decoderStatus +
                                " (size=" + info.size + ")");
                        rawSize += info.size;
                        if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            if (VERBOSE) Log.d(TAG, "output EOS");
                            outputDone = true;
                        }

                        boolean doRender = (info.size != 0);

                        // As soon as we call releaseOutputBuffer, the buffer will be forwarded
                        // to SurfaceTexture to convert to a texture.  The API doesn't guarantee
                        // that the texture will be available before the call returns, so we
                        // need to wait for the onFrameAvailable callback to fire.
                        decoder.releaseOutputBuffer(decoderStatus, doRender);
                        if (doRender) {
                            if (VERBOSE) Log.d(TAG, "awaiting frame " + checkIndex);
                            surfaceStuff.awaitNewImage(checkIndex++);
                        }
                    }
                }
            }
        }

        if (checkIndex != NUM_FRAMES) {
            fail("expected " + NUM_FRAMES + " frames, only decoded " + checkIndex);
        }

        if (VERBOSE) Log.d(TAG, "decoded " + checkIndex + " frames at "
                + mWidth + "x" + mHeight + ": raw=" + rawSize + ", enc=" + encodedSize);
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ioe) {
                Log.w(TAG, "failed closing debug file");
                throw new RuntimeException(ioe);
            }
        }
    }

    /**
     * Generates data for frame N into the supplied buffer.  We have an 8-frame animation
     * sequence that wraps around.  It looks like this:
     * <pre>
     *   0 1 2 3
     *   7 6 5 4
     * </pre>
     * We draw one of the eight rectangles and leave the rest set to the zero-fill color.
     */
    private void generateFrame(int frameIndex, int colorFormat, byte[] frameData) {
        final int HALF_WIDTH = mWidth / 2;
        boolean semiPlanar = isSemiPlanarYUV(colorFormat);

        // Set to zero.  In YUV this is a dull green.
        Arrays.fill(frameData, (byte) 0);

        int startX, startY, countX, countY;

        frameIndex %= 8;
        //frameIndex = (frameIndex / 8) % 8;    // use this instead for debug -- easier to see
        if (frameIndex < 4) {
            startX = frameIndex * (mWidth / 4);
            startY = 0;
        } else {
            startX = (7 - frameIndex) * (mWidth / 4);
            startY = mHeight / 2;
        }

        for (int y = startY + (mHeight/2) - 1; y >= startY; --y) {
            for (int x = startX + (mWidth/4) - 1; x >= startX; --x) {
                if (semiPlanar) {
                    // full-size Y, followed by CbCr pairs at half resolution
                    // e.g. Nexus 4 OMX.qcom.video.encoder.avc COLOR_FormatYUV420SemiPlanar
                    // e.g. Galaxy Nexus OMX.TI.DUCATI1.VIDEO.H264E
                    //        OMX_TI_COLOR_FormatYUV420PackedSemiPlanar
                    frameData[y * mWidth + x] = (byte) TEST_Y;
                    if ((x & 0x01) == 0 && (y & 0x01) == 0) {
                        frameData[mWidth*mHeight + y * HALF_WIDTH + x] = (byte) TEST_U;
                        frameData[mWidth*mHeight + y * HALF_WIDTH + x + 1] = (byte) TEST_V;
                    }
                } else {
                    // full-size Y, followed by quarter-size Cb and quarter-size Cr
                    // e.g. Nexus 10 OMX.Exynos.AVC.Encoder COLOR_FormatYUV420Planar
                    // e.g. Nexus 7 OMX.Nvidia.h264.encoder COLOR_FormatYUV420Planar
                    frameData[y * mWidth + x] = (byte) TEST_Y;
                    if ((x & 0x01) == 0 && (y & 0x01) == 0) {
                        frameData[mWidth*mHeight + (y/2) * HALF_WIDTH + (x/2)] = (byte) TEST_U;
                        frameData[mWidth*mHeight + HALF_WIDTH * (mHeight / 2) +
                                  (y/2) * HALF_WIDTH + (x/2)] = (byte) TEST_V;
                    }
                }
            }
        }

        if (false) {
            // make sure that generate and check agree
            Log.d(TAG, "SPOT CHECK");
            checkFrame(frameIndex, colorFormat, ByteBuffer.wrap(frameData));
            Log.d(TAG, "SPOT CHECK DONE");
        }
    }

    /**
     * Performs a simple check to see if the frame is more or less right.
     * <p>
     * See {@link generateFrame} for a description of the layout.  The idea is to sample
     * one pixel from the middle of the 8 regions, and verify that the correct one has
     * the non-background color.  We can't know exactly what the video encoder has done
     * with our frames, so we just check to see if it looks like more or less the right thing.
     * <p>
     * Throws a failure if the frame looks wrong.
     */
    private void checkFrame(int frameIndex, int colorFormat, ByteBuffer frameData) {
        // Check for color formats we don't understand.  There is no requirement for video
        // decoders to use a "mundane" format, so we just give a pass on proprietary formats.
        // e.g. Nexus 4 0x7FA30C03 OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar64x32Tile2m8ka
        if (!isRecognizedFormat(colorFormat)) {
            Log.d(TAG, "unable to check frame contents for colorFormat=" +
                    Integer.toHexString(colorFormat));
            return;
        }

        final int HALF_WIDTH = mWidth / 2;
        boolean frameFailed = false;
        boolean semiPlanar = isSemiPlanarYUV(colorFormat);

        for (int i = 0; i < 8; i++) {
            int x, y;
            if (i < 4) {
                x = i * (mWidth / 4) + (mWidth / 8);
                y = mHeight / 4;
            } else {
                x = (7 - i) * (mWidth / 4) + (mWidth / 8);
                y = (mHeight * 3) / 4;
            }

            int testY, testU, testV;
            if (semiPlanar) {
                // Galaxy Nexus uses OMX_TI_COLOR_FormatYUV420PackedSemiPlanar
                testY = frameData.get(y * mWidth + x) & 0xff;
                testU = frameData.get(mWidth*mHeight + 2*(y/2) * HALF_WIDTH + 2*(x/2)) & 0xff;
                testV = frameData.get(mWidth*mHeight + 2*(y/2) * HALF_WIDTH + 2*(x/2) + 1) & 0xff;
            } else {
                // Nexus 10, Nexus 7 use COLOR_FormatYUV420Planar
                testY = frameData.get(y * mWidth + x) & 0xff;
                testU = frameData.get(mWidth*mHeight + (y/2) * HALF_WIDTH + (x/2)) & 0xff;
                testV = frameData.get(mWidth*mHeight + HALF_WIDTH * (mHeight / 2) +
                        (y/2) * HALF_WIDTH + (x/2)) & 0xff;
            }

            boolean failed = false;
            if (i == frameIndex % 8) {
                failed = !isColorClose(testY, TEST_Y) ||
                         !isColorClose(testU, TEST_U) ||
                         !isColorClose(testV, TEST_V);
            } else {
                // should be our zeroed-out buffer
                failed = !isColorClose(testY, 0) ||
                         !isColorClose(testU, 0) ||
                         !isColorClose(testV, 0);
            }
            if (failed) {
                Log.w(TAG, "Bad frame " + frameIndex + " (rect=" + i + ": Y=" + testY +
                        " U=" + testU + " V=" + testV + ")");
                frameFailed = true;
            }
        }

        if (frameFailed) {
            fail("bad frame (" + frameIndex + ")");
        }
    }

    /**
     * Returns true if the actual color value is close to the expected color value.
     */
    static boolean isColorClose(int actual, int expected) {
        if (expected < 5) {
            return actual < (expected + 5);
        } else if (expected > 250) {
            return actual > (expected - 5);
        } else {
            return actual > (expected - 5) && actual < (expected + 5);
        }
    }

    /**
     * Holds state associated with a Surface used for output.
     * <p>
     * By default, the Surface will be using a BufferQueue in asynchronous mode, so we
     * will likely miss a number of frames.
     */
    private static class SurfaceStuff implements SurfaceTexture.OnFrameAvailableListener {
        private static final int EGL_OPENGL_ES2_BIT = 4;

        private EGL10 mEGL;
        private EGLDisplay mEGLDisplay;
        private EGLContext mEGLContext;
        private EGLSurface mEGLSurface;

        private SurfaceTexture mSurfaceTexture;
        private Surface mSurface;

        private Object mFrameSyncObject = new Object();     // guards mFrameAvailable
        private boolean mFrameAvailable;

        private int mWidth;
        private int mHeight;

        private TextureRender mTextureRender;

        public SurfaceStuff(int width, int height) {
            mWidth = width;
            mHeight = height;

            eglSetup();

            mTextureRender = new TextureRender();
            mTextureRender.surfaceCreated();

            // Even if we don't access the SurfaceTexture after the constructor returns, we
            // still need to keep a reference to it.  The Surface doesn't retain a reference
            // at the Java level, so if we don't either then the object can get GCed, which
            // causes the native finalizer to run.
            if (VERBOSE) Log.d(TAG, "textureID=" + mTextureRender.getTextureId());
            mSurfaceTexture = new SurfaceTexture(mTextureRender.getTextureId());

            // This doesn't work if SurfaceStuff is created on the thread that CTS started for
            // these test cases.
            //
            // The CTS-created thread has a Looper, and the SurfaceTexture constructor will
            // create a Handler that uses it.  The "frame available" message is delivered
            // there, but since we're not a Looper-based thread we'll never see it.  For
            // this to do anything useful, SurfaceStuff must be created on a thread without
            // a Looper, so that SurfaceTexture uses the main application Looper instead.
            //
            // Java language note: passing "this" out of a constructor is generally unwise,
            // but we should be able to get away with it here.
            mSurfaceTexture.setOnFrameAvailableListener(this);

            mSurface = new Surface(mSurfaceTexture);
        }

        /**
         * Prepares EGL.  We want a GLES 2.0 context and a surface that supports pbuffer.
         */
        private void eglSetup() {
            mEGL = (EGL10)EGLContext.getEGL();
            mEGLDisplay = mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (!mEGL.eglInitialize(mEGLDisplay, null)) {
                fail("unable to initialize EGL10");
            }

            // Configure surface for pbuffer and OpenGL ES 2.0.  We want enough RGB bits
            // to be able to tell if the frame is reasonable.
            int[] attribList = {
                    EGL10.EGL_RED_SIZE, 8,
                    EGL10.EGL_GREEN_SIZE, 8,
                    EGL10.EGL_BLUE_SIZE, 8,
                    EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
                    EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                    EGL10.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] numConfigs = new int[1];
            if (!mEGL.eglChooseConfig(mEGLDisplay, attribList, configs, 1, numConfigs)) {
                fail("unable to find RGB888+pbuffer EGL config");
            }

            // Configure context for OpenGL ES 2.0.
            int[] attrib_list = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL10.EGL_NONE
            };
            mEGLContext = mEGL.eglCreateContext(mEGLDisplay, configs[0], EGL10.EGL_NO_CONTEXT,
                    attrib_list);
            checkEglError("eglCreateContext");
            assertNotNull(mEGLContext);

            // Create a pbuffer surface.  By using this for output, we can use glReadPixels
            // to test values in the output.
            int[] surfaceAttribs = {
                    EGL10.EGL_WIDTH, mWidth,
                    EGL10.EGL_HEIGHT, mHeight,
                    EGL10.EGL_NONE
            };
            mEGLSurface = mEGL.eglCreatePbufferSurface(mEGLDisplay, configs[0], surfaceAttribs);
            checkEglError("eglCreatePbufferSurface");
            assertNotNull(mEGLSurface);

            if (!mEGL.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext)) {
                fail("eglMakeCurrent failed");
            }
        }

        /**
         * Checks for EGL errors.
         */
        private void checkEglError(String msg) {
            boolean failed = false;
            int error;
            while ((error = mEGL.eglGetError()) != EGL10.EGL_SUCCESS) {
                Log.e(TAG, msg + ": EGL error: 0x" + Integer.toHexString(error));
                failed = true;
            }
            if (failed) {
                fail("EGL error encountered (see log)");
            }
        }


        /**
         * Returns the Surface that the MediaCodec will draw onto.
         */
        public Surface getSurface() {
            return mSurface;
        }

        /**
         * Latches the next buffer into the texture, and checks it for validity.  Must be
         * called from the thread that created the SurfaceStuff object, after the
         * onFrameAvailable callback has signaled that new data is available.
         */
        public void awaitNewImage(int frameIndex) {
            final int TIMEOUT_MS = 500;

            synchronized (mFrameSyncObject) {
                while (!mFrameAvailable) {
                    try {
                        // Wait for onFrameAvailable() to signal us.  Use a timeout to avoid
                        // stalling the test if it doesn't arrive.
                        mFrameSyncObject.wait(TIMEOUT_MS);
                        if (!mFrameAvailable) {
                            // TODO: if "spurious wakeup", continue while loop
                            fail("Surface frame wait timed out");
                        }
                    } catch (InterruptedException ie) {
                        // shouldn't happen
                        throw new RuntimeException(ie);
                    }
                }
                mFrameAvailable = false;
            }

            // Latch the data, render it to the Surface, then check how it looks.
            mTextureRender.checkGlError("before updateTexImage");
            mSurfaceTexture.updateTexImage();
            mTextureRender.drawFrame(mSurfaceTexture);
            checkSurfaceFrame(frameIndex);
        }

        @Override
        public void onFrameAvailable(SurfaceTexture st) {
            if (VERBOSE) Log.d(TAG, "new frame available");
            synchronized (mFrameSyncObject) {
                if (mFrameAvailable) {
                    Log.e(TAG, "mFrameAvailable already set, frame could be dropped");
                    fail("dropped a frame");
                }
                mFrameAvailable = true;
                mFrameSyncObject.notifyAll();
            }
        }


        /**
         * Checks the frame for correctness.
         */
        private void checkSurfaceFrame(int frameIndex) {
            ByteBuffer pixelBuf = ByteBuffer.allocateDirect(4); // TODO - reuse this
            boolean frameFailed = false;

            for (int i = 0; i < 8; i++) {
                // Note the coordinates are inverted on the Y-axis in GL.
                int x, y;
                if (i < 4) {
                    x = i * (mWidth / 4) + (mWidth / 8);
                    y = (mHeight * 3) / 4;
                } else {
                    x = (7 - i) * (mWidth / 4) + (mWidth / 8);
                    y = mHeight / 4;
                }

                GLES20.glReadPixels(x, y, 1, 1, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixelBuf);
                int r = pixelBuf.get(0) & 0xff;
                int g = pixelBuf.get(1) & 0xff;
                int b = pixelBuf.get(2) & 0xff;

                boolean failed = false;
                if (i == frameIndex % 8) {
                    // colored rect
                    failed = !isColorClose(r, TEST_R1) ||
                            !isColorClose(g, TEST_G1) ||
                            !isColorClose(b, TEST_B1);
                } else {
                    // zero background color
                    failed = !isColorClose(r, TEST_R0) ||
                            !isColorClose(g, TEST_G0) ||
                            !isColorClose(b, TEST_B0);
                }
                if (failed) {
                    Log.w(TAG, "Bad frame " + frameIndex + " (rect=" + i + ": r=" + r +
                            " g=" + g + " b=" + b + ")");
                    frameFailed = true;
                }
            }

            if (frameFailed) {
                fail("bad frame (" + frameIndex + ")");
            }
        }
    }
}

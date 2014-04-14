/*
 * Copyright 2014 The Android Open Source Project
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

package android.hardware.camera2.cts.helpers;

import android.graphics.Rect;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CameraMetadata.Key;
import android.hardware.camera2.Size;
import android.hardware.camera2.cts.CameraTestUtils;
import android.util.Log;

import junit.framework.Assert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helpers to get common static info out of the camera.
 *
 * <p>Avoid boiler plate by putting repetitive get/set patterns in this class.</p>
 *
 * <p>Attempt to be durable against the camera device having bad or missing metadata
 * by providing reasonable defaults and logging warnings when that happens.</p>
 */
public class StaticMetadata {

    private static final String TAG = "StaticMetadata";
    private static final int IGNORE_SIZE_CHECK = -1;

    // TODO: don't hardcode, generate from metadata XML
    private static final int SENSOR_INFO_EXPOSURE_TIME_RANGE_SIZE = 2;
    private static final int SENSOR_INFO_EXPOSURE_TIME_RANGE_MIN = 0;
    private static final int SENSOR_INFO_EXPOSURE_TIME_RANGE_MAX = 1;
    private static final long SENSOR_INFO_EXPOSURE_TIME_RANGE_MIN_AT_MOST = 100000L; // 100us
    private static final long SENSOR_INFO_EXPOSURE_TIME_RANGE_MAX_AT_LEAST = 100000000; // 100ms
    private static final int SENSOR_INFO_SENSITIVITY_RANGE_SIZE = 2;
    private static final int SENSOR_INFO_SENSITIVITY_RANGE_MIN = 0;
    private static final int SENSOR_INFO_SENSITIVITY_RANGE_MAX = 1;
    private static final int SENSOR_INFO_SENSITIVITY_RANGE_MIN_AT_MOST = 100;
    private static final int SENSOR_INFO_SENSITIVITY_RANGE_MAX_AT_LEAST = 1600;
    private static final int STATISTICS_INFO_MAX_FACE_COUNT_MIN_AT_LEAST = 4;
    private static final int TONEMAP_MAX_CURVE_POINTS_AT_LEAST = 64;

    // TODO: Consider making this work across any metadata object, not just camera characteristics
    private final CameraCharacteristics mCharacteristics;
    private final CheckLevel mLevel;
    private final CameraErrorCollector mCollector;

    public enum CheckLevel {
        /** Only log warnings for metadata check failures. Execution continues. */
        WARN,
        /**
         * Use ErrorCollector to collect the metadata check failures, Execution
         * continues.
         */
        COLLECT,
        /** Assert the metadata check failures. Execution aborts. */
        ASSERT
    }

    /**
     * Construct a new StaticMetadata object.
     *
     *<p> Default constructor, only log warnings for the static metadata check failures</p>
     *
     * @param characteristics static info for a camera
     * @throws IllegalArgumentException if characteristics was null
     */
    public StaticMetadata(CameraCharacteristics characteristics) {
        this(characteristics, CheckLevel.WARN, /*collector*/null);
    }

    /**
     * Construct a new StaticMetadata object with {@link CameraErrorCollector}.
     * <p>
     * When level is not {@link CheckLevel.COLLECT}, the {@link CameraErrorCollector} will be
     * ignored, otherwise, it will be used to log the check failures.
     * </p>
     *
     * @param characteristics static info for a camera
     * @param collector The {@link CameraErrorCollector} used by this StaticMetadata
     * @throws IllegalArgumentException if characteristics or collector was null.
     */
    public StaticMetadata(CameraCharacteristics characteristics, CameraErrorCollector collector) {
        this(characteristics, CheckLevel.COLLECT, collector);
    }

    /**
     * Construct a new StaticMetadata object with {@link CheckLevel} and
     * {@link CameraErrorCollector}.
     * <p>
     * When level is not {@link CheckLevel.COLLECT}, the {@link CameraErrorCollector} will be
     * ignored, otherwise, it will be used to log the check failures.
     * </p>
     *
     * @param characteristics static info for a camera
     * @param level The {@link CheckLevel} of this StaticMetadata
     * @param collector The {@link CameraErrorCollector} used by this StaticMetadata
     * @throws IllegalArgumentException if characteristics was null or level was
     *         {@link CheckLevel.COLLECT} but collector was null.
     */
    public StaticMetadata(CameraCharacteristics characteristics, CheckLevel level,
            CameraErrorCollector collector) {
        if (characteristics == null) {
            throw new IllegalArgumentException("characteristics was null");
        }
        if (level == CheckLevel.COLLECT && collector == null) {
            throw new IllegalArgumentException("collector must valid when COLLECT level is set");
        }

        mCharacteristics = characteristics;
        mLevel = level;
        mCollector = collector;
    }

    /**
     * Get the CameraCharacteristics associated with this StaticMetadata.
     *
     * @return A non-null CameraCharacteristics object
     */
    public CameraCharacteristics getCharacteristics() {
        return mCharacteristics;
    }

    /**
     * Whether or not the hardware level reported by android.info.supportedHardwareLevel
     * is {@value CameraMetadata#INFO_SUPPORTED_HARDWARE_LEVEL_FULL}.
     *
     * <p>If the camera device is incorrectly reporting the hardwareLevel, this
     * will always return {@code false}.</p>
     *
     * @return true if the device is FULL, false otherwise.
     */
    public boolean isHardwareLevelFull() {
        // TODO: Make this key non-optional for all HAL3.2+ devices
        Integer hwLevel = getValueFromKeyNonNull(
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);

        // Bad. Missing metadata. Warning is logged.
        if (hwLevel == null) {
            return false;
        }

        // Normal. Device could be limited.
        int hwLevelInt = hwLevel;
        return hwLevelInt == CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_FULL;
    }

    /**
     * Whether or not the hardware level reported by android.info.supportedHardwareLevel
     * is {@value CameraMetadata#INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED}.
     *
     * <p>If the camera device is incorrectly reporting the hardwareLevel, this
     * will always return {@code true}.</p>
     *
     * @return true if the device is LIMITED, false otherwise.
     */
    public boolean isHardwareLevelLimited() {
        return !isHardwareLevelFull();
    }

    /**
     * Get the exposure time value and clamp to the range if needed.
     *
     * @param exposure Input exposure time value to check.
     * @return Exposure value in the legal range.
     */
    public long getExposureClampToRange(long exposure) {
        long minExposure = getExposureMinimumOrDefault(Long.MAX_VALUE);
        long maxExposure = getExposureMaximumOrDefault(Long.MIN_VALUE);
        if (minExposure > SENSOR_INFO_EXPOSURE_TIME_RANGE_MIN_AT_MOST) {
            failKeyCheck(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE,
                    String.format(
                    "Min value %d is too large, set to maximal legal value %d",
                    minExposure, SENSOR_INFO_EXPOSURE_TIME_RANGE_MIN_AT_MOST));
            minExposure = SENSOR_INFO_EXPOSURE_TIME_RANGE_MIN_AT_MOST;
        }
        if (maxExposure < SENSOR_INFO_EXPOSURE_TIME_RANGE_MAX_AT_LEAST) {
            failKeyCheck(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE,
                    String.format(
                    "Max value %d is too small, set to minimal legal value %d",
                    maxExposure, SENSOR_INFO_EXPOSURE_TIME_RANGE_MAX_AT_LEAST));
            maxExposure = SENSOR_INFO_EXPOSURE_TIME_RANGE_MAX_AT_LEAST;
        }

        return Math.max(minExposure, Math.min(maxExposure, exposure));
    }

    /**
     * Check if the camera device support focuser.
     *
     * @return true if camera device support focuser, false otherwise.
     */
    public boolean hasFocuser() {
        Key<Float> key = CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE;
        Float minFocusDistance = getValueFromKeyNonNull(key);

        if (minFocusDistance == null) {
            return false;
        }

        return (minFocusDistance > 0);
    }

    /**
     * Get max 3A regions and do sanity check.
     *
     * @return 3A max regions supported by the camera device
     */
    public int[] get3aMaxRegionsChecked() {
        Key<int[]> key = CameraCharacteristics.CONTROL_MAX_REGIONS;
        int[] regionCounts = getValueFromKeyNonNull(key);

        if (regionCounts == null) {
            return new int[]{0, 0, 0};
        }

        checkTrueForKey(key, " value should contain 3 elements", regionCounts.length == 3);
        return regionCounts;
    }

    /**
     * Get the available anti-banding modes.
     *
     * @return The array contains available anti-banding modes.
     */
    public byte[] getAeAvailableAntiBandingModesChecked() {
        Key<byte[]> key = CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES;
        byte[] modes = getValueFromKeyNonNull(key);

        boolean foundAuto = false;
        for (byte mode : modes) {
            checkTrueForKey(key, "mode value " + mode + " is out if range",
                    mode >= CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_OFF ||
                    mode <= CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_AUTO);
            if (mode == CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_AUTO) {
                foundAuto = true;
                return modes;
            }
        }
        // Must contain AUTO mode.
        checkTrueForKey(key, "AUTO mode is missing", foundAuto);

        return modes;
    }

    public Boolean getFlashInfoChecked() {
        Key<Boolean> key = CameraCharacteristics.FLASH_INFO_AVAILABLE;
        Boolean hasFlash = getValueFromKeyNonNull(key);

        // In case the failOnKey only gives warning.
        if (hasFlash == null) {
            return false;
        }

        return hasFlash;
    }

    public int[] getAvailableTestPatternModesChecked() {
        CameraMetadata.Key<int[]> key =
                CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES;
        int[] modes = getValueFromKeyNonNull(key);

        if (modes == null) {
            return new int[0];
        }

        int expectValue = CameraCharacteristics.SENSOR_TEST_PATTERN_MODE_OFF;
        Integer[] boxedModes = CameraTestUtils.toObject(modes);
        checkTrueForKey(key, " value must contain OFF mode",
                Arrays.asList(boxedModes).contains(expectValue));

        return modes;
    }

    /**
     * Get available thumbnail sizes and do the sanity check.
     *
     * @return The array of available thumbnail sizes
     */
    public Size[] getAvailableThumbnailSizesChecked() {
        Key<Size[]> key = CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES;
        Size[] sizes = getValueFromKeyNonNull(key);
        final List<Size> sizeList = Arrays.asList(sizes);

        // Size must contain (0, 0).
        checkTrueForKey(key, "size should contain (0, 0)", sizeList.contains(new Size(0, 0)));

        // Each size must be distinct.
        checkElementDistinct(key, sizeList);

        // Must be sorted in ascending order by area, by width if areas are same.
        List<Size> orderedSizes =
                CameraTestUtils.getAscendingOrderSizes(sizeList, /*ascending*/true);
        checkTrueForKey(key, "Sizes should be in ascending order: Original " + sizeList.toString()
                + ", Expected " + orderedSizes.toString(), orderedSizes.equals(sizeList));

        // TODO: Aspect ratio match, need wait for android.scaler.availableStreamConfigurations
        // implementation see b/12958122.

        return sizes;
    }

    /**
     * Get available focal lengths and do the sanity check.
     *
     * @return The array of available focal lengths
     */
    public float[] getAvailableFocalLengthsChecked() {
        Key<float[]> key = CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS;
        float[] focalLengths = getValueFromKeyNonNull(key);

        checkTrueForKey(key, "Array should contain at least one element", focalLengths.length >= 1);

        for (int i = 0; i < focalLengths.length; i++) {
            checkTrueForKey(key,
                    String.format("focalLength[%d] %f should be positive.", i, focalLengths[i]),
                    focalLengths[i] > 0);
        }
        checkElementDistinct(key, Arrays.asList(CameraTestUtils.toObject(focalLengths)));

        return focalLengths;
    }

    /**
     * Get available apertures and do the sanity check.
     *
     * @return The non-null array of available apertures
     */
    public float[] getAvailableAperturesChecked() {
        Key<float[]> key = CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES;
        float[] apertures = getValueFromKeyNonNull(key);

        checkTrueForKey(key, "Array should contain at least one element", apertures.length >= 1);

        for (int i = 0; i < apertures.length; i++) {
            checkTrueForKey(key,
                    String.format("apertures[%d] %f should be positive.", i, apertures[i]),
                    apertures[i] > 0);
        }
        checkElementDistinct(key, Arrays.asList(CameraTestUtils.toObject(apertures)));

        return apertures;
    }

    /**
     * Get and check available face detection modes.
     *
     * @return The non-null array of available face detection modes
     */
    public byte[] getAvailableFaceDetectModesChecked() {
        Key<byte[]> key = CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES;
        byte[] modes = getValueFromKeyNonNull(key);

        if (modes == null) {
            return new byte[0];
        }

        List<Byte> modeList = Arrays.asList(CameraTestUtils.toObject(modes));
        checkTrueForKey(key, "Array should contain OFF mode",
                modeList.contains((byte)CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF));
        checkElementDistinct(key, modeList);
        checkArrayValuesInRange(key, modes, (byte)CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF,
                (byte)CameraMetadata.STATISTICS_FACE_DETECT_MODE_FULL);

        return modes;
    }

    /**
     * Get and check max face detected count.
     *
     * @return max number of faces that can be detected
     */
    public int getMaxFaceCountChecked() {
        Key<Integer> key = CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT;
        Integer count = getValueFromKeyNonNull(key);

        if (count == null) {
            return 0;
        }

        List<Byte> faceDetectModes =
                Arrays.asList(CameraTestUtils.toObject(getAvailableFaceDetectModesChecked()));
        if (faceDetectModes.contains((byte)CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF) &&
                faceDetectModes.size() == 1) {
            checkTrueForKey(key, " value must be 0 if only OFF mode is supported in "
                    + "availableFaceDetectionModes", count == 0);
        } else {
            int maxFaceCountAtLeat = STATISTICS_INFO_MAX_FACE_COUNT_MIN_AT_LEAST;
            checkTrueForKey(key, " value must be no less than " + maxFaceCountAtLeat + " if SIMPLE"
                    + "or FULL is also supported in availableFaceDetectionModes",
                    count >= maxFaceCountAtLeat);
        }

        return count;
    }

    /**
     * Get and check the available tone map modes.
     *
     * @return the availalbe tone map modes
     */
    public byte[] getAvailableToneMapModesChecked() {
        Key<byte[]> key = CameraCharacteristics.TONEMAP_AVAILABLE_TONE_MAP_MODES;
        byte[] modes = getValueFromKeyNonNull(key);

        if (modes == null) {
            return new byte[0];
        }

        List<Byte> modeList = Arrays.asList(CameraTestUtils.toObject(modes));
        if (isHardwareLevelFull()) {
            checkTrueForKey(key, "Full-capability camera devices must always support"
                    + "CONTRAST_CURVE and FAST",
                    modeList.contains((byte)CameraMetadata.TONEMAP_MODE_CONTRAST_CURVE) &&
                    modeList.contains((byte)CameraMetadata.TONEMAP_MODE_FAST));
        }
        checkElementDistinct(key, modeList);
        checkArrayValuesInRange(key, modes, (byte)CameraMetadata.TONEMAP_MODE_CONTRAST_CURVE,
                (byte)CameraMetadata.TONEMAP_MODE_HIGH_QUALITY);

        return modes;
    }

    /**
     * Get and check max tonemap curve point.
     *
     * @return Max tonemap curve points.
     */
    public int getMaxTonemapCurvePointChecked() {
        Key<Integer> key = CameraCharacteristics.TONEMAP_MAX_CURVE_POINTS;
        Integer count = getValueFromKeyNonNull(key);

        if (count == null) {
            return 0;
        }

        List<Byte> modeList =
                Arrays.asList(CameraTestUtils.toObject(getAvailableToneMapModesChecked()));
        if (modeList.contains((byte)CameraMetadata.TONEMAP_MODE_CONTRAST_CURVE)) {
            checkTrueForKey(key, "Full-capability camera device must support maxCurvePoints "
                    + ">= " + TONEMAP_MAX_CURVE_POINTS_AT_LEAST,
                    count >= TONEMAP_MAX_CURVE_POINTS_AT_LEAST);
        }

        return count;
    }

    /**
     * Get and check pixel array size.
     */
    public Size getPixelArraySizeChecked() {
        Key<Size> key = CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE;
        Size pixelArray = getValueFromKeyNonNull(key);
        if (pixelArray == null) {
            return new Size(0, 0);
        }

        return pixelArray;
    }

    /**
     * Get and check active array size.
     */
    public Rect getActiveArraySizeChecked() {
        Key<Rect> key = CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE;
        Rect activeArray = getValueFromKeyNonNull(key);

        if (activeArray == null) {
            return new Rect(0, 0, 0, 0);
        }

        Size pixelArraySize = getPixelArraySizeChecked();
        checkTrueForKey(key, "values left/top are invalid", activeArray.left >= 0 && activeArray.top >= 0);
        checkTrueForKey(key, "values width/height are invalid",
                activeArray.width() <= pixelArraySize.getWidth() &&
                activeArray.height() <= pixelArraySize.getHeight());

        return activeArray;
    }

    /**
     * Get the sensitivity value and clamp to the range if needed.
     *
     * @param sensitivity Input sensitivity value to check.
     * @return Sensitivity value in legal range.
     */
    public int getSensitivityClampToRange(int sensitivity) {
        int minSensitivity = getSensitivityMinimumOrDefault(Integer.MAX_VALUE);
        int maxSensitivity = getSensitivityMaximumOrDefault(Integer.MIN_VALUE);
        if (minSensitivity > SENSOR_INFO_SENSITIVITY_RANGE_MIN_AT_MOST) {
            failKeyCheck(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE,
                    String.format(
                    "Min value %d is too large, set to maximal legal value %d",
                    minSensitivity, SENSOR_INFO_SENSITIVITY_RANGE_MIN_AT_MOST));
            minSensitivity = SENSOR_INFO_SENSITIVITY_RANGE_MIN_AT_MOST;
        }
        if (maxSensitivity < SENSOR_INFO_SENSITIVITY_RANGE_MAX_AT_LEAST) {
            failKeyCheck(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE,
                    String.format(
                    "Max value %d is too small, set to minimal legal value %d",
                    maxSensitivity, SENSOR_INFO_SENSITIVITY_RANGE_MAX_AT_LEAST));
            maxSensitivity = SENSOR_INFO_SENSITIVITY_RANGE_MAX_AT_LEAST;
        }

        return Math.max(minSensitivity, Math.min(maxSensitivity, sensitivity));
    }

    /**
     * Get the minimum value for a sensitivity range from android.sensor.info.sensitivityRange.
     *
     * <p>If the camera is incorrectly reporting values, log a warning and return
     * the default value instead, which is the largest minimum value required to be supported
     * by all camera devices.</p>
     *
     * @return The value reported by the camera device or the defaultValue otherwise.
     */
    public int getSensitivityMinimumOrDefault() {
        return getSensitivityMinimumOrDefault(SENSOR_INFO_SENSITIVITY_RANGE_MIN_AT_MOST);
    }

    /**
     * Get the minimum value for a sensitivity range from android.sensor.info.sensitivityRange.
     *
     * <p>If the camera is incorrectly reporting values, log a warning and return
     * the default value instead.</p>
     *
     * @param defaultValue Value to return if no legal value is available
     * @return The value reported by the camera device or the defaultValue otherwise.
     */
    public int getSensitivityMinimumOrDefault(int defaultValue) {
        return getArrayElementOrDefault(
                CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE,
                defaultValue,
                "minimum",
                SENSOR_INFO_SENSITIVITY_RANGE_MIN,
                SENSOR_INFO_SENSITIVITY_RANGE_SIZE);
    }

    /**
     * Get the maximum value for a sensitivity range from android.sensor.info.sensitivityRange.
     *
     * <p>If the camera is incorrectly reporting values, log a warning and return
     * the default value instead, which is the smallest maximum value required to be supported
     * by all camera devices.</p>
     *
     * @return The value reported by the camera device or the defaultValue otherwise.
     */
    public int getSensitivityMaximumOrDefault() {
        return getSensitivityMaximumOrDefault(SENSOR_INFO_SENSITIVITY_RANGE_MAX_AT_LEAST);
    }

    /**
     * Get the maximum value for a sensitivity range from android.sensor.info.sensitivityRange.
     *
     * <p>If the camera is incorrectly reporting values, log a warning and return
     * the default value instead.</p>
     *
     * @param defaultValue Value to return if no legal value is available
     * @return The value reported by the camera device or the defaultValue otherwise.
     */
    public int getSensitivityMaximumOrDefault(int defaultValue) {
        return getArrayElementOrDefault(
                CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE,
                defaultValue,
                "maximum",
                SENSOR_INFO_SENSITIVITY_RANGE_MAX,
                SENSOR_INFO_SENSITIVITY_RANGE_SIZE);
    }

    /**
     * Get the minimum value for an exposure range from android.sensor.info.exposureTimeRange.
     *
     * <p>If the camera is incorrectly reporting values, log a warning and return
     * the default value instead.</p>
     *
     * @param defaultValue Value to return if no legal value is available
     * @return The value reported by the camera device or the defaultValue otherwise.
     */
    public long getExposureMinimumOrDefault(long defaultValue) {
        return getArrayElementOrDefault(
                CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE,
                defaultValue,
                "minimum",
                SENSOR_INFO_EXPOSURE_TIME_RANGE_MIN,
                SENSOR_INFO_EXPOSURE_TIME_RANGE_SIZE);
    }

    /**
     * Get the minimum value for an exposure range from android.sensor.info.exposureTimeRange.
     *
     * <p>If the camera is incorrectly reporting values, log a warning and return
     * the default value instead, which is the largest minimum value required to be supported
     * by all camera devices.</p>
     *
     * @return The value reported by the camera device or the defaultValue otherwise.
     */
    public long getExposureMinimumOrDefault() {
        return getExposureMinimumOrDefault(SENSOR_INFO_EXPOSURE_TIME_RANGE_MIN_AT_MOST);
    }

    /**
     * Get the maximum value for an exposure range from android.sensor.info.exposureTimeRange.
     *
     * <p>If the camera is incorrectly reporting values, log a warning and return
     * the default value instead.</p>
     *
     * @param defaultValue Value to return if no legal value is available
     * @return The value reported by the camera device or the defaultValue otherwise.
     */
    public long getExposureMaximumOrDefault(long defaultValue) {
        return getArrayElementOrDefault(
                CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE,
                defaultValue,
                "maximum",
                SENSOR_INFO_EXPOSURE_TIME_RANGE_MAX,
                SENSOR_INFO_EXPOSURE_TIME_RANGE_SIZE);
    }

    /**
     * Get the maximum value for an exposure range from android.sensor.info.exposureTimeRange.
     *
     * <p>If the camera is incorrectly reporting values, log a warning and return
     * the default value instead, which is the smallest maximum value required to be supported
     * by all camera devices.</p>
     *
     * @return The value reported by the camera device or the defaultValue otherwise.
     */
    public long getExposureMaximumOrDefault() {
        return getExposureMaximumOrDefault(SENSOR_INFO_EXPOSURE_TIME_RANGE_MAX_AT_LEAST);
    }

    /**
     * Get aeAvailableModes and do the sanity check.
     *
     * <p>Depending on the check level this class has, for WAR or COLLECT levels,
     * If the aeMode list is invalid, return an empty mode array. The the caller doesn't
     * have to abort the execution even the aeMode list is invalid.</p>
     * @return AE available modes
     */
    public byte[] getAeAvailableModesChecked() {
        CameraMetadata.Key<byte[]> modesKey = CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES;
        byte[] modes = getValueFromKeyNonNull(modesKey);
        if (modes == null) {
            modes = new byte[0];
        }
        List<Integer> modeList = new ArrayList<Integer>();
        for (byte mode : modes) {
            modeList.add((int)(mode));
        }
        checkTrueForKey(modesKey, "value is empty", !modeList.isEmpty());

        // All camera device must support ON
        checkTrueForKey(modesKey, "values " + modeList.toString() + " must contain ON mode",
                modeList.contains(CameraMetadata.CONTROL_AE_MODE_ON));

        // All camera devices with flash units support ON_AUTO_FLASH and ON_ALWAYS_FLASH
        CameraMetadata.Key<Boolean> flashKey= CameraCharacteristics.FLASH_INFO_AVAILABLE;
        Boolean hasFlash = getValueFromKeyNonNull(flashKey);
        if (hasFlash == null) {
            hasFlash = false;
        }
        if (hasFlash) {
            boolean flashModeConsistentWithFlash =
                    modeList.contains(CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH) &&
                    modeList.contains(CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
            checkTrueForKey(modesKey,
                    "value must contain ON_AUTO_FLASH and ON_ALWAYS_FLASH and  when flash is" +
                    "available", flashModeConsistentWithFlash);
        } else {
            boolean flashModeConsistentWithoutFlash =
                    !(modeList.contains(CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH) ||
                    modeList.contains(CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH) ||
                    modeList.contains(CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE));
            checkTrueForKey(modesKey,
                    "value must not contain ON_AUTO_FLASH, ON_ALWAYS_FLASH and" +
                    "ON_AUTO_FLASH_REDEYE when flash is unavailable",
                    flashModeConsistentWithoutFlash);
        }

        // FULL mode camera devices always support OFF mode.
        boolean condition =
                !isHardwareLevelFull() || modeList.contains(CameraMetadata.CONTROL_AE_MODE_OFF);
        checkTrueForKey(modesKey, "Full capability device must have OFF mode", condition);

        // Boundary check.
        for (byte mode : modes) {
            checkTrueForKey(modesKey, "Value " + mode + " is out of bound",
                    mode >= CameraMetadata.CONTROL_AE_MODE_OFF
                    && mode <= CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE);
        }

        return modes;
    }

    /**
     * Get supported raw output sizes and do the check.
     *
     * @return Empty size array if raw output is not supported
     */
    public Size[] getRawOutputSizesChecked() {
        return getAvailableSizesForFormatChecked(ImageFormat.RAW_SENSOR,
                CameraCharacteristics.SCALER_AVAILABLE_STREAM_CONFIGURATIONS_OUTPUT);
    }

    /**
     * Get supported jpeg output sizes and do the check.
     *
     * @return Empty size array if jpeg output is not supported
     */
    public Size[] getJpegOutputSizeChecked() {
        return getAvailableSizesForFormatChecked(ImageFormat.JPEG,
                CameraCharacteristics.SCALER_AVAILABLE_STREAM_CONFIGURATIONS_OUTPUT);
    }

    /**
     * Get available sizes for given format
     *
     * @param format The format for the requested size array.
     * @param direction The stream direction, input or output.
     * @return The sizes of the given format, empty array if no available size is found.
     */
    public Size[] getAvailableSizesForFormatChecked(int format, int direction) {
        final int NUM_ELEMENTS_IN_STREAM_CONFIG = 4;
        ArrayList<Size> sizeList = new ArrayList<Size>();
        CameraMetadata.Key<int[]> key =
                CameraCharacteristics.SCALER_AVAILABLE_STREAM_CONFIGURATIONS;
        int[] config = getValueFromKeyNonNull(key);

        if (config == null) {
            return new Size[0];
        }

        checkTrueForKey(key, "array length is invalid", config.length
                % NUM_ELEMENTS_IN_STREAM_CONFIG == 0);
        // Round down to 4 boundary if it is not integer times of 4, to avoid array out of bound
        // in case the above check fails.
        int configLength = (config.length / NUM_ELEMENTS_IN_STREAM_CONFIG)
                * NUM_ELEMENTS_IN_STREAM_CONFIG;
        for (int i = 0; i < configLength; i += NUM_ELEMENTS_IN_STREAM_CONFIG) {
            if (config[i] == format && config[i+3] == direction) {
                sizeList.add(new Size(config[i+1], config[i+2]));
            }
        }

        Size[] sizes = new Size[sizeList.size()];
        return sizeList.toArray(sizes);
    }

    /**
     * Get available AE target fps ranges.
     *
     * @return Empty int array if aeAvailableTargetFpsRanges is invalid.
     */
    public int[] getAeAvailableTargetFpsRangesChecked() {
        final int NUM_ELEMENTS_IN_FPS_RANGE = 2;
        CameraMetadata.Key<int[]> key =
                CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES;
        int[] fpsRanges = getValueFromKeyNonNull(key);

        if (fpsRanges == null) {
            return new int[0];
        }

        checkTrueForKey(key, "array length is invalid", fpsRanges.length
                % NUM_ELEMENTS_IN_FPS_RANGE == 0);
        // Round down to 2 boundary if it is not integer times of 2, to avoid array out of bound
        // in case the above check fails.
        int fpsRangeLength = (fpsRanges.length / NUM_ELEMENTS_IN_FPS_RANGE)
                * NUM_ELEMENTS_IN_FPS_RANGE;
        int minFps, maxFps;
        long maxFrameDuration = getMaxFrameDurationChecked();
        for (int i = 0; i < fpsRangeLength; i += NUM_ELEMENTS_IN_FPS_RANGE) {
            minFps = fpsRanges[i];
            maxFps = fpsRanges[i + 1];
            checkTrueForKey(key, " min fps must be no larger than max fps!",
                    minFps > 0 && maxFps >= minFps);
            long maxDuration = (long) (1e9 / minFps);
            checkTrueForKey(key, String.format(
                    " the frame duration %d for min fps %d must smaller than maxFrameDuration %d",
                    maxDuration, minFps, maxFrameDuration), maxDuration <= maxFrameDuration);
        }

        return fpsRanges;
    }

    /**
     * Get max frame duration.
     *
     * @return 0 if maxFrameDuration is null
     */
    public long getMaxFrameDurationChecked() {
        CameraMetadata.Key<Long> key =
                CameraCharacteristics.SENSOR_INFO_MAX_FRAME_DURATION;
        Long maxDuration = getValueFromKeyNonNull(key);

        if (maxDuration == null) {
            return 0;
        }

        return maxDuration;
    }

    /**
     * Get available minimal frame durations for a given format.
     *
     * @param format One of the format from {@link ImageFormat}.
     * @return HashMap of minimal frame durations for different sizes, empty HashMap
     *         if availableMinFrameDurations is null.
     */
    public HashMap<Size, Long> getAvailableMinFrameDurationsForFormatChecked(int format) {
        final int NUM_ELEMENTS_IN_DURATIONS = 4;
        CameraMetadata.Key<long[]> key =
                CameraCharacteristics.SCALER_AVAILABLE_MIN_FRAME_DURATIONS;
        long[] minDurations = getValueFromKeyNonNull(key);
        HashMap<Size, Long> minDurationMap = new HashMap<Size, Long>();

        if (minDurations == null) {
            return minDurationMap;
        }

        checkTrueForKey(key, "array length is invalid", minDurations.length
                % NUM_ELEMENTS_IN_DURATIONS == 0);
        // Round down to 4 boundary if it is not integer times of 4, to avoid array out of bound
        // in case the above check fails.
        int durationLength = (minDurations.length / NUM_ELEMENTS_IN_DURATIONS)
                * NUM_ELEMENTS_IN_DURATIONS;
        for (int i = 0; i < durationLength; i += NUM_ELEMENTS_IN_DURATIONS) {
            if (minDurations[i] == format) {
                Size size = new Size((int)minDurations[i+1], (int)minDurations[i+2]);
                Long value = minDurations[i + 3];
                minDurationMap.put(size, value);
            }
        }

        return minDurationMap;
    }

    /**
     * Get the value in index for a fixed-size array from a given key.
     *
     * <p>If the camera device is incorrectly reporting values, log a warning and return
     * the default value instead.</p>
     *
     * @param key Key to fetch
     * @param defaultValue Default value to return if camera device uses invalid values
     * @param name Human-readable name for the array index (logging only)
     * @param index Array index of the subelement
     * @param size Expected fixed size of the array
     *
     * @return The value reported by the camera device, or the defaultValue otherwise.
     */
    private <T> T getArrayElementOrDefault(Key<?> key, T defaultValue, String name, int index,
            int size) {
        T elementValue = getArrayElementCheckRangeNonNull(
                key,
                index,
                size);

        if (elementValue == null) {
            failKeyCheck(key,
                    "had no valid " + name + " value; using default of " + defaultValue);
            elementValue = defaultValue;
        }

        return elementValue;
    }

    /**
     * Fetch an array sub-element from an array value given by a key.
     *
     * <p>
     * Prints a warning if the sub-element was null.
     * </p>
     *
     * <p>Use for variable-size arrays since this does not check the array size.</p>
     *
     * @param key Metadata key to look up
     * @param element A non-negative index value.
     * @return The array sub-element, or null if the checking failed.
     */
    private <T> T getArrayElementNonNull(Key<?> key, int element) {
        return getArrayElementCheckRangeNonNull(key, element, IGNORE_SIZE_CHECK);
    }

    /**
     * Fetch an array sub-element from an array value given by a key.
     *
     * <p>
     * Prints a warning if the array size does not match the size, or if the sub-element was null.
     * </p>
     *
     * @param key Metadata key to look up
     * @param element The index in [0,size)
     * @param size A positive size value or otherwise {@value #IGNORE_SIZE_CHECK}
     * @return The array sub-element, or null if the checking failed.
     */
    private <T> T getArrayElementCheckRangeNonNull(Key<?> key, int element, int size) {
        Object array = getValueFromKeyNonNull(key);

        if (array == null) {
            // Warning already printed
            return null;
        }

        if (size != IGNORE_SIZE_CHECK) {
            int actualLength = Array.getLength(array);
            if (actualLength != size) {
                failKeyCheck(key,
                        String.format("had the wrong number of elements (%d), expected (%d)",
                                actualLength, size));
                return null;
            }
        }

        @SuppressWarnings("unchecked")
        T val = (T) Array.get(array, element);

        if (val == null) {
            failKeyCheck(key, "had a null element at index" + element);
            return null;
        }

        return val;
    }

    /**
     * Gets the key, logging warnings for null values.
     */
    private <T> T getValueFromKeyNonNull(Key<T> key) {
        if (key == null) {
            throw new IllegalArgumentException("key was null");
        }

        T value = mCharacteristics.get(key);

        if (value == null) {
            failKeyCheck(key, "was null");
        }

        return value;
    }

    private void checkArrayValuesInRange(Key<byte[]> key, byte[] array, byte min, byte max) {
        for (byte value : array) {
            checkTrueForKey(key, String.format(" value is out of range [%d, %d]", min, max),
                    value <= max && value >= min);
        }
    }

    /**
     * Check the uniqueness of the values in a list.
     *
     * @param key The key to be checked
     * @param list The list contains the value of the key
     */
    private <U, T> void checkElementDistinct(Key<U> key, List<T> list) {
        // Each size must be distinct.
        Set<T> sizeSet = new HashSet<T>(list);
        checkTrueForKey(key, "Each size must be distinct", sizeSet.size() == list.size());
    }

    private <T> void checkTrueForKey(Key<T> key, String message, boolean condition) {
        if (!condition) {
            failKeyCheck(key, message);
        }
    }

    private <T> void failKeyCheck(Key<T> key, String message) {
        // TODO: Consider only warning once per key/message combination if it's too spammy.
        // TODO: Consider offering other options such as throwing an assertion exception
        String failureCause = String.format("The static info key '%s' %s", key.getName(), message);
        switch (mLevel) {
            case WARN:
                Log.w(TAG, failureCause);
                break;
            case COLLECT:
                mCollector.addMessage(failureCause);
                break;
            case ASSERT:
                Assert.fail(failureCause);
            default:
                throw new UnsupportedOperationException("Unhandled level " + mLevel);
        }
    }
}

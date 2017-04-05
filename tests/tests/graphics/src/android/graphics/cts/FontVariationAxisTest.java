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

package android.graphics.cts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import android.graphics.fonts.FontVariationAxis;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class FontVariationAxisTest {
    private static final float FLOT_EQUALITY_PREC = 1e-4f;  // precision for float equality.
    private static final String[] INVALID_TAGS = {
        "", "abcde", "\n\n\n\n", "\u0000bcd", "\u3042bcd"
    };

    @Test
    public void testConstruction() throws FontVariationAxis.InvalidFormatException {
        new FontVariationAxis("abcd", 1.0f);
        new FontVariationAxis("    ", 0.0f);
        new FontVariationAxis("A X ", -1.0f);
    }

    @Test
    public void testGetterTest() throws FontVariationAxis.InvalidFormatException {
        FontVariationAxis axis = new FontVariationAxis("abcd", 1.0f);
        assertEquals("abcd", axis.getTag());
        assertEquals(1.0f, axis.getStyleValue(), FLOT_EQUALITY_PREC);

        axis = new FontVariationAxis("    ", -1.0f);
        assertEquals("    ", axis.getTag());
        assertEquals(-1.0f, axis.getStyleValue(), FLOT_EQUALITY_PREC);
    }

    @Test
    public void testInvalidTagConstructionTest() {
        for (String invalidTag : INVALID_TAGS) {
            try {
                new FontVariationAxis(invalidTag, 0.0f);
                fail(invalidTag + " should be treated as invalid tag");
            } catch (FontVariationAxis.InvalidFormatException e) {
                // pass
            }
        }
    }

    @Test
    public void testFromFontVariationSetting_Single()
            throws FontVariationAxis.InvalidFormatException {
        FontVariationAxis[] axes = FontVariationAxis.fromFontVariationSettings("");
        assertNull(axes);

        axes = FontVariationAxis.fromFontVariationSettings(null);
        assertNull(axes);

        axes = FontVariationAxis.fromFontVariationSettings("'wdth' 1");
        assertEquals(1, axes.length);
        assertEquals("wdth", axes[0].getTag());
        assertEquals(1.0f, axes[0].getStyleValue(), FLOT_EQUALITY_PREC);

        axes = FontVariationAxis.fromFontVariationSettings("\"wdth\" 100");
        assertEquals(1, axes.length);
        assertEquals("wdth", axes[0].getTag());
        assertEquals(100.0f, axes[0].getStyleValue(), FLOT_EQUALITY_PREC);

        axes = FontVariationAxis.fromFontVariationSettings("   'wdth' 100");
        assertEquals(1, axes.length);
        assertEquals("wdth", axes[0].getTag());
        assertEquals(100.0f, axes[0].getStyleValue(), FLOT_EQUALITY_PREC);

        axes = FontVariationAxis.fromFontVariationSettings("\t'wdth' 0.5");
        assertEquals(1, axes.length);
        assertEquals("wdth", axes[0].getTag());
        assertEquals(0.5f, axes[0].getStyleValue(), FLOT_EQUALITY_PREC);

        axes = FontVariationAxis.fromFontVariationSettings("'AX  ' 1");
        assertEquals(1, axes.length);
        assertEquals("AX  ", axes[0].getTag());
        assertEquals(1.0f, axes[0].getStyleValue(), FLOT_EQUALITY_PREC);

        axes = FontVariationAxis.fromFontVariationSettings("'AX  '\t1");
        assertEquals(1, axes.length);
        assertEquals("AX  ", axes[0].getTag());
        assertEquals(1.0f, axes[0].getStyleValue(), FLOT_EQUALITY_PREC);

        axes = FontVariationAxis.fromFontVariationSettings("'AX  '\n1");
        assertEquals(1, axes.length);
        assertEquals("AX  ", axes[0].getTag());
        assertEquals(1.0f, axes[0].getStyleValue(), FLOT_EQUALITY_PREC);

        axes = FontVariationAxis.fromFontVariationSettings("'AX  '\r1");
        assertEquals(1, axes.length);
        assertEquals("AX  ", axes[0].getTag());
        assertEquals(1.0f, axes[0].getStyleValue(), FLOT_EQUALITY_PREC);

        axes = FontVariationAxis.fromFontVariationSettings("'AX  '\r\t\n 1");
        assertEquals(1, axes.length);
        assertEquals("AX  ", axes[0].getTag());
        assertEquals(1.0f, axes[0].getStyleValue(), FLOT_EQUALITY_PREC);

        axes = FontVariationAxis.fromFontVariationSettings("'wdth' 10,'AX  '\r1");
        assertEquals(2, axes.length);
        assertEquals("wdth", axes[0].getTag());
        assertEquals(10.0f, axes[0].getStyleValue(), FLOT_EQUALITY_PREC);
        assertEquals("AX  ", axes[1].getTag());
        assertEquals(1.0f, axes[1].getStyleValue(), FLOT_EQUALITY_PREC);
    }

    @Test
    public void testFromFontVariationSettings_Invalid() {
        for (String invalidTag : INVALID_TAGS) {
            try {
                FontVariationAxis.fromFontVariationSettings("'" + invalidTag + "' : 1.0");
                fail(invalidTag + " should be treated as invalid settings");
            } catch (FontVariationAxis.InvalidFormatException e) {
                // pass
            }
        }
        for (String invalidTag : INVALID_TAGS) {
            try {
                FontVariationAxis.fromFontVariationSettings(
                        "'" + invalidTag + "' : 1.0, 'wdth' 10");
                fail(invalidTag + " should be treated as invalid settings");
            } catch (FontVariationAxis.InvalidFormatException e) {
                // pass
            }
        }
    }

    @Test
    public void testtoFontVariationSettings() throws FontVariationAxis.InvalidFormatException {
        assertEquals("", FontVariationAxis.toFontVariationSettings(null));
        assertEquals("", FontVariationAxis.toFontVariationSettings(new FontVariationAxis[0]));

        final FontVariationAxis[] axes = {
            new FontVariationAxis("wght", 1.0f),
            new FontVariationAxis("    ", 2.0f),
            new FontVariationAxis("AX  ", 3.0f)
        };

        String stringData = FontVariationAxis.toFontVariationSettings(axes);
        FontVariationAxis[] newAxes = FontVariationAxis.fromFontVariationSettings(stringData);
        assertEquals(newAxes.length, axes.length);
        for (int i = 0; i < axes.length; ++i) {
            assertEquals(axes[i].getTag(), newAxes[i].getTag());
            assertEquals(axes[i].getStyleValue(), newAxes[i].getStyleValue(), FLOT_EQUALITY_PREC);
        }
    }
}
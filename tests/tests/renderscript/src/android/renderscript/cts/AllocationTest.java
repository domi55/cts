/*
 * Copyright (C) 2011 The Android Open Source Project
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

package android.renderscript.cts;

import com.android.cts.stub.R;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.AllocationAdapter;
import android.renderscript.Allocation.MipmapControl;
import android.renderscript.Element;
import android.renderscript.RSIllegalArgumentException;
import android.renderscript.Type;
import android.renderscript.Type.Builder;
import android.renderscript.Type.CubemapFace;

public class AllocationTest extends RSBaseGraphics {

    // Test power of two and non power of two, equal and non-equal sizes
    void createTypedHelper(Element e) {

        Type.Builder typeBuilder = new Type.Builder(mRS, e);
        for (int mips = 0; mips <= 1; mips ++) {
            boolean useMips = (mips == 1);

            for (int faces = 0; faces <= 1; faces++) {
                boolean useFaces = (faces == 1);

                for (int x = 1; x < 8; x ++) {
                    for (int y = 1; y < 8; y ++) {
                        typeBuilder.setMipmaps(useMips);
                        typeBuilder.setFaces(useFaces);
                        typeBuilder.setX(x).setY(y);
                        Allocation.createTyped(mRS, typeBuilder.create());
                    }
                }
            }
        }

    }

    void createTypedTextureHelper(Element e) {
        // No mips graphics
        Type.Builder typeBuilder = new Type.Builder(mRS, e);
        Allocation.createTyped(mRS, typeBuilder.setX(8).create(),
                               MipmapControl.MIPMAP_NONE,
                               Allocation.USAGE_GRAPHICS_TEXTURE);
        Allocation.createTyped(mRS, typeBuilder.setY(8).create(),
                               MipmapControl.MIPMAP_NONE,
                               Allocation.USAGE_GRAPHICS_TEXTURE);
        // No mips graphics and script
        Allocation.createTyped(mRS, typeBuilder.create(),
                               MipmapControl.MIPMAP_NONE,
                               Allocation.USAGE_GRAPHICS_TEXTURE |
                               Allocation.USAGE_SCRIPT);
        // With mips
        Allocation.createTyped(mRS, typeBuilder.create(),
                               MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
                               Allocation.USAGE_GRAPHICS_TEXTURE);
        Allocation.createTyped(mRS, typeBuilder.create(),
                               MipmapControl.MIPMAP_FULL,
                               Allocation.USAGE_GRAPHICS_TEXTURE |
                               Allocation.USAGE_SCRIPT);

        // Only texture npot
        Allocation.createTyped(mRS, typeBuilder.setX(7).setY(1).create(),
                               MipmapControl.MIPMAP_NONE,
                               Allocation.USAGE_GRAPHICS_TEXTURE);
        Allocation.createTyped(mRS, typeBuilder.setX(7).setY(3).create(),
                               MipmapControl.MIPMAP_NONE,
                               Allocation.USAGE_GRAPHICS_TEXTURE);
        Allocation.createTyped(mRS, typeBuilder.setX(7).setY(7).create(),
                               MipmapControl.MIPMAP_NONE,
                               Allocation.USAGE_GRAPHICS_TEXTURE);

        // Script and texture
        Allocation.createTyped(mRS, typeBuilder.setX(7).setY(1).create(),
                               MipmapControl.MIPMAP_NONE,
                               Allocation.USAGE_GRAPHICS_TEXTURE |
                               Allocation.USAGE_SCRIPT);
        Allocation.createTyped(mRS, typeBuilder.setX(7).setY(3).create(),
                               MipmapControl.MIPMAP_NONE,
                               Allocation.USAGE_GRAPHICS_TEXTURE |
                               Allocation.USAGE_SCRIPT);
        Allocation.createTyped(mRS, typeBuilder.setX(7).setY(7).create(),
                               MipmapControl.MIPMAP_NONE,
                               Allocation.USAGE_GRAPHICS_TEXTURE |
                               Allocation.USAGE_SCRIPT);
    }

    void createSizedHelper(Element e) {
        for (int i = 1; i <= 8; i ++) {
            Allocation A = Allocation.createSized(mRS, e, i);
            assertEquals(A.getType().getElement(), e);
            assertEquals(A.getType().getX(), i);
        }
    }

    public void testCreateTyped() {
         createTypedHelper(Element.A_8(mRS));
         createTypedHelper(Element.RGB_565(mRS));
         createTypedHelper(Element.RGB_888(mRS));
         createTypedHelper(Element.RGBA_8888(mRS));
         createTypedHelper(Element.F32(mRS));
         createTypedHelper(Element.F32_2(mRS));
         createTypedHelper(Element.F32_3(mRS));
         createTypedHelper(Element.F32_4(mRS));
         createTypedHelper(Element.BOOLEAN(mRS));
         createTypedHelper(Element.F64(mRS));
         createTypedHelper(Element.I8(mRS));
         createTypedHelper(Element.I16(mRS));
         createTypedHelper(Element.I32(mRS));
         createTypedHelper(Element.I64(mRS));
         createTypedHelper(Element.U8(mRS));
         createTypedHelper(Element.U8_4(mRS));
         createTypedHelper(Element.U16(mRS));
         createTypedHelper(Element.U32(mRS));
         createTypedHelper(Element.U64(mRS));
         createTypedHelper(Element.MATRIX_2X2(mRS));
         createTypedHelper(Element.MATRIX_3X3(mRS));
         createTypedHelper(Element.MATRIX_4X4(mRS));
         createTypedHelper(Element.MESH(mRS));
         createTypedHelper(Element.PROGRAM_FRAGMENT(mRS));
         createTypedHelper(Element.PROGRAM_RASTER(mRS));
         createTypedHelper(Element.PROGRAM_STORE(mRS));
         createTypedHelper(Element.PROGRAM_VERTEX(mRS));
         createTypedHelper(Element.ALLOCATION(mRS));
         createTypedHelper(Element.SAMPLER(mRS));
         createTypedHelper(Element.SCRIPT(mRS));
         createTypedHelper(Element.TYPE(mRS));

         createTypedTextureHelper(Element.A_8(mRS));
         createTypedTextureHelper(Element.RGB_565(mRS));
         createTypedTextureHelper(Element.RGB_888(mRS));
         createTypedTextureHelper(Element.RGBA_8888(mRS));
    }

    public void testCreateSized() {
         createSizedHelper(Element.A_8(mRS));
         createSizedHelper(Element.RGB_565(mRS));
         createSizedHelper(Element.RGB_888(mRS));
         createSizedHelper(Element.RGBA_8888(mRS));
         createSizedHelper(Element.F32(mRS));
         createSizedHelper(Element.F32_2(mRS));
         createSizedHelper(Element.F32_3(mRS));
         createSizedHelper(Element.F32_4(mRS));
         createSizedHelper(Element.BOOLEAN(mRS));
         createSizedHelper(Element.F64(mRS));
         createSizedHelper(Element.I8(mRS));
         createSizedHelper(Element.I16(mRS));
         createSizedHelper(Element.I32(mRS));
         createSizedHelper(Element.I64(mRS));
         createSizedHelper(Element.U8(mRS));
         createSizedHelper(Element.U8_4(mRS));
         createSizedHelper(Element.U16(mRS));
         createSizedHelper(Element.U32(mRS));
         createSizedHelper(Element.U64(mRS));
         createSizedHelper(Element.MATRIX_2X2(mRS));
         createSizedHelper(Element.MATRIX_3X3(mRS));
         createSizedHelper(Element.MATRIX_4X4(mRS));
         createSizedHelper(Element.MESH(mRS));
         createSizedHelper(Element.PROGRAM_FRAGMENT(mRS));
         createSizedHelper(Element.PROGRAM_RASTER(mRS));
         createSizedHelper(Element.PROGRAM_STORE(mRS));
         createSizedHelper(Element.PROGRAM_VERTEX(mRS));
         createSizedHelper(Element.ALLOCATION(mRS));
         createSizedHelper(Element.SAMPLER(mRS));
         createSizedHelper(Element.SCRIPT(mRS));
         createSizedHelper(Element.TYPE(mRS));
    }

    static int bDimX = 48;
    static int bDimY = 8;

    void helperCreateFromBitmap(Bitmap B,
                                Allocation.MipmapControl mc) {
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
                for (int k = 0; k < 1; k++) {
                    for (int l = 0; l < 1; l++) {
                        int u = 0;
                        u |= (i * Allocation.USAGE_SCRIPT);
                        u |= (j * Allocation.USAGE_GRAPHICS_TEXTURE);
                        u |= (k * Allocation.USAGE_GRAPHICS_VERTEX);
                        u |= (l * Allocation.USAGE_GRAPHICS_CONSTANTS);
                        assertTrue(null !=
                            Allocation.createFromBitmap(mRS, B, mc, u));
                        assertTrue(null !=
                            Allocation.createCubemapFromBitmap(mRS, B, mc, u));
                    }
                }
            }
        }
    }

    public void testCreateFromBitmap() {
        Bitmap B = Bitmap.createBitmap(bDimX, bDimY, Bitmap.Config.ARGB_8888);
        Allocation.createFromBitmap(mRS, B);
        Allocation.createCubemapFromBitmap(mRS, B);
        for (Allocation.MipmapControl mc : Allocation.MipmapControl.values()) {
            helperCreateFromBitmap(B, mc);
        }

        try {
            int invalidUsage = 0x0020;
            Allocation.createFromBitmap(mRS, B,
                Allocation.MipmapControl.MIPMAP_NONE, invalidUsage);
            fail("should throw RSIllegalArgumentException.");
        } catch (RSIllegalArgumentException e) {
        }

        try {
            // width % 6 != 0
            Bitmap badB = Bitmap.createBitmap(47, 8, Bitmap.Config.ARGB_8888);
            Allocation.createCubemapFromBitmap(mRS, badB,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            fail("should throw RSIllegalArgumentException.");
        } catch (RSIllegalArgumentException e) {
        }

        try {
            // width / 6 != height
            Bitmap badB = Bitmap.createBitmap(48, 4, Bitmap.Config.ARGB_8888);
            Allocation.createCubemapFromBitmap(mRS, badB,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            fail("should throw RSIllegalArgumentException.");
        } catch (RSIllegalArgumentException e) {
        }

        try {
            // height not power of 2
            Bitmap badB = Bitmap.createBitmap(36, 6, Bitmap.Config.ARGB_8888);
            Allocation.createCubemapFromBitmap(mRS, badB,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            fail("should throw RSIllegalArgumentException.");
        } catch (RSIllegalArgumentException e) {
        }
    }

    public void testAllocationMipmapControl() {
        assertEquals(MipmapControl.MIPMAP_NONE,
                     MipmapControl.valueOf("MIPMAP_NONE"));
        assertEquals(MipmapControl.MIPMAP_FULL,
                     MipmapControl.valueOf("MIPMAP_FULL"));
        assertEquals(MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
                     MipmapControl.valueOf("MIPMAP_ON_SYNC_TO_TEXTURE"));
        // Make sure no new enums are added
        assertEquals(3, Allocation.MipmapControl.values().length);

        for (Allocation.MipmapControl mc : Allocation.MipmapControl.values()) {
            Type.Builder b = new Type.Builder(mRS, Element.U8(mRS));
            b.setX(8).setY(8);
            Allocation.createTyped(mRS, b.create(), mc,
                                   Allocation.USAGE_GRAPHICS_TEXTURE);
        }
    }

    public void testCubemapFaces() {
        Type.Builder b = new Type.Builder(mRS, Element.U8(mRS));
        b.setX(8).setY(8).setFaces(true);
        Allocation cubemap = Allocation.createTyped(mRS, b.create(),
                                                    MipmapControl.MIPMAP_NONE,
                                                    Allocation.USAGE_SCRIPT);
        AllocationAdapter adapter = AllocationAdapter.create2D(mRS, cubemap);
        for (Type.CubemapFace cf : Type.CubemapFace.values()) {
            adapter.setFace(cf);
        }
    }

    /*
     * Test all copy from/to routines for byte/short/int/float
     */

    void helperFloatCopy(int nElems) {
        Allocation A = Allocation.createSized(mRS, Element.F32(mRS), nElems);

        float src[], dst[];
        src = new float[nElems];
        dst = new float[nElems];
        for (int i = 0; i < nElems; i++) {
            src[i] = (float)i;
            dst[i] = -1.0f;
        }

        A.copyFrom(src);
        A.copyTo(dst);

        for (int i = 0; i < nElems; i++) {
            assertEquals(dst[i], src[i]);
        }
    }

    void helperByteCopy(int nElems) {
        Allocation A = Allocation.createSized(mRS, Element.I8(mRS), nElems);

        byte src[], dst[];
        src = new byte[nElems];
        dst = new byte[nElems];
        for (int i = 0; i < nElems; i++) {
            src[i] = (byte)i;
            dst[i] = -1;
        }

        A.copyFrom(src);
        A.copyTo(dst);

        for (int i = 0; i < nElems; i++) {
            assertEquals(dst[i], src[i]);
        }
    }

    void helperShortCopy(int nElems) {
        Allocation A = Allocation.createSized(mRS, Element.I16(mRS), nElems);

        short src[], dst[];
        src = new short[nElems];
        dst = new short[nElems];
        for (int i = 0; i < nElems; i++) {
            src[i] = (short)i;
            dst[i] = -1;
        }

        A.copyFrom(src);
        A.copyTo(dst);

        for (int i = 0; i < nElems; i++) {
            assertEquals(dst[i], src[i]);
        }
    }

    void helperIntCopy(int nElems) {
        Allocation A = Allocation.createSized(mRS, Element.I32(mRS), nElems);

        int src[], dst[];
        src = new int[nElems];
        dst = new int[nElems];
        for (int i = 0; i < nElems; i++) {
            src[i] = i;
            dst[i] = -1;
        }

        A.copyFrom(src);
        A.copyTo(dst);

        for (int i = 0; i < nElems; i++) {
            assertEquals(dst[i], src[i]);
        }
    }

    void helperBaseObjCopy(int nElems) {
        Allocation A =
            Allocation.createSized(mRS, Element.ELEMENT(mRS), nElems);
        Element E[] = new Element[nElems];
        for (int i = 0; i < nElems; i++) {
            E[i] = Element.BOOLEAN(mRS);
        }

        A.copyFrom(E);
    }

    void helperBitmapCopy(int x, int y) {
        Bitmap bSrc = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Bitmap bDst = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);

        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                bSrc.setPixel(i, j, 9);
                bDst.setPixel(i, j, 0);
            }
        }

        Type.Builder typeBuilder =
            new Type.Builder(mRS, Element.RGBA_8888(mRS));
        typeBuilder.setMipmaps(false);
        typeBuilder.setFaces(false);
        typeBuilder.setX(x).setY(y);
        Allocation A = Allocation.createTyped(mRS, typeBuilder.create());

        A.copyFrom(bSrc);
        A.copyTo(bDst);

        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                assertEquals(bSrc.getPixel(i, j), bDst.getPixel(i, j));
            }
        }
    }

    static int elemsToTest = 20;

    public void testCopyOperations() {
        for (int s = 8; s <= elemsToTest; s += 2) {
            helperFloatCopy(s);
            helperByteCopy(s);
            helperShortCopy(s);
            helperIntCopy(s);
            helperBaseObjCopy(s);
        }
        helperBitmapCopy(bDimX, bDimY);
    }
}



/*
 * Copyright (C) 2014 The Android Open Source Project
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

// Don't edit this file!  It is auto-generated by frameworks/rs/api/gen_runtime.

package android.renderscript.cts;

import android.renderscript.Allocation;
import android.renderscript.RSRuntimeException;
import android.renderscript.Element;

public class TestFrexp extends RSBaseCompute {

    private ScriptC_TestFrexp script;
    private ScriptC_TestFrexpRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestFrexp(mRS);
        scriptRelaxed = new ScriptC_TestFrexpRelaxed(mRS);
    }

    public class ArgumentsFloatIntFloat {
        public float inV;
        public int outIptr;
        public Target.Floaty out;
    }

    private void checkFrexpFloatIntFloat() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x57ae9fe07384e56dl, false);
        try {
            Allocation outIptr = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 1), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocOutIptr(outIptr);
            script.forEach_testFrexpFloatIntFloat(inV, out);
            verifyResultsFrexpFloatIntFloat(inV, outIptr, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFrexpFloatIntFloat: " + e.toString());
        }
        try {
            Allocation outIptr = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 1), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocOutIptr(outIptr);
            scriptRelaxed.forEach_testFrexpFloatIntFloat(inV, out);
            verifyResultsFrexpFloatIntFloat(inV, outIptr, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFrexpFloatIntFloat: " + e.toString());
        }
    }

    private void verifyResultsFrexpFloatIntFloat(Allocation inV, Allocation outIptr, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 1];
        inV.copyTo(arrayInV);
        int[] arrayOutIptr = new int[INPUTSIZE * 1];
        outIptr.copyTo(arrayOutIptr);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeFrexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (args.outIptr != arrayOutIptr[i * 1 + j]) {
                    valid = false;
                }
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append("Expected output outIptr: ");
                    message.append(String.format("%d", args.outIptr));
                    message.append("\n");
                    message.append("Actual   output outIptr: ");
                    message.append(String.format("%d", arrayOutIptr[i * 1 + j]));
                    if (args.outIptr != arrayOutIptr[i * 1 + j]) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 1 + j], Float.floatToRawIntBits(arrayOut[i * 1 + j]), arrayOut[i * 1 + j]));
                    if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkFrexpFloatIntFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkFrexpFloat2Int2Float2() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x544e0a688fe7701l, false);
        try {
            Allocation outIptr = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 2), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocOutIptr(outIptr);
            script.forEach_testFrexpFloat2Int2Float2(inV, out);
            verifyResultsFrexpFloat2Int2Float2(inV, outIptr, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFrexpFloat2Int2Float2: " + e.toString());
        }
        try {
            Allocation outIptr = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 2), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocOutIptr(outIptr);
            scriptRelaxed.forEach_testFrexpFloat2Int2Float2(inV, out);
            verifyResultsFrexpFloat2Int2Float2(inV, outIptr, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFrexpFloat2Int2Float2: " + e.toString());
        }
    }

    private void verifyResultsFrexpFloat2Int2Float2(Allocation inV, Allocation outIptr, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 2];
        inV.copyTo(arrayInV);
        int[] arrayOutIptr = new int[INPUTSIZE * 2];
        outIptr.copyTo(arrayOutIptr);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeFrexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (args.outIptr != arrayOutIptr[i * 2 + j]) {
                    valid = false;
                }
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append("Expected output outIptr: ");
                    message.append(String.format("%d", args.outIptr));
                    message.append("\n");
                    message.append("Actual   output outIptr: ");
                    message.append(String.format("%d", arrayOutIptr[i * 2 + j]));
                    if (args.outIptr != arrayOutIptr[i * 2 + j]) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 2 + j], Float.floatToRawIntBits(arrayOut[i * 2 + j]), arrayOut[i * 2 + j]));
                    if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkFrexpFloat2Int2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkFrexpFloat3Int3Float3() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x2afb1f097eb0e3bal, false);
        try {
            Allocation outIptr = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 3), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocOutIptr(outIptr);
            script.forEach_testFrexpFloat3Int3Float3(inV, out);
            verifyResultsFrexpFloat3Int3Float3(inV, outIptr, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFrexpFloat3Int3Float3: " + e.toString());
        }
        try {
            Allocation outIptr = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 3), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocOutIptr(outIptr);
            scriptRelaxed.forEach_testFrexpFloat3Int3Float3(inV, out);
            verifyResultsFrexpFloat3Int3Float3(inV, outIptr, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFrexpFloat3Int3Float3: " + e.toString());
        }
    }

    private void verifyResultsFrexpFloat3Int3Float3(Allocation inV, Allocation outIptr, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        int[] arrayOutIptr = new int[INPUTSIZE * 4];
        outIptr.copyTo(arrayOutIptr);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeFrexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (args.outIptr != arrayOutIptr[i * 4 + j]) {
                    valid = false;
                }
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append("Expected output outIptr: ");
                    message.append(String.format("%d", args.outIptr));
                    message.append("\n");
                    message.append("Actual   output outIptr: ");
                    message.append(String.format("%d", arrayOutIptr[i * 4 + j]));
                    if (args.outIptr != arrayOutIptr[i * 4 + j]) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkFrexpFloat3Int3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkFrexpFloat4Int4Float4() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x50b15d6c74635073l, false);
        try {
            Allocation outIptr = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 4), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocOutIptr(outIptr);
            script.forEach_testFrexpFloat4Int4Float4(inV, out);
            verifyResultsFrexpFloat4Int4Float4(inV, outIptr, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFrexpFloat4Int4Float4: " + e.toString());
        }
        try {
            Allocation outIptr = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 4), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocOutIptr(outIptr);
            scriptRelaxed.forEach_testFrexpFloat4Int4Float4(inV, out);
            verifyResultsFrexpFloat4Int4Float4(inV, outIptr, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFrexpFloat4Int4Float4: " + e.toString());
        }
    }

    private void verifyResultsFrexpFloat4Int4Float4(Allocation inV, Allocation outIptr, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        int[] arrayOutIptr = new int[INPUTSIZE * 4];
        outIptr.copyTo(arrayOutIptr);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeFrexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (args.outIptr != arrayOutIptr[i * 4 + j]) {
                    valid = false;
                }
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append("Expected output outIptr: ");
                    message.append(String.format("%d", args.outIptr));
                    message.append("\n");
                    message.append("Actual   output outIptr: ");
                    message.append(String.format("%d", arrayOutIptr[i * 4 + j]));
                    if (args.outIptr != arrayOutIptr[i * 4 + j]) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkFrexpFloat4Int4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testFrexp() {
        checkFrexpFloatIntFloat();
        checkFrexpFloat2Int2Float2();
        checkFrexpFloat3Int3Float3();
        checkFrexpFloat4Int4Float4();
    }
}

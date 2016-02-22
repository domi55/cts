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

// Don't edit this file!  It is auto-generated by frameworks/rs/api/generate.sh.

package android.renderscript.cts;

import android.renderscript.Allocation;
import android.renderscript.RSRuntimeException;
import android.renderscript.Element;
import android.renderscript.cts.Target;

import java.util.Arrays;

public class TestIlogb extends RSBaseCompute {

    private ScriptC_TestIlogb script;
    private ScriptC_TestIlogbRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestIlogb(mRS);
        scriptRelaxed = new ScriptC_TestIlogbRelaxed(mRS);
    }

    public class ArgumentsFloatInt {
        public float inV;
        public int out;
    }

    private void checkIlogbFloatInt() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x5664967bl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 1), INPUTSIZE);
            script.forEach_testIlogbFloatInt(inV, out);
            verifyResultsIlogbFloatInt(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloatInt: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testIlogbFloatInt(inV, out);
            verifyResultsIlogbFloatInt(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloatInt: " + e.toString());
        }
    }

    private void verifyResultsIlogbFloatInt(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 1];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        int[] arrayOut = new int[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (int) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatInt args = new ArgumentsFloatInt();
                args.inV = arrayInV[i];
                // Extract the outputs.
                args.out = arrayOut[i * 1 + j];
                // Ask the CoreMathVerifier to validate.
                String errorMessage = CoreMathVerifier.verifyIlogb(args);
                boolean valid = errorMessage == null;
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
                        message.append("\n");
                        message.append("Output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append(errorMessage);
                        message.append("Errors at");
                    }
                    message.append(" [");
                    message.append(Integer.toString(i));
                    message.append(", ");
                    message.append(Integer.toString(j));
                    message.append("]");
                }
            }
        }
        assertFalse("Incorrect output for checkIlogbFloatInt" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkIlogbFloat2Int2() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xb6f32a61l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 2), INPUTSIZE);
            script.forEach_testIlogbFloat2Int2(inV, out);
            verifyResultsIlogbFloat2Int2(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat2Int2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 2), INPUTSIZE);
            scriptRelaxed.forEach_testIlogbFloat2Int2(inV, out);
            verifyResultsIlogbFloat2Int2(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat2Int2: " + e.toString());
        }
    }

    private void verifyResultsIlogbFloat2Int2(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 2];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        int[] arrayOut = new int[INPUTSIZE * 2];
        Arrays.fill(arrayOut, (int) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatInt args = new ArgumentsFloatInt();
                args.inV = arrayInV[i * 2 + j];
                // Extract the outputs.
                args.out = arrayOut[i * 2 + j];
                // Ask the CoreMathVerifier to validate.
                String errorMessage = CoreMathVerifier.verifyIlogb(args);
                boolean valid = errorMessage == null;
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
                        message.append("\n");
                        message.append("Output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append(errorMessage);
                        message.append("Errors at");
                    }
                    message.append(" [");
                    message.append(Integer.toString(i));
                    message.append(", ");
                    message.append(Integer.toString(j));
                    message.append("]");
                }
            }
        }
        assertFalse("Incorrect output for checkIlogbFloat2Int2" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkIlogbFloat3Int3() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x9b3a97l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 3), INPUTSIZE);
            script.forEach_testIlogbFloat3Int3(inV, out);
            verifyResultsIlogbFloat3Int3(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat3Int3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 3), INPUTSIZE);
            scriptRelaxed.forEach_testIlogbFloat3Int3(inV, out);
            verifyResultsIlogbFloat3Int3(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat3Int3: " + e.toString());
        }
    }

    private void verifyResultsIlogbFloat3Int3(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        int[] arrayOut = new int[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (int) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatInt args = new ArgumentsFloatInt();
                args.inV = arrayInV[i * 4 + j];
                // Extract the outputs.
                args.out = arrayOut[i * 4 + j];
                // Ask the CoreMathVerifier to validate.
                String errorMessage = CoreMathVerifier.verifyIlogb(args);
                boolean valid = errorMessage == null;
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
                        message.append("\n");
                        message.append("Output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append(errorMessage);
                        message.append("Errors at");
                    }
                    message.append(" [");
                    message.append(Integer.toString(i));
                    message.append(", ");
                    message.append(Integer.toString(j));
                    message.append("]");
                }
            }
        }
        assertFalse("Incorrect output for checkIlogbFloat3Int3" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkIlogbFloat4Int4() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x4a434acdl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 4), INPUTSIZE);
            script.forEach_testIlogbFloat4Int4(inV, out);
            verifyResultsIlogbFloat4Int4(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat4Int4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 4), INPUTSIZE);
            scriptRelaxed.forEach_testIlogbFloat4Int4(inV, out);
            verifyResultsIlogbFloat4Int4(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat4Int4: " + e.toString());
        }
    }

    private void verifyResultsIlogbFloat4Int4(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        int[] arrayOut = new int[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (int) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatInt args = new ArgumentsFloatInt();
                args.inV = arrayInV[i * 4 + j];
                // Extract the outputs.
                args.out = arrayOut[i * 4 + j];
                // Ask the CoreMathVerifier to validate.
                String errorMessage = CoreMathVerifier.verifyIlogb(args);
                boolean valid = errorMessage == null;
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
                        message.append("\n");
                        message.append("Output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append(errorMessage);
                        message.append("Errors at");
                    }
                    message.append(" [");
                    message.append(Integer.toString(i));
                    message.append(", ");
                    message.append(Integer.toString(j));
                    message.append("]");
                }
            }
        }
        assertFalse("Incorrect output for checkIlogbFloat4Int4" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    public void testIlogb() {
        checkIlogbFloatInt();
        checkIlogbFloat2Int2();
        checkIlogbFloat3Int3();
        checkIlogbFloat4Int4();
    }
}

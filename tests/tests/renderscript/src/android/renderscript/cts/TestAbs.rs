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

#pragma version(1)
#pragma rs java_package_name(android.renderscript.cts)

// Don't edit this file!  It is auto-generated by frameworks/rs/api/gen_runtime.


uchar __attribute__((kernel)) testAbsCharUchar(char inN) {
    return abs(inN);
}

uchar2 __attribute__((kernel)) testAbsChar2Uchar2(char2 inN) {
    return abs(inN);
}

uchar3 __attribute__((kernel)) testAbsChar3Uchar3(char3 inN) {
    return abs(inN);
}

uchar4 __attribute__((kernel)) testAbsChar4Uchar4(char4 inN) {
    return abs(inN);
}

ushort __attribute__((kernel)) testAbsShortUshort(short inN) {
    return abs(inN);
}

ushort2 __attribute__((kernel)) testAbsShort2Ushort2(short2 inN) {
    return abs(inN);
}

ushort3 __attribute__((kernel)) testAbsShort3Ushort3(short3 inN) {
    return abs(inN);
}

ushort4 __attribute__((kernel)) testAbsShort4Ushort4(short4 inN) {
    return abs(inN);
}

uint __attribute__((kernel)) testAbsIntUint(int inN) {
    return abs(inN);
}

uint2 __attribute__((kernel)) testAbsInt2Uint2(int2 inN) {
    return abs(inN);
}

uint3 __attribute__((kernel)) testAbsInt3Uint3(int3 inN) {
    return abs(inN);
}

uint4 __attribute__((kernel)) testAbsInt4Uint4(int4 inN) {
    return abs(inN);
}

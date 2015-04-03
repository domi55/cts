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

// Don't edit this file!  It is auto-generated by frameworks/rs/api/generate.sh.

#pragma version(1)
#pragma rs java_package_name(android.renderscript.cts)

rs_allocation gAllocOutExponent;

float __attribute__((kernel)) testFrexpFloatIntFloat(float inV, unsigned int x) {
    int outExponent = 0;
    float out = frexp(inV, &outExponent);
    rsSetElementAt_int(gAllocOutExponent, outExponent, x);
    return out;
}

float2 __attribute__((kernel)) testFrexpFloat2Int2Float2(float2 inV, unsigned int x) {
    int2 outExponent = 0;
    float2 out = frexp(inV, &outExponent);
    rsSetElementAt_int2(gAllocOutExponent, outExponent, x);
    return out;
}

float3 __attribute__((kernel)) testFrexpFloat3Int3Float3(float3 inV, unsigned int x) {
    int3 outExponent = 0;
    float3 out = frexp(inV, &outExponent);
    rsSetElementAt_int3(gAllocOutExponent, outExponent, x);
    return out;
}

float4 __attribute__((kernel)) testFrexpFloat4Int4Float4(float4 inV, unsigned int x) {
    int4 outExponent = 0;
    float4 out = frexp(inV, &outExponent);
    rsSetElementAt_int4(gAllocOutExponent, outExponent, x);
    return out;
}

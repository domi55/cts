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

rs_allocation gAllocInDenominator;
rs_allocation gAllocOutQuotient;

float __attribute__((kernel)) testRemquoFloatFloatIntFloat(float inNumerator, unsigned int x) {
    float inDenominator = rsGetElementAt_float(gAllocInDenominator, x);
    int outQuotient = 0;
    float out = remquo(inNumerator, inDenominator, &outQuotient);
    rsSetElementAt_int(gAllocOutQuotient, outQuotient, x);
    return out;
}

float2 __attribute__((kernel)) testRemquoFloat2Float2Int2Float2(float2 inNumerator, unsigned int x) {
    float2 inDenominator = rsGetElementAt_float2(gAllocInDenominator, x);
    int2 outQuotient = 0;
    float2 out = remquo(inNumerator, inDenominator, &outQuotient);
    rsSetElementAt_int2(gAllocOutQuotient, outQuotient, x);
    return out;
}

float3 __attribute__((kernel)) testRemquoFloat3Float3Int3Float3(float3 inNumerator, unsigned int x) {
    float3 inDenominator = rsGetElementAt_float3(gAllocInDenominator, x);
    int3 outQuotient = 0;
    float3 out = remquo(inNumerator, inDenominator, &outQuotient);
    rsSetElementAt_int3(gAllocOutQuotient, outQuotient, x);
    return out;
}

float4 __attribute__((kernel)) testRemquoFloat4Float4Int4Float4(float4 inNumerator, unsigned int x) {
    float4 inDenominator = rsGetElementAt_float4(gAllocInDenominator, x);
    int4 outQuotient = 0;
    float4 out = remquo(inNumerator, inDenominator, &outQuotient);
    rsSetElementAt_int4(gAllocOutQuotient, outQuotient, x);
    return out;
}

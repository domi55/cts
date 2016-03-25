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

#pragma version(1)
#pragma rs java_package_name(android.renderscript.cts)


float __attribute__((kernel)) testNativeRecipFloatFloat(float inV) {
    return native_recip(inV);
}

float2 __attribute__((kernel)) testNativeRecipFloat2Float2(float2 inV) {
    return native_recip(inV);
}

float3 __attribute__((kernel)) testNativeRecipFloat3Float3(float3 inV) {
    return native_recip(inV);
}

float4 __attribute__((kernel)) testNativeRecipFloat4Float4(float4 inV) {
    return native_recip(inV);
}

half __attribute__((kernel)) testNativeRecipHalfHalf(half inV) {
    return native_recip(inV);
}

half2 __attribute__((kernel)) testNativeRecipHalf2Half2(half2 inV) {
    return native_recip(inV);
}

half3 __attribute__((kernel)) testNativeRecipHalf3Half3(half3 inV) {
    return native_recip(inV);
}

half4 __attribute__((kernel)) testNativeRecipHalf4Half4(half4 inV) {
    return native_recip(inV);
}

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

#include <android/log.h>
#include <jni.h>
#include <string.h>

#include "seccomp-tests/tests/test_harness.h"

// Forward declare from seccomp_bpf_tests.c.
extern "C" {
struct __test_metadata* get_seccomp_test_list();
}

static const char TAG[] = "SecompBpfTest-Native";

jboolean android_security_cts_SeccompBpfTest_runKernelUnitTest(
      JNIEnv* env, jobject thiz __unused, jstring name) {
#if defined(ARCH_SUPPORTS_SECCOMP)
    const char* nameStr = env->GetStringUTFChars(name, nullptr);

    for (struct __test_metadata* t = get_seccomp_test_list(); t; t = t->next) {
        if (strcmp(t->name, nameStr) == 0) {
            __android_log_print(ANDROID_LOG_INFO, TAG, "Start: %s", t->name);
            __run_test(t);
            __android_log_print(ANDROID_LOG_INFO, TAG, "%s: %s",
                t->passed ? "PASS" : "FAIL", t->name);
            return t->passed;
        }
    }
#endif  // ARCH_SUPPORTS_SECCOMP

    return false;
}

static JNINativeMethod methods[] = {
    { "runKernelUnitTest", "(Ljava/lang/String;)Z",
        (void*)android_security_cts_SeccompBpfTest_runKernelUnitTest },
};

int register_android_os_cts_SeccompTest(JNIEnv* env) {
    jclass clazz = env->FindClass("android/os/cts/SeccompTest");
    return env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(JNINativeMethod));
}

#
# Copyright (C) 2014 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME := CtsSplitApp_armeabi-v7a
LOCAL_SDK_VERSION := current

LOCAL_JAVA_RESOURCE_DIRS := raw

# tag this module as a cts test artifact
LOCAL_COMPATIBILITY_SUITE := cts vts general-tests

LOCAL_CERTIFICATE := cts/hostsidetests/appsecurity/certs/cts-testkey1
LOCAL_AAPT_FLAGS := --version-code 100 --replace-version

# Disable AAPT2 to fix:
# unknown option '--replace-version'.
# TODO(b/79755007): Re-enable AAPT2 when it supports the missing features.
LOCAL_USE_AAPT2 := false

include $(BUILD_CTS_SUPPORT_PACKAGE)

# Copyright (C) 2009 The Android Open Source Project
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

LOCAL_PATH:= $(call my-dir)

ifeq ($(BUILD_CTSCORE_PACKAGE),)
    $(error BUILD_CTSCORE_PACKAGE must be defined)
endif

#
# Concurrent Tests
##########################################################
include $(CLEAR_VARS)

# don't include this package in any target
LOCAL_MODULE_TAGS := optional
# and when built explicitly put it in the data partition
LOCAL_MODULE_PATH := $(TARGET_OUT_DATA_APPS)

LOCAL_SRC_FILES := $(call all-java-files-under,../../../../libcore/concurrent/src/test/java) \
	$(call all-java-files-under,../../../../libcore/luni/src/test/java/junit) \
	$(call all-java-files-under,../../../../libcore/support/src/test/java/)

LOCAL_PACKAGE_NAME := android.core.tests.concurrent

include $(BUILD_CTSCORE_PACKAGE)

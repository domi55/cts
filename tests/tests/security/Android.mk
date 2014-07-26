# Copyright (C) 2011 The Android Open Source Project
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

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := tests

LOCAL_STATIC_JAVA_LIBRARIES := ctstestserver ctstestrunner ctsdeviceutil guava

LOCAL_JAVA_LIBRARIES := android.test.runner

LOCAL_JNI_SHARED_LIBRARIES := libctssecurity_jni

LOCAL_SRC_FILES := $(call all-java-files-under, src)\
                   src/android/security/cts/activity/ISecureRandomService.aidl

LOCAL_PACKAGE_NAME := CtsSecurityTestCases

LOCAL_SDK_VERSION := current

intermediates.COMMON := $(call intermediates-dir-for,APPS,$(LOCAL_PACKAGE_NAME),,COMMON)

sepolicy_asset_dir := $(intermediates.COMMON)/assets

LOCAL_ASSET_DIR := $(sepolicy_asset_dir)

include $(BUILD_CTS_PACKAGE)

selinux_policy.xml := $(sepolicy_asset_dir)/selinux_policy.xml
selinux_policy_parser := packages/experimental/SELinux/CTS/src/gen_SELinux_CTS.py
general_sepolicy_policy.conf := $(call intermediates-dir-for,ETC,general_sepolicy.conf)/general_sepolicy.conf
$(selinux_policy.xml): PRIVATE_POLICY_PARSER := $(selinux_policy_parser)
$(selinux_policy.xml): $(general_sepolicy_policy.conf) $(selinux_policy_parser)
	mkdir -p $(dir $@)
	$(PRIVATE_POLICY_PARSER) $< $@ neverallow_only=t

$(R_file_stamp): $(selinux_policy.xml)

include $(call all-makefiles-under,$(LOCAL_PATH))

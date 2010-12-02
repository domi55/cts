#
# Copyright (C) 2010 The Android Open Source Project
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

# Makefile for producing CTS coverage reports.
# Run "make cts-test-coverage" in the $ANDROID_BUILD_TOP directory.

include cts/CtsTestCaseList.mk

CTS_API_COVERAGE_EXE := $(HOST_OUT_EXECUTABLES)/cts-api-coverage
DEXDEPS_EXE := $(HOST_OUT_EXECUTABLES)/dexdeps

COVERAGE_OUT := $(HOST_OUT)/cts/api-coverage
COVERAGE_RES := cts/tools/cts-api-coverage/res

cts-test-coverage-report := $(COVERAGE_OUT)/test-coverage.xml
cts-verifier-coverage-report := $(COVERAGE_OUT)/verifier-coverage.xml
api-coverage-css := $(COVERAGE_OUT)/api-coverage.css
api-coverage-xsl := $(COVERAGE_OUT)/api-coverage.xsl

CTS_API_COVERAGE_DEPENDENCIES := $(CTS_API_COVERAGE_EXE) $(DEXDEPS_EXE) $(ACP) \
		$(api-coverage-css) $(api-coverage-xsl)

$(cts-test-coverage-report) : $(CTS_COVERAGE_TEST_CASE_LIST) $(CTS_API_COVERAGE_DEPENDENCIES)
	$(call generate-coverage-report,"CTS Tests API Coverage Report",\
			$(CTS_COVERAGE_TEST_CASE_LIST),xml,$(HOST_OUT)/cts/api-coverage,test-coverage.xml)

$(cts-verifier-coverage-report) : CtsVerifier $(CTS_API_COVERAGE_DEPENDENCIES)
	$(call generate-coverage-report,"CTS Verifier API Coverage Report",\
			CtsVerifier,xml,$(HOST_OUT)/cts/api-coverage,verifier-coverage.xml)

$(api-coverage-css) : $(COVERAGE_RES)/api-coverage.css $(ACP)
	$(call copy-coverage-resource,api-coverage.css,$(api-coverage-css))

$(api-coverage-xsl) : $(COVERAGE_RES)/api-coverage.xsl $(ACP)
	$(call copy-coverage-resource,api-coverage.xsl,$(api-coverage-xsl))

.PHONY: cts-test-coverage
cts-test-coverage : $(cts-test-coverage-report)

.PHONY: cts-verifier-coverage
cts-verifier-coverage : $(cts-verifier-coverage-report)

# Put the test coverage report in the dist dir if "cts" is among the build goals.
ifneq ($(filter cts, $(MAKECMDGOALS)),)
  $(call dist-for-goals, cts, $(cts-test-coverage-report):cts-test-coverage-report.xml)
  $(call dist-for-goals, cts, $(cts-verifier-coverage-report):cts-verifier-coverage-report.xml)
  $(call dist-for-goals, cts, $(api-coverage-css):api-coverage.css)
  $(call dist-for-goals, cts, $(api-coverage-xsl):api-coverage.xsl)
endif

# Arguments;
#  1 - Name of the report printed out on the screen
#  2 - Name of APK packages that will be scanned to generate the report
#  3 - Format of the report
#  4 - Output directory to put the report
#  5 - Output file name of the report
define generate-coverage-report
	$(hide) mkdir -p $(4)

	$(foreach testcase,$(2),$(eval $(call add-testcase-apk,$(testcase))))
	$(hide) $(CTS_API_COVERAGE_EXE) -d $(DEXDEPS_EXE) -f $(3) -o $(4)/$(5) $(TEST_APKS)

	@echo $(1): file://$(ANDROID_BUILD_TOP)/$(4)/$(5)
endef

define add-testcase-apk
	TEST_APKS += $(call intermediates-dir-for,APPS,$(1))/package.apk
endef

# Arguments:
#  1 - Name of the resources to copy like "api-coverage.css" with no path.
#  2 - Destination file name of the copied resource
define copy-coverage-resource
	$(hide) mkdir -p `dirname $(2)`
	$(hide) $(ACP) $(COVERAGE_RES)/$(1) $(2)
endef

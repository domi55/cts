# Build the unit tests.

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := tests

LOCAL_C_INCLUDES:= \
    bionic \
    bionic/libstdc++/include \
    external/gtest/include \
    system/media/wilhelm/include \
    external/stlport/stlport \
    system/media/wilhelm/src/ut

LOCAL_SRC_FILES:= \
    src/XAObjectCreationTest.cpp

LOCAL_SHARED_LIBRARIES := \
  libutils \
  libOpenMAXAL \
  libstlport

LOCAL_STATIC_LIBRARIES := \
    libgtest

LOCAL_MODULE:= NativeMediaTest_XA

LOCAL_MODULE_PATH := $(TARGET_OUT_DATA)/nativetest

include $(BUILD_EXECUTABLE)
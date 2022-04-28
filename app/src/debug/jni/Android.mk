LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ndk-test

LOCAL_SRC_FILES := test_android_ndk.c

include $(BUILD_SHARED_LIBRARY)
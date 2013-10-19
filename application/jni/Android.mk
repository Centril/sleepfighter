LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := sleepfighter-minesweeper
LOCAL_SRC_FILES := sleepfighter-minesweeper.cpp
LOCAL_CFLAGS    := -Werror

include $(BUILD_SHARED_LIBRARY)

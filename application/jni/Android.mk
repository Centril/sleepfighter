LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := sleepfighter-minesweeper
FILE_LIST		:= $(wildcard $(LOCAL_PATH)/minesweeper/src/*.cpp)
LOCAL_SRC_FILES	:= $(FILE_LIST:$(LOCAL_PATH)/%=%)
LOCAL_CFLAGS    := -Werror

include $(BUILD_SHARED_LIBRARY)

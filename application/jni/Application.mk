NDK_TOOLCHAIN_VERSION := 4.8

APP_PROJECT_PATH	:=	$(call my-dir)
APP_BUILD_SCRIPT	:=	$(APP_PROJECT_PATH)/Android.mk

APP_MODULES			:=	sleepfighter-minesweeper
APP_OPTIM			:=	release
APP_ABI				:=	all
APP_STL				:=	gnustl_static
LOCAL_CPPFLAGS		:=	-std=c++11 -pthread -frtti -fexceptions
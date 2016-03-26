#include <jni.h>
#include <linux/uinput.h>
#include <linux/input.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>

#define TAG "CVirtualMouse"
#define logv(msg) __android_log_print(ANDROID_LOG_VERBOSE, TAG, msg);
#define die(str, args...) do{ \
    perror(str);\
    exit(EXIT_FAILURE);\
} while (0)

int fd;

JNIEXPORT void JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_mouseMove(JNIEnv *env, jobject instance, jint x, jint y) {
    //TODO 向鼠标发送消息
}

JNIEXPORT jint JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_open(JNIEnv *env, jobject instance) {
    fd = open("/dev/uinput", O_WRONLY | O_NDELAY);
    if (fd < 0) {
        return errno;
    }
    return JNI_TRUE;
}

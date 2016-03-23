#include <jni.h>
#include <linux/uinput.h>
#include <linux/input.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#define die(str, args...) do{ \
    perror(str);\
    exit(EXIT_FAILURE);\
} while (0)

int fd;

JNIEXPORT void JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_mouseMove(JNIEnv *env, jobject instance, jint x, jint y) {

}

JNIEXPORT jboolean JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_open(JNIEnv *env, jobject instance) {
    fd = open("/dev/uinput", O_WRONLY | O_NDELAY);
    if (fd < 0) {
//        die("err open");
        //TODO 解决打开的权限问题
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

#include <jni.h>
#include <linux/uinput.h>
#include <fcntl.h>
#include <stdio.h>
#include <android/log.h>

#define TAG "CVirtualMouse"
#define logv(msg, ...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, msg);
#define die(str, ...) do{ \
    logv(str, ...);\
    return errno;\
} while (0)

int fd;

//void logd(const char* msg, ...) {
//    va_list args;
//    int count = 0;
//    int maxArgs = 31;
//    char* array[maxArgs + 1];
//
//    char* arg;
//    va_start(args, msg);
//    while (count <= maxArgs) {
//        array[count++] =  va_arg(args, const char*);
//    }
//    array[count] = 0;
//    va_end(args);
//    __android_log_print(ANDROID_LOG_DEBUG, TAG, msg, array);
//}

JNIEXPORT void JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_mouseMove(JNIEnv *env, jobject instance, jint x, jint y) {
    //FIXME 修正鼠标移动缓慢的问题
    __android_log_print(ANDROID_LOG_DEBUG, TAG, "fd:%d move to %d, %d", fd, x, y);
    struct input_event event;
    size_t eventSize = sizeof(event);

    memset(&event, 0, eventSize);
    gettimeofday(&event.time, NULL);
    event.type = EV_REL;
    event.code = REL_X;
    event.value = x;
    write(fd, &event, eventSize);

    memset(&event, 0, eventSize);
    gettimeofday(&event.time, NULL);
    event.type = EV_REL;
    event.code = REL_Y;
    event.value = y;
    write(fd, &event, eventSize);
}

JNIEXPORT jint JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_open(JNIEnv *env, jobject instance) {
    fd = open("/dev/uinput", O_WRONLY | O_NDELAY);
    if (fd < 0) {
        return errno;
    }
    __android_log_print(ANDROID_LOG_DEBUG, TAG, "fd: %d", fd);

    ioctl(fd, UI_SET_EVBIT, EV_KEY);
    ioctl(fd, UI_SET_EVBIT, EV_REL);
    ioctl(fd, UI_SET_RELBIT, REL_X);
    ioctl(fd, UI_SET_RELBIT, REL_Y);
    //显示鼠标的关键一行
    ioctl(fd, UI_SET_KEYBIT, BTN_MOUSE);

    struct uinput_user_dev dev;
    memset(&dev, 0, sizeof(dev));
    char *name = "virtual mouse";
    strncpy(dev.name, name, sizeof(name));
    dev.id.bustype = BUS_USB;
    dev.id.version = 4;

    ssize_t ret = write(fd, &dev, sizeof(dev));

    int creRes = ioctl(fd, UI_DEV_CREATE);
    if (creRes < 0) {
        return errno;
    }

    if (ret < 0)
        return errno;
    return JNI_TRUE;
}

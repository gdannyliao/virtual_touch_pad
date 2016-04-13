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
int send(__u16 type, __u16 code, __s32 value) {
    struct input_event event;
    size_t eventSize = sizeof(event);

    memset(&event, 0, eventSize);
    gettimeofday(&event.time, NULL);
    event.type = type;
    event.code = code;
    event.value = value;

    ssize_t res = 0;
    size_t leftCount = eventSize;
    do {
        res = write(fd, &event, eventSize);
        leftCount -= res;
    } while (leftCount > 0);
}

JNIEXPORT void JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_mouseMove(JNIEnv *env, jobject instance, jfloat x, jfloat y) {
    __android_log_print(ANDROID_LOG_DEBUG, TAG, "fd:%d move to %f, %f", fd, x, y);

    send(EV_REL, REL_X, x);
    send(EV_REL, REL_Y, y);
    //下面这行可以将两次事件间隔开来，解决了鼠标移动不顺畅的问题
    send(EV_SYN, SYN_REPORT, 0);
}

JNIEXPORT void JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_click(JNIEnv *env, jobject instance, jfloat x, jfloat y) {
    __android_log_print(ANDROID_LOG_DEBUG, TAG, "click: %f, %f", x, y);
    send(EV_KEY, BTN_MOUSE, 1);
    send(EV_SYN, SYN_REPORT, 0);

    send(EV_KEY, BTN_MOUSE, 0);
    send(EV_SYN, SYN_REPORT, 0);
    //FIXME 貌似鼠标可以点击了，但点击的位置应该不正确。点击自己了，造成一直会有鼠标事件
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
    ioctl(fd, UI_SET_KEYBIT, BTN_LEFT);
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

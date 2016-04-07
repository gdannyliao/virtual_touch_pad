//
// Created by jkl on 16/3/23.
//

#ifndef VIRTUAL_TOUCH_PAD_VRITUALMOUSE_H
#define VIRTUAL_TOUCH_PAD_VRITUALMOUSE_H

#include <jni.h>
JNIEXPORT void JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_mouseMove(JNIEnv *env, jobject instance, jint x, jint y);


JNIEXPORT jboolean JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_open(JNIEnv *env, jobject instance);

JNIEXPORT void JNICALL
Java_com_ggdsn_virtualtouchpad_TouchPad_click(JNIEnv *env, jobject instance, jfloat x, jfloat y);

#endif //VIRTUAL_TOUCH_PAD_VRITUALMOUSE_H

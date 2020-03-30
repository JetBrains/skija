#include <iostream>
#include <jni.h>
#include "SkRefCnt.h"

static void unrefSkRefCnt(SkRefCnt* p) {
    if (p->unique())
        std::cout << "Deleting [" << p << "]" << std::endl;
    else
        std::cout << "Unref [" << p << "]" << std::endl;
    p->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_RefCnt_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&unrefSkRefCnt));
}

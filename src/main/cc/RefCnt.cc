#include <iostream>
#include <jni.h>
#include "SkRefCnt.h"

static void unrefSkRefCnt(SkRefCnt* p) {
    std::cout << "unrefSkRefCnt p:" << p << ", unique:" << p->unique() << std::endl;
    p->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_RefCnt_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&unrefSkRefCnt));
}

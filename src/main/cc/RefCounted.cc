#include <iostream>
#include <jni.h>
#include "SkRefCnt.h"
#include "interop.hh"

static void unrefSkRefCnt(SkRefCnt* p) {
    if (p->unique())
        std::cout << "Deleting [" << p << "]" << std::endl;
    else
        std::cout << "Unref [" << p << "] " << getRefCnt(p) << " - 1" << std::endl;
    p->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_RefCounted_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&unrefSkRefCnt));
}

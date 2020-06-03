#include <iostream>
#include <jni.h>
#include "SkRefCnt.h"
#include "interop.hh"

class SkRefCntHack {
public:
    void* x;
    mutable std::atomic<int32_t> fRefCnt;
};

int32_t getRefCnt(SkRefCnt* ref) {
    return reinterpret_cast<SkRefCntHack*>(ref)->fRefCnt.load(std::memory_order_relaxed);
}

void unrefSkRefCnt(SkRefCnt* p) {
    // if (p->unique())
    //     std::cout << "Deleting [" << p << "]" << std::endl;
    // else
    //     std::cout << "Unref [" << p << "] " << getRefCnt(p) << " - 1" << std::endl;
    p->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_RefCounted_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&unrefSkRefCnt));
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_RefCounted_nGetRefCount(JNIEnv* env, jclass jclass, jlong ptr) {
    SkRefCnt* instance = reinterpret_cast<SkRefCnt*>(static_cast<uintptr_t>(ptr));
    return getRefCnt(instance);
}

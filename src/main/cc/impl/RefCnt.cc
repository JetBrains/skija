#include <jni.h>
#include "SkRefCnt.h"

class SkRefCntHack {
public:
    void* x;
    mutable std::atomic<int32_t> fRefCnt;
};

void unrefSkRefCnt(SkRefCnt* p) {
    p->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_impl_RefCnt__1nGetFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&unrefSkRefCnt));
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_impl_RefCnt__1nGetRefCount(JNIEnv* env, jclass jclass, jlong ptr) {
    SkRefCnt* instance = reinterpret_cast<SkRefCnt*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<SkRefCntHack*>(instance)->fRefCnt.load(std::memory_order_relaxed);
}

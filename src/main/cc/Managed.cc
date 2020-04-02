#include <iostream>
#include <jni.h>

typedef void (*FreeFunction)(void*);

extern "C" JNIEXPORT void JNICALL Java_skija_Managed_applyNativeFinalizer
  (JNIEnv* env, jclass jclass, jlong ptr, jlong nativeFinalizerPtr) {
    void* nativePtr = reinterpret_cast<void*>(static_cast<uintptr_t>(ptr));
    FreeFunction nativeFinalizer = reinterpret_cast<FreeFunction>(static_cast<uintptr_t>(nativeFinalizerPtr));
    nativeFinalizer(nativePtr);
}
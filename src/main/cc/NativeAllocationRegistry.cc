#include <iostream>
#include <jni.h>

typedef void (*FreeFunction)(void*);

extern "C" JNIEXPORT void JNICALL Java_skija_NativeAllocationRegistry_applyFreeFunction
  (JNIEnv* env, jclass jclass, jlong freeFunction, jlong ptr) {
    void* nativePtr = reinterpret_cast<void*>(static_cast<uintptr_t>(ptr));
    FreeFunction nativeFreeFunction = reinterpret_cast<FreeFunction>(static_cast<uintptr_t>(freeFunction));
    nativeFreeFunction(nativePtr);
}
#include <jni.h>
#include "../interop.hh"

namespace {
    jclass klass_IllegalArgumentException = nullptr;
    jmethodID method_Ctor = nullptr;
}

extern "C" {
    JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_impl_BufferUtil__1nGetByteBufferFromPointer
      (JNIEnv *env, jclass, jlong ptr, jint size) {
        return env->NewDirectByteBuffer(jlongToPtr<void*>(ptr), size);
    }

    JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_impl_BufferUtil__1nGetPointerFromByteBuffer
      (JNIEnv *env, jclass, jobject buffer) {
        return ptrToJlong(env->GetDirectBufferAddress(buffer));
    }
}
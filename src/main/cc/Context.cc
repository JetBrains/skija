#include <jni.h>
#include "RefCnt.hh"
#include "GrContext.h"

extern "C" JNIEXPORT jlong JNICALL Java_skija_Context_nMakeGL(JNIEnv* env, jclass jclass) {
    return sk_sp_ref(GrContext::MakeGL());
}

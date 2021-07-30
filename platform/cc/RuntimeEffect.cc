#include <jni.h>

#include "SkRuntimeEffect.h"
#include "interop.hh"

JNIEXPORT jlong JNICALL
Java_org_jetbrains_skija_RuntimeEffect__1nMakeShader(JNIEnv* env,
                                                     jclass jclass,
                                                     jlong ptr,
                                                     jlong uniformPtr,
                                                     jlongArray childrenPtrs,
                                                     jlong childCount,
                                                     jfloatArray localMatrixArr,
                                                     jboolean isOpaque) {
    SkRuntimeEffect* runtimeEffect = jlongToPtr<SkRuntimeEffect*>(ptr);
    SkData* uniform = jlongToPtr<SkData*>(uniformPtr);
    sk_sp<SkData> uniformData = SkData::MakeFromMalloc(uniform, uniform->size());

    // Matrix
    jfloat* m = env->GetFloatArrayElements(localMatrixArr, 0);
    SkMatrix* mPtr = new SkMatrix();
    mPtr->setAll(m[0], m[1], m[2], m[3], m[4], m[5], m[6], m[7], m[8]);
    env->ReleaseFloatArrayElements(localMatrixArr, m, 0);

    sk_sp<SkShader>* children = new sk_sp<SkShader>[childCount];
    SkShader** cPtrs = reinterpret_cast<SkShader**>(childrenPtrs);
    for (size_t i = 0; i < childCount; i++) {
        // This bare pointer was already part of an sk_sp (owned outside of here),
        // so we want to ref the new sk_sp so makeShader doesn't clean it up.
        children[i] = sk_ref_sp<SkShader>(cPtrs[i]);
    }
    sk_sp<SkShader> shader =
        runtimeEffect->makeShader(uniformData, children, childCount, mPtr, isOpaque);
    return ptrToJlong(&shader);
}

JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_RuntimeEffect__1nMakeColorFilter(JNIEnv* env,
                                                                                  jclass jclass,
                                                                                  jstring sksl) {
    SkString skslProper = skString(env, sksl);
    SkRuntimeEffect::Result result = SkRuntimeEffect::MakeForColorFilter(skslProper);
    return ptrToJlong(result.effect.release());
}
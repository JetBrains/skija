#include <jni.h>

#include "SkRuntimeEffect.h"
#include "interop.hh"

JNIEXPORT jlong JNICALL
Java_org_jetbrains_skija_RuntimeEffect__1nMakeShader(JNIEnv* env,
                                                     jclass jclass,
                                                     jlong ptr,
                                                     jlong uniformPtr,
                                                     jlongArray childrenPtrs,
                                                     jfloatArray localMatrixArr,
                                                     jboolean isOpaque) {
    SkRuntimeEffect* runtimeEffect = jlongToPtr<SkRuntimeEffect*>(ptr);

    // Uniform
    SkData* uniform = jlongToPtr<SkData*>(uniformPtr);
    sk_sp<SkData> uniformData = SkData::MakeFromMalloc(uniform, uniform->size());

    // Matrix
    std::unique_ptr<SkMatrix> localMatrix = skMatrix(env, localMatrixArr);

    // Children
    jsize childCount = env->GetArrayLength(childrenPtrs);
    jlong* c = env->GetLongArrayElements(childrenPtrs, 0);
    std::vector<sk_sp<SkShader>> children(childCount);
    for (size_t i = 0; i < childCount; i++) {
        SkShader* si = jlongToPtr<SkShader*>(c[i]);
        children[i] = sk_ref_sp(si);
    }
    env->ReleaseLongArrayElements(childrenPtrs, c, 0);

    sk_sp<SkShader> shader = runtimeEffect->makeShader(uniformData, children.data(), childCount,
                                                       localMatrix.get(), isOpaque);
    return ptrToJlong(shader.release());
}

JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_RuntimeEffect__1nMakeColorFilter(JNIEnv* env,
                                                                                  jclass jclass,
                                                                                  jstring sksl) {
    SkString skslProper = skString(env, sksl);
    SkRuntimeEffect::Result result = SkRuntimeEffect::MakeForColorFilter(skslProper);
    if (result.errorText.isEmpty()) {
        return ptrToJlong(result.effect.release());
    } else {
        env->ThrowNew(java::lang::RuntimeException::cls, result.errorText.c_str());
    }
}
#include <jni.h>

#include "SkRuntimeEffect.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL
Java_org_jetbrains_skija_RuntimeEffect__1nMakeShader(JNIEnv* env,
                                                     jclass jclass,
                                                     jlong ptr,
                                                     jlong uniformPtr,
                                                     jlongArray childrenPtrsArr,
                                                     jfloatArray localMatrixArr,
                                                     jboolean isOpaque) {
    SkRuntimeEffect* runtimeEffect = jlongToPtr<SkRuntimeEffect*>(ptr);
    SkData* uniform = jlongToPtr<SkData*>(uniformPtr);
    std::unique_ptr<SkMatrix> localMatrix = skMatrix(env, localMatrixArr);

    jsize childCount = env->GetArrayLength(childrenPtrsArr);
    jlong* childrenPtrs = env->GetLongArrayElements(childrenPtrsArr, 0);
    std::vector<sk_sp<SkShader>> children(childCount);
    for (size_t i = 0; i < childCount; i++) {
        SkShader* si = jlongToPtr<SkShader*>(childrenPtrs[i]);
        children[i] = sk_ref_sp(si);
    }
    env->ReleaseLongArrayElements(childrenPtrsArr, childrenPtrs, 0);

    sk_sp<SkShader> shader = runtimeEffect->makeShader(sk_ref_sp<SkData>(uniform),
                                                       children.data(),
                                                       childCount,
                                                       localMatrix.get(),
                                                       isOpaque);
    return ptrToJlong(shader.release());
}

extern "C" JNIEXPORT jlong JNICALL
Java_org_jetbrains_skija_RuntimeEffect__1nMakeForShader(JNIEnv* env, jclass jclass, jstring sksl) {
    SkString skslProper = skString(env, sksl);
    SkRuntimeEffect::Result result = SkRuntimeEffect::MakeForShader(skslProper);
    if (result.errorText.isEmpty()) {
        sk_sp<SkRuntimeEffect> effect = result.effect;
        return ptrToJlong(effect.release());
    } else {
        env->ThrowNew(java::lang::RuntimeException::cls, result.errorText.c_str());
        return 0;
    }
}

extern "C" JNIEXPORT jlong JNICALL
Java_org_jetbrains_skija_RuntimeEffect__1nMakeForColorFilter(JNIEnv* env,
                                                             jclass jclass,
                                                             jstring sksl) {
    SkString skslProper = skString(env, sksl);
    SkRuntimeEffect::Result result = SkRuntimeEffect::MakeForColorFilter(skslProper);
    if (result.errorText.isEmpty()) {
        return ptrToJlong(result.effect.release());
    } else {
        env->ThrowNew(java::lang::RuntimeException::cls, result.errorText.c_str());
        return 0;
    }
}
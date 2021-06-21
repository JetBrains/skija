#include <jni.h>
#include "interop.hh"
#include "unicode/ubrk.h"

static void deleteBreakIterator(UBreakIterator* instance) {
  ubrk_close(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_BreakIterator__1nGetFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteBreakIterator));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_BreakIterator__1nMake
  (JNIEnv* env, jclass jclass, jint type, jstring localeStr) {
    UErrorCode status = U_ZERO_ERROR;
    UBreakIterator* instance;
    if (localeStr == nullptr)
      instance = ubrk_open(static_cast<UBreakIteratorType>(type), uloc_getDefault(), nullptr, 0, &status);
    else {
      SkString locale = skString(env, localeStr);
      instance = ubrk_open(static_cast<UBreakIteratorType>(type), locale.c_str(), nullptr, 0, &status);
    }
    
    if (U_FAILURE(status)) {
      env->ThrowNew(java::lang::RuntimeException::cls, u_errorName(status));
      return 0;
    } else
      return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_BreakIterator__1nClone
  (JNIEnv* env, jclass jclass, jlong ptr) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    UErrorCode status = U_ZERO_ERROR;
    UBreakIterator* clone = ubrk_safeClone(instance, nullptr, 0, &status);
    if (U_FAILURE(status)) {
      env->ThrowNew(java::lang::RuntimeException::cls, u_errorName(status));
      return 0;
    } else
      return reinterpret_cast<jlong>(clone);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_BreakIterator__1nCurrent
  (JNIEnv* env, jclass jclass, jlong ptr) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    return ubrk_current(instance);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_BreakIterator__1nNext
  (JNIEnv* env, jclass jclass, jlong ptr) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    return ubrk_next(instance);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_BreakIterator__1nPrevious
  (JNIEnv* env, jclass jclass, jlong ptr) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    return ubrk_previous(instance);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_BreakIterator__1nFirst
  (JNIEnv* env, jclass jclass, jlong ptr) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    return ubrk_first(instance);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_BreakIterator__1nLast
  (JNIEnv* env, jclass jclass, jlong ptr) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    return ubrk_last(instance);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_BreakIterator__1nPreceding
  (JNIEnv* env, jclass jclass, jlong ptr, jint offset) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    return ubrk_preceding(instance, offset);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_BreakIterator__1nFollowing
  (JNIEnv* env, jclass jclass, jlong ptr, jint offset) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    return ubrk_following(instance, offset);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_BreakIterator__1nIsBoundary
  (JNIEnv* env, jclass jclass, jlong ptr, jint offset) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    return ubrk_isBoundary(instance, offset);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_BreakIterator__1nGetRuleStatus
  (JNIEnv* env, jclass jclass, jlong ptr) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    return ubrk_getRuleStatus(instance);
}

extern "C" JNIEXPORT jintArray JNICALL Java_org_jetbrains_skija_BreakIterator__1nGetRuleStatuses
  (JNIEnv* env, jclass jclass, jlong ptr) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    UErrorCode status = U_ZERO_ERROR;
    int32_t len = ubrk_getRuleStatusVec(instance, nullptr, 0, &status);
    if (U_FAILURE(status))
      env->ThrowNew(java::lang::RuntimeException::cls, u_errorName(status));
    std::vector<jint> vec(len);
    ubrk_getRuleStatusVec(instance, reinterpret_cast<int32_t*>(vec.data()), len, &status);
    if (U_FAILURE(status))
      env->ThrowNew(java::lang::RuntimeException::cls, u_errorName(status));
    return javaIntArray(env, vec);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_BreakIterator__1nSetText
  (JNIEnv* env, jclass jclass, jlong ptr, jlong textPtr) {
    UBreakIterator* instance = reinterpret_cast<UBreakIterator*>(static_cast<uintptr_t>(ptr));
    std::vector<jchar>* text = reinterpret_cast<std::vector<jchar>*>(static_cast<uintptr_t>(textPtr));
    UErrorCode status = U_ZERO_ERROR;
    ubrk_setText(instance, reinterpret_cast<UChar *>(text->data()), text->size(), &status);
    if (U_FAILURE(status))
      env->ThrowNew(java::lang::RuntimeException::cls, u_errorName(status));
}
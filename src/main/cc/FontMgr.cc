#include <iostream>
#include <jni.h>
#include "interop.hh"
#include "SkData.h"
#include "SkTypeface.h"
#include "SkFontMgr.h"

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_FontMgr__1nGetFamiliesCount
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkFontMgr* instance = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(ptr));
    return instance->countFamilies();
}

extern "C" JNIEXPORT jstring JNICALL Java_org_jetbrains_skija_FontMgr__1nGetFamilyName
  (JNIEnv* env, jclass jclass, jlong ptr, jint index) {
    SkFontMgr* instance = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(ptr));
    SkString familyName;
    instance->getFamilyName(index, &familyName);
    return javaString(env, familyName);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontMgr__1nMakeStyleSet
  (JNIEnv* env, jclass jclass, jlong ptr, jint index) {
    SkFontMgr* instance = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(ptr));
    SkFontStyleSet* styleSet = instance->createStyleSet(index);
    return reinterpret_cast<jlong>(styleSet);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontMgr__1nMatchFamily
  (JNIEnv* env, jclass jclass, jlong ptr, jstring familyNameStr) {
    SkFontMgr* instance = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(ptr));
    const char* familyName = env->GetStringUTFChars(familyNameStr, nullptr);
    SkFontStyleSet* styleSet = instance->matchFamily(familyName);
    env->ReleaseStringUTFChars(familyNameStr, familyName);
    return reinterpret_cast<jlong>(styleSet);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontMgr__1nMatchFamilyStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jstring familyNameStr, jint fontStyle) {
    SkFontMgr* instance = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(ptr));
    const char* familyName = env->GetStringUTFChars(familyNameStr, nullptr);
    SkTypeface* typeface = instance->matchFamilyStyle(familyName, skFontStyle(fontStyle));
    env->ReleaseStringUTFChars(familyNameStr, familyName);
    return reinterpret_cast<jlong>(typeface);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontMgr__1nMatchFamilyStyleCharacter
  (JNIEnv* env, jclass jclass, jlong ptr, jstring familyNameStr, jint fontStyle, jobjectArray bcp47Array, jint character) {
    SkFontMgr* instance = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(ptr));

    const char* familyName = env->GetStringUTFChars(familyNameStr, nullptr);
    
    jsize bcp47Count = env->GetArrayLength(bcp47Array);
    const char** bcp47 = new const char*[bcp47Count];
    for (int i = 0; i < bcp47Count; ++i) {
        jstring bcp47String = reinterpret_cast<jstring>(env->GetObjectArrayElement(bcp47Array, i));
        bcp47[i] = env->GetStringUTFChars(bcp47String, nullptr);
    }
    
    SkTypeface* typeface = instance->matchFamilyStyleCharacter(familyName, skFontStyle(fontStyle), bcp47, bcp47Count, character);
    
    for (int i = 0; i < bcp47Count; ++i) {
        jstring bcp47String = reinterpret_cast<jstring>(env->GetObjectArrayElement(bcp47Array, i));
        env->ReleaseStringUTFChars(bcp47String, bcp47[i]);
    }

    env->ReleaseStringUTFChars(familyNameStr, familyName);
    
    return reinterpret_cast<jlong>(typeface);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontMgr__1nMatchFaceStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jlong typefacePtr, jint fontStyle) {
    SkFontMgr* instance = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(ptr));
    SkTypeface* typeface = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(typefacePtr));
    SkTypeface* match = instance->matchFaceStyle(typeface, skFontStyle(fontStyle));
    return reinterpret_cast<jlong>(match);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontMgr__1nMakeFromData
  (JNIEnv* env, jclass jclass, jlong ptr, jlong dataPtr, jint ttcIndex) {
    SkFontMgr* instance = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(ptr));
    SkData* data = reinterpret_cast<SkData*>(static_cast<uintptr_t>(dataPtr));
    SkTypeface* typeface = instance->makeFromData(sk_ref_sp(data), ttcIndex).release();
    return reinterpret_cast<jlong>(typeface);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontMgr__1nDefault
  (JNIEnv* env, jclass jclass) {
    SkFontMgr* instance = SkFontMgr::RefDefault().release();
    return reinterpret_cast<jlong>(instance);
}
#include <iostream>
#include <jni.h>
#include "interop.hh"
#include "SkRefCnt.h"
#include "FontCollection.h"

using namespace std;
using namespace skia::textlayout;

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontCollection_nInit
  (JNIEnv* env, jclass jclass) {
    FontCollection* ptr = new FontCollection();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontCollection_nGetFontManagersCount
  (JNIEnv* env, jclass jclass, jlong ptr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    return instance->getFontManagersCount();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_FontCollection_nSetAssetFontManager
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontManagerPtr, jstring defaultFamilyNameStr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    SkFontMgr* fontManager = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontManagerPtr));
    instance->setAssetFontManager(sk_ref_sp(fontManager));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_FontCollection_nSetDynamicFontManager
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontManagerPtr, jstring defaultFamilyNameStr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    SkFontMgr* fontManager = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontManagerPtr));
    instance->setDynamicFontManager(sk_ref_sp(fontManager));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_FontCollection_nSetTestFontManager
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontManagerPtr, jstring defaultFamilyNameStr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    SkFontMgr* fontManager = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontManagerPtr));
    instance->setTestFontManager(sk_ref_sp(fontManager));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_FontCollection_nSetDefaultFontManager
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontManagerPtr, jstring defaultFamilyNameStr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    SkFontMgr* fontManager = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontManagerPtr));

    if (defaultFamilyNameStr == nullptr)
        instance->setDefaultFontManager(sk_ref_sp(fontManager));
    else {
        jsize       len   = env->GetStringUTFLength(defaultFamilyNameStr);
        const char* chars = env->GetStringUTFChars(defaultFamilyNameStr, nullptr);
        instance->setDefaultFontManager(sk_ref_sp(fontManager), chars);
        env->ReleaseStringUTFChars(defaultFamilyNameStr, chars);
    }
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontCollection_nGetFallbackManager
  (JNIEnv* env, jclass jclass, jlong ptr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(instance->getFallbackManager().release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontCollection_nDefaultFallbackChar
  (JNIEnv* env, jclass jclass, jlong ptr, jint unicode, jint weight, jint width, jint slant, jstring locale) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    SkFontStyle style(weight, width, static_cast<SkFontStyle::Slant>(slant));
    return reinterpret_cast<jlong>(instance->defaultFallback(unicode, style, skString(env, locale)).release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontCollection_nDefaultFallback
  (JNIEnv* env, jclass jclass, jlong ptr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(instance->defaultFallback().release());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_FontCollection_nSetEnableFallback
  (JNIEnv* env, jclass jclass, jlong ptr, jboolean value) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    if (value)
        instance->enableFontFallback();
    else
        instance->disableFontFallback();
}
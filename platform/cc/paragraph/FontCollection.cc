#include <iostream>
#include <jni.h>
#include "../interop.hh"
#include "SkRefCnt.h"
#include "FontCollection.h"

using namespace std;
using namespace skia::textlayout;

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nMake
  (JNIEnv* env, jclass jclass) {
    FontCollection* ptr = new FontCollection();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nGetFontManagersCount
  (JNIEnv* env, jclass jclass, jlong ptr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    return instance->getFontManagersCount();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nSetAssetFontManager
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontManagerPtr, jstring defaultFamilyNameStr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    SkFontMgr* fontManager = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontManagerPtr));
    instance->setAssetFontManager(sk_ref_sp(fontManager));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nSetDynamicFontManager
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontManagerPtr, jstring defaultFamilyNameStr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    SkFontMgr* fontManager = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontManagerPtr));
    instance->setDynamicFontManager(sk_ref_sp(fontManager));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nSetTestFontManager
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontManagerPtr, jstring defaultFamilyNameStr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    SkFontMgr* fontManager = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontManagerPtr));
    instance->setTestFontManager(sk_ref_sp(fontManager));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nSetDefaultFontManager
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontManagerPtr, jstring defaultFamilyNameStr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    SkFontMgr* fontManager = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontManagerPtr));

    if (defaultFamilyNameStr == nullptr)
        instance->setDefaultFontManager(sk_ref_sp(fontManager));
    else {
        SkString defaultFamilyName = skString(env, defaultFamilyNameStr);
        instance->setDefaultFontManager(sk_ref_sp(fontManager), defaultFamilyName.c_str());
    }
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nGetFallbackManager
  (JNIEnv* env, jclass jclass, jlong ptr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(instance->getFallbackManager().release());
}

extern "C" JNIEXPORT jlongArray JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nFindTypefaces
  (JNIEnv* env, jclass jclass, jlong ptr, jobjectArray familyNamesArray, jint fontStyle) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));

    jsize len = env->GetArrayLength(familyNamesArray);
    vector<SkString> familyNames(len);
    for (int i = 0; i < len; ++i) {
        jstring str = static_cast<jstring>(env->GetObjectArrayElement(familyNamesArray, i));
        familyNames.push_back(skString(env, str));
        env->DeleteLocalRef(str);
    }

    vector<sk_sp<SkTypeface>> found = instance->findTypefaces(familyNames, skija::FontStyle::fromJava(fontStyle));
    vector<jlong> res(found.size());
    for (int i = 0; i < found.size(); ++i)
        res[i] = reinterpret_cast<jlong>(found[i].release());

    jlongArray resArray = env->NewLongArray((jsize) found.size());
    env->SetLongArrayRegion(resArray, 0, (jsize) found.size(), res.data());
    return resArray;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nDefaultFallbackChar
  (JNIEnv* env, jclass jclass, jlong ptr, jint unicode, jint fontStyle, jstring locale) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(instance->defaultFallback(unicode, skija::FontStyle::fromJava(fontStyle), skString(env, locale)).release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nDefaultFallback
  (JNIEnv* env, jclass jclass, jlong ptr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(instance->defaultFallback().release());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nSetEnableFallback
  (JNIEnv* env, jclass jclass, jlong ptr, jboolean value) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    if (value)
        instance->enableFontFallback();
    else
        instance->disableFontFallback();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_FontCollection__1nGetParagraphCache
  (JNIEnv* env, jclass jclass, jlong ptr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(instance->getParagraphCache());
}
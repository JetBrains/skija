#include <iostream>
#include <jni.h>
#include "SkFont.h"
#include "SkShaper.h"
#include "interop.hh"

static void deleteFont(SkFont* font) {
    // std::cout << "Deleting [Font " << font << "]" << std::endl;
    delete font;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Font_nInit(JNIEnv* env, jclass jclass, jlong typefacePtr, jfloat size) {
    SkTypeface* typeface = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(typefacePtr));
    SkFont* obj = new SkFont(sk_sp<SkTypeface>(SkRef(typeface)), size);
    return reinterpret_cast<jlong>(obj);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Font_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteFont));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Font_nShape(JNIEnv* env, jclass jclass, jlong ptr, jstring text, jfloat width) {
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(ptr));

    std::unique_ptr<SkShaper> shaper = SkShaper::Make();
    const char* chars = env->GetStringUTFChars(text, nullptr); // FIXME UTF-8 conversion
    size_t len = env->GetStringUTFLength(text);
    SkTextBlobBuilderRunHandler rh(chars, {0, 0});
    shaper->shape(chars, len, *font, true, width, &rh);
    env->ReleaseStringUTFChars(text, chars);

    SkTextBlob* blob = rh.makeBlob().release();
    return reinterpret_cast<jlong>(blob);
}

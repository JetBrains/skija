#include <jni.h>
#include "../interop.hh"
#include "SkFontMgr.h"
#include "SkShaper.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_FontMgrRunIterator__1nMake
  (JNIEnv* env, jclass jclass, jlong textPtr, jlong fontPtr, jlong fontMgrPtr) {
    SkString* text = reinterpret_cast<SkString*>(static_cast<uintptr_t>(textPtr));
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));
    sk_sp<SkFontMgr> fontMgr = fontMgrPtr == 0 ? SkFontMgr::RefDefault() : sk_ref_sp(reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontMgrPtr)));
    
    std::unique_ptr<SkShaper::FontRunIterator> instance(SkShaper::MakeFontMgrRunIterator(text->c_str(), text->size(), *font, fontMgr));
    return reinterpret_cast<jlong>(instance.release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_FontMgrRunIterator__1nGetCurrentFont
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkShaper::FontRunIterator* instance = reinterpret_cast<SkShaper::FontRunIterator*>(static_cast<uintptr_t>(ptr));
    SkFont* font = new SkFont(instance->currentFont());
    return reinterpret_cast<jlong>(font);
}

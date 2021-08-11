#include <jni.h>
#include "../interop.hh"
#include "interop.hh"
#include "FontRunIterator.hh"
#include "SkFontMgr.h"
#include "SkShaper.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_FontMgrRunIterator__1nMake
  (JNIEnv* env, jclass jclass, jlong textPtr, jlong fontPtr, jobject opts) {
    SkString* text = reinterpret_cast<SkString*>(static_cast<uintptr_t>(textPtr));
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));
    jobject fontMgrObj = env->GetObjectField(opts, skija::shaper::ShapingOptions::_fontMgr);
    sk_sp<SkFontMgr> fontMgr = fontMgrObj == nullptr
      ? SkFontMgr::RefDefault()
      : sk_ref_sp(reinterpret_cast<SkFontMgr*>(skija::impl::Native::fromJava(env, fontMgrObj, skija::FontMgr::cls)));
    std::shared_ptr<UBreakIterator> graphemeIter = skija::shaper::graphemeBreakIterator(*text);
    if (!graphemeIter) return 0;

    auto instance = new FontRunIterator(
      text->c_str(),
      text->size(),
      *font,
      fontMgr,
      graphemeIter,
      env->GetBooleanField(opts, skija::shaper::ShapingOptions::_approximateSpaces),
      env->GetBooleanField(opts, skija::shaper::ShapingOptions::_approximatePunctuation)
    );
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_FontMgrRunIterator__1nGetCurrentFont
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkShaper::FontRunIterator* instance = reinterpret_cast<SkShaper::FontRunIterator*>(static_cast<uintptr_t>(ptr));
    SkFont* font = new SkFont(instance->currentFont());
    return reinterpret_cast<jlong>(font);
}

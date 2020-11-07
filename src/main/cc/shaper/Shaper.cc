#include <iostream>
#include <jni.h>
// #include <unicode/ubidi.h>
#include "../interop.hh"
#include "interop.hh"
#include "SkShaper.h"
#include "src/utils/SkUTF.h"

static void deleteShaper(SkShaper* instance) {
    // std::cout << "Deleting [SkShaper " << instance << "]" << std::endl;
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nGetFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteShaper));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nMakePrimitive
  (JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(SkShaper::MakePrimitive().release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nMakeShaperDrivenWrapper
  (JNIEnv* env, jclass jclass, jlong fontMgrPtr) {
    SkFontMgr* fontMgr = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontMgrPtr));
    return reinterpret_cast<jlong>(SkShaper::MakeShaperDrivenWrapper(sk_ref_sp(fontMgr)).release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nMakeShapeThenWrap
  (JNIEnv* env, jclass jclass, jlong fontMgrPtr) {
    SkFontMgr* fontMgr = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontMgrPtr));
    return reinterpret_cast<jlong>(SkShaper::MakeShapeThenWrap(sk_ref_sp(fontMgr)).release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nMakeShapeDontWrapOrReorder
  (JNIEnv* env, jclass jclass, jlong fontMgrPtr) {
    SkFontMgr* fontMgr = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontMgrPtr));
    return reinterpret_cast<jlong>(SkShaper::MakeShapeDontWrapOrReorder(sk_ref_sp(fontMgr)).release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nMakeCoreText
  (JNIEnv* env, jclass jclass) {
    #ifdef SK_SHAPER_CORETEXT_AVAILABLE
        return reinterpret_cast<jlong>(SkShaper::MakeCoreText().release());
    #else
        return 0;
    #endif
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nMake
  (JNIEnv* env, jclass jclass, jlong fontMgrPtr) {
    SkFontMgr* fontMgr = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontMgrPtr));
    return reinterpret_cast<jlong>(SkShaper::Make(sk_ref_sp(fontMgr)).release());
}

template <typename RunIteratorSubclass>
class SkijaRunIterator: public RunIteratorSubclass {
public:
    SkijaRunIterator(JNIEnv* env, jobject obj, SkString& text):
      fEnv(env),
      fIteratorObj(obj),
      fIndicesConverter(text.c_str(), text.size()),
      fEndOfRun(0)
    {
        fHasNext = fEnv->CallBooleanMethod(fIteratorObj, java::util::Iterator::hasNext);
    }

    void consume() override {
        SkASSERT(fHasNext);
        jobject runObj = fEnv->CallObjectMethod(fIteratorObj, java::util::Iterator::next);
        jint endOfRun16 = onConsume(runObj);
        fEndOfRun = fIndicesConverter.from16To8(endOfRun16);
        fHasNext = fEnv->CallBooleanMethod(fIteratorObj, java::util::Iterator::hasNext);
    }

    size_t endOfCurrentRun() const override {
        return fEndOfRun;
    }
    
    bool atEnd() const override {
        return !fHasNext;
    }

protected:
    JNIEnv* fEnv;
    jobject fIteratorObj;
    skija::UtfIndicesConverter fIndicesConverter;
    size_t fEndOfRun;
    bool fHasNext;

    virtual jint onConsume(jobject) = 0;
};

class SkijaFontRunIterator: public SkijaRunIterator<SkShaper::FontRunIterator> {
public:
    SkijaFontRunIterator(JNIEnv* env, jobject obj, SkString text): 
      SkijaRunIterator<SkShaper::FontRunIterator>(env, obj, text)
    {}

    const SkFont& currentFont() const override {
        return *fFont;
    }

    jint onConsume(jobject runObj) override {
        long fontPtr = fEnv->CallLongMethod(runObj, skija::shaper::FontRun::_getFontPtr);
        fFont = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));
        return fEnv->GetIntField(runObj, skija::shaper::FontRun::_end);
    }

protected:
    SkFont* fFont;
};

class SkijaBidiRunIterator: public SkijaRunIterator<SkShaper::BiDiRunIterator> {
public:
    SkijaBidiRunIterator(JNIEnv* env, jobject obj, SkString& text): 
      SkijaRunIterator<SkShaper::BiDiRunIterator>(env, obj, text)
    {}

    uint8_t currentLevel() const override {
        return fLevel;
    }

    jint onConsume(jobject runObj) override {
        fLevel = fEnv->GetIntField(runObj, skija::shaper::BidiRun::_level);
        return fEnv->GetIntField(runObj, skija::shaper::BidiRun::_end);
    }
    
protected:
    uint8_t fLevel;
};

class SkijaScriptRunIterator: public SkijaRunIterator<SkShaper::ScriptRunIterator> {
public:
    SkijaScriptRunIterator(JNIEnv* env, jobject obj, SkString text):
      SkijaRunIterator<SkShaper::ScriptRunIterator>(env, obj, text)
    {}

    SkFourByteTag currentScript() const override {
        return fScript;
    }

    jint onConsume(jobject runObj) override {
        fScript = fEnv->GetIntField(runObj, skija::shaper::ScriptRun::_scriptTag);
        return fEnv->GetIntField(runObj, skija::shaper::ScriptRun::_end);
    }
    
protected:
    uint8_t fScript;
};

class SkijaLanguageRunIterator: public SkijaRunIterator<SkShaper::LanguageRunIterator> {
public:
    SkijaLanguageRunIterator(JNIEnv* env, jobject obj, SkString text):
      SkijaRunIterator<SkShaper::LanguageRunIterator>(env, obj, text)
    {}

    const char* currentLanguage() const override {
        return fLang.c_str();
    }

    jint onConsume(jobject runObj) override {
        jstring langObj = static_cast<jstring>(fEnv->GetObjectField(runObj, skija::shaper::LanguageRun::_language));
        fLang = skString(fEnv, langObj);
        return fEnv->GetIntField(runObj, skija::shaper::LanguageRun::_end);
    }
    
protected:
    SkString fLang;
};

class SkijaRunHandler: public SkShaper::RunHandler {
public:
    SkijaRunHandler(JNIEnv* env, jobject runHandler, const SkString& text):
        fEnv(env),
        fRunHandler(runHandler),
        fIndicesConverter(text.c_str(), text.size())
    {}

    void beginLine() {
        fEnv->CallVoidMethod(fRunHandler, skija::shaper::RunHandler::beginLine);
    }

    void runInfo(const SkShaper::RunHandler::RunInfo& info) {
        fEnv->CallVoidMethod(fRunHandler, skija::shaper::RunHandler::runInfo, skija::shaper::RunInfo::toJava(fEnv, info, fIndicesConverter));
    }

    void commitRunInfo() {
        fEnv->CallVoidMethod(fRunHandler, skija::shaper::RunHandler::commitRunInfo);
    }

    SkShaper::RunHandler::Buffer runBuffer(const SkShaper::RunHandler::RunInfo& info) {
        fGlyphs    = std::vector<jshort>(info.glyphCount);
        fPositions = std::vector<SkPoint>(info.glyphCount);
        fClusters  = std::vector<jint>(info.glyphCount);

        jobject point = fEnv->CallObjectMethod(fRunHandler, skija::shaper::RunHandler::runOffset, skija::shaper::RunInfo::toJava(fEnv, info, fIndicesConverter));
        jfloat x = fEnv->GetFloatField(point, skija::Point::x);
        jfloat y = fEnv->GetFloatField(point, skija::Point::y);

        return SkShaper::RunHandler::Buffer{
            reinterpret_cast<SkGlyphID*>(fGlyphs.data()),
            fPositions.data(),
            nullptr,
            reinterpret_cast<uint32_t*>(fClusters.data()),
            {x, y}};
    }

    void commitRunBuffer(const SkShaper::RunHandler::RunInfo& info) {
        jobject runInfo = skija::shaper::RunInfo::toJava(fEnv, info, fIndicesConverter);
        jshortArray glyphs = javaShortArray(fEnv, fGlyphs);
        jobjectArray positions = skija::Point::fromSkPoints(fEnv, fPositions);
        jintArray clusters = javaIntArray(fEnv, fClusters);

        fEnv->CallObjectMethod(fRunHandler, skija::shaper::RunHandler::commitRun, runInfo, glyphs, positions, clusters);
    }

    void commitLine() {
        fEnv->CallVoidMethod(fRunHandler, skija::shaper::RunHandler::commitLine);
    }

private:
    JNIEnv* fEnv;
    jobject fRunHandler;
    skija::UtfIndicesConverter fIndicesConverter;
    std::vector<jshort> fGlyphs;
    std::vector<SkPoint> fPositions;
    std::vector<jint> fClusters;
};

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nShape
  (JNIEnv* env, jclass jclass, jlong ptr, jstring textObj, jobject fontRunIterObj, jobject bidiRunIterObj, jobject scriptRunIterObj, jobject languageRunIterObj,
   jobjectArray featuresArr, jfloat width, jobject runHandlerObj)
{
    SkShaper* instance = reinterpret_cast<SkShaper*>(static_cast<uintptr_t>(ptr));
    SkString text = skString(env, textObj);

    auto nativeFontRunIter = (SkShaper::FontRunIterator*) skija::impl::Native::fromJava(env, fontRunIterObj, skija::shaper::FontMgrRunIterator::cls);
    std::unique_ptr<SkijaFontRunIterator> localFontRunIter;
    if (nativeFontRunIter == nullptr)
        localFontRunIter.reset(new SkijaFontRunIterator(env, fontRunIterObj, text));

    auto nativeBidiRunIter = (SkShaper::BiDiRunIterator*) skija::impl::Native::fromJava(env, bidiRunIterObj, skija::shaper::IcuBidiRunIterator::cls);
    std::unique_ptr<SkijaBidiRunIterator> localBidiRunIter;
    if (nativeBidiRunIter == nullptr)
        localBidiRunIter.reset(new SkijaBidiRunIterator(env, bidiRunIterObj, text));

    auto nativeScriptRunIter = (SkShaper::ScriptRunIterator*) skija::impl::Native::fromJava(env, scriptRunIterObj, skija::shaper::HbIcuScriptRunIterator::cls);
    std::unique_ptr<SkijaScriptRunIterator> localScriptRunIter;
    if (nativeScriptRunIter == nullptr)
        localScriptRunIter.reset(new SkijaScriptRunIterator(env, scriptRunIterObj, text));
    
    auto languageRunIter = SkijaLanguageRunIterator(env, languageRunIterObj, text);

    std::vector<SkShaper::Feature> features = skija::FontFeature::fromJavaArray(env, featuresArr);
    
    auto nativeRunHandler = (SkShaper::RunHandler*) skija::impl::Native::fromJava(env, runHandlerObj, skija::shaper::TextBlobBuilderRunHandler::cls);
    std::unique_ptr<SkijaRunHandler> localRunHandler;
    if (nativeRunHandler == nullptr)
        localRunHandler.reset(new SkijaRunHandler(env, runHandlerObj, text));

    instance->shape(text.c_str(), text.size(),
        nativeFontRunIter != nullptr ? *nativeFontRunIter : *localFontRunIter,
        nativeBidiRunIter != nullptr ? *nativeBidiRunIter : *localBidiRunIter,
        nativeScriptRunIter != nullptr ? *nativeScriptRunIter : *localScriptRunIter,
        languageRunIter,
        features.data(),
        features.size(),
        width,
        nativeRunHandler != nullptr ? nativeRunHandler : localRunHandler.get());
}
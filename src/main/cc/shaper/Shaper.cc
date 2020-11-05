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

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nShapeToTextBlob
  (JNIEnv* env, jclass jclass, jlong ptr, jstring textObj, jlong fontPtr, jobjectArray featuresArr, jboolean leftToRight, jfloat width, jfloat offsetX, jfloat offsetY) {
    SkShaper* instance = reinterpret_cast<SkShaper*>(static_cast<uintptr_t>(ptr));
    SkString text = skString(env, textObj);
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));
    std::vector<SkShaper::Feature> features = skija::FontFeature::fromJavaArray(env, featuresArr);

    std::unique_ptr<SkShaper::FontRunIterator> fontRunIter(SkShaper::MakeFontMgrRunIterator(text.c_str(), text.size(), *font, SkFontMgr::RefDefault()));
    if (!fontRunIter) return 0;

    uint8_t defaultBiDiLevel = leftToRight ? 0xfe /* UBIDI_DEFAULT_LTR */ : 0xff /* UBIDI_DEFAULT_RTL */; // unicode/ubidi.h
    std::unique_ptr<SkShaper::BiDiRunIterator> bidiRunIter(SkShaper::MakeBiDiRunIterator(text.c_str(), text.size(), defaultBiDiLevel));
    if (!bidiRunIter) return 0;

    std::unique_ptr<SkShaper::ScriptRunIterator> scriptRunIter(SkShaper::MakeHbIcuScriptRunIterator(text.c_str(), text.size()));
    if (!scriptRunIter) return 0;

    std::unique_ptr<SkShaper::LanguageRunIterator> languageRunIter(SkShaper::MakeStdLanguageRunIterator(text.c_str(), text.size()));
    if (!languageRunIter) return 0;

    SkTextBlobBuilderRunHandler rh(text.c_str(), {offsetX, offsetY});
    instance->shape(text.c_str(), text.size(), *fontRunIter, *bidiRunIter, *scriptRunIter, *languageRunIter, features.data(), features.size(), width, &rh);
    SkTextBlob* blob = rh.makeBlob().release();
    
    return reinterpret_cast<jlong>(blob);
}

class SkijaRunHandler: public SkShaper::RunHandler {
public:
    SkijaRunHandler(JNIEnv* env, jobject runHandler): fEnv(env), fRunHandler(runHandler) {}

    void beginLine() {
        fEnv->CallVoidMethod(fRunHandler, skija::shaper::RunHandler::beginLine);
    }

    void runInfo(const SkShaper::RunHandler::RunInfo& info) {
        fEnv->CallVoidMethod(fRunHandler, skija::shaper::RunHandler::runInfo, skija::shaper::RunInfo::toJava(fEnv, info));
    }

    void commitRunInfo() {
        fEnv->CallVoidMethod(fRunHandler, skija::shaper::RunHandler::commitRunInfo);
    }

    SkShaper::RunHandler::Buffer runBuffer(const SkShaper::RunHandler::RunInfo& info) {
        fGlyphs    = std::vector<jshort>(info.glyphCount);
        fPositions = std::vector<SkPoint>(info.glyphCount);
        fClusters  = std::vector<jint>(info.glyphCount);

        jobject point = fEnv->CallObjectMethod(fRunHandler, skija::shaper::RunHandler::runOffset, skija::shaper::RunInfo::toJava(fEnv, info));
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
        jobject runInfo = skija::shaper::RunInfo::toJava(fEnv, info);
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
    std::vector<jshort> fGlyphs;
    std::vector<SkPoint> fPositions;
    std::vector<jint> fClusters;
};

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nShape
  (JNIEnv* env, jclass jclass, jlong ptr, jstring textObj, jlong fontPtr, jlong fontMgrPtr, jobjectArray featuresArr, jboolean leftToRight, jfloat width, jobject runHandler) {
    SkShaper* instance = reinterpret_cast<SkShaper*>(static_cast<uintptr_t>(ptr));
    SkString text = skString(env, textObj);
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));
    sk_sp<SkFontMgr> fontMgr = fontMgrPtr == 0 ? SkFontMgr::RefDefault() : sk_ref_sp(reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontMgrPtr)));
    std::vector<SkShaper::Feature> features = skija::FontFeature::fromJavaArray(env, featuresArr);

    std::unique_ptr<SkShaper::FontRunIterator> fontRunIter(SkShaper::MakeFontMgrRunIterator(text.c_str(), text.size(), *font, fontMgr));
    if (!fontRunIter) return;

    uint8_t defaultBiDiLevel = leftToRight ? 0xfe /* UBIDI_DEFAULT_LTR */ : 0xff /* UBIDI_DEFAULT_RTL */; // unicode/ubidi.h
    std::unique_ptr<SkShaper::BiDiRunIterator> bidiRunIter(SkShaper::MakeBiDiRunIterator(text.c_str(), text.size(), defaultBiDiLevel));
    if (!bidiRunIter) return;

    std::unique_ptr<SkShaper::ScriptRunIterator> scriptRunIter(SkShaper::MakeHbIcuScriptRunIterator(text.c_str(), text.size()));
    if (!scriptRunIter) return;

    std::unique_ptr<SkShaper::LanguageRunIterator> languageRunIter(SkShaper::MakeStdLanguageRunIterator(text.c_str(), text.size()));
    if (!languageRunIter) return;

    SkijaRunHandler rh(env, runHandler);
    instance->shape(text.c_str(), text.size(), *fontRunIter, *bidiRunIter, *scriptRunIter, *languageRunIter, features.data(), features.size(), width, &rh);
}

template <typename RunIteratorSubclass>
class SkijaRunIterator: public RunIteratorSubclass {
public:
    SkijaRunIterator(JNIEnv* env, jobject obj, SkString text): fEnv(env), fObj(obj), fText(text), fPtr8(text.c_str()), fPos16(0) {}

    void consume() override {
        fEnv->CallVoidMethod(fObj, skija::shaper::RunIterator::consume);
    }

    size_t endOfCurrentRun() const override {
        size_t i16 = fEnv->CallLongMethod(fObj, skija::shaper::RunIterator::getEndOfCurrentRun);
        return idx16To8(i16);
    }
    
    bool atEnd() const override {
        return fEnv->CallBooleanMethod(fObj, skija::shaper::RunIterator::isAtEnd);
    }

protected:
    JNIEnv* fEnv;
    jobject fObj;
    SkString fText;

    mutable const char* fPtr8;
    mutable size_t fPos16;

    size_t idx16To8(size_t i16) const {
        const char* start8 = fText.c_str();
        const char* end8 = fText.c_str() + fText.size();

        // if new i16 >= last fPos16, continue from where we started
        if (i16 < fPos16) {
            fPtr8 = start8;
            fPos16 = 0;
        }

        while (fPtr8 < end8 && fPos16 < i16) {
            SkUnichar u = SkUTF::NextUTF8(&fPtr8, end8);
            fPos16 += SkUTF::ToUTF16(u);
        }

        return fPtr8 - start8;
    }
};

class SkijaFontRunIterator: public SkijaRunIterator<SkShaper::FontRunIterator> {
public:
    SkijaFontRunIterator(JNIEnv* env, jobject obj, SkString text): SkijaRunIterator<SkShaper::FontRunIterator>(env, obj, text) {}

    const SkFont& currentFont() const override {
        jlong fontPtr = fEnv->CallLongMethod(fObj, skija::shaper::FontRunIterator::_getCurrentFontPtr);
        SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));
        return *font;
    }
};

class SkijaBidiRunIterator: public SkijaRunIterator<SkShaper::BiDiRunIterator> {
public:
    SkijaBidiRunIterator(JNIEnv* env, jobject obj, SkString text): SkijaRunIterator<SkShaper::BiDiRunIterator>(env, obj, text) {}

    uint8_t currentLevel() const override {
        return fEnv->CallByteMethod(fObj, skija::shaper::BidiRunIterator::getCurrentLevel);
    }
};

class SkijaScriptRunIterator: public SkijaRunIterator<SkShaper::ScriptRunIterator> {
public:
    SkijaScriptRunIterator(JNIEnv* env, jobject obj, SkString text): SkijaRunIterator<SkShaper::ScriptRunIterator>(env, obj, text) {}

    SkFourByteTag currentScript() const override {
        return fEnv->CallIntMethod(fObj, skija::shaper::ScriptRunIterator::_getCurrentScriptTag);
    }
};

class SkijaLanguageRunIterator: public SkijaRunIterator<SkShaper::LanguageRunIterator> {
public:
    SkijaLanguageRunIterator(JNIEnv* env, jobject obj, SkString text): SkijaRunIterator<SkShaper::LanguageRunIterator>(env, obj, text) {}

    const char* currentLanguage() const override {
        jstring langObj = (jstring) fEnv->CallObjectMethod(fObj, skija::shaper::LanguageRunIterator::getCurrentLanguage);
        fLang = skString(fEnv, langObj);
        return fLang.c_str();
    }
private:
    mutable SkString fLang;
};

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_shaper_Shaper__1nShapeRunIters
  (JNIEnv* env, jclass jclass, jlong ptr, jstring textObj, jobject fontRunIterObj, jobject bidiRunIterObj, jobject scriptRunIterObj, jobject languageRunIterObj,
   jobjectArray featuresArr, jfloat width, jobject runHandler)
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
    SkijaRunHandler rh(env, runHandler);

    instance->shape(text.c_str(), text.size(),
        nativeFontRunIter != nullptr ? *nativeFontRunIter : *localFontRunIter,
        nativeBidiRunIter != nullptr ? *nativeBidiRunIter : *localBidiRunIter,
        nativeScriptRunIter != nullptr ? *nativeScriptRunIter : *localScriptRunIter,
        languageRunIter,
        features.data(),
        features.size(),
        width,
        &rh);
}
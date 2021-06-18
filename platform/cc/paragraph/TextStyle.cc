#include <iostream>
#include <jni.h>
#include <vector>
#include "../interop.hh"
#include "interop.hh"
#include "TextStyle.h"

using namespace std;
using namespace skia::textlayout;

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nMake
  (JNIEnv* env, jclass jclass) {
    TextStyle* instance = new TextStyle();
    return reinterpret_cast<jlong>(instance);
}

static void deleteTextStyle(TextStyle* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteTextStyle));
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nEquals
  (JNIEnv* env, jclass jclass, jlong ptr, jlong otherPtr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    TextStyle* other = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(otherPtr));
    return instance->equals(*other);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nAttributeEquals
  (JNIEnv* env, jclass jclass, jlong ptr, jint attribute, jlong otherPtr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    TextStyle* other = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(otherPtr));
    if (attribute == static_cast<jint>(StyleType::kWordSpacing) + 1) // FONT_EXACT
        return instance->equalsByFonts(*other);
    else
        return instance->matchOneAttribute(static_cast<StyleType>(attribute), *other);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetColor
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getColor();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetColor
  (JNIEnv* env, jclass jclass, jlong ptr, jint color) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setColor(color);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetForeground
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return instance->hasForeground() ? reinterpret_cast<jlong>(new SkPaint(instance->getForeground())) : 0;
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetForeground
  (JNIEnv* env, jclass jclass, jlong ptr, jlong paintPtr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    if (paintPtr == 0)
        instance->clearForegroundColor();
    else {
        SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
        instance->setForegroundColor(*paint);
    }
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetBackground
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return instance->hasBackground() ? reinterpret_cast<jlong>(new SkPaint(instance->getBackground())) : 0;
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetBackground
  (JNIEnv* env, jclass jclass, jlong ptr, jlong paintPtr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    if (paintPtr == 0)
        instance->clearBackgroundColor();
    else {
        SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
        instance->setBackgroundColor(*paint);
    }
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetDecorationStyle
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    Decoration d = instance->getDecoration();
    return env->NewObject(skija::paragraph::DecorationStyle::cls, skija::paragraph::DecorationStyle::ctor,
      (d.fType & TextDecoration::kUnderline) != 0,
      (d.fType & TextDecoration::kOverline) != 0,
      (d.fType & TextDecoration::kLineThrough) != 0,
      d.fMode == TextDecorationMode::kGaps,
      d.fColor,
      static_cast<jint>(d.fStyle),
      d.fThicknessMultiplier); 
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetDecorationStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jboolean underline, jboolean overline, jboolean lineThrough, jboolean gaps, jint color, jint style, jfloat thicknessMultiplier) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    int typeMask = 0;
    if (underline)   typeMask |= TextDecoration::kUnderline;
    if (overline)    typeMask |= TextDecoration::kOverline;
    if (lineThrough) typeMask |= TextDecoration::kLineThrough;
    instance->setDecoration(static_cast<TextDecoration>(typeMask));
    instance->setDecorationMode(gaps ? TextDecorationMode::kGaps : TextDecorationMode::kThrough);
    instance->setDecorationColor(color);
    instance->setDecorationStyle(static_cast<TextDecorationStyle>(style));
    instance->setDecorationThicknessMultiplier(thicknessMultiplier);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetFontStyle
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return skija::FontStyle::toJava(instance->getFontStyle());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetFontStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jint fontStyleValue) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setFontStyle(skija::FontStyle::fromJava(fontStyleValue));
}

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetShadows
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    std::vector<TextShadow> shadows = instance->getShadows();
    jobjectArray shadowsArr = env->NewObjectArray((jsize) shadows.size(), skija::paragraph::Shadow::cls, nullptr);
    for (int i = 0; i < shadows.size(); ++i) {
        const TextShadow& s = shadows[i];
        skija::AutoLocal<jobject> shadowObj(env, env->NewObject(skija::paragraph::Shadow::cls, skija::paragraph::Shadow::ctor, s.fColor, s.fOffset.fX, s.fOffset.fY, s.fBlurSigma));
        env->SetObjectArrayElement(shadowsArr, i, shadowObj.get());
    }
    return shadowsArr;
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nAddShadow
  (JNIEnv* env, jclass jclass, jlong ptr, jint color, jfloat offsetX, jfloat offsetY, jdouble blurSigma) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->addShadow({static_cast<SkColor>(color), {offsetX, offsetY}, blurSigma});
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nClearShadows
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->resetShadows();
}

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetFontFeatures
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    std::vector<FontFeature> fontFeatures = instance->getFontFeatures();
    jobjectArray fontFeaturesArr = env->NewObjectArray((jsize) fontFeatures.size(), skija::FontFeature::cls, nullptr);
    for (int i = 0; i < fontFeatures.size(); ++i) {
        const FontFeature& ff = fontFeatures[i];
        auto featureObj = env->NewObject(skija::FontFeature::cls, skija::FontFeature::ctor, javaString(env, ff.fName), ff.fValue);
        env->SetObjectArrayElement(fontFeaturesArr, i, featureObj);
        env->DeleteLocalRef(featureObj);
    }
    return fontFeaturesArr;
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nAddFontFeature
  (JNIEnv* env, jclass jclass, jlong ptr, jstring nameStr, jint value) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->addFontFeature(skString(env, nameStr), value);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nClearFontFeatures
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->resetFontFeatures();
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetFontSize
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getFontSize();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetFontSize
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat size) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setFontSize(size);
}

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetFontFamilies
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return javaStringArray(env, instance->getFontFamilies());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetFontFamilies
  (JNIEnv* env, jclass jclass, jlong ptr, jobjectArray familiesArray) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setFontFamilies(skStringVector(env, familiesArray));
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetHeight
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getHeightOverride() ? javaFloat(env, instance->getHeight()) : nullptr;
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetHeight
  (JNIEnv* env, jclass jclass, jlong ptr, jboolean override, jfloat height) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setHeightOverride(override);
    instance->setHeight(height);
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetLetterSpacing
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getLetterSpacing();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetLetterSpacing
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat letterSpacing) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setLetterSpacing(letterSpacing);
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetWordSpacing
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getWordSpacing();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetWordSpacing
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat wordSpacing) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setWordSpacing(wordSpacing);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetTypeface
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(instance->refTypeface().release());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetTypeface
  (JNIEnv* env, jclass jclass, jlong ptr, jlong typefacePtr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    SkTypeface* typeface = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(typefacePtr));
    instance->setTypeface(sk_ref_sp(typeface));
}

extern "C" JNIEXPORT jstring JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetLocale
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return javaString(env, instance->getLocale());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetLocale
  (JNIEnv* env, jclass jclass, jlong ptr, jstring locale) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setLocale(skString(env, locale));
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetBaselineMode
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->getTextBaseline());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetBaselineMode
  (JNIEnv* env, jclass jclass, jlong ptr, jint baselineModeValue) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setTextBaseline(static_cast<TextBaseline>(baselineModeValue));
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nGetFontMetrics
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    SkFontMetrics m;
    instance->getFontMetrics(&m);
    return skija::FontMetrics::toJava(env, m);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nIsPlaceholder
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    return instance->isPlaceholder();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_TextStyle__1nSetPlaceholder
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setPlaceholder();
}

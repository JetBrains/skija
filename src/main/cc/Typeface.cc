#include <iostream>
#include <jni.h>
#include "SkData.h"
#include "SkTypeface.h"
#include "interop.hh"

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Typeface__1nGetFontStyle
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    return skija::FontStyle::toJava(instance->fontStyle());
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Typeface__1nIsFixedPitch
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    return instance->isFixedPitch();
}

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_Typeface__1nGetVariationPosition
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    int count = instance->getVariationDesignPosition(nullptr, 0);
    if (count > 0) {
        SkFontArguments::VariationPosition::Coordinate coords[count];
        instance->getVariationDesignPosition(coords, count);
        jobjectArray res = env->NewObjectArray(count, skija::FontVariation::cls, nullptr);
        for (int i=0; i < count; ++i) {
            jobject var = env->NewObject(skija::FontVariation::cls, skija::FontVariation::ctor, coords[i].axis, coords[i].value);
            env->SetObjectArrayElement(res, i, var);
        }
        return res;
    } else
        return nullptr;
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Typeface__1nGetUniqueId
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    return instance->uniqueID();
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Typeface__1nEquals
  (JNIEnv* env, jclass jclass, jlong ptr, jlong otherPtr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    SkTypeface* other = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(otherPtr));
    return SkTypeface::Equal(instance, other);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nMakeDefault
  (JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(SkTypeface::MakeDefault().release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nMakeFromName
  (JNIEnv* env, jclass jclass, jstring nameStr, jint styleValue) {
    SkString name = skString(env, nameStr);
    SkFontStyle style = skija::FontStyle::fromJava(styleValue);
    SkTypeface* instance = SkTypeface::MakeFromName(name.c_str(), style).release();
    return reinterpret_cast<jlong>(instance);
}
    
extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nMakeFromFile
  (JNIEnv* env, jclass jclass, jstring pathStr, jint index) {
    SkString path = skString(env, pathStr);
    SkTypeface* ptr = SkTypeface::MakeFromFile(path.c_str(), index).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nMakeFromData
  (JNIEnv* env, jclass jclass, jlong dataPtr, jint index) {
    SkData* data = reinterpret_cast<SkData*>(static_cast<uintptr_t>(dataPtr));
    SkTypeface* ptr = SkTypeface::MakeFromData(sk_ref_sp(data), index).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nMakeClone
  (JNIEnv* env, jclass jclass, jlong typefacePtr, jobjectArray variations, jint collectionIndex) {
    SkTypeface* typeface = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(typefacePtr));
    int variationCount = env->GetArrayLength(variations);
    SkFontArguments::VariationPosition::Coordinate coordinates[variationCount];
    for (int i=0; i < variationCount; ++i) {
        jobject jvar = env->GetObjectArrayElement(variations, i);
        coordinates[i] = {
            static_cast<SkFourByteTag>(env->GetIntField(jvar, skija::FontVariation::tag)),
            env->GetFloatField(jvar, skija::FontVariation::value)
        };
        env->DeleteLocalRef(jvar);
    }
    SkFontArguments arg = SkFontArguments()
                            .setCollectionIndex(collectionIndex)
                            .setVariationDesignPosition({coordinates, variationCount});
    SkTypeface* clone = typeface->makeClone(arg).release();
    return reinterpret_cast<jlong>(clone);
}

extern "C" JNIEXPORT jshortArray JNICALL Java_org_jetbrains_skija_Typeface__1nGetGlyphIds
  (JNIEnv* env, jclass jclass, jlong ptr, jintArray uniArr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    int count = env->GetArrayLength(uniArr);
    std::vector<short> glyphs(count);
    jint* uni = env->GetIntArrayElements(uniArr, nullptr);
    instance->unicharsToGlyphs(uni, count, reinterpret_cast<SkGlyphID*>(glyphs.data()));
    env->ReleaseIntArrayElements(uniArr, uni, 0);
    return javaShortArray(env, glyphs);
}

extern "C" JNIEXPORT jshort JNICALL Java_org_jetbrains_skija_Typeface__1nGetGlyphId
  (JNIEnv* env, jclass jclass, jlong ptr, jint uni) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    return instance->unicharToGlyph(uni);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Typeface__1nGetGlyphsCount
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    return instance->countGlyphs();
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Typeface__1nGetTablesCount
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    return instance->countTables();
}

extern "C" JNIEXPORT jintArray JNICALL Java_org_jetbrains_skija_Typeface__1nGetTableTags
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    int count = instance->countTables();
    std::vector<int> tags(count);
    instance->getTableTags(reinterpret_cast<SkFontTableTag*>(tags.data()));
    return javaIntArray(env, tags);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nGetTableSize
  (JNIEnv* env, jclass jclass, jlong ptr, jint tag) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    return instance->getTableSize(tag);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nGetTableData
  (JNIEnv* env, jclass jclass, jlong ptr, jint tag) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    SkData* data = instance->copyTableData(tag).release();
    return reinterpret_cast<jlong>(data);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Typeface__1nGetUnitsPerEm
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    return instance->getUnitsPerEm();
}

extern "C" JNIEXPORT jintArray JNICALL Java_org_jetbrains_skija_Typeface__1nGetKerningPairAdjustments
  (JNIEnv* env, jclass jclass, jlong ptr, jshortArray glyphsArr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    int count = glyphsArr == nullptr ? 0 : env->GetArrayLength(glyphsArr);
    if (count > 0) {
        std::vector<int32_t> adjustments(count);
        jshort* glyphs = env->GetShortArrayElements(glyphsArr, nullptr);
        bool res = instance->getKerningPairAdjustments(reinterpret_cast<SkGlyphID*>(glyphs), count, adjustments.data());
        env->ReleaseShortArrayElements(glyphsArr, glyphs, 0);
        return res ? javaIntArray(env, adjustments) : nullptr;
    } else {
        bool res = instance->getKerningPairAdjustments(nullptr, 0, nullptr);
        return res ? javaIntArray(env, std::vector<int32_t>(0)) : nullptr;
    }
}

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_Typeface__1nGetFamilyNames
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    SkTypeface::LocalizedStrings* iter = instance->createFamilyNameIterator();
    std::vector<SkTypeface::LocalizedString> names;
    SkTypeface::LocalizedString name;
    while (iter->next(&name)) {
        names.push_back(name);
    }
    iter->unref();
    jobjectArray res = env->NewObjectArray(names.size(), skija::FontFamilyName::cls, nullptr);
    for (int i = 0; i < names.size(); ++i) {
        jstring nameStr = javaString(env, names[i].fString);
        jstring langStr = javaString(env, names[i].fLanguage);
        jobject obj = env->NewObject(skija::FontFamilyName::cls, skija::FontFamilyName::ctor, nameStr, langStr);
        env->SetObjectArrayElement(res, i, obj);
    }
    return res;
}

extern "C" JNIEXPORT jstring JNICALL Java_org_jetbrains_skija_Typeface__1nGetFamilyName
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    SkString name;
    instance->getFamilyName(&name);
    return javaString(env, name);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Typeface__1nGetBounds
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    return skija::Rect::fromSkRect(env, instance->getBounds());
}
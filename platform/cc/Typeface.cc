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

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_Typeface__1nGetVariations
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    int count = instance->getVariationDesignPosition(nullptr, 0);
    if (count > 0) {
        std::vector<SkFontArguments::VariationPosition::Coordinate> coords(count);
        instance->getVariationDesignPosition(coords.data(), count);
        jobjectArray res = env->NewObjectArray(count, skija::FontVariation::cls, nullptr);
        for (int i=0; i < count; ++i) {
            jobject var = env->NewObject(skija::FontVariation::cls, skija::FontVariation::ctor, coords[i].axis, coords[i].value);
            env->SetObjectArrayElement(res, i, var);
        }
        return res;
    } else
        return nullptr;
}

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_Typeface__1nGetVariationAxes
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    int count = instance->getVariationDesignParameters(nullptr, 0);
    if (count > 0) {
        std::vector<SkFontParameters::Variation::Axis> params(count);
        instance->getVariationDesignParameters(params.data(), count);
        jobjectArray res = env->NewObjectArray(count, skija::FontVariationAxis::cls, nullptr);
        for (int i=0; i < count; ++i) {
            jobject var = env->NewObject(skija::FontVariationAxis::cls, skija::FontVariationAxis::ctor, params[i].tag, params[i].min, params[i].def, params[i].max, params[i].isHidden());
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

// TODO remove after https://bugs.chromium.org/p/skia/issues/detail?id=10929
sk_sp<SkTypeface> setDefaultVariationCoords(sk_sp<SkTypeface> face) {
    #if defined(SK_BUILD_FOR_WIN)
        int count = face->getVariationDesignParameters(nullptr, 0);
        if (count > 0) {
            std::vector<SkFontParameters::Variation::Axis> params(count);
            face->getVariationDesignParameters(params.data(), count);
            std::vector<SkFontArguments::VariationPosition::Coordinate> coords(count);
            for (int i = 0; i < count; ++i) {
                coords[i].axis = params[i].tag;
                coords[i].value = params[i].def;
            }
            SkFontArguments arg;
            arg.setVariationDesignPosition({coords.data(), count});
            return face->makeClone(arg);
        }
    #endif

    return face;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nMakeFromName
  (JNIEnv* env, jclass jclass, jstring nameStr, jint styleValue) {
    SkString name = skString(env, nameStr);
    SkFontStyle style = skija::FontStyle::fromJava(styleValue);
    sk_sp<SkTypeface> instance = SkTypeface::MakeFromName(name.c_str(), style);
    SkTypeface* ptr = setDefaultVariationCoords(instance).release();
    return reinterpret_cast<jlong>(ptr);
}
    
extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nMakeFromFile
  (JNIEnv* env, jclass jclass, jstring pathStr, jint index) {
    SkString path = skString(env, pathStr);
    sk_sp<SkTypeface> instance = SkTypeface::MakeFromFile(path.c_str(), index);
    SkTypeface* ptr = setDefaultVariationCoords(instance).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nMakeFromData
  (JNIEnv* env, jclass jclass, jlong dataPtr, jint index) {
    SkData* data = reinterpret_cast<SkData*>(static_cast<uintptr_t>(dataPtr));
    sk_sp<SkTypeface> instance = SkTypeface::MakeFromData(sk_ref_sp(data), index);
    SkTypeface* ptr = setDefaultVariationCoords(instance).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface__1nMakeClone
  (JNIEnv* env, jclass jclass, jlong typefacePtr, jobjectArray variations, jint collectionIndex) {
    SkTypeface* typeface = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(typefacePtr));
    int variationCount = env->GetArrayLength(variations);
    std::vector<SkFontArguments::VariationPosition::Coordinate> coordinates(variationCount);
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
                            .setVariationDesignPosition({coordinates.data(), variationCount});
    SkTypeface* clone = typeface->makeClone(arg).release();
    return reinterpret_cast<jlong>(clone);
}

extern "C" JNIEXPORT jshortArray JNICALL Java_org_jetbrains_skija_Typeface__1nGetUTF32Glyphs
  (JNIEnv* env, jclass jclass, jlong ptr, jintArray uniArr) {
    SkTypeface* instance = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(ptr));
    jint count = env->GetArrayLength(uniArr);
    std::vector<short> glyphs(count);
    jint* uni = env->GetIntArrayElements(uniArr, nullptr);
    instance->unicharsToGlyphs(reinterpret_cast<SkUnichar*>(uni), count, reinterpret_cast<SkGlyphID*>(glyphs.data()));
    env->ReleaseIntArrayElements(uniArr, uni, 0);
    return javaShortArray(env, glyphs);
}

extern "C" JNIEXPORT jshort JNICALL Java_org_jetbrains_skija_Typeface__1nGetUTF32Glyph
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
    std::vector<jint> tags(count);
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
        std::vector<jint> adjustments(count);
        jshort* glyphs = env->GetShortArrayElements(glyphsArr, nullptr);
        bool res = instance->getKerningPairAdjustments(
          reinterpret_cast<SkGlyphID*>(glyphs), count,
            reinterpret_cast<int32_t*>(adjustments.data()));
        env->ReleaseShortArrayElements(glyphsArr, glyphs, 0);
        return res ? javaIntArray(env, adjustments) : nullptr;
    } else {
        bool res = instance->getKerningPairAdjustments(nullptr, 0, nullptr);
        return res ? javaIntArray(env, std::vector<jint>(0)) : nullptr;
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
    jobjectArray res = env->NewObjectArray((jsize) names.size(), skija::FontFamilyName::cls, nullptr);
    for (int i = 0; i < names.size(); ++i) {
        skija::AutoLocal<jstring> nameStr(env, javaString(env, names[i].fString));
        skija::AutoLocal<jstring> langStr(env, javaString(env, names[i].fLanguage));
        skija::AutoLocal<jobject> obj(env, env->NewObject(skija::FontFamilyName::cls, skija::FontFamilyName::ctor, nameStr.get(), langStr.get()));
        env->SetObjectArrayElement(res, i, obj.get());
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
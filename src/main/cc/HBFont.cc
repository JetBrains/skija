#include <iostream>
#include <jni.h>
#include "hb.h"
#include "hb_util.hh"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_skija_HBFont_nInit(JNIEnv* env, jclass jclass, jlong facePtr, jfloat size, jobjectArray variations) {
    hb_face_t* face = reinterpret_cast<hb_face_t*>(static_cast<uintptr_t>(facePtr));
    hb_font_t* font = hb_font_create(face);

    hb_font_set_ppem(font, size, size);
    hb_font_set_scale(font, HBFloatToFixed(size), HBFloatToFixed(size));
    // hb_ot_font_set_funcs(font);

    int variationCount = env->GetArrayLength(variations);
    if (variationCount > 0) {
        maybeInitFontVariationClass(env);
        hb_variation_t variationData[variationCount];
        for (int i=0; i < variationCount; ++i) {
            jobject jvar = env->GetObjectArrayElement(variations, i);
            variationData[i].tag = env->GetIntField(jvar, fontVariationClass->tagID);
            variationData[i].value = env->GetFloatField(jvar, fontVariationClass->valueID);
        }
        hb_font_set_variations(font, variationData, variationCount);
    }

    return reinterpret_cast<jlong>(font);
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_HBFont_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&hb_font_destroy));
}

extern "C" JNIEXPORT jfloatArray JNICALL Java_skija_HBFont_nGetHorizontalExtents(JNIEnv* env, jclass jclass, jlong ptr) {
    hb_font_t* font = reinterpret_cast<hb_font_t*>(static_cast<uintptr_t>(ptr));
    hb_font_extents_t extents; 
    hb_font_get_h_extents(font, &extents);
    return javaFloatArray(env, {-HBFixedToFloat(extents.ascender), -HBFixedToFloat(extents.descender), HBFixedToFloat(extents.line_gap)}); // TODO invert lineGap?
}

extern "C" JNIEXPORT jfloatArray JNICALL Java_skija_HBFont_nGetVerticalExtents(JNIEnv* env, jclass jclass, jlong ptr) {
    hb_font_t* font = reinterpret_cast<hb_font_t*>(static_cast<uintptr_t>(ptr));
    hb_font_extents_t extents; 
    hb_font_get_v_extents(font, &extents);
    return javaFloatArray(env, {HBFixedToFloat(extents.ascender), HBFixedToFloat(extents.descender), HBFixedToFloat(extents.line_gap)});
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_HBFont_nShape(JNIEnv* env, jclass jclass, jlong ptr, jstring text, jintArray featuresArray) {
    hb_font_t* font = reinterpret_cast<hb_font_t*>(static_cast<uintptr_t>(ptr));

    hb_buffer_t *buffer = hb_buffer_create();
    const char* chars = env->GetStringUTFChars(text, nullptr);
    hb_buffer_add_utf8(buffer, chars, -1, 0, -1);
    env->ReleaseStringUTFChars(text, chars);
    hb_buffer_guess_segment_properties(buffer);

    jsize featuresLen = env->GetArrayLength(featuresArray);
    jint* features = env->GetIntArrayElements(featuresArray, 0);
    hb_shape(font, buffer, reinterpret_cast<hb_feature_t*>(features), featuresLen / 4);
    env->ReleaseIntArrayElements(featuresArray, features, 0);
    return reinterpret_cast<jlong>(buffer);
}

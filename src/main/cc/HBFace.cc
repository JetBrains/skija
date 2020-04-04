#include <iostream>
#include <jni.h>
#include "hb.h"
#include "hb_util.hh"

extern "C" JNIEXPORT jlong JNICALL Java_skija_HBFace_nMakeFromFile(JNIEnv* env, jclass jclass, jstring path, jint index) {
    const char* chars = env->GetStringUTFChars(path, nullptr);
    hb_blob_t* blob = hb_blob_create_from_file(chars);
    env->ReleaseStringUTFChars(path, chars);

    assert(blob);
    if (!blob || hb_blob_get_length(blob) == 0) return 0;
    hb_blob_make_immutable(blob);
    hb_face_t* face = hb_face_create(blob, (unsigned)index);
    hb_blob_destroy(blob);

    assert(face);
    if (!face) return 0;
    hb_face_set_index(face, (unsigned)index);
    // hb_face_set_upem(face, fSkiaTypeface->getUnitsPerEm());
    
    return reinterpret_cast<jlong>(face);
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_HBFace_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&hb_face_destroy));
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_HBFace_nShape(JNIEnv* env, jclass jclass, jlong ptr, jstring text, jfloat size, jstring opts) {
    hb_face_t* face = reinterpret_cast<hb_face_t*>(static_cast<uintptr_t>(ptr));
    hb_font_t* font = hb_font_create(face);

    hb_font_set_ppem(font, size, size);
    hb_font_set_scale(font, HBFloatToFixed(size), HBFloatToFixed(size));
    // hb_ot_font_set_funcs(font);

    hb_buffer_t *buffer = hb_buffer_create();
    const char* chars = env->GetStringUTFChars(text, nullptr);
    hb_buffer_add_utf8(buffer, chars, -1, 0, -1);
    env->ReleaseStringUTFChars(text, chars);
    hb_buffer_guess_segment_properties(buffer);

    hb_shape(font, buffer, NULL, 0);
    return reinterpret_cast<jlong>(buffer);
}

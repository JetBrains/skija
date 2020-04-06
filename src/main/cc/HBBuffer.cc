#include <iostream>
#include <jni.h>
#include "hb.h"
#include "interop.hh"
#include "hb_util.hh"

extern "C" JNIEXPORT jfloatArray JNICALL Java_skija_HBBuffer_nGetAdvances(JNIEnv* env, jclass jclass, jlong ptr) {
    hb_buffer_t* hb_buffer = reinterpret_cast<hb_buffer_t*>(static_cast<uintptr_t>(ptr));
    unsigned len = hb_buffer_get_length (hb_buffer);
    hb_glyph_info_t *info = hb_buffer_get_glyph_infos (hb_buffer, NULL);
    hb_glyph_position_t *pos = hb_buffer_get_glyph_positions (hb_buffer, NULL);
    float xAdvance = 0;
    float yAdvance = 0;
    for (int i = 0; i < len; ++i) {
        xAdvance += HBFixedToFloat(pos[i].x_advance);
        yAdvance += HBFixedToFloat(pos[i].y_advance);
    }
    return javaFloatArray(env, {xAdvance, yAdvance});
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_HBBuffer_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t> (&hb_buffer_destroy));
}

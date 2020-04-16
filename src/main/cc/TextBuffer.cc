#include <iostream>
#include <jni.h>
#include "hb.h"
#include "interop.hh"
#include "hb_util.hh"

extern "C" JNIEXPORT jfloatArray JNICALL Java_org_jetbrains_skija_TextBuffer_nGetAdvances(JNIEnv* env, jclass jclass, jlong ptr) {
    hb_buffer_t* hb_buffer = reinterpret_cast<hb_buffer_t*>(static_cast<uintptr_t>(ptr));
    unsigned len = hb_buffer_get_length(hb_buffer);
    hb_glyph_position_t *pos = hb_buffer_get_glyph_positions (hb_buffer, NULL);
    int xAdvance = 0;
    int yAdvance = 0;
    for (int i = 0; i < len; ++i) {
        xAdvance += pos[i].x_advance;
        yAdvance += pos[i].y_advance;
    }
    return javaFloatArray(env, {HBFixedToFloat(xAdvance), HBFixedToFloat(yAdvance)});
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBuffer_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t> (&hb_buffer_destroy));
}

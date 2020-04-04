#include <iostream>
#include <jni.h>
#include "hb.h"
#include "interop.hh"
#include "hb_util.hh"

extern "C" JNIEXPORT jintArray JNICALL Java_skija_HBBuffer_nGetBounds (JNIEnv* env, jclass jclass, jlong ptr) {
    hb_buffer_t* hb_buffer = reinterpret_cast<hb_buffer_t*>(static_cast<uintptr_t>(ptr));
    unsigned len = hb_buffer_get_length (hb_buffer);
    hb_glyph_info_t *info = hb_buffer_get_glyph_infos (hb_buffer, NULL);
    hb_glyph_position_t *pos = hb_buffer_get_glyph_positions (hb_buffer, NULL);
    int maxX = 0;
    int maxY = 0;
    for (int i = 0; i < len; ++i) {
        float posX = HBFixedToFloat(pos[i].x_offset);
        float posY = HBFixedToFloat(pos[i].y_offset);
        float sizeX = HBFixedToFloat(pos[i].x_advance);
        float sizeY = HBFixedToFloat(pos[i].y_advance);

        if (posX + sizeX > maxX) maxX = posX + sizeX;
        if (posY + sizeY > maxY) maxY = posY + sizeY;

        std::cout << "[" << info[i].codepoint << " at " << posX << ", " << posY << " size " << sizeX << "x" << sizeY <<  "] ";
    }
    std::cout << std::endl;

    return javaIntArray(env, {0, 0, maxX, maxY});
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_HBBuffer_nGetNativeFinalizer (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t> (&hb_buffer_destroy));
}

#include <iostream>
#include <jni.h>
#include "hb-ot.h"
#include "hb_util.hh"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_HBFace_nMakeFromFile(JNIEnv* env, jclass jclass, jstring path, jint index) {
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
    // hb_face_set_index(face, (unsigned)index);
    // hb_face_set_upem(face, fSkiaTypeface->getUnitsPerEm());

    return reinterpret_cast<jlong>(face);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_HBFace_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&hb_face_destroy));
}

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_HBFace_nGetAxes(JNIEnv* env, jclass jclass, jlong ptr) {
    hb_face_t* face = reinterpret_cast<hb_face_t*>(static_cast<uintptr_t>(ptr));
    unsigned int axis_count = hb_ot_var_get_axis_count(face);
    hb_ot_var_axis_info_t axes_array[axis_count];
    hb_ot_var_get_axis_infos(face, 0, &axis_count, axes_array);

    maybeInitFontAxisInfoClass(env);
    jobjectArray res = env->NewObjectArray(axis_count, fontAxisInfoClass->cls, nullptr);
    for (int i = 0; i < axis_count; ++i) {
        hb_ot_var_axis_info_t info = axes_array[i];
        char nameBuf[256];
        unsigned int nameBufLen = 256;
        hb_ot_name_get_utf8(face, info.name_id, HB_LANGUAGE_INVALID, &nameBufLen, nameBuf);
        jbyteArray nameBytes = env->NewByteArray(nameBufLen);
        env->SetByteArrayRegion(nameBytes, 0, nameBufLen, reinterpret_cast<signed char *>(nameBuf));
        jobject obj = env->NewObject(fontAxisInfoClass->cls, fontAxisInfoClass->ctorID, info.axis_index, info.tag, nameBytes, info.flags, info.min_value, info.default_value, info.max_value);
        env->SetObjectArrayElement(res, i, obj);
    }

    return res;
}

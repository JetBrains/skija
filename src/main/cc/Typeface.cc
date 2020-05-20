#include <jni.h>
#include <cassert>
#include "SkData.h"
#include "SkStream.h"
#include "SkTypeface.h"
#include "hb.h"
#include "hb_util.hh"
#include "interop.hh"

extern "C" JNIEXPORT jlongArray JNICALL Java_org_jetbrains_skija_Typeface_nMakeFromFile(JNIEnv* env, jclass jclass, jstring path, jint index) {
    // data
    const char* chars = env->GetStringUTFChars(path, nullptr);
    auto data = SkData::MakeFromFileName(chars);
    env->ReleaseStringUTFChars(path, chars);
    assert(data);
    if (!data) { return nullptr; }

    // skTypeface
    SkTypeface* skTypeface = SkTypeface::MakeFromData(data, index).release();

    // blob
    auto destroy = [](void *d) { static_cast<SkData*>(d)->unref(); };
    hb_blob_t* blob = hb_blob_create((const char*)data->data(),
                                     (unsigned int)data->size(),
                                     HB_MEMORY_MODE_READONLY,
                                     data.release(),
                                     destroy);
    assert(blob);
    if (!blob || hb_blob_get_length(blob) == 0) return nullptr;
    hb_blob_make_immutable(blob);

    // hbFace
    hb_face_t* hbFace = hb_face_create(blob, (unsigned) index);
    hb_blob_destroy(blob);

    assert(hbFace);
    if (!hbFace) {
        skTypeface->unref();
        return nullptr;
    }
    // hb_face_set_index(hbFace, (unsigned)index);
    // hb_face_set_upem(hbFace, fSkiaTypeface->getUnitsPerEm());

    return javaLongArray(env, {reinterpret_cast<jlong>(skTypeface), reinterpret_cast<jlong>(hbFace)});
}

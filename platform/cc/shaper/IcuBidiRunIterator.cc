#include <jni.h>
#include "../interop.hh"
#include "SkShaper.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_IcuBidiRunIterator__1nMake
  (JNIEnv* env, jclass jclass, jlong textPtr, jint bidiLevel) {
    SkString* text = reinterpret_cast<SkString*>(static_cast<uintptr_t>(textPtr));
    std::unique_ptr<SkShaper::BiDiRunIterator> instance(SkShaper::MakeIcuBiDiRunIterator(text->c_str(), text->size(), bidiLevel & 0xFF));
    return reinterpret_cast<jlong>(instance.release());
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_shaper_IcuBidiRunIterator__1nGetCurrentLevel
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkShaper::BiDiRunIterator* instance = reinterpret_cast<SkShaper::BiDiRunIterator*>(static_cast<uintptr_t>(ptr));
    return instance->currentLevel();
}

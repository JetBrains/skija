#include <jni.h>
#include "../interop.hh"
#include "SkShaper.h"

static void deleteRunIterator(SkShaper::RunIterator* instance) {
    // std::cout << "Deleting [RunIterator " << instance << "]" << std::endl;
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_ManagedRunIterator__1nGetFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteRunIterator));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_shaper_ManagedRunIterator__1nConsume
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkShaper::RunIterator* instance = reinterpret_cast<SkShaper::RunIterator*>(static_cast<uintptr_t>(ptr));
    instance->consume();
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_shaper_ManagedRunIterator__1nGetEndOfCurrentRun
  (JNIEnv* env, jclass jclass, jlong ptr, jlong textPtr) {
    SkShaper::RunIterator* instance = reinterpret_cast<SkShaper::RunIterator*>(static_cast<uintptr_t>(ptr));
    SkString* text = reinterpret_cast<SkString*>(static_cast<uintptr_t>(textPtr));
    size_t end8 = instance->endOfCurrentRun();
    return skija::UtfIndicesConverter(*text).from8To16(end8);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_shaper_ManagedRunIterator__1nIsAtEnd
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkShaper::RunIterator* instance = reinterpret_cast<SkShaper::RunIterator*>(static_cast<uintptr_t>(ptr));
    return instance->atEnd();
}

#include <jni.h>
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

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_shaper_ManagedRunIterator__1nGetEndOfCurrentRun
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkShaper::RunIterator* instance = reinterpret_cast<SkShaper::RunIterator*>(static_cast<uintptr_t>(ptr));
    return instance->endOfCurrentRun(); // FIXME convert to UTF-16
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_shaper_ManagedRunIterator__1nIsAtEnd
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkShaper::RunIterator* instance = reinterpret_cast<SkShaper::RunIterator*>(static_cast<uintptr_t>(ptr));
    return instance->atEnd();
}

#include <jni.h>
#include "interop.hh"
#include "SkString.h"

static void deleteString(SkString* instance) {
    // std::cout << "Deleting [SkString " << instance << "]" << std::endl;
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ManagedString__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteString));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ManagedString__1nMake
  (JNIEnv* env, jclass jclass, jstring textStr) {
    SkString* text = new SkString(skString(env, textStr));
    return reinterpret_cast<jlong>(text);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_ManagedString__1nToString
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkString* instance = reinterpret_cast<SkString*>(static_cast<uintptr_t>(ptr));
    return javaString(env, *instance);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_ManagedString__1nInsert
  (JNIEnv* env, jclass jclass, jlong ptr, jint offset, jstring s) {
    SkString* instance = reinterpret_cast<SkString*>(static_cast<uintptr_t>(ptr));
    skija::UtfIndicesConverter conv(*instance);
    instance->insert(conv.from16To8(offset), skString(env, s));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_ManagedString__1nAppend
  (JNIEnv* env, jclass jclass, jlong ptr, jstring s) {
    SkString* instance = reinterpret_cast<SkString*>(static_cast<uintptr_t>(ptr));
    instance->append(skString(env, s));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_ManagedString__1nRemoveSuffix
  (JNIEnv* env, jclass jclass, jlong ptr, jint from) {
    SkString* instance = reinterpret_cast<SkString*>(static_cast<uintptr_t>(ptr));
    skija::UtfIndicesConverter conv(*instance);
    size_t from8 = conv.from16To8(from);
    instance->remove(from8, instance->size() - from8);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_ManagedString__1nRemove
  (JNIEnv* env, jclass jclass, jlong ptr, jint from, jint len) {
    SkString* instance = reinterpret_cast<SkString*>(static_cast<uintptr_t>(ptr));
    skija::UtfIndicesConverter conv(*instance);
    size_t from8 = conv.from16To8(from);
    size_t to8 = conv.from16To8(from + len);
    instance->remove(from8, to8 - from8);
}

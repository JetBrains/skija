#include <jni.h>
#include "interop.hh"
#include "SkData.h"

static void deleteData(SkData* data) {
    // std::cout << "Deleting [SkData " << data << "]" << std::endl;
    data->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Data_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteData));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Data_nSize
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkData* instance = reinterpret_cast<SkData*>(static_cast<uintptr_t>(ptr));
    return instance->size();
}

extern "C" JNIEXPORT jbyteArray JNICALL Java_org_jetbrains_skija_Data_nBytes
  (JNIEnv* env, jclass jclass, jlong ptr, jlong offset, jlong length) {
    SkData* instance = reinterpret_cast<SkData*>(static_cast<uintptr_t>(ptr));
    jbyteArray bytesArray = env->NewByteArray(length);
    const jbyte* bytes = reinterpret_cast<const jbyte*>(instance->bytes() + offset);
    env->SetByteArrayRegion(bytesArray, 0, length, bytes); 
    return bytesArray;
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Data_nEquals
  (JNIEnv* env, jclass jclass, jlong ptr, jlong otherPtr) {
    SkData* instance = reinterpret_cast<SkData*>(static_cast<uintptr_t>(ptr));
    SkData* other = reinterpret_cast<SkData*>(static_cast<uintptr_t>(otherPtr));
    return instance->equals(other);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Data_nMakeFromBytes
  (JNIEnv* env, jclass jclass, jbyteArray bytesArray, jlong offset, jlong length) {
    jbyte* bytes = reinterpret_cast<jbyte*>(malloc(length));
    if (!bytes) return 0;
    env->GetByteArrayRegion(bytesArray, offset, length, bytes);
    SkData* instance = SkData::MakeFromMalloc(bytes, length).release();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Data_nMakeFromFileName
  (JNIEnv* env, jclass jclass, jstring pathStr) {
    const char* path = env->GetStringUTFChars(pathStr, nullptr);
    SkData* instance = SkData::MakeFromFileName(path).release();
    env->ReleaseStringUTFChars(pathStr, path);
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Data_nMakeSubset
  (JNIEnv* env, jclass jclass, jlong ptr, jlong offset, jlong length) {
    SkData* instance = reinterpret_cast<SkData*>(static_cast<uintptr_t>(ptr));
    SkData* subset = SkData::MakeSubset(instance, offset, length).release();
    return reinterpret_cast<jlong>(subset);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Data_nMakeEmpty
  (JNIEnv* env, jclass jclass) {
    SkData* instance = SkData::MakeEmpty().release();
    return reinterpret_cast<jlong>(instance);
}

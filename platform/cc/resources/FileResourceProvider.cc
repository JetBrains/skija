#include <jni.h>
#include "../interop.hh"
#include "SkResources.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_resources_FileResourceProvider__1nMake
  (JNIEnv* env, jclass jclass, jstring basePathStr, jboolean predecode) {
    SkString basePath = skString(env, basePathStr);
    auto instance = skresources::FileResourceProvider::Make(basePath, predecode);
    return ptrToJlong(instance.release());
}
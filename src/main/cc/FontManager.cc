#include <iostream>
#include <jni.h>
#include "SkTypeface.h"
#include "SkFontMgr.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontManager_nDefault
  (JNIEnv* env, jclass jclass) {
    SkFontMgr* instance = SkFontMgr::RefDefault().release();    
    return reinterpret_cast<jlong>(instance);
}

#include <jni.h>
#include "SkRefCnt.h"
#include "FontCollection.h"

using namespace std;
using namespace skia::textlayout;

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_FontCollection_nInit
  (JNIEnv* env, jclass jclass) {
    FontCollection* ptr = new FontCollection();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_FontCollection_nSetDefaultFontManager
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontManagerPtr) {
    FontCollection* instance = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(ptr));
    SkFontMgr* fontManager = reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontManagerPtr));
    instance->setDefaultFontManager(sk_ref_sp(fontManager));
}

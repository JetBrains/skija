#include <jni.h>
#include "SkSurfaceProps.h"
#include "interop.hh"

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_SurfaceProps__1nComputeDefaultGeometry
  (JNIEnv* env, jclass jclass) {
    SkSurfaceProps sp(SkSurfaceProps::kLegacyFontHost_InitType);
    return static_cast<jint>(sp.pixelGeometry());
}

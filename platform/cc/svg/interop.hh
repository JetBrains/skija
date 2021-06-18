#pragma once
#include <jni.h>
#include "SkSVGTypes.h"

namespace skija {
    namespace svg {
        namespace SVGLength {
            extern jclass cls;
            extern jmethodID ctor;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
            jobject toJava(JNIEnv* env, SkSVGLength length);
        }

        namespace SVGPreserveAspectRatio {
            extern jclass cls;
            extern jmethodID ctor;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
            jobject toJava(JNIEnv* env, SkSVGPreserveAspectRatio ratio);
        }

        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }
}
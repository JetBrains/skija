#pragma once
#include <jni.h>
#include "../interop.hh"

namespace skija {
    namespace skottie {
        namespace Logger {
            extern jclass cls;
            extern jmethodID log;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        namespace LogLevel {
            extern jobject WARNING;
            extern jobject ERROR;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }
}
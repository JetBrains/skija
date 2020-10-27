#pragma once
#include <jni.h>
#include "SkShaper.h"

namespace skija {
    namespace shaper {
        namespace RunHandler {
            extern jclass cls;
            extern jmethodID beginLine;
            extern jmethodID runInfo;
            extern jmethodID commitRunInfo;
            extern jmethodID runOffset;
            extern jmethodID commitRun;
            extern jmethodID commitLine;
            
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        namespace RunInfo {
            extern jclass cls;
            extern jmethodID ctor;

            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
            jobject toJava(JNIEnv* env, const SkShaper::RunHandler::RunInfo& info);
        }
    }
}
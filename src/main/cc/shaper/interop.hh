#pragma once
#include <jni.h>
#include "../interop.hh"
#include "SkShaper.h"

namespace skija {
    namespace shaper {
        namespace BidiRun {
            extern jfieldID _end;
            extern jfieldID _level;

            void onLoad(JNIEnv* env);
        }

        namespace FontMgrRunIterator {
            extern jclass cls;

            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        namespace FontRun {
            extern jfieldID _end;
            extern jmethodID _getFontPtr;

            void onLoad(JNIEnv* env);
        }

        namespace HbIcuScriptRunIterator {
            extern jclass cls;

            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        namespace IcuBidiRunIterator {
            extern jclass cls;

            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        namespace LanguageRun {
            extern jfieldID _end;
            extern jfieldID _language;

            void onLoad(JNIEnv* env);
        }

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
            jobject toJava(JNIEnv* env, const SkShaper::RunHandler::RunInfo& info, skija::UtfIndicesConverter& indicesConverter);
        }

        namespace ScriptRun {
            extern jfieldID _end;
            extern jfieldID _scriptTag;

            void onLoad(JNIEnv* env);
        }
    }
}
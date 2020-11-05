#pragma once
#include <jni.h>
#include "SkShaper.h"

namespace skija {
    namespace shaper {
        namespace BidiRunIterator {
            extern jmethodID getCurrentLevel;

            void onLoad(JNIEnv* env);
        }

        namespace FontMgrRunIterator {
            extern jclass cls;

            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        namespace FontRunIterator {
            extern jmethodID _getCurrentFontPtr;

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

        namespace LanguageRunIterator {
            extern jmethodID getCurrentLanguage;

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
            jobject toJava(JNIEnv* env, const SkShaper::RunHandler::RunInfo& info);
        }

        namespace RunIterator {
            extern jmethodID consume;
            extern jmethodID getEndOfCurrentRun;
            extern jmethodID isAtEnd;

            void onLoad(JNIEnv* env);
        }
 
        namespace ScriptRunIterator {
            extern jmethodID _getCurrentScriptTag;

            void onLoad(JNIEnv* env);
        }
  }
}
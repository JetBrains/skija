#pragma once
#include <jni.h>
#include "../interop.hh"
#include "SkShaper.h"
#include "unicode/ubrk.h"
#include "unicode/utext.h"

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
            extern jfieldID _fontPtr;

            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
            jobject toJava(JNIEnv* env, const SkShaper::RunHandler::RunInfo& info, size_t begin, size_t end);
            jobject toJava(JNIEnv* env, const SkShaper::RunHandler::RunInfo& info, skija::UtfIndicesConverter& indicesConverter);
        }

        namespace ScriptRun {
            extern jfieldID _end;
            extern jfieldID _scriptTag;

            void onLoad(JNIEnv* env);
        }

        namespace ShapingOptions {
            extern jfieldID _fontMgr;
            extern jfieldID _features;
            extern jfieldID _leftToRight;
            extern jfieldID _approximateSpaces;
            extern jfieldID _approximatePunctuation;

            void onLoad(JNIEnv* env);
            std::vector<SkShaper::Feature> getFeatures(JNIEnv* env, jobject opts);
        }

        namespace TextBlobBuilderRunHandler {
             extern jclass cls;

            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }
       
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);        

        using ICUUText = std::unique_ptr<UText, SkFunctionWrapper<decltype(utext_close), utext_close>>;
        std::shared_ptr<UBreakIterator> graphemeBreakIterator(SkString& text);
    }
}
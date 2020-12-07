#include <jni.h>
#include "interop.hh"
#include "SkFont.h"
#include "SkShaper.h"

namespace skija {
    namespace shaper {
        namespace BidiRun {
            jfieldID _end;
            jfieldID _level;

            void onLoad(JNIEnv* env) {
                jclass cls = env->FindClass("org/jetbrains/skija/shaper/BidiRun");
                _end = env->GetFieldID(cls, "_end", "I");
                _level = env->GetFieldID(cls, "_level", "I");
            }
        }

        namespace FontMgrRunIterator {
            jclass cls;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/shaper/FontMgrRunIterator");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }

        namespace FontRun {
            jfieldID _end;
            jmethodID _getFontPtr;

            void onLoad(JNIEnv* env) {
                jclass cls = env->FindClass("org/jetbrains/skija/shaper/FontRun");
                _end = env->GetFieldID(cls, "_end", "I");
                _getFontPtr = env->GetMethodID(cls, "_getFontPtr", "()J");
            }
        }

        namespace HbIcuScriptRunIterator {
            jclass cls;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/shaper/HbIcuScriptRunIterator");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));            
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }

        }

        namespace IcuBidiRunIterator {
            jclass cls;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/shaper/IcuBidiRunIterator");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));            
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }

        }

        namespace LanguageRun {
            jfieldID _end;
            jfieldID _language;

            void onLoad(JNIEnv* env) {
                jclass cls = env->FindClass("org/jetbrains/skija/shaper/LanguageRun");
                _end = env->GetFieldID(cls, "_end", "I");
                _language = env->GetFieldID(cls, "_language", "Ljava/lang/String;");
            }
        }

        namespace RunHandler {
            jclass cls;
            jmethodID beginLine;
            jmethodID runInfo;
            jmethodID commitRunInfo;
            jmethodID runOffset;
            jmethodID commitRun;
            jmethodID commitLine;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/shaper/RunHandler");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
                beginLine       = env->GetMethodID(cls, "beginLine",     "()V");
                runInfo         = env->GetMethodID(cls, "runInfo",       "(Lorg/jetbrains/skija/shaper/RunInfo;)V");
                commitRunInfo   = env->GetMethodID(cls, "commitRunInfo", "()V");
                runOffset       = env->GetMethodID(cls, "runOffset",     "(Lorg/jetbrains/skija/shaper/RunInfo;)Lorg/jetbrains/skija/Point;");
                commitRun       = env->GetMethodID(cls, "commitRun",     "(Lorg/jetbrains/skija/shaper/RunInfo;[S[Lorg/jetbrains/skija/Point;[I)V");
                commitLine      = env->GetMethodID(cls, "commitLine",    "()V");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }

        namespace RunInfo {
            jclass cls;
            jmethodID ctor;
            jfieldID _fontPtr;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/shaper/RunInfo");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
                ctor = env->GetMethodID(cls, "<init>", "(JIFFJII)V");
                _fontPtr = env->GetFieldID(cls, "_fontPtr", "J");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }

            jobject toJava(JNIEnv* env, const SkShaper::RunHandler::RunInfo& info, size_t begin, size_t end) {
                SkFont* font = new SkFont(info.fFont);
                return env->NewObject(
                    cls, 
                    ctor,
                    reinterpret_cast<jlong>(font),
                    info.fBidiLevel,
                    info.fAdvance.fX,
                    info.fAdvance.fY,
                    info.glyphCount,
                    begin,
                    end - begin);
            }

            jobject toJava(JNIEnv* env, const SkShaper::RunHandler::RunInfo& info, skija::UtfIndicesConverter& indicesConverter) {
                size_t begin = indicesConverter.from8To16(info.utf8Range.fBegin);
                size_t end = indicesConverter.from8To16(info.utf8Range.fBegin + info.utf8Range.fSize);
                return toJava(env, info, begin, end);
            }
        }

        namespace ScriptRun {
            jfieldID _end;
            jfieldID _scriptTag;

            void onLoad(JNIEnv* env) {
                jclass cls = env->FindClass("org/jetbrains/skija/shaper/ScriptRun");
                _end = env->GetFieldID(cls, "_end", "I");
                _scriptTag = env->GetFieldID(cls, "_scriptTag", "I");
            }
        }

        namespace TextBlobBuilderRunHandler {
            jclass cls;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/shaper/TextBlobBuilderRunHandler");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
       }
       
       void onLoad(JNIEnv* env) {
            BidiRun::onLoad(env);
            FontMgrRunIterator::onLoad(env);
            FontRun::onLoad(env);
            HbIcuScriptRunIterator::onLoad(env);
            IcuBidiRunIterator::onLoad(env);
            LanguageRun::onLoad(env);
            RunHandler::onLoad(env);
            RunInfo::onLoad(env);
            ScriptRun::onLoad(env);
            TextBlobBuilderRunHandler::onLoad(env);
        }

        void onUnload(JNIEnv* env) {
            TextBlobBuilderRunHandler::onUnload(env);
            RunInfo::onUnload(env);
            RunHandler::onUnload(env);
            IcuBidiRunIterator::onUnload(env);
            HbIcuScriptRunIterator::onUnload(env);
            FontMgrRunIterator::onUnload(env);
        }
   }
}
#pragma once
#include <jni.h>

namespace skija {
    namespace paragraph {
        namespace LineMetrics {
            extern jclass cls;
            extern jmethodID ctor;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        namespace TextBox {
            extern jclass cls;
            extern jmethodID ctor;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        namespace DecorationStyle {
            extern jclass cls;
            extern jmethodID ctor;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);   
        }

        namespace Shadow {
            extern jclass cls;
            extern jmethodID ctor;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);   
        }

        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }
}
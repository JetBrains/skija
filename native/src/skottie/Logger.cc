#include <jni.h>
#include "../interop.hh"
#include "Skottie.h"

using namespace skottie;

class SkijaLoggerImpl: public Logger {
public:
    SkijaLoggerImpl() {
    }

    ~SkijaLoggerImpl() {
        fEnv->DeleteWeakGlobalRef(fObject);
    }

    void init(JNIEnv* e, jobject o) {
        fEnv = e;
        fObject = fEnv->NewWeakGlobalRef(o);
    }

public:
    void log(Level level, const char message[], const char* json = nullptr) override {
        jclass loggerLevelClass = fEnv->FindClass("org/jetbrains/skija/skottie/Logger$Level");
        jmethodID valuesMethod = fEnv->GetStaticMethodID(loggerLevelClass, "values", "()[Lorg/jetbrains/skija/skottie/Logger$Level;");
        jobjectArray loggerLevelValues = static_cast<jobjectArray>(fEnv->CallStaticObjectMethod(loggerLevelClass, valuesMethod));

        int ordinal = static_cast<int>(level);
        jobject javaLoggerLevel = static_cast<jobject>(fEnv->GetObjectArrayElement(loggerLevelValues, ordinal));
        jstring javaStringMessage = javaString(fEnv, message);
        jstring javaStringJson = nullptr;
        if (json) {
            javaStringJson = javaString(fEnv, json);
        }
        fEnv->CallVoidMethod(fObject, skija::skottie::Logger::log, javaLoggerLevel, javaStringMessage, javaStringJson);
    }

private:
    JNIEnv* fEnv;
    jobject fObject;
};

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_skottie_Logger__1nMake
  (JNIEnv* env, jclass jclass) {
    SkijaLoggerImpl* instance = new SkijaLoggerImpl();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_skottie_Logger__1nInit
  (JNIEnv* env, jobject jthis, jlong ptr) {
    SkijaLoggerImpl* instance = reinterpret_cast<SkijaLoggerImpl*>(static_cast<uintptr_t>(ptr));
    instance->init(env, jthis);
}

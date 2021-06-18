#include <iostream>
#include <jni.h>
#include "interop.hh"
#include "SkStream.h"

class OutputWStream: public SkWStream {
public:
    OutputWStream(JNIEnv* env, jobject out): fEnv(env) {
        fOut = env->NewGlobalRef(out);
    }

    ~OutputWStream() override {
        fEnv->DeleteGlobalRef(fOut);
    }

    size_t bytesWritten() const override {
        return fBytesWritten;
    }

    bool write(const void* buffer, size_t size) override  {
        jbyteArray arr = fEnv->NewByteArray((jsize) size);
        fEnv->SetByteArrayRegion(arr, 0, (jsize) size, static_cast<const jbyte *>(buffer));
        fEnv->CallVoidMethod(fOut, java::io::OutputStream::write, arr, 0, (jint) size);
        fEnv->DeleteLocalRef(arr);
        if (java::lang::Throwable::exceptionThrown(fEnv)) {
            return false;
        } else {
            fBytesWritten += size;
            return true;
        }
    }

    void flush() override  {
        fEnv->CallVoidMethod(fOut, java::io::OutputStream::flush);
    }

private:
    JNIEnv* fEnv;
    jobject fOut;
    size_t fBytesWritten = 0;
};

static void deleteOutputWStream(OutputWStream* out) {
    delete out;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_OutputWStream__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteOutputWStream));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_OutputWStream__1nMake
  (JNIEnv* env, jclass jclass, jobject outputStream) {
    OutputWStream* instance = new OutputWStream(env, outputStream);
    return reinterpret_cast<jlong>(instance);
}

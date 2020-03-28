#pragma once

#include <jni.h>
#include "SkRefCnt.h"

template <typename T> static jlong sk_sp_ref(sk_sp<T> sp) {
    T* p = sp.get();
    p->ref(); // bump ref counter
    return reinterpret_cast<jlong>(p);
}

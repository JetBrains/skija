#include <iostream>
#include <jni.h>
#include "include/docs/SkPDFDocument.h"
#include "SkDocument.h"
#include "SkCanvas.h"
#include "SkStream.h"
#include "SkRect.h"

/*static void deleteDocument(SkDocument* pr) {
    // std::cout << "Deleting [SkPictureRecorder " << PictureRecorder << "]" << std::endl;
    delete pr;
}*/

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_pdf_Document__1nMake(JNIEnv* env, jclass jclass) {
    SkPDF::Metadata metadata;
    metadata.fTitle = "documentTitle";
    metadata.fCreator = "Example WritePDF() Function";
    metadata.fCreation = {0, 2019, 1, 4, 31, 12, 34, 56};
    metadata.fModified = {0, 2019, 1, 4, 31, 12, 34, 56};
    SkFILEWStream out("~/Downloads/1.pdf");
    //SkFont* obj = new SkFont(sk_sp<SkTypeface>(SkRef(typeface)));
    sk_sp<SkDocument> pdfDocument = SkPDF::MakeDocument(&out, metadata);
    return reinterpret_cast<jlong>(&pdfDocument);
}

/*extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_pdf_Document__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteDocument));
}*/

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_pdf_Document__1nBeginPage
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat width, jfloat height, jfloat left, jfloat top, jfloat right, jfloat bottom) {
    SkDocument* instance = reinterpret_cast<SkDocument*>(static_cast<uintptr_t>(ptr));
    SkRect contentRect = SkRect::MakeLTRB(left, top, right, bottom);
    SkCanvas* canvas = instance->beginPage(width, height, &contentRect);
    return reinterpret_cast<jlong>(canvas);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_pdf_Document__1nEndPage(JNIEnv* env, jclass jclass, jlong ptr) {
    SkDocument* instance = reinterpret_cast<SkDocument*>(static_cast<uintptr_t>(ptr));
    instance->endPage();
}
extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_pdf_Document__1nClosePage(JNIEnv* env, jclass jclass, jlong ptr) {
    SkDocument* instance = reinterpret_cast<SkDocument*>(static_cast<uintptr_t>(ptr));
    instance->close();
}


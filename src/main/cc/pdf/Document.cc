#include <iostream>
#include <jni.h>
#include "include/docs/SkPDFDocument.h"
#include "SkCanvas.h"
#include "SkStream.h"
#include "SkRect.h"
#include "SkPictureRecorder.h"

struct PageRecord {
    PageRecord(int width, int height, const SkRect& contentRect)
            : mPictureRecorder(new SkPictureRecorder())
            , mPicture(NULL)
            , mWidth(width)
            , mHeight(height) {
        mContentRect = contentRect;
    }
    ~PageRecord() {
        delete mPictureRecorder;
        if (NULL != mPicture) {
            mPicture->unref();
        }
    }
    SkPictureRecorder* mPictureRecorder;
    SkPicture* mPicture;
    const int mWidth;
    const int mHeight;
    SkRect mContentRect;
};

class PdfDocument {
public:
    PdfDocument() {
        mCurrentPage = NULL;
    }
    SkCanvas* startPage(int width, int height,
            int left, int top, int right, int bottom) {
        assert(mCurrentPage == NULL);
        SkRect contentRect = SkRect::MakeLTRB(
                left, top, right, bottom);
        PageRecord* page = new PageRecord(width, height, contentRect);
        mPages.push_back(page);
        mCurrentPage = page;
        SkCanvas* canvas = page->mPictureRecorder->beginRecording(
                SkRect::MakeWH(contentRect.width(), contentRect.height()));
        return canvas;
    }
    void finishPage() {
        assert(mCurrentPage != NULL);
        assert(mCurrentPage->mPictureRecorder != NULL);
        assert(mCurrentPage->mPicture == NULL);
        mCurrentPage->mPicture = mCurrentPage->mPictureRecorder->finishRecordingAsPicture().release();
        delete mCurrentPage->mPictureRecorder;
        mCurrentPage->mPictureRecorder = NULL;
        mCurrentPage = NULL;
    }
    void write(SkWStream* stream) {
        sk_sp<SkDocument> document = SkPDF::MakeDocument(stream);
        for (unsigned i = 0; i < mPages.size(); i++) {
            PageRecord* page =  mPages[i];
            SkCanvas* canvas = document->beginPage(page->mWidth, page->mHeight,
                    &(page->mContentRect));
            canvas->drawPicture(page->mPicture);
            document->endPage();
        }
        document->close();
    }
    void close() {
        assert(NULL == mCurrentPage);
        for (unsigned i = 0; i < mPages.size(); i++) {
            delete mPages[i];
        }
    }
private:
    ~PdfDocument() {
        close();
    }
    std::vector<PageRecord*> mPages;
    PageRecord* mCurrentPage;
};

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_pdf_Document__1nMake(JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(new PdfDocument());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_pdf_Document__1nBeginPage
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat width, jfloat height, jfloat left, jfloat top, jfloat right, jfloat bottom) {
    PdfDocument* instance = reinterpret_cast<PdfDocument*>(static_cast<uintptr_t>(ptr));
    SkCanvas* canvas = instance->startPage(width, height,
                left, top, right, bottom);
    return reinterpret_cast<jlong>(canvas);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_pdf_Document__1nWrite(JNIEnv* env, jclass jclass, jlong ptr, jstring pathStr) {
    PdfDocument* document = reinterpret_cast<PdfDocument*>(ptr);
    const char* path = env->GetStringUTFChars(pathStr, nullptr);
    SkFILEWStream out(path);
    env->ReleaseStringUTFChars(pathStr, path);
    document->write(&out);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_pdf_Document__1nEndPage(JNIEnv* env, jclass jclass, jlong ptr) {
    PdfDocument* document = reinterpret_cast<PdfDocument*>(ptr);
    document->finishPage();
}
extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_pdf_Document__1nClose(JNIEnv* env, jclass jclass, jlong ptr) {
    PdfDocument* document = reinterpret_cast<PdfDocument*>(ptr);
    document->close();
}


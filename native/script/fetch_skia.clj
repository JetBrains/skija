#!/usr/bin/env bb

(def skia-release "m88-fc6759b235")

(def build-type "Release")

(def platform "windows")

(def arch "x64")

(def skia-dir (str "Skia-" skia-release "-" platform "-" build-type "-" arch))

(def working-dir (.getParent (.getParentFile (io/file *file*))))

(def zip (io/file (str working-dir "/" skia-dir ".zip")))

(def target (io/file (str working-dir "/" skia-dir)))

(defn unzip [src target]
  (println "Unpacking" (.getName src))
  (with-open [is  (io/input-stream src)
              zis (java.util.zip.ZipInputStream. is)]
    (loop [entry (.getNextEntry zis)]
      (when (some? entry)
        (let [file (io/file target (.getName entry))
              last-modified (.getTime entry)]
          (when (pos? last-modified)
            (.setLastModified file last-modified))
          (io/make-parents file)
          (if (.isDirectory entry)
            (.mkdir file)
            (io/copy zis file))
          (recur (.getNextEntry zis)))))))

(when-not (.exists target)
  (when-not (.exists zip)
    (let [url (str "https://github.com/JetBrains/skia-build/releases/download/" skia-release "/" skia-dir ".zip")]
      (println "Downloading" (.getName zip))
      (io/copy
        (:body @(org.httpkit.client/get url {:as :stream}))
        zip)))
  (unzip zip target)
  (.delete zip))

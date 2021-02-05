#! /usr/bin/env bb
(ns native.build
  (:require 
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def script-dir (.getParent (io/file *file*)))

(load-file (io/file script-dir ".." ".." "script" "util.clj"))

(util/set-working-dir! (.getParent (io/file script-dir)))

(def build-type
  (str/capitalize (name util/build-type)))

;; fetch-skia
(def skia-dir
  (if-some [dir (util/get-arg "--skia-dir")]
    (.getAbsolutePath (io/file dir))
    (let [dir (str "Skia-" util/skia-release "-" (name util/platform) "-" build-type "-" (name util/arch))]
      (when-not (.exists (util/file dir))
        (let [url (str "https://github.com/JetBrains/skia-build/releases/download/" util/skia-release "/" dir ".zip")
              zip (util/fetch url (str dir ".zip"))]
          (util/unzip zip dir)))
      (.getAbsolutePath (util/absolutize dir)))))

(println "Using Skia from" skia-dir)

;; cmake
(.mkdirs (util/file "build"))

(util/run! {:dir "build"}
  "cmake"
  "-G" "Ninja"
  (str "-DCMAKE_BUILD_TYPE=" build-type)
  "--config" build-type
  (str "-DSKIA_DIR=" skia-dir)
  (util/working-dir))

;; ninja
(util/run! {:dir "build"} "ninja")

;; icudtl.dat
(let [icudtl (io/file skia-dir "out" (str build-type "-" (name util/arch)) "icudtl.dat")]
  (when (.exists icudtl)
    (io/copy icudtl (util/file "build" "icudtl.dat"))))

nil
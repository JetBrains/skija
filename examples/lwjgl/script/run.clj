#! /usr/bin/env bb
(ns run
  (:require 
   [babashka.process :as ps]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [org.httpkit.client :as http]))

(def script-dir (.getParent (io/file *file*)))

(load-file (io/file script-dir ".." ".." ".." "script" "util.clj"))

(util/set-working-dir! (.getParent (io/file script-dir)))

;; compile
(def classifier
  (str "natives-" (name util/platform)))

(def classpath
  (->>
    [(io/file (util/working-dir) ".." ".." "native" "build")
     (io/file (util/working-dir) ".." ".." "shared" "target" "classes")
     ; (fetch-maven "org.jetbrains.skija" "skija-shared" "0.89.3" "https://packages.jetbrains.team/maven/p/skija/maven")
     ; (fetch-maven "org.jetbrains.skija" (str "skija-" (name util/platform)) "0.89.3" "https://packages.jetbrains.team/maven/p/skija/maven")
     (util/fetch-maven "org.projectlombok" "lombok" "1.18.12")
     (util/fetch-maven "com.google.code.gson" "gson" "2.8.6")
     (util/fetch-maven "org.lwjgl" "lwjgl" "3.2.3")
     (util/fetch-maven "org.lwjgl" "lwjgl-glfw" "3.2.3")
     (util/fetch-maven "org.lwjgl" "lwjgl-opengl" "3.2.3")
     (util/fetch-maven "org.lwjgl" "lwjgl" "3.2.3" {:classifier classifier})
     (util/fetch-maven "org.lwjgl" "lwjgl-glfw" "3.2.3" {:classifier classifier})
     (util/fetch-maven "org.lwjgl" "lwjgl-opengl" "3.2.3" {:classifier classifier})]
    (map #(.getPath (.getCanonicalFile %)))
    (str/join util/path-separator)))

(def sources
  (concat
    (util/glob "../scenes/src/**.java")
    (util/glob "src/**.java")))

(apply util/run! {} "javac" "-encoding" "UTF8" "--release" "11" "-cp" classpath "-d" "target/classes" sources)

;; run
(apply util/run! {}
  "java"
  "-cp" (str "target/classes" util/path-separator classpath)
  (when (= :macos util/platform) "-XstartOnFirstThread")
  "-Djava.awt.headless=true"
  "-ea"
  "-esa"
  "-Dskija.logLevel=DEBUG"
  "org.jetbrains.skija.examples.lwjgl.Main"
  *command-line-args*)

nil
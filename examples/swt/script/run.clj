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
  (str "natives-" (name util/os)))

(def classpath
  (->>
    [(io/file (util/working-dir) ".." ".." "native" "build")
     (io/file (util/working-dir) ".." ".." "shared" "target" "classes")
     ; (fetch-maven "org.jetbrains.skija" "skija-shared" "0.89.3" "https://packages.jetbrains.team/maven/p/skija/maven")
     ; (fetch-maven "org.jetbrains.skija"
     ;   (case os
     ;     :macos   "skija-macos"
     ;     :windows "skija-windows"
     ;     :linux   "skija-linux")
     ;   "0.89.3"
     ;   "https://packages.jetbrains.team/maven/p/skija/maven")
     (util/fetch-maven "org.eclipse.platform" 
       (case util/os
         :macos   "org.eclipse.swt.cocoa.macosx.x86_64"
         :windows "org.eclipse.swt.win32.win32.x86_64"
         :linux   "org.eclipse.swt.gtk.linux.x86_64")
       "3.115.100")]
    (map #(.getPath (.getCanonicalFile %)))
    (str/join util/path-separator)))

(def sources
  (util/glob "src/**.java"))

(apply util/run! {} "javac" "-encoding" "UTF8" "--release" "11" "-cp" classpath "-d" "target/classes" sources)

;; run
(apply util/run! {}
  "java"
  "-cp" (str "target/classes" util/path-separator classpath)
  (when (= :macos util/os) "-XstartOnFirstThread")
  "-Djava.awt.headless=true"
  "-ea"
  "-esa"
  "-Dskija.logLevel=DEBUG"
  "org.jetbrains.skija.examples.swt.Main"
  *command-line-args*)

nil
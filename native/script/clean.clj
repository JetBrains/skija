#! /usr/bin/env bb
(ns native.clean
  (:require 
   [clojure.java.io :as io]))

(def script-dir (.getParent (io/file *file*)))

(load-file (io/file script-dir ".." ".." "script" "util.clj"))

(util/set-working-dir! (.getParent (io/file script-dir)))

(util/delete-tree "build")

nil
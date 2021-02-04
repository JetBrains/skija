#! /usr/bin/env bb

(def script-dir (.getParent (io/file *file*)))

(load-file (io/file script-dir ".." ".." ".." "script" "util.clj"))

(util/set-working-dir! (.getParent (io/file script-dir)))

(util/delete-tree "target")

nil
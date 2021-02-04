(ns util
  (:require 
   [babashka.deps :as deps]
   [babashka.process :as ps]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [org.httpkit.client :as http]))

(deps/add-deps
  {:deps
   {'babashka/fs {:git/url "https://github.com/babashka/fs"
                  :sha "6f6fd64f8523080fa2195ed86f869a93dd443a16"}}})

(require '[babashka.fs :as fs])

(def home-dir (System/getProperty "user.home"))

(defn relative-dir [path & paths]
  (as-> (io/file path) %
    (.getCanonicalFile %)
    (.getParentFile %)
    (apply io/file % paths)
    (.getCanonicalFile %)
    (.getPath %)))

(def *working-dir (atom (System/getProperty "user.dir")))

(defn set-working-dir! [dir]
  (reset! *working-dir dir))

(defn working-dir []
  @*working-dir)

(def path-separator (System/getProperty "path.separator"))

(def os
  (condp re-find (str/lower-case (System/getProperty "os.name"))
    #"(mac|darwin)" :macos
    #"windows"      :windows
    #"(unix|linux)" :linux))

(defn run! [opts & cmd]
  (let [cmd' (keep identity cmd)
        {:keys [dir out err env echo]
         :or {dir "", out System/out, err System/err, env {}, echo false}} opts
        dir'      (.getPath (io/file @*working-dir dir))
        env-set   (into {} (for [[k v] env :when (some? v)] [k v]))
        env-unset (into #{} (for [[k v] env :when (nil? v)] k))
        env'      (as-> (into {} (System/getenv)) %
                    (reduce dissoc % env-unset)
                    (merge % env-set))
        _         (when echo
                    (println ">" (str/join " " cmd')))
        res       @(ps/process cmd' {:dir dir', :out out, :err err, :env env'})]
    (when (not= 0 (:exit res))
      (throw (ex-info (str "Failed with exit code " (:exit res) ": " (str/join " " cmd')) (assoc res :type ::error))))
    res))

(defn fetch-maven
  ([group name version]
   (fetch-maven group name version {}))
  ([group name version {:keys [repo classifier]
                        :or {repo "https://repo1.maven.org/maven2"}}]
   (let [path (str (str/replace group "." "/") "/" name "/" version "/" (str name "-" version (when classifier (str "-" classifier)) ".jar"))
         file (io/file home-dir ".m2" "repository" path)
         url  (str repo "/" path)]
     (when-not (.exists file)
       (println "Downloading" (.getName file))
       (.mkdirs (.getParentFile file))
       (io/copy (:body @(http/get url {:as :stream})) file))
     file)))

(defn glob [pattern]
  (let [[path mask] (split-with #(re-matches #"[^*?]+" %) (str/split pattern #"/"))
        dir   (apply fs/path @*working-dir path)
        found (fs/glob dir (str/join "/" mask))]
    (mapv #(str (fs/relativize @*working-dir %)) found)))

(defn delete-tree [root]
  (run! {} "rm" "-rf" root))
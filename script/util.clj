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

(defn get-arg [arg]
  (->> (drop-while #(not= arg %) *command-line-args*)
    fnext))

(defn has-arg? [arg]
  (some #(= arg %) *command-line-args*))

(def *working-dir (atom (System/getProperty "user.dir")))

(defn set-working-dir! [dir]
  (reset! *working-dir dir))

(defn working-dir []
  @*working-dir)

(defn file [& paths]
  (apply io/file @*working-dir paths))

(def path-separator (System/getProperty "path.separator"))

(def platform
  (condp re-find (str/lower-case (System/getProperty "os.name"))
    #"(mac|darwin)" :macos
    #"windows"      :windows
    #"(unix|linux)" :linux))

(def arch
  (condp = (System/getProperty "os.arch")
    "aarch64" :arm64
    :x64))

(def skia-release
  "m89-15595ea39c")

(def build-type
  (cond
    (has-arg? "--debug") :debug
    true :release))

(defn absolutize [file-rel]
  (let [file (io/file file-rel)]
    (if (.isAbsolute file)
      file
      (.getCanonicalFile (io/file @*working-dir file-rel)))))

(def default-env
  (into {} (System/getenv)))

(defn run! [opts & cmd]
  (let [cmd' (keep identity cmd)
        {:keys [dir out err env]
         :or {dir "", out System/out, err System/err, env {}}} opts
        dir'      (.getPath (io/file @*working-dir dir))
        env-set   (into {} (for [[k v] env :when (some? v)] [k v]))
        env-unset (into #{} (for [[k v] env :when (nil? v)] k))
        env'      (as-> default-env %
                    (reduce dissoc % env-unset)
                    (merge % env-set))
        _         (when (has-arg? "--verbose")
                    (println ">" (str/join " " cmd')))
        res       @(ps/process cmd' {:dir dir', :out out, :err err, :env env'})]
    (when (not= 0 (:exit res))
      (throw (ex-info (str "Failed with exit code " (:exit res) ": " (str/join " " cmd')) (assoc res :type ::error))))
    res))

(defn fetch [url target-rel]
  (let [target (absolutize target-rel)]
    (when-not (.exists target)
      (println "Downloading" (.getName target))
      (.mkdirs (.getParentFile target))
      (io/copy
        (:body @(http/get url {:as :stream}))
        target))
    target))

(defn fetch-maven
  ([group name version]
   (fetch-maven group name version {}))
  ([group name version {:keys [repo classifier]
                        :or {repo "https://repo1.maven.org/maven2"}}]
   (let [path (str (str/replace group "." "/") "/" name "/" version "/" (str name "-" version (when classifier (str "-" classifier)) ".jar"))
         file (io/file home-dir ".m2" "repository" path)]
     (fetch (str repo "/" path) file))))

(defn unzip [zip-rel target-rel]
  (let [zip    (absolutize zip-rel)
        target (absolutize target-rel)]
    (println "Unpacking" (.getName zip))
    (with-open [is  (io/input-stream zip)
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
            (recur (.getNextEntry zis))))))
    (.delete zip)
    target))

(defn glob [pattern]
  (let [[path mask] (split-with #(re-matches #"[^*?]+" %) (str/split pattern #"/"))
        dir   (apply fs/path @*working-dir path)
        found (fs/glob dir (str/join "/" mask))]
    (mapv #(str (fs/relativize @*working-dir %)) found)))

(defn delete-tree [root]
  (run! {} "rm" "-rf" root))
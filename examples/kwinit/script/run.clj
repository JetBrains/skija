#! /usr/bin/env bb

(require '[babashka.process :as ps])

(def home (System/getProperty "user.home"))

(def working-dir (-> (io/file *file*) (.getCanonicalFile) (.getParentFile) (.getParent)))

(def os
  (condp re-find (str/lower-case (System/getProperty "os.name"))
    #"(mac|darwin)" :macos
    #"windows"      :windows
    #"(unix|linux)" :linux))

(defn run! [opts & cmd]
  (let [cmd' (keep identity cmd)
        {:keys [dir out err env]
         :or {dir "", out System/out, err :string, env {}}} opts
        dir' (.getPath (io/file working-dir dir))
        env' (merge (into {} (System/getenv)) env)
        _    (println (str/join " " cmd'))
        res  (ps/process cmd' {:dir dir', :out out, :err err, :env env'})]
    (if (identical? :string err)
      (ps/check res)
      @res)))

(defn fetch-maven
  ([group name version]
   (fetch-maven group name version {:repo "https://repo1.maven.org/maven2"}))
  ([group name version {:keys [repo classifier]}]
   (let [path (str (str/replace group "." "/") "/" name "/" version "/" (str name "-" version (when classifier (str "-" classifier)) ".jar"))
         file (io/file home ".m2" "repository" path)
         url  (str repo "/" path)]
     (when-not (.exists file)
       (println "Downloading" (.getName file))
       (.mkdirs (.getParentFile file))
       (io/copy (:body @(org.httpkit.client/get url {:as :stream})) file))
     file)))

(def classpath
  (->>
    [(io/file working-dir ".." ".." "native" "build")
     (io/file working-dir ".." ".." "shared" "target" "classes")
     ; (fetch-maven "org.jetbrains.skija" "skija-shared" "0.89.3" "https://packages.jetbrains.team/maven/p/skija/maven")
     ; (fetch-maven "org.jetbrains.skija"
     ;   (case os
     ;     :macos   "skija-macos"
     ;     :windows "skija-windows"
     ;     :linux   "skija-linux")
     ;   "0.89.3"
     ;   "https://packages.jetbrains.team/maven/p/skija/maven")
     (fetch-maven "com.google.code.gson" "gson" "2.8.6")
    ]
    (map #(.getPath (.getCanonicalFile %)))
    (str/join (System/getProperty "path.separator"))))

(def sources
  (->> (io/file working-dir "src_java")
    (file-seq)
    (filter #(str/ends-with? (.getName %) ".java"))
    (mapv #(.getPath %))))

;; build native
(run! {} "cargo" "build" "--release" "--lib")

;; compile
; (.mkdirs (io/file working-dir "target" "classes"))
(apply run! {} "javac" "-encoding" "UTF8" "--release" "11" "-cp" classpath "-d" "target/classes" sources)

;; run
(run!
  {:err System/err
   :env (cond-> {"RUST_BACKTRACE" "1"}
          (= :windows os) (assoc "KWINIT_ANGLE" "1"))}
  "java"
  "-cp" (str "target/classes" (System/getProperty "path.separator") classpath)
  (when (= :macos os) "-XstartOnFirstThread")
  "-Djava.awt.headless=true"
  "-ea"
  "-esa"
  "-Dskija.logLevel=DEBUG"
  "noria.kwinit.impl.Main")

'DONE
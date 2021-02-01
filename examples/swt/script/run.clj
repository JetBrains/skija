#! /usr/bin/env bb

(require '[babashka.process :as ps])

(def home (System/getProperty "user.home"))

(def working-dir (-> (io/file *file*) (.getCanonicalFile) (.getParentFile) (.getParent)))

(def os
  (condp re-find (str/lower-case (System/getProperty "os.name"))
    #"(mac|darwin)" :macos
    #"windows"      :windows
    #"(unix|linux)" :linux))

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
     (fetch-maven "org.eclipse.platform" 
       (case os
         :macos   "org.eclipse.swt.cocoa.macosx.x86_64"
         :windows "org.eclipse.swt.win32.win32.x86_64"
         :linux   "org.eclipse.swt.gtk.linux.x86_64")
         "3.115.100")]
    (map #(.getPath (.getCanonicalFile %)))
    (str/join (System/getProperty "path.separator"))))

(def sources
  (->> (io/file working-dir "src")
    (file-seq)
    (filter #(str/ends-with? (.getName %) ".java"))
    (mapv #(.getPath %))))

;; compile
(->
  (ps/process
    (concat
      ["javac" 
       "-encoding" "UTF8"
       "--release" "11"
       "-cp" classpath
       "-d" "target/classes"]
      sources)
    {:dir working-dir})
  (ps/check))

;; run
@(ps/process
   (concat
     ["java" "-cp" (str "target/classes" (System/getProperty "path.separator") classpath)]
     (when (= :macos os)
      ["-XstartOnFirstThread"])
     #_["-Djava.awt.headless=true"
      "-ea"
      "-esa"
      "-Dskija.logLevel=DEBUG"]
     ["org.jetbrains.skija.examples.swt.Main"])
   {:dir working-dir
    :out *out*
    :err *out*})
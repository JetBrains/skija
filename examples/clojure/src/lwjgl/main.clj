(ns lwjgl.main
  (:require
   [nrepl.server :as nrepl])
  (:import
   [org.jetbrains.skija BackendRenderTarget Canvas ColorSpace DirectContext FramebufferFormat Paint Rect Surface SurfaceColorFormat SurfaceOrigin]
   [org.lwjgl.glfw Callbacks GLFW GLFWErrorCallback]
   [org.lwjgl.opengl GL GL11]
   [org.lwjgl.system MemoryUtil]))

(set! *warn-on-reflection* true)

(defn color [^long l]
  (.intValue (Long/valueOf l)))

(def *rect-color (atom (color 0xFFCC3333)))

(defn draw [^Canvas canvas]
  (let [paint (doto (Paint.) (.setColor @*rect-color))]
    (.translate canvas 320 240)
    (.rotate canvas (mod (/ (System/currentTimeMillis) 10) 360))
    (.drawRect canvas (Rect/makeXYWH -50 -50 100 100) paint)))

(defn -main [& args]
  (.set (GLFWErrorCallback/createPrint System/err))
  (GLFW/glfwInit)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)
  (let [width 640
        height 480
        window (GLFW/glfwCreateWindow width height "Skija LWJGL Demo" MemoryUtil/NULL MemoryUtil/NULL)]
    (GLFW/glfwMakeContextCurrent window)
    (GLFW/glfwSwapInterval 1)
    (GLFW/glfwShowWindow window)  
    (GL/createCapabilities)

    (doto (Thread. #(clojure.main/main))
      (.start))

    (nrepl/start-server :port 7888)
    (println "nREPL server started at locahost:7888")

    (let [context (DirectContext/makeGL)
          fb-id   (GL11/glGetInteger 0x8CA6)
          target  (BackendRenderTarget/makeGL width height 0 8 fb-id FramebufferFormat/GR_GL_RGBA8)
          surface (Surface/makeFromBackendRenderTarget context target SurfaceOrigin/BOTTOM_LEFT SurfaceColorFormat/RGBA_8888 (ColorSpace/getSRGB))
          canvas  (.getCanvas surface)]
      (loop []
        (when (not (GLFW/glfwWindowShouldClose window))
          (.clear canvas (color 0xFFFFFFFF))
          (let [layer (.save canvas)]
            (draw canvas)
            (.restoreToCount canvas layer))
          (.flush context)
          (GLFW/glfwSwapBuffers window)
          (GLFW/glfwPollEvents)
          (recur)))

      (Callbacks/glfwFreeCallbacks window)
      (GLFW/glfwHideWindow window)
      (GLFW/glfwDestroyWindow window)
      (GLFW/glfwPollEvents)
      (GLFW/glfwTerminate)
      (.free (GLFW/glfwSetErrorCallback nil))
      (shutdown-agents)
      )))

(comment
  (reset! lwjgl.main/*rect-color (lwjgl.main/color 0xFF33CC33)))
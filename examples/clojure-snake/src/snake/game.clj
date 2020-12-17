(ns snake.game
  (:require
   [clojure.stacktrace :as stacktrace])
  (:import
   [org.jetbrains.skija Canvas Color4f Font FontStyle FontMgr Paint PaintMode PaintStrokeCap Rect Typeface]))

(def *broken (atom false))

(defn new-state []
  (let [width 30
        height 22]
    {:width     width
     :height    height
     :cell      20
     :tick      100
     :dead?     false
     :dir       [0 0]
     :snake     [[(/ width 2) (/ height 2)]
                 [(/ width 2) (+ (/ height 2) 1)]
                 [(/ width 2) (+ (/ height 2) 2)]
                 [(/ width 2) (+ (/ height 2) 3)]]
     :rabbits   (set (repeatedly 10
                       #(vector (rand-int width) (rand-int height))))
     :last-tick (System/currentTimeMillis)
     :colors    (vec (repeatedly (* width height)
                       #(Color4f.
                          (+ 0.51 (- (* 0.1 (rand)) 0.05))
                          (+ 0.89 (- (* 0.1 (rand)) 0.05))
                          (+ 0.47  (- (* 0.1 (rand)) 0.05)))))}))

(defonce *state (atom (new-state)))

(let [mgr (FontMgr/getDefault)
      emoji-face (.matchFamilyStyleCharacter mgr nil FontStyle/NORMAL nil (.codePointAt "🐰" 0))
      _ (def emoji-font (Font. emoji-face (float 24)))
      text-face-bold (.matchFamiliesStyle mgr (into-array String [".SF NS", "Helvetica Neue", "Arial"]) FontStyle/BOLD)
      _ (def text-font-100 (Font. text-face-bold (float 100)))
      ; text-face (.matchFamiliesStyle mgr (into-array String [".SF NS", "Helvetica Neue", "Arial"]) FontStyle/NORMAL)
      _ (def text-font-24 (Font. text-face-bold (float 24)))])

(defn color [^long l]
  (.intValue (Long/valueOf l)))

(defn in? [x xs]
  (some #(when (= % x) true) xs))

(defn getx [[x _]] x)
(defn gety [[_ y]] y)

(defn subtract-points [a b]
  [(condp = [(getx a) (getx b)]
     [0 (dec (:width @*state))] 1
     [(dec (:width @*state)) 0] -1
     (- (getx a) (getx b)))
   (condp = [(gety a) (gety b)]
     [0 (dec (:height @*state))] 1
     [(dec (:height @*state)) 0] -1
     (- (gety a) (gety b)))])

(defn on-tick [state now]
  (let [{:keys [dir snake rabbits width height]} state
        [x y]   (first snake)
        [dx dy] dir
        x'      (mod (+ x dx width) width)
        y'      (mod (+ y dy height) height)
        head'   [x' y']
        ate?    (contains? rabbits head')
        tail'   (if ate? snake (butlast snake))
        dead?   (in? head' tail')
        snake'  (vec (cons head' tail'))]
    (assoc state
      :snake snake'
      :dead? dead?
      :rabbits (disj rabbits head')
      :last-tick now)))

(defn draw-impl [^Canvas canvas window-width window-height]
  (let [{:keys [colors cell dead? dir snake rabbits width height tick last-tick] :as state} @*state
        won? (empty? rabbits)
        now (System/currentTimeMillis)]
    
    ;; tick
    (when (and 
            (not= [0 0] dir)
            (not dead?)
            (not won?)
            (> (- now last-tick) tick))
      (swap! *state on-tick now))

    (.clear canvas (color 0xFFFFFFFF))

    ;; draw grass
    (with-open [fill (doto (Paint.) (.setColor (color 0xFF33CC33)))]
      (.translate canvas
        (/ (- window-width (* cell width)) 2)
        (/ (- window-height (* cell height)) 2))
      (doseq [x (range 0 width)
              y (range 0 height)]
        (.setColor4f fill (nth (:colors state) (+ (* y width) x)))
        (.drawRect canvas (Rect/makeXYWH (* x cell) (* y cell) cell cell) fill)))

    ;; draw rabbits
    (with-open [fill (Paint.)]
      (let [bounds (.measureText emoji-font "🐰")
            dx     (-> (- (.getLeft bounds))
                     (- (/ (- (.getWidth bounds) (:cell @*state)) 2)))
            dy     (-> (- (.getTop bounds))
                     (- (/ (- (.getHeight bounds) (:cell @*state)) 2)))]
        (doseq [[x y] rabbits]
          ; (.drawRect canvas (.translate bounds (+ dx (* x cell)) (+ dy (* y cell))) fill)
          (.drawString canvas "🐰" (+ dx (* x cell)) (+ dy (* y cell)) emoji-font fill))))

    ;; draw snake
    (with-open [stroke (doto (Paint.)
                         (.setColor (color 0xFF333333))
                         (.setMode (PaintMode/STROKE))
                         (.setStrokeCap (PaintStrokeCap/ROUND))
                         (.setStrokeWidth (/ cell 4)))]
      ;; draw head
      (let [[x y :as head] (first snake)
            neck (second snake)]
        (.save canvas)
        (.translate canvas (* (+ 0.5 x) cell) (* (+ 0.5 y) cell))
        (condp = (subtract-points neck head)
          [1 0]  (.rotate canvas 270)
          [-1 0] (.rotate canvas 90)
          [0 1]  :noop
          [0 -1] (.rotate canvas 180))
        (.drawLine canvas 0 0 (/ cell 3) (- (/ cell 3)) stroke)
        (.drawLine canvas 0 0 (- (/ cell 3)) (- (/ cell 3)) stroke)
        (.drawLine canvas 0 (/ cell 2) 0 0 stroke)
        (.restore canvas))
      ;; draw body
      (doseq [[before current after] (partition 3 1 snake)
              :let [from (condp = (subtract-points current before)
                           [ 1  0] [0 (/ cell 2)]
                           [-1  0] [cell (/ cell 2)]
                           [ 0  1] [(/ cell 2) 0]
                           [ 0 -1] [(/ cell 2) cell])
                    to   (condp = (subtract-points after current)
                           [ 1  0] [cell (/ cell 2)]
                           [-1  0] [0 (/ cell 2)]
                           [ 0  1] [(/ cell 2) cell]
                           [ 0 -1] [(/ cell 2) 0])]]
        (.save canvas)
        (.translate canvas (* (getx current) cell) (* (gety current) cell))
        (.drawLine canvas (getx from) (gety from) (getx to) (gety to) stroke)
        (.restore canvas))
      ;; draw head
      (let [[before tail] (take-last 2 snake)]
        (.save canvas)
        (.translate canvas (* (getx tail) cell) (* (gety tail) cell))
        (condp = (subtract-points tail before)
          [ 1  0] (.drawLine canvas (float (/ cell 2)) (float (/ cell 2)) (float 0)          (float (/ cell 2)) stroke)
          [-1  0] (.drawLine canvas (float (/ cell 2)) (float (/ cell 2)) (float cell)       (float (/ cell 2)) stroke)
          [0   1] (.drawLine canvas (float (/ cell 2)) (float (/ cell 2)) (float (/ cell 2)) (float 0) stroke)
          [0  -1] (.drawLine canvas (float (/ cell 2)) (float (/ cell 2)) (float (/ cell 2)) (float cell) stroke))
        (.restore canvas)))

    ;; Game over
    (when dead?
      (with-open [fill (doto (Paint.) (.setColor (color 0xFFCC3333)))]
        (let [text "YOU DIED"
              bounds (.measureText text-font-100 text)]
          (.drawString canvas text
            (->
              (- (* width cell) (.getWidth bounds))
              (/ 2)
              (- (.getLeft bounds)))
            (->
              (- (* height cell) (.getHeight bounds))
              (/ 2)
              (- (.getTop bounds)))
            text-font-100
            fill)
          (let [text "Press R to restart"
                dy (.getHeight bounds)
                bounds (.measureText text-font-24 text)]
            (.setColor fill (color 0xFF333333))
            (.drawString canvas text
              (->
                (- (* width cell) (.getWidth bounds))
                (/ 2)
                (- (.getLeft bounds)))
              (->
                (- (* height cell) (.getHeight bounds))
                (/ 2)
                (- (.getTop bounds))
                (+ dy))
              text-font-24
              fill)))))

    ;; Game won
    (when won?
      (with-open [fill (doto (Paint.) (.setColor (color 0xFF3333CC)))]
        (let [text "YOU WON"
              bounds (.measureText text-font-100 text)]
          (.drawString canvas text
            (->
              (- (* width cell) (.getWidth bounds))
              (/ 2)
              (- (.getLeft bounds)))
            (->
              (- (* height cell) (.getHeight bounds))
              (/ 2)
              (- (.getTop bounds)))
            text-font-100
            fill))))
    ))

(defn draw [canvas window-width window-height]
  (try
    (when-not @*broken
      (draw-impl canvas window-width window-height))
    (catch Exception e
      (reset! *broken true)
      (stacktrace/print-stack-trace (stacktrace/root-cause e)))))

(defn on-key-pressed-impl [key]
  (let [{:keys [snake]} @*state
        head (first snake)
        neck (second snake)
        forbidden (subtract-points neck head)]
    (condp = key
      262 ;; right
      (when (not= forbidden [1 0])
        (swap! *state assoc :dir [1 0]))

      263 ;; left
      (when (not= forbidden [-1 0])
        (swap! *state assoc :dir [-1 0]))

      264 ;; bottom
      (when (not= forbidden [0 1])
        (swap! *state assoc :dir [0 1]))

      265 ;; up
      (when (not= forbidden [0 -1])
        (swap! *state assoc :dir [0 -1]))

      82 ;; R
      (reset! *state (new-state))

      ;; (println key)
      nil
      )))

(defn on-key-pressed [key]
  (try
    (when-not @*broken
      (on-key-pressed-impl key))
    (catch Exception e
      (reset! *broken true)
      (stacktrace/print-stack-trace (stacktrace/root-cause e)))))

(comment
  (do (reset! *state (new-state)) :done)
)
(ns doomsun.solarium.dashboard.sse
  (:require [doomsun.solarium.dashboard.http :as http]
            [doomsun.solarium.dashboard.html :as html]
            [doomsun.solarium.dashboard.views.overview :as overview]
            [doomsun.solarium.util :as util]
            [promesa.core :as p]))

(defonce ^:private clients (atom {}))

(defn- add-client! [channel ^js res]
  (http/respond-sse res)
  (swap! clients update channel (fnil conj #{}) res)
  (.on res "close"
       (fn []
         (swap! clients update channel disj res))))

(defn- broadcast! [channel event-name html-str]
  (let [data (-> html-str
                 (.replace (js/RegExp. "\n" "g") "\ndata: "))]
    (doseq [res (get @clients channel)]
      (try
        (.write res (str "event: " event-name "\ndata: " data "\n\n"))
        (catch :default _
          (swap! clients update channel disj res))))))

(defn handle-overview [config res]
  (add-client! :overview res))

(defn handle-documents [config res]
  (add-client! :documents res))

(defn start-polling!
  "Poll Qdrant every 60s and broadcast updates to SSE clients."
  [config]
  (js/setInterval
   (fn []
     (when (seq (get @clients :overview))
       (-> (p/let [fragment (overview/stats-fragment config)]
             (broadcast! :overview "morph" (html/render fragment)))
           (p/catch (fn [err]
                      (util/log "SSE poll error:" (.-message err)))))))
   60000))

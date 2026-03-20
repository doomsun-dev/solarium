(ns doomsun.solarium.dashboard.views.layout)

(def ^:private nav-items
  [{:path "/" :label "Overview" :key :overview}
   {:path "/documents" :label "Documents" :key :documents}
   {:path "/search" :label "Search" :key :search}
   {:path "/analytics" :label "Analytics" :key :analytics}])

(defn page [{:keys [title active-nav]} & body]
  [:html {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:title (str title " \u2014 Solarium")]
    [:link {:rel "preconnect" :href "https://fonts.googleapis.com"}]
    [:link {:rel "preconnect" :href "https://fonts.gstatic.com" :crossorigin true}]
    [:link {:rel "stylesheet"
            :href "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=JetBrains+Mono:wght@400;500;600&family=Playfair+Display:wght@600;700&display=swap"}]
    [:link {:rel "stylesheet" :href "/static/app.css"}]
    [:script {:src "https://cdn.jsdelivr.net/npm/idiomorph@0.3.0/dist/idiomorph.min.js"
              :defer true}]]
   [:body
    [:nav {:class "sidebar"}
     [:div {:class "sidebar-header"}
      [:a {:href "/"}
       [:img {:src "/static/logo.svg" :alt "Solarium" :class "sidebar-logo"}]]
      [:h1 "SOLARIUM"]
      [:div {:class "subtitle"} "Knowledge Base"]]
     (for [{:keys [path label key]} nav-items]
       [:a {:href path :class (when (= key active-nav) "active")}
        [:span label]])]
    [:main {:id "content"}
     body]
    [:script {:src "/static/app.js"}]]])

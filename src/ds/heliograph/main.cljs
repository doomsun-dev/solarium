(ns ds.heliograph.main
  (:require ["@modelcontextprotocol/sdk/server/mcp.js" :refer [McpServer]]
            ["@modelcontextprotocol/sdk/server/stdio.js" :refer [StdioServerTransport]]
            [ds.heliograph.config :as config]
            [ds.heliograph.server :as server]
            [ds.heliograph.qdrant.collections :as collections]
            [ds.heliograph.util :as util]
            [promesa.core :as p]))

(defn main! []
  (let [cfg       (config/load-config)
        mcp-server (McpServer. (clj->js {:name    "heliograph"
                                         :version "0.1.0"}))]
    (server/register-tools! mcp-server cfg)

    (p/let [_ (collections/ensure-collection! cfg)]
      (let [transport (StdioServerTransport.)]
        (p/let [_ (.connect mcp-server transport)]
          (util/log "Heliograph MCP server running on stdio."))))))

(ns doomsun.solarium.qdrant.client-test
  (:require [cljs.test :refer [deftest is testing async]]
            [doomsun.solarium.qdrant.client :as client]
            [promesa.core :as p]))

;; These tests require a running Qdrant instance.
;; Run: docker run -p 6333:6333 qdrant/qdrant

(def test-config
  {:qdrant-url      "http://localhost:6333"
   :qdrant-api-key  nil
   :collection-name "solarium_test"})

(deftest health-check
  (testing "Qdrant health endpoint responds"
    (async done
           (-> (p/let [resp (client/request test-config "GET" "/healthz")]
                 (is (some? resp)))
               (p/catch (fn [_err]
                          (is false "Qdrant not reachable — is it running?")))
               (p/finally done)))))

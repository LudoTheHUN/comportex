(defproject org.nfrac/comportex "0.0.8-SNAPSHOT"
  :description "Functionally composable cortex, an implementation of Hierarchical Temporal Memory"
  :url "http://github.com/nupic-community/comportex/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/cljx"]
  :test-paths ["target/test-classes"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.cemerick/pprng "0.0.2"]
                 [clj-http "1.0.1"]
                 [cljs-http "0.1.19"]
                 [cljs-uuid "0.0.4"]]

  :jar-exclusions [#"\.cljx"]
  :jvm-opts ^:replace ["-server" "-Xmx2500m"]

  :cljx {:builds [{:source-paths ["src/cljx"]
                   :output-path "target/classes"
                   :rules :clj}

                  {:source-paths ["src/cljx"]
                   :output-path "target/classes"
                   :rules :cljs}

                  {:source-paths ["test/cljx"]
                   :output-path "target/test-classes"
                   :rules :clj}

                  {:source-paths ["test/cljx"]
                   :output-path "target/test-classes"
                   :rules :cljs}]}

  :cljsbuild {:builds [{:source-paths ["target/classes"
                                       "target/test-classes"]
                        :jar true
                        :compiler {:output-to "target/testable.js"
                                   :optimizations :advanced}}]}

  :profiles {:dev {:dependencies [[org.clojure/clojurescript "0.0-2234"]
                                  [criterium "0.4.3"]]
                   :plugins [[com.keminglabs/cljx "0.5.0"]
                             [lein-cljsbuild "1.0.3"]
                             [com.cemerick/clojurescript.test "0.3.1"]
                             [com.cemerick/austin "0.1.4"]
                             [lein-marginalia "0.8.0"]]

                   :auto-clean false

                   :aliases {"install" ["do" "clean,"
                                        "cljx" "once,"
                                        "install"]}}
             :repl {:source-paths ["dev"]}})

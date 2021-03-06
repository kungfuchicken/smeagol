(defproject smeagol "1.0.2"
  :description "A simple Git-backed Wiki inspired by Gollum"
  :url "https://github.com/simon-brooke/smeagol"
  :license {:name "GNU General Public License,version 2.0 or (at your option) any later version"
            :url "https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html"}
  :dependencies [[clj-jgit "0.8.10"]
                 [clj-yaml "0.4.0"]
                 [com.cemerick/url "0.1.1"]
                 [com.fzakaria/slf4j-timbre "0.3.7"]
                 [com.stuartsierra/component "0.3.2"]
                 [com.taoensso/encore "2.92.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.taoensso/tower "3.0.2" :exclusions [com.taoensso/encore]]
                 [crypto-password "0.2.0"]
                 [environ "1.1.0"]
                 [hiccup "1.0.5"]
                 [im.chit/cronj "1.4.4"]
                 [lib-noir "0.9.9" :exclusions [org.clojure/tools.reader]]
                 [markdown-clj "0.9.99" :exclusions [com.keminglabs/cljx]]
                 [noir-exception "0.2.5"]
                 [org.clojars.simon_brooke/internationalisation "1.0.3"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/core.memoize "0.5.9"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.slf4j/slf4j-api "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [prismatic/schema "1.1.9"]
                 [prone "1.1.4"]
                 [ring/ring-anti-forgery "1.1.0"]
                 [ring-server "0.4.0"]
                 [selmer "1.11.0"]]

  :repl-options {:init-ns smeagol.repl}

  :jvm-opts ["-server"]

  :plugins [[lein-ancient "0.5.5" :exclusions [org.clojure/clojure org.clojure/data.xml]]
            [lein-bower "0.5.1"]
            [lein-codox "0.10.3"]
            [io.sarnowski/lein-docker "1.0.0"]
            [lein-environ "1.0.0"]
            [lein-marginalia "0.7.1" :exclusions [org.clojure/clojure]]
            [lein-ring "0.8.13" :exclusions [org.clojure/clojure]]]

  :bower-dependencies [[simplemde "1.11.2"]
                       ;; [vega-embed "3.0.0-beta.20"] ;; vega-embed currently not loaded from Bower because of
                       ;; dependency conflict which will hopefully be resolved soon.
                       [vega-lite "2.0.0-beta.11"]
                       [mermaid "6.0.0"]]

  :docker {:image-name "simonbrooke/smeagol"
           :dockerfile "Dockerfile"}

  :ring {:handler smeagol.handler/app
         :init    smeagol.handler/init
         :destroy smeagol.handler/destroy}

  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "v." "--no-sign"]
                  ["clean"]
                  ["bower" "install"]
                  ["ring" "uberjar"]
                  ["docker" "build"]
                  ["docker" "push"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]]

  :profiles {:uberjar {:omit-source true
                       :env {:production true}
                       :aot :all}
             :production {:ring {:open-browser? false
                                 :stacktraces?  false
                                 :auto-reload?  false}}
             :dev {:dependencies [[ring-mock "0.1.5"]
                                  [ring/ring-devel "1.6.2"]
                                  [pjstadig/humane-test-output "0.8.2"]]
                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]
                   :env {:dev true}}}

  :min-lein-version "2.0.0")

(defproject mitsubishi-il-simulator "0.1.0-SNAPSHOT"
  :description "Simulator for the Mitsubishi IL Instruction Set"
  :url "https://github.com/gregorybrooks/mitsubishi-il-simulator"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [instaparse "1.4.12"]
                 [cljfx "1.7.24"]
                 [org.openjfx/javafx-controls "17.0.2"]
                 [org.openjfx/javafx-fxml "17.0.2"]]
  :main ^:skip-aot mitsubishi-il-simulator.core
  :target-path "target/%s"
  :jvm-opts []
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})

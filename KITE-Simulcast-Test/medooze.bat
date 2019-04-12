@echo off
java -Dkite.firefox.profile=../third_party/ -cp "../KITE-Engine/target/kite-jar-with-dependencies.jar;../KITE-Extras/target/*;../KITE-Framework/target/*;../KITE-Engine/target/*;target/*" org.webrtc.kite.Engine configs/medooze.simulcast.config.json

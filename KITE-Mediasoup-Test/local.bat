@echo off
java -cp "../KITE-Engine/target/kite-jar-with-dependencies.jar;../KITE-Extras/target/*;../KITE-Framework/target/*;../KITE-Engine/target/*;target/*" org.webrtc.kite.Engine configs/local.mediasoup.config.json

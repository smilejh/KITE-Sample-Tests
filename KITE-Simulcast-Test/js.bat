@echo off
IF "%1"=="h264" (
	java -cp "../KITE-Engine/target/kite-jar-with-dependencies.jar;../KITE-Extras/target/*;../KITE-Framework/target/*;../KITE-Engine/target/*;target/*" org.webrtc.kite.Engine configs/js.h264.medooze.config.json
) else IF "%1"=="vp8" (
 	java -cp "../KITE-Engine/target/kite-jar-with-dependencies.jar;../KITE-Extras/target/*;../KITE-Framework/target/*;../KITE-Engine/target/*;target/*" org.webrtc.kite.Engine configs/js.vp8.medooze.config.json
) else (
	java -cp "../KITE-Engine/target/kite-jar-with-dependencies.jar;../KITE-Extras/target/*;../KITE-Framework/target/*;../KITE-Engine/target/*;target/*" org.webrtc.kite.Engine configs/js.config.json
)

@echo off
set KITE_JAR=../../KITE-2.0/KITE-Engine/target/kite-jar-with-dependencies.jar
IF EXIST "%KITE_JAR%" (
  IF "%1"=="h264" (
    java -Dkite.firefox.profile=../third_party/ -cp "%KITE_JAR%;target/*" org.webrtc.kite.Engine configs/js.h264.medooze.config.json
  ) else IF "%1"=="vp8" (
    java -Dkite.firefox.profile=../third_party/ -cp "%KITE_JAR%;target/*" org.webrtc.kite.Engine configs/js.vp8.medooze.config.json
  ) else (
    java -Dkite.firefox.profile=../third_party/ -cp "%KITE_JAR%;target/*" org.webrtc.kite.Engine configs/js.medooze.config.json
  )
) ELSE (
  echo "File not found: %KITE_JAR%"
  echo "Please edit this file and set KITE_JAR to the correct path."
)


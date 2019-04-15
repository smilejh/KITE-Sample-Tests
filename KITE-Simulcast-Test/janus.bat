@echo off
set KITE_JAR=../../KITE-2.0/KITE-Engine/target/kite-jar-with-dependencies.jar
IF EXIST "%KITE_JAR%" (
  java -Dkite.firefox.profile=../third_party/ -cp "%KITE_JAR%;target/*" org.webrtc.kite.Engine configs/janus.simulcast.config.json
) ELSE (
  echo "File not found: %KITE_JAR%"
  echo "Please edit this file and set KITE_JAR to the correct path."
)

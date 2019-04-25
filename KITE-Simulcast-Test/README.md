#  KITE-Simulcast-Test

This sample test script is designed for testing the simulcast loopback page against medooze SFU.

__NOTE:__ This sample test requires the following servers to be up:
https://d10.conf.meetecho.com/ietf104/echotest-cap.html?simulcast2=true&vcodec=vp8
https://playground.cosmosoftware.io/simulcast/index.html?codec=h264

## Pre-requisite: Selenium Grid

To run this test you will need a Selenium Grid with the browsers to be tested. There is a sample Selenium grid under `localgrid/` which can help you to get started.

## Config
 
 A sample config file is provided at  
 
 `configs/janus.simulcast.config.json`


## Test Script


1.	Open URL https://d10.conf.meetecho.com/ietf104/echotest-cap.html?simulcast2=true&vcodec=vp8
2.	Check the published video
3.	Check received video (looped back) video
4.	GetStats on all the peerConnection
5.	Take a screenshot

**Optional Checks**

6.	Simucast check:
To enable/disable this check, set `checkSimulcast` in the config file to `true`/`false`.
For each of the 9 layers (3 FPS x 3 reesolution): click the button to set the resolution (SL2, SL1, SL0) and click the button to set the FPS (TL2, TL1, TL0), then check the received video resolution: if the sent video is 1280x720, the received resolution is expected to be respectively:
SL2: 1280x720
SL1: 640x360
SL0: 320x180

7. Bitrate bug check:

There is a bug in Chrome when the bandwidth is limited where the lower bitrate exceeds the medium and/or high bitrate.
To enable/disable this test, set `bandwidthCheckDuration` in the config file to a value > 0 (in seconds) or set it to 0 to disbale this check.
This step will compare the bitrate values every second for `bandwidthCheckDuration` seconds of low, medium and high and increment two counters `nbLowHigherThanMedium` and `nbMediumHigherThanHighwhenever` low > medium or medium > high respectively.


## Compile

Under the root directory:  
``` 
mvn -DskipTests clean install 
``` 

## Run

Under the KITE-Simulcast-Test/ folder, execute:
```
java -Dkite.firefox.profile=../third_party/ -cp ../KITE-Engine/target/kite-jar-with-dependencies.jar;../KITE-Common/target/kite-extras-1.0-SNAPSHOT.jar;../KITE-Engine-IF/target/kite-if-1.0-SNAPSHOT.jar;../KITE-Engine/target/kite-engine-1.0-SNAPSHOT.jar;target/simulcast-test-1.0-SNAPSHOT.jar org.webrtc.kite.Engine configs/local.simulcast.config.json
```


## Test output

Each will generate allure report found in `kite-allure-report/` folder.
To run Allure:
```
allure serve kite-allure-reports
```






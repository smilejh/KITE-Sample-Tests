#  KITE-Janus-Test

This sample test script is designed for load testing. It creates **N** meeting rooms on Janus SFU server and connect **K** users to each room.
It tests against a modified Janus videoroom plugin web app.

## Test Script


1.	Open URL https://lbclient.cosmosoftware.io/videoroomtest_videoanalysis.html?roomId=meetingID&username=userID
Meeting ID will be provided by the RoomManager. Every **K** participants, depending on the model in the config file.
2.	Check the published video
3.	Check received videos from all participants
4.	GetStats on all the peerConnections
5.	Take a screenshot
6.	Stay in the meeting until the end of the test


## Pre-requisite: Selenium Grid

To run this test you will need a Selenium Grid with at least **K** instance of Chrome.

## Config
 
 A sample config file is provided at  
 
 `configs/local.janus.config.json`  

### Important parameters 

Set the address of your Selenium Hub:  
  `"remoteAddress": "http://localhost:4444/wd/hub"`  
  
Set your Chrome version and OS according to what is available on your Grid. The test has been written specifically for Chrome. Some functionality will not work on other web browsers.
```
"browsers": [
    {
      "browserName": "chrome",
      "version": "72",
      "platform": "WINDOWS",
      "headless": false
    }
  ]
```


Set the total number of participants (= number of Chrome instances):  
`"tupleSize": 6`  

Set the **K** number of participant in each meeting room:  
`"usersPerRoom": 2`  

With this setting, KITE will create **N**=3 meeting rooms with **K**=2 participants in each meeting.  


### Report parameters

Whether to take screenshot for each test/client (if false, it will still take screenshot in case of failure)     
`"takeScreenshotForEachTest": true`  


### GetStats parameters

Whether to call getStats  
`"getStats": true`  

How long to collect stats for (in seconds)  
`"statsCollectionTime" : 4`  

Interval between 2 getStats calls (in seconds)  
`"statsCollectionInterval" : 2`



You should not need to change any other parameter.


## Compile

Under the root directory:  
``` 
mvn -DskipTests clean install 
``` 

## Run

Under the KITE-Janus-Test/ folder, execute:  
```
java -cp ../KITE-Engine/target/kite-jar-with-dependencies.jar;../KITE-Common/target/kite-extras-1.0-SNAPSHOT.jar;../KITE-Engine-IF/target/kite-if-1.0-SNAPSHOT.jar;../KITE-Engine/target/kite-engine-1.0-SNAPSHOT.jar;target/Janus-test-1.0-SNAPSHOT.jar org.webrtc.kite.Engine configs/local.janus.config.json
```


## Test output

Each will generate allure report found in `kite-allure-report/` folder.
To run Allure:
```
allure serve kite-allure-reports
```






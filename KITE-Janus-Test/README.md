#  KITE-Janus-Test


To be updated.

## Test Script


1.	Open Janus demo URL
2.	Check the published video
3.	Check received videos from all participants
4.	GetStats on all the peerConnections
5.	Take a screenshot
6.	Stay in the meeting until the end of the test


## Pre-requisite: KITE-2.0

You'll need KITE to run these sample tests.  
To setup KITE, please follow these [instructions](https://github.com/webrtc/KITE/blob/master/README.md).   

## Compile

To compile, cd to the KITE-Janus-Test and enter `c all` to compile the entire project or `c` to compile only this module:
```
cd %KITE_HOME%\KITE-Janus-Test
c all
```

## Run KITE-Janus-Test

Edit the file `./KITE-Janus-Test/configs/local.janus.config.json` with your favorite text editor.  
You will need to change __`version`__ and __`platform`__ according to what is installed on your local grid.

To run the Janus test:
```
cd %KITE_HOME%\KITE-Janus-Test
r local.janus.config.json
```

Alternatively, you can launch the test with the full command.
On Windows:  
```
-Dkite.firefox.profile="%KITE_HOME%"/third_party/ -cp "%KITE_HOME%/KITE-Engine/target/kite-jar-with-dependencies.jar;target/*" org.webrtc.kite.Engine configs/local.janus.config.json
```
On Linux/Mac:  
```
-Dkite.firefox.profile="$KITE_HOME"/third_party/ -cp "$KITE_HOME/KITE-Engine/target/kite-jar-with-dependencies.jar:target/*" org.webrtc.kite.Engine configs/local.janus.config.json
```

## Open the dashboard

After running the test, you can open the Allure dashboard with the command `a`.
```
cd %KITE_HOME%\KITE-AppRTC-Test
a
```

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






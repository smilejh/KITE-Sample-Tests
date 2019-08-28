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

## Config
 
 A sample config file is provided at  
 
 `configs/local.janus.config.json`  

### Important parameters 

Set the address of your Selenium Hub:  
  `"remoteAddress": "http://localhost:4444/wd/hub"`  
  
Set your Chrome version and OS according to what is available on your Grid. You can use `localhost` as the platform name if the grid is running on your localhost, KITE will automatically set it according to your OS.
```
"browsers": [
    {
      "browserName": "chrome",
      "version": "76",
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

```
"getStats" : {
          "enabled": true,
          "statsCollectionTime": 2,
          "statsCollectionInterval": 1,
          "peerConnections": ["sfutest.webrtcStuff.pc"],
          "selectedStats" : ["inbound-rtp", "outbound-rtp", "candidate-pair"]
        }
```


Whether to call getStats  
`"enabled": true`  

How long to collect stats for (in seconds)  
`"statsCollectionTime" : 4`  

Interval between 2 getStats calls (in seconds)  
`"statsCollectionInterval" : 2`


You should not need to change any other parameter.


## Compile

Just type `c` (which will execute `mvn clean install -DskipTests`). 
    
    ```
    c
    ```

If you are within a test folder, for example in KITE-Janus-Test, you can type __`c`__ to compile the test module
only or __`c all`__ to recompile the entire project:

    ```
    cd KITE-Janus-Test  
    c all
    ```  
    


## Run

```
cd KITE-Janus-Test
r configs\videoroom.janus.config.json
```


## Test output

Each will generate allure report found in `kite-allure-report/` folder.  
After running the test, you can open the Allure dashboard with the command `a`.

```
cd KITE-Janus-Test
a
```


Alternatively, the full command to launch the Allure dashboard is:  
```
allure serve kite-allure-reports
```

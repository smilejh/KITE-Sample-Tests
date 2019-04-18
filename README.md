# KITE-Sample-Tests

You'll need KITE to run these sample tests.  
To setup KITE, please follow these [instructions](https://github.com/webrtc/KITE/blob/master/README.md).   


### Run KITE-Janus-Test

Edit the file `./KITE-Janus-Test/configs/local.janus.config.json` with your favorite text editor.  
You will need to change __`version`__ and __`platform`__ according to what is installed on your local grid.

To run the AppRTC iceconnection test:
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

### Open the dashboard

After running the test, you can open the Allure dashboard with the command `a`.
```
cd %KITE_HOME%\KITE-AppRTC-Test
a
```
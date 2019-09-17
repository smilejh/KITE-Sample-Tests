# KITE-Hangouts-Tests

## Requirements
In order to run the test, you'll need the KITE Engine (KITE 2.0) from webrtc.org's [KITE Github repository](https://github.com/webrtc/KITE/tree/kite-2.0)

Please follow the instructions to configure and compile the Engine. The KITE-Sample-Tests are all dependent on the KITE Engine and will not compile on their own.


### Compiling KITE-Hangouts-Test

Prior to running any test, whether it is in Java or Javascript, it is necessary to compile the module with Maven.
The Maven will not only compile the java classes, but also execute the 'npm install' used by the javascript version of the test.


#### If you have already follow the instructions for the KITE Engine, you will have:

* `KITE_HOME` is set as the path to where you configured KITE Engine
* The appropriate path (depends on your OS) is included to you `PATH`, so that you will have access to these commands:

	* `c` : compile with maven in your current directory.
	* `r <config file>` : run kite test with a config file.
	* `a` : open Allure report from the `kite-allure-reports` folder from your current directory.
	

#### If not, you might need to do that first. But you can still compile the project with:

```
mvn -DskipTests clean install
```

### Test configuration

The config files are `configs/hangouts.config.json` (for Java) and `configs/js.hangouts.config.json` (for Javascript).  

Important parameters:
* "matrix": [[0,0,1,0]] => this is a the Client matrix definition. It's an optional parameter that allows to specify the combination of Clients (browsers)
In the sample config, client[0] is Chrome and client[1] is Firefox. Since the tuple size is 4, it will run one test with Chrome - Chrome - Firefox - Chrome.
* "tupleSize": 4 => the number of concurrent Clients.
* "payload" => the payload is a free json object that contains all the parameters meant for the Test itself (whereas other params in the config file are mean for KITE):
```json
{
      "url": "https://hangouts.google.com/",
      "usersPerRoom": 2,
      "meetingDuration": 30,
      "windowSize": "800x600",
      "users": [
        {"user":"kite.test.cosmo", "pass":"Test!123"},
        {"user":"kite5.test.cosmo", "pass":"Test!123"},
        {"user":"kite2.test.cosmo", "pass":"Test!123"},
        {"user":"kite3.test.cosmo", "pass":"Test!123"}
      ]
  }
```
usersPerRoom => it's the number of participant in one meeting room. Since we have a tuple of 4, in this config, there will be two meetings with two participants in each.
meetingDuration is in seconds.
The other parameters are self-explanatory.


#### Audio and Video (Chrome only)


The current configuration assume that you have the following audio and video files, which can be downloaded at:
https://drive.google.com/file/d/1kBAEgiJbzos1fUeGMs86xvxKrkN9JpsX/view?usp=sharing

If possible, extract the files to:
/home/ubuntu/Videos/e-dv548_lwe08_christa_casebeer_003.y4m  
/home/ubuntu/Audios/e-dv548_lwe08_christa_casebeer_003.wav  

On Windows:
c:\home\ubuntu\Videos\e-dv548_lwe08_christa_casebeer_003.y4m  
c:\home\ubuntu\Audios\e-dv548_lwe08_christa_casebeer_003.wav  

If you want to use a different location, just updated the "directory" in the config so KITE can find the files.

```
  "video": {
    "directory": "/home/ubuntu/Videos/",
    "filename": "e-dv548_lwe08_christa_casebeer_003",
    "duration": "01:00:00",
    "type": "Video"
  },
  "audio": {
    "directory": "/home/ubuntu/Audios/",
    "filename": "e-dv548_lwe08_christa_casebeer_003",
    "duration": "01:00:00",
    "type": "Audio"
  }
```

In order to use the default media, you can 'comment out' the "video" and "audio" configs or just delete it altogether.

```
  "_video": {
    "directory": "/home/ubuntu/Videos/",
    "filename": "e-dv548_lwe08_christa_casebeer_003",
    "duration": "01:00:00",
    "type": "Video"
  },
  "_audio": {
    "directory": "/home/ubuntu/Audios/",
    "filename": "e-dv548_lwe08_christa_casebeer_003",
    "duration": "01:00:00",
    "type": "Audio"
  }
```

### Running the KITE-Hangouts-Test 

#### Java

To run the Java verison of the test:
```
cd KITE-Hangouts-Test
r hangouts.config.json
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


#### Javascript

To run the Java verison of the test:
```
cd KITE-Hangouts-Test
r js.hangouts.config.json
```


### Open the dashboard (Allure Report)

When the tests is launched using the above script, it will generate Allure reports in the folder `kite-allure-reports`.

After running the test, you can open the Allure dashboard with the command `a`.
```
cd KITE-Hangouts-Test
a
```

or (assuming you've `allure` in your PATH):
```
allure serve kite-allure-reports\
```

Your default browser should open automatically on the Allure dashboard page.


### Understanding the Test Script

Both Java and Javascript tests are written following the Page Object Model (POM) where the code is organised between Pages (where the UI element to interact with are defined), Steps and Checks.
The Test is a succession of Steps (actions) and Checks (validations). 

#### Javascript

The entry file is Hangouts.js and the list of steps and checks are added in the testScript() function:

```
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
      this.page = new MainPage(this.driver);

      let loginStep = new LoginStep(this);
      await loginStep.execute(this);
      
      let startVideoCallStep = new StartVideoCallStep(this);
      await startVideoCallStep.execute(this);      
      await this.waitAllSteps();
      
      let joinVideoCallStep = new JoinVideoCallStep(this);
      await joinVideoCallStep.execute(this);      
      await this.waitAllSteps();
      
      let firstVideoCheck = new FirstVideoCheck(this);
      await firstVideoCheck.execute(this);

      let allVideoCheck = new AllVideoCheck(this);
      await allVideoCheck.execute(this);

      await this.waitAllSteps();
      let screenshotStep = new ScreenshotStep(this);    
      await screenshotStep.execute(this);
      
      
    } catch (e) {
      console.log('Exception in testScript():' + e);
    } finally {
      await this.driver.quit();
    }
```

For more information about the KITE test scripts implementation, there are tutorials available:
* Java https://github.com/webrtc/KITE/blob/master/tutorial/Tutorial_Java.md
* Javascript https://github.com/webrtc/KITE/blob/master/tutorial/Tutorial_Javascript.md


The KITE engine and sample tests are sometimes updated faster than the tutorial and some info may be outdated. If you encounter any issue or need any help, please do not hesitate to open a ticket on GitHub and the CoSMo Software Team will look at it promptly. 



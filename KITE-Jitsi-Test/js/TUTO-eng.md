# Jitsi test tutoral
*****
## Writing a test


This tutorial will guide you step-by-step in writing your first KITE Test. The test will open the https://meet.jit.si/ page on a web browser (Chrome and Firefox), check the sent and received videos, collect the WebRTC statistics and take a screenshot.  

1. Create the Test
2. Adding a Step
    2. Step: open a the web page
3. Adding a new Page
    1. Page: jitsi page
4. Adding the Checks 
    1. Sent Video Check
    2. Received Videos Check
5. Step: collect WebRTC Stats
6. Step: taking a Screenshot
&nbsp;

### KITE Test Design Pattern

KITE Tests are implemented in a framework that follows the Page Object Model design pattern [(POM)](https://medium.com/tech-tajawal/page-object-model-pom-design-pattern-f9588630800b), and organises a Test in Pages, Steps and Checks.  
Using Page Object Model, all element locators are being managed in separate directory and can be updated easily without any change in test cases.  
A Test consists of a succession of Steps and Checks:
- Steps are actions, for example, navigate to a page or click a button. Steps are not expected to fail, they either 'Pass' or 'Break' (any unexpected error).
- Checks are assertations, for example, validate that a video is playing. Checks can 'Pass' (video is playing), 'Fail' (video is blank/still) or 'Break' (e.g. page timeout). 

Pages are where we place the element locators and the functions to interact with them.



*****

### 1. Create the Test

To create the test, execute the following commands:

__On Windows:__  
```
cd %KITE_HOME%
kite_init jitsiTutorial
c
```
__On Linux/Mac:__   
```
cd $KITE_HOME
kite_init jitsiTutorial
c
```

This will create KITE-JitsiTutorial-Test and with inside, all the basic files and folders used in all KITE Tests, for both Java and Javascript.
This tutorial is about the Javascript KITE Test and all the javascript files are located in: `KITE-JitsiTutorial-Test/js`.   
It will also recompile the project.

#### Generate the test 
```
const {TestUtils, WebDriverFactory, KiteBaseTest} = require('kite-common'); 
```

To start, here is the base of the test:  
**Template.js**
 
```
const {TestUtils, WebDriverFactory, KiteBaseTest} = require('kite-common'); 
```
**TestUtils** contains a set of common functions that are used by all KITE Tests.
**WebDriverFactory** allows the creation of the WebDriver.
**KiteBaseTest** this is the parent class of the test, it will enable the generation of the Allure Report.

The following codes gets the capabilities required to create the webdrivers and the payload which is the test specific configuration passed to the test by the KITE Engine.
```
const globalVariables = TestUtils.getGlobalVariables(process);
const capabilities = require(globalVariables.capabilitiesPath);
const payload = require(globalVariables.payloadPath);
```
  
Next, we will edit the configuration file.
**Expliquer le document de config**
*****

Once done, you can already run the test with:
```
r configs\js.jitsiTutorial.config.json
```
At this stage, the test only launch a webbrowser, open https://meet.jit.si/ and does a random check.

Open the Allure Report with:
```
a
```
You should get the following:
////////////////// SCREENSHOT //////////////////

*****
### 2. Adding a Step
#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. Step: open a the web page

Thanks to the kite_init command, you have the file `/steps/OpenUrlStep.js`.
This one contains everything to create this step.

**TestStep** this is the parent class that enables the Allure Report generation.

The constructor of each Step must contain the WebDriver object. It is needed for the Step to control the web browser and execute what it needs to do.  
Next, the constructor will include the other variables and objects needed for the Step. Moreover, there is the attribute this.page which contains an instance of the MainPage.

⚠ To create a Step or a Check, you must implement the two functions **stepDescription()** and **step()**, which are abstract functions of the parent class **TestStep**.

**stepDescription()** returns a string which will be displayed in the report.

**step()** is asynchronous because it executes WebDriver functions which are. Therefore, we use `async/await` to make it synchronous.

Therefore:
    
    async step() {
        await this.page.open(this);
    }

**step()** will call **open()** in `/pages/MainPage.js` which use **TestUtils.open()** to open the browser to the url. Indeed, this.page is an instance of MainPage.

Now we're going to add the code that allows joining a meeting.
*****
### 3. Adding a Page
#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. Page: Main page

Similarly to the Steps which are placed in the `steps/` folder, the Pages are placed in the `pages/` folder.
The `kite_init` script created two files:  `pages/MainPage.js` and `pages/index.js`.
Here we're going to edit `pages/MainPage.js` and add the HTML locators and functions that interact with the web page.
 
To add HTLM locators, we are going to use the class `By` from [Selenium-webdriver](https://seleniumhq.github.io/selenium/docs/api/javascript/module/selenium-webdriver/).
Here, we will use the text input to choose our meeting room;

`const meetingRoom = By.id('enter_room_field')` **enter_room_field** is the id of the HTML object.

In MainPage class, add:

    async enterRoom(stepInfo, room) {
        const roomId = room + stepInfo.uuid;
        let meeting = await stepInfo.driver.findElement(meetingRoom);
        await meeting.sendKeys(roomId); // Fill out the field and add some random numbers
        await meeting.sendKeys(Key.ENTER); // Press ENTER to enter in the room
    }

The variable **stepInfo** is a reference to the Step object (= this).
To interact with a HTML element, for example the button or the text input, we're using the selenium-webdriver API.
The full API doc can be found here: [Selenium-webdriver](https://seleniumhq.github.io/selenium/docs/api/javascript/module/selenium-webdriver/)
`Key` is an enumeration of keys like "ENTER" that can be used: [Key](https://seleniumhq.github.io/selenium/docs/api/javascript/module/selenium-webdriver/index_exports_Key.html).

Now, we are going to modify `/steps/OpenUrlStep.js` to join our metting room.

in **step()**, add:
`await this.page.enterRoom(this, "I am a random room");

To obtain:

    async step() {
        await TestUtils.open(this);
        await jitsiPage.enterRoom(this, "I am a random room");
    }    

You can run the test with:
```
r configs\js.jitsiTutorial.config.json
```
As you can see, the page loads, the text input fills up and we join a room.

Open the Allure Report with:
```
a
```
You should get the following:
    ![First Step Allure Report](./TutorialScreenshots/Screenshot2AllureReport.png)
****
### 4. Adding the Checks
#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. Sent Video Check

Now, we're going to add a Check. A Check is a kind of Step that asserts a condition and can 'Fail'. All Checks are places in the `checks/` folder.

We're goind to create a file named `checks/SentVideoCheck.js`.
Once the file has been created, we'll add a reference to `checks/index.js` so it's available to any code that requires the folder `checks/`:

__** checks/index.js **__  
`exports.SentVideoCheck = require('./SentVideoCheck');`
    
The objective of this Check is to validate that the video is being sent.  We'll need some elements in the `pages/MainPage.js` to access the \<video\> elements.

Open the file `pages/MainPage.js`.

To add the \<video\> elements, we're going to use the class `By` from [Selenium-webdriver](https://seleniumhq.github.io/selenium/docs/api/javascript/module/selenium-webdriver/) by simply adding:
`const videos = By.css('video');` => This allows us to get all the HTML elements with tag \<video\> into our `videos` array.
Next we're going to add a synchronous function called **videoCheck()** where we'll implement the video check logic.

    async videoCheck(stepInfo, index) {},
    
**stepInfo** is a reference to the Check object.
**index** is the index of the video to be checked (0 for the first video, 1 for the 2nd, etc...) 

First, we will declare our variables

    async videoCheck(stepInfo, index) {
        let checked; // Result of the verification
        let i; // iteration indicator
        let timeout = stepInfo.timeout;
        stepInfo.numberOfParticipant = parseInt(stepInfo.numberOfParticipant) + 1; // To add the first video      
    }
    
Then we will wait for all the videos. So, we are going to use `TestUtils.waitVideos()` from kite-common.
  
    async videoCheck(stepInfo, index) {
        let checked; // Result of the verification
        let i; // iteration indicator
        let timeout = stepInfo;
        stepInfo.numberOfParticipant = parseInt(stepInfo.numberOfParticipant) + 1; // To add the first video         
        // Waiting for all the videos
        let i = await TestUtils.waitVideos(stepInfo, videoElements);
        stepInfo.numberOfParticipant --; // To delete the first video
    }

As you can see, we add `parseInt(stepInfo.numerOfParticipant)` **`+ 1`** because there is an other video that does not need to be checked. But, we remove it right after. 

Next we'll check that the video is actually playing, meaning that it isn't blank (all the pixels of the video frame are the same color) or still, 
which means that the same image is still displayed after a second interval. For that we'll use the utility function `TestUtils.verifyVideoDisplayByIndex()` from kite-common:

    async videoCheck(stepInfo, index) {
        let checked; // Result of the verification
        let i; // iteration indicator
        let timeout = stepInfo.timeout;
        stepInfo.numberOfParticipant = parseInt(stepInfo.numberOfParticipant) + 1; // To add the first video
        
        // Waiting for all the videos
        await TestUtils.waitVideos(stepInfo, videoElements);
        stepInfo.numberOfParticipant --; // To delete the first video

        // Check the status of the video
        // checked.result = 'blank' || 'still' || 'video'
        i = 0;
        checked = await verifyVideoDisplayByIndex(stepInfo.driver, index + 1);
            while(checked.result === 'blank' || checked.result === undefined && i < timeout) {
            checked = await verifyVideoDisplayByIndex(stepInfo.driver, index + 1);
            i++;
            await waitAround(1000);
        }

        i = 0;
        while(i < 3 && checked.result != 'video') {
            checked = await verifyVideoDisplayByIndex(stepInfo.driver, index + 1);
            i++;
            await waitAround(3 * 1000); // waiting 3s after each iteration
        }
        return checked.result;
    }
    
We need to add `index` **`+ 1`** to skip the large video in our.
    
To make the check robust to poor connections, we decided to repeat it 3 times at 3 seconds interval. We could make the checks much stricter by doing it only once, which would cause it to fail more easily in case of low framerate.

Now that we completed the implementation in `pages/MainPage.js`, we're going to edit `checks/SentVideoCheck.js`.    

As previously mentioned, a Check is a kind of Step and inherits from the the framework's `TestStep` class.
At the top of the file, we add the following require:
`const {TestStep, KiteTestError, Status} = require('kite-common');`

Then we implement the SentVideoCheck Class and its constructor:
```
class SentVideoCheck extends TestStep {
    constructor(kiteBaseTest) {
        super();
        this.driver = kiteBaseTest.driver;
        this.timeout = kiteBaseTest.timeout;
        this.numberOfParticipant = kiteBaseTest.numberOfParticipant;
        this.page = kiteBaseTest.page;

        // Test reporter if you want to add attachment(s)
        this.testReporter = kiteBaseTest.reporter;
    }
}
```
Update the **stepDescription()**:

    stepDescription() {
        return "Check the first video is being sent OK";
    }
    
Then the **step()**, where we call `this.page.videoCheck()` for the first video in the page (the sender's video).
We compare the result and if it's not 'video' we throw a KiteTestError with the Status.FAILED, that will be reported accordingly in the Allure Report.
```
    async step() {
        try {
            let result = await this.page.videoCheck(this, 0);
            if (result != 'video') {
                this.testReporter.textAttachment(this.report, "Sent video", result, "plain");
                throw new KiteTestError(Status.FAILED, "The video sent is " + result);
            }
        } catch (error) {
            console.log(error);
            if (error instanceof KiteTestError) {
                throw error;
            } else {
                throw new KiteTestError(Status.BROKEN, "Error looking for the video");
            }
        }
    }
```

To finish with this file, at the end, add:
`module.exports = SentVideoCheck;`

Lastly, we return to the main Test class file: `JitsiTutorial.js` and add our check there: 
At the top of the file, we import the class
`const {SentVideoCheck} = require('./checks');`

And add the check to the **testScript()** function:
```
    this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);
    let openUrlStep = new OpenUrlStep(this);
    await openUrlStep.execute(this);
    // New check
    let sentVideoCheck = new SentVideoCheck(this);
    await sentVideoCheck.execute(this);
```    
Now, our test is able to check the sentVideo. 

You can run the test again with:
```
r configs\js.jitsiTutorial.config.json
```
And open the Allure Report with:
```
a
```
You should get the following:
    ![First Check Allure Report](./TutorialScreenshots/Screenshot3AllureReport.png)
*****
#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2. Received Videos Check

Now, we are going to add an other Check. It will check that we received all the remote videos.

We're goind to create a file named `checks/ReceivedVideoCheck.js`.
Once the file has been created, we will add a reference to `checks/index.js`.

__** checks/index.js **__  
`exports.VideoSentCheck = require('./VideoSentCheck');`

The objective of this Check is to validate that all the remode videos are being received.
Fortunately, we already have everything to do it.

Open the file `checks/ReceivedVideoCheck.js`.

Like the last check, at the top of the file, we add the following require:
`const {TestStep, KiteTestError, Status} = require('kite-common');`

Then we implement the ReceivedVideoCheck Class and its constructor:
```
class ReceivedVideoCheck extends TestStep {
    constructor(kiteBaseTest) {
        super();
        this.driver = kiteBaseTest.driver;
        this.timeout = kiteBaseTest.timeout;
        this.numberOfParticipant = kiteBaseTest.numberOfParticipant;
        this.page = kiteBaseTest.page;
    
        // Test reporter if you want to add attachment(s)
        this.testReporter = kiteBaseTest.reporter;
    }
} 
```
    
Update the **stepDescription()**:

    stepDescription() {
        return "Check the other videos are being received OK";
    }
    
Then the **step()**, where we call `this.page.videoCheck()` for each remote video.
We compare every result and if one is not 'video', then we throw a KiteTestError with the Status.FAILED, that will be reported accordingly in the Allure Report.

    async step() {
        let result = "";
        let tmp;
        let error = false;
        try {
            for(let i = 1; i < this.numberOfParticipant; i++) {
                tmp = await this.page.videoCheck(this, i);
                result += tmp;
                if (i < this.numberOfParticipant) {
                    result += ' | ';
                }
                if (tmp != 'video') {
                    error = true;
                }
            }
            if (error) {
                this.testReporter.textAttachment(this.report, "Received videos", result, "plain");
                throw new KiteTestError(Status.FAILED, "Some videos are still or blank: " + result);
            }
        } catch (error) {
            console.log(error);
            if (error instanceof KiteTestError) {
                throw error;
            } else {
                throw new KiteTestError(Status.BROKEN, "Error looking for the video");
            }
        }
    }

The for-loop, starting at index 1, allows to check every reveived video. Indeed, the one of index 0 is the video sent.

Do not forget, at the end of this file, add:
`module.exports = ReceivedVideoCheck;`

Finally, we add this check as the previous one in ```JitsiTutorial.js```. 
We modify: 
`const {SentVideoCheck} = require('./checks');`
by
`const {SentVideoCheck, ReceivedVideoCheck} = require('./checks');`

and add in **testScript()**:

    this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);
    let openJitsiUrlStep = new OpenJitsiUrlStep(this);
    await openJitsiUrlStep.execute(this);
    let videoSentCheck = new VideoSentCheck(this);
    await videoSentCheck.execute(this);
    // New check
    let videoReceivedCheck = new VideoReceivedCheck(this);
    await videoReceivedCheck.execute(this);
    
Now, our test is able to every videos (sent and received). 

You can run the test again with:
```
r configs\js.jitsiTutorial.config.json
```
And open the Allure Report with:
```
a
```
You should get the following:
    ![Second Step Allure Report](./TutorialScreenshots/Screenshot4AllureReport.png)
*****
### 5. Step: get stats

#CONFIG FILE
Before we start, we have to modify our config file, **configs/js.jitsiTutorial.config.json**.
Indeed, this step require some information to work properly.
So, in `payload`, we add :

    "getStats" : {
        "enabled": true,
        "statsCollectionTime": 2,
        "statsCollectionInterval": 1,
        "peerConnections": ["window.pc[0]"],
        "selectedStats" : ["inbound-rtp", "outbound-rtp", "candidate-pair"]
    }

To obtain: 

    "payload":{
        "url": "https://meet.jit.si/",
        "getStats" : {
            "enabled": true,
            "statsCollectionTime": 2,
            "statsCollectionInterval": 1,
            "peerConnections": ["window.pc[0]"],
            "selectedStats" : ["inbound-rtp", "outbound-rtp", "candidate-pair"]`
        }
    }


Now, we are going to create a step from scratch. Remember, all Steps are places in the `steps/` folder.

We're goind to create a file named `steps/GetStatsStep.js`.
Once the file has been created, we'll also add a reference to `steps/index.js`.

__** steps/index.js **__  
`exports.GetStatsStep = require('./GetStatsStep');`

Open the file `checks/GetStatsStep.js`.

At the top of the file, we add the following require:
`const {TestStep, KiteTestError, Status} = require('kite-common');`

Then, in this file, we implement the GetStatsStep class and its constructor:

```
class GetStatsStep extends TestStep {
    constructor() {
        super();
        this.driver = kiteBaseTest.driver;
        this.statsCollectionTime = kiteBaseTest.statsCollectionTime;
        this.statsCollectionInterval = kiteBaseTest.statsCollectionInterval;
        this.selectedStats = kiteBaseTest.selectedStats;
        this.peerConnections = kiteBaseTest.peerConnections;
        this.page = kiteBaseTest.page;

        // Test reporter if you want to add attachment(s)
        this.testReporter = kiteBaseTest.reporter;
    }
}
```
`this.statsCollectionTime`: duration of data collection
`this.statsCollectionTime`: interval of data collection
`this.selectedStats`: the data you want to collect
`this.peerConnections`: array of peer connections used to get stats;

    
Update the **stepDescription()**:

    stepDescription() {
        return 'Getting WebRTC stats via getStats'; 
    }

Then the **step()**:

    async step() {
        try {
            let rawStats = await this.page.getStats(this);
            let summaryStats = TestUtils.extractJson(rawStats, 'both');
            // // Data
            this.testReporter.textAttachment(this.report, 'GetStatsRaw', JSON.stringify(rawStats), "json");
            this.testReporter.textAttachment(this.report, 'GetStatsSummary', JSON.stringify(summaryStats), "json");
        } catch (error) {
            console.log(error);
            throw new KiteTestError(Status.BROKEN, "Failed to getStats");
        }
    }
    
The **this.page.getStats()** allows to get stats. We are going to create it right after in MainPage();
The **TestUtils.extractJson()** allows to make a summary of the data collected.

Now, we are going to create **getSats()**.
Open the file **/pages/MainPage.js**.

First, we need a script to get the peer connection.
Here is the script:

    window.peerConnections = [];
    map = APP.conference._room.rtc.peerConnections;
    for(var key of map.keys()){
        window.peerConnections.push(map.get(key).peerconnection);
    }

So, we create a function, outside our class, to get this script:

    const getPeerConnectionScript = function() {
        return "window.peerConnections = [];"
        + "map = APP.conference._room.rtc.peerConnections;"
        + "for(var key of map.keys()){"
        + "  window.peerConnections.push(map.get(key).peerconnection);"
        + "}";
    }
    
Then, in **MainPage** class: 

    async getStats(stepInfo) {
        await stepInfo.driver.executeScript(getPeerConnectionScript());
        let stats = await TestUtils.getStats(stepInfo, 'kite', stepInfo.peerConnections[0]);
        return stats;
    }
    
The **stepInfo.driver.executeScript(getPeerConnectionScript())** will execute our script to get the peer connection more easily.
The **TestUtils.getStats()** get stats from the peer connection.

Do not forget, at the end of this file, add:
`module.exports = GetStatsStep;`

Finally, we add this step in **JitsiTutorial.js**.
We modify: 
`const {OpenUrlStep} = require('./steps');`
by
`const {OpenUrlStep, GetStatsStep} = require('./steps');`

and add in **testScript()**:

    this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);
    let openJitsiUrlStep = new OpenJitsiUrlStep(this);
    await openJitsiUrlStep.execute(this);
    let videoSentCheck = new VideoSentCheck(this);
    await videoSentCheck.execute(this);
    let videoReceivedCheck = new VideoReceivedCheck(this);
    await videoReceivedCheck.execute(this);
    // New step
    if (this.getStats) {
        let getStatsStep = new new GetStatsStep(this);
        await getStatsStep.execute(this);
    }

Like this, it is easier to enable or disable this step using the config file.

Now, you can get stats from the peer connection.

You can run the test again with:
```
r configs\js.jitsiTutorial.config.json
```
And open the Allure Report with:
```
a
```
You should get the following:
    ![GetStats Step Allure Report](./TutorialScreenshots/Screenshot5AllureReport.png)  
*****
### 6. Step: take a screenshot 

Now, we are going to create a new step (Steps in `steps/` folder).

We're goind to create a file named `steps/ScreenshotStep.js`.
Once the file has been created, we'll again add a reference to `steps/index.js`.

__** steps/index.js **__  
`exports.ScreenshotStep = require('./ScreenshotStep');`

Open the file `checks/ScreenshotStep.js`.

At the top of the file, we add the following require:
`const {TestUtils, TestStep} = require('kite-common');`

Then, in this file, we implement the ScreenshotStep class and its constructor:

```
class ScreenshotStep extends TestStep {
    constructor() {
        super();
        this.driver = kiteBaseTest.driver;

        // Test reporter if you want to add attachment(s)
        this.testReporter = kiteBaseTest.reporter;
    }
}
```
    
Update the **stepDescription()**:

    stepDescription() {
        return 'Get a screenshot'; 
    }

Then the **step()**:

    async step() {
        let screenshot = await TestUtils.takeScreenshot(this.driver);
        this.testReporter.screenshotAttachment(this.report, "Screenshot step", screenshot); 
    }


The **TestUtils.takeScreenshot()** allows to take a screenthot.

Finally, we add this step in **JitsiTutorial.js**.
We modify: 
`const {OpenUrlStep} = require('./steps');`
by
`const {OpenUrlStep, GetStatsStep} = require('./steps');`

and add in **testScript()**:

    this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);
    let openJitsiUrlStep = new OpenJitsiUrlStep(this);
    await openJitsiUrlStep.execute(this);
    let videoSentCheck = new VideoSentCheck(this);
    await videoSentCheck.execute(this);
    let videoReceivedCheck = new VideoReceivedCheck(this);
    await videoReceivedCheck.execute(this);
    if (this.getStats) {
        let getStatsStep = new new GetStatsStep(this);
        await getStatsStep.execute(this);
    }
    // New step
    let screenshotStep = new ScreenshotStep(this);
    await screenshotStep.execute(this);


You can also do the same as getStats to enable or disable this step more easily. You just have to add "takeScreenshotForEachTest": true, in **configs/js.jitsiTutorial.config.json** in the payload.

You can run the test again with:
```
r configs\js.jitsiTutorial.config.json
```
And open the Allure Report with:
```
a
```
Finally, you should get the following:
    ![Screenshot Step Allure Report](./TutorialScreenshots/Screenshot6AllureReport.png)
*****
The test creation is complete.

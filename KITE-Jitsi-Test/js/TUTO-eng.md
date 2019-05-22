# Jitsi test tutoral
*****
## Writing a test


This tutorial will guide you step-by-step in writing your first KITE Test. The test will open the https://meet.jit.si/ page on a web browser (Chrome and Firefox), check the sent and received videos, collect the WebRTC statistics and take a screenshot.  
KITE Tests follow the Page Object Model design pattern [(POM)](https://medium.com/tech-tajawal/page-object-model-pom-design-pattern-f9588630800b). 

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

todo: the tutorial needs to be update once the template has been done.
 

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
r configs\jitsiTutorial.config.json
```
At this stage, the test only launch a webbrowser, open https://meet.jit.si/ and does a random check.



*****
### 2. Adding a Step
#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. Step: open a the web page

todo: update this is already done in the template

Pour ajouter une etape, il est preferable de l'ajouter dans le dossier steps qui contiendra l'ensemble des etapes de votre test.

Ensuite a l'interieur de ce dossier, on cree un fichier nomme: *OpenJitsiUrlStep.js*
Cette etape permettra de naviguer jusqu'a l'URL: https://meet.jit.si/

Voici une base de votre etape:
**TemplateStep.js**

**TestStep** this is the parent class that enables the Allure Report generation.

The constructor of each Step must contain the WebDriver object. It is needed for the Step to control the web browser and execute what it needs to do.  
Next, the constructor will include the other variables and objects needed for the Step.


⚠ To create a Step, you must implement the two functions *stepDescription()* and *step()*, which are abstract functions of the parent class *TestStep*.

Next you'll update the text in *stepDescription*, which will be displayed in the report:  

    stepDescription() {
        return 'Open https://meet.jit.si/ and wait for page to load';
    }

The function *step()* is asynchronous because it executes WebDriver functions which are. Therefore, we use `async/await` to make it synchronous.
To open the browser to the url, we use the TestUtils.open() function:      
    async step() {
        await TestUtils.open(this);
    }

todo: not needed for the open page which comes from the template
All Steps, Checks and Pages classes need to be exported to be available to others in the test. We do that in the file `steps/index.js`: 

`exports.OpenJitsiUrlStep = require("./OpenJitsiUrlStep");`
todo: not needed for the open page which comes from the template
Puis on ajoute au debut du fichier *Jitsi.js*:
todo: not needed for the open page which comes from the template
`const {OpenJitsiUrlStep} = require('./steps');`
todo: not needed for the open page which comes from the template
et ensuite on ajoute a la fonction testcript():
todo: not needed for the open page which comes from the template
`let openJitsiUrlStep = new OpenJitsiUrlStep(this);`
`await openJitsiUrlStep.execute(this);`
todo: not needed for the open page which comes from the template
To obtain:

    async testScript() {
        try {
            this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);

            let openJitsiUrlStep = new OpenJitsiUrlStep(this);
            await openJitsiUrlStep.execute(this);

        } catch (e) {
            console.log(e);
        } finally {
            await this.driver.quit();
        }
    }

todo: not needed for the open page which comes from the template
```
r configs\your.config.json
```
Un navigateur s'ouvre et navigue jusqu'a l'URL: https://meet.jit.si/

Maintenant, on va ajouter le code permettant de rejoindre un meeting.
*****
### 3. Adding a Page
#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. Page: jitsi page

Comme pour les etapes, les pages seront places dans le dossier pages.
Un fichier page contient l'ensemble des elements et fonctions necessaires au fonctionnement de l'etape. 
On va donc creer dans ce dossier un fichier nomme *jitsi-page.js* et l'ajouter au fichier index.js du dossier comme fait precedemment.

Fichier index.js:

`exports.jitsiPage = require("./jitsi-page");`
    
Concernant les elements, ici, nous avons besoin de la barre permettant de choisir le nom du metting.
Pour la recuperer, nous allons avoir besoin de ceci:
`const {By} = require('selenium-webdriver');`
et 
`const meetingRoom = By.id('enter_room_field')` *enter_room_field* est l'id de cet objet.

Ensuite, on va exporter la fonction nous permettant de rentrer dans cette room.

    module.exports = {
        enterRoom: async function(stepInfo, room) {
            let meeting = await stepInfo.driver.findElement(meetingRoom); // Find the element 
            await meeting.sendKeys(room); // Fill out the field
            await meeting.sendKeys(Key.ENTER); // Press ENTER to enter in the room
    }
    
La variable **stepInfo** est le contexte de l'etape (= this).
Les fonctions utilisees sur le bouton sont disponibles ici: [Selenium-webdriver](https://seleniumhq.github.io/selenium/docs/api/javascript/module/selenium-webdriver/)
    
Pour observer le resultat on pourra ajouter:
`const {TestUtils} = require('kite-common');` au debut du fichier.
&
`await TestUtils.waitAround(10000);` a la fin de la fonction enterRoom().

On obtient ainsi:

    const {By, Key} = require('selenium-webdriver');
    const {TestUtils} = require('kite-common'); 
    const meetingRoom = By.id('enter_room_field');
    
    const today = new Date();
    const roomId = (today.getDate()) * (today.getHours()+100) * (today.getMinutes()+100);
    module.exports = {
        enterRoom: async function(stepInfo, room) {
            let meeting = await stepInfo.driver.findElement(meetingRoom);
            await meeting.sendKeys(room + roomId); // Fill out the field and add some random numbers
            await meeting.sendKeys(Key.ENTER); // Press ENTER to enter in the room
            await TestUtils.waitAround(20000);
        }
    }
    
Maintenant il faut modifier le fichier test OpenJitsiUrlStep.js
On ajoute:
`const {jitsiPage} = require('../pages');` au debut du fichier.
&
`await jitsiPage.enterRoom(this, "ChooseYourOwnRoomName");` a la fin de la fonction step.
On obtient:

    const {TestUtils, TestStep} = require('kite-common');
    const {jitsiPage} = require('../pages');
    class OpenJitsiUrlStep extends TestStep {
        constructor(kiteBaseTest) {
            super();
            this.driver = kiteBaseTest.driver;
            this.timeout = kiteBaseTest.timeout;
            this.url = kiteBaseTest.url;
        }
        stepDescription() {
            return 'Open https://meet.jit.si/ and wait for page to load';
        }
        async step() {
            await TestUtils.open(this);
            await jitsiPage.enterRoom(this, "I'm a random room");
        }
    }
    module.exports = OpenJitsiUrlStep;


    
Vous pouvez relancer la commande:
```
r configs\your.config.json
```
Vous observez que la page se charge, le champ se remplit, et nous nous retrouvons dans une room.

****
### 4. Ajouter des checks
#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. Verifier la video envoyee

Maintenant, nous allons ajouter un check. Un check est l'equivalent d'une etape, hormis qu'il permet de verifier quelque chose, par exemple l'etat d'une connexion.
Comme pour les autres fichiers, les checks seront places dans le dossier checks.

On va donc creer dans ce dossier un fichier nomme: *VideoSentCheck.js* et aussi l'ajouter au fichier index.js du dossier

Fichier index.js:
`exports.VideoSentCheck = require('./VideoSentCheck');`
    
Ici, nous voulons verifier que la video a envoye fonctionne.
Nous allons avoir besoin d'elements de la jistsi-page ainsi qu'une fonction deja cree dans le fichier TestUtils de kite-common.

Commencons par jistsi-page.js.

Pour ajouter les elements videos, nous allons utiliser la classe By de [Selenium-webdriver](https://seleniumhq.github.io/selenium/docs/api/javascript/module/selenium-webdriver/), en faisant simplement:
`const videos = By.css('video');` => Ceci permet de recuperer toutes les balises 'video'.
Ensuite, nous allons ajouter une fonction asynchrone dans module.exports:
Appelons cette fonction **videoCheck**:

    videoCheck: async function(stepInfo, direction) {},
    
**stepInfo** est le contexte du Check cree.
**index** est l'index de la video a verifier.
Cette fonction permettra donc d'effectuer les deux checks consistant a verifier les videos.

Pour commencer, nous allons d'abord declarer nos variables:

    videoCheck: async function(stepInfo, index) {
        // wait a while to allow the video to load
        await TestUtils.waitAround(2000);
        let videos; // Our video elements
        let i = 0;       
    }
    
Ensuite nous allons verifier le nombre de videos disponibles:
    
    videoCheck: async function(stepInfo, index) {
        // wait a while to allow the video to load
        await TestUtils.waitAround(2000);
        let videos; // Our video elements
        let checked; // Result of the verification
        let i = 0;       
        // Waiting for all the videos
        while (videos.length < stepInfo.numberOfParticipant + 1 && i < stepInfo.timeout / 1000) {
            videos = await stepInfo.driver.findElements(videoElements);
            i++;
            await TestUtils.waitAround(1000); // waiting 1s after each iteration
        }
    }
    
Comme vous pouvez le constater, nous ajoutons `stepInfo.numerOfParticipant` **`+ 1`** car en effet, il y a une video supplementaire, une large video, qui elle n'a pas besoin d'etre check.

Le fait de recuperer les videos peut provoquer une erreur. En effet, si la derniere etape time out, il faut lancer une erreur. Pour cela, il faut ajouter `KiteTestError` et `Status` dans les dependances, comme ceci:
`const {TestUtils, KiteTestError, Status} = require('kite-common');`

Puis, on verifie qu'il n'y a pas d'erreur dans la verification:

    videoCheck: async function(stepInfo, index) {
        // wait a while to allow the video to load
        await TestUtils.waitAround(2000);
        let videos; // Our video elements
        let checked; // Result of the verification
        let i = 0;       
        // Waiting for all the videos
        while (videos.length < stepInfo.numberOfParticipant + 1 && i < stepInfo.timeout / 1000) {
            videos = await stepInfo.driver.findElements(videoElements);
            i++;
            await TestUtils.waitAround(1000); // waiting 1s after each iteration
        }
        // Make sure that it has not timed out
        if (i === stepInfo.timeout) {
            throw new KiteTestError(Status.FAILED, "unable to find " +
            stepInfo.numberOfParticipant + " <video> element on the page. Number of video found = " +
            videos.length);
        }
    }

Si cette erreur apparait, elle aura pour effet de passer les prochains tests.

Ensuite, on verifie la video a l'aide de la fonction verifyVideoDisplayByIndex() de la librairie Kite-common :

    videoCheck: async function(stepInfo, index) {
        // wait a while to allow the video to load
        await TestUtils.waitAround(2000);
        let videos = []; // Our video elements
        let checked; // Result of the verification
        let i = 0;
        // Waiting for all the videos
        while (videos.length < stepInfo.numberOfParticipant + 1 && i < stepInfo.timeout / 1000) {
            videos = await stepInfo.driver.findElements(videoElements);
            i++;
            await TestUtils.waitAround(1000); // waiting 1s after each iteration
        }
        // Make sure that it has not timed out
        if (i === stepInfo.timeout) {
            throw new KiteTestError(Status.FAILED, "unable to find " +
            stepInfo.numberOfParticipant + " <video> element on the page. Number of video found = " +
            videos.length);
        }
        // Check the status of the video
        // status = 'blank' || 'still' || 'video'
        checked = await TestUtils.verifyVideoDisplayByIndex(stepInfo.driver, index + 1);
        i = 0;
        while(i < 3 && checked.result != 'video') {
            checked = await TestUtils.verifyVideoDisplayByIndex(stepInfo.driver, index + 1);
            i++;
            await TestUtils.waitAround(3 * 1000); // waiting 3s after each iteration
        }
        return checked.result;
    }
    
On ajoute `index` **`+ 1`** pour eviter la large video dans nos checks. 
    
Pour verifier la video, on effectue cette verification 4 fois dont 3 fois a 3 secondes d'intervalle.
    
Une fois ceci fait, retournons dans le fichier VideoSentCheck.js:

Comme dit precedemment, un check est l'equivalent d'un step, nous pouvons donc prendre la meme base.
On ajoute les dependances au debut du fichier:
`const {TestStep, KiteTestError, Status} = require('kite-common');`
&
`const {jitsiPage} = require('../pages');`

On ajoute les valeurs dont on a besoin dans le constructeur:

    constructor(kiteBaseTest) {
        super();
        this.driver = kiteBaseTest.driver;
        this.timeout = kiteBaseTest.timeout;
        this.numberOfParticipant = kiteBaseTest.numberOfParticipant;

        // Test reporter if you want to add attachment(s)
        this.testReporter = kiteBaseTest.reporter;
    }

On modifie la description:

    stepDescription() {
        return "Check the first video is being sent OK";
    }
    
Puis la fonction step:

    async step() {
        try {
            let result = await jitsiPage.videoCheck(this, 0);
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
    
Dans cette fonction, on appelle la fonction videoCheck de la page jitsi, on compare le resultat.
S'il est different de 'video', on lance une erreur.

Pour finir, on revient sur le test principal, Jitsi.js, on ajoute le check dans les dependances, puis on l'ajoute dans la fonction testScript.
`const {VideoSentCheck} = require('./checks');`

    this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);
    let openJitsiUrlStep = new OpenJitsiUrlStep(this);
    await openJitsiUrlStep.execute(this);
    // New check
    let videoSentCheck = new VideoSentCheck(this);
    await videoSentCheck.execute(this);
    
Maintenant, le test Jitsi permet en plus de verifier la video envoyee.

*****
#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2. Verifier les videos recues
On va maintenant ajouter le check permettant de verifier les autres videos.
Pour cela nous allons faire la meme chose que pour le check precedent.
- Creer le fichier VideoReceivedCheck.js dans le dossier checks
- Ajouter `exports.VideoReceivedCheck = require('./VideoReceivedCheck');` au fichier index.js
- Ajouter `const {TestStep, KiteTestError, Status} = require('kite-common');` 
& `const {jitsiPage} = require('../pages');` au debut du fichier VideoReceivedCheck.js

Nous reprenons la meme base que le check precedent, et nous modifier le constructeur de la meme maniere:

    constructor(kiteBaseTest) {
        super();
        this.driver = kiteBaseTest.driver;
        this.timeout = kiteBaseTest.timeout;
        this.numberOfParticipant = kiteBaseTest.numberOfParticipant;
    
        // Test reporter if you want to add attachment(s)
        this.testReporter = kiteBaseTest.reporter;
    } 
    
Ensuite nous modifions la description:

    stepDescription() {
        return "Check the other videos are being received OK";
    }
    
Puis nous creons la fonction step:

    async step() {
        let result = "";
        let tmp;
        let error = false;
        try {
            for(let i = 1; i < this.numberOfParticipant; i++) {
                tmp = await jitsiPage.videoCheck(this, i);
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
    
La boucle for commencant par 1 permet de parcourir chacune des videos recues. En effet, celle d'index 0 est la video envoyee.

Puis nous ajoutons ce check au fichier Jitsi.js de la meme maniere que le check precedent.

Au debut du fichier: `const {VideoSentCheck, VideoReceivedCheck} = require('./checks');`

testScript():

    this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);
    let openJitsiUrlStep = new OpenJitsiUrlStep(this);
    await openJitsiUrlStep.execute(this);
    let videoSentCheck = new VideoSentCheck(this);
    await videoSentCheck.execute(this);
    // New check
    let videoReceivedCheck = new VideoReceivedCheck(this);
    await videoReceivedCheck.execute(this);
    
Le check permettant de verifier les videos recues est termine. 

*****
# Une fois fixe
### 5. Etape: recuperer les stats
En ce qui concerne l'etape pour recuperer les stats, c'est un peu plus delicat.
Encore une fois, quelque pre-requis:
- Creer le fichier GetStatsStep.js dans le dossier steps
- Ajouter `exports.GetStatsStep = require('./GetStatsStep');` au fichier index.js
- Ajouter au debut du fichier GetStatsStep.js: 
`const {TestUtils, TestStep, } = require('kite-common');` 
& 
`const {jitsiPage} = require('../pages');` 

Ensuite, dans ce fichier, nous allons modifier le constructeur. Ici nous aurons besoin du driver, statsCollectionTime, statsCollectionInterval, statsCollectionInterval et du testReporter pour ecrire les donnees.

    constructor(kiteBaseTest) {
        super();
        this.driver = kiteBaseTest.driver;
        this.statsCollectionTime = kiteBaseTest.statsCollectionTime;
        this.statsCollectionInterval = kiteBaseTest.statsCollectionInterval;
        this.selectedStats = kiteBaseTest.selectedStats;
        // Test reporter if you want to add attachment(s)
        this.testReporter = kiteBaseTest.reporter;
    }
    
Nous modifions ensuite la description:

    stepDescription() {
        return 'Getting WebRTC stats via getStats'; 
    }

Et pour finir, nous modifions la fonction step:

    async step() {
        try {
            let rawStats = await jitsiPage.getStats(this);
            let summaryStats = TestUtils.extractJson(rawStats, 'both');
            // // Data
            this.testReporter.textAttachment(this.report, 'GetStatsRaw', JSON.stringify(rawStats), "json");
            this.testReporter.textAttachment(this.report, 'GetStatsSummary', JSON.stringify(summaryStats), "json");
        } catch (error) {
            console.log(error);
            throw new KiteTestError(Status.BROKEN, "Failed to getStats");
        }
    }
    
La fonction jitsiPage.getStats() est la fonction nous permettant de recuperer les stats. Elle n'est pas encore cree mais c'est ce que nous allons faire juste apres.
La fonction TestUtils.extractJson() permet de faire un resume des donnees recuperer par la fonction precedente.

Maintenant nous allons creer la fonction getSats() dans le fichier jitsi-page.js.
Pour commencer, il faut executer un script pernettant de recuperer la peer connection:
Voici le script:

    const getPeerConnectionScript = function() {
        return "window.peerConnections = [];"
        + "map = APP.conference._room.rtc.peerConnections;"
        + "for(var key of map.keys()){"
        + "  window.peerConnections.push(map.get(key).peerconnection);"
        + "}";
    }
    
Ensuite, dans la fonction getStats():

    getStats: async function(stepInfo) {
        await stepInfo.driver.executeScript(getPeerConnectionScript()); 
        // pc = "window.peerConnections[0]"
        let stats = await TestUtils.getStats(stepInfo, 'kite', "window.peerConnections[0]");
        return stats;
    },
    
La fonction TestUtils.getStats() retourne les statistiques du type donnee de la peer connection.

L'etape permettant de recuperer les statistiques est terminee.
    
*****
### 6. Etape: prendre un screenshot 

On va maintenant ajouter l'etape permettant de prendre un screenshot.
Comme d'habitude quelques pre-requis, il faut:
- Creer le fichier ScreenshotStep.js dans le dossier steps
- Ajouter `exports.ScreenshotStep = require('./ScreenshotStep');` au fichier index.js
- Ajouter `const {ScreenshotStep, TestStep} = require('kite-common');` au debut du fichier ScreenshotStep.js

Ensuite dans ce fichier:
Nous prenons la base pour creer un step et on modifie le constructeur.
Ici, nous aurons besoin uniquement du driver et du testReporter.

    constructor(kiteBaseTest) {
        super();
        this.driver = kiteBaseTest.driver;
        this.testReporter = kiteBaseTest.reporter; //if you want to add attachment(s)
    }
    
Ensuite nous modifons la description:

    stepDescription() {
        return 'Get a screenshot';
    }
    
Ensuite mous modifions la fonction step:

    async step() {
        let screenshot = await TestUtils.takeScreenshot(this.driver);
        this.testReporter.screenshotAttachment(this.report, "Screenshot step", screenshot); 
    }
    
TestUtils contient deja la fonction permettant de prendre un screenshot, il suffit juste de passer en parametre le driver.
Ensuite, nous l'ajoutons au testReporter pour ajouter ce screenshot caux pieces jointes.

ScreenshotStep est terminee.

*****
La creation du test est terminee.
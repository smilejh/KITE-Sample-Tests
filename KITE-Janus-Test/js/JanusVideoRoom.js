const {TestUtils, WebDriverFactory, KiteBaseTest} = require('kite-common'); 
const {JoinUrlStep, ScreenshotStep, GetStatsStep} = require('./steps');
const {FirstVideoCheck, AllVideoCheck} = require('./checks');
const {JanusVideoRoomPage} = require('./pages'); 

// KiteBaseTest config
const globalVariables = TestUtils.getGlobalVariables(process);
const capabilities = require(globalVariables.capabilitiesPath);
const payload = require(globalVariables.payloadPath);

class JanusVideoRoom extends KiteBaseTest {
  constructor(name, globalVariables, capabilities, payload) {
    super(name, globalVariables, capabilities, payload);
    this.page = new JanusVideoRoomPage();
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);
      this.page = new JanusVideoRoomPage(this.driver);

      let joinUrlStep = new JoinUrlStep(this);
      await joinUrlStep.execute(this);

      let firstVideoCheck = new FirstVideoCheck(this);
      await firstVideoCheck.execute(this);

      let allVideoCheck = new AllVideoCheck(this);
      await allVideoCheck.execute(this);

      if (this.getStats) {
        let getStatsStep = new GetStatsStep(this);
        await getStatsStep.execute(this);
      }

      if (this.takeScreenshot) {
        let screenshotStep = new ScreenshotStep(this);
        await screenshotStep.execute(this);
      }
      
    } catch (e) {
      console.log(e);
    } finally {
      await this.driver.quit();
    }
  }
}

module.exports= JanusVideoRoom;

let test = new JanusVideoRoom('VideoRoom test', globalVariables, capabilities, payload);
test.run();

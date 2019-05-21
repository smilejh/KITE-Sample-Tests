const {TestUtils, WebDriverFactory, KiteBaseTest} = require('kite-common'); 
const {JoinUrlStep, ScreenshotStep} = require('./steps');
const {FirstVideoCheck, AllVideoCheck} = require('./checks');
const {JanusVideoCallPage} = require('./pages');

// KiteBaseTest config
const globalVariables = TestUtils.getGlobalVariables(process);
const capabilities = require(globalVariables.capabilitiesPath);
const payload = require(globalVariables.payloadPath);

class JanusVideoCall extends KiteBaseTest {
  constructor(name, globalVariables, capabilities, payload) {
    super(name, globalVariables, capabilities, payload);
    // page
    this.page = new JanusVideoCallPage();
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);

      let joinUrlStep = new JoinUrlStep(this);
      await joinUrlStep.execute(this);

      let firstVideoCheck = new FirstVideoCheck(this);
      await firstVideoCheck.execute(this);

      let allVideoCheck = new AllVideoCheck(this);
      await allVideoCheck.execute(this);

      // Peer connection ?
      // let getStatsStep = new GetStatsStep(this);
      // await getStatsStep.execute(this);

      let screenshotStep = new ScreenshotStep(this);
      await screenshotStep.execute(this);

      await TestUtils.waitAround(5000 * this.numberOfParticipant); // 3s per participant

    } catch (e) {
      console.log(e);
    } finally {
      await this.driver.quit();
    }
  }
}

module.exports= JanusVideoCall;

let test = new JanusVideoCall('VideoCall test', globalVariables, capabilities, payload);
test.run();

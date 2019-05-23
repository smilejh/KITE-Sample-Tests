const {TestUtils, WebDriverFactory, KiteBaseTest} = require('kite-common'); 
const {JoinUrlStep, ScreenshotStep, GetStatsStep} = require('./steps');
const {AllVideoCheck} = require('./checks');
const {JanusStreamingPage} = require('./pages');

// KiteBaseTest config
const globalVariables = TestUtils.getGlobalVariables(process);
const capabilities = require(globalVariables.capabilitiesPath);
const payload = require(globalVariables.payloadPath);

class JanusStreaming extends KiteBaseTest {
  constructor(name, globalVariables, capabilities, payload) {
    super(name, globalVariables, capabilities, payload);
    this.page = new JanusStreamingPage();
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);

      let joinUrlStep = new JoinUrlStep(this);
      await joinUrlStep.execute(this);

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

      await TestUtils.waitAround(3000 * this.numberOfParticipant); // 3s per participant

    } catch (e) {
      console.log(e);
    } finally {
      await this.driver.quit();
    }
  }
}

module.exports= JanusStreaming;

let test = new JanusStreaming('Streaming test', globalVariables, capabilities, payload);
test.run();

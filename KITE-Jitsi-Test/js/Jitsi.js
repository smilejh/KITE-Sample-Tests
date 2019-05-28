const {TestUtils, WebDriverFactory, KiteBaseTest} = require('./node_modules/kite-common'); 
const {OpenJitsiUrlStep, ScreenshotStep, GetStatsStep} = require('./steps');
const {SentVideoCheck, ReceivedVideoCheck} = require('./checks');
const {JitsiPage} = require('./pages');

// KiteBaseTest config
const globalVariables = TestUtils.getGlobalVariables(process);
const capabilities = require(globalVariables.capabilitiesPath);
const payload = require(globalVariables.payloadPath);

class Jitsi extends KiteBaseTest {
  constructor(name, globalVariables, capabilities, payload) {
    super(name, globalVariables, capabilities, payload);
    this.page = new JitsiPage();
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);

      let openJitsiUrlStep = new OpenJitsiUrlStep(this);
      await openJitsiUrlStep.execute(this);

      let sentVideoCheck = new SentVideoCheck(this);
      await sentVideoCheck.execute(this);

      let receivedVideoCheck = new ReceivedVideoCheck(this);
      await receivedVideoCheck.execute(this);

      if (this.getStats) {
        let getStatsStep = new GetStatsStep(this);
        await getStatsStep.execute(this);
      }

      let screenshotStep = new ScreenshotStep(this);
      await screenshotStep.execute(this);

      await TestUtils.waitAround(5000 * this.numberOfParticipant); // 5s per participant
    } catch (e) {
      console.log(e);
    } finally {
      await this.driver.quit();
    }
  }
}

module.exports= Jitsi;

let test = new Jitsi('Jitsi test', globalVariables, capabilities, payload);
test.run();

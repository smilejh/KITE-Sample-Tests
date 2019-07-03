const {TestUtils, WebDriverFactory, KiteBaseTest, ScreenshotStep} = require('kite-common'); 
const {JoinUrlStep} = require('./steps');
const {FirstVideoCheck, AllVideoCheck} = require('./checks');
const {JanusVideoCallPage} = require('./pages');

class JanusVideoCall extends KiteBaseTest {
  constructor(name, kiteConfig) {
    super(name, kiteConfig);
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
      this.page = new JanusVideoCallPage(this.driver);

      let joinUrlStep = new JoinUrlStep(this);
      await joinUrlStep.execute(this);
      await this.waitAllSteps();
      
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

      await this.waitAllSteps();
    } catch (e) {
      console.log(e);
    } finally {
      await this.driver.quit();
    }
  }
}

module.exports= JanusVideoCall;

(async () => {
  const kiteConfig = await TestUtils.getKiteConfig(__dirname);
  let test = new JanusVideoCall('VideoCall test', kiteConfig);
  await test.run();
})();

const {TestUtils, WebDriverFactory, KiteBaseTest, ScreenshotStep} = require('kite-common'); 
const {JoinUrlStep, GetStatsStep} = require('./steps');
const {AllVideoCheck} = require('./checks');
const {JanusStreamingPage} = require('./pages');

class JanusStreaming extends KiteBaseTest {
  constructor(name, kiteConfig) {
    super(name, kiteConfig);
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
      this.page = new JanusStreamingPage(this.driver);

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

      await this.waitAllSteps();
    } catch (e) {
      console.log(e);
    } finally {
      await this.driver.quit();
    }
  }
}

module.exports= JanusStreaming;

(async () => {
  const kiteConfig = await TestUtils.getKiteConfig(__dirname);
  let test = new JanusStreaming('Streaming test', kiteConfig);
  await test.run();
})();

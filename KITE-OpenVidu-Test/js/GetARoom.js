const {TestUtils, WebDriverFactory, KiteBaseTest, ScreenshotStep} = require('kite-common'); 
const {OpenUrlStep} = require('./steps');
const {VideoSentCheck, VideoReceivedCheck} = require('./checks');
const {GetARoomPage} = require('./pages');

class GetARoom extends KiteBaseTest {
  constructor(name, kiteConfig) {
    super(name, kiteConfig);
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
      this.page = new GetARoomPage(this.driver);

      let openUrlStep = new OpenUrlStep(this);
      await openUrlStep.execute(this);

      let videoSentCheck = new VideoSentCheck(this);
      await videoSentCheck.execute(this);

      let videoReceivedCheck = new VideoReceivedCheck(this);
      await videoReceivedCheck.execute(this);

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

module.exports= GetARoom;

(async () => {
  const kiteConfig = await TestUtils.getKiteConfig(__dirname);
  let test = new GetARoom('GetARoom test', kiteConfig);
  await test.run();
})();

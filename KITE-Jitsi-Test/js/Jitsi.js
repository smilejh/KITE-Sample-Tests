const {TestUtils, WebDriverFactory, KiteBaseTest, ScreenshotStep} = require('./node_modules/kite-common'); 
const {OpenJitsiUrlStep, GetStatsStep} = require('./steps');
const {SentVideoCheck, ReceivedVideoCheck} = require('./checks');
const {JitsiPage} = require('./pages');

class Jitsi extends KiteBaseTest {
  constructor(name, kiteConfig) {
    super(name, kiteConfig);
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
      this.page = new JitsiPage(this.driver);

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

module.exports= Jitsi;

(async () => {
  const kiteConfig = await TestUtils.getKiteConfig(__dirname);
  let test = new Jitsi('Jitsi test', kiteConfig);
  await test.run();
})();


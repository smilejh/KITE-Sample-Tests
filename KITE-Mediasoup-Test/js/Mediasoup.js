const {TestUtils, WebDriverFactory, KiteBaseTest, ScreenshotStep} = require('kite-common'); 
const {JoinVideoCallStep, GetStatsStep} = require('./steps');
const {FirstVideoCheck, AllVideoCheck} = require('./checks');
const {MediasoupPage} = require('./pages');

class Mediasoup extends KiteBaseTest {
  constructor(name, kiteConfig) {
    super(name, kiteConfig);
    this.url = this.getRoomUrl(this) + '&username=user' + Array(Math.max(3 - String(this.id).length + 1, 0)).join(0) + this.id;
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
      this.page = new MediasoupPage(this.driver);

      let joinVideoCallStep = new JoinVideoCallStep(this);
      await joinVideoCallStep.execute(this);
      
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

module.exports = Mediasoup;

(async () => {
  const kiteConfig = await TestUtils.getKiteConfig(__dirname);
  let test = new Mediasoup('Mediasoup test', kiteConfig);
  await test.run();
})();

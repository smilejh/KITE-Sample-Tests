const {TestUtils, WebDriverFactory, KiteBaseTest, ScreenshotStep} = require('kite-common'); 
const {LoadPageStep, GetStatsStep, SelectProfileStep} = require('./steps');
const {SenderVideoCheck, ReceivedVideoCheck, GaugesCheck} = require('./checks');
const {MedoozePage} = require('./pages');

const rids = ['a', 'b', 'c'];
const tids = [0, 1, 2];

class Medooze extends KiteBaseTest {
  constructor(name, kiteConfig) {
    super(name, kiteConfig);
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
      this.page = new MedoozePage(this.driver);

      let loadPageStep = new LoadPageStep(this);
      await loadPageStep.execute(this);
      
      let senderVideoCheck = new SenderVideoCheck(this);
      await senderVideoCheck.execute(this);

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

      for(let i = 0; i < rids.length; i++) {
        for(let j = 0; j < tids.length; j++) {
          let selectProfileStep = new SelectProfileStep(this, rids[i], tids[j]);
          await selectProfileStep.execute(this);
          let gaugesCheck = new GaugesCheck(this, rids[i], tids[j]);
          await gaugesCheck.execute(this);
        }
      }
    } catch (e) {
      console.log(e);
    } finally {
      await this.driver.quit();
    }
  }
}

module.exports = Medooze;

(async () => {
  const kiteConfig = await TestUtils.getKiteConfig(__dirname);
  let test = new Medooze('Medooze test', kiteConfig);
  await test.run();
})();

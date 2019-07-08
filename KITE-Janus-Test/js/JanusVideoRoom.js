const {TestUtils, WebDriverFactory, KiteBaseTest, ScreenshotStep} = require('kite-common'); 
const {JoinUrlStep, GetStatsStep} = require('./steps');
const {FirstVideoCheck, AllVideoCheck} = require('./checks');
const {JanusVideoRoomPage} = require('./pages'); 

class JanusVideoRoom extends KiteBaseTest {
  constructor(name, kiteConfig) {
    super(name, kiteConfig);
    this.page = new JanusVideoRoomPage();
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
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
      await this.waitAllSteps();
      await this.page.stopVideo();
    } catch (e) {
      console.log(e);
    } finally {
      await this.driver.quit();
    }
  }
}

module.exports= JanusVideoRoom;

(async () => {
  const kiteConfig = await TestUtils.getKiteConfig(__dirname);
  let test = new JanusVideoRoom('VideoRoom test', kiteConfig);
  await test.run();
})();

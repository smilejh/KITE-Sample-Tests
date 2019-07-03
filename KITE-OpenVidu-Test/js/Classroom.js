const {KiteBaseTest, TestUtils, WebDriverFactory, ScreenshotStep} = require('kite-common');
const {ClassroomPage} = require('./pages');
const {OpenUrlStep, GetStatsStep, StartLessonStep} = require('./steps');
const {VideoSentCheck, VideoReceivedCheck} = require('./checks');

class Classroom extends KiteBaseTest {
  constructor(name, kiteConfig) {
    super(name, kiteConfig);
  }

  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
      this.page = new ClassroomPage(this.driver);

      let openUrlStep = new OpenUrlStep(this);
      await openUrlStep.execute(this);

      console.log("Wait for all steps");
      await this.waitAllSteps();

      let startLessonStep = new StartLessonStep(this);
      await startLessonStep.execute(this);

      await this.waitAllSteps();

      let videoCheck; 
      this.numberOfParticipant--; // Only one video
      if (this.id % 2 === 0) {
        videoCheck = new VideoSentCheck(this);
      } else {
        videoCheck = new VideoReceivedCheck(this);
      }
      await videoCheck.execute(this);
      this.numberOfParticipant++;

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

(async () => {
  const kiteConfig = await TestUtils.getKiteConfig(__dirname);
  let test = new Classroom("Classroom", kiteConfig);
  await test.run();
})();
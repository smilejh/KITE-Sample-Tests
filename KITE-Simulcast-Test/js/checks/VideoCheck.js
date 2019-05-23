const {TestStep, Status, KiteTestError} = require('kite-common');

/**
 * Class: VideoCheck
 * Extends: TestStep
 * Description:
 */
class VideoCheck extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.timeout = kiteBaseTest.timeout;
    this.numberOfParticipant = 2; // 2 videos
    this.page = kiteBaseTest.page;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return "Check the first video";
  }

  async step(direction) {
    try {
      let sentResult = await this.page.videoCheck(this, direction); 
      if (sentResult != 'video') {
        this.testReporter.textAttachment(this.report, direction + " video", sentResult, "plain");
        throw new KiteTestError(Status.FAILED, 'The ' + direction + ' video is ' + sentResult);
      } else {
        console.log(direction + ' video is OK');
      }
    } catch (error) {
      console.log(error);
      if (error instanceof KiteTestError) {
        throw error;
      } else {
        throw new KiteTestError(Status.BROKEN, "Error looking for the video");
      }
    }
  }
}

module.exports = VideoCheck;
const {TestStep, KiteTestError, Status} = require('kite-common');

/**
 * Class: FirstVideoCheck
 * Extends: TestStep
 * Description:
 */
class FirstVideoCheck extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.numberOfParticipant = kiteBaseTest.numberOfParticipant;
    this.timeout = kiteBaseTest.timeout;
    this.page = kiteBaseTest.page;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return "Check the first video is being sent OK";
  }

  async step() {
    try {
    
      //first video is the fullscreen
      //second video is "display": "none"
      //third video is "You" (the publisher)
      const FIRST_VIDEO_INDEX = this.page.getVideos().length > 1 ? 2 : 0;
      let result = await this.page.videoCheck(this, FIRST_VIDEO_INDEX);
      if (result != 'video') {
        this.testReporter.textAttachment(this.report, "Sent video", result, "plain");
        throw new KiteTestError(Status.FAILED, "The first video is " + result);
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

module.exports = FirstVideoCheck;
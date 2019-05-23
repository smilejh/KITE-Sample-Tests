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
      let result = await this.page.videoCheck(this, 0);
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
const {TestStep, Status, KiteTestError} = require('kite-common');
const {JanusStreamingPage, JanusVideoRoomPage} = require('../pages');

const executeStep = async function(stepInfo) {
  let start = 1;
  if (stepInfo.page instanceof JanusStreamingPage) {
    // Only 1 video received with only 1 participant
    start = 0;
  }
  let result = "";
  let tmp;
  let error = false;
  for(i = start; i < stepInfo.numberOfParticipant; i++) {
    tmp = await stepInfo.page.videoCheck(stepInfo, i);
    result += tmp;
    if (i < stepInfo.numberOfParticipant) {
      result += ' | ';
    }
    if (tmp != 'video') {
      error = true;
    }
  }

  if (error) {
    stepInfo.testReporter.textAttachment(stepInfo.report, "Received videos", result, "plain");
    throw new KiteTestError(Status.FAILED, "Some videos are still or blank: " + result);
  }
}

class AllVideoCheck extends TestStep {
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
    return "Check the other videos are being received OK";
  }

  async step() {
    try {
      await executeStep(this);

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

module.exports = AllVideoCheck;
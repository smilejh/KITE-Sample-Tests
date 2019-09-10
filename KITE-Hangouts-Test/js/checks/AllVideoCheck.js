const {TestStep, Status, KiteTestError} = require('kite-common');

const executeStep = async function(stepInfo) {
  //first video is the fullscreen
  //second video is "display": "none"
  //third video is "You" (the publisher)
  const noVideos = stepInfo.page.getVideos().length;
  const FIRST_VIDEO_INDEX = noVideos > 1 ? 2 : 0;
  let result = "";
  let tmp;
  let error = false;
  for(i = FIRST_VIDEO_INDEX + 1; i < noVideos; i++) {
    let tmp = await stepInfo.page.videoCheck(stepInfo, i);
    console.log('received video[' + i + '] ' + tmp);
    result += tmp;
    if (i < noVideos - 1) {
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
    this.usersPerRoom = kiteBaseTest.payload.usersPerRoom;

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
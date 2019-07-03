const {TestStep, KiteTestError, Status} = require('kite-common');
const {VideoConferencePage, WebinarPage, ClassroomPage} = require('../pages');

const executeStep = async function(stepInfo) {
  let result = "";
  let tmp;
  let error = false;
  if (stepInfo.page instanceof VideoConferencePage || stepInfo.page instanceof WebinarPage) {
    let videosId = await stepInfo.page.getVideos(stepInfo);
    for(let i = 0; i < stepInfo.numberOfParticipant-1; i++) {
      if (videosId[i].includes("remote")) {
        tmp = await stepInfo.page.videoCheck(stepInfo, i);
        result += tmp;
        if (i < stepInfo.numberOfParticipant) {
          result += ' | ';
        }
        if (tmp != 'video') {
          error = true;
        }
      }
    }
  } else if (stepInfo.page instanceof ClassroomPage) {
    tmp = await stepInfo.page.videoCheck(stepInfo, 0); // 0 is the index of received video for the student
    result += tmp;
    if (tmp != 'video') {
      error = true;
    }
  } else {
    for(let i = 1; i < stepInfo.numberOfParticipant; i++) {
      tmp = await stepInfo.page.videoCheck(stepInfo, i);
      result += tmp;
      if (i < stepInfo.numberOfParticipant) {
        result += ' | ';
      }
      if (tmp != 'video') {
        error = true;
      }
    }
  }
  if (error) {
    stepInfo.testReporter.textAttachment(stepInfo.report, "Received videos", result, "plain");
    throw new KiteTestError(Status.FAILED, "Some videos are still or blank: " + result);
  }
  return result;
}

class VideoReceivedCheck extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.timeout = kiteBaseTest.timeout;
    this.numberOfParticipant = kiteBaseTest.numberOfParticipant;
    this.page = kiteBaseTest.page;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return "Check the other videos are being received OK";
  }

  async step() {
    try {
      // By index
      let result = await executeStep(this);
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

module.exports = VideoReceivedCheck;
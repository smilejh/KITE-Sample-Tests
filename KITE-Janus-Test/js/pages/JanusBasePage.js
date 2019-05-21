const {By} = require('selenium-webdriver');
const {TestUtils, KiteTestError, Status} = require('kite-common'); 
const waitAround = TestUtils.waitAround;
const verifyVideoDisplayByIndex = TestUtils.verifyVideoDisplayByIndex;

const startButton = By.id('start');
const videoElements = By.css('video');

class JanusBasePage {
  constructor() {
    this.startButton = startButton;
    this.videos = videoElements;
  }

  async open(stepInfo) {
    await TestUtils.open(stepInfo);
  }

  // VideoCheck with verifyVideoDisplayByIndex
  async videoCheck(stepInfo, index) {
    let videos = [];
    let i = 0;
    let timeout = stepInfo.timeout;

    // Waiting for the videos
    while (videos.length < stepInfo.numberOfParticpant && i < timeout) {
      videos = await stepInfo.driver.findElements(this.videos);
      i++;
      await waitAround(1000); // waiting 1s after each iteration
    }

    // Make sure that it has not timed out
    if (i === timeout) {
      throw new KiteTestError(Status.FAILED, "unable to find " +
        stepInfo.numberOfParticpant + " <video> element on the page. Number of video found = " +
        videos.length);
    }

    // Check the status of the video
    // checked.result = 'blank' || 'still' || 'video'
    i = 0;
    let checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
    while(checked.result === 'blank' || checked.result === undefined && i < timeout) {
      checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
      i++;
      await waitAround(1000);
    }

    i = 0;
    while(i < 3 && checked.result != 'video') {
      checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
      i++;
      await waitAround(3 * 1000); // waiting 3s after each iteration
    }
    return checked.result;
  }
}

module.exports = JanusBasePage;
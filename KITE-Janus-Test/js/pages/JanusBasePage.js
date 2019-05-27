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
    let timeout = stepInfo.timeout;

    // Waiting for the videos
    await TestUtils.waitVideos(stepInfo, this.videos);

    // Check the status of the video
    // checked.result = 'blank' || 'still' || 'video'
    let i = 0;
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
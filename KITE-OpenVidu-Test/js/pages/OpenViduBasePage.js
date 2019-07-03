const {By, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');
const waitAround = TestUtils.waitAround;
const verifyVideoDisplayByIndex = TestUtils.verifyVideoDisplayByIndex;

const videoElements = By.css('video');

class OpenViduBasePage {
  constructor(driver) {
    this.driver = driver;
    this.videos = videoElements;
  }

  async open(stepInfo) {
    await TestUtils.open(stepInfo);
  }

  async videoCheck(stepInfo, index) {
    let checked; // Result of the verification
    let i = 0;
    let timeout = stepInfo.timeout;

    // Waiting for all the videos
    await TestUtils.waitForVideos(stepInfo, this.videos);

    // Check the status of the video
    // checked.result = 'blank' || 'still' || 'video'
    checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
    while(checked.result === 'blank' && i < timeout) {
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

  async getVideos(stepInfo) {
    let ids = [];
    let videos;
    let i = 0;
    let timeout = stepInfo.timeout;
    let numberOfParticipant = stepInfo.numberOfParticipant;
  
    // Getting all the videos
    while (ids.length < numberOfParticipant && i < timeout) {
      videos = await stepInfo.driver.findElements(videoElements);
      ids = await promise.map(videos, e => e.getAttribute('id'));
      i++;
      await waitAround(1000); // waiting 1s after each iteration
    }
    // Make sure that it has not timed out
    if (i === timeout) {
      throw new KiteTestError(Status.FAILED, "unable to find " +
        numberOfParticipant + " <video> element on the page. Number of video found = " +
        ids.length);
    }

    ids = ids.splice(1, ids.length); // deleting the video which does not have id
    return ids;
  }
}

module.exports = OpenViduBasePage;
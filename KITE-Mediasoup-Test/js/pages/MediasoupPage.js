const {By} = require('selenium-webdriver');
const {TestUtils, Status, KiteTestError} = require('kite-common'); 
const waitAround = TestUtils.waitAround;
const verifyVideoDisplayByIndex = TestUtils.verifyVideoDisplayByIndex;

// Elements
const videoElements = By.css('video'); 

class MediasoupPage {
  constructor() {}

  async open(stepInfo) {
    await TestUtils.open(stepInfo);
  }

  // VideoCheck with verifyVideoDisplayByIndex
  async videoCheck(stepInfo, index) {
    let videos = [];
    let i = 0;
    let timeout = stepInfo.timeout;

    while (videos.length < stepInfo.numberOfParticipant && i < timeout) {
      videos = await stepInfo.driver.findElements(videoElements);
      i++;
      await waitAround(1000);
    }

    if (i === timeout) {
      throw new KiteTestError(Status.FAILED, "Unable to find " 
        + stepInfo.numberOfParticipant 
        + " <video> element on the page. Number of video found = " 
        + videos.length);
    }

    let checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
    i = 0;
    checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
    while(checked.result === 'blank' || checked.result === undefined && i < timeout) {
      checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
      i++;
      await waitAround(1000);
    }

    i = 0;
    while(i < 3 && checked.result != 'video') {
      checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
      i++;
      await waitAround(3 * 1000);
    }
    return checked.result;
  }
}

module.exports = MediasoupPage;


const {By} = require('selenium-webdriver');
const {TestUtils, KiteTestError, Status} = require('kite-common'); 
const waitAround = TestUtils.waitAround;
const verifyVideoDisplayByIndex = TestUtils.verifyVideoDisplayByIndex;

const elements = {
  publishingLocator: By.xpath("//b[text()='Publishing...']"),
  video: {
    type: 'tagName',
    value: 'video',
  },
  videos: By.css('video'),
};


module.exports = {

  open: async function(stepInfo) {
    await TestUtils.open(stepInfo);
  },

  verifyVideo: async function(stepInfo, index) {
    let videos = [];
    let i = 0;
    let timeout = stepInfo.timeout / 1000;

    while (videos.length < stepInfo.numberOfParticipant && i < timeout) {
      videos = await stepInfo.driver.findElements(elements.videos);
      i++;
      await waitAround(1000);
    }

    if (i === timeout) {
      throw new KiteTestError(Status.FAILED, "Unable to find " + stepInfo.numberOfParticipant + " <video> element on the page. No video found = " + videos.length);
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
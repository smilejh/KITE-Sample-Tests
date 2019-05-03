const {By, until, promise} = require('selenium-webdriver');
const {TestUtils, KiteTestError, Status} = require('kite-common'); 
const waitAround = TestUtils.waitAround;
const map = promise.map;

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
    await stepInfo.driver.get(stepInfo.url);
    await TestUtils.waitForPage(stepInfo.driver, stepInfo.timeout);
  },

  verifyVideo: async function(stepInfo, index) {
    /*
    if (index === 0) {
      await stepInfo.driver.wait(until.elementLocated(elements.publishingLocator));
    } */

    // wait for videos 
    await TestUtils.waitAround(stepInfo.numberOfParticipant * 5 * 1000);

    let videos;
    let ids = [];
    let i = 0;
    await TestUtils.waitForElement(stepInfo.driver, elements.video.type, elements.video.value, stepInfo.timeout);
    while (ids.length < stepInfo.numberOfParticipant && i < stepInfo.timeout / 1000) {
      videos = await stepInfo.driver.findElements(elements.videos);
      ids = await map(videos, e => e.getAttribute("id"));
      i++;
      console.log(ids);
      await waitAround(1000);
    }

    if (i === stepInfo.timeout) {
      throw new KiteTestError(Status.FAILED, "Unable to find " + stepInfo.numberOfParticipants + " <video> element on the page. No video found = " + ids.length);
    }

    let checked = await TestUtils.verifyVideoDisplayById(stepInfo.driver, ids[index]);
    
    i = 0;
    while(i < 3 && checked.result != 'video') {
      checked = await TestUtils.verifyVideoDisplayById(stepInfo.driver, ids[index]);
      i++;
      await waitAround(3 * 1000);
    }
    return checked.result;
  }
}
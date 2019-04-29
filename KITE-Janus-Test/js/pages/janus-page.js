const {By, until, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common'); 
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

  open: async function(driver, url, timeout) {
    await driver.get(url);
    await TestUtils.waitForPage(driver, timeout);
  },

  verifyVideo: async function(driver, numberOfParticipant, timeout) {
    await driver.wait(until.elementLocated(elements.publishingLocator));
    //wait a while to allow all videos to load.
    await TestUtils.waitAround(numberOfParticipant * 10 * 1000);
    await TestUtils.waitForElement(driver, elements.video.type, elements.video.value, timeout);
    let videos = await driver.findElements(elements.videos);
    let details = {};
    let ids = await map(videos, e => e.getAttribute("id"));
    console.log(ids);
    for(let i = 0; i < ids.length; i++) {
      if(ids[i] != undefined) {
        const videoCheck =  await TestUtils.verifyVideoDisplayById(driver, ids[i]);
        details['videoCheck_' + ids[i]] = videoCheck;
      } else {
        details['videoCheck_' + ids[i]] = {};
      }

    }
    console.log(details);
    return details;
  }
}
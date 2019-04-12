const {By} = require('selenium-webdriver');
const {TestUtils} = require('kite-common'); 

module.exports = {

  video: {
    type: 'tagName',
    value: 'video'
  },

  // Elements
  elements: {
    publishingLocator: By.xpath("//b[text()='Publishing...']"),
    video: By.tagName('video')
  },

  // Wait for video element then find them
  getVideoElements: async function(driver, timeout) {
    await TestUtils.waitForElement(driver, this.video.type, this.video.value, timeout);
    let videos = await driver.findElements(this.elements.video);
    return videos;
  }

}
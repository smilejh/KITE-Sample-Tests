const OpenViduBasePage = require('./OpenViduBasePage');
const {By, Key, until} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');

// Elements
const roomInput = By.css('input');
const joinButton = By.id('joinButton');

class CallPage extends OpenViduBasePage {
  constructor(driver) {
    super(driver);
  }

  async joinSession(sessionId) {
    // const sessionId = session + stepInfo.uuid;
    let room = await this.driver.findElement(roomInput);
    await room.sendKeys(sessionId);
    await room.sendKeys(Key.ENTER);
    await this.driver.wait(until.elementLocated(joinButton) || 6000);
    // await TestUtils.waitAround(5000);
    let join = await this.driver.findElement(joinButton);
    await join.click();
    // await TestUtils.waitForPage(stepInfo.driver, stepInfo.timeout);
  }

}

module.exports = CallPage;
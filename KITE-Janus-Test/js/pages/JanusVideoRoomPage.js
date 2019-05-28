const JanusBasepage = require('./JanusBasePage');
const {By, Key} = require('selenium-webdriver');
const {TestUtils} = require('kite-common'); 
const waitAround = TestUtils.waitAround;

// Elements
const usernameInput = By.id('username');

class JanusVideoRoomPage extends JanusBasepage {
  constructor(driver) {
    super(driver);
  }

  async joinSession(sessionId) {
    let start = await this.driver.findElement(this.startButton);
    await start.click();
    await waitAround(2000) // wait for element to be visible
    let username = await this.driver.findElement(usernameInput);
    await username.sendKeys(sessionId);
    await username.sendKeys(Key.ENTER);

    await waitAround(5000);
  }

  // This will stop the video broadcasting properly
  async stopVideo() {
    await waitAround(5000); // waiting for checks to be done
    // stop button and start button are the same
    let stop = await this.driver.findElement(this.startButton);
    await stop.click();
  }
}

module.exports = JanusVideoRoomPage;
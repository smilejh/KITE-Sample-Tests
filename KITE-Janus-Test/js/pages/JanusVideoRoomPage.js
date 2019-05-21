const JanusBasepage = require('./JanusBasePage');
const {By, Key} = require('selenium-webdriver');
const {TestUtils} = require('kite-common'); 
const waitAround = TestUtils.waitAround;

// Elements
const usernameInput = By.id('username');

class JanusVideoRoomPage extends JanusBasepage {
  constructor() {
    super();
  }

  async joinSession(stepInfo, session) {
    const sessionId = session + stepInfo.uuid + stepInfo.id;
    let start = await stepInfo.driver.findElement(this.startButton);
    await start.click();
    await waitAround(2000) // wait for element to be visible
    let username = await stepInfo.driver.findElement(usernameInput);
    await username.sendKeys(sessionId);
    await username.sendKeys(Key.ENTER);

    await waitAround(5000);
  }

  // This will stop the video broadcasting properly
  async stopVideo(stepInfo) {
    await waitAround(5000); // waiting for checks to be done
    // stop button and start button are the same
    let stop = await stepInfo.driver.findElement(this.startButton);
    await stop.click();
  }
}

module.exports = JanusVideoRoomPage;
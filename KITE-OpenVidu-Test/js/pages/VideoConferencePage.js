const OpenViduBasePage = require('./OpenViduBasePage');
const {By, Key} = require('selenium-webdriver');

// Element
const meetingRoom = By.id('sessionId');

class VideoConferencePage extends OpenViduBasePage {
  constructor(driver) {
    super(driver);
  }

  async joinSession(sessionId) {
    let meeting = await this.driver.findElement(meetingRoom);
    await meeting.clear(); // Clean the field
    await meeting.sendKeys(sessionId); // Fill out the session field and add some random numbers
    await meeting.sendKeys(Key.ENTER); // Press ENTER to enter in the room
  }
}

module.exports = VideoConferencePage;
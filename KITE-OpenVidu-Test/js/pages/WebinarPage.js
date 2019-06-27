const OpenViduBasePage = require('./OpenViduBasePage');
const {By, Key} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');

// Elements
const sessionRoom = By.id('sessionName');
const userInput = By.id('user');
const passInput = By.id('pass');

const username = 'publisher1';
const password = 'pass';

class WebinarPage extends OpenViduBasePage {
  constructor(driver) {
    super(driver);
  }

  async joinSession(sessionId) {
    let user = await this.driver.findElement(userInput);
    let pass = await this.driver.findElement(passInput);
    await user.sendKeys(username);
    await pass.sendKeys(password);
    await pass.sendKeys(Key.ENTER);
    await TestUtils.waitAround(2000);
    let room = await this.driver.findElement(sessionRoom);
    await room.clear(); // Clean the field
    await room.sendKeys(sessionId); // Fill out the session field and add some random numbers
    await room.sendKeys(Key.ENTER); // Press ENTER to enter in the room
  }

}

module.exports = WebinarPage;
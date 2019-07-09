const OpenViduBasePage = require('./OpenViduBasePage');
const {TestUtils} = require('kite-common');
const {By, Key} = require('selenium-webdriver');

// Elements
const inputEmail = By.id("email");
const inputPassword = By.id("password");
const matIcons = By.className("mat-icon");
const joinButton = By.id("join-btn");

// Fake mails & password
const emails = ["CosmoTeacher@gmail.com","CosmoStudent@gmail.com"];
const password = "Cosmo";

class ClassroomPage extends OpenViduBasePage {
  constructor(driver) {
    super(driver);
  }

  async joinSession(id) {
    let email = await this.driver.findElement(inputEmail);
    let pass = await this.driver.findElement(inputPassword);
    await email.sendKeys(emails[parseInt(id) % 2]);
    await pass.sendKeys(password);
    await pass.sendKeys(Key.ENTER);
    await TestUtils.waitAround(3000); // Wait 3 sec to connect 
  }

  async startLesson(id) {
    let icons = await this.driver.findElements(matIcons);
    let startButton;
    if (id % 2 === 0) {
      startButton = icons[3];
      await startButton.click();
      await TestUtils.waitAround(2000);
      let join = await this.driver.findElement(joinButton);
      await join.click();
    } else {
      startButton = icons[1];
      await TestUtils.waitAround(4000); // Wait for the lesson to be started
      await startButton.click();
    }
  }
}

module.exports = ClassroomPage;
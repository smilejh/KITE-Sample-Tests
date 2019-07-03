const JanusBasepage = require('./JanusBasePage');
const {By, Key, until} = require('selenium-webdriver');
const {TestUtils} = require('kite-common'); 
const waitAround = TestUtils.waitAround;

// Elements
const usernameInput = By.id('username');
const peerInput = By.id('peer');
const answerButton = By.className("btn btn-success");
const closeButton = By.className("bootbox-close-button close");
const bodyModal =  By.className("bootbox-body");
const modalWaitingText = "Waiting for the peer to answer...";

const call = async function(stepInfo) {
  let running = true;
  let i = 0;
  do {
    let peer = await stepInfo.driver.findElement(peerInput);
    await peer.sendKeys(stepInfo.sessionId + parseInt((stepInfo.id + 1)));
    await peer.sendKeys(Key.ENTER);
    await stepInfo.driver.wait(until.elementLocated(bodyModal));
    let txt = await stepInfo.driver.findElements(bodyModal); // Array of elements
    let modalText = await txt[0].getText();
    if (typeof modalText !== "undefined" && modalText.includes(modalWaitingText)) {
      running = false;
    } else {
      await stepInfo.driver.wait(until.elementLocated(closeButton));
      let close = await stepInfo.driver.findElement(closeButton);
      await close.click();
      await waitAround(2000);
    }
    i++;
  } while(running && i < stepInfo.timeout);
}

const receive = async function(driver, timeout) {
  let answer = await driver.findElements(answerButton);
  let i = 0;
  // answer[2] is a modal button that permites to answer the call
  while(typeof answer[2] === "undefined" && i < timeout) {
    answer = await driver.findElements(answerButton);
    await waitAround(2000);
    i++;
  }
  await answer[2].click();
}

class JanusVideoCallPage extends JanusBasepage {
  constructor(driver) {
    super(driver);
  }

  async joinSession(stepInfo, session) {
    stepInfo.sessionId = session + stepInfo.uuid;
    let start = await stepInfo.driver.findElement(this.startButton);
    await start.click();
    await waitAround(2000) // wait for element to display
    let username = await stepInfo.driver.findElement(usernameInput);
    await username.sendKeys(stepInfo.sessionId + stepInfo.id);
    await username.sendKeys(Key.ENTER);
    await waitAround(2000); // wait for element to display
    // The browser with an even id is the caller and the other the receiver 
    if (stepInfo.id %2 === 0) {
      await call(stepInfo);
    } else {
      await receive(stepInfo.driver, stepInfo.timeout);
    }
  }
}

module.exports = JanusVideoCallPage;
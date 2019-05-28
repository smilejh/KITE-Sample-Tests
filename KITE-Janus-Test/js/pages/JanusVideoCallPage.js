const JanusBasepage = require('./JanusBasePage');
const {By, Key} = require('selenium-webdriver');
const {TestUtils} = require('kite-common'); 
const waitAround = TestUtils.waitAround;

// Elements
const usernameInput = By.id('username');
const peerInput = By.id('peer');
const answerButton = By.className("btn btn-success");
const btnElements = By.className("btn");
const closeButton = By.className("bootbox-close-button close");

const call = async function(stepInfo) {
  let peer = await stepInfo.driver.findElement(peerInput);
  await peer.sendKeys(stepInfo.sessionId + parseInt((stepInfo.id + 1)));
  await peer.sendKeys(Key.ENTER);
  await waitAround(5000); // wait for modal to display
  
  let btns = await stepInfo.driver.findElements(btnElements);
  // If there are more than 6 buttons, it means
  // there is a modal which indicates the receiver doesn't exist
  if(btns.length > 6) {
    do {
      // To close the modal
      let close = await stepInfo.driver.findElement(closeButton);
      await close.click();

      await waitAround(3000); // wait for element to display
      peer = await stepInfo.driver.findElement(peerInput);
      await peer.clear();
      await peer.sendKeys(stepInfo.sessionId + parseInt(stepInfo.id + 1));
      await peer.sendKeys(Key.ENTER);

      await waitAround(5000); // wait for buttons to display
      
      btns = await stepInfo.driver.findElements(btnElements);
    } while(btns.length > 6);
  }
}

const receive = async function(driver) {
  let answer = await driver.findElements(answerButton);
  // answer[2] is a modal button that permites to answer the call
  while(answer[2] === undefined) {
    answer = await driver.findElements(answerButton);
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
      await receive(stepInfo.driver);
    }
  }
}

module.exports = JanusVideoCallPage;
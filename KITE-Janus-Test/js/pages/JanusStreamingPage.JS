const JanusBasepage = require('./JanusBasePage');
const {By} = require('selenium-webdriver');
const {TestUtils} = require('kite-common'); 

// Elements
const streamList = By.id('streamset');
const id = By.id('1');
const watchButton = By.id('watch');

class JanusStreamingpage extends JanusBasepage {
  constructor() {
    super();
  }

  async joinSession(stepInfo) {
    let start = await stepInfo.driver.findElement(this.startButton);
    await start.click();
    await TestUtils.waitAround(2000); // wait for element to display
    let list = await stepInfo.driver.findElement(streamList);
    await list.click();
    let idList = await stepInfo.driver.findElement(id);
    await idList.click();
    let watch = await stepInfo.driver.findElement(watchButton);
    await watch.click();
    await TestUtils.waitAround(5000);
  }
}

module.exports = JanusStreamingpage;
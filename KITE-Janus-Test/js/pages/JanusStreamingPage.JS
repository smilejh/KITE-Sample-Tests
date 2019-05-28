const JanusBasepage = require('./JanusBasePage');
const {By} = require('selenium-webdriver');
const {TestUtils} = require('kite-common'); 

// Elements
const streamList = By.id('streamset');
const id = By.id('1');
const watchButton = By.id('watch');

class JanusStreamingpage extends JanusBasepage {
  constructor(driver) {
    super(driver);
  }

  async joinSession() {
    let start = await this.driver.findElement(this.startButton);
    await start.click();
    await TestUtils.waitAround(2000); // wait for element to display
    let list = await this.driver.findElement(streamList);
    await list.click();
    let idList = await this.driver.findElement(id);
    await idList.click();
    let watch = await this.driver.findElement(watchButton);
    await watch.click();
    await TestUtils.waitAround(5000);
  }
}

module.exports = JanusStreamingpage;
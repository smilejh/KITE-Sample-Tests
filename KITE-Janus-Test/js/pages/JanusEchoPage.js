const JanusBasepage = require('./JanusBasePage');

class JanusEchoPage extends JanusBasepage {
  constructor(driver) {
    super(driver);
  }

  async joinSession() {
    let start = await this.driver.findElement(this.startButton);
    await start.click();
  }
}

module.exports = JanusEchoPage;
const JanusBasepage = require('./JanusBasePage');

class JanusEchoPage extends JanusBasepage {
  constructor() {
    super();
  }

  async joinSession(stepInfo) {
    let start = await stepInfo.driver.findElement(this.startButton);
    await start.click();
  }
}

module.exports = JanusEchoPage;
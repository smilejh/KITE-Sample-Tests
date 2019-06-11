const OpenViduBasePage = require('./OpenViduBasePage');
const {By} = require('selenium-webdriver');

class GetARoomPage extends OpenViduBasePage {
  constructor(driver) {
    super(driver);
  }

  async open(stepInfo) {
    stepInfo.url += stepInfo.uuid;
    super.open(stepInfo);
  }

}

module.exports = GetARoomPage;
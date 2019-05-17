const {TestStep} = require('kite-common');
const {janusPage} = require('../pages')
/**
 * Class: JoinVideoCallStep
 * Extends: TestStep
 * Description:
 */
class JoinVideoCallStep extends TestStep {
  constructor(kiteBaseTest, url) {
    super();
    this.driver = kiteBaseTest.driver;
    this.url = url;
    this.timeout = kiteBaseTest.timeout;
  }

  stepDescription() {
    return 'Open ' + this.url;
  }

  async step() {
    await janusPage.open(this);
  }
}

module.exports = JoinVideoCallStep;
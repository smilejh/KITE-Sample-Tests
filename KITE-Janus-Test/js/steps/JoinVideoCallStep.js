const {TestUtils, TestStep} = require('kite-common');

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
    return 'Open ' + this.url + ' and check the video';
  }

  async step() {
    await TestUtils.open(this);
  }
}

module.exports = JoinVideoCallStep;
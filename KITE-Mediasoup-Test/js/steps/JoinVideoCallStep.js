const {TestStep} = require('kite-common');

/**
 * Class: JoinVideoCallStep
 * Extends: TestStep
 * Description:
 */
class JoinVideoCallStep extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.timeout = kiteBaseTest.timeout;
    this.url = kiteBaseTest.url;
    this.uuid = kiteBaseTest.uuid;
    this.page = kiteBaseTest.page;
  }

  stepDescription() {
    return 'Open ' + this.url + ' wait for page to load';
  }

  async step() {
    await this.page.open(this);
  }
}

module.exports = JoinVideoCallStep;
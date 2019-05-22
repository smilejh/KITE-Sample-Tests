const {TestStep} = require('kite-common');
const {mediasoupPage}  = require('../pages');

/**
 * Class: JoinVideoCallStep
 * Extends: TestStep
 * Description:
 */
class JoinVideoCallStep extends TestStep {
  constructor(kiteBaseTest, url) {
    super();
    this.driver = kiteBaseTest.driver;
    this.timeout = kiteBaseTest.timeout;
    this.url = url;
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
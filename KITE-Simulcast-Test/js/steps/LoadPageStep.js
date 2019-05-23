const {TestStep} = require('kite-common');

/**
 * Class: LoadPageStep
 * Extends: TestStep
 * Description:
 */
class LoadPageStep extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.timeout = kiteBaseTest.timeout;
    this.url = kiteBaseTest.url;
    this.page = kiteBaseTest.page;
  }

  stepDescription() {
    return 'Open ' + this.url;
  }

  async step() {
    await this.page.open(this);
  }
}

module.exports = LoadPageStep;
const {TestUtils, TestStep} = require('kite-common');
const {medoozePage} = require('../pages');
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
  }

  stepDescription() {
    return 'Open ' + this.url;
  }

  async step() {
    await medoozePage.open(this);
  }
}

module.exports = LoadPageStep;
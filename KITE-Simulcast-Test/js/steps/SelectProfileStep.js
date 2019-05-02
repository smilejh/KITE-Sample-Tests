const {TestStep} = require('kite-common');
const {medoozePage} = require('../pages');

/**
 * Class: SelectProfileStep
 * Extends: TestStep
 * Description:
 */
class SelectProfileStep extends TestStep {
  constructor(kiteBaseTest, rid, tid) {
    super();
    this.driver = kiteBaseTest.driver;
    this.rid = rid;
    this.tid = tid;
    this.statsCollectionInterval = kiteBaseTest.statsCollectionInterval;
  }

  stepDescription() {
    return 'Choose a profile id gauge : ' + this.rid + ' ' + this.tid;
  }

  async step() {
    await medoozePage.selectProfile(this);
  }
}

module.exports = SelectProfileStep;
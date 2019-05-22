const {TestStep} = require('kite-common');

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
    this.page = kiteBaseTest.page;
  }

  stepDescription() {
    return 'Choose a profile id gauge : ' + this.rid + ' ' + this.tid;
  }

  async step() {
    await this.page.selectProfile(this);
  }
}

module.exports = SelectProfileStep;
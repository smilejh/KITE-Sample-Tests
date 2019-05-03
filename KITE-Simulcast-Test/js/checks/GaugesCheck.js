const {TestUtils, TestStep} = require('kite-common');
const {medoozePage} = require('../pages');
/**
 * Class: GaugesCheck
 * Extends: TestStep
 * Description:
 */
class GaugesCheck extends TestStep {
  constructor(kiteBaseTest, rid, tid) {
    super();
    this.driver = kiteBaseTest.driver;
    this.rid = rid;
    this.tid = tid;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return 'Gauges values for profile ' + this.rid + this.tid;
  }

  async step() {

    let loopBackStats = await medoozePage.getLoopbackStats(this);

    // Data
    this.testReporter.textAttachment(this.report, 'stats', JSON.stringify(loopBackStats), "json");

    let screenshot = await TestUtils.takeScreenshot(this.driver);
    this.testReporter.screenshotAttachment(this.report, "Screenshot step", screenshot); 
  }
}

module.exports = GaugesCheck;
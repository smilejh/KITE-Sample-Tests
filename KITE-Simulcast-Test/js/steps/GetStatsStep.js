const {TestUtils, TestStep, Status, KiteTestError} = require('kite-common');

/**
 * Class: GetStatsStep
 * Extends: TestStep
 * Description:
 */
class GetStatsStep extends TestStep {
  constructor(kiteBaseTest, pc) {
    super();
    this.driver = kiteBaseTest.driver;
    this.statsCollectionTime = kiteBaseTest.statsCollectionTime;
    this.statsCollectionInterval = kiteBaseTest.statsCollectionInterval;
    this.selectedStats = kiteBaseTest.selectedStats;
    this.pc = pc;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return 'Getting WebRTC stats via getStats';
  }

  async step() {
    try {
    let receivedStats = await TestUtils.getStats(this, 'kite', this.pc);

    // Data
    this.testReporter.textAttachment(this.report, 'getStatsRaw', JSON.stringify(receivedStats, null, 4), "json");
    
    } catch (error) {
      console.log(error);
      throw new KiteTestError(Status.BROKEN, "Failed to getStats");
    }
  }
}

module.exports = GetStatsStep;
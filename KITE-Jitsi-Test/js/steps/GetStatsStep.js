const {TestUtils, TestStep, Status, KiteTestError} = require('kite-common');

class GetStatsStep extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.statsCollectionTime = kiteBaseTest.statsCollectionTime;
    this.statsCollectionInterval = kiteBaseTest.statsCollectionInterval;
    this.selectedStats = kiteBaseTest.selectedStats;
    this.page = kiteBaseTest.page;
    this.peerConnections = kiteBaseTest.peerConnections;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return 'Getting WebRTC stats via getStats';
  }

  async step() {
    try {
    let rawStats = await this.page.getStats(this);
    let summaryStats = TestUtils.extractJson(rawStats, 'both');
    // // Data
    this.testReporter.textAttachment(this.report, 'GetStatsRaw', JSON.stringify(rawStats, null, 4), "json");
    this.testReporter.textAttachment(this.report, 'GetStatsSummary', JSON.stringify(summaryStats, null, 4), "json");
  } catch (error) {
      console.log(error);
      throw new KiteTestError(Status.BROKEN, "Failed to getStats");
    }
  }
}

module.exports = GetStatsStep;
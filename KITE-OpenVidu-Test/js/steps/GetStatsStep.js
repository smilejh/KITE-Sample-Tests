const {TestUtils, TestStep, Status, KiteTestError} = require('kite-common');

/**
 * Class: GetStatsStep
 * Extends: TestStep
 * Description:
 */
class GetStatsStep extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.numberOfParticipant = kiteBaseTest.numberOfParticipant;
    this.statsCollectionTime = kiteBaseTest.statsCollectionTime;
    this.statsCollectionInterval = kiteBaseTest.statsCollectionInterval;
    this.selectedStats = kiteBaseTest.selectedStats;
    this.peerConnections = kiteBaseTest.peerConnections;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return 'Getting WebRTC stats via getStats';
  }

  async step() {
    try {
    // pc ??
    let sentStats = await TestUtils.getStats(this, 'kite', this.peerConnections[0]);

    let receivedStats = [];
    for(let i = 1; i < this.numberOfParticipant; i++) {
      // pc ??
      let receivedObj = await TestUtils.getStats(this, 'kite', this.peerConnections[1]);
      receivedStats.push(receivedObj);
    }

    let builder = {};
    builder['local'] = sentStats;
    builder['remote'] = receivedStats;
    let obj = await TestUtils.extractStats(sentStats, receivedStats);

    // Data
    this.testReporter.textAttachment(this.report, 'getStatsRaw', JSON.stringify(builder, null, 4), "json");
    this.testReporter.textAttachment(this.report, 'getStatsSummary', JSON.stringify(obj, null, 4), "json");
    
    } catch (error) {
      console.log(error);
      throw new KiteTestError(Status.BROKEN, "Failed to getStats");
    }
  }
}

module.exports = GetStatsStep;
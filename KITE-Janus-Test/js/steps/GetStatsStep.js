const {TestUtils, TestStep, Status, KiteTestError} = require('kite-common');

class GetStatsStep extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.statsCollectionTime = kiteBaseTest.statsCollectionTime;
    this.statsCollectionInterval = kiteBaseTest.statsCollectionInterval;
    this.selectedStats = kiteBaseTest.selectedStats;
    this.pcArray = kiteBaseTest.peerConnections;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return 'Getting WebRTC stats via getStats';
  }

  async step() {
    try {

      let sentStats = await TestUtils.getStats(this, 'kite', this.pcArray[0]);
      
      let receivedStats = [];
      for(let i = 1; i < this.pcArray.length; i++) {
        let receivedObj = await TestUtils.getStats(this, 'kite', this.pc);
        receivedStats.push(receivedObj);
      }

      let builder = {};
      builder['local'] = sentStats;
      builder['remote'] = receivedStats;
      let obj = await TestUtils.extractStats(sentStats, receivedStats);
      
      // Data
      this.testReporter.textAttachment(this.report, 'getStatsRaw', JSON.stringify(builder), "json");
      this.testReporter.textAttachment(this.report, 'getStatsSummary', JSON.stringify(obj), "json");
  
    } catch (error) {
      console.log(error);
      throw new KiteTestError(Status.BROKEN, 'Failed to getStats');
    }
  }
}

module.exports = GetStatsStep;
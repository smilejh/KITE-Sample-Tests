const {TestUtils, TestStep, Status, KiteTestError} = require('kite-common');

/**
 * Class: GetStatsStep
 * Extends: TestStep
 * Description:
 */
class GetStatsStep extends TestStep {
  constructor(kiteBaseTest, pcArray) {
    super();
    this.driver = kiteBaseTest.driver;
    this.statsCollectionTime = kiteBaseTest.statsCollectionTime;
    this.statsCollectionInterval = kiteBaseTest.statsCollectionInterval;
    this.selectedStats = kiteBaseTest.selectedStats;
    this.pcArray = pcArray;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return 'Getting WebRTC stats via getStats';
  }

  async step() {
    try {
      this.pc = "window.pc";
      let sentStats = await TestUtils.getStats(this);

      let receivedStats = [];
      for(let i = 0; i < this.pcArray.length; i++) {
        this.pc = this.pcArray[i];
        let receivedObj = await TestUtils.getStats(this);
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
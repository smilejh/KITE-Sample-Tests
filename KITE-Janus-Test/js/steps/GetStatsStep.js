const {TestUtils, TestStep} = require('kite-common');
const waitAround = TestUtils.waitAround;
// const {extractStats, buildClientStatObject} = require('../statsUtils');
/**
 * Class: GetStatsStep
 * Extends: TestStep
 * Description:
 */
class GetStatsStep extends TestStep {
  constructor(driver, statsCollectionDuration, statsCollectionInterval, pcArray, selectedStats) {
    super();
    this.driver = driver;
    this.statsCollectionDuration = statsCollectionDuration;
    this.statsCollectionInterval = statsCollectionInterval;
    this.pcArray = pcArray;
    this.selectedStats = selectedStats;
  }

  stepDescription() {
    return 'Getting WebRTC stats via getStats';
  }

  async step(allureTestReport, reporter) {
    
    let sentStats = await TestUtils.getStats(this.driver, "window.pc", this.statsCollectionDuration, this.statsCollectionInterval, this.selectedStats);

    let receivedStats = [];
    for(let i = 0; i < this.pcArray.length; i++) {
      var receivedObj = await TestUtils.getStats(this.driver, this.pcArray[i], this.statsCollectionDuration, this.statsCollectionInterval, this.selectedStats);
      receivedStats.push(receivedObj);
    }

    let builder = {};
    builder['local'] = sentStats;
    builder['remote'] = receivedStats;
    let obj = await TestUtils.extractStats(sentStats, receivedStats);

    // wait for getstats to actually finish
    await waitAround(this.statsCollectionDuration*this.pcArray.length + 1000);
    // Data
    reporter.textAttachment(this.report, 'getStatsRaw', JSON.stringify(builder), "json");
    reporter.textAttachment(this.report, 'getStatsSummary', JSON.stringify(obj), "json");
  }
}

module.exports = GetStatsStep;
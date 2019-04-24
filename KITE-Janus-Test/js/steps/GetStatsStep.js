const {TestUtils, TestStep} = require('kite-common');
const waitAround = TestUtils.waitAround;
/**
 * Class: GetStatsStep
 * Extends: TestStep
 * Description:
 */
class GetStatsStep extends TestStep {
  constructor(driver, statsCollectionDuration, statsCollectionInterval, pcArray) {
    super();
    this.driver = driver;
    this.statsCollectionDuration = statsCollectionDuration;
    this.statsCollectionInterval = statsCollectionInterval;
    this.pcArray = pcArray;
  }

  stepDescription() {
    return 'Getting WebRTC stats via getStats';
  }

  async step(allureTestReport, reporter) {
    let receivedStats = [];
    let stats = {};
    
    this.pcArray.forEach(async(pc) => {
      let getStats = await TestUtils.getStats(this.driver, pc, this.statsCollectionDuration, this.statsCollectionInterval);
      if (pc.includes('remote')) {
        receivedStats.push(getStats);
      } else {
        stats['sentStats'] = getStats;
      }
    });
    // wait for getstats to actually finish
    await waitAround(this.statsCollectionDuration*this.pcArray.length + 1000);
    stats['receivedStats'] = receivedStats;
    reporter.textAttachment(this.report, "Stats", JSON.stringify(stats), "json");
  }
}

module.exports = GetStatsStep;
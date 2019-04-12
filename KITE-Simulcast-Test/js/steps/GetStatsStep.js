const {Builder, By, Key, until, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');
const waitAround = TestUtils.waitAround;

module.exports = {
	execute: async function(driver, testReport, pc, statsCollectionTime, statsCollectionInterval) {
		let report = {};
		let attachment = {};
		attachment['type'] = 'json';
		let stats = {};
		let result = 'passed';
    report['name'] = 'Getting WebRTC stats via getStats';
    report['start'] = Date.now();
    if (testReport.status === 'passed') {
			try {
        console.log('executing: ' + report.name);
				stats['receivedStats'] = await TestUtils.getStats(driver, pc, statsCollectionTime, statsCollectionInterval);
	      attachment['value'] = stats;
			} catch (error) {
				result = 'failed';
        attachment['value'] = '' + error;
        console.log(error);
			}
		} else {
      result = 'skipped';
      console.log('skipping: ' + report.name);
		}
		await waitAround(getStatDuration*pcArray.length); // wait for getstats to actually finish
    report['status'] = result;
    report['stop'] = Date.now();
    report['attachment'] = attachment;
    testReport.steps.push(report);
	}
}
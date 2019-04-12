const {Builder, By, Key, until, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');
const waitAround = TestUtils.waitAround;
const getStatDuration = 5000;

module.exports = {
	execute: async function(driver, testReport, pcArray) {

		// Report
		let report = {};
		let attachment = {};
		attachment['type'] = 'json';
		let stats = {};
		let result = 'passed';
		const receivedStats = [];
		report['name'] = 'Getting WebRTC stats via getStats';
		
		// Start of step
    report['start'] = Date.now();
    if (testReport.status === 'passed') {
			try {
        console.log('executing: ' + report.name);
				pcArray.forEach( async(pc) => {
					let getStats = await TestUtils.getStats(driver, pc, getStatDuration, 1000);
					if (pc.includes('remote')) {
						receivedStats.push(getStats);
					} else {
						stats['sentStats'] = getStats;
					}
				});
				stats['receivedStats'] = receivedStats;
			} catch (error) {
				result = 'failed';
				console.log(error);
				//report['error'] = error;
			}
		} else {
      result = 'skipped';
      console.log('skipping: ' + report.name);
		}
		await waitAround(getStatDuration*pcArray.length); // wait for getstats to actually finish
		// End of the report
		report['stop'] = Date.now();
		report['status'] = result;
    attachment['value'] = stats;
    report['attachment'] = attachment;
    testReport.steps.push(report);
	}
}
const {Builder, By, Key, until, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');
const waitAround = TestUtils.waitAround;

const map = promise.map;

module.exports = {
	execute: async function(driver, testReport, duration) {
		let report = {};
		let result = 'passed';
		let attachment = {};
		attachment['type'] = 'json';
    report['name'] = 'Stay in the meeting for ' + duration + 's.';
    report['start'] = Date.now();
    if (testReport.status === 'passed') {
			try {
        console.log('executing: ' + report.name);
        await waitAround(duration*1000);
      } catch (error) {
        result = 'failed';
        testReport['status'] = result;
        attachment['value'] = '' + error;
        console.log(error);
      }
    } else {
      result = 'skipped';
      console.log('skipping: ' + report.name);
    }
    report['attachment'] = attachment;
    report['status'] = result;
    report['stop'] = Date.now();
    testReport.steps.push(report);
	}
}
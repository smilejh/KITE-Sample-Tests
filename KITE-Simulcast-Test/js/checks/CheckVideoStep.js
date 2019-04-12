const {Builder, By, Key, until, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');

const map = promise.map;

module.exports = {
	execute: async function(driver, testReport, id, timeout) {
		let report = {};
		let result = 'passed';
		let attachment = {};
		attachment['type'] = 'json';
    report['name'] = 'Check a video for: ' + id;
    report['start'] = Date.now();
    if (testReport.status === 'passed') {
      try {
        console.log('executing: ' + report.name);
        let details = {};
        await TestUtils.waitForElement(driver, 'tagName', 'video', timeout);
        const videoCheck = await TestUtils.verifyVideoDisplayById(driver, id);
	      details['videoCheck_' + id] = videoCheck;
	      attachment['value'] = details;
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
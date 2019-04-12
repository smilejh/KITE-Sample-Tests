const {Builder, By, Key, until, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');

const map = promise.map;

module.exports = {
	execute: async function(driver, testReport, filePath, fileName) {
		let report = {};
		let result = 'passed';
		let attachment = {};
		attachment['type'] = 'json';
    report['name'] = 'Get a screenshot';
    report['start'] = Date.now();
    try {
			const image = await TestUtils.takeScreenshot(driver, filePath, fileName);
			//attachment['value'] = image; // this is raw data, can be really big
			attachment['value'] = filePath + fileName;
    } catch (error) {
      result = 'failed';
      testReport['status'] = result;
      attachment['value'] = '' + error;
      console.log(error);
    }
    report['attachment'] = attachment;
    report['status'] = result;
    report['stop'] = Date.now();
    testReport.steps.push(report);
	}
}
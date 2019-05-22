const VideoCheck = require('./VideoCheck');

/**
 * Class: SenderVideoCheck
 * Extends: VideoCheck
 * Description:
 */
class SenderVideoCheck extends VideoCheck {
  constructor(kiteBaseTest) {
    super(kiteBaseTest);
  }

  stepDescription() {
    return "Check the first video is being sent OK";
  }

  async step() {
    let direction = 'sent';
    await super.step(direction);
  }
}

module.exports = SenderVideoCheck;
const webpack = require('@cypress/webpack-preprocessor');

module.exports = (on) => {
  const options = require('../webpack.config.js');
  on('file:preprocessor', webpack(options));
};

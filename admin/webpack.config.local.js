const path = require('path');
const webpackMerge = require('webpack-merge');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

module.exports = (env = {analyze: false}) => {
  const plugins = [
    new MiniCssExtractPlugin({filename: '[name].css'}),
  ];

  if (env.analyze) {
    plugins.push(new BundleAnalyzerPlugin());
  }

  const webpackConfig = {
    mode: 'development',
    devtool: 'cheap-module-source-map',
    devServer: {
      historyApiFallback: true,
      disableHostCheck: true,
      compress: true,
      publicPath: "/static/bundle/",
      host: "0.0.0.0",
      port: 3002,
      proxy: {
        "**": "http://localhost:9901"
      }
    },
    plugins: plugins,
    optimization: {
      minimize: false,
    }
  };

  return webpackMerge(require(`./webpack.config.js`), webpackConfig);
};

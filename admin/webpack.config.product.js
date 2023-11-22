const os = require('os');
const path = require('path');
const webpackMerge = require('webpack-merge');
const ManifestReplacePlugin = require('webpack-manifest-replace-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const TerserPlugin = require('terser-webpack-plugin');
const OptimizeCssnanoPlugin = require('@intervolga/optimize-cssnano-plugin');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

const srcdir = path.resolve(__dirname, 'src/main/resources/templates');
const destdir = path.resolve(__dirname, 'build/prepare');

module.exports = (env = {debug: false, analyze: false}) => {
  const {debug, analyze} = env;

  const plugins = [
    new MiniCssExtractPlugin({filename: '[name]-[contenthash].css'}),
    new ManifestReplacePlugin({
      include: srcdir,
      outputDir: path.resolve(__dirname, 'build/resources/main/templates'),
      test: /\.(jsp|htm|html)$/
    }),
  ];

  if (analyze) {
    plugins.push(new BundleAnalyzerPlugin());
  }

  const webpackConfig = {
    mode: debug ? 'development' : 'production',
    devtool: debug ? 'cheap-module-source-map' : false,
    output: {
      filename: '[name]-[chunkhash].js',
      path: path.join(destdir, 'static/bundle/'),
    },
    plugins: plugins,
    optimization: {
      minimizer: [
        new TerserPlugin({
          cache: path.resolve('./cache/.uglify_cache'),
          sourceMap: debug,
          parallel: 2,
          terserOptions: {
            compress: {
              drop_debugger: !debug,
              unused: true,
              dead_code: true,
              warnings: false,
            },
            output: {
              ecma: 5,
              comments: false,
            },
          },
        }),
        new OptimizeCssnanoPlugin({
          sourceMap: debug,
          cssnanoOptions: {
            preset: [
              'default',
              {
                discardComments: {
                  removeAll: true,
                },
              },
            ],
          },
        }),
      ],
    }
  };

  return webpackMerge(require(`./webpack.config.js`), webpackConfig);
}

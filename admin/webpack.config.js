const path = require('path');
const webpack = require('webpack');
const os = require('os');
const fs = require('fs-extra');
const VueLoaderPlugin = require('vue-loader/lib/plugin');
const HappyPack = require('happypack');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

const happyPackThreadPool = HappyPack.ThreadPool({size: os.cpus().length}); // eslint-disable-line

const srcDir = path.resolve(__dirname, 'src/main/webapp');

const exclusivePattern = /sw[pon]$/

function makeEntries(additionalEntry) {
  const entries = {};
  const entryDir = path.join(srcDir, '/static/entry');

  extractEntry(entryDir);

  return Object.assign(entries, additionalEntry);

  function extractEntry(absolutePath, relativePath) {
    if (fs.lstatSync(absolutePath).isDirectory()) {
      fs.readdirSync(absolutePath).forEach(function (file) {
        const currentRelativePath = relativePath ? `${relativePath}/${file}` : file;
        if (exclusivePattern.test(currentRelativePath))
          return;
        extractEntry(path.join(absolutePath, file), currentRelativePath);
      });
      return;
    }

    const id = relativePath.substr(0, relativePath.lastIndexOf('.')) || relativePath;
    entries[id] = absolutePath;
  }
}

const cacheLoader = {
  loader: 'cache-loader',
  options: {
    cacheDirectory: path.resolve('./cache/.loader_cache')
  }
}

module.exports = {
  devtool: 'cheap-module-source-map',
  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      'ModuleRoot': path.join(srcDir, 'static/module'),
      'VendorRoot': path.join(srcDir, 'static/vendor'),
      'StoreRoot': path.join(srcDir, 'static/module/store'),
      'SvcRoot': path.join(srcDir, 'static/module/service'),
      'CssRoot': path.join(srcDir, 'static/css'),
      'UtilRoot': path.join(srcDir, 'static/module/utils'),
      'RouterRoot': path.join(srcDir, 'static/module/router'),
      'jquery': require.resolve('jquery'),
      'vue': 'vue/dist/vue.esm.js', //https://github.com/vuejs-templates/webpack/issues/215
      'semantic-ui-calendar': 'semantic-ui-calendar/dist/',
      'billboard': 'billboard.js/dist/',
      'Template': path.join(srcDir, 'static/handlebars/template'),
      'notifications': 'UtilRoot/notifications.js'
    }
  },
  entry: makeEntries({
    'vendor': [
      '@babel/polyfill',
      require.resolve('jquery'),
      require.resolve('axios'),
      'vue',
    ],
  }),
  output: {
    filename: '[name].js',
    publicPath: '/static/bundle/'
  },
  module: {
    rules: [
      {
        test: /\.css$/,
        use: [MiniCssExtractPlugin.loader, 'happypack/loader?id=css'],
      }, {
        test: /\.less/,
        use: [MiniCssExtractPlugin.loader, 'happypack/loader?id=less']
      }, {
        test: /\.vue$/,
        loader: 'vue-loader',
      }, {
        test: /\.js$/,
        include: [
          path.join(srcDir, '/static/entry'),
          path.join(srcDir, '/static/handlebars'),
          path.join(srcDir, '/static/module')
        ],
        use: 'happypack/loader?id=js',
      }, {
        test: require.resolve('jquery'),
        use: 'expose-loader?jQuery',
      }, {
        test: /\.jpe?g$|\.gif$|\.png$|\.ttf$|\.eot$|\.svg$|\.woff$|\.woff2$|\.ttf$/,
        use: 'file-loader?name=[name].[ext]?[hash]',
      }, {
        test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        loader: 'url-loader?limit=10000&mimetype=application/fontwoff&name=[name].[ext]?[hash]',
      }, {
        test: /\.hbs$/,
        use: 'happypack/loader?id=hbs',
      }, {
        test: /\.s(c|a)ss$/,
        use: [
          'vue-style-loader',
          'css-loader',
          {
            loader: 'sass-loader',
            // Requires sass-loader@^8.0.0
            options: {
              implementation: require('sass'),
              sassOptions: {
                fiber: false,
                indentedSyntax: false // optional
              },
            },
          },
        ],
      }
    ]
  },
  plugins: [
    new VueLoaderPlugin(),
    new webpack.ProvidePlugin({
      jQuery: 'jquery',
      $: 'jquery',
      'window.jQuery': require.resolve('jquery'),
      'alert': ['notifications', 'alert'],
      'confirm': ['notifications', 'confirm'],
      'prompt': ['notifications', 'prompt'],
    }),
    new HappyPack({
      id: 'css',
      threadPool: happyPackThreadPool,
      loaders: [cacheLoader, 'css-loader'],
    }),
    new HappyPack({
      id: 'less',
      threadPool: happyPackThreadPool,
      loaders: [cacheLoader, 'css-loader', 'less-loader'],
    }),
    new HappyPack({
      id: 'js',
      threadPool: happyPackThreadPool,
      loaders: [
        cacheLoader,
        {
          loader: 'babel-loader',
          options: {
            configFile: path.join(__dirname, ".babelrc")
          }
        }
      ],
    }),
    new HappyPack({
      id: 'hbs',
      threadPool: happyPackThreadPool,
      loaders: [`handlebars-loader?helperDirs[]=${srcDir}/static/handlebars/helper`],
    }),
  ]
};

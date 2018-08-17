const path = require('path');
const HtmlWebPackPlugin = require('html-webpack-plugin');

const root = process.env.BASE_DIR || '/../eco-feature/eco-feature-member/';
console.log(root)

const htmlPlugin = new HtmlWebPackPlugin({
  template: path.join(__dirname, root, 'src/main/react/index.html'),
  filename: "./index.html"
});

module.exports = {

  entry: path.join(__dirname, root, 'src/main/react'),

  output: {
    path: path.join(__dirname, root, 'target/generated-resources')
  },

  module: {
    rules: [
      {
        test: /\.js|jsx$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['env', 'react'],
            plugins: [
              "babel-plugin-transform-object-rest-spread"
            ]
          },
        }
      }
    ]
  },

  plugins: [htmlPlugin],

  devServer: {
    port: 3000,
    host: '0.0.0.0',
    proxy: {
      '/api/**': 'http://localhost:8080',
      '/login': 'http://localhost:8080'
    }
  }

};
const path = require('path')
const HtmlWebPackPlugin = require('html-webpack-plugin')

const root =
  process.env.BASE_DIR || `../eco-feature/eco-feature-${process.env.FEATURE}/`

const htmlPlugin = new HtmlWebPackPlugin({
  template: path.join(__dirname, root, 'src/main/frontend/index.html'),
  filename: './index.html',
})

module.exports = {
  entry: path.join(__dirname, root, 'src/main/frontend'),

  output: {
    path: path.join(__dirname, root, 'target/generated-resources'),
  },

  devtool: 'eval-source-map',

  resolve: {
    extensions: ['.ts', '.tsx', '.js', '.json'],
  },

  module: {
    rules: [
      {
        test: /\.tsx?$/,
        loader: 'awesome-typescript-loader',
      },
      {
        test: /\.js|jsx$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['env', 'react', 'stage-2'],
          },
        },
      },
    ],
  },

  plugins: [htmlPlugin],

  devServer: {
    port: 3000,
    host: '0.0.0.0',
    proxy: {
      '/api/**': 'http://localhost:8080',
      '/login': 'http://localhost:8080',
    },
  },
}

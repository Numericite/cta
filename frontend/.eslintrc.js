module.exports = {
  root: true,
  env: {
    browser: true,
    node: true
  },
  parserOptions: {
    parser: 'babel-eslint'
  },
  extends: [ 'plugin:vue/recommended' ],
  // required to lint *.vue files
  plugins: [ 'vue' ],
  // add your custom rules here
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'vue/max-attributes-per-line': [
      0,
      {
        singleline: 4,
        multiline: {
          max: 1,
          allowFirstLine: true
        }
      }
    ],
    "vue/attribute-hyphenation": [ "error", "never", {
      "ignore": [ "data-", "aria-", "before-change", "get-label", "empty-fill", "completed-steps", "total-steps", "insert-mode", "animation-start-value", "start-angle", "show-percent",
        "line-cap", "input-attrs", "min-len", "component-item", "close-", "clear-", "preserve-", "track-"
      ]
    } ]
  }
}

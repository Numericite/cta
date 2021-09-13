import Vue from 'vue'

Vue.filter('withoutTags', function (html) {
  return html.replace(new RegExp('<[^>]*>', 'g'), '');
});

Vue.filter('str_limit', function (value, size) {
  if (!value) return '';
  value = value.toString();

  if (value.length <= size) {
    return value;
  }
  return value.substr(0, size).trim() + '...';
});

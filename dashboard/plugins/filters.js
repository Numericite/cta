import Vue from 'vue';
import moment from 'moment'

Vue.filter('displayDate', function(value, format = 'DD/MM/YYYY') {
  if (value) {
    return moment(value).format(format)
  }
});

Vue.filter('displayTime', function(value, format = 'HH:mm') {
  if (value) {
    return moment(value).format(format)
  }
});

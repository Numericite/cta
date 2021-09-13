import Vue from 'vue'

Vue.mixin({
  methods: {
    capitalize(s) {
      if (typeof s !== 'string') return ''
      return s.charAt(0).toUpperCase() + s.slice(1)
    }
  }
})

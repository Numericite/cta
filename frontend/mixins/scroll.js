export default {
  mounted() {
    // this.appearElms()
  },

  data() {
    return {
      onScreen: false
    }
  },

  methods: {
    appearElms() {
      const elms = this.$refs.appear
      if (elms !== undefined) {
        for (let i = 0; i < elms.length; i += 1) {
          const elm = elms[i].$el
          if (
            this.isElementInViewport(elm) &&
            !elm.classList.contains('onView')
          ) {
            elm.classList.add('onView')
          }
        }
      }
    },

    isElementInViewport(elm) {
      const rect = elm.getBoundingClientRect()
      return (
        rect.top <=
          (window.innerHeight / 2 + 100 ||
            document.documentElement.clientHeight / 2 + 100) &&
        rect.left >= 0 &&
        rect.right <=
          (window.innerWidth || document.documentElement.clientWidth)
      )
    }
  }
}

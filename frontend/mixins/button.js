export default {
  methods: {
    animateButton: function(target, bg) {
      this.$anime({
        targets: target,
        backgroundColor: bg,
        easing: 'easeInQuad',
        duration: 100
      })
    },

    enterButton(e) {
      this.animateButton(e.target, '#ff8f5e')
    },

    leaveButton(e) {
      this.animateButton(e.target, 'rgba(0,0,0,0)')
    }
  }
}

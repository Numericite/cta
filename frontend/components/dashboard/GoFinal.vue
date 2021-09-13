<template>
  <div class="final fixed pin-t pin-l w-screen h-screen flex justify-center items-center px-3 sm:py-3">

    <div class="layer" />

    <div class="onboardingCard w-1/3 sm:w-full bg-white py-8 rounded-lg flex flex-col items-center sm:my-3">

      <h1 class="text-blue font-bold text-5xl">
        <span v-html="texts[0]" />
      </h1>
      <img class="mt-8" src="~/assets/img/houra.png">
      <div class="text-blue text-center p-8 mt-2 leading-normal max-w-full w-full">
        <span v-html="texts[1]" />
      </div>

      <button class="button button--blue mt-4"
              @click="$emit('close')">J'ai compris!</button>

    </div>

  </div>
</template>

<script>

export default {
  name: 'GoFinal',
  data() {
    return {
      texts: this.getPageTexts('popup-go-final')
    }
  },
  async destroyed() {
    this.$store.commit('auth/setSawFinalPopup', true)
    await this.$api.user.updateUser(this.$store.state.auth.user)
  }
}
</script>

<style lang="scss">
.final {
  z-index: 9000;
  top: 2em;
}
.layer {
  @apply bg-black w-screen h-screen z-50 fixed pin-t pin-l;
  opacity: 0.6;
}
.onboardingCard {
  z-index: 9000;
}
</style>

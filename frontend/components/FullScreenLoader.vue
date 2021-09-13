<template>
  <div v-if="display" class="full-screen-loader w-screen h-screen pin-t pin-l fixed">
    <div class="loader-content">
      <img src="~/assets/img/loader.gif" class="block mx-auto mt-16 w-16 h-16">
      <p class="text-white text-xl mt-2">Chargement...</p>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'FullScreenLoader',
    props: {
      display: {
        type: Boolean,
        default: false
      }
    }
  }
</script>

<style lang="scss" scoped>
  .full-screen-loader {
    background-color: transparentize(#7f828b, 0.05);
    z-index: 999;

    .loader-content {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translateX(-50%)translateY(-50%);
    }
  }
</style>

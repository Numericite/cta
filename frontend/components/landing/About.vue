<template>
  <div id="about"
       class="flexContainer mt-24 mb-64 py-16 relative sm:my-4">

    <h3 class="text-6xl sm:text-5xl xsm:text-4xl title font-extrabold text-blue mb-6">
      <span v-html="title" />
    </h3>

    <div class="flex sm:flex-col">

      <!-- Left part -->
      <div class="flex xl:w-2/5 md:w-3/5 flex-col">
        <p class="mt-4 appear text-xl text-justify" v-html="text" />
        <button v-scroll-to="'#how'"
                class="button button--orange button--shadow self-start mt-12 appear sm:self-center">
          Comment ça marche ?
        </button>
      </div>
      <!-- END Left part -->

      <!-- Right part -->
      <div class="xl:flex absolute pin-r xl:pin-t appear w-1/2 flex justify-end md:w-2/5 md:items-center sm:relative sm:w-full sm:block sm:mt-12">
        <picture>
          <source srcset="~/assets/img/landing/about.webp"
                  type="image/webp">
          <img src="~/assets/img/landing/about.png"
               alt="À propos de l'association Crée ton avenir - Illustration">
        </picture>
      </div>
      <!-- END Right part -->

    </div>

  </div>
</template>

<script>
export default {
  name: 'About',
  props: {
    title: {
      type: String,
      default: ''
    },
    text: {
      type: String,
      default: ''
    }
  }
}
</script>

<style scoped>
.about--img {
  transform: translateY(-10%);
}
</style>

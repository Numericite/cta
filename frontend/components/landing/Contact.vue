<template>
  <div id="contact"
       class="contact flexContainer pt-32 mt-16 mb-32 pb-32 relative sm:pt-0 sm:mb-0 sm:pb-0">

    <h3 class="text-6xl sm:text-5xl xsm:text-4xl title font-extrabold text-blue mb-12">
      <span v-html="title" />
    </h3>

    <div class="flex sm:flex-col">
      <!-- Left part -->
      <div class="contact__left w-1/2 md:w-3/5 sm:w-full">

        <h4 class="text-blue font-extrabold text-2xl mb-6 appear">
          <span v-html="subTitle" />
        </h4>

        <p class="appear text-base">
          <span v-html="subText" />
        </p>

        <p class="text-orange font-extrabold text-2xl mb-4 appear"><span v-html="availabilityTitle" /></p>
        <div class="flex flex-col appear">
          <span class="text-blue-lighter mb-4"><span v-html="availabilityText" /></span>
          <span class="text-blue font-extrabold text-base my-2"><span v-html="phone" /></span>
          <span class="text-blue font-extrabold text-base underline cursor-pointer"><a :href="'mailto:' + email" class="cursor-pointer" v-html="email" /></span>
        </div>

        <nuxt-link tag="button"
                   to="inscription"
                   class="button button--orange mt-12 button--shadow appear">S'inscrire</nuxt-link>

      </div>
      <!-- END Left part -->

      <div class="absolute xl:pin-t pin-r appear w-1/2 flex justify-end md:w-2/5 md:items-center sm:relative sm:w-full sm:block">
        <picture>
          <source srcset="~/assets/img/landing/contact.webp"
                  type="image/webp">
          <img src="~/assets/img/landing/contact.png"
               alt="Contact de l'association crée ton avenir - Illustration">
        </picture>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'Contact',
  props: {
    title: {
      type: String,
      default: ''
    },
    subTitle: {
      type: String,
      default: ''
    },
    subText: {
      type: String,
      default: ''
    },
    availabilityTitle: {
      type: String,
      default: ''
    },
    availabilityText: {
      type: String,
      default: ''
    },
    phone: {
      type: String,
      default: ''
    },
    email: {
      type: String,
      default: ''
    }
  }
}
</script>

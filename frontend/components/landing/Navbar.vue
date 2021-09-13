<template>
  <div class="navbar w-screen h-32 absolute z-20">
    <div class="flex justify-between flexContainer absolute pin-t z-10 h-32 w-full xsm:h-24">

      <!-- Logo -->
      <div>
        <a class="h-full flex items-center"
           href="/">
          <picture>
            <source srcset="~/assets/img/logo.webp"
                    type="image/webp">
            <img src="~/assets/img/logo.png"
                 alt="Logo de l'association crée ton avenir">
          </picture>

        </a>
      </div>
      <!-- End logo -->

      <!-- Menu -->
      <div class="flex items-center justify-end">
        <ul class="flex text-lg sm:hidden">
          <li class="mx-4">
            <a v-scroll-to="'#news'"
               href="#news"
               class="inline-block text-white font-bold">Actualités</a>
          </li>
          <li class="mx-4">
            <a v-scroll-to="'#about'"
               href="#about"
               class="inline-block text-white font-bold">À propos</a>
          </li>
          <li class="mx-4">
            <a v-scroll-to="'#how'"
               href="#how"
               class="inline-block text-white font-bold">Comment ça marche ?</a>
          </li>
          <li class="ml-4">
            <a v-scroll-to="'#contact'"
               href="#contact"
               class="inline-block text-white font-bold">Nous contacter</a>
          </li>
        </ul>
        <button class="button button--white ml-3 smmax:hidden xsm:text-sm"
                @click="showModal = true">Se connecter</button>
      </div>
      <!-- End menu -->

    </div>
    <div :class="[scrollNav ? 'scrollNav--show' : 'scrollNav--hidden']"
         class="scrollNav flex justify-between flexContainer--nav fixed pin-t z-10 h-24 w-full xsm:h-24 bg-blue">

      <!-- Logo -->
      <div>
        <a class="h-full flex items-center"
           href="/">
          <picture>
            <source srcset="~/assets/img/logo.webp"
                    type="image/webp">
            <img src="~/assets/img/logo.png"
                 alt="Logo de l'association crée ton avenir">
          </picture>
        </a>
      </div>
      <!-- End logo -->

      <!-- Menu -->
      <div class="flex items-center justify-start">
        <ul class="flex xl:text-lg text-normal sm:hidden xl:mr-8">
          <li class="xl:mx-4 mx-2 text-center">
            <a v-scroll-to="'#news'"
               href="#news"
               class="inline-block text-white font-bold">Actualités</a>
          </li>
          <li class="xl:mx-4 mx-2 text-center">
            <a v-scroll-to="'#about'"
               href="#about"
               class="inline-block text-white font-bold">À propos</a>
          </li>
          <li class="xl:mx-4 mx-2 text-center">
            <a v-scroll-to="'#how'"
               href="#how"
               class="inline-block text-white font-bold">Comment ça marche ?</a>
          </li>
          <li class="xl:ml-4 mx-2 text-center">
            <a v-scroll-to="'#contact'"
               href="#contact"
               class="inline-block text-white font-bold">Nous contacter</a>
          </li>
        </ul>
        <button class="button button--white mr-3 allsm:text-sm allsm:px-4 allsm:py-3 allsm:ml-3"
                @click="showModal = true">Se connecter</button>
      </div>
      <!-- End menu -->

    </div>
    <transition name="modal">
      <Login v-if="showModal"
             @close="showModal = false" />
    </transition>
  </div>
</template>

<script>
import Login from '~/components/user/Login'

export default {
  name: 'NavbarLanding',
  components: { Login },
  data() {
    return {
      showModal: false,
      scrollNav: false
    }
  },
  mounted() {
    this.initEvents()
  },
  methods: {
    initEvents() {
      document.addEventListener('scroll', e => {
        document.documentElement.scrollTop > 120
          ? (this.scrollNav = true)
          : (this.scrollNav = false)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.flexContainer--nav  {
  padding: 0 8.5rem;
  @screen md {
    padding: 0 1.5rem;
  }
  @screen sm {
    padding: 0 1.5rem;
  }
}

.modal-enter {
  opacity: 0;
}

.modal-leave-active {
  opacity: 0;
}

.modal-enter .modal-container,
.modal-leave-active .modal-container {
  transform: scale(1.1);
}

.scrollNav--hidden {
  transform: translateY(-200%);
}

.scrollNav--show {
  transform: translateY(0);
}

.scrollNav {
  transition: all 0.3s ease;
  box-shadow: 0 2.5px 25px 0 rgba(0, 0, 0, 0.1s);
}
</style>

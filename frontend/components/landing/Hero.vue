<template>
  <div :class="[appear ? 'onView' : '']"
       class="hero flexContainer flex w-full appear--bg relative">
    <div class="hero__content justify-center z-10 flex flex-col text-white pt-6">
      <h1 class="font-extrabold text-8xl title w-full sm:text-5xl sm:max-w-full">
        <span v-html="title" />
      </h1>
      <h2 class="appear text-4xl pt-4 pb-8 sm:text-3xl ml-1"><span v-html="subTitle" /></h2>
      <div class="buttons appear">
        <button class="button button--white xsm:ml-0 xsm:mt-4 sm:hidden"
                @click="showModal = true">Se connecter</button>
      </div>
    </div>

    <transition name="modal">
      <Login v-if="showModal"
             @close="showModal = false" />
    </transition>
    <div class="hero-helper"/>
  </div>
</template>

<script>
import Login from '~/components/user/Login'

export default {
  name: 'Hero',
  components: { Login },
  props: {
    title: {
      type: String,
      default: ''
    },
    subTitle: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      showModal: true,
      appear: false
    }
  },
  created() {
    this.showModal = false
    this.appear = true
  },
  mounted() {
    if (this.$route.hash === '#connect')
      this.showModal = true
  }
}
</script>

<style lang="scss" scoped>

.hero-helper {
  position: absolute;
  top: 0;
  left: 0;
  z-index: -1;
  min-height: 800px;
  width: 100%;
  @apply bg-blue;

  @screen xxxl {
    background: linear-gradient(70deg, #432cb4 80%, #1b0782 20%);
  }
}

.hero__content {
  display: flex;
  min-height: 800px;
  vertical-align: middle;
}
.hero {
  display: flex;
  min-height: 800px;
  background-image: url(/img/landing/hero-characteres.png);
  background-repeat: no-repeat;
  background-position-x: 30%;
  background-position-y: center;

  @screen sm {
    height: 550px;
    background-position: 70% 0;
  }
  @screen xsm {
    height: 500px;
    background-position: 65% 0;
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
</style>

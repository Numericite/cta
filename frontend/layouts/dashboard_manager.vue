<template>
  <div class="bg-blue-lightest sm:overflow-x-hidden">
    <Navbar class="navbar w-screen" />
    <manager-hero class="mt-20 sm:mt-24" />
    <toolbar class="toolbar" />
    <nuxt />
    <Footer class="footer" />
  </div>
</template>

<script>
import ManagerHero from '~/components/dashboard/ManagerHero'
import Toolbar from '~/components/dashboard/Toolbar'
import Footer from '~/components/Footer'
import Navbar from '~/components/dashboard/Navbar'
import OnBoarding from '~/components/onboarding/OnBoarding'

export default {
  name: 'Dashboard',
  components: { Navbar, Footer, ManagerHero, Toolbar, OnBoarding },
  data() {
    return {
      onboarding: false
    }
  }
}
</script>

<style lang="scss" scoped>
p {
  @apply leading-normal;
  margin-bottom: 1.3333rem;
}

.toolbar {
  top: 5rem;
  position: sticky;
  z-index: 100;
  @screen sm {
    position: fixed;
    top: auto;
    bottom: 0;
    z-index: 3000;
  }
}

.navbar {
  position: fixed;
  top: 0;
  z-index: 101;
}

.footer {
  z-index: 150;
}
</style>

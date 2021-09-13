<template>
  <div>
    <Navbar-dash v-if="$store.state.auth.user"
                 class="fixed w-full pin-t z-20" />
    <nuxt />
    <Footer class="blogFooter" />
  </div>
</template>

<script>
import Footer from '~/components/Footer'
import NavbarDash from '~/components/dashboard/Navbar'

export default {
  name: 'Blog',
  components: { Footer, NavbarDash },
  mounted() {
    window.scrollTo(0, 0)
  }
}
</script>

<style lang="scss">
.blogFooter {
  position: relative;
  margin-top: 0;
}
</style>

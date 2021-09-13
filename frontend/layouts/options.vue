<template>
  <div>
    <Navbar />
    <nuxt />
  </div>
</template>

<script>
import Navbar from '~/components/user/register/Navbar'

export default {
  name: 'Options',
  components: { Navbar }
}
</script>


<style>
html {
  font-family: 'OpenSans', 'Source Sans Pro', -apple-system, BlinkMacSystemFont,
    'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  font-size: 14px;
  word-spacing: 1px;
  -ms-text-size-adjust: 100%;
  -webkit-text-size-adjust: 100%;
  -moz-osx-font-smoothing: grayscale;
  -webkit-font-smoothing: antialiased;
  box-sizing: border-box;
  overflow-x: hidden;
}

*,
*:before,
*:after {
  box-sizing: border-box;
  margin: 0;
}

p {
  @apply leading-normal;
  margin-bottom: 1.3333rem;
}
</style>

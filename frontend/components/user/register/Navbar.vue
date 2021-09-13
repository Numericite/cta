<template>
  <div class="navbar w-full flexContainer flex items-center justify-between bg-blue">

    <nuxt-link to="/">
      <picture>
        <source srcset="~/assets/img/logo.webp"
                type="image/webp">
        <img src="~/assets/img/logo.png"
             alt="Logo de l'association crée ton avenir">
      </picture>
    </nuxt-link>

    <nuxt-link to="/"
               class="text-blue-lighter text-base font-bold uppercase flex justify-center items-center">
      Retour à l'accueil
      <svg class="arrow ml-2"
           height="15px"
           width="15px"
           viewBox="0 0 240.823 240.823"
           fill="#A2ACE3">
        <g>
          <path d="M183.189,111.816L74.892,3.555c-4.752-4.74-12.451-4.74-17.215,0c-4.752,4.74-4.752,12.439,0,17.179
		l99.707,99.671l-99.695,99.671c-4.752,4.74-4.752,12.439,0,17.191c4.752,4.74,12.463,4.74,17.215,0l108.297-108.261
		C187.881,124.315,187.881,116.495,183.189,111.816z" />
        </g>
      </svg>
    </nuxt-link>
  </div>
</template>

<script>
export default {
  name: 'RegisterNavbar'
}
</script>

<style scoped lang="scss">
.navbar {
  height: 80px;
  a {
    g {
      transition: fill 0.3s ease;
    }
    &:hover {
      g {
        fill: #ff8f5e;
      }
    }
  }
}
</style>

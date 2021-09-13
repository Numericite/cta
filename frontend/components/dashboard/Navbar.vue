<template>
  <div class="navbar flexContainer h-24 flex justify-between items-center bg-white relative">

    <picture>
      <source srcset="~/assets/img/logo-violet.webp"
              type="image/webp">
      <img src="~/assets/img/logo-violet.png"
           alt="Logo de l'association crée ton avenir">
    </picture>

    <div class="flex items-center h-full"
         @mouseenter="dropdown = true"
         @mouseleave="dropdown = false">
      <div class="profile ml-8 flex items-center relative h-full cursor-pointer">
        <div class="rounded-full w-10 h-10 bg-blue items-center justify-center mr-3 padding-2px">
          <img :src="$store.state.auth.user.avatar_path"
               class="w-full rounded-full">
        </div>
        <div v-if="$store.state.auth.user"
             class="flex items-center cursor-pointer">
          <span class="text-blue font-semibold sm:hidden">{{ $store.state.auth.user.firstName }} {{ $store.state.auth.user.lastName }}</span>
          <svg class="arrow ml-2"
               height="15px"
               width="15px"
               viewBox="0 0 240.823 240.823"
               fill="#432cb4">
            <g>
              <path d="M183.189,111.816L74.892,3.555c-4.752-4.74-12.451-4.74-17.215,0c-4.752,4.74-4.752,12.439,0,17.179
		l99.707,99.671l-99.695,99.671c-4.752,4.74-4.752,12.439,0,17.191c4.752,4.74,12.463,4.74,17.215,0l108.297-108.261
		C187.881,124.315,187.881,116.495,183.189,111.816z" />
            </g>
          </svg>
        </div>
        <div v-if="dropdown"
             class="dropdown absolute pin-l pin-t bg-white w-48 py-3 px-6 pt-8 rounded-lg ">
          <span class="triangle" />
          <ul>
            <nuxt-link to="/dashboard/profil"
                       tag="li"
                       class="cursor-pointer mb-6 text-left text-blue font-semibold"
                       @click.native="dropdown = false">Mes informations</nuxt-link>
            <hr class="hr mb-4">
            <nuxt-link to="/dashboard/student/aide"
                       tag="li"
                       class="cursor-pointer mb-6 text-left text-blue font-semibold"
                       @click.native="dropdown = false">Aide</nuxt-link>
            <hr class="hr mb-4">
            <li class="cursor-pointer mb-6 text-left text-blue font-semibold"
                @click="logout">Se déconnecter</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NavbarDashboard',
  data() {
    return {
      dropdown: false
    }
  },
  methods: {
    logout: function() {
      this.$store
        .commit( 'auth/destroy' )
      this.$router.push( '/' )
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  box-shadow: 0 2px 4px 0 rgba(162, 172, 227, 0.31);

  .padding-2px {
    padding: 2px;
  }

  .profile {
    height: 130%;
    .arrow {
      transform: rotate(90deg);
    }
    .triangle {
      width: 0;
      height: 0;
      position: absolute;
      transform: translate(100%, -80%);

      top: 0;
      border-style: solid;
      border-width: 0 7.5px 15px 7.5px;
      border-radius: 0.5rem;
      border-color: transparent transparent #fff transparent;
    }
    .dropdown {
      transform: translate(0, 60%);
    }
  }
}
</style>

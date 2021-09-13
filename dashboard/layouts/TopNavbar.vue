<template>
  <nav class="navbar navbar-default">
    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" @click="toggleSidebar">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar bar1"></span>
          <span class="icon-bar bar2"></span>
          <span class="icon-bar bar3"></span>
        </button>
        <a v-if="$store.state.auth.user" class="navbar-brand" :href="websiteUrl" target="_blank"><i class="fa fa-globe"></i> Accéder au site</a>
        <a v-if="!$store.state.auth.user" class="navbar-brand">Dashboard - Crée ton Avenir</a>
      </div>
      <div v-if="$store.state.auth.user" class="collapse navbar-collapse">
        <ul class="nav navbar-nav navbar-right relative">
          <img class="md-avatar navbar-avatar img-circle" :src="$store.state.auth.user.avatar_path">
          <drop-down :title="$store.state.auth.user.firstName + ' ' + $store.state.auth.user.lastName">
            <li><a href="" @click="logout()">Déconnexion</a></li>
          </drop-down>
        </ul>
      </div>
    </div>
  </nav>
</template>
<script>

  export default {
    data() {
      return {
        activeNotifications: false,
        websiteUrl: 'https://parcours-orientation.cree-ton-avenir.fr'
      }
    },
    methods: {
      capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1)
      },
      toggleNotificationDropDown() {
        this.activeNotifications = !this.activeNotifications
      },
      closeDropDown() {
        this.activeNotifications = false
      },
      toggleSidebar() {
        // this.$sidebar.displaySidebar(!this.$sidebar.showSidebar)
      },
      hideSidebar() {
        this.$sidebar.displaySidebar(false)
      },
      logout() {
        this.$store
          .commit( 'auth/destroy' )
        this.$router.push( '/login' )
      }
    }
  }

</script>
<style>

</style>

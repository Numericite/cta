<template>
  <div class="confirm w-full flex flex-col items-center flexContainer mt-12">
    <div v-if="accountValidated" class="text-center">
      <h1 class="text-blue text-4xl text-center title font-extrabold mb-4">
        Compte validé !
      </h1>
      <p class="mt-8">Tu peux dès à présent accéder à ton tableau de bord et commencer la phase de connaissance de soi.<br>À toi de jouer !</p>
      <div class="uppercase text-blue flex items-center font-bold mt-4 sm:mt-4 sm:self-start"
           @click="goDashboard">
        <button class="button button--orange mt-5 mx-auto">ACCEDER À MON TABLEAU DE BORD</button>
      </div>
    </div>
    <div v-if="!accountValidated" class="text-center">
      <h1 class="text-blue text-4xl text-center title font-extrabold mb-4 mt-4">
        Ce lien n'est plus valide
      </h1>
      <p class="mt-8">Le lien utilisé pour la validation est invalide.</p>
      <nuxt-link to="/"
                 class="uppercase text-blue flex items-center font-bold sm:mt-4 sm:self-start">
        <button class="button button--orange mt-5 mx-auto">RETOUR À L'ACCUEIL</button>
      </nuxt-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Confirm',
  layout: 'options',
  data() {
    user: {}
    token: ''
  },
  methods: {
    goDashboard: function() {
      this.$store.commit('auth/setUser', this.user)
      this.$store.commit('auth/setToken', this.token)
      this.$router.push('/dashboard/student')
    }
  },
  async asyncData({ app, route, store }) {
    try {
      let response = await app.$api.user.validateAccount(
        route.params.token
      )
      return { accountValidated: true, user: response.data.res.user, token: response.data.res.token }
    } catch (err) {
      return { accountValidated: false }
    }
  }
}
</script>

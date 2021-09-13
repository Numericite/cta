<template>
  <div class="flexContainer justify-center mt-16">
    <div v-if="!sent" class="w-full flex flex-col items-center">
      <h1 class="text-blue text-4xl text-center title font-extrabold">
        Nouveau mot de passe
      </h1>

      <FormulateForm class="w-full flex flex-col items-center" @submit="resetPassword">

        <!-- password -->
        <FormulateInput v-model="user.password" :outerClass="'field w-2/5 mt-12 allsm:w-full'" :wrapperClass="'w-full h-full'" :inputClass="'w-full focus:field--active'" :errorsClass="['mt-2']" :errorClass="['text-red my-1']"
                        :validation="[['required'],['min', 8, 'length'],['matches', /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$/]]"
                        :validationMessages="{
                          required: 'Ce champ est obligatoire',
                          min : 'Votre mot de passe doit contenir au moins 8 caractères',
                          matches: 'Votre mot de passe doit contenir au moins un nombre et une lettre.'
                        }"
                        outerHasValueClass="field--active" outerHasErrorsClass="field--error"
                        type="password" name="password" label="Nouveau mot de passe *"/>
        <!-- End password -->

        <!-- confirmPassword -->
        <FormulateInput v-model="confirmPassword" :outerClass="'field w-2/5 mt-20 mb-12 allsm:w-full'" :wrapperClass="'w-full h-full'" :inputClass="'w-full focus:field--active'" :errorsClass="['mt-2']" :errorClass="['text-red my-1']"
                        :validation="[['required'],['confirm', 'password']]"
                        :validationMessages="{
                          required: 'Ce champ est obligatoire',
                          confirm : 'Les mots de passe ne correspondent pas'
                        }"
                        outerHasValueClass="field--active" outerHasErrorsClass="field--error"
                        type="password" name="confirm_password" label="Confirmation de mot de passe *"/>
        <!-- End confirmPassword -->

        <span v-if="invalidToken" class="mt-8">
          <span class="text-red">Le lien est invalide.</span> <nuxt-link to="/mot-de-passe-oublie">Cliquez ici pour obtenir un nouveau lien par email</nuxt-link>.
        </span>

        <button class="button button--orange mt-8" @submit="resetPassword()">Valider</button>

      </FormulateForm>

    </div>

    <div v-else class="flex flex-col items-center ">

      <h1 class="text-blue text-4xl text-center title font-extrabold">
        Mot de passe modifié
      </h1>

      <h2 class="text-blue text-2xl mt-8 text-center">
        Votre mot de passe a bien été modifié.
      </h2>

      <nuxt-link to="/#connect" class="button button--orange mt-8">
        Se connecter
      </nuxt-link>

      <picture>
        <source srcset="~/assets/img/welldone-register.webp"
                type="image/webp">
        <img src="~/assets/img/welldone-register.png"
             class="mt-12"
             alt="Confirmation d'inscription !">
      </picture>

    </div>
  </div>
</template>

<script>
export default {
  name: 'NewPassword',
  layout: 'options',
  data() {
    return {
      confirmPassword: '',
      invalidToken: false,
      sent: false,
      user: {
        password: ''
      }
    }
  },
  methods: {
    async resetPassword() {
      try {
        await this.$api.user.resetPassword(this.$route.params.token, this.user.password)
        this.sent = true
      } catch (e) {
        if (e.response.status === 404) {
          this.invalidToken = true
        }
      }
    }
  }
}
</script>

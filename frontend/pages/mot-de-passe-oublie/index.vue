<template>
  <div class="flexContainer flex flex-col items-center justify-center mt-16">

    <div v-if="!sent"
         class="flex flex-col items-center justify-center w-full">
      <h1 class="text-blue text-4xl text-center title font-extrabold">
        Mot de passe oublié ?
      </h1>

      <div class="relative flex flex-col items-center xl:w-3/5">
        <h2 class="text-blue text-center text-2xl mt-8">
          Nous allons vous envoyer un mail pour le changer.
        </h2>

        <!-- mail -->
        <FormulateInput v-model="user.email" :outerClass="'field mt-12 mb-6 w-1/2 allsm:w-full'" :wrapperClass="'w-full'" :inputClass="'w-full focus:field--active'" :errorsClass="['mt-20']" :errorClass="['text-red']"
                        :validation="[['bail'],['required'],['email'],['invalidEmail']]"
                        :validationRules="{
                          invalidEmail: () => !invalidEmail
                        }"
                        :validationMessages="{
                          required: 'Ce champ est obligatoire',
                          email : 'Votre adresse email n\'est pas valide',
                          invalidEmail: 'Cet email n\'est rattaché à aucun compte.'
                        }"
                        outerHasValueClass="field--active" outerHasErrorsClass="field--error"
                        label="Adresse email *"/>
        <!-- End mail -->

        <button class="button button--orange mt-8"
                @click="sendMail()">Envoyer un mail</button>
      </div>
    </div>

    <div v-else
         class="flex flex-col items-center">
      <h1 class="text-blue text-4xl text-center title font-extrabold">
        Le mail a bien été envoyé !
      </h1>
      <h2 class="text-blue text-2xl mt-8 text-center">
        Cliquez sur le lien reçu pour réinitialiser votre mot de passe.
      </h2>

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
  name: 'ForgotPassword',
  layout: 'options',
  data() {
    return {
      user: {
        email: ''
      },
      invalidEmail: false,
      sent: false
    }
  },
  methods: {
    async sendMail() {
      try {
        await this.$api.user.forgotPassword(this.user.email)
        this.sent = true
        return 'success'
      } catch (e) {
        if (e.response.status === 404) {
          this.invalidEmail = true
        }
      }
    }
  }
}
</script>

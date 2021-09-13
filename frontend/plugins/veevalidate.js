import Vue from 'vue'
import fr from 'vee-validate/dist/locale/fr'
import VeeValidate, { Validator } from 'vee-validate'

Validator.extend('password', {
  getMessage(field, args) {
    return 'Votre mot de passe doit contenir au moins 8 caractères comprenant au moins une lettre et un nombre.'
  },
  validate(value) {
    return new RegExp(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/).test(
      String(value)
    )
  }
})

Vue.use(VeeValidate, {
  inject: false,
  fieldsBagName: 'veeFields'
})

Validator.localize('fr', fr)

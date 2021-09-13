import Vue from 'vue';
import VeeValidate from "vee-validate";
import { Validator } from "vee-validate";

Vue.use(VeeValidate, {
  mode: 'eager',
  inject: false
});

const dictionary = {
  fr: {
    messages:{
      required: () => 'Ce champ est obligatoire',
      email: () => 'Email invalide',
      min: (field, counter) => 'Au moins ' + counter + ' caractères requis',
      url: () => 'Lien invalide',
      regex: () => 'Lien vidéo invalide',
    },
    custom: {
      // email: {
      //   required: 'Error custom for email'
      // }
    }
  },
};

Validator.localize(dictionary);

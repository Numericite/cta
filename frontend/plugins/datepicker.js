import Vue from 'vue'

// import component and stylesheet
import AirbnbStyleDatepicker from 'vue-airbnb-style-datepicker'

// see docs for available options
const datepickerOptions = {
  days: ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche'],
  daysShort: ['Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam', 'Dim'],
  monthNames: [
    'Janvier',
    'Février',
    'Mars',
    'Avril',
    'Mai',
    'Juin',
    'Juillet',
    'Août',
    'Septembre',
    'Octobre',
    'Novembre',
    'Décembre',
  ]
}

// make sure we can use it in our components
Vue.use(AirbnbStyleDatepicker, datepickerOptions)

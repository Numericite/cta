import Vue from 'vue';
import VueFormulate from '@braid/vue-formulate'
import { fr } from '@braid/vue-formulate-i18n'

// import Multiselect from 'vue-multiselect'

Vue.use(VueFormulate, {
  plugins: [ fr ],
  locale: 'fr',
  // library: {
  //   'v-formulate-multiselect': {
  //     classification: 'select',
  //     component: Multiselect
  //   }
  // },
  classes: {
    outer: [''],
    wrapper: [''],
    input: ['bg-white border-blue-lightest min-w-full border-1 border-solid border-purple-light-periwinkle shadow-sm focus:outline-none rounded-lg2 box-border max'],
    label: ['uppercase leading-normal font-plex tracking-wider text-ms-gray-dark text-lg font-semibold'],
  }
})

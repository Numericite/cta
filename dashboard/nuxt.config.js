import VuetifyLoaderPlugin from 'vuetify-loader/lib/plugin'

import {
  API_ROOT,
  API_ROOT_PROD,
  REQUEST
} from './config'

const routerBase = (process.env.ENV === 'PROD' || process.env.ENV === 'STAGING')
  ?
  '/ctadashboard'
  :
  '';

export default {
  router: {
    base: routerBase,
    linkExactActiveClass: 'active',
    middleware: ['root-redirect', 'vue-kindergarten']
  },
  head: {
    title: 'Dashboard - Crée ton avenir',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: 'Nuxt.js project' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
      { rel: 'stylesheet', href: "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" },
      { rel: 'stylesheet', href: "https://fonts.googleapis.com/css?family=Muli:400,300" },
      { rel: 'stylesheet', href: "https://fonts.googleapis.com/css?family=Montserrat" },
      { rel: 'stylesheet', href: "https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" },
      { rel: 'stylesheet', href: routerBase + "/css/themify-icons.css" },
      { rel: 'stylesheet', href: "https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900|Material+Icons" },
      {
        rel: 'stylesheet',
        type: 'text/css',
        href: 'https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons'
      }
    ]
  },
  loading: { color: '#432cb4' },
  plugins: [
    '~/plugins/api.js',
    '~/plugins/axios.js',
    '~/plugins/vue-kindergarten.js',
    '~/plugins/vuetify.js',
    '~/plugins/vee-validate.js',
    '~/plugins/filters.js',
    '~/plugins/mixinCommonMethods.js',
    '~/plugins/vue-formulate',
    { src: '~/plugins/wysiwyg', ssr: false },
    { src: '~/plugins/upload', ssr: false },
    { src: '~/plugins/dashboard' },
    { src: '~/plugins/globalComponents' },
    { src: '~/plugins/globalDirectives' },
    { src: '~/plugins/charts', ssr: false }
  ],
  env: {
    API_DEV: API_ROOT,
    API_PROD: API_ROOT_PROD,
  },
  publicRuntimeConfig: {
    API_URL: process.env.API_URL,
    FRONT_URL: process.env.FRONT_URL,
  },
  modules: [
    '@nuxtjs/axios',
    '@nuxtjs/style-resources'
  ],
  styleResources: {
    sass:  [
      '@/assets/sass/custom/_variables.scss',
      '@/assets/sass/custom/_mixins.scss',
      '@/assets/sass/custom/_ui-kit.scss'
    ]
  },
  /*
  ** Build configuration
  */
  build: {
    /*
    ** Run ESLint on save
    */
    plugins: [new VuetifyLoaderPlugin()],
    extractCSS: true,
    transpile: ['vuetify/lib'],
    extend (config, { isDev, isClient }) {
      if (isDev && isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
    }
  },
  css: ['~/assets/app.styl']
}

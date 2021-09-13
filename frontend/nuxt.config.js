const pkg = require( './package' )
const glob = require( 'glob-all' )
const path = require( 'path' )

import {
  API_ROOT,
  API_ROOT_PROD,
  REQUEST
} from './config'

module.exports = {
  mode: 'universal',
  head: {
    title: 'Crée ton avenir',
    meta: [ {
        charset: 'utf-8'
      },
      {
        name: 'viewport',
        content: 'width=device-width, initial-scale=1'
      },
      {
        hid: 'keywords',
        name: 'keywords',
        content: 'association, avenir, lycéen, collégien, orientation, test, métiers, futur'
      },
      {
        hid: 'description',
        name: 'description',
        content: "Crée ton avenir - plateforme d'orientation post-bac"
      }
    ],
    link: [
      { rel: 'stylesheet', href: 'https://fonts.googleapis.com/css?family=Open+Sans:400,500,600,700,800|Proza+Libre:400,500,600,700' },
      { rel: 'stylesheet', href: 'https://fonts.googleapis.com/css2?family=IBM+Plex+Sans&display=swap' },
      { rel: 'stylesheet', href: 'https://unpkg.com/vue-airbnb-style-datepicker@2.1.0/dist/vue-airbnb-style-datepicker.min.css' },
      { rel: 'stylesheet', href: 'https://unpkg.com/leaflet@1.2.0/dist/leaflet.css' },]
  },
  loading: {
    color: '#ff8f5e'
  },
  css: [
    // Attention!It is essential to keep this
    // import order and file format(css
    //   for tailwind and scss
    //   for custom css).There is currently a bug with tailwind that makes the compilation very slow with scss.
    '@/assets/css/tailwindBase.css',
    '@/assets/css/main.scss',
    '@/assets/css/tailwindUtilities.css'
  ],
  router: {
    middleware: 'vue-kindergarten'
  },
  plugins: [
    '~/plugins/api.js',
    '~/plugins/axios.js',
    '~/plugins/veevalidate.js',
    '~/plugins/formwizard.js',
    '~/plugins/vue-kindergarten.js',
    '~/plugins/filters.js',
    '~/plugins/globals.js',
    '~/plugins/vue-formulate.js',
    // {
    //   src: '~/plugins/force-next-tick.js',
    //   ssr: false
    // },
    {src:'~/plugins/cropper.js', ssr: false },
    {src: '~/plugins/vue-leaflet', ssr: false },
    {
      src: '~/plugins/datepicker.js',
      ssr: false
    },
    // {
    //   src: '~/plugins/clipper.js',
    //   ssr: false
    // },
    {
      src: '~/plugins/vue-draggable.js',
      ssr: false
    },
    {
      src: '~/plugins/vue-modal.js',
      ssr: false
    },
    {
      src: '~/plugins/notifications.js',
      ssr: false
    },
    {
      src: '~/plugins/markdown.js',
      ssr: false
    },
    {
      src: '~/plugins/vue-touch.js',
      ssr: false
    },
    {
      src: '~/plugins/scrollto.js',
      ssr: false
    },
    {
      src: '~/plugins/vue-swiper.js',
      ssr: false
    },
    {
      src: '~/plugins/autocomplete.js',
      ssr: false
    },
    {
      src: '~/plugins/pretty-checkbox.js',
      ssr: false
    },
    // {
    //   src: '~/plugins/vue-single-select.js',
    //   ssr: false                             
    // },
    {
      src: '~/plugins/vue-simple-calendar.js',
      ssr: false
    },
    {
      src: '~/plugins/radial-progress.js',
      ssr: false
    },
    {
      src: '~/plugins/vue-video-embed.js',
      ssr: false
    },
  ],
  env: {
    API_DEV: API_ROOT,
    API_PROD: API_ROOT_PROD,
  },
  publicRuntimeConfig: {
    API_URL: process.env.API_URL,
    DASHBOARD_URL: process.env.DASHBOARD_URL,
  },
  modules: [
    // Simple usage
    'nuxt-leaflet',
    '@nuxtjs/pwa',
    [
      '@nuxtjs/axios',
      {
        baseURL: API_ROOT,
        headers: {
          'Content-Type': 'application/json'
        },
        timeout: REQUEST.TIMEOUT
      }
    ],
    '@nuxtjs/toast',
    ['nuxt-matomo', { matomoUrl: 'https://matomo.numericite.eu/', siteId: 14 }],
    '@nuxtjs/composition-api'
  ],
  toast: {
    position: 'top-center'
  },
  build: {
    babel: {
      plugins: [
        ["@babel/plugin-transform-arrow-functions"]
      ]
    },
    transpile: ['vue-image-crop-upload' ],
    extractCSS: true,
    postcss: {
      plugins: {
        tailwindcss: path.resolve( './tailwind.js' )
      }
    },
    extend( config, ctx ) {

      if ( ctx.isServer ) {
        config.resolve.alias[ 'hammerjs$' ] =
          '~/node_modules/vue-touch/dist/hammer-ssr.js'
      }
      if ( ctx.isDev && ctx.isClient ) {
        config.module.rules.push( {
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        } )
      }
    }
  }
}

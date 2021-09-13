const join = require( 'path' ).join
const tailwindJS = join( __dirname, 'tailwind.js' )
const purgecss = require( '@fullhuman/postcss-purgecss' )
const cssnano = require( 'cssnano' )
const autoprefixer = require( 'autoprefixer' )

class TailwindExtractor {
  static extract( content ) {
    return content.match( /[A-z0-9-:/]+/g ) || []
  }
}

module.exports = {
  plugins: [
    require( 'postcss-import' )(),
    require( 'tailwindcss' )( tailwindJS ),
    cssnano( {
      preset: 'default'
    } ),
    require( 'autoprefixer' ),
    purgecss( {
      content: [
        './pages/**/*.vue',
        './layouts/**/*.vue',
        './components/**/*.vue'
      ],
      extractors: [ {
        extractor: TailwindExtractor,
        extensions: [ 'html', 'js', 'vue' ]
      } ],
      whitelist: [ 'html', 'body' ],
      whitelistPatterns: [ /nuxt-/, /vue-form-wizard/, /wizard-/, /wizard/ ],
      whitelistPatternsChildren: [
        /nuxt-/,
        /vue-form-wizard/,
        /wizard-/,
        /wizard/,
        /w-/,
        /xl/,
        /sm/,
        /allsm/,
        /semibold/,
        /h-*/,
        /w-*/,
        /py-*/,
        /px-*/,
        /pr-*/,
        /mb-*/,
        /my-*/,
        /m-*/,
        /p-*/,
        /text-*/,
        /mr-*/,
        /mx-*/,
        /z-*/,
        /appeared/,
        /justify-around/,
        /bg-*/,
        /rounded-*/,
        /border-*/,
        /fill-*/
      ]
    } ),
    autoprefixer
  ]
}

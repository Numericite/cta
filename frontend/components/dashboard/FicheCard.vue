<template>
  <div class="cardShadow bg-white rounded-xl p-6 px-8 sm:px-6">
    <div class="flex flex-no-wrap relative">
      <div class="flex items-center p-4 pr-8 sm:py-8 sm:pr-4 sm:pl-0 border-solid border-r border-blue-lightest">
        <svg xmlns="http://www.w3.org/2000/svg"
             xmlns:xlink="http://www.w3.org/1999/xlink"
             width="47"
             height="58"
             viewBox="0 0 47 58"
             class="w-16 h-16 sm:w-12 sm:h-12">
          <defs>
            <path id="a"
                  d="M0 0h46.505v57.26H0z" />
          </defs>
          <g fill="none"
             fill-rule="evenodd">
            <path fill="#1DD4B6"
                  d="M16.81 31.265h20.335c.783 0 1.423-.647 1.423-1.437s-.64-1.438-1.423-1.438H16.81c-.783 0-1.424.647-1.424 1.438 0 .79.64 1.437 1.424 1.437M16.81 22.4h8.471c.783 0 1.424-.646 1.424-1.437 0-.79-.64-1.437-1.424-1.437h-8.47c-.783 0-1.424.647-1.424 1.437s.64 1.438 1.424 1.438M16.81 40.13h20.335c.783 0 1.423-.647 1.423-1.438 0-.79-.64-1.437-1.423-1.437H16.81c-.783 0-1.424.646-1.424 1.437 0 .79.64 1.438 1.424 1.438" />
            <g>
              <mask id="b"
                    fill="#fff">
                <use xlink:href="#a" />
              </mask>
              <path fill="#1DD4B6"
                    d="M2.847 11.416c0-1.186.914-2.192 2.1-2.192h1.934v35.985c0 2.779 2.29 4.983 5.042 4.983H37.37v2.06a2.142 2.142 0 0 1-2.147 2.133H4.947c-1.174 0-2.1-.947-2.1-2.133V11.416zm6.881-6.409c0-1.186 1.009-2.132 2.195-2.132H34.76v8.002c0 .79.617 1.461 1.412 1.461h7.486v32.87c0 1.187-.913 2.109-2.1 2.109H11.923c-1.175 0-2.195-.922-2.195-2.108V5.007zm27.88-.012l4.187 4.468h-4.187V4.995zM4.948 57.26h30.275a5.004 5.004 0 0 0 4.995-5.008v-2.06h1.34c2.752 0 4.947-2.216 4.947-4.983V10.865c0-.371-.118-.73-.38-.994L37.18.43A1.373 1.373 0 0 0 36.16 0H11.923C9.17 0 6.88 2.24 6.88 5.007V6.35H4.947C2.195 6.349 0 8.637 0 11.416v40.836c0 2.767 2.195 5.008 4.947 5.008z"
                    mask="url(#b)" />
            </g>
          </g>
        </svg>
      </div>
      <div class="left-part flex flex-col ml-8 justify-around sm:mr-8 sm:ml-4 sm:h-32">
        <div class="font-semibold text-xl text-blue-lighter sm:text-base">
          {{ document.description }}
        </div>
        <div class="font-bold text-2xl text-blue sm:text-xl">
          {{ document.title }}
        </div>
        <div class="text-sm text-blue italic sm:text-xs">
          {{ getDocumentType(document.description) }}
        </div>
      </div>
      <a :href="document.path" class="absolute pin-b pin-r button button--blue py-3 px-4 sm:px-2 sm:py-2 sm:text-sm" target="_blank">
        Télécharger
      </a>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DocumentCard',
  props: {
    document: {
      type: Object,
      default() {
        return {
          name: ''
        }
      }
    }
  },
  methods: {
    getDocumentType(path) {
      return path.substring(path.lastIndexOf('.') + 1).toUpperCase()
    }
  }
}
</script>

<style lang="scss" scoped>
.cardShadow {
  box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
}

.left-part {
  min-width: 200px;
}
.italic {
  font-style: italic;
}
</style>

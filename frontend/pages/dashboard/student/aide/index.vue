<template>
  <div class="flexContainer-dash bg-blue-lightest ml-32 sm:ml-0 relative">

    <h1 class="title title--dash pt-16 sm:text-4xl sm:pt-8">Besoin d'aide ?</h1>

    <div class="questions w-full flex flex-col mt-8">
      <div v-for="question in faq"
           :key="question.title"
           class="question w-full overflow-hidden bg-transparent">

        <!-- Question -->
        <div class="w-full relative bg-blue flex items-center justify-between py-8 px-4 cursor-pointer z-10"
             @click="toggleQuestion(question.id)">
          <h3 class="text-white font-bold text-2xl sm:text-xl leading-normal">{{ question.title }}</h3>
          <svg :class="[activeQuestion == question.id ? 'arrow--active' : '']"
               class="arrow"
               height="15px"
               width="15px"
               viewBox="0 0 240.823 240.823"
               fill="#FFFFFF">
            <g>
              <path d="M183.189,111.816L74.892,3.555c-4.752-4.74-12.451-4.74-17.215,0c-4.752,4.74-4.752,12.439,0,17.179 l99.707,99.671l-99.695,99.671c-4.752,4.74-4.752,12.439,0,17.191c4.752,4.74,12.463,4.74,17.215,0l108.297-108.261 C187.881,124.315,187.881,116.495,183.189,111.816z" />
            </g>
          </svg>
        </div>
        <!-- End Question -->

        <!-- Answer -->
        <transition name="slideBottom">
          <div v-if="activeQuestion == question.id"
               class="answer relative bg-blue-lightest px-4 py-6 z-0">
            <p>
              {{ question.answer }}
            </p>
          </div>
        </transition>
        <!-- End Answer -->

      </div>
    </div>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import heads from '~/config/meta.json'

export default {
  name: 'Aide',
  layout: 'dashboard_student',
  perimeters: [DashboardPerimeter],
  head() {
    if (heads[this.$options.name]) return heads[this.$options.name]
    return {
      title: 'Crée ton avenir'
    }
  },
  async asyncData({ app, route, error }) {
    try {
      let response = await app.$api.faq.getFaq()
      const faq = response.data
      return { faq: faq }
    } catch (err) {
      console.log(err)
    }
  },
  data() {
    return {
      faq: null,
      activeQuestion: null
    }
  },
  methods: {
    toggleQuestion: function(id) {
      this.activeQuestion != id
        ? (this.activeQuestion = id)
        : (this.activeQuestion = null)
    }
  }
}
</script>

<style lang="scss">
.questions {
  .question {
    z-index: 10;
    border-top: 1px solid transparent;
    &:first-child {
      border-top-left-radius: 10px;
      border-top-right-radius: 10px;
      border-top: 0;
    }
    .arrow {
      @apply ml-3;
      transition: transform 0.3s ease;
      transform: rotate(0);
    }
    .arrow--active {
      transform: rotate(90deg);
    }
  }
  .answer {
    z-index: 0;
    border: 2px solid;
    border-top-width: 0;
    @apply border-blue;
    @apply bg-white;
  }
}
</style>

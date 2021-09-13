<template>
  <div class="activity__right min-h-screen bg-white xl:w-3/5 w-full xl:pb-12 pb-16 pt-6 xl:pl-16 xl:pr-32 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Intelligences multiples</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">3.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex w-full py-12 sm:flex-wrap">
      <div class="xl:w-3/5 w-full">
        <div class="flex flex-col justify-around h-full">
          <div v-for="(intelligence, index) in slicedIntelligences" :key="intelligence.id" class="result flex items-center">
            <div :class="{ 'bg-yellow': index === 0, 'bg-peach': index === 1, 'bg-topaze': index === 2 }"
                 class="rounded-full w-24 h-24 flex items-center justify-center">
              <radial-progress-bar :diameter="80"
                                   :completed-steps="intelligence.weight / 8"
                                   :startColor="'#fff'"
                                   :stopColor="'#fff'"
                                   :strokeWidth="6"
                                   :innerStrokeColor="'#ffffff59'"
                                   :total-steps="9">
                <img :src="intelligence.img_path" class="w-full h-full p-4">
              </radial-progress-bar>
            </div>
            <div class="items-center break-words">
              <span :class="{ 'text-yellow': index === 0, 'text-peach': index === 1, 'text-topaze': index === 2 }"
                    class="text-6xl font-bold ml-8 mr-3">{{ intelligence.weight }}</span>
              <span class="text-blue font-bold text-xl">{{ intelligence.text }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="xl:w-2/5 w-full flex justify-end sm:mt-16">
        <picture>
          <img src="~/assets/img/intelligences-multiples/end.png"
               alt="Activité numéro 1">
        </picture>
      </div>
    </div>

    <div class="flex sm:flex-col">
      <button v-scroll-to="'#actTitle'"
              class="button button--white mt-8 self-start mt-3 border-blue border-2 border-solid"
              @click="restart">
        Je recommence
      </button>
      <button v-scroll-to="'#actTitle'"
              class="button button--blue mt-8 self-start ml-6 mt-3"
              @click="validate">
        Je valide mon activité
      </button>
    </div>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'IntelligencesMultiples',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    intelligences: {
      type: Array,
      default: function() {
        return { intelligences: null }
      }
    }
  },
  data() {
    return {
      questionStep: 1,
      slicedIntelligences: [],
      texts: this.getPageTexts(this.$route.name)
    }
  },
  created() {
    if (!this.intelligences) {
      this.$router.push('../intelligences-multiples')
    } else {
      this.slicedIntelligences = this.intelligences.slice(0, 3)
      this.$emit('setThirdStep')
    }
  },
  methods: {
    validate: function() {
      this.$emit('createResults')
      this.$emit('createLogs', () => {
        this.$router.push( {path: '/dashboard/student/domaines/introspection', hash: '#intelligences-multiples' })
      })
    },
    restart: function() {
      this.$router.push('/dashboard/student/domaines/introspection/intelligences-multiples')
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .questionCard {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }
}
</style>

<template>
  <div class="activity__right h-auto min-h-screen bg-white xl:w-3/5 w-full xl:pb-12 pb-16 pt-6 xl:pl-16 xl:pr-32 px-6 flex flex-col justify-center">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes compétences primaires</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">6.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex w-full py-12 sm:flex-wrap">
      <div class="xl:w-2/3 w-full">
        <div class="flex flex-col justify-around h-full">
          <div v-for="(result, key) in results"
               :key="key"
               class="result flex items-center my-2">
            <div :class="{ 'bg-yellow': key === 0, 'bg-peach': key === 1, 'bg-topaze': key === 2 }" class="flex items-center align-middle rounded-full h-20 w-20">
              <img :src="result.img_path"
                   alt="illustration d'une activité"
                   class="w-10 h-10 m-auto logo">
            </div>
            <div class="flex items-center align-middle">
              <span :class="{ 'text-yellow': key === 0, 'text-peach': key === 1, 'text-topaze': key === 2 }" class="text-6xl font-bold ml-8 mr-5 titleNumber">{{ key + 1 }}</span>
              <span class="text-blue font-bold text-xl">{{ result.text }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="xl:w-2/3 w-full flex justify-end sm:mt-16 ">
        <picture class="xl:w-5/6">
          <img src="~/assets/img/competences/end/illu.png"
               alt="Activité numéro 1">
        </picture>
      </div>
    </div>

    <div class="flex sm:flex-col">
      <button v-scroll-to="'#actTitle'"
              class="button button--white mt-8 self-start mt-3 border-blue border-2 border-solid "
              @click="restart">
        Je recommence
      </button>
      <button v-scroll-to="'#actTitle'"
              class="button button--blue mt-8 self-start xl:ml-6 mt-3"
              @click="validate">
        Je termine mon activité
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
    passedQuizz: {
      type: Boolean,
      default: function() {
        return false
      }
    },
    results: {
      type: Array,
      default: function() {
        return { results: null }
      }
    }
  },
  data() {
    return {
      questionStep: 1,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    if (!this.passedQuizz || !this.results)
      this.$router.push('../competences')

    this.$emit( 'setStep', 5 )
  },
  methods: {
    validate: function() {
      this.$emit('createResults')
      this.$emit( 'createLogs', () => {
        this.$router.push( {path: '/dashboard/student/domaines/introspection', hash: '#competences' })
      })
    },
    restart: function() {
      this.$router.push( '/dashboard/student/domaines/introspection/competences' )
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .titleNumber {
    transform: translateY(4px);
  }
  .logo {
    transform: translateY(-2px);
  }
}
</style>

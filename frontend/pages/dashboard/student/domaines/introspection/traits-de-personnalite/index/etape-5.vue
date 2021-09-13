<template>
  <div class="activity__right min-h-screen bg-white xl:w-3/5 min-h-screen w-full xl:pb-12 pb-16 pt-6 xl:pl-16 xl:pr-32 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Traits de personnalité</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4  mb-6">6.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4 ">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex w-full py-12 sm:flex-wrap">
      <div class="xl:w-2/3 w-full ml-16">
        <div class="flex flex-col justify-around h-full">
          <div v-for="(personnalite, key) in personnalites" :key="key" class="result flex items-center">
            <div :class="{'bg-topaze': key === 0, 'bg-peach': key === 1, 'bg-yellow': key === 2, 'sm:my-6': key === 1}" class="flex justify-center align-middle rounded-full p-4 perso-icon">
              <img src="~/static/icons/check.svg" class="w-full h-full">
            </div>

            <div class="flex items-center">
              <span :class="{'text-topaze': key === 0, 'text-peach': key === 1, 'text-yellow': key === 2}" class="text-6xl font-bold ml-8 mr-3">{{ key + 1 }}</span>
              <div class="text-blue font-bold text-xl ml-3 leading-normal">Je suis <br>{{ personnalite.desc }}</div>
            </div>
          </div>

        </div>
      </div>
      <div class="xl:w-2/3 w-full flex justify-end sm:mt-16">
        <picture>
          <img src="~/assets/img/traits-de-personnalites/end.png"
               alt="Activité numéro 1">
        </picture>
      </div>
    </div>

    <div class="flex sm:flex-col ml-16">
      <button v-scroll-to="'#actTitle'"
              class="button button--blue mt-8 self-start mt-3"
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
    results: {
      type: Array,
      default: function() {
        return { results: null }
      }
    }
  },
  data() {
    return {
      personnalites: [],
      texts: this.getPageTexts(this.$route.name)
    }
  },
  created() {
    this.$emit( 'setThirdStep' )
    this.personnalites = Array.prototype.slice.call(this.results).sort(function ( a, b ) {
      return b.count - a.count
    }).slice(0, 3)
  },
  methods: {
    validate: function () {
      const endResults = []
      Array.prototype.slice.call(this.results).filter(function(result) {
        return result.count > 0
      }).sort(function ( a, b ) {
        return b.count - a.count
      }).forEach(function (result) {
        [...Array(result.count)].forEach((_, i) => {
          endResults.push(result.id)
        })
      })

      this.$emit( 'setEndResults', endResults)
      this.$emit('createResults')
      this.$emit('createLogs', () => {
        this.$router.push( {path: '/dashboard/student/domaines/introspection', hash: '#traits-de-personnalite' })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .questionCard {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }

  .perso-icon {
    width: 67px;
    height: 67px;
  }
}
</style>

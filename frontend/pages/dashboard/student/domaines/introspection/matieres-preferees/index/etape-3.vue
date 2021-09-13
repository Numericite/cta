<template>
  <div class="activity__right min-h-screen bg-white xl:w-3/5 w-full xl:pb-12 pb-16 pt-6 xl:pl-16 xl:pr-32 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes matières préférées</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">3.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex w-full py-12 sm:flex-wrap">
      <div class="xl:w-1/2 w-full">
        <div class="flex flex-col justify-around h-full">
          <div v-for="(matiere, key) in matieresPref.slice(0,3)"
               :key="key"
               class="result flex items-center">
            <img :src="matiere.img_path"
                 :class="{ 'bg-yellow': key === 0, 'bg-peach': key === 1, 'bg-topaze': key === 2 }"
                 alt="illustration d'une activité"
                 class="p-2 rounded-full h-20 w-20 logo">
            <div class="flex items-center">
              <span :class="{ 'text-yellow': key === 0, 'text-peach': key === 1, 'text-topaze': key === 2 }" class="text-6xl font-bold ml-8 mr-5 titleNumber">{{ key + 1 }}</span>
              <span class="text-blue font-bold text-xl leading-normal">{{ matiere.text }}</span>
            </div>
          </div>

        </div>
      </div>
      <div class="xl:w-1/2 xl:ml-3 w-full flex justify-end sm:mt-16">
        <picture>
          <img src="~/assets/img/matieres-preferees/illu.png"
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
              class="button button--blue mt-8 self-start mt-3 ml-6"
              @click="validate">
        Je termine mon activité
      </button>
    </div>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'MatieresPreferees',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    endResults: {
      type: Object,
      default: function() {
        return { endResults: null }
      }
    },
    results: {
      type: Array,
      default: function () {
        return { results: null }
      }
    }
  },
  data() {
    return {
      matieresPref: [{},{},{}],
      questionStep: 1,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    if ( !this.results )
      this.$router.push( '../matieres-preferees' )

    this.$emit('setStep', 5)

    this.matieresPref = this.results.filter(function(result) {
      return (result.value && result.value > 0)
    }).sort(this.compareValues)
  },
  methods: {
    compareValues: function(a, b) {
      const valueA = a.value;
      const valueB = b.value;

      let comparison = 0;
      if (valueA > valueB) {
        comparison = 1;
      } else if (valueA < valueB) {
        comparison = -1;
      }
      return comparison;
    },
    validate: function() {
      this.$emit('createResults')
      this.$emit('createLogs', () => {
        this.$router.push( {path: '/dashboard/student/domaines/introspection', hash: '#matieres-preferees' })
      })
    },
    restart: function() {
      this.$router.push('/dashboard/student/domaines/introspection/matieres-preferees')
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .titleNumber {
    transform: translateY(4px)
  }
  .logo {
    transform: translateY(-4px);
  }
}
</style>

<template>
  <div class="activity__right bg-blue-lightest min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes matières préférées</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">2.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex flex-no-wrap flex-row sm:flex-wrap items-end min-h-3/5 sm:h-auto my-8">
      <div class="card w-1/3 cardShadow min-h-1/2 sm:w-full m-4 rounded-xlg overflow-hidden flex flex-col">
        <div class="card-head bg-peach flex px-2 py-1 items-center relative">
          <div class="w-3/5 flex flex-col items-start py-2 pl-2 pr-2">
            <div class="text-5.5xl text-white font-bold w-full">{{ matieresPref[1].value }}</div>
            <div class="text-white font-bold w-full break-words pr-2 leading-normal">{{ matieresPref[1].text }}</div>
          </div>
          <div class="pin-r m-3 mt-1 w-2/5">
            <img :src="matieresPref[1].img_path"
                 alt="illustration d'une activité"
                 class="opacity-75 w-full">
          </div>
        </div>
        <div class="p-3">
          <div class="card-body second flex flex-wrap xl:h-72 overflow-y-auto overflow-x-hidden">
            <div class="flex items-center flex-wrap text-peach mb-2">
              <span v-for="action in matieresPref[1].actions"
                    :key="action.id"
                    class="choice-action border-peach w-full"
                    @click="action.value = !action.value">
                <svg width="12"
                     height="15"
                     class="mr-3 w-1/6"
                     viewBox="0 0 100 125"
                     x="0px"
                     y="0px">
                  <path :fill="[action.value ? '#ff9c9c' : '#fff']"
                        d="M50,94,9.3,53.3A27.71,27.71,0,0,1,48.49,14.11L50,15.63l1.51-1.52A27.71,27.71,0,0,1,90.7,53.3Z"
                        y="115"
                        stroke="#ff9c9c"
                        stroke-width="10" />
                </svg>
                <span class="w-5/6">{{ action.text }}</span>
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="card w-1/3 cardShadow min-h-2/3 sm:w-full  m-4 rounded-xlg overflow-hidden flex flex-col">
        <div class="card-head bg-yellow flex px-2 py-1 items-center relative">
          <div class="w-3/5 flex flex-col items-start py-2 pl-2 pr-2">
            <div class="text-5.5xl text-white font-bold w-full">{{ matieresPref[0].value }}</div>
            <div class="text-white font-bold w-full break-words pr-2 leading-normal">{{ matieresPref[0].text }}</div>
          </div>
          <div class="pin-r m-3 mt-1 w-2/5">
            <img :src="matieresPref[0].img_path"
                 alt="illustration d'une activité"
                 class="w-full">
          </div>
        </div>
        <div class="p-3">
          <div class="card-body first flex flex-wrap xl:h-80 overflow-y-auto overflow-x-hidden">
            <div class="flex items-center flex-wrap text-yellow mb-2">
              <span v-for="action in matieresPref[0].actions"
                    :key="action.id"
                    class="choice-action border-yellow w-full"
                    @click="action.value = !action.value">
                <svg width="12"
                     height="15"
                     class="mr-3 w-1/6"
                     viewBox="0 0 100 125"
                     x="0px"
                     y="0px">
                  <path :fill="[action.value ? '#ffc600' : '#fff']"
                        d="M50,94,9.3,53.3A27.71,27.71,0,0,1,48.49,14.11L50,15.63l1.51-1.52A27.71,27.71,0,0,1,90.7,53.3Z"
                        y="115"
                        stroke="#ffc600"
                        stroke-width="10" />
                </svg>
                <span class="w-5/6">{{ action.text }}</span>
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="card w-1/3 cardShadow min-h-1/2  sm:w-full  m-4 rounded-xlg overflow-hidden flex flex-col">
        <div class="card-head bg-topaze flex px-2 py-1 items-center relative">
          <div class="w-3/5 flex flex-col items-start py-2 pl-2 pr-2">
            <div class="text-5.5xl text-white font-bold w-full">{{ matieresPref[2].value }}</div>
            <div class="text-white font-bold w-full break-words pr-2 leading-normal">{{ matieresPref[2].text }}</div>
          </div>
          <div class="pin-r m-3 mt-1 w-2/5">
            <img :src="matieresPref[2].img_path"
                 alt="illustration d'une activité"
                 class="opacity-75 w-full">
          </div>
        </div>
        <div class="p-3">
          <div class="card-body third flex flex-wrap xl:h-64 overflow-y-auto overflow-x-hidden">
            <div class="flex items-center flex-wrap text-topaze mb-2">
              <span v-for="action in matieresPref[2].actions"
                    :key="action.id"
                    class="choice-action border-topaze w-full"
                    @click="action.value = !action.value">
                <svg width="12"
                     height="15"
                     class="mr-3 w-1/6"
                     viewBox="0 0 100 125"
                     x="0px"
                     y="0px">
                  <path :fill="[action.value ? '#1dd4b6' : '#fff']"
                        d="M50,94,9.3,53.3A27.71,27.71,0,0,1,48.49,14.11L50,15.63l1.51-1.52A27.71,27.71,0,0,1,90.7,53.3Z"
                        y="115"
                        stroke="#1dd4b6"
                        stroke-width="10" />
                </svg>
                <span class="w-5/6">{{ action.text }}</span>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            class="button button--blue mt-8 self-start mt-3"
            @click="nextStep">
      Je Valide
    </button>

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
    actions: {
      type: Array,
      default: function () {
        return { actions: null }
      }
    },
    results: {
      type: Array,
      default: function () {
        return { results: null }
      }
    },
    activityLogs: {
      type: Object,
      default: function() {
        return {}
      }
    }
  },
  data() {
    return {
      matieresPref: [{},{},{}],
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    if ( !this.results )
      this.$router.push( '../matieres-preferees' )

    this.$emit( 'setStep', 5 )

    const groupedActions = this.groupBy(this.actions, 'selection_id')

    this.matieresPref = this.results.filter(function(result) {
      return (result.value && result.value > 0)
    }).sort(this.compareValues)

    this.matieresPref.forEach(function(matiere) {
      matiere.actions = groupedActions[matiere.id] || []
    })

    this.activityLogs.selection_ids = this.matieresPref.map(function(matiere) {
      return matiere.id
    })

    if (this.activityLogs.choice_ids) {
      const choice_ids = this.activityLogs.choice_ids
      this.matieresPref.forEach(function(matiere) {
        matiere.actions.forEach(function(action) {
          action.value = (choice_ids.indexOf(action.id) !== -1)
        })
      })
    }
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
    groupBy: function (xs, key) {
      return xs.reduce(function(rv, x) {
        (rv[x[key]] = rv[x[key]] || []).push(x);
        return rv;
      }, {});
    },
    nextStep: function() {
      const endResults = []
      this.matieresPref.forEach(function(matiere) {
        matiere.actions.forEach(function(action) {
          if (action.value) {
            endResults.push(action.id)
          }
        })
      })
      this.$emit( 'setEndResults', endResults )

      this.activityLogs.choice_ids = endResults
      this.$emit( 'setActivityLogs', this.activityLogs)
      this.$router.push( 'etape-3' )
    }
  }
}
</script>

<style lang="scss" scoped>
.activity__right {

  .card-body {
    &::-webkit-scrollbar
    {
      width: 6px;
    }

    &::-webkit-scrollbar-track
    {
      @apply bg-blue-lightest;
      border-radius: 50px;
    }

    &::-webkit-scrollbar-thumb {
      border-radius: 50px;
    }

    &.first::-webkit-scrollbar-thumb
    {
      @apply bg-yellow;
    }

    &.second::-webkit-scrollbar-thumb
    {
      @apply bg-peach;
    }

    &.third::-webkit-scrollbar-thumb
    {
      @apply bg-topaze;
    }

  }

  .card-head {
    min-height: 110px;
  }

  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }
  .text-misaligned {
    transform: translateY(1px);
  }

  .choice-action {
    @apply flex items-center font-bold uppercase text-xs mr-2 mb-2 border-solid border rounded-full p-2 cursor-pointer;
  }
}
</style>

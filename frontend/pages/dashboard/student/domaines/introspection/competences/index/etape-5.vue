<template>
  <div class="activity__right min-h-screen bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex competences-center underline">Mes compétences primaires</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">5.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex flex-no-wrap mt-4 items-stretch sm:overflow-x-scroll">
      <div v-for="(result, key) in results" :key="key" class="flex items-stretch xl:w-1/3 sm:w-full flex-no-shrink p-4">
        <div class="flex flex-col items-stretch bg-white rounded-xlg w-full overflow-hidden">
          <div :class="{ 'bg-yellow': key === 0, 'bg-peach': key === 1, 'bg-topaze': key === 2 }"
               class="h-32 flex-none flex">
            <div class="w-1/2 flex flex-wrap items-center justify-center p-4">
              <div class="w-full text-white font-bold text-5.5xl ml-2">
                {{ key + 1 }}
                <picture>
                  <svg xmlns="http://www.w3.org/2000/svg"
                       width="17"
                       height="13"
                       viewBox="0 0 17 13">
                    <g fill="none"
                       fill-rule="nonzero">
                      <circle cx="6.5"
                              cy="6.5"
                              r="6.5"
                              fill="#FFF"
                              opacity=".381" />
                      <path stroke="#FFF"
                            stroke-linecap="round"
                            stroke-width="2"
                            d="M3.28 5l4.36 4 7.99-7" />
                    </g>
                  </svg>
                </picture>
              </div>
              <div class="w-full text-white font-bold leading-normal ">
                {{ result.text }}
              </div>
            </div>
            <div class="w-1/2 flex items-center justify-end relative mr-4">
              <div class="flex justify-center h-20 w-20 items-center relative">
                <picture class="p-11 rounded-full border-6 border-solid border-white opacity-50" />
                <img :src="result.img_path" class="absolute pin-t pin-l">
              </div>
            </div>
          </div>
          <div class="p-4 overflow-auto min-h-2/5">
            <div class="flex items-center flex-wrap mb-2">
              <span v-for="action in result.actions"
                    :key="action.id"
                    :class="{'text-yellow boder-yellow': key === 0, 'text-peach border-peach': key === 1, 'text-topaze border-topaze': key === 2}"
                    class="choice-action w-full"
                    @click="action.like = !action.like">
                <svg width="12"
                     height="15"
                     class="mr-3 w-1/6"
                     viewBox="0 0 100 125"
                     x="0px"
                     y="0px">
                  <path :fill="[action.like ? (key === 0 ? '#ffc600' : (key === 1 ? '#ff9c9c' : (key === 2 ? '#1dd4b6' : '#fff'))) : '#fff']"
                        :stroke="key === 0 ? '#ffc600' : (key === 1 ? '#ff9c9c' : (key === 2 ? '#1dd4b6' : '#fff'))"
                        d="M50,94,9.3,53.3A27.71,27.71,0,0,1,48.49,14.11L50,15.63l1.51-1.52A27.71,27.71,0,0,1,90.7,53.3Z"
                        y="115"
                        stroke-width="10" />
                </svg>
                <span class="w-5/6">{{ action.text }}</span>
              </span>
            </div>
          </div>
          <div class="flex flex-col items-stretch h-full">
            <div class="flex w-full p-4 flex-grow items-stretch min-h-48">
              <textarea class="bg-white text-blue h-full"
                        placeholder="Exemples : Je planifie mon travail. Je range mes affaires. Je respecte les délais. J’organise mes activités. Je fais preuve de rigueur." />

            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="block">
      <button class="button button--white bg-transparent self-start mt-8 border-blue border-2 border-solid"
              @click="goBack">
        Je modifie ma selection
      </button>
      <button class="button button--blue inline-block mt-8 self-start mt-3"
              @click="nextStep">
        Je Valide
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
        return []
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
      count: 0,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    if (!this.passedQuizz || !this.results)
      this.$router.push('../competences')

    if (this.activityLogs.choice_ids) {
      const activityLogs = this.activityLogs
      this.results.forEach(function(competence) {
        competence.actions.forEach(function(action) {
          action.like = activityLogs.choice_ids.indexOf(action.id) !== -1
        })
      })
    }

    this.$emit( 'setStep', 5 )
  },
  methods: {
    nextStep: function() {
      let action_ids = []

      this.results.forEach(function(competence) {
        competence.validatedActions = _.filter(competence.actions, (action) => { return action.like })
        action_ids = _.concat(action_ids, _.map(competence.validatedActions, 'id'))
      })

      this.activityLogs.choice_ids = action_ids
      this.$emit('setActivityLogs', this.activityLogs)

      const endResults = this.results.map(function (result) {
        return result.id
      })
      this.$emit( 'setEndResults', endResults )
      this.$router.push( 'etape-6' )
    },
    goBack() {
      this.$router.push( 'etape-4' )
    }
  }
}
</script>

<style lang="scss" scoped>
.activity__right {
  .competence-checkbox-position {
    position: absolute;
    top: -5px;
    right: 0px;
  }

  .radial-progress-inner {
    width: 125px;
  }
  .radial-progress-inner img{
    width: 60px;
  }
  .card-head {
    height: 106px;
  }

  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }

  .cursor-grab {
    cursor: grab;
  }

  .reduce-size {
    transform: scale(0.8);
  }

  .competences {
    transition: all 0.3s;
    &:hover {
      box-shadow: 0 10px 25px 5px rgba(14, 7, 51, 0.1);
    }
  }

  .heart {
    transform: translateY(1px);
  }

  .choice-action {
    @apply flex items-center font-bold uppercase text-xs mr-2 mb-2 border-solid border rounded-full p-2 cursor-pointer;
  }
}
</style>

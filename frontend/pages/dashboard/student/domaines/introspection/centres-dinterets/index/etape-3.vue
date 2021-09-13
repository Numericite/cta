<template>
  <div class="activity__right min-h-screen bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes centres d'intérêts</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">3.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex flex-no-wrap flex-row sm:flex-wrap sm:h-auto mt-8">
      <div v-for="result in results" :key="result.id"
           class="card xl:w-1/3 xs:w-full cardShadow h-full sm:w-full m-4 rounded-xlg overflow-hidden">
        <div :style="{backgroundColor: result.color}" class="card-head flex p-2 items-center relative">
          <div class="heart absolute pin-t pin-r mt-5 mr-2">
            <svg xmlns="http://www.w3.org/2000/svg"
                 width="11"
                 height="10"
                 viewBox="0 0 11 10"
                 class="mr-2 h-6 w-6 fill-current opacity-45 text-white">
              <path d="M5.9 1.514C6.449.556 7.093.01 8.258.01a2.594 2.594 0 0 1 2.593 2.593c0 2.62-2.893 5.347-4.784 7.239a.237.237 0 0 1-.334 0C3.842 7.95.95 5.222.95 2.602.95.33 3.6-.686 5.09.505c.309.247.567.584.811 1.01z" />
            </svg>
          </div>
          <div class="flex flex-wrap items-center content-center w-full">
            <div class="icon w-full flex justify-center mb-6">
              <img :src="result.img_path"
                   alt="illustration d'un centre d'interet">
            </div>
            <div class="title w-full text-white uppercase font-bold flex justify-center">
              {{ result.text }}
            </div>
          </div>
        </div>
        <div class="card-body justify-center items-center p-3">
          <div class="flex flex-wrap text-blue-lighter text-xs p-4">
            <span class="leading-normal h-48 truncate text-xs" v-html="result.displayDescription"/>
          </div>
          <div v-if="result.validatedActions.length > 0"
               class="flex flex-wrap items-center justify-center">
            <div class="flex w-full justify-center">
              <div :style="{backgroundColor: result.color}" class="flex justify-center items-center text-xs p-2 rounded-full uppercase font-semibold text-white">
                <div class="heart">
                  <svg xmlns="http://www.w3.org/2000/svg"
                       viewBox="0 0 11 10"
                       class="mr-1 h-4 w-4 fill-current opacity-45 text-white">
                    <path d="M5.9 1.514C6.449.556 7.093.01 8.258.01a2.594 2.594 0 0 1 2.593 2.593c0 2.62-2.893 5.347-4.784 7.239a.237.237 0 0 1-.334 0C3.842 7.95.95 5.222.95 2.602.95.33 3.6-.686 5.09.505c.309.247.567.584.811 1.01z" />
                  </svg>
                </div>
                <span class="">{{ result.validatedActions.length }} activité<span v-if="result.validatedActions.length > 1">s</span></span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="flex flex-wrap sm:h-auto my-4 sm:justify-center sm:ml-0">
      <div v-for="(activity, key) in activities"
           :key="key"
           :class="{ 'bg-blue-grayish border-transparent': (activity.count < 2), ' border-orange border-transparent' : (activity.count > 1)}"
           class="bg-white border-solid border-3 m-3 text-sm pl-6 pr-2 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center activity action-label">
        {{ activity.text }}
        <div class="heart ml-3">
          <svg :class="{ 'text-orange opacity-45': (activity.count > 1), 'text-blue-lighter' : (activity.count < 2) }"
               xmlns="http://www.w3.org/2000/svg"
               width="11"
               height="10"
               viewBox="0 0 11 10"
               class="mr-2 h-6 w-6 fill-current">
            <path d="M5.9 1.514C6.449.556 7.093.01 8.258.01a2.594 2.594 0 0 1 2.593 2.593c0 2.62-2.893 5.347-4.784 7.239a.237.237 0 0 1-.334 0C3.842 7.95.95 5.222.95 2.602.95.33 3.6-.686 5.09.505c.309.247.567.584.811 1.01z" />
          </svg>
        </div>
      </div>
    </div>

    <div class="flex sm:flex-col">

      <button class="button button--white bg-transparent mt-8 self-start mt-3 border-blue border-2 border-solid"
              @click="restart">
        Je recommence ma selection
      </button>
      <button class="button button--blue mt-8 self-start mt-3 ml-6 sm:w-full sm:ml-0"
              @click="nextStep">
        Je valide
      </button>
    </div>
  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'CentresDinterets',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
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
      activities: [],
      texts: this.getPageTexts(this.$route.name)
    }
  },
  created() {
    if (!this.results) {
      this.$router.push('../centres-dinterets')
    } else {
      this.$emit('setStep', 8)
      this.activities = this.results[0].actions
      this.activities.forEach(function(activity) {
        activity.count = 0
      });
      const activities = this.activities
      this.results.forEach(function(result) {
        result.validatedActions.forEach(function(validatedAction) {
          const activity = activities.find(function(activity) {
            return validatedAction.text === activity.text
          })

          if (activity)
            activity.count++
        })
      })
    }
  },
  methods: {
    nextStep: function() {
      const endResults = []
      this.results.forEach(function(result) {
        result.validatedActions.forEach(function(action) {
          if (action.value) {
            endResults.push(action.id)
          }
        })
      })
      this.$emit( 'setEndResults', endResults )

      this.activityLogs.choice_ids = endResults
      this.$emit( 'setActivityLogs', this.activityLogs )

      this.$router.push( 'etape-4' )
    },
    restart() {
      this.$router.push( 'etape-2' )
    }
  }
}
</script>

<style lang="scss" scoped>
.activity__right {
  .card-head {
    height: 103px;
  }
  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }
  .text-misaligned {
    transform: translateY(1px);
  }
  .heart {
    transform: translateY(1px);
  }

  .action-label {
    max-width: 90vw;
  }
}
</style>

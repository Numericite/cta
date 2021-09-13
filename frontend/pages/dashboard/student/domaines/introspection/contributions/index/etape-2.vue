<template>
  <div class="activity__right bg-blue-lightest xl:min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes contributions</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">2.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex flex-no-wrap flex-row sm:flex-wrap items-stretch items-end h sm:h-auto mt-8">
      <div v-for="(result, key) in results" :key="key"
           class="card xl:w-1/3 xs:w-full cardShadow h-full sm:w-full m-4 rounded-xlg overflow-hidden">
        <div :class="{'bg-yellow': key === 0, 'bg-peach': key === 1, 'bg-topaze': key === 2}" class="card-head flex p-2 items-center relative">
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
            <div class="icon w-full justify-center mb-6">
              <img :src="result.img_path"
                   class="block mx-auto w-12"
                   alt="illustration d'une mission">
            </div>
            <div class="title w-full text-white uppercase font-bold flex justify-center">
              {{ result.text }}
            </div>
          </div>
        </div>
        <div class="p-4">
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
        <div class="p-4 pb-6">
          <textarea :placeholder="'Exemples : \n' + result.description"
                    name=""
                    cols="30"
                    rows="10" />
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            class="button button--blue mt-8 self-start mt-3 sm:w-full sm:ml-0"
            @click="nextStep">
      Je valide
    </button>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import _ from 'lodash'

export default {
  name: 'MesContributions',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    results: {
      type: Array,
      default: function() {
        return null
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
      texts: this.getPageTexts(this.$route.name)
    }
  },
  computed: {
  },
  created() {
    if (!this.results)
      this.$router.push('../contributions')

    if (this.activityLogs.choice_ids) {
      const activityLogs = this.activityLogs
      this.results.forEach(function(mission) {
        mission.actions.forEach(function(action) {
          action.like = activityLogs.choice_ids.indexOf(action.id) !== -1
        })
      })
    }
  },
  methods: {
    nextStep: function() {
      let action_ids = []

      this.results.forEach(function(mission) {
        mission.validatedActions = _.filter(mission.actions, (action) => { return action.like })
        action_ids = _.concat(action_ids, _.map(mission.validatedActions, 'id'))
      })

      this.activityLogs.choice_ids = action_ids
      this.$emit('setActivityLogs', this.activityLogs)
      this.$emit('sendResults', this.results)

      this.$router.push( 'etape-3' )
    }
  }
}
</script>

<style lang="scss" scoped>
.activity__right {
  .card-head {
    height: 103px;
  }

  .button.activity {
    transform: scale(0.8);
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

  .choice-action {
    @apply flex items-center font-bold uppercase text-xs mr-2 mb-2 border-solid border rounded-full p-2 cursor-pointer;
  }
}
</style>

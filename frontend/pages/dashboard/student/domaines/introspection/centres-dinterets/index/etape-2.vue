<template>
  <div class="activity__right bg-blue-lightest xl:min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes centres d'intérêts</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">2.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex flex-no-wrap flex-row sm:flex-wrap items-stretch items-end h sm:h-auto mt-8">
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
            <span class="leading-normal h-48 truncate" v-html="result.displayDescription"/>
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
            <button class="button button--white border-blue activity select self-start mt-2"
                    @click="goToActivity(result)">
              Je recommence
            </button>
          </div>
          <div v-else class="flex flex-wrap items-center justify-center">
            <button class="button button--blue activity select self-start mt-3 mb-5"
                    @click="goToActivity(result)">
              Mes activités
            </button>
          </div>
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            :disabled="!allDone"
            class="button button--blue mt-8 self-start mt-3 sm:w-full sm:ml-0"
            @click="nextStep">
      Je compare les résultats
    </button>

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
    allDone() {
      let i = 0;

      if (this.results) {
        this.results.forEach( element => {
          i += (element.validatedActions.length > 0) ? 1 : 0
        } );
      }
      this.$emit( 'setStep', i + 5 )
      return i >= 3
    }
  },
  created() {
    if (!this.results)
      this.$router.push('../centres-dinterets')

    this.results.forEach(function(result) {
      result.validatedActions = result.actions.filter(function(action) {
        return action.value
      });
    });

    if (this.activityLogs.choice_ids) {
      delete this.activityLogs.choice_ids
      this.$emit('setActivityLogs', this.activityLogs)
    }
  },
  methods: {
    nextStep: function() {
      this.$router.push( 'etape-3' )
    },
    goToActivity(result) {
      this.$router.push({ name: 'dashboard-student-domaines-introspection-centres-dinterets-index-choices-id', params : {id: result.id } })
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
}
</style>

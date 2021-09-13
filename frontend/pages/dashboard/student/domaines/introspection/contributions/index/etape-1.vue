<template>
  <div class="activity__right relative bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:pl-16 px-6 xl:pr-32 flex flex-col justify-center min-h-screen">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes contributions</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4 sm:text-3xl">1.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal xl:mt-4 sm:text-xl">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex flex-wrap mt-8">
      <div v-for="(mission, key) in missions"
           :key="key"
           class="p-4 w-1/2 sm:w-full flex flex-grow"
           @click="mission.like = (!allChecked ? !mission.like : false)">

        <div :class="allChecked && !mission.like ? 'disabled' : 'cursor-pointer'"
             class="card cardShadow mission rounded-xlg w-full p-6">
          <div class="flex h-24 mb-2">
            <span :class="allChecked && !mission.like ? 'text-blue-lighter' : 'text-blue'"
                  class="w-3/4 uppercase text-blue font-bold text-lg">{{ mission.text }}</span>
            <span class="w-1/4 text-right heart">
              <svg :class="allChecked && !mission.like ? 'text-blue-lighter' : 'text-orange'"
                   xmlns="http://www.w3.org/2000/svg"
                   width="11"
                   height="10"
                   viewBox="0 0 11 10"
                   class=" h-5 w-5 fill-current">
                <path v-if="mission.like"
                      d="M5.9 1.514C6.449.556 7.093.01 8.258.01a2.594 2.594 0 0 1 2.593 2.593c0 2.62-2.893 5.347-4.784 7.239a.237.237 0 0 1-.334 0C3.842 7.95.95 5.222.95 2.602.95.33 3.6-.686 5.09.505c.309.247.567.584.811 1.01z" />
                <path v-else
                      d="M5.5 9.964a.454.454 0 0 1-.206-.06C5.088 9.763.344 6.322.069 3.19-.011 2.31.264 1.524.882.87 1.753-.036 2.876-.202 4.148.393a6.026 6.026 0 0 1 1.34.893A6.026 6.026 0 0 1 6.83.393c1.272-.595 2.406-.44 3.266.476.619.655.894 1.429.813 2.321-.275 3.131-5.03 6.572-5.225 6.715a.309.309 0 0 1-.183.06zM2.75.774c-.458 0-.928.166-1.352.607-.47.5-.676 1.06-.619 1.726.103 1.143.974 2.56 2.532 4.12A23.134 23.134 0 0 0 5.5 9.142c.894-.679 4.503-3.572 4.72-6.036.058-.667-.137-1.238-.618-1.726C8.95.702 8.124.595 7.127 1.06c-.79.369-1.375.976-1.386.976a.35.35 0 0 1-.505 0C5.18 1.976 4 .774 2.75.774z" />
              </svg>
            </span>
          </div>
          <div class="desc flex flex-wrap text-blue-lighter text-xs mt-4">
            <span class="leading-normal truncate" v-html="mission.description"/>
          </div>
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            class="button button--blue mt-8 self-start ml-6 mt-3"
            @click="nextPage">
      Je continue
    </button>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'


export default {
  name: 'MesContributions',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    missions: {
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
      texts: this.getPageTexts(this.$route.name)
    }
  },
  computed: {
    allChecked() {
      let i = 0;

      this.missions.forEach( element => {
        i += element.like ? 1 : 0
      } );
      this.$emit( 'setStep', i + 1 )
      return i > 2
    }
  },
  mounted() {
    this.$emit( 'setStep', 1 )

    if (this.activityLogs.selection_ids) {
      const selection_ids = this.activityLogs.selection_ids
      this.missions.forEach(function(mission) {
        mission.like = selection_ids.indexOf(mission.id) !== -1
      })
    }
  },
  methods: {
    nextPage() {
      const results = this.missions.filter(function(mission) {
        return mission.like
      })
      this.$emit('sendResults', results)

      this.activityLogs.selection_ids = results.map(function(result) {
        return result.id
      })
      this.$emit('setActivityLogs', this.activityLogs)
      this.$router.push( '/dashboard/student/domaines/introspection/contributions/etape-3' )
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }

  .cursor-grab {
    cursor: grab;
  }
}
</style>

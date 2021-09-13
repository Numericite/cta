<template>
  <div class="activity__right relative bg-blue-lightest min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:pl-16 px-6 xl:pr-32 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes premières pistes</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4 sm:text-3xl">2.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal xl:mt-4 sm:text-xl ">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex flex-wrap mt-8">
      <div v-for="(actionVerb, key) in actionVerbs"
           :key="key"
           class="p-4 w-1/3 sm:w-full flex flex-none"
           @click="actionVerb.like = (!allChecked ? !actionVerb.like : false)">

        <div :class="allChecked && !actionVerb.like ? 'disabled' : 'cursor-pointer'"
             class="card cardShadow actionVerb rounded-xlg w-full p-6">
          <div class="flex h-14 mb-2">
            <span :class="allChecked && !actionVerb.like ? 'text-blue-lighter' : 'text-blue'"
                  class="w-3/4 uppercase text-blue font-bold text-lg">{{ actionVerb.name }}</span>
            <span class="w-1/4 text-right heart">
              <svg :class="allChecked && !actionVerb.like ? 'text-blue-lighter' : 'text-orange'"
                   xmlns="http://www.w3.org/2000/svg"
                   width="11"
                   height="10"
                   viewBox="0 0 11 10"
                   class=" h-5 w-5 fill-current">
                <path v-if="actionVerb.like"
                      d="M5.9 1.514C6.449.556 7.093.01 8.258.01a2.594 2.594 0 0 1 2.593 2.593c0 2.62-2.893 5.347-4.784 7.239a.237.237 0 0 1-.334 0C3.842 7.95.95 5.222.95 2.602.95.33 3.6-.686 5.09.505c.309.247.567.584.811 1.01z" />
                <path v-else
                      d="M5.5 9.964a.454.454 0 0 1-.206-.06C5.088 9.763.344 6.322.069 3.19-.011 2.31.264 1.524.882.87 1.753-.036 2.876-.202 4.148.393a6.026 6.026 0 0 1 1.34.893A6.026 6.026 0 0 1 6.83.393c1.272-.595 2.406-.44 3.266.476.619.655.894 1.429.813 2.321-.275 3.131-5.03 6.572-5.225 6.715a.309.309 0 0 1-.183.06zM2.75.774c-.458 0-.928.166-1.352.607-.47.5-.676 1.06-.619 1.726.103 1.143.974 2.56 2.532 4.12A23.134 23.134 0 0 0 5.5 9.142c.894-.679 4.503-3.572 4.72-6.036.058-.667-.137-1.238-.618-1.726C8.95.702 8.124.595 7.127 1.06c-.79.369-1.375.976-1.386.976a.35.35 0 0 1-.505 0C5.18 1.976 4 .774 2.75.774z" />
              </svg>
            </span>
          </div>
          <div class="desc flex flex-wrap text-blue-lighter text-xs">
            <span class="leading-normal" v-html="actionVerb.description"/>
          </div>
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            :disabled="!allChecked"
            class="button button--blue mt-8 self-start ml-6 mt-3"
            @click="nextPage">
      Je valide
    </button>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import _ from 'lodash'

  export default {
    name: 'PremieresPistes',
    layout: 'activity',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    props: {
      actionVerbs: {
        type: Array,
        default: function() {
          return { questions: null }
        }
      },
      activityLogs: {
        type: Object,
        default: function() {
          return {
            nbActionVerbs: null
          }
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
        return _.filter(this.actionVerbs, {like: true}).length === 3
      }
    },
    created() {
      if ( !this.activityLogs.selection_ids )
        this.$router.push( '../premieres-pistes' )

      if ( this.activityLogs.choice_ids ) {
        const choice_ids = this.activityLogs.choice_ids
        this.actionVerbs.forEach( function( actionVerb ) {
          actionVerb.like = ( choice_ids.indexOf( actionVerb.id ) !== -1 )
        } );
      } else {
        this.actionVerbs = _.map(this.actionVerbs, function(actionVerb) {
          return _.extend({}, actionVerb, {like: false});
        });
      }
    },
    methods: {
      nextPage() {
        this.activityLogs.choice_ids = _.map(_.filter(this.actionVerbs, {like: true}), 'id')
        this.$emit( 'setActivityLogs', this.activityLogs )
        this.$router.push( '/dashboard/student/domaines/introspection/premieres-pistes/etape-3' )
      },
      changeValue(actionVerb) {
        actionVerb.value = !actionVerb.value
      }
    }
  }
</script>

<style lang="scss" scoped>
  .activity {
    .cardShadow {
      box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
    }

    .mobile-matiere-pref {
      @apply border-dashed text-sm w-full border-white h-12 ml-4 border-2 rounded-full text-white justify-center text-center font-semibold items-center cursor-pointer;
    }

    .mobile-matiere-pref-selected {
      @apply bg-white text-sm text-blue font-semibold text-center h-12 w-full p-4 ml-4 rounded-full  justify-center text-center font-semibold overflow-hidden items-center cursor-pointer;
    }

    .cursor-grab {
      cursor: grab;
    }
  }
</style>

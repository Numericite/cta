<template>
  <div class="activity__right relative bg-blue-lightest min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:pl-16 px-6 xl:pr-32 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes premières pistes</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4 sm:text-3xl">1.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal xl:mt-4 sm:text-xl ">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="questionCard card rounded-lg mt-12 w-full xl:mx-6 mx-2 xl:p-8 p-4">
      <div class="flex flex-wrap -mx-1 overflow-hidden lg:-mx-1">
        <div v-for="(domain, key) in localDomains"
             :key="key"
             :class=" key < 24 ? 'border-b-2 border-blue-lightest border-solid' : ''"
             class="w-1/3 sm:w-full my-2 px-2 overflow-hidden flex lg:my-1 lg:px-1 lg:w-1/3 cursor-pointer"
             @click="changeValue(domain)">
          <div class="checkbox w-4 h-6 xl:mr-6 mr-3">
            <input :id="domain.id"
                   v-model="domain.value"
                   name="value"
                   type="checkbox">
            <label :for="domain.id" />
          </div>
          <p class="text-blue xl:text-lg text-base sm:mb-8 sm:ml-4 mt-4 px-5">
            {{ domain.name }}
          </p>
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            :disabled="nbDomains < 3"
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
    domains: {
      type: Array,
      default: function() {
        return { questions: null }
      }
    },
    activityLogs: {
      type: Object,
      default: function() {
        return {
          nbDomains: null
        }
      }
    }
  },
  data() {
    return {
      localDomains: this.domains,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  computed: {
    nbDomains() {
      return _.filter(this.localDomains, {value: true}).length
    }
  },
  created() {
    this.localDomains = _.map(this.localDomains, function(domain) {
      return _.extend({}, domain, {value: false});
    });

    if ( this.activityLogs.selection_ids ) {
      const selection_ids = this.activityLogs.selection_ids
      this.localDomains.forEach( function( domain ) {
        domain.value = ( selection_ids.indexOf( domain.id ) !== -1 )
      } )
    }
  },
  methods: {
    nextPage() {
      this.activityLogs.selection_ids = _.map(_.filter(this.localDomains, {value: true}), 'id')
      this.$emit( 'setActivityLogs', this.activityLogs )
      this.$router.push( '/dashboard/student/domaines/introspection/premieres-pistes/etape-2' )
    },
    changeValue(domain) {
      domain.value = !domain.value
      document.getElementById(domain.id).checked = domain.value
      this.$forceUpdate()
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

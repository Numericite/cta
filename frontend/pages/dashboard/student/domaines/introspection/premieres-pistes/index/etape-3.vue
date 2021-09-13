<template>
  <div class="activity__right min-h-screen bg-white xl:w-3/5 w-full xl:pb-12 pb-16 pt-6 xl:pl-16 xl:pr-32 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes premières pistes</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">3.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <!-- ACTION VERBS -->
    <div class="my-8">
      <div class="flex flex-row">
        <div v-for="(actionVerb, key) in actionVerbsPref" :key="key" class="flex flex-col items-center w-1/3 text-center">
          <div :class="{'bg-yellow': key === 0, 'bg-peach': key === 1, 'bg-topaze': key === 2}" class="rounded-full p-4 flex mx-auto">
            <img :src="actionVerb.img_path" class="w-10 h-10">
          </div>
          <p :class="{'text-yellow': key === 0, 'text-peach': key === 1, 'text-topaze': key === 2}" class="text-6xl font-bold mt-4 mb-0">
            {{ key + 1 }}
          </p>
          <p class="block mx-auto text-center flex text-blue text-xl font-bold mt-0">
            J'aime :<br>{{ actionVerb.name }}
          </p>
        </div>
      </div>
    </div>
    <!-- END ACTION VERBS -->

    <!-- DOMAINS -->
    <div class="mb-8 w-full">
      <div v-for="(domain, key) in domainsPref" :key="key" class="card inline-block items-center rounded-full self-start p-3 px-6 mr-3 mt-3 bg-blue-lightest pr-20 relative">
        <p class="text-3xl font-semibold text-blue mb-0">{{ domain.name }}</p>
        <input id="t"
               class="cursor-default"
               type="checkbox"
               disabled
               checked>
        <label for="t"
               class="cursor-default cursor-custom ml-2 mb-6 ml-3" />
      </div>
    </div>
    <!-- END DOMAINS -->

    <div class="flex sm:flex-col">
      <button v-scroll-to="'#actTitle'"
              class="button button--white mt-8 self-start mt-3 border-blue border-2 border-solid sm:mx-auto"
              @click="restart">
        Je recommence
      </button>
      <button v-scroll-to="'#actTitle'"
              class="button button--blue mt-8 self-start mt-3 ml-6 sm:mx-auto"
              @click="validate">
        Je termine mon activité
      </button>
    </div>

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
    activityLogs: {
      type: Object,
      default: function() {
        return {}
      }
    },
    domains: {
      type: Array,
      default: function() {
        return []
      }
    },
    actionVerbs: {
      type: Array,
      default: function() {
        return []
      }
    }
  },
  data() {
    return {
      domainsPref: [],
      actionVerbsPref: [{},{},{}],
      questionStep: 1,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    if ( !this.activityLogs.choice_ids )
      this.$router.push( '../premieres-pistes' )

    const activityLogs = this.activityLogs

    this.domainsPref = this.domains.filter(function(domain) {
      return _.includes(activityLogs.selection_ids, domain.id)
    })

    this.actionVerbsPref = this.actionVerbs.filter(function(actionVerb) {
      return _.includes(activityLogs.choice_ids, actionVerb.id)
    })
  },
  methods: {
    validate: function() {
      this.$emit('createLogs', () => {
        this.$router.push( {path: '/dashboard/student/domaines/introspection', hash: '#premieres-pistes' })
      })
    },
    restart: function() {
      this.$router.push('/dashboard/student/domaines/introspection/premieres-pistes')
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .cursor-custom {
    position: absolute;
    top: 0.5em;
    transform: translateY(-50%);
    right: 0;

    &:before {
      background-color: #c8d0ff;
    }
  }
}
</style>

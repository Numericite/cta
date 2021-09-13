<template>
  <div class="pr-4">
    <div class="bg-white pt-8 pb-4 relative">
      <span class="text-blue-lighter underline uppercase text-xs font-bold ml-16">Fiche détaillée</span>
      <span class="close z-50 w-12 h-12 bg-orange cursor-pointer absolute pin-t pin-r flex justify-center items-center" @click="$emit('close')">
        <svg version="1.1"
             xmlns="http://www.w3.org/2000/svg"
             xmlns:xlink="http://www.w3.org/1999/xlink"
             width="30"
             height="30"
             viewBox="0 0 20 20">
          <path fill="#ffffff"
                d="M10.707 10.5l5.646-5.646c0.195-0.195 0.195-0.512 0-0.707s-0.512-0.195-0.707 0l-5.646 5.646-5.646-5.646c-0.195-0.195-0.512-0.195-0.707 0s-0.195 0.512 0 0.707l5.646 5.646-5.646 5.646c-0.195 0.195-0.195 0.512 0 0.707 0.098 0.098 0.226 0.146 0.354 0.146s0.256-0.049 0.354-0.146l5.646-5.646 5.646 5.646c0.098 0.098 0.226 0.146 0.354 0.146s0.256-0.049 0.354-0.146c0.195-0.195 0.195-0.512 0-0.707l-5.646-5.646z" />
        </svg>
      </span>
    </div>
    <div class="bg-white mx-6">
      <user-hero :user="eleve" :isManager="true" />
    </div>
    <div class="overflow-hidden w-full">
      <div class="flex flex-no-wrap mt-8 mx-4 relative custom-tabs overflow-auto">
        <span :class="{'hover:text-emerald text-emerald border-solid': currentStep === 1}"
              class="text-blue-lighter whitespace-no-wrap hover:text-emerald hover:border-solid border-emerald py-6 px-3 mr-2 border-b-3 inline-block text-xl cursor-pointer"
              @click="changeStep(1)">
          Autoportrait
        </span>
        <span v-if="eleve.config.currentStep >= 3"
              :class="{'hover:text-emerald text-emerald border-solid': currentStep === 2}"
              class="text-blue-lighter whitespace-no-wrap hover:text-emerald hover:border-solid border-emerald py-6 px-3 mr-2 border-b-3 inline-block text-xl cursor-pointer"
              @click="changeStep(2)">
          Domaines - Plan d'action
        </span>
        <span v-if="eleve.config.currentStep >= 5 && eleve.config.criterion_ids"
              :class="{'hover:text-emerald text-emerald border-solid': currentStep === 3}"
              class="text-blue-lighter whitespace-no-wrap hover:text-emerald hover:border-solid border-emerald py-6 px-3 mr-2 border-b-3 inline-block text-xl cursor-pointer"
              @click="changeStep(3)">
          Filières - Fiche critères
        </span>
        <span v-if="eleve.config.currentStep >= 6"
              :class="{'hover:text-emerald text-emerald border-solid': currentStep === 4}"
              class="text-blue-lighter whitespace-no-wrap hover:text-emerald hover:border-solid border-emerald py-6 px-3 mr-2 border-b-3 inline-block text-xl cursor-pointer"
              @click="changeStep(4)">
          Filières - Plan d'action
        </span>
      </div>
    </div>
    <div class="bg-blue-lightest px-20 py-12 min-h-custom sm:px-4">
      <MainActivity
        v-if="currentStep === 1"
        :activityList="activities"
        :isManager="true"
        :userLogs="userLogs"
        :finalUserLog="getFinalUserLog"
        :selections="eleve.autoPortrait.userSelections"
        :choices="eleve.autoPortrait.userChoices"
        :domains="domains.concat(actionVerbs)"
        :course="eleve.course"
        class="relative z-10 h-full px-32 sm:mt-16 sm:px-0"/>
      <rex v-if="eleve.config.currentStep >= 3" v-show="currentStep === 2" :user_id="eleve.userID" :domains="domains" :isStream="false" :isManager="true"/>
      <div v-if="eleve.config.criterion_ids" v-show="currentStep === 3">
        <span class="text-blue text-5xl font-bold">Les critères de choix de filière de {{ eleve.firstName }}</span>
        <ul class="mt-8">
          <li v-for="criterion_id in eleve.config.criterion_ids" :key="criterion_id" class="mt-4">
            <span>{{ criterion_id }} - {{ getCriterion(criterion_id) }}</span>
          </li>
        </ul>
      </div>
      <rex v-if="eleve.config.currentStep >= 6" v-show="currentStep === 4" :user_id="eleve.userID" :domains="streams" :isStream="true" :isManager="true"/>
    </div>
  </div>
</template>

<script>
  import AutoportraitComponent from '~/components/dashboard/Autoportrait.vue'
  import MainActivity from '~/components/dashboard/MainActivity.vue'
  import UserHero from '~/components/dashboard/UserHero'
  import Rex from './Rex'
  import { CRITERIA } from '~/config'
  import _ from 'lodash'

  export default {
    name: 'ManagerStudentDetails',
    components: { Rex, AutoportraitComponent, UserHero, MainActivity },
    props: {
      eleve: {
        type: Object,
        default: function() {
          return {}
        }
      },
      activities: {
        type: Array,
        default: function () {
          return []
        }
      },
      userLogs: {
        type: Array,
        default: function () {
          return []
        }
      },
      domains: {
        type: Array,
        default: function () {
          return []
        }
      },
      streams: {
        type: Array,
        default: function () {
          return []
        }
      },
      actionVerbs: {
        type: Array,
        default: function () {
          return []
        }
      }
    },
    data() {
      return {
        currentStep: 1,
        criteria: CRITERIA,
      }
    },
    computed: {
      getFinalUserLog() {
        return this.userLogs.slice(-1).pop()
      }
    },
    methods: {
      changeStep(value) {
        if (value !== this.currentStep && value > 0 && value <= 4) {
          this.currentStep = value
          this.$forceUpdate()
        }
      },
      getCriterion(id) {
        return _.get(_.find(this.criteria, {id: id}), 'text', '')
      }
    }
  }
</script>

<style lang="scss">
  .v--modal-overlay {
    background-color: rgba(14, 7, 51, 0.8);
    overflow-x: hidden;
  }

  .v--modal {
    top: 0 !important;
  }

  .min-h-custom {
    min-height: 80vh;
  }
</style>

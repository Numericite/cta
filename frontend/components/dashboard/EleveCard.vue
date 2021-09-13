<template>
  <div class="cardShadow bg-white rounded-xl p-5 px-8 sm:px-6">
    <div class="flex flex-no-wrap justify-between">
      <div class="mr-8 flex flex-no-wrap flex-grow sm:flex-wrap sm:mr-0 sm:w-full">
        <div class="avatar z-10 sm:flex sm:items-center sm:justify-between sm:w-full">
          <div class="flex flex-no-wrap items-center">
            <div class="h-24 sm:h-20 w-24 sm:w-20 rounded-full border-3 border-blue-lighter border-solid overflow-hidden flex justify-center items-start">
              <img v-if="eleve.avatar_path"
                   :src="eleve.avatar_path"
                   class="w-full"
                   alt="Votre image de profil chez Crée ton avenir">
              <picture v-else
                       class="w-full h-auto">
                <source srcset="~/assets/img/profile.webp"
                        type="image/webp">
                <img src="~/assets/img/profile.png"
                     class="w-full h-auto"
                     alt="Image de profil par défaut">
              </picture>
            </div>
            <div class="xl:hidden text-blue font-extrabold text-xl sm:ml-4">
              {{ eleve.firstName }} {{ eleve.lastName }}
            </div>
          </div>
        </div>
        <div class="ml-12 w-full sm:ml-4 sm:w-full">
          <div class="sm:hidden text-blue font-extrabold text-2xl mt-1 sm:mt-0 sm:text-xl">
            {{ eleve.firstName }} {{ eleve.lastName }}
          </div>
          <progress-bar :nbActivitiesUndone="0" :step="userStep" :isManager="true" :isLightVersion="true" class="z-10 relative" />
        </div>
      </div>

      <div class="flex flex-wrap justify-end">
        <div v-if="eleve.classroom" class=" text-blue-lighter font-extrabold text-3xl mt-2 mr-1">
          {{ eleve.classroom.name }}
        </div>
        <div class="flex w-full justify-end">
          <button class="button button--blue py-1 px-5" @click="openAutoPortrait()">
            Fiche détaillée
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

  import ManagerStudentDetails from '~/components/dashboard/ManagerStudentDetails.vue'
  import ProgressBar from '~/components/dashboard/ProgressBar'

export default {
  name: 'EleveCard',
  components: { ProgressBar },
  props: {
    eleve: {
      type: Object,
      default() {
        return {
          autoPortrait: {
            logs: [],
            userSelections: [],
            userChoices: []
          }
        }
      }
    },
    activities: {
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
      userLogs: [],
      userStep: 0,
      modalOptions: {
        height: 'auto',
        width: '90%',
        classes: 'min-h-screen v--modal',
        scrollable: true,
        adaptive: true,
        pivotX: 0.9,
        pivotY: 0.8,
        adaptive: true,
        reset: true
      }
    }
  },
  watch: {
    eleve() {
      this.setSteps()
    }
  },
  created() {
    this.setSteps()
  },
  methods: {
    setSteps() {
      switch (this.eleve.config.currentStep) {
        case 0:
        case 1:
        case 2:
          this.userStep = 0
          break
        case 3:
        case 4:
          this.userStep = 1
          break
        case 5:
          this.userStep = 2
          break
        case 6:
        case 7:
          this.userStep = 4
          break
        case 8:
          this.userStep = 5
          break
        default:
          this.userStep = 0
          break;
      }
    },
    async openAutoPortrait() {
      let response = await this.$api.activities.getActivities()
      const activities = response.data
      response = await this.$api.activities.getUserAdvancedLogs(this.eleve.userID)
      const logs = response.data.logs || []

      logs.filter(function(log) {
        const activity = activities.find(function(activity) {
          return activity.id === log.activity_id
        })
        if (activity) {
          log.num = activity.num
          log.name = activity.name
          return log
        } else if (log.activity_id === 'final') {
          log.num = 8
          log.name = 'Mes premières pistes'
        }
      })

      logs.sort(function(a, b) {
        const valueA = a.num;
        const valueB = b.num;

        let comparison = 0;
        if (valueA > valueB) {
          comparison = 1;
        } else if (valueA < valueB) {
          comparison = -1;
        }
        return comparison;
      })

      const userLogs = [
        _.find(logs, { num: 1 }),
        _.find(logs, { num: 2 }),
        _.find(logs, { num: 3 }),
        _.find(logs, { num: 4 }),
        _.find(logs, { num: 5 }),
        _.find(logs, { num: 6 }),
        _.find(logs, { num: 7 }),
        _.find(logs, { activity_id: 'final' })
      ]
      this.userLogs = userLogs
      this.$modal.show(ManagerStudentDetails, { eleve: this.eleve, domains: this.domains, streams: this.streams, actionVerbs: this.actionVerbs, activities: this.activities, userLogs: this.userLogs }, this.modalOptions)
    }
  }
}
</script>

<style lang="scss" scoped>
.cardShadow {
  box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
}
.pins {
  .svg-pin {
    font-size: 8px;
    transform: translate(7px, 2px);
    @apply rounded-full font-bold h-5 w-5 text-white absolute pin-b pin-r flex justify-center items-center;
  }
}
</style>

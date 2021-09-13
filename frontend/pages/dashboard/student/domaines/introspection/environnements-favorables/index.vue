<template>
  <div class="activity w-screen xl:min-h-screen flex justify-end items-start sm:pb-16">

    <!-- Left part -->
    <div :class="[step > 0 ? 'bg-blue' : 'bg-home']"
         class="activity__left fixed pin-l xl:pin-t pin-b xl:h-screen xl:w-2/5 w-full flex justify-center items-center z-10">

      <!-- Home -->
      <picture v-if="step == 0"
               class="pr-3 sm:hidden w-full">
        <img src="~/assets/img/activity-4.png"
             alt="Activité numéro 1">
      </picture>
      <!-- End Home -->

      <!-- Step 1 -->
      <div v-else
           class="relative w-full xl:h-full h-16 flex items-center justify-center">
        <picture class="absolute pin-t pin-l sm:w-1/3">
          <img src="~/assets/img/environnements-favorables/bg-top-left.svg"
               alt="background intelligences multiples">
        </picture>

        <picture v-if="step != 0"
                 class="h-full flex items-center justify-center sm:h-16 sm:w-1/4 sm:bg-blue sm:rounded-full absolute brain sm:relative overflow-hidden activity_illustration">
          <img v-show="step == 1"
               class="step-1"
               src="~/assets/img/environnements-favorables/cards/etape-1.svg"
               alt="etape 1">
          <img v-show="step == 2"
               class="step-2"
               src="~/assets/img/environnements-favorables/cards/etape-2.svg"
               alt="etape 2">
          <img v-show="step == 3"
               class="step-3"
               src="~/assets/img/environnements-favorables/cards/etape-3.svg"
               alt="etape 3">
          <img v-show="step == 4"
               class="step-4"
               src="~/assets/img/environnements-favorables/cards/etape-4.svg"
               alt="etape 4">
          <img v-show="step == 5"
               class="step-5"
               src="~/assets/img/environnements-favorables/cards/etape-5.svg"
               alt="etape 5">
          <img v-show="step == 6"
               class="step-6"
               src="~/assets/img/environnements-favorables/cards/etape-6.svg"
               alt="etape 6">
          <img v-show="step == 7"
               class="step-7"
               src="~/assets/img/environnements-favorables/cards/etape-7.svg"
               alt="etape 7">
          <img v-show="step == 8"
               class="step-8"
               src="~/assets/img/environnements-favorables/cards/etape-8.svg"
               alt="etape 8">
        </picture>

        <picture class="absolute pin-b pin-r brainBgBottom">
          <img src="~/assets/img/environnements-favorables/bg-bottom-right.svg"
               alt="background intelligences multiples">
        </picture>
      </div>
      <!-- End Step 1 -->

    </div>
    <!-- End Left part -->

    <!-- Close -->
    <nuxt-link to="/dashboard/student/domaines/introspection"
               class="close z-50 w-12 h-12 bg-orange cursor-pointer fixed pin-t pin-r flex justify-center items-center">
      <svg version="1.1"
           xmlns="http://www.w3.org/2000/svg"
           xmlns:xlink="http://www.w3.org/1999/xlink"
           width="30"
           height="30"
           viewBox="0 0 20 20">
        <path fill="#ffffff"
              d="M10.707 10.5l5.646-5.646c0.195-0.195 0.195-0.512 0-0.707s-0.512-0.195-0.707 0l-5.646 5.646-5.646-5.646c-0.195-0.195-0.512-0.195-0.707 0s-0.195 0.512 0 0.707l5.646 5.646-5.646 5.646c-0.195 0.195-0.195 0.512 0 0.707 0.098 0.098 0.226 0.146 0.354 0.146s0.256-0.049 0.354-0.146l5.646-5.646 5.646 5.646c0.098 0.098 0.226 0.146 0.354 0.146s0.256-0.049 0.354-0.146c0.195-0.195 0.195-0.512 0-0.707l-5.646-5.646z" />
      </svg>
    </nuxt-link>
    <!-- End Close -->

    <nuxt-child :actions="actions"
                :activityLogs="activityLogs"
                @createResults="createResults"
                @startActivity="startActivity"
                @stopActivity="stopActivity"
                @setStep="setStep"
                @setActivityLogs="setActivityLogs"
                @createLogs="createLogs" />

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import _ from 'lodash'

export default {
  name: 'EnvironnementsFavorables',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  async asyncData( { app, route, error, store } ) {
    try {
      const chapters = store.state.auth.user.course.chapters
      let domain = _.find(chapters, { slug: 'activity-area' })
      let introspection = _.find(domain.children, { slug: 'introspection' })

      if(!_.some(introspection.activities, {'id': '476bada2-3fae-445c-855d-fd20a81983c3'})) {
        return error({ statusCode: 404, message: 'Post not found' })
      }

      let response
      const version_id = _.get(_.find(introspection.activities, {id: '476bada2-3fae-445c-855d-fd20a81983c3'}), 'version_id', null)

      if (version_id) {
        response = await app.$api.activities.getVersionsByIds([version_id])
      } else {
        response = await app.$api.activities.getVersions({
          parent_ids: ['476bada2-3fae-445c-855d-fd20a81983c3'],
          isDefault: true
        })
      }
      const version = response.data[0] || {}

      response = await app.$api.activities.getSelections({version_ids: [version.id]})
      const passerelles = response.data

      const selections_ids = passerelles.map(selection => selection.id)

      response = await app.$api.activities.getChoices(selections_ids)
      const actions = response.data
      actions.forEach(function(action) {
        const parent = passerelles.find(function(passerelle) {
          return action.selection_id === passerelle.id
        })
        action.icon_path = action.img_path.substring(0, action.img_path.indexOf('.svg')) + '_icon.svg'
        action.value = false
        action.color = parent.color
      })

      response = await app.$api.activities.getUserLogs(app.store.state.auth.user.userID, '476bada2-3fae-445c-855d-fd20a81983c3')
      const activityLogs = response.data[0]

      if (activityLogs) {
        return { actions, activityLogs }
      } else {
        return { actions }
      }
    } catch ( e ) {
      return 'error'
    }
  },
  data() {
    return {
      step: 0,
      results: null,
      activityLogs: {
        status_name: 'open',
        activity_id: '476bada2-3fae-445c-855d-fd20a81983c3',
        config: {}
      },
      endResults: null
    }
  },
  created() {
    // ACTIVITY TURNED OFF
    this.$router.push( '/dashboard/student/domaines/introspection' )
  },
  methods: {
    startActivity: function() {
      this.step = 1
      this.$router.push( 'environnements-favorables/etape-1' )
    },
    stopActivity: function() {
      this.step = 0
    },
    setStep: function( step ) {
      this.step = step
    },
    setActivityLogs: function ( activityLogs ) {
      this.activityLogs = activityLogs
    },
    createLogs: async function (callback) {
      //SENDING LOGS
      this.activityLogs.user_id = this.$store.state.auth.user.userID
      await this.$api.activities.createLog(this.activityLogs)
      callback()
    },
    createResults: function (results) {
      //SENDING RESULTS
      //this.$api.activities.createResults(this.$store.state.auth.user.userID, results)
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .activity__left {
    @screen xl {
      width: 30vw;
    }

    @screen xxl {
      width: 35vw;
    }

    .activity_illustration {
      max-width: 90%;

      @screen sm {
        height: 6em;
      }
    }

    .brain {
      @screen sm {
        width: 7rem;
        height: 7rem;
        transform: translateY(-1rem);
      }

      @sreen xl {
        .step-3 {
          transform: translateY(-44px);
        }

        .step-4 {
          transform: translateY(-71px);
        }

        .step-5 {
          transform: translateY(-75.5px);
        }

        .step-6 {
          transform: translateY(-52.5px);
        }

        .step-7 {
          transform: translateY(-16px);
        }

        .step-8 {
          transform: translate(-7px, -60px);
        }
      }
    }
    .brainColor {
      @screen xl {
        transform: translate(40px, -80px);
      }
      @screen sm {
        width: 47%;
        height: auto;
        transform: translate(5px, -13px);
      }
    }
    .brainBgBottom {
      @screen sm {
        transform: translateX(20%);
        height: 60%;
      }
    }
  }

  .activity__right {
    @screen xl {
      width: 70vw;
    }

    @screen xxl {
      width: 65vw;
    }
  }

  .bg-home {
    background: #ffece4;
  }
}
</style>

<template>
  <div class="activity w-screen xl:min-h-screen flex justify-end items-start sm:pb-16">

    <!-- Left part -->
    <div class="activity__left fixed pin-l xl:pin-t pin-b xl:h-screen xl:w-2/5 w-full flex justify-center items-center z-10 bg-blue">

      <!-- Home -->
      <picture class="pr-3 sm:hidden w-full">
        <img src="~/assets/img/activity-final.png"
             alt="Activité numéro 1">
      </picture>
      <!-- End Home -->

    </div>
    <!-- End Left part -->

    <!-- Close -->
    <nuxt-link to="/dashboard/student/domaines/introspection"
               class="close z-40 w-12 h-12 bg-orange cursor-pointer fixed pin-t pin-r flex justify-center items-center">
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

    <nuxt-child :domains="domains"
                :actionVerbs="actionVerbs"
                :activityLogs="activityLogs"
                @startActivity="startActivity"
                @stopActivity="stopActivity"
                @setActivityLogs="setActivityLogs"
                @createLogs="createLogs" />

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
  async asyncData( { app, store } ) {
    try {
      const chapters = store.state.auth.user.course.chapters
      let domainsChapter = _.find(chapters, { slug: 'activity-area' })

      let response = await app.$api.domains.getList({page: 1, numberPerPage: 1000})
      const data = response.data || []

      const domains = _.filter(data, (domain) => {
        return domain.kind === 'activity-area' && _.includes(domainsChapter.domain_ids, domain.id)
      })
      const actionVerbs = _.filter(data, {kind: 'action-verb'})

      response = await app.$api.activities.getUserUndoneActivities(app.store.state.auth.user.userID, {course_id: app.store.state.auth.user.course.id})
      const userUndoneActivities = response.data || []
      const isAuthorized = userUndoneActivities.length === 0

      response = await app.$api.activities.getUserLogs(app.store.state.auth.user.userID, 'final')
      const activityLogs = response.data[0]

      if (activityLogs) {
        return { domains, actionVerbs, isAuthorized, activityLogs }
      } else {
        const firstTime = true
        return { domains, actionVerbs, isAuthorized, firstTime }
      }
    } catch ( e ) {
      console.log(e)
      return 'error'
    }
  },
  data() {
    return {
      domains: [],
      actionVerbs: [],
      activityLogs: {
        status_name: 'open',
        activity_id: 'final',
        config: {}
      },
      firstTime: false,
      endResults: null
    }
  },
  created() {
    if (!this.isAuthorized) {
      this.$router.push('/')
    }
  },
  methods: {
    startActivity: function() {
      this.step = 1
      this.$router.push( 'premieres-pistes/etape-1' )
    },
    stopActivity: function() {
      this.step = 0
    },
    setActivityLogs: function ( activityLogs ) {
      this.activityLogs = activityLogs
    },
    createLogs: async function (callback) {
      const user_id = this.$store.state.auth.user.userID

      //SENDING LOGS
      this.activityLogs.user_id = user_id
      await this.$api.activities.createLog(this.activityLogs)
      callback()

      // CREATE EXPERIENCES
      if (this.firstTime) {

        const user = this.$store.state.auth.user
        if (this.stepCalculator(user.config.currentStep) < 3) {
          this.$store.commit('auth/setCurrentStep', 3)
          await this.$api.user.updateUser(this.$store.state.auth.user)
        }

        const experienceApi = this.$api.experiences
        _.forEach(this.activityLogs.selection_ids, function(domain_id) {
          const experience = {
            domain_id: domain_id,
            user_id: user_id,
            kind: 'domain',
          };

          experienceApi.create(experience)
          experienceApi.create(experience)
        })
      }
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

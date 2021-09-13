<template>
  <div class="activity w-screen xl:h-screen flex justify-end items-start sm:pb-16">

    <!-- Left part -->
    <div :class="[step > 0 ? 'bg-blue' : 'bg-home']"
         class="activity__left fixed pin-l xl:pin-t pin-b xl:h-screen xl:w-2/5 w-full flex justify-center items-center z-10">

      <!-- Home -->
      <picture v-if="step == 0"
               class="sm:hidden w-full">
        <img src="~/assets/img/activity-6.png"
             alt="Activité numéro 6">
      </picture>
      <!-- End Home -->

      <!-- Step 1 -->
      <div v-else
           class="relative w-full xl:h-full h-16 flex items-center justify-center">
        <picture class="absolute pin-t pin-l sm:w-1/3">
          <img src="~/assets/img/competences/bg-top-left.svg"
               alt="background rencontres">
        </picture>

        <picture v-if="step != 0"
                 class="h-full flex items-center justify-center sm:h-16 sm:w-1/4 sm:bg-blue sm:rounded-full brain sm:relative overflow-hidden">
          <svg xmlns="http://www.w3.org/2000/svg"
               width="383"
               height="615"
               viewBox="0 0 383 615">
            <g fill="none"
               fill-rule="evenodd">
              <g fill-rule="nonzero">
                <g transform="translate(84 111)">
                  <rect width="208"
                        height="62"
                        fill="#281586"
                        rx="31" />
                  <rect :stroke="step === 5 ? '#FFC600' : step < 2 ? '#432cb4' : '#A2ACE3'"
                        width="190.2"
                        height="48.2"
                        x="8.9"
                        y="7.9"
                        stroke-width="1.8"
                        rx="24.1" />
                  <circle :stroke="step === 5 ? '#FFC600' : step < 2 ? '#432cb4' : '#A2ACE3'"
                          :cx="step < 2 ? '35' :'172.5'"
                          cy="31.5"
                          r="17.5"
                          stroke-width="1.44" />

                </g>
                <g fill="#281586"
                   transform="translate(84 200)">
                  <rect width="208"
                        height="62"
                        rx="31" />
                  <rect :stroke="step === 5 ? '#1DD4B6' : step < 3 ? '#432cb4' : '#A2ACE3'"
                        width="190.2"
                        height="48.2"
                        x="8.9"
                        y="7.9"
                        stroke-width="1.8"
                        rx="24.1" />
                  <circle :stroke="step === 5 ? '#1DD4B6' : step < 3 ? '#432cb4' : '#A2ACE3'"
                          :cx="step < 3 ? '35' :'172.5'"
                          cy="31.5"
                          r="17.5"
                          stroke-width="1.44" />

                </g>
                <g fill="#281586"
                   transform="translate(84 290)">
                  <rect width="208"
                        height="62"
                        rx="31" />
                  <rect :stroke="step === 5 ? '#FF9C9C' : step < 4 ? '#432cb4' : '#A2ACE3'"
                        width="190.2"
                        height="48.2"
                        x="8.9"
                        y="7.9"
                        stroke-width="1.8"
                        rx="24.1" />
                  <circle :stroke="step === 5 ? '#FF9C9C' : step < 4 ? '#432cb4' : '#A2ACE3'"
                          :cx="step < 4 ? '35' :'172.5'"
                          cy="31.5"
                          r="17.5"
                          stroke-width="1.44" />
                </g>
              </g>
              <path fill="#281586"
                    d="M216.362 425.928c-5.747 0-12.38 2.446-12.38 7.968v3.912c0-10.815-9.194-19.789-20.687-19.789-5.746 0-10.343 4.372-10.343 9.895v3.911l-.23-4.142-5.55-43.746c-.46-6.902-3.874-19.07-10.54-19.07-6.665 0-8.908 12.398-8.908 19.07l-3.271 41.906v60.975l-10.805-16.567c-7.356-11.274-12.18-25.31-24.135-19.328-2.986 1.611-3.448 4.832-2.068 7.824l12.641 38.656c2.299 6.213 15.4 23.93 20.227 28.304 0 0 16.32 24.848 16.32 34.282l4.82 40.769s23.64-10.306 40.037-9.163c16.398 1.146 33.177 9.163 33.177 9.163l2.669-37.41c0-9.433 9.913-30.92 9.913-30.92 3.448-5.752 12.203-18.9 12.203-25.575 0 0 .763-23.856 0-38.937-.763-15.076-12.203-34.354-24.44-24.126 0-10.814-7.158-17.862-18.65-17.862z" />
              <path fill="#140963"
                    fill-rule="nonzero"
                    d="M161.703 600.854c21.88-18.649 46.223-18.649 73.027 0-19.34 18.648-43.682 18.648-73.027 0zM172.494 432.367c-1.055 21.645-1.055 32.467 0 32.467 1.582 0 1.246-27.467 1.246-32.467 0-3.333-.416-3.333-1.246 0zM203.494 437.438c-1.055 13.597-1.055 20.396 0 20.396 1.582 0 1.246-17.255 1.246-20.396 0-2.094-.416-2.094-1.246 0zM234.494 446.438c-1.055 13.597-1.055 20.396 0 20.396 1.582 0 1.246-17.255 1.246-20.396 0-2.094-.416-2.094-1.246 0z" />
              <g v-if="step == 5"
                 stroke-width="2">
                <path stroke="#A2ACE3"
                      stroke-linecap="round"
                      d="M163 59a8 8 0 1 1-16 0 8 8 0 0 1 16 0z" />
                <path stroke="#FFC600"
                      stroke-linecap="round"
                      d="M71 388.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 1 1 11 0z" />
                <g stroke="#A2ACE3"
                   stroke-linecap="round">
                  <path d="M273.995 386.195h13M279.407 379.195l2.136 14" />
                </g>
                <g stroke="#1DD4B6"
                   stroke-linecap="round">
                  <path d="M54.851 123.172l9.994.354M48.073 104.776l18.544 8.951M66.55 97.97l6.285 9.028" />
                </g>
                <path stroke="#FF9C9C"
                      d="M14.963 248.176L2 242.889 7.664 229l12.964 5.287z" />
                <g stroke="#A2ACE3"
                   stroke-linecap="round">
                  <path d="M314.858 115.685l-4.14 9.101M334.449 116.44l-15.351 13.724M333.687 136.116l-10.742 2.364" />
                </g>
                <g stroke-linecap="round">
                  <path stroke="#1DD4B6"
                        d="M209.5 1.284v39" />
                  <path stroke="#A2ACE3"
                        d="M219.5 30.284v22" />
                </g>
                <g stroke-linecap="round">
                  <path stroke="#A2ACE3"
                        d="M344.995 262.479l23-12" />
                  <path stroke="#FFC600"
                        d="M363.995 262.479l18-10" />
                </g>
              </g>
            </g>
          </svg>

        </picture>

        <picture class="absolute pin-b pin-r brainBgBottom">
          <img src="~/assets/img/competences/bg-bottom-right.svg"
               alt="background mes premieres rencontres">
        </picture>
      </div>
      <!-- End Step 1 -->

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

    <nuxt-child :affirmations="affirmations"
                :results="results"
                :activityLogs="activityLogs"
                @createResults="createResults"
                @startActivity="startActivity"
                @stopActivity="stopActivity"
                @setStep="setStep"
                @sendResults="getResults"
                @setEndResults="getEndResults"
                @setActivityLogs="setActivityLogs"
                @createLogs="createLogs" />

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import _ from 'lodash'

export default {
  name: 'MesRencontres',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  async asyncData( { app, store, error } ) {
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
      const version = response.data[0] || {}

      response = await app.$api.activities.getSelections({version_ids: [version.id]})
      const affirmations = response.data

      affirmations.forEach(function(affirmation) {
        affirmation.like = false
      })

      response = await app.$api.activities.getUserLogs(app.store.state.auth.user.userID, '476bada2-3fae-445c-855d-fd20a81983c3')
      const activityLogs = response.data[0]

      if (activityLogs) {
        return { affirmations, activityLogs }
      } else {
        return { affirmations }
      }
    } catch ( e ) {
      console.log(e)
    }
  },
  data() {
    return {
      step: 0,
      results: null,
      activityLogs: {
        status_name: 'open',
        activity_id: '476bada2-3fae-445c-855d-fd20a81983c3'
      },
      endResults: null
    }
  },
  methods: {
    startActivity: function() {
      this.step = 1
      this.$router.push( 'rencontres/etape-1' )
    },
    stopActivity: function() {
      this.step = 0
    },
    setStep: function( step ) {
      this.step = step
    },
    getResults: function( results ) {
      this.results = results
    },
    getEndResults: function( results ) {
      this.endResults = results
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
    createResults: function () {
      //SENDING RESULTS
      //this.$api.activities.createResults(this.$store.state.auth.user.userID, this.endResults)
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

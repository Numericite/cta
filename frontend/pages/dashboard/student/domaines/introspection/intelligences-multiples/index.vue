<template>
  <div class="activity w-screen xl:min-h-screen flex justify-end items-start sm:pb-16">

    <!-- Left part -->
    <div :class="[step > 0 ? 'bg-blue' : 'bg-home']"
         class="activity__left fixed pin-l xl:pin-t pin-b xl:h-screen xl:w-2/5 w-full flex justify-center items-center z-10">

      <!-- Home -->
      <picture v-if="step == 0"
               class="pr-3 sm:hidden w-full">
        <img src="~/assets/img/activity-5.png"
             alt="Activité numéro 1">
      </picture>
      <!-- End Home -->

      <!-- Step 1 -->
      <div v-else
           class="relative w-full xl:h-full h-16 flex items-center justify-center">
        <picture class="absolute pin-t pin-l sm:w-1/3">
          <img src="~/assets/img/intelligences-multiples/bg-top-left.svg"
               alt="background intelligences multiples">
        </picture>

        <picture v-if="step != 0"
                 class="h-full flex items-center justify-center sm:h-16 sm:w-1/4 sm:bg-blue sm:rounded-full brain sm:relative">
          <img class="z-10 sm:h-16"
               src="~/assets/img/intelligences-multiples/brain-1.svg"
               alt="Illustration cerveau intelligences multiples étape 1">
        </picture>

        <picture class="absolute pin-b pin-r brainBgBottom">
          <img src="~/assets/img/intelligences-multiples/bg-bottom-right.svg"
               alt="background intelligences multiples">
        </picture>


        <svg class="absolute z-20 brainColor activity_illustration pin-l"
             height="382px"
             viewBox="0 0 363 382"
             version="1.1"
             xmlns="http://www.w3.org/2000/svg"
             xmlns:xlink="http://www.w3.org/1999/xlink">
          <g stroke="none"
             stroke-width="1"
             fill="none"
             fill-rule="evenodd">
            <g>
              <g>
                <path v-if="step > 1"
                      d="M110.995,76.479 C110.995,80.8975541 107.413054,84.4795 102.9945,84.4795 C98.5759459,84.4795 94.994,80.8975541 94.994,76.479 C94.994,72.060722 98.575722,68.479 102.994,68.479 C107.412278,68.479 110.994,72.060722 110.994,76.479 L110.995,76.479 Z"
                      stroke="#A2ACE3"
                      stroke-linecap="round" />
                <path v-if="step > 1"
                      d="M290.995,182.979 C290.995,186.016566 288.532566,188.479 285.495,188.479 C282.457434,188.479 279.995,186.016566 279.995,182.979 C279.995,179.941434 282.457434,177.479 285.495,177.479 C288.532566,177.479 290.995,179.941434 290.995,182.979 Z"
                      stroke="#FFC600"
                      stroke-linecap="round" />
                <g v-if="step > 1"
                   stroke-linecap="round"
                   transform="translate(326.000000, 246.000000)"
                   stroke="#A2ACE3">
                  <path d="M0.995,7.479 L13.995,7.479 M6.407,0.479 L8.543,14.479" />
                </g>
                <g v-if="step > 1"
                   stroke-linecap="round"
                   transform="translate(0.000000, 117.000000)"
                   stroke="#1DD4B6">
                  <path d="M6.851,26.172 L16.845,26.526 M0.073,7.776 L18.617,16.727 M18.55,0.97 L24.835,9.998" />
                </g>
                <polygon v-if="step > 1"
                         stroke="#FF9C9C"
                         points="272.644 322.567 259.681 317.28 265.345 303.39 278.309 308.678" />
                <g v-if="step > 1"
                   stroke-linecap="round"
                   transform="translate(253.000000, 62.000000)"
                   stroke="#A2ACE3">
                  <path d="M0.47,9.602 L1.262,19.57 M17.972,0.766 L11.199,20.211 M26.845,18.344 L18.595,25.619" />
                </g>
                <g v-if="step > 1"
                   transform="translate(173.000000, 0.000000)">
                  <path d="M0.994,16.479 L0.994,55.479"
                        stroke="#1DD4B6" />
                  <path d="M7.994,0.479 L7.994,22.479"
                        stroke="#A2ACE3" />
                </g>
                <g v-if="step > 1"
                   transform="translate(325.000000, 160.000000)">
                  <path d="M0.995,12.479 L23.995,0.479"
                        stroke="#A2ACE3" />
                  <path d="M19.995,12.479 L37.995,2.479"
                        stroke="#FFC600" />
                </g>
                <path v-if="questionStep > 1"
                      :stroke="step == 1 ? '#a2ace3' : '#FFC600'"
                      d="M96.495,125.979 C86.995,115.479 82.495,132.979 71.995,130.479 C71.995,130.479 48.995,158.479 56.995,172.479 C56.995,172.479 61.995,177.479 64.995,168.479 C64.995,168.479 68.995,151.479 77.995,153.479"
                      stroke-linecap="round" />
                <path v-if="questionStep > 2"
                      :stroke="step == 1 ? '#a2ace3' : '#FFC600'"
                      d="M122.995,137.479 C122.995,137.479 111.995,148.479 97.995,143.479 C97.995,143.479 86.995,135.479 97.995,126.479 C97.995,126.479 100.995,124.479 104.995,126.479 C104.995,126.479 106.995,127.479 109.995,121.479 C112.995,115.479 118.995,111.479 121.995,111.479 C124.995,111.479 130.995,113.479 134.995,110.479 C134.995,110.479 138.995,107.479 142.995,109.479 C146.995,111.479 155.149,115.59 157.995,111.479 C157.995,111.479 161.995,105.479 157.995,96.479 C157.995,96.479 157.995,93.479 148.995,95.479 C148.995,95.479 134.995,94.479 131.995,96.479 C128.995,98.479 111.995,102.479 105.995,104.479 C99.995,106.479 97.995,109.479 93.995,111.479 C89.995,113.479 81.995,114.479 78.995,118.479 C75.995,122.479 67.995,125.479 71.995,130.479 M73.995,182.479 C73.995,182.479 73.995,177.479 80.995,177.479 C87.995,177.479 89.995,167.479 98.995,166.479 C98.995,166.479 120.995,165.479 121.995,165.479 C122.995,165.479 127.995,163.479 127.995,157.479 C127.995,157.479 127.995,147.479 134.995,145.479 C134.995,145.479 146.995,144.479 146.995,138.479"
                      stroke-linecap="round" />
                <path v-if="questionStep > 3"
                      :stroke="step == 1 ? '#a2ace3' : '#FFC600'"
                      d="M144.995,128.479 C144.995,128.479 153.995,135.479 167.995,123.479 C167.995,123.479 174.228,118.547 174.995,115.479 C175.995,111.479 177.995,109.479 183.995,112.479 C183.995,112.479 187.498,116.327 192.245,109.403 C196.995,102.479 200.995,107.479 201.995,105.479 C202.995,103.479 193.995,100.479 180.995,101.479 C170.995,101.479 157.995,95.479 157.995,95.479 M58.31,173.993 C58.31,173.993 53.995,192.479 70.995,199.479 C87.995,206.479 88.995,209.479 91.995,208.479 C93.238,208.065 100.995,201.479 104.995,200.479 C108.995,199.479 119.311,193.723 123.995,195.479 C131.995,198.479 141.995,200.479 152.995,191.479 C152.995,191.479 163.995,184.479 176.995,191.479 C196.995,204.479 198.995,175.479 199.995,167.479 C200.995,159.479 203.995,162.479 203.995,162.479 M166.995,188.479 C166.995,188.479 158.995,177.479 162.995,171.479 M116.995,182.479 C116.995,182.479 113.995,185.479 119.995,194.479 M97.995,180.479 C97.995,180.479 97.995,190.479 115.995,195.479"
                      stroke-linecap="round" />
                <path v-if="questionStep > 4"
                      :stroke="step == 1 ? '#a2ace3' : '#FFC600'"
                      d="M139.995,190.479 C139.995,190.479 143.995,190.479 141.995,177.479 C139.995,164.479 150.995,153.479 154.995,152.479 C158.995,151.479 173.995,154.479 170.995,145.479 C170.995,145.479 166.995,135.479 178.995,132.479 C178.995,132.479 185.995,124.479 193.995,128.479 C201.995,132.479 207.995,133.479 207.995,128.479 C207.995,128.479 199.995,120.479 204.995,116.479 C209.995,112.479 212.995,116.479 215.995,114.479 C218.995,112.479 207.995,103.479 201.995,105.479"
                      stroke-linecap="round" />
                <path v-if="questionStep > 5"
                      :stroke="step == 1 ? '#a2ace3' : '#FFC600'"
                      d="M184.994,176.479 C184.994,176.479 179.994,178.479 178.994,173.479 C177.994,168.479 179.994,157.479 182.994,155.479 C182.994,155.479 190.994,150.479 191.994,147.479 C192.994,144.479 197.994,142.479 202.994,143.479 C207.994,143.479 221.994,146.479 222.994,142.479 C222.994,142.479 225.994,135.479 231.994,140.479 C231.994,140.479 239.994,146.479 239.994,153.479 C239.994,160.479 236.994,166.479 238.994,167.479 C240.994,168.479 250.994,167.479 254.994,175.479 C254.994,175.479 258.994,181.479 259.994,194.479 C259.994,194.479 260.994,198.479 264.994,196.479 C264.994,196.479 273.994,195.479 266.994,176.479 C251.994,135.479 232.994,115.479 215.97,112.555"
                      stroke-linecap="round" />
                <path v-if="questionStep > 6"
                      :stroke="step == 1 ? '#a2ace3' : '#1DD4B6'"
                      d="M91.995,208.479 C91.995,208.479 89.995,218.479 93.995,221.479 C97.995,224.479 94.995,227.479 95.995,229.479 C96.995,231.479 101.995,240.479 103.995,237.479 C105.995,234.479 104.995,229.479 105.995,227.479 C105.995,227.479 108.995,225.479 108.995,218.479 C108.995,211.479 113.995,209.479 117.995,210.479 M120.995,235.479 C120.995,235.479 125.995,234.479 128.995,235.479 C131.995,236.479 129.995,227.479 133.995,225.479 M148.995,226.479 C148.995,226.479 145.995,212.479 160.995,210.479 C175.995,208.479 181.995,215.479 187.995,213.479 C187.995,213.479 205.995,204.479 210.995,195.479 C215.995,186.479 214.995,174.479 214.995,174.479 C214.995,174.479 213.995,163.479 223.995,163.479 M193.994,211.479 C193.994,211.479 192.994,221.479 196.994,222.479 C200.994,223.479 211.994,220.479 213.994,222.479 C215.994,224.479 218.994,233.479 217.994,237.479 C216.994,241.479 211.994,246.479 211.994,246.479"
                      stroke-linecap="round" />
                <path v-if="questionStep > 7"
                      :stroke="step == 1 ? '#a2ace3' : '#FF9C9C'"
                      d="M103.995,238.479 C103.995,238.479 111.995,251.479 122.995,251.479 C122.995,251.479 134.995,254.479 140.995,253.479 C146.995,252.479 157.995,246.479 159.995,247.479 C159.995,247.479 170.995,250.479 181.995,248.479 C181.995,248.479 202.995,242.479 210.995,248.479 C218.995,254.479 228.995,254.479 234.995,252.479 C240.995,250.479 239.995,255.479 239.995,255.479 C239.995,255.479 235.995,280.479 202.995,276.479 C202.995,276.479 193.995,278.479 182.995,275.479 C177.995,274.479 172.995,268.479 169.995,265.479 C166.995,262.479 163.995,254.479 164.995,249.479" />
                <path v-if="questionStep > 8"
                      :stroke="step == 1 ? '#a2ace3' : '#FF9C9C'"
                      d="M144.995,253.479 C144.995,253.479 145.995,261.479 154.995,270.479 C163.995,279.479 170.995,301.479 170.995,301.479 C170.995,301.479 172.995,307.479 180.995,306.479 C188.995,305.479 189.995,304.479 189.995,302.479 C189.995,300.479 185.995,276.479 185.995,276.479" />
                <path v-if="questionStep == 9"
                      :stroke="step == 1 ? '#a2ace3' : '#1DD4B6'"
                      d="M196.994,223.479 C196.994,223.479 193.994,232.479 178.994,233.479 M210.994,196.479 C210.994,196.479 212.994,210.479 218.994,211.479 C224.994,212.479 236.994,208.479 238.994,212.479 C240.994,216.479 238.994,222.479 234.994,228.479 M238.994,196.479 C239.994,196.479 243.994,193.479 248.994,204.479 C248.994,204.479 249.994,222.479 261.994,218.479 C261.994,218.479 266.994,214.479 269.994,215.479 C272.994,216.479 276.787,198.534 268.891,195.006 M240.357,257.218 C240.357,257.218 252.995,257.479 257.995,247.479 C262.995,237.479 271.101,222.672 271.048259,216.075"
                      stroke-linecap="round" />
              </g>
            </g>
          </g>
        </svg>
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

    <nuxt-child :questions="questions"
                :intelligences="intelligences"
                :actions="actions"
                :results="results"
                :endResults="endResults"
                :activityLogs="activityLogs"
                @startActivity="startActivity"
                @stopActivity="stopActivity"
                @setQuestionStep="setQuestionStep"
                @setFirstStep="setStep(1)"
                @setSecondStep="setStep(2)"
                @setThirdStep="setStep(3)"
                @sendResults="getResults"
                @setEndResults="getEndResults"
                @setActivityLogs="setActivityLogs"
                @createResults="createResults"
                @createLogs="createLogs" />

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import _ from 'lodash'
export default {
  name: 'IntelligencesMultiples',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  async asyncData( { app, route, error, store } ) {
    try {
      const chapters = store.state.auth.user.course.chapters
      let domain = _.find(chapters, { slug: 'activity-area' })
      let introspection = _.find(domain.children, { slug: 'introspection' })

      if(!_.some(introspection.activities, {'id': '38768549-ad30-44a5-a479-7f9fa3c22b60'})) {
        return error({ statusCode: 404, message: 'Post not found' })
      }

      let response
      const version_id = _.get(_.find(introspection.activities, {id: '38768549-ad30-44a5-a479-7f9fa3c22b60'}), 'version_id', null)

      if (version_id) {
        response = await app.$api.activities.getVersionsByIds([version_id])
      } else {
        response = await app.$api.activities.getVersions({
          parent_ids: ['38768549-ad30-44a5-a479-7f9fa3c22b60'],
          isDefault: true
        })
      }
      const version = response.data[0] || {}

      response = await app.$api.activities.getSelections({version_ids: [version.id]})
      const intelligences = response.data

      const selections_ids = intelligences.map(selection => selection.id)

      response = await app.$api.activities.getSelectors(selections_ids)
      const questions = _.orderBy(response.data, ['num'], ['asc'])

      response = await app.$api.activities.getChoices(selections_ids)
      const actions = response.data
      actions.forEach(function(action) {
        action.value = false
      })

      response = await app.$api.activities.getUserLogs(app.store.state.auth.user.userID, '38768549-ad30-44a5-a479-7f9fa3c22b60')
      const activityLogs = response.data[0]

      if (activityLogs) {
        return { questions, intelligences, actions, activityLogs }
      } else {
        return { questions, intelligences, actions }
      }
    } catch ( e ) {
      return 'error'
    }
  },
  data() {
    return {
      step: 0,
      questions: null,
      activityLogs: {
        status_name: 'open',
        activity_id: '38768549-ad30-44a5-a479-7f9fa3c22b60',
        config: {}
      },
      questionStep: 1,
      results: null,
      endResults: null
    }
  },
  mounted() {
    this.questionStep = 1
  },
  methods: {
    startActivity: function () {
      this.step = 1
      this.$router.push( 'intelligences-multiples/etape-1' )
    },
    stopActivity: function () {
      this.step = 0
    },
    setStep: function ( step ) {
      this.step = step
    },
    setQuestionStep: function ( step ) {
      this.questionStep = step
    },
    getResults: function ( results ) {
      this.results = results
    },
    getEndResults: function ( results ) {
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
      top: calc(50% - 80px);
      -webkit-transform: translate(-50%,-50%);
      transform: translate(-50%,-50%);
      left: calc(50% + 40px);
      @screen xl {
      }
      @screen sm {
        display: none;
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

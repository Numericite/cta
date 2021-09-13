<template>
  <div class="flexContainer-dash ml-32 sm:ml-0 relative">
    <picture>
      <source srcset="~/assets/img/dash-bg-3.webp"
              type="image/webp">
      <img src="~/assets/img/dash-bg-3.png"
           class="absolute pin-r custom-background-picture sm:hidden"
           alt="introspection background image">
    </picture>

    <h2 class="title title--dash pt-16 sm:text-4xl"><span v-html="texts[0]" /></h2>

    <!-- Vidéo -->
    <div class="mt-12 relative">
      <videoComp v-if="stepCalculator($store.state.auth.user.config.currentStep) < 2 || showVideo"
                 vimeoId="357032608"
                 class="sm:w-full"
                 @clicked="incrementCurrentStep()"/>
      <div v-if="stepCalculator($store.state.auth.user.config.currentStep) >= 2 && !showVideo">
        <button class="button button--blue" @click="showVideo = true">Revoir la vidéo de connaissance de soi</button>
      </div>
    </div>
    <!-- End Vidéo -->

    <div class="pt-12 z-10">
      <h3 class="title font-bold custom-title-autoportrait text-blue"><span v-html="texts[1]" /></h3>
      <div class="desc text-blue text-3xl sm:text-xl font-normal mt-4 leading-normal text-justify">
        <span v-html="texts[2]" />
      </div>
    </div>

    <!--ACTIVITIES COMPONENT-->
    <MainActivity
      :activityList="activities"
      :userLogs="userLogs"
      :finalUserLog="getFinalUserLog"
      :selections="userSelections"
      :choices="userChoices"
      :domains="domains"
      class="relative h-full mt-12 sm:mt-16"/>
  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import VideoComp from '~/components/Video'
import ActivityCard from '~/components/dashboard/introspection/ActivityCard'
import AutoportraitComponent from '~/components/dashboard/Autoportrait.vue'
import MainActivity from '~/components/dashboard/MainActivity.vue'
import _ from 'lodash'

export default {
  name: 'Introspection',
  layout: 'dashboard_student',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  components: { VideoComp, ActivityCard, AutoportraitComponent, MainActivity},
  async asyncData( { app, route, error, store } ) {
    try {
      const chapters = store.state.auth.user.course.chapters
      let domain = _.find(chapters, { slug: 'activity-area' })

      if (!(_.get(_.find(domain.children, { slug: 'introspection' }), 'display', false))) {
        return error({ statusCode: 404, message: 'Post not found' })
      }
        let response = await app.$api.activities.getActivities()
        const activities = response.data

        response = await app.$api.activities.getUserAdvancedLogs(app.store.state.auth.user.userID)
        const logs = response.data.logs || []
        const userSelections = response.data.selections || []
        const userChoices = response.data.choices || []

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

        response = await app.$api.domains.getList({ page: 1, numberPerPage: 1000 })
        const domains = response.data || []
        return { activities, userLogs, userSelections, userChoices, domains }
    } catch ( err ) {
      console.log( err )
    }
  },
  data() {
    return {
      userLogs: null,
      activities: null,
      showVideo: false,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  computed: {
    getFinalUserLog() {
      return this.userLogs.slice(-1).pop()
    }
  },
  methods: {
    async incrementCurrentStep() {
      const user = this.$store.state.auth.user
      if (this.stepCalculator(user.config.currentStep) < 2) {
        this.$store.commit('auth/setCurrentStep', 2)
        await this.$api.user.updateUser(this.$store.state.auth.user)
        this.showVideo = true
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.videoSize,
video {
  width: 640px;
}
.video {
  @apply rounded-lg;
}

  .custom-title-autoportrait {
    font-size: 30px;
  }

  .custom-background-picture {
    top: 35%;
  }
</style>

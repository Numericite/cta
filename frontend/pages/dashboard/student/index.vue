<template>
  <div class="dashboard flexContainer-dash bg-blue-lightest ml-32 sm:ml-0 relative">

    <picture>
      <source srcset="~/assets/img/dash-bg-1.webp"
              type="image/webp">
      <img src="~/assets/img/dash-bg-1.png"
           class="absolute pin-t pin-r z-0 custom-background-dashboard sm:hidden"
           alt="Illustration d'arrière plan">
    </picture>

    <progress-bar :nbActivitiesUndone="activityStep" :step="userStep" :title="texts[0]" class="z-10 relative" />

    <to-do v-if="userUndoneActivities.length > 0" :activities="userUndoneActivities.slice(0,3)" :title="texts[1]" class="z-10 relative mt-24 sm:mt-16" />

    <domains v-if="finalLog && finalDomains.length === 0" :domains="finalLog.domains" :verbs="finalLog.verbs" :title="texts[2]" class="mt-24" />
    <domains v-if="finalDomains.length > 0" :domains="finalDomains" :title="texts[3]" class="mt-24" />

    <domains v-if="introspectionStreams.length > 0 && finalStreams.length === 0" :domains="introspectionStreams" :title="texts[4]" class="mt-16"/>
    <domains v-if="finalStreams.length > 0" :domains="finalStreams" :title="texts[5]" class="mt-16"/>

    <picture>
      <source srcset="~/assets/img/dash-bg-2.webp"
              type="image/webp">
      <img src="~/assets/img/dash-bg-2.png"
           class="absolute pin-l z-0 sm:hidden"
           alt="Illustration d'arrière plan">
    </picture>

    <scores :nbActivityDone="userHistory.length" :nbDomains="finalDomains.length" :nbStreams="finalStreams.length" :title="texts[6]" class="mt-16 z-10 relative" />

    <history v-if="userHistory.length > 0" :userHistory="userHistory" :title="texts[6]" class="mt-16 z-10 relative" />

    <picture>
      <source srcset="~/assets/img/dash-bg-3.webp"
              type="image/webp">
      <img src="~/assets/img/dash-bg-3.png"
           class="absolute pin-r z-0 sm:hidden"
           alt="Illustration d'arrière plan">
    </picture>

    <news v-if="posts.length > 0" :posts="posts"
          class="mt-16 z-10 relative" />
  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import ProgressBar from '~/components/dashboard/ProgressBar'
import ToDo from '~/components/dashboard/ToDo'
import Scores from '~/components/dashboard/Scores'
import News from '~/components/dashboard/News'
import History from '~/components/dashboard/History'
import Domains from '~/components/dashboard/Domains'
import heads from '~/config/meta.json'

export default {
  name: 'StudentDashboard',
  layout: 'dashboard_student',
  head() {
    if ( heads[this.$options.name] ) return heads[this.$options.name]
    return {
      title: 'Crée ton avenir'
    }
  },
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  components: { ProgressBar, ToDo, Scores, News, History, Domains },
  async asyncData( { app, error } ) {
    try {
      let response = await app.$api.blog.getFirstPosts( 4 )
      const posts = response.data || []

      response = await app.$api.activities.getUserHistory (app.store.state.auth.user.userID)
      const userHistory = response.data || []

      response = await app.$api.activities.getUserUndoneActivities(app.store.state.auth.user.userID, {course_id: app.store.state.auth.user.course.id})
      const userUndoneActivities = response.data || []
      const activityStep = userUndoneActivities.length

      response = await app.$api.activities.getUserLogs(app.store.state.auth.user.userID, 'final')
      const finalLog = response.data[0]

      if (userUndoneActivities.length <= 2 && !finalLog) {
        userUndoneActivities.push({
          id: 'final',
          name: 'Mes premieres pistes',
          desc: 'Tu as terminé ton autoportrait, dans cette activité, tu vas pouvoir l’analyser et ensuite sélectionner des domaines et des activités à explorer. Prochaine étape pour toi, aller consulter les fiches domaines qui te donneront plus de précisions sur chaque domaine.',
          num: 8,
          slug: 'premieres-pistes',
          status_name: userUndoneActivities.length > 0 ? 'closed' : 'open'
        })
      } else if (finalLog) {
        response = await app.$api.domains.getByIds({ids: finalLog.selection_ids})
        finalLog.domains = response.data || []

        response = await app.$api.domains.getByIds({ids: finalLog.choice_ids})
        finalLog.verbs = response.data || []
      }

      let finalDomains = []
      if (app.store.state.auth.user.config.domain_ids) {
        response = await app.$api.domains.getByIds({ids: app.store.state.auth.user.config.domain_ids})
        finalDomains = response.data || []
      }

      let introspectionStreams = []
      if (app.store.state.auth.user.config.introspection_stream_ids) {
        response = await app.$api.domains.getByIds({ids: app.store.state.auth.user.config.introspection_stream_ids})
        introspectionStreams = response.data || []
      }

      let finalStreams = []
      if (app.store.state.auth.user.config.stream_ids) {
        response = await app.$api.domains.getByIds({ids: app.store.state.auth.user.config.stream_ids})
        finalStreams = response.data || []
      }

      return { posts, userHistory, userUndoneActivities, activityStep, finalLog, finalDomains, introspectionStreams, finalStreams }
    } catch ( e ) {
      return 'error'
    }
  },
  data() {
    return {
      completedSteps: 3,
      totalSteps: 10,
      userUndoneActivities: [],
      finalLog: {},
      finalDomains: [],
      activityStep: 7,
      userStep: 0,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  created() {
    switch (this.stepCalculator(this.$store.state.auth.user.config.currentStep)) {
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
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  width: calc(100vw - 9.1rem);
  @screen sm {
    width: 100vw;
  }

  .custom-background-dashboard {
    top: 10%;
  }
}
</style>

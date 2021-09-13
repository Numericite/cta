<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue">
      <span v-html="texts[0]" />
    </h3>
    <videoComp vimeoId="357033614"
               class="w-full mt-4"
               @clicked="incrementCurrentStep()"/>
    <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-6 text-justify">
      <!--      <span v-html="texts[1]" />-->
      <span> Tu as maintenant défini tes choix de Filières, bravo ! Maintenant que tu as identifié les filières de formation et les spécialités, il te reste à rechercher les établissements auxquels tu peux postuler. Pour ce faire, nous allons te proposerune démarche en plusieurs étapes... </span>
    </div>
    <nuxt-link to="/dashboard/student/etablissements/introspection/identifier">
      <button class="button button--blue mt-8">Je continue</button>
    </nuxt-link>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import VideoComp from '~/components/Video'

  export default {
    name: 'IntrospectionExplanations',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    scrollToTop: true,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    components: { VideoComp },
    data() {
      return {
        texts: this.getPageTexts(this.$route.name)
      }
    },
    methods: {
      async incrementCurrentStep() {
        const user = this.$store.state.auth.user
        if (this.stepCalculator(user.config.currentStep) < 9) {
          this.$store.commit('auth/setCurrentStep', 9)
          await this.$api.user.updateUser(this.$store.state.auth.user)
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>

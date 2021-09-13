<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue"><span v-html="texts[0]" /></h3>
    <div class="desc text-blue text-3xl sm:text-xl font-normal mt-4">
      <span v-html="texts[1]" />
    </div>
    <videoComp :vimeoId="texts[2]"
               class="w-full mt-4"
               @clicked="incrementCurrentStep()"/>
    <img class="mt-8 mb-4" src="~/assets/img/domaines/explanations.png">
    <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal text-justify mt-8">
      <span v-html="texts[3]" />
    </div>
    <nuxt-link to="/dashboard/student/domaines/exploration/rex">
      <button class="button button--blue mt-8">Je continue</button>
    </nuxt-link>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import VideoComp from '~/components/Video'

  export default {
    name: 'ExplorationExplanations',
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
        if (this.stepCalculator(user.config.currentStep) < 4) {
          this.$store.commit('auth/setCurrentStep', 4)
          await this.$api.user.updateUser(this.$store.state.auth.user)
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>

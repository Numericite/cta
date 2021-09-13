<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue">
      <span v-html="texts[0]" />
    </h3>
    <videoComp vimeoId="357033614"
               class="w-full mt-4" />
    <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-6 text-justify">
      <span v-html="texts[1]" />
    </div>
    <img class="mt-8 mb-4 block" src="~/assets/img/filieres/steps.png">
    <nuxt-link to="/dashboard/student/filieres/introspection/education">
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
    }
  }
</script>

<style lang="scss" scoped>
</style>

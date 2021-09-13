<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue">
      <span v-html="texts[0]" />
    </h3>
    <videoComp vimeoId="357033173"
               class="w-full mt-4" />
    <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-12 mb-6 text-justify">
      <span v-html="texts[1]" />
    </div>
    <img class="mt-8 mb-4" src="~/assets/img/filieres/schema.png">
    <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-6 text-justify">
      <span v-html="texts[2]" /><br><br>
      <span class="font-bold text-yellow text-5xl">1.</span> <span v-html="texts[3]" /><br>
      <span class="font-bold text-topaze text-5xl">2.</span> <span v-html="texts[4]" /><br>
      <span class="font-bold text-peach text-5xl">3.</span> <span v-html="texts[5]" /><br>
      <span class="font-bold text-emerald text-5xl">4.</span> <span v-html="texts[6]" /><br><br>
      <span v-html="texts[7]" />
    </div>
    <nuxt-link to="/dashboard/student/filieres/introspection/criterion">
      <button class="button button--blue mt-8">Je continue</button>
    </nuxt-link>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import VideoComp from '~/components/Video'

  export default {
    name: 'IntrospectionEducation',
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

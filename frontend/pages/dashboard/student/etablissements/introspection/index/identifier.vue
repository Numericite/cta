<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue">
      <span v-html="texts[0]" />
    </h3>
    <videoComp vimeoId="357033614"
               class="w-full mt-4" />
    <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-6 text-justify flex flex-col">
      <!--      <span v-html="texts[1]" />-->
      <span>Cet outil est très simple. Tu peux faire tes recherches par :</span>
      <span>• Type de formation : Licences, DUT, BTS...</span>
      <span>• Mention : spécialisation.</span>
      <span>• Type d’établissement : privé / public.</span>
      <span>• Lieu : région, académie, département, ville.</span>
      <span>• Critère spécifique : apprentissage, à distance ...</span>
      <span>  Une fois ta recherche effectuée, tu vas avoir le choix entre un certain nombre de formations.</span>
    </div>
    <button class="button button--blue mt-8" @click="$router.push('/dashboard/student/etablissements/introspection/explorer')">Je continue</button>
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

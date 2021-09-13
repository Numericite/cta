<template>
  <div class="flexContainer-dash min-h-screen dashboard-exploration ml-32 sm:ml-0 mt-12">
    <div class="desc text-blue text-3xl sm:text-xl font-normal mt-4">
      <div class="pb-10">
        C’est le moment de remplir ta Fiche Projet Etudes Sup.
        Pour   chaque   choix   de   filière   (F1,   F2,   F3,   Fn...),   note   les   spécialités   qui
        t’intéressent ainsi que les établissements que tu as repérés avec leurs
        modalités d’entrée (ParcourSup – Concours ou dossier spécifiques avec dates).
        Tu auras ainsi un récapitulatif aide-mémoire de tous tes choix.
      </div>
      <board-project-sup :streams="streams"/>
      <div class="pt-10">Formidable, tu as maintenant ce qu’il te faut pour commencer à t’inscrire sur ParcourSup !</div>
    </div>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import VideoComp from '~/components/Video'
  import _ from 'lodash'
  import BoardProjectSup from '../../../../../../components/dashboard/etablissement/BoardProjectSup'

  export default {
    name: 'ProjectExploration',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    scrollToTop: true,
    components: {
      BoardProjectSup,
      VideoComp
    },
    async asyncData({app, store}) {
      try {
        const chapters = store.state.auth.user.course.chapters
        let streamsChapter = _.find(chapters, { slug: 'stream' })

        let response = await app.$api.domains.getByIds({ids: streamsChapter.domain_ids, page: 1, numberPerPage: 1000})
        let streams = response.data || []

        return { streams }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        texts: this.getPageTexts(this.$route.name),
        streams: [],
      }
    },
    methods: {
      async incrementCurrentStep() {
        const user = this.$store.state.auth.user
        if (this.stepCalculator(user.config.currentStep) < 11) {
          this.$store.commit('auth/setCurrentStep', 11)
          await this.$api.user.updateUser(this.$store.state.auth.user)
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>

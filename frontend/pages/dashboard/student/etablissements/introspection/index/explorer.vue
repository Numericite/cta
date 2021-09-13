<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue">
      <span v-html="texts[0]" />
    </h3>
    <div class="desc text-blue text-3xl sm:text-xl font-normal leading-normal mt-6 pb-16 text-justify flex flex-col">
      <span>Comment choisir les établissements ?</span>
      <span>Pour faire ton choix, tu peux prendre connaissance, sur Parcoursup, des caractéristiques des formations souhaitées et consulter les sites internet de chaque formation. Tu y trouveras :</span>
      <span>• Les contenus des enseignements et leur organisation</span>
      <span>• Les compétences et connaissances attendues, communiquées par le ministère pour ce type de formation </span>
      <span>• Les perspectives en termes de poursuite d’études ou d’insertion professionnelle.</span>
      <span>• Critère spécifique : apprentissage, à distance ...</span>
      <span class="pt-10">Remplis le tableau lié à tes établissements pré-sélectionnés avec toutes les informations ci-dessus.</span>
    </div>
    <div class="w-full flex flex-col items-start">
      <div class="flex w-full pb-16">
        <a href="https://dossier.parcoursup.fr/Candidat/carte" target="_blank"><button class="button button--blue bg-blue-dark mr-20">ParcourSup</button></a>
        <button v-if="!$store.state.auth.user.config.introspection_school_uais" class="button button--blue" @click="openModal">Ajouter un établissement</button>
      </div>
      <div v-for="feedback in onisepFeedbacks" :key="feedback.id" class="pb-16">
        <SchoolFeedback :feedback="feedback" @reloadFeedbacks="loadOnisepFeedback"/>
      </div>
      <div v-if="onisepFeedbacks.length" :class="{'pt-20': onisepFeedbacks.length}" class="flex w-full">
        <button v-if="!$store.state.auth.user.config.introspection_school_uais" class="button button--blue" @click="openModal">Ajouter un établissement</button>
      </div>
    </div>
    <SchoolModal v-if="modalOpen" v-model="modalOpen" :modalOpen="modalOpen" :onisepFeedbacks="onisepFeedbacks" @closeModal="closeModal" @reloadOnisepFeedback="loadOnisepFeedback"/>
    <nuxt-link to="/dashboard/student/etablissements/introspection/choix">
      <button :class="{'pointer-events-none opacity-25' : !onisepFeedbacks.length}" class="button button--blue mt-8">Je continue</button>
    </nuxt-link>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import VideoComp from '~/components/Video'
  import SchoolFeedback from '../../../../../../components/dashboard/etablissement/SchoolFeedback'
  import SchoolModal from '../../../../../../components/dashboard/etablissement/SchoolModal'
  import Button from '../../../../../../mixins/button'

  export default {
    name: 'IntrospectionExplanations',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    scrollToTop: true,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    components: { SchoolFeedback, VideoComp, SchoolModal, Button},
    data() {
      return {
        texts: this.getPageTexts(this.$route.name),
        modalOpen: false,
        onisepFeedbacks: [],
      }
    },
    created() {
      this.loadOnisepFeedback()
    },
    methods: {
       openModal() {
        this.modalOpen = true

      },
      closeModal() {
        this.modalOpen = false
      },
      async loadOnisepFeedback() {
        let response = await this.$api.onisepFeedback.get({page: 1, numberPerPage: 1000, user_ids: [this.$store.state.auth.user.userID]})
        this.onisepFeedbacks = response.data || []

      }
    }
  }
</script>

<style lang="scss" scoped>
</style>

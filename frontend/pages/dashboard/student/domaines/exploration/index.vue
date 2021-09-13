<template>
  <div class="sm:ml-0 relative bg-lightest">
    <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 bg-broken-white border-solid border-b-2 border-light-periwinkle">
      <h2 class="title title--dash pt-16 sm:text-4xl">Domaines - Phase d'exploration</h2>
      <div class="overflow-hidden w-full">
        <div class="flex flex-no-wrap mt-8 relative custom-tabs overflow-auto">
          <nuxt-link to="/dashboard/student/domaines/exploration/explanations" activeClass="hover:text-emerald text-emerald border-solid" class="text-blue-lighter whitespace-no-wrap hover:text-emerald hover:border-solid border-emerald py-6 px-3 mr-2 border-b-3 inline-block text-xl">
            Vidéo explicative
          </nuxt-link>
          <nuxt-link to="/dashboard/student/domaines/exploration/rex" activeClass="hover:text-emerald text-emerald border-solid" class="text-blue-lighter whitespace-no-wrap hover:text-emerald hover:border-solid border-emerald py-6 px-3 mr-2 border-b-3 inline-block text-xl">
            Mon plan d'action
          </nuxt-link>
          <nuxt-link to="/dashboard/student/domaines/exploration/documents" activeClass="hover:text-emerald text-emerald border-solid" class="text-blue-lighter whitespace-no-wrap hover:text-emerald hover:border-solid border-emerald py-6 px-3 mr-2 border-b-3 inline-block text-xl">
            Fiches pédagogiques
          </nuxt-link>
          <nuxt-link to="/dashboard/student/domaines/exploration/ressources" activeClass="hover:text-emerald text-emerald border-solid" class="text-blue-lighter whitespace-no-wrap hover:text-emerald hover:border-solid border-emerald py-6 px-3 border-b-3 inline-block text-xl">
            Ressources externes
          </nuxt-link>
        </div>
      </div>
    </div>
    <nuxt-child/>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

  export default {
    name: 'Exploration',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    data() {
      return {}
    },
    created() {
      const user = this.$store.state.auth.user
      if (this.stepCalculator(user.config.currentStep) < 3) {
        this.$router.push('/')
      }
    }
  }
</script>

<style lang="scss" scoped>
  .custom-tabs {
    top: 2px;
  }
</style>

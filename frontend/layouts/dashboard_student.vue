<template>
  <div class="bg-blue-lightest sm:overflow-x-hidden">
    <on-boarding v-if="onboarding"
                 @close="closeOnBoarding()" />
    <go-exploration v-if="goDomainsExplorationModal" kind="activity_area" @close="goDomainsExploration()" />
    <go-exploration v-if="goStreamsExplorationModal" kind="stream" @close="goStreamsExploration()" />
    <go-exploration v-if="goSchoolExplorationModal" kind="school" @close="goSchoolExploration()" />
    <Navbar class="navbar w-screen" />
    <user-hero :user="$store.state.auth.user" :classroom="userClassroom" :isManager="false" class="mt-24" />
    <toolbar class="toolbar" />
    <nuxt />
    <Footer class="footer" />
  </div>
</template>

<script>
import UserHero from '~/components/dashboard/UserHero'
import Toolbar from '~/components/dashboard/Toolbar'
import Footer from '~/components/Footer'
import Navbar from '~/components/dashboard/Navbar'
import OnBoarding from '~/components/onboarding/OnBoarding'
import GoExploration from '~/components/dashboard/GoExploration'

export default {
  name: 'Dashboard',
  components: { Navbar, Footer, UserHero, Toolbar, OnBoarding, GoExploration },
  data() {
    return {
      onboarding: true,
      goDomainsExplorationModal: false,
      goStreamsExplorationModal: false,
      goSchoolExplorationModal: false,
      userClassroom: {}
    }
  },
  created() {
    if (this.stepCalculator(this.$store.state.auth.user.config.currentStep) > 0) {
      this.onboarding = false
    }
    if (this.stepCalculator(this.$store.state.auth.user.config.currentStep) === 3) {
      this.goDomainsExplorationModal = true
    }
    if (this.stepCalculator(this.$store.state.auth.user.config.currentStep) === 6) {
      this.goStreamsExplorationModal = true
    }
    if (this.stepCalculator(this.$store.state.auth.user.config.currentStep) === 10) {
      this.goSchoolExplorationModal = true
    }
  },
  methods: {
    goDomainsExploration() {
      this.goDomainsExplorationModal = false
      this.$router.push('/dashboard/student/domaines/exploration/explanations')
    },
    goStreamsExploration() {
      this.goStreamsExplorationModal = false
      this.$router.push('/dashboard/student/filieres/exploration/explanations')
    },
    goSchoolExploration() {
      this.goSchoolExplorationModal = false
      this.$router.push('/dashboard/student/etablissements/exploration/explanations')
    },
    closeOnBoarding() {
      this.onboarding = false
      this.$router.push('/dashboard/student/domaines/introspection')
    }
  }
}
</script>

<style lang="scss" scoped>
p {
  @apply leading-normal;
  margin-bottom: 1.3333rem;
}

.toolbar {
  top: 5rem;
  position: sticky;
  z-index: 100;
  @screen sm {
    position: fixed;
    top: auto;
    bottom: 0;
    z-index: 3000;
  }
}

.navbar {
  position: fixed;
  top: 0;
  z-index: 101;
}

.footer {
  z-index: 150;
}
</style>

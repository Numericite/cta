<template>
  <div :class="{'bg-blue-darker': !isManager}" class="userHero w-full relative overflow-hidden">
    <div class="flexContainer py-12 sm:py-6 sm:pb-12 flex">

      <svg v-if="!isManager" class="hero__wave absolute pin-t pin-t pin-l z-0"
           width="508"
           height="255"
           viewBox="0 0 508 250">
        <path fill="#432CB4"
              fill-rule="nonzero"
              d="M0 0c58.667 5.931 98.583 30.08 119.749 72.444 31.749 63.548 83.108 136.63 222.108 133.072 92.667-2.372 147.938 12.08 165.815 43.355L0 250V0z" />
      </svg>

      <!-- Avatar -->
      <div class="avatar z-10 mr-12 sm:absolute">
        <div class="h-48 sm:h-24 w-48 sm:w-24 rounded-full border-8 border-blue-lighter border-solid relative flex justify-center items-start">
          <img v-if="user.avatar_path"
               :src="user.avatar_path"
               class="w-full rounded-full">
          <img v-if="!isManager" src="~/assets/img/pencil.svg" class="bg-blue-lighter p-3 rounded-full absolute pin-b pin-r cursor-pointer sm:hidden" @click="goProfile">
        </div>
      </div>
      <!-- End Avatar -->

      <!-- Content -->
      <div class="flex flex-col w-full z-10">

        <!-- Top part -->
        <div v-if="user"
             class="w-full flex justify-between sm:pl-32 sm:pt-2 pt-8">
          <h1 :class="{'text-white': !isManager, 'text-blue': isManager}" class="text-6xl sm:text-3xl font-extrabold sm:w-2/3">
            <span v-if="!isManager">Bonjour</span><span v-else>Fiche détaillée de </span> {{ user.firstName }}
          </h1>
          <span v-if="!isManager" class="text-5xl font-extrabold text-blue text-right leading-normal">{{ school.name }}<br>{{ classroom.name }}</span>
        </div>
        <!-- End Top part -->

      </div>
      <!-- End Content -->
    </div>

    <div v-if="isManager" class="block w-full px-24 h-28 sm:hidden">
      <progress-bar :nbActivitiesUndone="0" :step="userStep" :isManager="true" :isLightVersion="false" class="z-10 relative" />
    </div>
  </div>
</template>

<script>
import ProgressBar from '~/components/dashboard/ProgressBar'

export default {
  name: 'UserHero',
  components: { ProgressBar },
  props: {
    user: {
      type: Object,
      default() {
        return {
          firstName: '',
          lastName: '',
          avatar_path: ''
        }
      }
    },
    isManager: {
      type: Boolean,
      default() {
        return false
      }
    }
  },
  data() {
    return {
      userStep: 0,
      school: this.$store.state.auth.user.school,
      classroom: this.$store.state.auth.user.classroom
    }
  },
  created() {
    switch (this.stepCalculator(this.user.config.currentStep)) {
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
  },
  methods: {
    goProfile() {
      this.$router.push('/dashboard/profil')
    }
  }
}
</script>

<style lang="scss" scoped>
.avatar {
  transform: translateY(-10%);
}
.hero__wave {
  @screen sm {
    transform: translateY(25%);
  }
}
</style>

<template>
  <div class="onboarding fixed pin-t pin-l w-screen h-screen flex justify-center items-center px-3 sm:py-3">

    <div class="layer" />

    <div class="onboardingCard w-1/2 sm:w-full bg-white py-8 rounded-lg flex flex-col items-center sm:my-3 max-h-80 overflow-auto">

      <img class="cursor-pointer self-end pr-8"
           src="~/static/icons/close-blue.svg"
           alt="fermer le tutoriel"
           @click="$emit('close')">

      <div v-if="currentStep == 0" class="w-full flex flex-col items-center">
        <videoComp vimeoId="357031204"
                   class="w-full mt-4" />
        <button class="button button--blue mt-4"
                @click="currentStep++">Suivant</button>
      </div>

      <!-- Step 1 -->
      <div v-if="currentStep == 1"
           class="w-full flex flex-col items-center">
        <img :src="'/img/onboarding/step' + currentStep + '.png'"
             alt="">
        <p class="text-blue uppercase font-bold mt-3">Etape {{ currentStep }} <span class="text-orange">sur 3</span></p>
        <h3 class="text-blue title text-2xl font-bold pb-2">
          <span v-html="texts[0]" />
        </h3>
        <p class="text-blue text-3xl w-3/5 text-center">
          <span v-html="texts[1]" />
        </p>

        <button class="button button--blue mt-4"
                @click="currentStep++">suivant</button>
      </div>
      <!-- End Step 1 -->

      <!-- Step 2 -->
      <div v-if="currentStep == 2"
           class="w-full flex flex-col items-center">
        <img :src="'/img/onboarding/step' + currentStep + '.png'"
             alt="">
        <p class="text-blue uppercase font-bold mt-3">Etape {{ currentStep }} <span class="text-orange">sur 3</span></p>
        <h3 class="text-blue title text-2xl font-bold pb-2">
          <span v-html="texts[2]" />
        </h3>
        <p class="text-blue text-3xl w-3/5 text-center">
          <span v-html="texts[3]" />
        </p>

        <button class="button button--blue mt-4"
                @click="currentStep++">suivant</button>
      </div>
      <!-- End Step 2 -->

      <!-- Step 3 -->
      <div v-if="currentStep == 3"
           class="w-full flex flex-col items-center">
        <img :src="'/img/onboarding/step' + currentStep + '.png'"
             alt="">
        <p class="text-blue uppercase font-bold mt-3">Etape {{ currentStep }} <span class="text-orange">sur 3</span></p>
        <h3 class="text-blue title text-2xl font-bold pb-2">
          <span v-html="texts[4]" />
        </h3>
        <p class="text-blue text-3xl w-3/5 text-center">
          <span v-html="texts[5]" />
        </p>
        <button class="button button--blue mt-4"
                @click="$emit('close')">Je découvre</button>
      </div>
      <!-- End Step 3 -->

      <div class="steps">
        <div v-for="(step, index) in 4"
             :key="step"
             :class="[(index - 1) < currentStep ? 'boarding-step--active' : '']"
             class="step" />
      </div>

    </div>

  </div>
</template>

<script>
import VideoComp from '~/components/Video'

export default {
  name: 'OnBoarding1',
  components: { VideoComp },
  data() {
    return {
      currentStep: 0,
      texts: this.getPageTexts('popup-welcome')
    }
  },
  async destroyed() {
    const user = this.$store.state.auth.user
    if (!user.config.currentStep || this.stepCalculator(user.config.currentStep) < 1) {
      this.$store.commit('auth/setCurrentStep', 1)
      await this.$api.user.updateUser(this.$store.state.auth.user)
    }
  }
}
</script>

<style lang="scss">
.onboarding {
  z-index: 9000;
}
.layer {
  @apply bg-black w-screen h-screen z-50 fixed pin-t pin-l;
  opacity: 0.6;
}
.onboardingCard {
  z-index: 9000;
}
.steps {
  @apply flex mt-6;
  .step {
    width: 9px;
    height: 9px;
    background-color: #ffece4;
    @apply rounded-full mx-2;
  }
  .boarding-step--active {
    @apply bg-orange;
  }
}
</style>

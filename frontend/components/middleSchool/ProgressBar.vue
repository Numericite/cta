<template>
  <div :class="[exploration || bgColor === 'red' ? 'border-ms-red' : 'border-ms-blue']" class="w-full border-1 border-solid rounded-full relative flex items-center justify-between bg-white">
    <div v-if="exploration" :class="{
      'border-ms-red' : bgColor === 'red',
    }" class="w-full py-1 px-1 border-1 border-solid rounded-lg">
      <div :class="{
        'border-ms-red bg-ms-red' : bgColor === 'red',
        'border-ms-blue bg-ms-blue' : bgColor === 'blue',
        'w-3' : currentStepExploration === 0,
        'w-1/5' : currentStepExploration === 1,
        'w-2/5' : currentStepExploration === 2,
        'w-3/5' : currentStepExploration === 3,
        'w-4/5' : currentStepExploration === 4,
        'w-full' : currentStepExploration === 5,
      }" class="rounded-full border-2 border-solid"/>
    </div>
    <div v-for="step in totalSteps" v-else :key="step" :class="{
      'rounded-l-full ml-1' : step === 1,
      'rounded-r-full mr-1' : step === currentStep,
      'border-ms-blue': (step <= currentStep) && bgColor !== 'red',
      'border-ms-red': (step <= currentStep) && bgColor === 'red',
      'border-2 border-solid bg-ms-blue' : step <= currentStep,
      'w-3' : currentStep === 0,
      'w-1/5' : currentStep === 1,
      'w-2/5' : currentStep === 2, 'w-4/5' : currentStep === 4, 'w-full' : currentStep === 5
    }" class="h-full w-full my-1"/>
  </div>
</template>

<script>
  export default {
    name: 'ProgressBar',
    props: {
      entity: {
        type: Object,
        default: function() {
          return {}
        }
      },
      bgColor: {
        type: String,
        default: 'yellow'
      },
      exploration: {
        type: Boolean,
        default: function() {
          return false
        }
      },
      currentStep: {
        type: Number,
        default: 1
      },
      currentStepExploration: {
        type: Number,
        default: 1
      },
      totalSteps: {
        type: Number,
        default: 1
      }
    }
  }
</script>

<style scoped>

</style>

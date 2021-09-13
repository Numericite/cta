<template>
  <div class="w-full border-1 border-solid rounded-full border-ms-blue relative flex items-center justify-between bg-white">
    <div v-for="step in totalSteps +1" :key="step" :class="{'border-ms-blue bg-ms-blue border-1 ' : step !==1 && step !== totalSteps +1}" class=" border-solid h-6 opacity-50 "/>
    <div class="w-full flex justify-around items-center absolute h-4/5">
      <div v-for="step in totalSteps" :key="step" :class="{'rounded-l-full ml-1' : step === 1, 'rounded-r-full mr-1' : step === currentStep, 'border-2 border-solid border-ms-blue bg-ms-blue' : step <= currentStep }" class="h-full w-full my-1"/>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'BigProgressBar',
    props: {
      currentStep: {
        type: Number,
        default: function() {
          return 1
        }
      },
      totalSteps: {
        type: Number,
        default: function() {
          return 1
        }
      }
    }
  }
</script>

<style scoped>

</style>

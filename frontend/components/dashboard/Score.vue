<template>
  <div class="score bg-white py-8 px-12 flex flex-col items-center justify-center">

    <no-ssr>
      <radial-progress-bar :diameter="110"
                           :completed-steps="completedSteps"
                           :startColor="color"
                           :stopColor="color"
                           :innerStrokeColor="innerColor"
                           :total-steps="totalSteps">
        <span class="flex items-end">
          <span class="text-blue text-4xl font-extrabold mr-1">{{ completedSteps }} </span>
          <span class="text-blue text-xl font-bold">/ {{ totalSteps }}</span>
        </span>
      </radial-progress-bar>
    </no-ssr>
    <p class="mb-0 mt-2 font-bold text-blue text-lg text-center"
       v-html="desc" />

  </div>
</template>

<script>
export default {
  name: 'Score',
  props: {
    desc: {
      type: String,
      default: function() {
        return { desc: null }
      }
    },
    totalSteps: {
      type: Number,
      default: function() {
        return { totalSteps: 0 }
      }
    },
    completedSteps: {
      type: Number,
      default: function() {
        return { completedSteps: 0 }
      }
    },
    color: {
      type: String,
      default: function() {
        return { color: 'blue' }
      }
    },
    innerColor: {
      type: String,
      default: function() {
        return { color: 'blue' }
      }
    }
  },
  methods: {
    progress(event, progress, stepValue) {
      // console.log(stepValue)
    }
  }
}
</script>

<style lang="scss" scoped>
.score {
  border-radius: 15px;
  box-shadow: 0 0 30px 0 rgba(40, 21, 134, 0.11);
}
</style>

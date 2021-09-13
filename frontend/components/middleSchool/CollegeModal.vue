<template>
  <div :class="{'items-center': !modalFullWidth}" class="flex justify-center fixed w-screen h-screen py-8 fixed pin-t pin-l bg-ms-gray-light bg-opacity-50 z-151">
    <div :class="[modalFullWidth ? 'w-5/6 xl:w-2/3 h-full' : 'w-1/3 h-1/3']" class="bg-white  shadow-lg rounded-lg relative  p-3 ">
      <div :class="{'border-ms-blue-dark': count === 0, 'border-ms-green' : count === 1, 'border-ms-red' : count === 2, 'border-ms-blue' : count === 3, 'border-ms-red-light' : newExploration }" class="h-full flex justify-center w-full border-solid border-1 rounded-lg border-opacity-25 relative">
        <div class="absolute pin-r pin-t p-10" @click="handleClose()">
          <img src="~/static/icons/times.png" class="w-4 h-4 cursor-pointer">
        </div>
        <div class="overflow-y-auto w-full">
          <slot/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'CollegeStudentDashboard',
    layout: 'dashboard_ms_student',
    props: {
      count: {
        type: Number,
        default: function() {
          return 1
        }
      },
      newExploration: {
        type: Boolean,
        default: function() {
          return false
        }
      },
      modalFullWidth: {
        type: Boolean,
        default: function() {
          return true
        }
      }
    },
    methods: {
      handleClose() {
        this.$emit('handleClose')
      }
    }
  }
</script>

<style lang="scss" scoped>
  .wave {
    filter: blur(100px);
  }
</style>

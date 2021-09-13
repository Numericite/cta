<template>
  <div class="student w-full shadow-md rounded-lg bg-white flex items-center py-6 px-4">
    <div class="flex xl:flex-row flex-col items-center justify-between w-full">
      <div class="flex items-center">
        <img :src="student.avatar_path" class=" w-10 h-10 border-2 border-purple border-solid rounded-full">
        <div class="flex justify-start items-center pl-4">
          <div class="text-4xl font-bold">{{ student.firstName }} {{ student.lastName }}</div>
        </div>
        <div class="text-ms-gray-light text-4xl pl-4"> {{ student.classroom.name }}</div>
      </div>
      <div class="flex xl:w-1/3 w-full items-center px-4 xl:px-0 xl:mb-0">
        <div class="flex flex-col w-full mr-8 mt-4 xl:mt-0">
          <ProgressBar :currentStep="currentStep" :totalSteps="totalSteps" :bgColor="accountType === 'partner' ? 'red' : 'blue'"/>
          <div class="justify-end flex text-center text-xl items-center text-ms-gray font-bold pt-4">
            <div :class="accountType === 'partner' ? 'text-ms-red' : 'text-ms-blue'" class="pr-2"> {{ totalSteps - currentStep }} </div>
            <div class="text-ms-gray-dark">séquences restantes</div>
          </div>
        </div>
        <div>
          <button :class="accountType === 'partner' ? 'border-ms-red' : 'border-ms-blue'" class="bg-white focus:outline-none border-1 border-solid px-6 py-5 rounded-lg" @click="$router.push('/dashboard/college/' + accountType + '/eleves/' + student.userID)">
            <svg :class="accountType === 'partner' ? 'text-ms-red' : 'text-ms-blue'" class="fill-current" width="12" height="10" viewBox="0 0 9 7" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M5.08594 1.27734L6.375 2.54688H0.59375C0.320312 2.54688 0.125 2.76172 0.125 3.01562V3.48438C0.125 3.75781 0.320312 3.95312 0.59375 3.95312H6.375L5.08594 5.24219C4.91016 5.41797 4.91016 5.73047 5.08594 5.90625L5.41797 6.23828C5.59375 6.41406 5.90625 6.41406 6.08203 6.23828L8.71875 3.58203C8.91406 3.40625 8.91406 3.11328 8.71875 2.9375L6.08203 0.28125C5.90625 0.105469 5.59375 0.105469 5.41797 0.28125L5.08594 0.613281C4.91016 0.789062 4.91016 1.10156 5.08594 1.27734Z"/>
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ProgressBar from './ProgressBar'

  export default {
    name: 'Student',
    components: { ProgressBar },
    props: {
      student: {
        type: Object,
        default: function() {
          return {}
        }
      },
      accountType: {
        type: String,
        default: function() {
          return ''
        }
      }
    },
    data() {
      return {
        totalSteps: 0,
        currentStep: 0
      }
    },
    watch: {
      student() {
        this.setSteps()
      }
    },
    created() {
      this.setSteps()
    },
    methods: {
      async setSteps() {
        let response = await this.$api.modules.getProgress(this.student.userID, undefined, this.student.classroom.grade)
        const progress = response.data || []

        this.totalSteps = progress.length
        this.currentStep = _.filter(progress, {'isDone': true}).length
      }
    }
  }
</script>

<style scoped>
</style>

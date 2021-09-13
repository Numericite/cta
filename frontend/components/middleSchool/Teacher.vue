<template>
  <div class="student w-full shadow-md rounded-lg bg-white flex items-center py-6 px-4">
    <div class="flex xl:flex-row flex-col items-center justify-between w-full">
      <div class="flex w-full items-center">
        <img :src="teacher.avatar_path" class="w-12 h-12 border-2 border-purple border-solid rounded-full">
        <div class="flex justify-start items-center pl-4">
          <div class="text-4xl font-bold">{{ teacher.firstName }} {{ teacher.lastName }}</div>
        </div>
        <div class="text-ms-gray-light text-4xl pl-4"> {{ teacher.school.name }}</div>
      </div>
      <div class="flex ml-auto justify-end items-center px-4 xl:px-0 xl:mb-0">
        <button class="bg-white focus:outline-none border-1 border-solid border-purple px-6 py-5 rounded-lg" @click="$router.push('/dashboard/college/' + accountType + '/teachers/' + teacher.userID)">
          <svg class="text-purple fill-current" width="12" height="10" viewBox="0 0 9 7" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M5.08594 1.27734L6.375 2.54688H0.59375C0.320312 2.54688 0.125 2.76172 0.125 3.01562V3.48438C0.125 3.75781 0.320312 3.95312 0.59375 3.95312H6.375L5.08594 5.24219C4.91016 5.41797 4.91016 5.73047 5.08594 5.90625L5.41797 6.23828C5.59375 6.41406 5.90625 6.41406 6.08203 6.23828L8.71875 3.58203C8.91406 3.40625 8.91406 3.11328 8.71875 2.9375L6.08203 0.28125C5.90625 0.105469 5.59375 0.105469 5.41797 0.28125L5.08594 0.613281C4.91016 0.789062 4.91016 1.10156 5.08594 1.27734Z"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
  import ProgressBar from './ProgressBar'

  export default {
    name: 'Teacher',
    components: { ProgressBar },
    props: {
      teacher: {
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
  }
</script>
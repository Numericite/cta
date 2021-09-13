<template>
  <div class="entity w-full z-10">
    <div class="bg-white shadow-lg p-3 rounded-lg cursor-pointer" @click="disabled ? null : $router.push('/dashboard/college/student/programme/module/' + module.id)">
      <div :class="{'border-ms-gray': disabled, 'border-ms-blue-light': !disabled}" class="min-w-100 border-ms-blue-light bg-white min-h-100 border-solid border-1 rounded-lg relative">
        <div class="py-2 w-full">
          <div class="flex flex-row items-center justify-between w-full px-6">
            <div class="flex flex-col w-2/3">
              <div class="flex justify-start items-center pt-6">
                <div :class="{'text-ms-gray': disabled}" class="text-5xl font-bold">{{ module.name }}</div>
              </div>
              <p :class="{'text-ms-gray': disabled, 'text-ms-gray-dark': !disabled}" class="font-plex py-2 text-left text-justify text-xl leading-loose break-all">
                {{ module.description | str_limit(200) }}
              </p>
            </div>
            <div v-if="disabled" class="flex flex-col w-1/3">
              <svg class="ml-auto mr-4 text-xl" width="11" height="13" viewBox="0 0 11 13" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M9.375 5.75H8.8125V4.0625C8.8125 2.11719 7.19531 0.5 5.25 0.5C3.28125 0.5 1.6875 2.11719 1.6875 4.0625V5.75H1.125C0.492188 5.75 0 6.26562 0 6.875V11.375C0 12.0078 0.492188 12.5 1.125 12.5H9.375C9.98438 12.5 10.5 12.0078 10.5 11.375V6.875C10.5 6.26562 9.98438 5.75 9.375 5.75ZM6.9375 5.75H3.5625V4.0625C3.5625 3.14844 4.3125 2.375 5.25 2.375C6.16406 2.375 6.9375 3.14844 6.9375 4.0625V5.75Z" fill="#AEAAC2"/>
              </svg>
            </div>
            <div v-if="!disabled && !doneDate" class="flex flex-col w-1/3 items-center">
              <button class="bg-white outline-none border-1 border-solid px-7 py-6 rounded-lg border-ms-blue ml-auto">
                <svg width="12" height="10" viewBox="0 0 9 7" fill="none" xmlns="http://www.w3.org/2000/svg" class="fill-current text-ms-blue">
                  <path d="M5.08594 1.27734L6.375 2.54688H0.59375C0.320312 2.54688 0.125 2.76172 0.125 3.01562V3.48438C0.125 3.75781 0.320312 3.95312 0.59375 3.95312H6.375L5.08594 5.24219C4.91016 5.41797 4.91016 5.73047 5.08594 5.90625L5.41797 6.23828C5.59375 6.41406 5.90625 6.41406 6.08203 6.23828L8.71875 3.58203C8.91406 3.40625 8.91406 3.11328 8.71875 2.9375L6.08203 0.28125C5.90625 0.105469 5.59375 0.105469 5.41797 0.28125L5.08594 0.613281C4.91016 0.789062 4.91016 1.10156 5.08594 1.27734Z" />
                </svg>
              </button>
            </div>
            <div v-if="!disabled && doneDate" class="flex flex-col w-1/3 pl-12">
              <svg class="ml-auto" width="13" height="9" viewBox="0 0 13 9" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M5.05469 8.8125C5.28906 9.04688 5.6875 9.04688 5.92188 8.8125L12.8125 1.92188C13.0469 1.6875 13.0469 1.28906 12.8125 1.05469L11.9688 0.210938C11.7344 -0.0234375 11.3594 -0.0234375 11.125 0.210938L5.5 5.83594L2.85156 3.21094C2.61719 2.97656 2.24219 2.97656 2.00781 3.21094L1.16406 4.05469C0.929688 4.28906 0.929688 4.6875 1.16406 4.92188L5.05469 8.8125Z" fill="#27AAFF"/>
              </svg>
              <div class="justify-end flex text-center text-xl items-center text-ms-gray font-bold pt-4">
                <div class="text-ms-gray-dark mr-2">complété le</div>
                <div class="text-ms-blue">{{ formatDate(doneDate) }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import moment from 'moment'
  import ProgressBar from '../middleSchool/ProgressBar'

  export default {
    name: 'Module',
    components: { ProgressBar },
    props: {
      module: {
        type: Object,
        default: function() {
          return {}
        }
      },
      disabled: {
        type: Boolean,
        default: false
      },
      doneDate: {
        type: Number,
        default: null
      }
    },
    data() {
      return {
      }
    },
    methods: {
      formatDate: function(date) {
        return moment(date).locale('fr').format('D MMMM YYYY')
      }
    }
  }
</script>

<style scoped>
</style>

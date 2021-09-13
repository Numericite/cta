<template>
  <div class="student w-full shadow-md rounded-lg bg-white flex items-center py-6 px-10 xl:px-0">
    <CollegeModal v-show="showVideo" :count="3" @handleClose="showVideo = false">
      <VideoModal :video="video"/>
    </CollegeModal>
    <div class="flex xl:flex-row flex-col items-center justify-between w-full">
      <div class="flex xl:w-2/3 w-full">
        <div class="flex w-24 xl:w-16 flex-col justify-start items-center pr-4 xl:pr-0">
          <img v-if="kind === 'link'" src="~/assets/img/link.svg" class="w-4 h-4">
          <img v-else-if="kind === 'link_video'" src="~/assets/img/video.svg" class="w-4 h-4">
          <img v-else src="~/assets/img/file.svg" class="w-4 h-4">
        </div>
        <div class="flex flex-col">
          <div class="pb-2">{{ kind === 'link_video' ? 'Vidéo' : kind === 'link' ? 'Lien externe' : 'Fiche' }} : <span class="font-bold">{{ document.title }}</span></div>
          <div class="text-ms-gray-dark"> {{ document.description }}</div>
        </div>
      </div>
      <div class="flex justify-end w-1/3 items-center pr-5 xl:mb-0">
        <button v-if="kind === 'link_video'" :href="''" class="center-vertical button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue p-4" @click="showVideoModal(document)">
          <img src="~/assets/img/play.svg" class="w-4 h-4">
        </button>
        <a v-else :href="document.path" target="_blank" class="center-vertical button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue p-4">
          <img v-if="kind === 'link'" src="~/assets/img/link_white.svg" class="w-4 h-4">
          <img v-else src="~/assets/img/arrow-to-bottom.svg" class="w-4 h-4">
        </a>
      </div>
    </div>
  </div>
</template>

<script>
  import CollegeModal from './CollegeModal'
  import VideoModal from './modals/VideoModal'

  export default {
    name: 'File',
    components: { CollegeModal, VideoModal },
    props: {
      document: {
        type: Object,
        default: function() {
          return {}
        }
      },
      kind: {
        type: String,
        default: function() {
          return ''
        }
      }
    },
    data() {
      return {
        showVideo: false,
        video: {},
      }
    },
    methods: {
      showVideoModal (document) {
        this.video = document
        this.showVideo = true
      }
    }
  }
</script>

<style scoped>

</style>

<template>
  <div class="flex justify-center">
    <CollegeModal v-show="showVideo" :count="3" @handleClose="showVideo = false">
      <VideoModal :video="video"/>
    </CollegeModal>
    <div class="w-4 mr-2">
      <img v-if="kind === 'form' && !activity.formDone" src="~/assets/img/times.svg" class="">
      <img v-else-if="activity.formDone" src="~/assets/img/check.svg">
      <img v-else-if="kind === 'link'" src="~/assets/img/link.svg">
      <img v-else-if="kind === 'link_video'" src="~/assets/img/video.svg" style="max-width: none;">
      <img v-else src="~/assets/img/file.svg">
    </div>
    <div class="w-full pr-16 text-justify">
      {{ kind === 'form' ? 'Retour' : kind === 'link' ? 'Lien externe' : kind === 'link_video' ? 'Vidéo' : kind === 'main-document' ? 'Fiche d\'activité' :'Fiche ressource' }} : <span class="font-bold">{{ kind === 'form' ? activity.name : document.title }}</span>
      <p v-if="(kind === 'main-document' && document.description) || (kind !== 'form' && kind !== 'main-document' && document.short_description)" class="text-xl text-ms-gray-dark mt-2 mb-0">
        {{ kind === 'main-document' ? document.description : kind !== 'form' ? document.short_description : 'form' }}
      </p>
    </div>
    <div class="w-48 relative">
      <nuxt-link v-if="kind === 'form'" :to="'/dashboard/college/student/formulaire/' + activity.id"
                 :class="{'bg-ms-blue border-ms-blue hover:bg-ms-blue': !isExploration, 'bg-ms-red border-ms-red hover:bg-ms-red': isExploration}"
                 class="w-full flex justify-center center-vertical button-ms text-white border-solid border-1 hover:text-white p-4">
        <div v-if="activity.formDone" class="flex">
          <img src="~/assets/img/eye.svg" class="w-4 h-4 mr-4">
          Revoir
        </div>
        <div v-else class="flex">
          <img src="~/assets/img/pen.svg" class="w-4 h-4 mr-4">
          Compléter
        </div>
      </nuxt-link>
      <button v-else-if="kind === 'link_video'" :href="''"
              :class="{'bg-ms-blue border-ms-blue hover:bg-ms-blue': !isExploration, 'bg-ms-red border-ms-red hover:bg-ms-red': isExploration}"
              class="center-vertical button-ms text-white border-solid border-1 p-4" @click="showVideoModal(document)">
        <img src="~/assets/img/play.svg" class="w-4 h-4">
      </button>
      <a v-else :href="document.path"
         :class="{'bg-ms-blue border-ms-blue hover:bg-ms-blue': !isExploration, 'bg-ms-red border-ms-red hover:bg-ms-red': isExploration}"
         class="center-vertical button-ms text-white border-solid border-1 p-4"
         target="_blank">
        <img v-if="kind === 'link'" src="~/assets/img/link_white.svg" class="w-4 h-4">
        <img v-else src="~/assets/img/arrow-to-bottom.svg" class="w-4 h-4">
      </a>
    </div>
  </div>
</template>

<script>
  import CollegeModal from './CollegeModal'
  import VideoModal from './modals/VideoModal'
  import Video from '../Video'
  import Button from '../../mixins/button'

  export default {
    name: 'ModuleActivityDocument',
    components: { Button, Video, CollegeModal, VideoModal },
    props: {
      document: {
        type: Object,
        default () {
          return {}
        }
      },
      kind: { // form /
        type: String,
        default: function() {
          return ''
        }
      },
      activity: {
        type: Object,
        default() {
          return {}
        }
      },
      isExploration: {
        type: Boolean,
        default: false
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

<style lang="scss" scoped>
  .center-vertical {
    @apply absolute;
    top: 50%;
    left: 50%;
    transform: translateY(-50%)translateX(-50%);
  }
</style>

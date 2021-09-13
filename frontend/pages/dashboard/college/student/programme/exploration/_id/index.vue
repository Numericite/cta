<template>
  <div class="w-full flex flex-col pt-10 px-10 xl:px-0">
    <CollegeModal v-show="showVideoStartExploration" :count="2" @handleClose="closeVideoStartModal">
      <VideoModal :video="videoStartExploration"/>
    </CollegeModal>
    <div v-if="!openModal">
      <div class="flex ml-6 xl:ml-0 pt-16 pb-6 xl:pb-20 items-center justify-between w-full relative">
        <button class="absolute ml-3 xl:ml-0 back-btn rounded-full w-8 h-8 bg-ms-red fill-current text-white flex items-center justify-center" @click="$router.go(-1)">
          <svg width="12" height="9" viewBox="0 0 12 9" fill="none" xmlns="http://www.w3.org/2000/svg" class="fill-current">
            <path d="M5.52344 6.89062L3.97656 5.34375H10.9375C11.2422 5.34375 11.5 5.10938 11.5 4.78125V4.21875C11.5 3.91406 11.2422 3.65625 10.9375 3.65625H3.97656L5.52344 2.13281C5.73438 1.92188 5.73438 1.54688 5.52344 1.33594L5.125 0.9375C4.91406 0.726562 4.53906 0.726562 4.32812 0.9375L1.16406 4.125C0.929688 4.33594 0.929688 4.6875 1.16406 4.89844L4.32812 8.08594C4.53906 8.29688 4.91406 8.29688 5.125 8.08594L5.52344 7.6875C5.73438 7.47656 5.73438 7.10156 5.52344 6.89062Z" fill="white"/>
          </svg>
        </button>
        <div class="flex justify-start items-center pl-2">
          <div class="dot mr-2 bg-ms-red"/>
          <div class="ms-subtitle text-ms-red">Explorer</div>
          <div class="dot ml-2 bg-ms-red"/>
        </div>
        <img :src="'/img/middle-school/boy-button.png'" class="absolute pin-t pin-r w-32 boy-button">
        <button class="center-vertical button-ms text-white bg-ms-red border-solid border-1 border-ms-red hover:bg-ms-red ml-4 p-4" @click="showVideoStartExploration = true">
          <img src="~/assets/img/play.svg" class="h-3 w-4"> Revoir la vidéo de présentation
        </button>
      </div>
      <div class="ms-title pb-12">{{ explorationType.name }}</div>
      <ActivityMs :explorationType="explorationType" class="mb-12 rounded-lg" @createExploration="createExploration"/>
      <div v-if="explorationType.documents.length > 0" class="flex items-center w-full justify-start py-12">
        <div class="dot mr-4 bg-black"/>
        <div class="ms-subtitle">Ressources et guides</div>
        <div class="dot ml-4 bg-black"/>
      </div>
      <div v-if="explorationType.documents.length > 0" class="w-full bg-white p-3 rounded-lg relative">
        <img :src="'/img/middle-school/girl-activity.png'" class="absolute pin-r pin-t w-28 girl-activity">
        <div class="w-full bg-white p-6 rounded-lg border border-1 border-solid border-ms-red">
          <div class="flex items-center w-full justify-start mb-12">
            <div class="dot mr-4 bg-ms-red"/>
            <div class="ms-subtitle text-ms-red">{{ explorationType.documents.length }} ressource(s)</div>
            <div class="dot ml-4 bg-ms-red"/>
          </div>
          <ModuleActivityDocument v-for="(document, index) in explorationType.documents" :key="document.id" :document="document" :kind="document.file_kind" :class="{'mt-0': index === 0, 'mb-4': index === (explorationType.documents.length - 1)}" :isExploration="true" class="my-16"/>
        </div>
      </div>
    </div>
    <ExplorationModal v-if="openModal" :explorationType="explorationType" @handleClose="closeModal"/>
  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import ActivityMs from '../../../../../../../components/middleSchool/ActivityMs'
import Entity from '../../../../../../../components/middleSchool/Entity'
import ExplorationType from '../../../../../../../components/middleSchool/ExplorationType'
import dashboard_ms from '../../../../../../../layouts/dashboard_ms_student'
import Button from '../../../../../../../mixins/button'
import _ from 'lodash'
import ExplorationModal from '../../../../../../../components/middleSchool/modals/ExplorationModal'
import ModuleActivityDocument from '../../../../../../../components/middleSchool/ModuleActivityDocument'
import VideoModal from '../../../../../../../components/middleSchool/modals/VideoModal'
import CollegeModal from '../../../../../../../components/middleSchool/CollegeModal'

export default {
  name: 'Index',
  layout: 'dashboard_ms_student',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessMiddleSchoolStudentDashboard',
  components: { ExplorationModal, Button, Entity, ActivityMs, ExplorationType, ModuleActivityDocument, CollegeModal, VideoModal },
  async asyncData( { app, route } ) {
    try {
      let response = await app.$api.explorationsType.getExplorationTypeByIds([route.params.id])
      const explorationType = response.data[0] || {}

      response = await app.$api.explorationsType.getList({page: 1, numberPerPage: 1000})
      const explorationTypeList = response.data || {}

      response = await app.$api.documents.getList({
        page: 1,
        numberPerPage: 1000,
        parent_type: 'exploration',
        parent_ids: [explorationType.id]
      })
      explorationType.documents = response.data || []

      return { explorationType, explorationTypeList }
    } catch ( e ) {
      return 'error'
    }
  },
  data() {
    return {
      step: 1,
      openModal: false,
      explorationType: {},
      explorationTypeList : [],
      showVideoStartExploration: !this.$store.state.auth.user.config.sawExplorationVideoMs,
      videoStartExploration: {
        title: 'Vidéo de présentation des explorations',
        path: 'https://vimeo.com/470101215'
      }
    }
  },
  methods: {
    createExploration() {
      this.openModal = true
    },
    closeModal() {
      this.openModal = false
    },
    showVideoStartModal() {
      this.showVideoStartModule = true
    },
    async closeVideoStartModal() {
      const user = this.$store.state.auth.user
      user.config.sawExplorationVideoMs = true
      this.$store.commit('auth/setUser', user)
      await this.$api.user.updateUser(this.$store.state.auth.user)
      this.showVideoStartExploration = !this.$store.state.auth.user.config.sawExplorationVideoMs
    }
  }
}
</script>

<style scoped>
.back-btn {
  left: 0;
  transform: translateX(-120%);
}
.girl-activity {
  transform: translateY(-64%)translateX(-20%);
}
.boy-button {
  transform: translateX(-100%)translateY(-7px);
}
</style>

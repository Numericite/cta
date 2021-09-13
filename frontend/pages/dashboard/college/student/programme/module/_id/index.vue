<template>
  <div class="w-full flex flex-col pt-10 px-10 xl:px-0">
    <CollegeModal v-show="showVideoStartModule" :count="3" @handleClose="closeVideoStartModal">
      <VideoModal :video="videoStartModule"/>
    </CollegeModal>
    <div v-if="!openModal">
      <div class="flex ml-6 xl:ml-0 pt-16 pb-6 xl:pb-20 items-center justify-between w-full relative">
        <button class="absolute ml-3 xl:ml-0 back-btn rounded-full w-8 h-8 bg-ms-blue fill-current text-white flex items-center justify-center" @click="$router.go(-1)">
          <svg width="12" height="9" viewBox="0 0 12 9" fill="none" xmlns="http://www.w3.org/2000/svg" class="fill-current">
            <path d="M5.52344 6.89062L3.97656 5.34375H10.9375C11.2422 5.34375 11.5 5.10938 11.5 4.78125V4.21875C11.5 3.91406 11.2422 3.65625 10.9375 3.65625H3.97656L5.52344 2.13281C5.73438 1.92188 5.73438 1.54688 5.52344 1.33594L5.125 0.9375C4.91406 0.726562 4.53906 0.726562 4.32812 0.9375L1.16406 4.125C0.929688 4.33594 0.929688 4.6875 1.16406 4.89844L4.32812 8.08594C4.53906 8.29688 4.91406 8.29688 5.125 8.08594L5.52344 7.6875C5.73438 7.47656 5.73438 7.10156 5.52344 6.89062Z" fill="white"/>
          </svg>
        </button>
        <div class="flex justify-start items-center pl-2">
          <div class="dot mr-2 bg-ms-blue"/>
          <div class="ms-subtitle text-ms-blue">Séquences</div>
          <div class="dot ml-2 bg-ms-blue"/>
        </div>
        <img :src="'/img/middle-school/boy-button.png'" class="absolute pin-t pin-r w-32 boy-button">
        <button class="center-vertical button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue ml-4 p-4" @click="showVideoStartModule = true">
          <img src="~/assets/img/play.svg" class="h-3 w-4"> Revoir la vidéo de présentation
        </button>
      </div>
      <div class="ms-title pb-12">{{ module.name }}</div>
      <div class="flex items-center w-full justify-start pt-6 pb-4">
        <div class="dot mr-4 bg-black"/>
        <div class="ms-subtitle">Description de la séquence</div>
        <div class="dot ml-4 bg-black"/>
      </div>
      <div class="leading-normal text-justify pt-10 mb-16 text-ms-gray-dark">
        {{ module.description }}
      </div>
      <div v-if="module.help" class="flex justify-center w-full mb-16">
        <HelpMessage :helpMessage="{title: 'On est là pour t\'aider', message: module.help}"/>
      </div>
      <div v-if="activities.length > 0" class="flex items-center w-full justify-start mb-12 pb-4">
        <div class="dot mr-4 bg-black"/>
        <div class="ms-subtitle">{{ activities.length }} activité<span v-if="activities.length > 1">s</span> à compléter</div>
        <div class="dot ml-4 bg-black"/>
      </div>
      <ModuleActivityMs v-for="(activity, index) in activities" :displayIllustration="(index === 0)" :key="activity.id" :moduleActivity="activity" class="mb-8"/>
      <ModuleSummary :module="module" class="mb-8 rounded-lg" @endModule="endModule"/>
    </div>
    <ModuleModal v-if="openModal" :module="module" :moduleMaxNum="moduleMaxNum" @handleClose="closeModal"/>
  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import ModuleSummary from '../../../../../../../components/middleSchool/ModuleSummary'
import dashboard_ms from '../../../../../../../layouts/dashboard_ms_student'
import Button from '../../../../../../../mixins/button'
import ModuleActivityMs from '../../../../../../../components/middleSchool/ModuleActivityMs'
import HelpMessage from '~/components/middleSchool/HelpMessage'
import _ from 'lodash'
import ModuleModal from '../../../../../../../components/middleSchool/modals/ModuleModal'
import VideoModal from '../../../../../../../components/middleSchool/modals/VideoModal'
import CollegeModal from '../../../../../../../components/middleSchool/CollegeModal'

export default {
  name: 'Module',
  layout: 'dashboard_ms_student',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessMiddleSchoolStudentDashboard',
  components: { ModuleModal, ModuleActivityMs, Button, ModuleSummary, HelpMessage, CollegeModal, VideoModal },
  async asyncData( { app, route, store } ) {
    async function asyncForEach(array, callback) {
      for (let index = 0; index < array.length; index++) {
        await callback(array[index], index, array);
      }
    }

    try {
      let response = await app.$api.modules.getModuleByIds([route.params.id])
      const module = response.data[0] || {}

      response = await app.$api.fields.count({parent_id: module.id})
      module.hasForm = (response.data.res || 0) > 0

      response = await app.$api.modules.getMaxNum(store.state.auth.user.classroom.grade)
      const moduleMaxNum = (response.data || 0)

      response = await app.$api.modules.getProgress(store.state.auth.user.userID, module.id, store.state.auth.user.classroom.grade)
      module.isDone = (response.data[0] || {}).isDone
      module.doneDate = (response.data[0] || {}).doneDate

      response = await app.$api.modules.getModuleActivities({page: 1, numberPerPage: 1000, module_ids: [module.id]})
      const activities = response.data || []

      if (activities.length > 0) {
        response = await app.$api.documents.getList({
          page: 1,
          numberPerPage: 1000,
          parent_type: 'moduleActivity',
          parent_ids: _.map(activities, 'id')
        })
        const documents = response.data || []

        await asyncForEach(activities, async (activity) => {
          activity.documents = _.filter(documents, { 'parent_id': activity.id })

          response = await app.$api.fields.getList({
            parent_id: activity.id,
            page: 1,
            numberPerPage: 1000
          })
          const fields = response.data
          activity.hasForm = (fields.length || 0) > 0

          if (activity.hasForm) {
            response = await app.$api.fieldLogs.count({field_ids: _.map(fields, 'id'), user_ids: [store.state.auth.user.userID]})
            const countLogs = _.get(response.data, 'res', 0)
            activity.formDone = (countLogs > 0)
          }

          activity.nb_ressources = activity.documents.length + (activity.hasForm ? 1 : 0) + 1
        })

      }

      return { module, activities, moduleMaxNum }
    } catch ( e ) {
      console.log(e)
    }
  },
  data() {
    return {
      module: {},
      activities: [],
      openModal: false,
      moduleMaxNum: 0,
      showVideoStartModule: !this.$store.state.auth.user.config.sawModuleVideoMs,
      videoStartModule: {
        title: 'Vidéo de présentation des séquences',
        path: 'https://vimeo.com/470100158'
      }
    }
  },
  methods: {
    endModule() {
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
      user.config.sawModuleVideoMs = true
      this.$store.commit('auth/setUser', user)
      await this.$api.user.updateUser(this.$store.state.auth.user)
      this.showVideoStartModule = !this.$store.state.auth.user.config.sawModuleVideoMs
    }
  }
}
</script>

<style scoped>
.back-btn {
  left: 0;
  transform: translateX(-120%);
}
  .boy-button {
    transform: translateX(-100%)translateY(-7px);
  }
</style>

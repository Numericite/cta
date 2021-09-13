<template>
  <div class="pt-16 pb-12 px-5 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <CollegeModal v-show="showFilesTeacherVideoMs" :count="3" @handleClose="closeFilesTeacherVideoMs">
      <VideoModal :video="filesTeacherVideoMs"/>
    </CollegeModal>
    <div class="flex flex-row justify-between">
      <div class="text-6xl">Bonjour <span class="ms-title">{{ $store.state.auth.user.firstName }} !</span></div>
      <button class="center-vertical button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue ml-4 p-4" @click="showFilesTeacherVideoMs = true">
        <img src="~/assets/img/play.svg" class="h-3 w-4"> Revoir la vidéo de présentation
      </button>
    </div>
    <div class="flex items-center pt-11 pb-6">
      <div class="dot mr-4 bg-black"/>
      <div class="ms-subtitle">{{ documents.length }} {{ documents.length === 1 ? 'ressource' : 'ressources' }}</div>
      <div class="dot ml-4 bg-black"/>
    </div>
    <File v-for="document in documents" :key="document.id" :document="document" :kind="document.file_kind" class="my-4"/>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import File from '../../../../../components/middleSchool/File'
  import CollegeModal from '../../../../../components/middleSchool/CollegeModal'
  import VideoModal from '../../../../../components/middleSchool/modals/VideoModal'
  import _ from 'lodash'
  export default {
    name: 'Fiches',
    components: { File, CollegeModal, VideoModal },
    layout: 'dashboard_ms_manager',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolManagerDashboard',
    async asyncData( {app, store} ) {
      try {
        let response = await app.$api.schools.getSchoolsByIds([store.state.auth.user.config.school_id])
        const school = response.data[0] || {}

        response = await app.$api.schools.getClassroomsByIds([store.state.auth.user.config.classroom_ids])
        const classrooms = response.data || []

        const teacherGrades = _.map(classrooms, 'grade')

        return { school, teacherGrades }
      } catch (e) {
        console.log(e)
      }
    },
    data(){
      return {
        documents: [],
        school: {},
        teacherGrades: [],
        count: 0,
        pagination: {
          page: 1,
          numberPerPage: 1000,
          numberOfPages: 0
        },
        showFilesTeacherVideoMs: !this.$store.state.auth.user.config.sawFilesTeacherVideoMs,
        filesTeacherVideoMs: {
          title: 'Vidéo de présentation de l\'espace fiche',
          path: 'https://vimeo.com/473038066'
        }
      }
    },
    mounted() {
      this.getRows()
    },
    methods: {
      async getRows() {
        try {
          let response = await this.$api.documents.count({parent_type: this.$store.state.auth.user.config.accountType + '-ms', grades: this.teacherGrades})
          this.count = response.data.res || 0

          this.pagination.numberOfPages = Math.trunc((this.count + this.pagination.numberPerPage - 1) / this.pagination.numberPerPage)

          response = await this.$api.documents.getList({parent_type: this.$store.state.auth.user.config.accountType + '-ms', grades: this.teacherGrades, page: this.pagination.page, numberPerPage: this.pagination.numberPerPage})
          this.documents = response.data || []
        } catch (e) {
          console.log(e)
        }
      },
      async closeFilesTeacherVideoMs() {
        const user = this.$store.state.auth.user
        user.config.sawFilesTeacherVideoMs = true
        this.$store.commit('auth/setUser', user)
        await this.$api.user.updateUser(this.$store.state.auth.user)
        this.showFilesTeacherVideoMs = !this.$store.state.auth.user.config.sawFilesTeacherVideoMs
      }
    }
}
</script>

<style lang="scss" scoped>
</style>

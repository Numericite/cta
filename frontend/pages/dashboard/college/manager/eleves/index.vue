<template>
  <div class="pt-16 px-5 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <CollegeModal v-show="showOnboardingTeacherMs" :count="0" @handleClose="closeOnboardingTeacherMs">
      <VideoModal :video="onboardingTeacherMs"/>
    </CollegeModal>
    <div class="flex items-center justify-between">
      <div class="text-6xl">Bonjour <span class="ms-title">{{ $store.state.auth.user.firstName }} !</span></div>
      <button class="center-vertical button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue p-4" @click="showOnboardingTeacherMs = true">
        <img src="~/assets/img/play.svg" class="h-3 w-4"> Revoir la vidéo de présentation
      </button>
    </div>
    <div class="flex items-center pt-11 pb-6">
      <div class="dot mr-4 bg-black"/>
      <div class="ms-subtitle">{{ students.length }} {{ students.length === 1 ? 'élève' : 'élèves' }}</div>
      <div class="dot ml-4 bg-black"/>
    </div>
    <div class="flex flex-col xl:flex-row w-full justify-between items-center pb-10">
      <div class="flex flex-col xl:flex-row items-center">
        <div class="pb-2 xl:pb-0 ms-text text-ms-gray-light"> Classe : </div>
        <div v-if="classes.length > 1">
          <button :class="allClassFilter ? 'border-ms-green text-ms-green': 'text-ms-gray-light'" class="ms-text focus:outline-none mb-2 xl:mb-0 border-1 border-solid rounded-lg py-3 px-4 ml-4" @click="getAllStudents"> Toutes les classes</button>
          <button v-for="classe in classes" :class="classe.filter ? 'border-ms-blue text-ms-blue': 'text-ms-gray-light'" :key="classe.id" class="ms-text focus:outline-none mb-2 xl:mb-0 border-1 border-solid rounded-lg py-3 px-4 ml-4" @click="getStudentsbyClassroom(classe)"> {{ classe.name }}</button>
        </div>
        <div v-else>
          <button class="ms-text focus:outline-none mb-2 xl:mb-0 border-1 border-solid border-ms-green text-ms-green rounded-lg py-3 px-4 ml-4"> {{ classes[0].name }}</button>
        </div>
      </div>
      <FormulateInput v-model="fullName" :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-purple text-ms-gray-dark']" type="text" placeholder="Rechercher..." @change="fullName ? searchByFullName() : getTeacherRows()" />
    </div>
    <Student v-for="student in students" :key="student.id" :student="student" :accountType="'manager'" class="my-3"/>
    <div v-if="pagination.numberOfPages > 1" class="text-center my-12">
      <span class="text-blue font-extrabold text-4xl cursor-pointer mr-4" @click="changePage(pagination.page - 1)"><img src="~/assets/img/chevron-left.svg"></span>
      <span v-for="(page, index) in pagination.numberOfPages" :key="index"
            :class="{'ml-4': index !== 0, 'font-extrabold': page === pagination.page}"
            class="text-blue text-4xl cursor-pointer"
            @click="changePage(page)">{{ page }}</span>
      <span class="text-blue font-extrabold text-4xl cursor-pointer ml-4" @click="changePage(pagination.page + 1)"><img src="~/assets/img/chevron-right.svg"></span>
    </div>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import Button from '../../../../../mixins/button'
  import Student from '../../../../../components/middleSchool/Student'
  import CollegeModal from '../../../../../components/middleSchool/CollegeModal'
  import VideoModal from '../../../../../components/middleSchool/modals/VideoModal'
  export default {
    name: 'Eleves',
    components: { Student, Button, CollegeModal, VideoModal },
    layout: 'dashboard_ms_manager',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolManagerDashboard',
    data() {
      return {
        allClassFilter: true,
        isSearch: false,
        students: [],
        count: 0,
        fullName: null,
        pagination: {
          page: 1,
          numberPerPage: 12,
          numberOfPages: 0
        },
        showOnboardingTeacherMs: !this.$store.state.auth.user.config.sawOnboardingTeacherMs,
        onboardingTeacherMs: {
          title: 'Vidéo de présentation de l\'espace enseignant',
          path: 'https://vimeo.com/473040404',
        }
      }
    },
    async asyncData ({app, store}) {
      try {
        let response = await app.$api.schools.getSchoolsByIds([app.store.state.auth.user.config.school_id])
        const school = response.data[0] || {}

        response = await app.$api.schools.getClassroomsByIds(app.store.state.auth.user.config.classroom_ids)
        const classes = response.data

        return { school, classes }
      } catch (e) {
        console.log(e)
      }
    },
    mounted() {
      if(this.$store.state.auth.user.config.accountType !== 'teacher') {
        this.$router.push('/dashboard/college/student/parcours')
      }

      else {
        this.getTeacherRows()
      }
    },
    methods: {
      getAllStudents() {
        this.classes.forEach( item => item.filter = false )
        this.allClassFilter = true
        this.getTeacherRows()
      },
      resetSearch() {
        this.isSearch = false
        this.fullName = null
        this.getTeacherRows()
      },
      getStudentsbyClassroom( item ) {
        item.filter = !item.filter
        this.allClassFilter = true
        let that = this
        this.classes.forEach( room => {
          if ( room.filter )
            that.allClassFilter = false
        } )
        this.getTeacherRows()
      },
      changePage(page) {
        if (page > 0 && page <= this.pagination.numberOfPages) {
          this.pagination.page = page
          this.getTeacherRows()
        }
      },
      async getTeacherRows() {
        try {
          const classroom_ids = this.allClassFilter ? _.map(this.classes, 'id') : _.map(_.filter(this.classes, {filter: true}), 'id')

          let response = await this.$api.teachers.countStudents({classroom_ids: classroom_ids})
          this.count = response.data.count || 0

          this.pagination.numberOfPages = Math.trunc((this.count + this.pagination.numberPerPage - 1) / this.pagination.numberPerPage)

          response = await this.$api.teachers.getStudents({classroom_ids: classroom_ids, page: this.pagination.page, numberPerPage: this.pagination.numberPerPage})
          this.students = response.data.res || []

          this.students.forEach((student) => {
            student.classroom = _.find(this.classes, {id: student.config.classroom_id})
            student.school = this.school
          })
        } catch (e) {
          console.log(e)
        }
      },
      async searchByFullName() {
        try {
          const classroom_ids = this.allClassFilter ? _.map(this.classes, 'id') : _.map(_.filter(this.classes, {filter: true}), 'id')

          let response = await this.$api.teachers.getStudentsByFullName({fullName: this.fullName, classroom_ids: classroom_ids})
          this.students = response.data.result || []

          this.students.forEach((student) => {
            student.classroom = _.find(this.classes, {id: student.config.classroom_id})
            student.school = this.school
          })
          this.pagination.numberOfPages = 1
          this.isSearch = true
        } catch (e) {
          console.log(e)
        }
      },

      async closeOnboardingTeacherMs() {
        const user = this.$store.state.auth.user
        user.config.sawOnboardingTeacherMs = true
        this.$store.commit('auth/setUser', user)
        await this.$api.user.updateUser(this.$store.state.auth.user)
        this.showOnboardingTeacherMs = !this.$store.state.auth.user.config.sawOnboardingTeacherMs
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>

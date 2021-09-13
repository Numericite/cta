<template>
  <div class="pt-16 px-5 xl:px-0 xl:pt-32 flex flex-col">
    <div class="flex items-center justify-between">
      <div class="text-6xl">Bonjour <span class="ms-title">{{ $store.state.auth.user.firstName }} </span></div>
    </div>
    <div class="flex items-center pt-11 pb-6">
      <div class="dot mr-4 bg-black"/>
      <div class="ms-subtitle">{{ count }} {{ count === 1 ? 'élève' : 'élèves' }}</div>
      <div class="dot ml-4 bg-black"/>
    </div>
    <FiltersPartner :account="partner" :schools="schools" :gradesAll="gradesAll" :typePage="'eleves'" @getDataFilters="getPartnerStudents" />
    <Student v-for="student in students" :key="student.id" :student="student" :accountType="'partner'" class="my-3"/>
    <div v-if="pagination.numberOfPages > 1" class="flex items-center justify-center mt-12">
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
import Student from '../../../../../components/middleSchool/Student'
import FiltersPartner from '../../../../../components/middleSchool/FiltersPartner'
import _ from 'lodash'

export default {
  layout: 'dashboard_ms_partner',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessMiddleSchoolPartnerDashboard',
  components: { Student, FiltersPartner },
  data() {
    return {
      students: [],
      classes: [],
      count: 0,
      dataFilters: {},
      pagination: {
        page: 1,
        numberPerPage: 30,
        numberOfPages: 0
      },
    }
  },
  mounted() {
    if(this.$store.state.auth.user.config.accountType !== 'partner') {
      this.$router.push('/dashboard/college/student/parcours')
    }
  },
  async asyncData({app}) {
    try {
      let response = await app.$api.partners.getPartnersByIds([app.store.state.auth.user.config.partner_id])
      const partner = response.data[0] || {}

      partner.accountType = app.store.state.auth.user.config.accountType

      response = await app.$api.schools.getSchoolsByIds([partner.school_ids])
      const schools = response.data || []

      response = await app.$api.schools.getGradesBySchools([partner.school_ids])
      const gradesAll = response.data.sort() || []

      return { partner, schools, gradesAll }
    } catch (e) {
      console.log(e)
    }
  },
  methods: {

    changePage(page) {
      if (page > 0 && page <= this.pagination.numberOfPages) {
        this.pagination.page = page
        this.getPartnerStudents(this.dataFilters)
      }
    },

    async getPartnerStudents(dataFilters) {
      try {
        this.dataFilters = dataFilters

        if (!this.dataFilters.full_name) {

          let response = await this.$api.user.countStudents({school_ids: this.dataFilters.school_ids, grade: this.dataFilters.grade, classroom_id: this.dataFilters.class_id })
          this.count = response.data.count || 0

          this.pagination.numberOfPages = Math.trunc((this.count + this.pagination.numberPerPage - 1) / this.pagination.numberPerPage)

          response = await this.$api.user.getStudents({school_ids: this.dataFilters.school_ids, grade: this.dataFilters.grade, classroom_id: this.dataFilters.class_id, page: this.pagination.page, numberPerPage: this.pagination.numberPerPage})
          this.students = response.data || []

        } else {

          this.count = 1000
          this.pagination.numberOfPages = 1

          let response = await this.$api.user.searchByFullName({school_ids: this.dataFilters.school_ids, grade: this.dataFilters.grade, classroom_id: this.dataFilters.class_id, full_name: this.dataFilters.full_name})
          this.students = response.data.result || []

        }

        this.students.forEach((student) => {
          student.classroom = _.find(this.dataFilters.classes, {id: student.config.classroom_id})
          student.school = _.find(this.schools, {id: student.config.school_id })
        })


      } catch (e) {
        console.log(e)
      }
    },
  },
}

</script>

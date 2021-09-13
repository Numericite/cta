<template>
  <div class="pt-16 px-5 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <div class="text-6xl">Bonjour <span class="ms-title">{{ $store.state.auth.user.firstName }}</span></div>
    <div class="flex items-center pt-11 pb-6">
      <div class="dot mr-4 bg-black"/>
      <div class="ms-subtitle">{{ teachers.length }} {{ teachers.length === 1 ? 'professeur' : 'professeurs' }}</div>
      <div class="dot ml-4 bg-black"/>
    </div>
    <FiltersPartner :account="account" :schools="schools" :gradesAll="gradesAll" :typePage="'eleves'" @getDataFilters="getDataTeachers" />
    <Teacher v-for="teacher in teachers" :key="teacher.userID" :teacher="teacher" accountType="operator" class="my-3"/>
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
  import Teacher from '../../../../../components/middleSchool/Teacher'
  import FiltersPartner from '../../../../../components/middleSchool/FiltersPartner'
  import _ from 'lodash'
  export default {
    name: 'Professeurs',
    components: { Teacher, Button, FiltersPartner },
    layout: 'dashboard_ms_operator',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolOperatorDashboard',
    data() {
      return {
        allClassFilter: true,
        isSearch: false,
        teachers: [],
        count: 0,
        fullName: null,
        pagination: {
          page: 1,
          numberPerPage: 12,
          numberOfPages: 0
        },
      }
    },
    async asyncData ({app, store}) {
      try {

        let account = {
          accountType: app.store.state.auth.user.config.accountType,
          school_ids: app.store.state.auth.user.config.school_ids
        }

        let response = await app.$api.schools.getSchoolsByIds([account.school_ids])
        const schools = response.data || {}

        response = await app.$api.schools.getGradesBySchools([account.school_ids])
        const gradesAll = response.data

        return { account, schools, gradesAll }
      } catch (e) {
        console.log(e)
      }
    },
    methods: {
      changePage(page) {
        if (page > 0 && page <= this.pagination.numberOfPages) {
          this.pagination.page = page
          this.getDataTeachers(this.dataFilters)
        }
      },

      async getDataTeachers(dataFilters) {
        try {
          this.dataFilters = dataFilters

          if (!this.dataFilters.full_name) {

            let response = await this.$api.teachers.count({ 
              school_ids: this.dataFilters.school_ids, 
              grade: this.dataFilters.grade, 
              classroom_id: this.dataFilters.class_id 
            })
            this.count = response.data.count || 0

            this.pagination.numberOfPages = Math.trunc((this.count + this.pagination.numberPerPage - 1) / this.pagination.numberPerPage)

            response = await this.$api.teachers.getTeachers({
              page: this.pagination.page, 
              numberPerPage: this.pagination.numberPerPage,
              school_ids: this.dataFilters.school_ids,
              grade: this.dataFilters.grade, 
              classroom_id: this.dataFilters.class_id,
            })
            this.teachers = response.data.res || []

          } else {

            this.count = 1000
            this.pagination.numberOfPages = 1

            let response = await this.$api.teachers.searchByFullName({fullName: this.dataFilters.full_name})
            this.teachers = response.data.result || []

          }

          this.teachers.forEach((teacher) => {
            teacher.school = _.find(this.schools, {id: teacher.config.school_id })
          })

        } catch (e) {
          console.log(e)
        }
      },
    }
  }
</script>
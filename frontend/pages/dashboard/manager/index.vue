<template>
  <div class="dashboard flexContainer-dash bg-blue-lightest ml-32 sm:ml-0 relative">

    <picture class="">
      <source srcset="~/assets/img/dash-bg-1.webp"
              type="image/webp">
      <img src="~/assets/img/dash-bg-1.png"
           class="absolute pin-t pin-r z-0 sm:hidden"
           alt="Illustration d'arrière plan">
    </picture>

    <div class=" pt-16 z-10 relative sm:pt-8">
      <div class="text-6xl sm:text-5xl text-blue font-bold font-proza">
        <span v-if="isTeacher" v-html="textsTeacher[0]" />
        <span v-else-if="$store.state.auth.user.config.accountType === 'parent'" v-html="textsParent[0]" />
        <span v-else-if="$store.state.auth.user.config.accountType === 'mentor'" v-html="textsMentor[0]" />
      </div>
      <div class="desc text-blue text-3xl sm:text-xl font-normal mt-12 sm:mt-4">
        <span v-if="isTeacher" v-html="textsTeacher[1]" />
        <span v-else-if="$store.state.auth.user.config.accountType === 'parent'" v-html="textsParent[1]" />
        <span v-else-if="$store.state.auth.user.config.accountType === 'mentor'" v-html="textsMentor[1]" />
      </div>
    </div>

    <div v-if="isTeacher"
         class="mt-12 z-10 relative sm:mt-0">
      <div class="flex flex-no-wrap items-center mb-12">
        <span class="text-blue font-bold text-3xl mr-8 sm:mr-0 whitespace-no-wrap sm:text-xl">
          Classes :
        </span>
        <div class="classFilters flex flex-no-wrap sm:flex-wrap sm:pt-2">
          <div v-for="( item, key ) in classes"
               :key="key"
               :class="item.filter ? 'border-dark-peach text-orange bg-light-peach' : 'border-blue-lighter text-blue-lighter bg-transparent'"
               class="px-3 py-2 mx-2 border-2 font-bold text-2xl sm:text-sm sm:px-2 sm:py-1 sm:my-2 border-solid rounded-4 cursor-pointer"
               @click="setFilter(item)">
            {{ item.name }}
          </div>
          <div :class="allClassFilter ? 'border-dark-peach text-orange bg-light-peach' : 'border-blue-lighter text-blue-lighter bg-transparent cursor-pointer'"
               class="px-3 py-2 mx-2 border-2 font-bold text-2xl sm:text-sm sm:px-2 sm:py-1 sm:my-2 border-solid rounded-4"
               @click="resetFilterByClass">
            TOUS
          </div>
        </div>
      </div>
      <div>
        <span class="text-blue font-bold text-3xl mr-8">
          Recherche par élèves :
        </span>
        <div class="flex flex-no-wrap sm:flex-wrap items-center justify-between w-full mt-6">
          <div :class="{'field--active': fullName && fullName.length > 0 }"
               class="field w-1/2 sm:w-full sm:ml-0 sm:mb-8 relative">
            <label for="search"
                   class="pointer-events-none">Rechercher</label>
            <input id="search"
                   v-model="fullName"
                   name="search"
                   class="w-full">
            <button v-if="isSearch" class="absolute position-reset-search" @click="resetSearch">
              <svg version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   xmlns:xlink="http://www.w3.org/1999/xlink"
                   width="20"
                   height="20"
                   viewBox="0 0 20 20">
                <path fill="#ff8f5e"
                      d="M10.707 10.5l5.646-5.646c0.195-0.195 0.195-0.512 0-0.707s-0.512-0.195-0.707 0l-5.646 5.646-5.646-5.646c-0.195-0.195-0.512-0.195-0.707 0s-0.195 0.512 0 0.707l5.646 5.646-5.646 5.646c-0.195 0.195-0.195 0.512 0 0.707 0.098 0.098 0.226 0.146 0.354 0.146s0.256-0.049 0.354-0.146l5.646-5.646 5.646 5.646c0.098 0.098 0.226 0.146 0.354 0.146s0.256-0.049 0.354-0.146c0.195-0.195 0.195-0.512 0-0.707l-5.646-5.646z" />
              </svg>
            </button>
          </div>
          <div class="">
            <button :disabled="!fullName"
                    class="button button--orange sm:px-4 sm:py-3 sm:text-base"
                    @click="searchByFullName">
              Je recherche
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="students && students.length" class="mt-8 z-10 relative">
      <eleve-card v-for="(eleve, key) in students"
                  :key="key"
                  :eleve="eleve"
                  :domains="domains"
                  :streams="streams"
                  :actionVerbs="actionVerbs"
                  :activities="activities"
                  :userLogs="userLogs"
                  class="my-5" />
    </div>

    <div v-if="pagination.numberOfPages > 1" class="text-center mt-12">
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
import EleveCard from '~/components/dashboard/EleveCard'
import heads from '~/config/meta.json'
import _ from 'lodash'

export default {
  name: 'ManagerDashboard',
  layout: 'dashboard_manager',
  head() {
    if ( heads[this.$options.name] ) return heads[this.$options.name]
    return {
      title: 'Crée ton avenir - Dashboard manager'
    }
  },
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolManagerDashboard',
  components: { EleveCard },
  async asyncData ({app, store}) {
    try {
      let response = await app.$api.schools.getSchoolsByIds([app.store.state.auth.user.config.school_id])
      const school = response.data[0] || {}

      response = await app.$api.schools.getClassroomsByIds(app.store.state.auth.user.config.classroom_ids)
      const classes = response.data

      response = await app.$api.activities.getActivities()
      const activities = response.data

      response = await app.$api.domains.getList({page: 1, numberPerPage: 1000, kind: 'activity-area'})
      const domains = response.data || []

      response = await app.$api.domains.getList({page: 1, numberPerPage: 1000, kind: 'stream'})
      const streams = response.data || []

      response = await app.$api.domains.getList({page: 1, numberPerPage: 1000, kind: 'action-verb'})
      const actionVerbs = response.data || []

      return { school, classes, activities, domains, streams, actionVerbs }
    } catch (e) {
      console.log(e)
    }
  },
  data() {
    return {
      activities: null,
      userLogs: null,
      allClassFilter: true,
      students: [],
      count: 0,
      pagination: {
        page: 1,
        numberPerPage: 5,
        numberOfPages: 0
      },
      fullName: null,
      isSearch: false,
      isTeacher: null,
      textsTeacher: this.getPageTexts('dashboard-teacher'),
      textsParent: this.getPageTexts('dashboard-parent'),
      textsMentor: this.getPageTexts('dashboard-mentor')
    }
  },
  mounted() {
    this.isTeacher = (this.$store.state.auth.user.config.accountType === 'teacher')
    if (this.isTeacher) {
      this.getTeacherRows()
    } else {
      this.getParentRows()
    }
  },
  methods: {
    changePage(page) {
      if (page > 0 && page <= this.pagination.numberOfPages) {
        this.pagination.page = page
        this.getTeacherRows()
      }
    },
    resetFilterByClass() {
      this.classes.forEach( item => item.filter = false )
      this.allClassFilter = true
      this.getTeacherRows()
    },
    setFilter( item ) {
      item.filter = !item.filter
      this.allClassFilter = true
      let that = this
      this.classes.forEach( room => {
        if ( room.filter )
          that.allClassFilter = false
      } )
      this.getTeacherRows()
    },

    resetSearch() {
      this.isSearch = false
      this.fullName = null
      this.getTeacherRows()
    },

    async searchByFullName() {
      try {
        const classroom_ids = this.allClassFilter ? _.map(this.classes, 'id') : _.map(_.filter(this.classes, {filter: true}), 'id')

        let response = await this.$api.teachers.getStudentsByFullName({fullName: this.fullName, classroom_ids: classroom_ids})
        this.students = response.data.result || []

        this.students.forEach((student) => {
          student.classroom = _.find(this.classes, {id: student.config.classroom_id})
          student.school = this.school
          this.setUserLogs(student, this.activities)
          this.setUserCourseId(student)
        })
        this.pagination.numberOfPages = 1
        this.isSearch = true
      } catch (e) {
        console.log(e)
      }
    },

    async setUserCourseId(user) {
      const course_id = user.classroom.course_id || user.school.course_id
      let response = await this.$api.courses.getByIds([course_id])
      user.course = response.data[0] || {}
    },

    async setUserLogs(user, activities) {
      let response = await this.$api.activities.getUserAdvancedLogs(user.userID)
      const logs = response.data.logs || []
      const userSelections = response.data.selections || []
      const userChoices = response.data.choices || []

      logs.forEach(function(log) {
        const activity = activities.find(function(activity) {
          return activity.id === log.activity_id
        })
        if (activity) {
          log.num = activity.num
          log.name = activity.name
          return log
        } else if (log.activity_id === 'final') {
          log.num = 8
          log.name = 'Mes premières pistes'
        }
      })

      logs.sort(function (a, b) {
        const valueA = a.num;
        const valueB = b.num;

        let comparison = 0;
        if (valueA > valueB) {
          comparison = 1;
        } else if (valueA < valueB) {
          comparison = -1;
        }
        return comparison;
      })

      user.autoPortrait = {
        logs: [
          _.find(logs, {num: 1}),
          _.find(logs, {num: 2}),
          _.find(logs, {num: 3}),
          _.find(logs, {num: 4}),
          _.find(logs, {num: 5}),
          _.find(logs, {num: 6}),
          _.find(logs, {num: 7}),
          _.find(logs, {activity_id: 'final'})
        ],
        userSelections: userSelections,
        userChoices: userChoices
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
          this.setUserLogs(student, this.activities)
          this.setUserCourseId(student)
        })
      } catch (e) {
        console.log(e)
      }
    },

    async getParentRows() {
      try {
        this.pagination.numberOfPages = 1

        let response

        if (this.$store.state.auth.user.config.accountType === 'parent') {
          response = await this.$api.parents.getStudents({
            parent_id: this.$store.state.auth.user.userID,
            page: 1,
            numberPerPage: 1000
          })
        } else {
          response = await this.$api.mentors.getStudents({
            mentor_id: this.$store.state.auth.user.userID,
            page: 1,
            numberPerPage: 1000
          })
        }
        this.students = response.data.res || []
        this.count = this.students.length

        response = await this.$api.schools.getClassroomsByIds(_.map(this.students, 'config.classroom_id'))
        const classes = response.data || []

        this.students.forEach((student) => {
          student.classroom = _.find(classes, {id: student.config.classroom_id})
          student.school = this.school
          this.setUserLogs(student, this.activities)
          this.setUserCourseId(student)
        })
      } catch (e) {
        console.log(e)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  width: calc(100vw - 9.1rem);
  @screen sm {
    width: 100vw;
  }
  .field {
    @apply mt-0;
  }
  .classFilters {
    @screen sm {
      transform: translateY(15px);
    }
  }

  .position-reset-search {
    bottom: 2px;
    right: 0;
  }
  // .button {
  //   @screen sm {
  //     transform: scale(0.9);
  //   }
  // }
}
</style>

<template>
  <div class="flexContainer-dash min-h-screen dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue">{{ texts[0] }}</h3>
    <div class="desc text-blue text-3xl sm:text-xl w-5/6 font-normal mt-4">
      <span v-if="!$store.state.auth.user.config.code_uais" ><span v-html="texts[1]" /></span>
      <span v-if="$store.state.auth.user.config.code_uais"><span v-html="texts[2]" /></span>
    </div>
    <div v-if="!$store.state.auth.user.config.code_uais" class="mt-8 mb-8 flex italic">
      <span class="flex">
        <div class="w-4 h-4 rounded-full flex bg-green"/>
        <span class="flex ml-2">
          <span v-html="texts[3]" />
        </span>
      </span>
      <span class="flex ml-6">
        <div class="w-4 h-4 rounded-full flex bg-orange"/>
        <span class="flex ml-2">
          <span v-html="texts[4]" />
        </span>
      </span>
      <span class="flex ml-6">
        <div class="w-4 h-4 rounded-full flex bg-red"/>
        <span class="flex ml-2">
          <span v-html="texts[5]" />
        </span>
      </span>
    </div>
    <rex-school :schools="schools" class="mt-8" @schoolsValidated="schoolsValidated"/>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import RexSchool from '~/components/dashboard/RexSchool'
  import _ from 'lodash'

  export default {
    name: 'ExplorationRex',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    scrollToTop: true,
    components: { RexSchool },
    async asyncData({app, store, error}) {
      try {
          const chapters = store.state.auth.user.course.chapters
          let schoolsChapter = _.find(chapters, { slug: 'school' })
          if (!(_.get(_.find(schoolsChapter.children, { slug: 'exploration' }), 'display', false))) {
            return error({ statusCode: 404, message: 'Post not found' })
          }

          let school_response = await app.$api.onisepFeedback.get({page: 1, numberPerPage: 1000, user_ids: [store.state.auth.user.userID]})
          const schools = _.filter(school_response.data || [], function(school) {
            return _.includes(store.state.auth.user.config.introspection_school_uais, school.code_uai)
          })

        return { schools }
      } catch (e) {
        error({statusCode: 404, message: 'Post not found' })
        console.log(e)
      }
    },
    data() {
      return {
        texts: this.getPageTexts(this.$route.name),
        schools: []
      }
    },
    methods: {
      schoolsValidated() {
        this.$forceUpdate()
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>

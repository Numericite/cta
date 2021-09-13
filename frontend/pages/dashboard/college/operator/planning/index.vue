<template>
  <div class="pt-24 flex flex-col px-10 xl:px-0">
    <Planning :eventUser="$store.state.auth.user" :teachers="teachers" />
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import Planning from '~/components/middleSchool/Planning'

  export default {
    name: 'CollegeOperatorDashboardPlanning',
    layout: 'dashboard_ms_operator',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolOperatorDashboard',
    components: { Planning },
    async asyncData({ app, route, store }) {
      try {
        let response = await app.$api.teachers.getList({ page: 1, numberPerPage: 100, school_ids: app.store.state.auth.user.config.school_ids })
        let teachers = response.data.res || []
        teachers = teachers.filter(teacher => teacher.userID !== store.state.auth.user.userID)
        teachers.map(teacher => {
          teacher.value = teacher.userID
          teacher.label = `${teacher.firstName} ${teacher.lastName}`
        })
        return { teachers }
      } catch (e) {
        return 'error'
      }
    },
  }
</script>
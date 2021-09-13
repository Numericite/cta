<template>
  <div class="pt-24 flex flex-col px-10 xl:px-0">
    <Planning :eventUser="$store.state.auth.user" :teachers="teachers" />
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import Planning from '~/components/middleSchool/Planning'

  export default {
    name: 'CollegeManagerDashboardPlanning',
    layout: 'dashboard_ms_manager',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolManagerDashboard',
    components: { Planning },
    async asyncData({ app, route, store }) {
      try {
        let response = await app.$api.teachers.getList({ page: 1, numberPerPage: 100 })
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
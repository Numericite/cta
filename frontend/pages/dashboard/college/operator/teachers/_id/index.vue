<template>
  <div class="pt-24 mx-0 flex flex-col px-10 xl:px-0">
    <div class="flex ml-6 xl:ml-0 pb-6 xl:pb-12 items-center w-full relative">
      <button class="absolute ml-3 xl:ml-0 back-btn rounded-full w-8 h-8 bg-ms-blue-dark fill-current text-white flex items-center justify-center focus:outline-none" @click="$router.back()">
        <svg width="12" height="9" viewBox="0 0 12 9" fill="none" xmlns="http://www.w3.org/2000/svg" class="fill-current">
          <path d="M5.52344 6.89062L3.97656 5.34375H10.9375C11.2422 5.34375 11.5 5.10938 11.5 4.78125V4.21875C11.5 3.91406 11.2422 3.65625 10.9375 3.65625H3.97656L5.52344 2.13281C5.73438 1.92188 5.73438 1.54688 5.52344 1.33594L5.125 0.9375C4.91406 0.726562 4.53906 0.726562 4.32812 0.9375L1.16406 4.125C0.929688 4.33594 0.929688 4.6875 1.16406 4.89844L4.32812 8.08594C4.53906 8.29688 4.91406 8.29688 5.125 8.08594L5.52344 7.6875C5.73438 7.47656 5.73438 7.10156 5.52344 6.89062Z" fill="white"/>
        </svg>
      </button>
      <div class="flex justify-start items-center pl-2">
        <div class="dot mr-2 bg-ms-blue-dark"/>
        <div class="ms-subtitle text-ms-blue-dark">SUIVI</div>
        <div class="dot ml-2 bg-ms-blue-dark"/>
      </div>
    </div>
    <div class="text-6xl pb-6"> 
      Planning de <span class="ms-title">{{ `${user.firstName} ${user.lastName}` }}</span>
    </div>
    <Planning :eventUser="user" :teachers="teachers" :monitoring="true" />
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import Planning from '~/components/middleSchool/Planning'

  export default {
    name: 'CollegeOperatorDashboardPlanningTeacher',
    layout: 'dashboard_ms_operator',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolOperatorDashboard',
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

        response = await app.$api.user.getUserInfos(route.params.id)
        let user = response.data[0]

        return { teachers, user }
      } catch (e) {
        return 'error'
      }
    },
  }
</script>

<style scoped>

.back-btn {
  left: 0;
  transform: translateX(-120%);
}

</style>
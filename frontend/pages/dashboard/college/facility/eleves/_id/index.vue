<template>
  <div class="pt-16 px-10 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <div class="flex ml-6 xl:ml-0 pb-6 xl:pb-12 items-center w-full relative">
      <button class="absolute ml-3 xl:ml-0 back-btn rounded-full w-8 h-8 bg-ms-red fill-current text-white flex items-center justify-center" @click="$router.push('/dashboard/college/facility/eleves')">
        <svg width="12" height="9" viewBox="0 0 12 9" fill="none" xmlns="http://www.w3.org/2000/svg" class="fill-current">
          <path d="M5.52344 6.89062L3.97656 5.34375H10.9375C11.2422 5.34375 11.5 5.10938 11.5 4.78125V4.21875C11.5 3.91406 11.2422 3.65625 10.9375 3.65625H3.97656L5.52344 2.13281C5.73438 1.92188 5.73438 1.54688 5.52344 1.33594L5.125 0.9375C4.91406 0.726562 4.53906 0.726562 4.32812 0.9375L1.16406 4.125C0.929688 4.33594 0.929688 4.6875 1.16406 4.89844L4.32812 8.08594C4.53906 8.29688 4.91406 8.29688 5.125 8.08594L5.52344 7.6875C5.73438 7.47656 5.73438 7.10156 5.52344 6.89062Z" fill="white"/>
        </svg>
      </button>
      <div class="flex justify-start items-center pl-2">
        <div class="dot mr-2 bg-ms-red"/>
        <div class="ms-subtitle text-ms-red">Mes élèves</div>
        <div class="dot ml-2 bg-ms-red"/>
      </div>
    </div>
    <div class="ms-title">Résultats de {{ student.firstName }} {{ student.lastName }}</div>
    <StudentResults :user_id="$route.params.id" :fullname="student.firstName + ' ' + student.lastName" />
  </div>
</template>

<script>
import StudentResults from '../../../../../../components/middleSchool/StudentResults'

export default {
  name: 'DataStudentDetails',
  layout: 'dashboard_ms_facility',
  components: { StudentResults },
  async asyncData({app, route}) {
    try {
      let response = await app.$api.user.getUserInfos([route.params.id])
      const student = response.data[0] || {}

      return { student }
    } catch (e) {
      console.log(e)
    }
  }
}
</script>

<style scoped>

.back-btn {
  left: 0;
  transform: translateX(-120%);
}

</style>

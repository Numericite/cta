<template>
  <div class="pt-16 px-10 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <div class="ms-title">Observe tes résultats</div>
    <div class="pt-5 pb-11 ms-subtitle-small text-justify text-ms-gray-dark md:max-w-5/6 xl:max-w-2/3">
      Ici tu retrouveras une vue d'ensemble sur tous tes résultats d'activités et d'actions d’exploration.
    </div>
    <StudentResults :user_id="$store.state.auth.user.userID" />
  </div>
</template>

<script>
  import StudentResults from '../../../../../components/middleSchool/StudentResults'
  export default {
    name: 'Resultats',
    layout: 'dashboard_ms_student',
    components: { StudentResults }
  }
</script>

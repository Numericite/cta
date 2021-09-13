<template>
  <div class="w-full flex justify-center">
    <CollegeModal :count="3" class="flex w-full justify-center" @handleClose="$router.push('/dashboard/college/student/parcours/')">
      <div class="flex flex-col h-5/6 justify-center items-center content-center px-12">
        <img src="~/static/img/middle-school/illustration-happy-xl.png" class="w-1/3">
        <div class="text-center flex flex-col text-6xl pt-8"><span class="ms-title pb-2">Bravo !</span> Tu as fini de programmer ton exploration !</div>
        <div class="xl:max-w-1/2 text-center pt-10 text-ms-gray-dark leading-normal">
          Tu peux à présent continuer ton parcours, ou aller consulter tes résultats dans la rubrique résultats.
        </div>
        <button class="button-outline-ms border-ms-blue text-ms-blue mt-6" @click="$router.push('/dashboard/college/student/parcours/')">Retourner au parcours</button>
      </div>
    </CollegeModal>
  </div>
</template>

<script>

import CollegeModal from '../CollegeModal'

export default {
  name: "ExplorationCompleteModal",
  components: { CollegeModal },
}

</script>

<style scoped>

</style>

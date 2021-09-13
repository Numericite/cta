<template>
  <div class="w-full flex justify-center">
    <CollegeModal :count="3" class="flex w-full justify-center" @handleClose="$router.push('/dashboard/college/student/parcours/')">
      <div v-if="module.num !== moduleMaxNum" class="flex flex-col h-5/6 justify-center items-center content-center">
        <img src="~/static/img/middle-school/illustration-happy-xl.png" class="w-1/3">
        <div class="text-center flex flex-col text-6xl pt-8"><span class="ms-title pb-2">Bravo !</span> Tu as fini une séquence !</div>
        <div class="xl:max-w-1/2 text-center pt-10 text-ms-gray-dark leading-normal">
          Tu peux à présent continuer ton parcours, ou aller consulter tes résultats dans la rubrique résultats.
        </div>
        <button class="button-outline-ms border-ms-blue text-ms-blue mt-6" @click="$router.push('/dashboard/college/student/parcours/')">Retourner au parcours</button>
      </div>
      <div v-else class="flex flex-col h-5/6 justify-center items-center content-center">
        <img src="~/static/img/middle-school/illustration-happy.png" class="w-1/3">
        <div class="text-center flex flex-col text-6xl pt-8"><span class="ms-title pb-2">Bravo !</span> Tu as fini toutes tes séquences !</div>
        <div class="xl:max-w-1/2 text-center pt-10 text-ms-gray-dark leading-normal">
          Maintenant, dernière étape, fais ton choix de fin pour la 3eme
        </div>
        <button class="button-outline-ms border-ms-blue text-ms-blue mt-6" @click="$router.push('/dashboard/college/student/parcours/')">Retourner au parcours</button>
      </div>
    </CollegeModal>
  </div>
</template>

<script>

import CollegeModal from '../CollegeModal'

export default {
  name: "ModuleModal",
  components: { CollegeModal },
  props: {
    module: {
      type: Object,
      default: function() {
        return {}
      }
    },
    moduleMaxNum: {
      type: Number,
      default: function() {
        return 0
      }
    }
  },
}

</script>

<style scoped>

</style>

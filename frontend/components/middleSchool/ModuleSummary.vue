<template>
  <div class="entity w-full bg-white z-10">
    <div class="bg-white shadow-lg p-3 rounded-lg mt-48">
      <div class="min-w-100 border-ms-blue border-opacity-50 bg-white min-h-100 border-solid border-1 rounded-t-lg relative">
        <div class="bg-white w-64 h-6 absolute bloc-white"/>
        <div class="dot-left bg-ms-blue absolute rounded-full"/>
        <img :src="'/img/middle-school/step1.png'" class="absolute illustration -mt-2 w-64 px-4">
        <div class="dot-right bg-ms-blue absolute rounded-full"/>
        <div class="pb-2 pt-24 w-full">
          <div class="flex xl:flex-row flex-col justify-center items-center w-full px-6">
            <div class="flex flex-col justify-center xl:w-full pt-12 mb-5">
              <div class="text-3xl font-bold text-center">
                <span v-if="!module.isDone && module.hasForm">Réalise la synthèse</span>
                <span v-if="module.isDone && module.hasForm">Modifie la synthèse</span>
                <span v-if="!module.hasForm">Termine la séquence</span>
              </div>
              <p class="pt-3 text-center text-xl text-ms-gray-dark leading-loose break-all">
                <span v-if="!module.isDone && module.hasForm">Une fois que tu as terminé tes activités, clique sur le bouton en bas pour réaliser la synthèse.</span>
                <span v-if="module.isDone && module.hasForm">Clique sur le bouton en bas pour modifier la synthèse.</span>
                <span v-if="!module.hasForm">Une fois que tu as terminé tes activités, clique sur le bouton en bas pour terminer la séquence.</span>
              </p>
              <div v-if="module.isDone" class="flex justify-center items-center">
                <div class="ms-subtitle text-ms-blue">Finalisée le {{ formatDate(module.doneDate) }}</div>
              </div>
              <div v-if="module.isDone && module.hasForm" class="flex flex-col items-center pt-6">
                <button class="button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue hover:text-white max-w-4/5 xl:max-w-2/5"
                        @click="$router.push('/dashboard/college/student/formulaire/' + module.id)">
                  <img src="~/assets/img/check-white.svg" class="w-4 h-4">
                  Modifier la synthèse
                </button>
              </div>
              <div v-if="!module.isDone" class="flex flex-col items-center">
                <button v-if="module.hasForm" class="button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue hover:text-white max-w-4/5 xl:max-w-2/5"
                        @click="$router.push('/dashboard/college/student/formulaire/' + module.id)">
                  <img src="~/assets/img/check-white.svg" class="w-4 h-4">
                  Réaliser la synthèse
                </button>
                <button v-else class="button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue hover:text-white max-w-4/5 xl:max-w-2/5"
                        @click="createFakeLog()">
                  <img src="~/assets/img/check-white.svg" class="w-4 h-4">
                  Terminer la séquence
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProgressBar from '../middleSchool/ProgressBar'
import Button from '../../mixins/button'
export default {
  name: 'ModuleMs',
  components: { Button, ProgressBar },
  props: {
    module: {
      type: Object,
      default: function() {
        return {}
      }
    },
  },
  data() {
    return {
    }
  },
  methods: {
    async createFakeLog() {
      const log = {
        field_id: 'fake_log',
        child_type: 'module',
        child_id: this.module.id,
        user_id: this.$store.state.auth.user.userID,
        sort_id: 0,
        values: []
      }

      await this.$api.fieldLogs.create(log)

      this.endModule()
    },
    endModule() {
      this.$emit('endModule')
    }
  }
}
</script>

<style lang="scss" scoped>
.illustration, .bloc-white {
  left: 50%;
  transform: translate(-50%, -50%);
}
.dot-left {
  left: 39.5%;
  transform: translate(-50%, -50%);
  width: 3px;
  height: 3px;
}
.dot-right {
  right: 39%;
  transform: translate(-50%, -50%);
  width: 3px;
  height: 3px;
}
</style>

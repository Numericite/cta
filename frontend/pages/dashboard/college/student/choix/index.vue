<template>
  <div class="pt-16 px-5 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <CollegeModal v-show="showVideoStartChoice" :count="1" @handleClose="closeVideoStartModal">
      <VideoModal :video="videoStartChoice"/>
    </CollegeModal>
    <div class="ms-title">
      Consulte tes pistes d'orientation
    </div>
    <div class="pt-5 pb-6 ms-subtitle-small text-justify text-ms-gray-dark md:max-w-5/6 xl:max-w-2/3">
      Ici tu retrouveras l'ensemble des choix que tu as émis depuis le début. Tu pourras y revenir pour compléter ta réflexion et vérifier que ces choix te correspondent bien.
    </div>
    <div class="pb-5 mt-8 relative">
      <img :src="'/img/middle-school/boy-button.png'" class="absolute pin-l pin-r w-32 boy-button">
      <button class="center-vertical button-ms text-white bg-ms-green border-solid border-1 border-ms-green hover:bg-ms-green p-4" @click="showVideoStartChoice = true">
        <img src="~/assets/img/play.svg" class="h-3 w-4"> Revoir la vidéo de présentation
      </button>
    </div>
    <div class="flex flex-wrap mb-12 justify-center w-full z-10">
      <Choice v-for="choice in choiceList" v-show="choice.show" :choice="choice" :key="choice.id" class="my-6 rounded-lg"/>
    </div>
    <div v-if="leads.length > 0" class="flex justify-start items-center pb-12">
      <div class="dot mr-3 bg-black"/>
      <div class="ms-subtitle">TON HISTORIQUE DE PISTES</div>
      <div class="dot ml-3 bg-black"/>
    </div>
    <ChoiceReview v-for="lead in leads" :lead="lead" :key="lead.id" class="mb-20 rounded-lg"/>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import Choice from '../../../../../components/middleSchool/Choice'
  import ChoiceReview from '../../../../../components/middleSchool/ChoiceReview'
  import VideoModal from '../../../../../components/middleSchool/modals/VideoModal'
  import CollegeModal from '../../../../../components/middleSchool/CollegeModal'
  import _ from 'lodash'

  export default {
    name: 'Choix',
    components: { Choice, ChoiceReview, CollegeModal, VideoModal },
    layout: 'dashboard_ms_student',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolStudentDashboard',
    async asyncData({ app, store }) {
      try {
        let response = await app.$api.leads.get({page: 1, numberPerPage: 1000, user_ids: [store.state.auth.user.userID]})
        const leads = response.data || []

        response = await app.$api.modules.getProgress(store.state.auth.user.userID, undefined, store.state.auth.user.classroom.grade)
        const progress = response.data || []
        const currentStep = _.filter(progress, {'isDone': true}).length
        const totalSteps = progress.length
        const hasDone4rankCourse = (currentStep === totalSteps) && (store.state.auth.user.classroom.grade === 4)
        const hasDone3rankModule6 = (currentStep > 4) && (store.state.auth.user.classroom.grade === 3)

        return { leads, hasDone4rankCourse, hasDone3rankModule6 }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        choiceList: [],
        leads: [],
        showVideoStartChoice: !this.$store.state.auth.user.config.sawChoiceVideoMs,
        videoStartChoice: {
          title: 'Vidéo de présentation des pistes',
          path: 'https://vimeo.com/470102068'
        }
      }
    },
    mounted() {
      this.choiceList = [
        {
          id: 'rank3',
          title: 'Fais ton choix de 3ème',
          description: 'Maintenant que tu as complété ton parcours de 4ème, choisis dans quel type de 3ème tu veux t\'orienter\n',
          show: !this.$store.state.auth.user.config.hasChooseRank3 && this.hasDone4rankCourse
        },
        {
          id: 'faculty',
          title: 'Fais ton choix de fillière',
          description: 'Maintenant que tu as complété ton autoportrait, que tu as choisi tes domaines et que tu as pris conscience de tes représentations sur le monde du travail, nous allons te demander de choisir quel type de filière et de parcours tu souhaites faire',
          show: !this.$store.state.auth.user.config.hasChooseFaculty && this.hasDone3rankModule6
        },
        {
          id: 'domains',
          title: 'Choisi des pistes de domaines',
          description: '...\n',
          show: false
        },
        {
          id: 'schools',
          title: 'Choisi des pistes d\'établissements',
          description: '...\n',
          show: false
        }
      ]
    },
    methods: {
      showVideoStartModal() {
        this.showVideoStartChoice = true
      },
      async closeVideoStartModal() {
        const user = this.$store.state.auth.user
        user.config.sawChoiceVideoMs = true
        this.$store.commit('auth/setUser', user)
        await this.$api.user.updateUser(this.$store.state.auth.user)
        this.showVideoStartChoice = !this.$store.state.auth.user.config.sawChoiceVideoMs
      }
    }
  }
</script>

<style scoped>
  .boy-button {
    transform: translateX(80%)translateY(-87%);
  }
</style>

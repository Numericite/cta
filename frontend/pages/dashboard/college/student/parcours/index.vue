<template>
  <div class="pt-16 px-5 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <CollegeModal v-show="showOnboarding" :count="1" @handleClose="closeOnboarding">
      <OnboardingMs/>
    </CollegeModal>
    <div class="flex items-center justify-between mb-10">
      <div class="text-6xl">Bonjour <span class="ms-title">{{ $store.state.auth.user.firstName }} !</span></div>
      <button class="center-vertical button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue p-4" @click="showOnboarding = true">
        <img src="~/assets/img/play.svg" class="h-3 w-4"> Revoir la vidéo de présentation
      </button>
    </div>
    <div v-if="isActions || choiceRank3.show || choiceFaculty.show">
      <div v-if="currentStep !== totalSteps">
        <div class="flex w-full justify-end pb-5">
          <div class="text-ms-gray-dark font-bold text-sm"> <span class="text-ms-blue">{{ totalSteps - currentStep }}</span> séquences restantes</div>
        </div>
        <BigProgressBar :currentStep="currentStep" :totalSteps="totalSteps"/>
      </div>
      <div class="flex items-center mt-8">
        <div class="dot mr-4 bg-black"/>
        <div class="ms-subtitle">Ta prochaine action à compléter</div>
        <div class="dot ml-4 bg-black"/>
      </div>
      <div class="flex flex-wrap mb-12 justify-center w-full z-10">
        <NextAction v-for="explorationType in nextActions.nextExplorationTypes" :key="explorationType.id" :action="explorationType" :nextAction="true" entity="exploration" class="py-6"/>
        <NextAction v-if="nextActions.nextModule && (!nextActions.nextExplorationTypes || (nextActions.nextExplorationTypes && nextActions.nextExplorationTypes.length === 0))" :action="nextActions.nextModule" :nextAction="true" entity="module" class="py-6"/>
        <Choice v-show="choiceRank3.show" :choice="choiceRank3" class="my-6 rounded-lg"/>
        <Choice v-show="choiceFaculty.show" :choice="choiceFaculty" class="my-6 rounded-lg"/>
      </div>
    </div>
    <div v-else-if="nextActions.nextLeads && nextActions.nextLeads.length > 0" class="mt-4">
      <div class="flex items-center">
        <div class="dot mr-4 bg-black"/>
        <div class="ms-subtitle">Ta dernière action à compléter</div>
        <div class="dot ml-4 bg-black"/>
      </div>
      <div class="flex flex-wrap mb-12 justify-center w-full z-10">
        <NextAction v-for="lead in nextActions.nextLeads" :key="lead.id" :action="lead" :nextAction="false" entity="lead" class="py-6" />
      </div>
    </div>
    <div class="flex items-center w-full justify-center pb-12">
      <div class="dot mr-4 bg-black"/>
      <div class="ms-subtitle">Ta progression sur l'année</div>
      <div class="dot ml-4 bg-black"/>
    </div>
    <div class="flex flex-wrap justify-center w-full pt-10">
      <Entity v-for="entity in entities" :key="entity.name" :entity="entity" :row="false" class="pr-4 pb-20"/>
    </div>
    <div v-if="nextActions.nextModule && (nextActions.nextExplorationTypes && (nextActions.nextExplorationTypes && nextActions.nextExplorationTypes.length > 0))" class="flex items-center mt-8">
      <div class="dot mr-4 bg-black"/>
      <div class="ms-subtitle">Ta prochaine séquence</div>
      <div class="dot ml-4 bg-black"/>
    </div>
    <div v-if="nextActions.nextModule && (nextActions.nextExplorationTypes && (nextActions.nextExplorationTypes && nextActions.nextExplorationTypes.length > 0))" class="flex flex-wrap mb-12 justify-center w-full z-10">
      <NextAction :action="nextActions.nextModule" :nextAction="true" entity="module" class="py-6"/>
    </div>
    <div class="flex mb-20 justify-center w-full">
      <HelpMessage :helpMessage="helpMessage"/>
    </div>
    <div v-if="nextActions.nextExplorationTypesToComplete && nextActions.nextExplorationTypesToComplete.length > 0" class="flex items-center w-full justify-start">
      <div class="dot mr-4 bg-black"/>
      <div class="ms-subtitle">TES EXPLORATIONS À COMPLÉTER</div>
      <div class="dot ml-4 bg-black"/>
    </div>
    <div v-if="nextActions.nextExplorationTypesToComplete && nextActions.nextExplorationTypesToComplete.length > 0" class="flex flex-wrap mb-20 justify-center w-full">
      <NextAction v-for="explorationType in nextActions.nextExplorationTypesToComplete" :key="explorationType.id" :action="explorationType" :nextAction="false" entity="exploration" class="py-6" />
    </div>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import OnboardingMs from '../../../../../components/middleSchool/OnboardingMs'
  import CollegeModal from '../../../../../components/middleSchool/CollegeModal'
  import NextAction from '../../../../../components/middleSchool/NextAction'
  import Entity from '../../../../../components/middleSchool/Entity'
  import HelpMessage from '../../../../../components/middleSchool/HelpMessage'
  import BigProgressBar from '../../../../../components/middleSchool/BigProgressBar'
  import Choice from '../../../../../components/middleSchool/Choice'
  import _ from 'lodash'

  export default {
    name: 'Parcours',
    layout: 'dashboard_ms_student',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolStudentDashboard',
    components: { NextAction, Entity, HelpMessage, OnboardingMs, CollegeModal, BigProgressBar, Choice },
    async asyncData({app, store}) {
      try {
        let response = await app.$api.leads.count({user_ids: [store.state.auth.user.userID]})
        const nbLeads = _.get(response.data, 'res', 0)

        response = await app.$api.modules.getProgress(store.state.auth.user.userID, undefined, 
          store.state.auth.user.classroom.grade, 
          store.state.auth.user.school.kind !== 'middle-school' ? store.state.auth.user.school.kind : undefined 
        )
        const progress = response.data || []
        const nbModulesDone = _.filter((progress), {'isDone': true}).length
        const totalSteps = progress.length
        const currentStep = _.filter(progress, {'isDone': true}).length
        const hasDone4rankCourse = (currentStep === totalSteps) && (store.state.auth.user.classroom.grade === 4)
        const hasDone3rankModule6 = (currentStep > 4) && (store.state.auth.user.classroom.grade === 3)

        response = await app.$api.explorations.count({user_ids: [store.state.auth.user.userID]})
        const nbExplorations = _.get(response.data, 'res', 0)

        response = await app.$api.user.getStudentNextActions({
          user_id: store.state.auth.user.userID, 
          grade: store.state.auth.user.classroom.grade, 
          school_type: store.state.auth.user.school.kind !== 'middle-school' ? store.state.auth.user.school.kind : undefined 
        })
        const nextActions = response.data || {}

        if (nextActions.nextExplorationTypesToComplete && nextActions.explorationsToComplete) {
          nextActions.nextExplorationTypesToComplete.forEach((eType) => {
            eType.explorations = _.filter(nextActions.explorationsToComplete, {exploration_type_id: eType.id})
          })
        }

        return { nbLeads, nbModulesDone, nbExplorations, nextActions, totalSteps, currentStep, hasDone4rankCourse, hasDone3rankModule6 }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        entities: [],
        helpMessage: {
          title: 'On est là pour t’aider !',
          message: 'Tu retrouveras ce bloc à différents moments de ton parcours, pour te donner un petit coup de pouce, et ainsi mener à bien les actions que tu devras réaliser.'
        },
        choiceRank3: {},
        choiceFaculty: {},
        showOnboarding: !this.$store.state.auth.user.config.sawOnboardingMs
      }
    },
    computed: {
      sortActions() {
        return _.filter(this.actions)
      },

      isActions() {
        return this.nextActions.nextModule || (this.nextActions.nextExplorationTypes && this.nextActions.nextExplorationTypes.length > 0)
      },
    },
    mounted() {
      this.entities = [
        {
          title: 'Séquences',
          name: 'module',
          description: 'Une série d\'activités pour t\'aider à mieux te connaître et connaître le monde qui t\'entoure.',
          info: 'séquences terminées',
          count: this.nbModulesDone,
          moreInfo: '',
          illustration: 'step1'
        },
        {
          title: 'Explorations',
          name: 'exploration',
          description: 'Des actions pour échanger, pour t\'informer et pour découvrir des filières de formation et le monde professionnel.',
          info: 'explorations créées',
          count: this.nbExplorations,
          moreInfo: '',
          illustration: 'step3'
        },
      ]

      this.choiceRank3 = {
        id: 'rank3',
        title: 'Fais ton choix de 3ème',
        description: 'Maintenant que tu as complété ton parcours de 4ème, choisis dans quel type de 3ème tu veux t\'orienter\n',
        show: !this.$store.state.auth.user.config.hasChooseRank3 && this.hasDone4rankCourse
      }

      this.choiceFaculty = {
        id: 'faculty',
        title: 'Fais ton choix de fillière',
        description: 'Maintenant que tu as complété ton autoportrait, que tu as choisi tes domaines et que tu as pris conscience de tes représentations sur le monde du travail, nous allons te demander de choisir quel type de filière et de parcours tu souhaites faire',
        show: !this.$store.state.auth.user.config.hasChooseFaculty && this.hasDone3rankModule6
      }

      if ((!this.isActions && this.$store.state.auth.user.classroom.grade === 4) || this.$store.state.auth.user.classroom.grade === 3) {
        this.entities.push(
          {
            title: 'Pistes',
            name: 'lead',
            description: 'Un outil qui va te permettre de formuler tes choix et de vérifier si ils te correspondent bien.',
            info: 'hypothèses formulées',
            count: this.nbLeads,
            illustration: 'step2'
          }
        )
      }
    },
    methods: {
      showModalOnboarding() {
        this.showOnboarding = true
      },

      async closeOnboarding() {
        const user = this.$store.state.auth.user
        user.config.sawOnboardingMs = true
        this.$store.commit('auth/setUser', user)
        await this.$api.user.updateUser(this.$store.state.auth.user)
        this.showOnboarding = false
      }
    },
  }
</script>

<style scoped>

</style>

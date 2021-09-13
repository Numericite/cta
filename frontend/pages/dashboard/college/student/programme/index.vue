<template>
  <div class="pt-16 px-10 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <div class="ms-title">Complète ton programme</div>
    <div class="pt-5 pb-11 ms-subtitle-small text-justify text-ms-gray-dark max-w-2/3">
      Ici tu pourras découvrir le détail du programme qui t’attend tout au long année ; tu auras également
      une vision d’ensemble sur la progression de ton parcours.
    </div>
    <div class="flex items-center pt-16 pb-4">
      <div class="dot mr-4 bg-black"/>
      <div class="ms-subtitle">Ta progression</div>
      <div class="dot ml-4 bg-black"/>
    </div>
    <div class="flex w-full justify-end pb-5">
      <div class="text-ms-gray-dark font-bold text-sm"> <span class="text-ms-blue">{{ totalSteps - currentStep }}</span> modules restants</div>
    </div>
    <BigProgressBar :currentStep="currentStep" :totalSteps="totalSteps"/>
    <Entity :entity="moduleMain" :row="true" class="py-4"/>
    <Module v-for="(module, index) in modules" :key="module.id" :disabled="index !== 0 && !modules[index - 1].isDone" :doneDate="module.done_date" :module="module" class="py-4"/>
    <Entity :entity="explorationMain" :row="true" class="py-4"/>
    <ExplorationType v-for="explorationType in explorationTypes" :key="explorationType.id" :explorationType="explorationType" class="p-4 rounded-lg bg-white shadow-lg mb-8"/>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import ExplorationType from '../../../../../components/middleSchool/ExplorationType'
  import Entity from '../../../../../components/middleSchool/Entity'
  import Module from '../../../../../components/middleSchool/Module'
  import _ from 'lodash'
  import BigProgressBar from '../../../../../components/middleSchool/BigProgressBar'

  export default {
    name: 'Programme',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolStudentDashboard',
    components: { BigProgressBar, Module, ExplorationType, Entity },
    layout: 'dashboard_ms_student',
    async asyncData ({app, store}) {
      try {
        let response = await app.$api.modules.getProgress(store.state.auth.user.userID, undefined,
          store.state.auth.user.classroom.grade, 
          store.state.auth.user.school.kind !== 'middle-school' ? store.state.auth.user.school.kind : undefined 
        )
        const progress = response.data || []
        const totalSteps = progress.length
        const currentStep = _.filter(progress, {'isDone': true}).length

        response = await app.$api.modules.getList({
          page: 1, 
          numberPerPage: 1000, 
          grade: store.state.auth.user.classroom.grade, 
          school_type: store.state.auth.user.school.kind !== 'middle-school' ? store.state.auth.user.school.kind : undefined
        })
        const modules = (response.data || []).map((module) => {
          const progressItem = _.find(progress, {'module_id': module.id})
          module.isDone = progressItem.isDone
          if (progressItem.isDone) {
            module.done_date = progressItem.doneDate
          }
          return module
        })

        response = await app.$api.explorationsType.getList({page: 1, numberPerPage: 1000, exclude_partner: true})
        let explorationTypesWithoutPartner = response.data || []

        response = await app.$api.explorationsType.getListByRestriction({classroom_id: store.state.auth.user.classroom.id})
        let explorationTypeWithPartner = response.data || []
        
        let partners = store.state.partners
        explorationTypeWithPartner.map(explorationType => {
          explorationType.img_path = _.find(partners, {'slug': explorationType.partner_slug}).img_path
        })

        let explorationTypes = explorationTypeWithPartner.concat(explorationTypesWithoutPartner)

        return { modules, explorationTypes, progress, totalSteps, currentStep }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        modules: [],
        explorations: [],
        explorationTypes: [],
        moduleMain: {
          title: 'Séquence',
          name: 'module',
          description: 'Une série d\'activités pour t\'aider à mieux te connaître et connaître le monde qui t\'entoure.',
          moreInfo: '',
          illustration: 'step1'
        },
        explorationMain: {
          title: 'Exploration',
          name: 'exploration',
          description: 'Des actions pour échanger, pour t\'informer et pour découvrir des filières de formation et le monde professionnel.',
          count: 0,
          info: 'explorations disponibles',
          moreInfo: '',
          illustration: 'step3'
        },
        entities: [
          {
            title: 'S\'orienter',
            name: 'lead',
            description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pretium diam ipsumddd.',
            info: 'Prochaine échéance dans',
            moreInfo: '1 mois',
            illustration: 'step2'
          },
        ],
      }
    },
    created() {
      this.explorationMain.count = this.explorationTypes.length
    }
  }
</script>

<style scoped>

</style>

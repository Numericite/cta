<template>
  <div class="pt-32">
    <div v-if="!openModalModule">
      <h1 class="ms-title mb-8">{{ entity.name }}</h1>
      <h2 v-if="exploration" class="ms-title-medium font-normal italic mb-8">{{ exploration.name }}</h2>
      <FormInterpretor :fields="fields" :fieldLogs="fieldLogs" @submit="createLogs"/>
    </div>
    <ModuleModal v-if="openModalModule" :module="entity" :moduleMaxNum="moduleMaxNum" @handleClose="closeModalModule"/>
    <ExplorationCompleteModal v-if="openModalExploration" @handleClose="closeModalModule"/>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import FormInterpretor from '../../../../../components/middleSchool/FormInterpretor'
  import _ from 'lodash'
  import ModuleModal from '../../../../../components/middleSchool/modals/ModuleModal'
  import ExplorationCompleteModal from '../../../../../components/middleSchool/modals/ExplorationCompleteModal'

  export default {
    name: 'Formulaire',
    layout: 'dashboard_ms_student',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolStudentDashboard',
    scrollToTop: true,
    components: { ExplorationCompleteModal, ModuleModal, FormInterpretor },
    async asyncData({app, route, store}) {
      try {
        let exploration = null
        let response = await app.$api.fields.getList({
          page: 1,
          numberPerPage: 1000,
          parent_id: route.params.parent_id
        })
        const fields = _.orderBy((response.data || []), ['sort_id'], ['ASC'])

        const field_ids = _.map(fields, 'id')
        response = await app.$api.fieldLogs.getList({
          page: 1,
          numberPerPage: 1000,
          field_ids: field_ids,
          user_ids: [store.state.auth.user.userID],
          child_id: route.query.exploration ? route.query.exploration : store.state.auth.user.userID,
          child_type: route.query.exploration ? 'exploration' : 'user'
        })
        const fieldLogs = response.data || []

        const parent_id = _.get(fields[0], 'parent_id', 'null')
        let formType = ''
        let entity = {}

        response = await app.$api.modules.getModuleByIds([parent_id])
        if ((response.data || []).length > 0) {
          formType = 'module'
          entity = response.data[0]
        }

        response = await app.$api.modules.getModuleActivityByIds([parent_id])
        if ((response.data || []).length > 0) {
          formType = 'activity'
          entity = response.data[0]
        }

        response = await app.$api.explorationsType.getExplorationTypeByIds([parent_id])
        if ((response.data || []).length > 0) {
          formType = 'exploration'
          entity = response.data[0]

          if (route.query.exploration) {
            response = await app.$api.explorations.getExplorationByIds([route.query.exploration])
            exploration = response.data[0] || {}
          }
        }

        response = await app.$api.modules.getMaxNum(store.state.auth.user.classroom.grade)
        const moduleMaxNum = (response.data || 0)

        return { fields, fieldLogs, formType, entity, exploration, moduleMaxNum }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        fields: [],
        openModalModule: false,
        openModalExploration: false,
        moduleMaxNum: 0
      }
    },
    methods: {
      async createLogs(logs) {
        await this.asyncForEach(logs, async (log) => {
          if (log.id) {
            await this.$api.fieldLogs.update(log)
          } else {
           await this.$api.fieldLogs.create(log)
          }
        })

        let toastMessage = null
        switch (this.formType) {
          case 'module':
            toastMessage = 'Séquence '
            break
          case 'activity':
            toastMessage = 'Activité terminée'
            break
          case 'exploration':
            toastMessage = 'Exploration '
            break
        }

        if (this.formType !== 'activity') {
          logs[0].id !== undefined ? toastMessage += 'modifiée' : toastMessage += 'terminée'
        }

        this.$toast.success(toastMessage, {
          position: 'bottom-right',
          duration: 3500
        })

        if (this.formType === 'module') {
          if (logs[0].id !== undefined) {
            this.$router.push('/dashboard/college/student/parcours/')
          } else {
            this.endModalModule()
          }
        } else if (this.formType === 'activity') {
          this.$router.push('/dashboard/college/student/programme/module/' + this.entity.id)
        } else if (this.formType === 'exploration') {
          if (logs[0].id !== undefined) {
            this.$router.push('/dashboard/college/student/parcours/')
          } else {
            this.endExplorationModule()
          }
        }
      },
      getFormTypeDisplay() {
        let display = ''
        switch (this.formType) {
          case 'module':
            display = 'la séquence'
            break
          case 'activity':
            display = 'l\'activité'
            break
          case 'exploration':
            display = 'l\'exploration'
            break
        }
        return display
      },
      endExplorationModule() {
        this.openModalExploration = true
      },
      endModalModule() {
        this.openModalModule = true
      },
      closeModalModule() {
        this.openModalModule = false
      }
    }
  }

</script>

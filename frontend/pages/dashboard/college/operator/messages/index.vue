<template>
  <div class="pt-16 pb-12 px-5 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <Chat v-if="Object.keys(userMatrix.initialSync).length" ref="chat_base" typeComponent="base" />
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import Chat from '../../../../../components/middleSchool/Conversations/Chat.vue'
  import _ from 'lodash'
  import { mapGetters } from 'vuex'

  export default {
    name: 'Messages',
    components: { Chat },
    layout: 'dashboard_ms_operator',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolOperatorDashboard',
    computed: {
      ...mapGetters({
        userMatrix: 'auth/getMatrix'
      })
    },
  }
</script>
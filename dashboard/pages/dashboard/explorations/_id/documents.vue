<template>
  <div>
    <nuxt-link :to="'/dashboard/explorations/'" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux explorations</nuxt-link>
    <v-app>
      <documents-component :parentType="parentType" :parentId="parentId" :itemProp="exploration"></documents-component>
    </v-app>
  </div>
</template>

<script>

import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import DocumentsComponent from "../../../../components/documents/DocumentsComponent";

export default {
  name: 'DashboardExplorationDocuments',
  layout: "dashboard",
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessDashboard',
  components: { DocumentsComponent },
  $_veeValidate: {
    validator: 'new'
  },
  async asyncData({ app, route }) {
    try {
      let requestExploration = await app.$api.explorations.getByIds([route.params.id])
      const exploration = requestExploration.data[0] || {}

      return  { exploration }
    } catch (e) {
      console.log(e)
    }
  },
  data() {
    return {
      parentId: this.$route.params.id,
      parentType: 'exploration'
    }
  },
}

</script>

<style lang="scss">

</style>

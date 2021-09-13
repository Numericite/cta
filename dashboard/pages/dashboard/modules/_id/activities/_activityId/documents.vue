<template>
  <div>
    <nuxt-link :to="'/dashboard/modules/' + $route.params.id + '/activities'" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux activités du module "{{ module.name }}"</nuxt-link>
    <v-app>
      <documents-component :parentType="parentType" :parentId="parentId" :itemProp="module"></documents-component>
    </v-app>
  </div>
</template>

<script>

import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import DocumentsComponent from "../../../../../../components/documents/DocumentsComponent";

export default {
  name: 'DashboardModuleActivityDocuments',
  layout: "dashboard",
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessDashboard',
  components: { DocumentsComponent },
  $_veeValidate: {
    validator: 'new'
  },
  async asyncData({ app, route }) {
    try {
      let request = await app.$api.modules.getByIds([route.params.id])
      const module = request.data[0] || {}

      return  { module }
    } catch (e) {
      console.log(e)
    }
  },
  data() {
    return {
      parentId: this.$route.params.activityId,
      parentType: 'moduleActivity'
    }
  },
}

</script>

<style lang="scss">

</style>

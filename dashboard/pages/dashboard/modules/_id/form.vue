<template>
  <div>
    <nuxt-link to="/dashboard/modules" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux modules</nuxt-link>
    <v-app>
      <form-generator :parentId="parentId"></form-generator>
    </v-app>
  </div>
</template>

<script>

import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import FormGenerator from '~/components/formGenerator/FormGenerator'

export default {
  name: 'DashboardModuleForm',
  layout: "dashboard",
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessDashboard',
  components: { FormGenerator },
  $_veeValidate: {
    validator: 'new'
  },
  data() {
    return {
      parentId: this.$route.params.id
    }
  },
}

</script>

<style lang="scss">

</style>

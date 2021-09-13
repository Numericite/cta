<template>
  <v-app class="no-style">
    <h2 class="mt-2 mb-4">Statistiques des visites</h2>
    <div class="matomo-iframe-container">
      <iframe src="https://matomo.numericite.eu/index.php?module=Widgetize&action=iframe&moduleToWidgetize=Dashboard&actionToWidgetize=index&idSite=14&period=week&date=yesterday" frameborder="0" marginheight="0" marginwidth="0" width="100%" height="100%"></iframe>
    </div>
  </v-app>
</template>


<script>

import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  layout: "dashboard",
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessDashboard',
}

</script>

<style lang="scss">
  
  .matomo-iframe-container {
    height: 80vh;
  }

</style>
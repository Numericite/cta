<template>
  <div class="wrapper">
    <side-bar type="sidebar">

      <sidebar-dropdown :link="{name: 'Dashboard', icon: 'ti-panel'}" :init-open="initOpenDashboard">
        <sidebar-link to="/dashboard/stats">
          <p>Statistiques</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/highSchool">
          <p>Lycée</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/middleSchool">
          <p>Collège</p>
        </sidebar-link>
      </sidebar-dropdown>

      <sidebar-dropdown :link="{name: 'Comptes', icon: 'ti-user'}" :init-open="initOpenAccounts">
        <sidebar-link to="/dashboard/users">
          <p>Utilisateurs</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/schools">
          <p>Établissements</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/partners">
          <p>Partenaires</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/companies">
          <p>Entreprises</p>
        </sidebar-link>
      </sidebar-dropdown>

      <sidebar-dropdown :link="{name: 'Lycée', icon: 'ti-rocket'}" :init-open="initOpenHighSchool">
        <sidebar-link to="/dashboard/activities">
          <p>Activités</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/courses">
          <p>Parcours</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/domains">
          <p>Domaines</p>
        </sidebar-link>
      </sidebar-dropdown>
      <sidebar-dropdown :link="{name: 'Collège', icon: 'ti-car'}" :init-open="initOpenMiddleSchool">
        <sidebar-link to="/dashboard/modules">
          <p>Modules</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/explorations">
          <p>Explorations</p>
        </sidebar-link>
      </sidebar-dropdown>

      <sidebar-dropdown :link="{name: 'Ressources', icon: 'ti-files'}" :init-open="initOpenResources">
        <sidebar-link to="/dashboard/documents">
          <p>Documents</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/resources">
          <p>Resources externes</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/meetings">
          <p>Réunions d'information</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/internships">
          <p>Stages</p>
        </sidebar-link>
      </sidebar-dropdown>

      <sidebar-dropdown :link="{name: 'Contenus', icon: 'ti-pencil'}" :init-open="initOpenContents">
        <sidebar-link to="/dashboard/pages">
          <p>Pages</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/posts">
          <p>Articles</p>
        </sidebar-link>
        <sidebar-link to="/dashboard/questions">
          <p>FAQ</p>
        </sidebar-link>
      </sidebar-dropdown>
    </side-bar>
    <div class="main-panel">
      <top-navbar></top-navbar>

      <dashboard-content @click.native="toggleSidebar">

      </dashboard-content>

      <content-footer></content-footer>
    </div>
  </div>
</template>
<style lang="scss">
  @import "../assets/sass/paper-dashboard.scss";
</style>
<script>
  import TopNavbar from './TopNavbar.vue'
  import ContentFooter from './ContentFooter.vue'
  import DashboardContent from './Content.vue'
  import _ from 'lodash'

  export default {
    components: {
      TopNavbar,
      ContentFooter,
      DashboardContent
    },
    data() {
      return {
        initOpenHighSchool: _.some(['activities', 'courses'], (el) => _.includes(this.$route.path, el)),
        initOpenMiddleSchool: _.some(['modules', 'explorations'], (el) => _.includes(this.$route.path, el)),
        initOpenAccounts: _.some(['users', 'schools', 'partners', 'companies'], (el) => _.includes(this.$route.path, el)),
        initOpenResources: _.some(['domains', 'documents', 'resources', 'meetings', 'internships'], (el) => _.includes(this.$route.path, el)),
        initOpenContents: _.some(['pages', 'posts', 'questions'], (el) => _.includes(this.$route.path, el)),
        initOpenDashboard: _.some(['stats', 'highSchool', 'middleSchool'], (el) => _.includes(this.$route.path, el)),
      }
    },
    methods: {
      toggleSidebar() {
        // if (this.$sidebar && this.$sidebar.showSidebar) {
        //   this.$sidebar.displaySidebar(false)
        // }
      }
    }
  }

</script>

<template>
  <div class="dashboard flexContainer-dash bg-blue-lightest ml-32 sm:ml-0 relative">

    <picture class="">
      <source srcset="~/assets/img/dash-bg-1.webp"
              type="image/webp">
      <img src="~/assets/img/dash-bg-1.png"
           class="absolute pin-t pin-r z-0 sm:hidden"
           alt="Illustration d'arrière plan">
    </picture>

    <div class=" pt-16 z-10 relative sm:pt-8">
      <div class="text-6xl sm:text-5xl text-blue font-bold font-proza">
        Mes fiches
      </div>
      <div class="desc text-blue text-3xl sm:text-xl font-normal mt-12 sm:mt-4">
        Voici la liste de toutes les <strong>fiches pédagogiques</strong> disponibles à ce jour. Vous pouvez les <strong>télécharger</strong>.
      </div>
    </div>
    <div class="mt-8 z-10 relative">
      <fiche-card v-for="(document, key) in documents"
                  :key="key"
                  :document="document"
                  class="my-5" />
    </div>

    <div v-if="pagination.numberOfPages > 1" class="text-center mt-12">
      <span class="text-blue font-extrabold text-4xl cursor-pointer mr-4" @click="changePage(pagination.page - 1)"><img src="~/assets/img/chevron-left.svg"></span>
      <span v-for="(page, index) in pagination.numberOfPages" :key="index"
            :class="{'ml-4': index !== 0, 'font-extrabold': page === pagination.page}"
            class="text-blue text-4xl cursor-pointer"
            @click="changePage(page)">{{ page }}</span>
      <span class="text-blue font-extrabold text-4xl cursor-pointer ml-4" @click="changePage(pagination.page + 1)"><img src="~/assets/img/chevron-right.svg"></span>
    </div>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import FicheCard from '~/components/dashboard/FicheCard'
import heads from '~/config/meta.json'

export default {
  name: 'ManagerDashboard',
  layout: 'dashboard_manager',
  head() {
    if ( heads[this.$options.name] ) return heads[this.$options.name]
    return {
      title: 'Crée ton avenir - Dashboard manager'
    }
  },
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessDocumentsPage',
  components: { FicheCard },
  async asyncData({app, store}) {
    try {
      let response = await app.$api.schools.getSchoolsByIds([store.state.auth.user.config.school_id])
      const school = response.data[0] || {}

      return { school }
    } catch (e) {
      console.log(e)
    }
  },
  data() {
    return {
      documents: [],
      school: {},
      school_kinds: undefined,
      count: 0,
      pagination: {
        page: 1,
        numberPerPage: 10,
        numberOfPages: 0
      }
    }
  },
  mounted() {
    if (this.$store.state.auth.user.config.accountType === 'teacher') {
      this.school_kinds = ['all', this.school.kind]
    }
    this.getRows()
  },
  methods: {

    changePage(page) {
      if (page > 0 && page <= this.pagination.numberOfPages) {
        this.pagination.page = page
        this.getRows()
      }
    },

    async getRows() {
      try {
        let response = await this.$api.documents.count({parent_type: this.$store.state.auth.user.config.accountType, school_kinds: this.school_kinds})
        this.count = response.data.res || 0

        this.pagination.numberOfPages = Math.trunc((this.count + this.pagination.numberPerPage - 1) / this.pagination.numberPerPage)

        response = await this.$api.documents.getList({parent_type: this.$store.state.auth.user.config.accountType, school_kinds: this.school_kinds, page: this.pagination.page, numberPerPage: this.pagination.numberPerPage})
        this.documents = response.data || []
      } catch (e) {
        console.log(e)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  width: calc(100vw - 9.1rem);
  @screen sm {
    width: 100vw;
  }
  .field {
    @apply mt-0;
  }
  .classFilters {
    @screen sm {
      transform: translateY(15px);
    }
  }
}
</style>

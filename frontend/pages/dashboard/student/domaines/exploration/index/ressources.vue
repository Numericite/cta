<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue"><span v-html="texts[0]" /></h3>
    <div class="desc text-blue text-3xl sm:text-xl w-5/6 font-normal mt-4">
      <span v-html="texts[1]" />
    </div>
    <div class="mt-8 z-10 relative">
      <resource-card v-for="(resource, key) in resources"
                     :key="key"
                     :index="key + 1"
                     :resource="resource"
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
  import ResourceCard from '~/components/dashboard/ResourceCard'

  export default {
    name: 'ExplorationResources',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    scrollToTop: true,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    components: { ResourceCard },
    data() {
      return {
        resources: [],
        count: 0,
        pagination: {
          page: 1,
          numberPerPage: 10,
          numberOfPages: 0
        },
        texts: this.getPageTexts(this.$route.name)
      }
    },
    mounted() {
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
          let response = await this.$api.resources.count({parent_type: this.$store.state.auth.user.config.accountType + '-domain'})
          this.count = response.data.res || 0

          this.pagination.numberOfPages = Math.trunc(this.count / this.pagination.numberPerPage) + 1

          response = await this.$api.resources.getList({parent_type: this.$store.state.auth.user.config.accountType + '-domain', page: this.pagination.page, numberPerPage: this.pagination.numberPerPage})
          this.resources = response.data || []
        } catch (e) {
          console.log(e)
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>

<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue">
      <span v-html="texts[0]" />
    </h3>
    <div class="desc text-blue text-3xl sm:text-xl w-5/6 font-normal mt-4">
      <span v-if="!$store.state.auth.user.config.stream_ids" >
        <span v-html="texts[1]" />
      </span>
      <span v-if="$store.state.auth.user.config.stream_ids">
        <span v-html="texts[2]" />
      </span>
    </div>
    <div v-if="!$store.state.auth.user.config.stream_ids" class="mt-8 mb-8 flex italic">
      <span class="flex">
        <div class="w-4 h-4 rounded-full flex bg-green"/>
        <span class="flex ml-2">
          <span v-html="texts[3]" />
        </span>
      </span>
      <span class="flex ml-6">
        <div class="w-4 h-4 rounded-full flex bg-orange"/>
        <span class="flex ml-2">
          <span v-html="texts[4]" />
        </span>
      </span>
      <span class="flex ml-6">
        <div class="w-4 h-4 rounded-full flex bg-red"/>
        <span class="flex ml-2">
          <span v-html="texts[5]" />
        </span>
      </span>
    </div>
    <rex :domains="domains" :isStream="true" class="mt-8" @domainsValidated="domainsValidated"/>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import Rex from '~/components/dashboard/Rex'

  export default {
    name: 'ExplorationRex',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    scrollToTop: true,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    components: { Rex },
    async asyncData({app, store, error}) {
      try {
        const chapters = store.state.auth.user.course.chapters
        let streamsChapter = _.find(chapters, { slug: 'stream' })
        if (!(_.get(_.find(streamsChapter.children, { slug: 'exploration' }), 'display', false))) {
          return error({ statusCode: 404, message: 'Post not found' })
        }
        let response = await app.$api.domains.getByIds({ids: streamsChapter.domain_ids, page: 1, numberPerPage: 1000})
        const domains = response.data || []

        return { domains }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        texts: this.getPageTexts(this.$route.name)
      }
    },
    methods: {
      domainsValidated() {
        this.$forceUpdate()
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>

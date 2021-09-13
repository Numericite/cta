<template>
  <div/>
</template>

<script>
  import _ from 'lodash'
  export default {
    async asyncData({app, store, error}) {
      try {
        const chapters = store.state.auth.user.course.chapters
        if (!(_.get(_.find(chapters, { slug: 'activity-area' }), 'display', false))) {
          return error({ statusCode: 404, message: 'Post not found' })
        }
          let response = await app.$api.domains.getList({ page: 1, numberPerPage: 1000, kind: 'activity-area' })
          const domains = response.data || []
          return { domains }
      } catch (e) {
        console.log(e)
      }
    },
  }
</script>

<template>
  <div/>
</template>

<script>
  import _ from 'lodash'
  export default {
    async asyncData({store, error}) {
      try {
        const chapters = store.state.auth.user.course.chapters
        if (!(_.get(_.find(chapters, { slug: 'stream' }), 'display', false))) {
          return error({ statusCode: 404, message: 'Post not found' })
        }
      } catch (e) {
        console.log(e)
      }
    },
  }
</script>

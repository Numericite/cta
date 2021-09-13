<template>
  <div>
    <nuxt-child @loadData="loadData"/>
  </div>
</template>
<script>
  import _ from 'lodash'
  export default {
    scrollToTop: true,
    name: 'DashboardFile',
    layout: 'dashboard',
    async asyncData({app, params, redirect}) {

        if(!params.version) {
          try {
            let request = await app.$api.files.getByIds([params.file])
            const file = request.data[0] || {}

            console.log('param version :' + params.version)
          const default_version_id = _.get(_.find(file.versions, {'isDefault': true}), 'id', 'none')
          redirect('/dashboard/page/' + params.file + '/' + default_version_id )
            }
            catch (e) {
              console.log(e)
            }
        }

    },
    methods: {
      async loadData(callback) {
        let request = await this.$api.files.getByIds([this.$route.params.file])
        callback(request.data[0] || {})
      }
    }
  }
</script>

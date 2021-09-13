<template>
  <div class="entity w-full">
    <div :class="{'rounded-b-none' : explorations.length }" class="min-w-100 border-ms-red-light bg-white min-h-100 border-solid border-1 rounded-lg relative">
      <div class="py-2 px-6 w-full">
        <div class="flex xl:flex-row flex-col items-center justify-between w-full">
          <div v-show="explorationType.img_path">
            <img :src="explorationType.img_path" class="w-48 rounded-lg">
          </div>
          <div :class="{'px-6' : explorationType.img_path}" class="flex flex-col xl:w-2/3">
            <div class="flex justify-start items-center pt-6">
              <div class="text-5xl font-bold">{{ explorationType.name }}</div>
            </div>
            <p class="pt-2 text-justify text-xl text-ms-gray-dark leading-loose break-all">
              {{ explorationType.description | str_limit(200) }}
            </p>
          </div>
          <div class="flex flex-col mb-4 xl:mb-0">
            <button class="button-ms text-ms-red bg-white border-solid border-1 border-ms-red hover:bg-ms-red hover:text-white" @click="$router.push({ path: '/dashboard/college/student/programme/exploration/' + explorationType.id, params: { explorationType: explorationType.id }})">Commencer</button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="explorations.length" class="min-w-100 border-ms-red-light bg-white min-h-100 border-solid border-b-1 border-l-1 border-r-1 rounded-b-lg relative p-5">
      <div class="ms-subtitle-bis w-full pt-4">{{ explorations.length }} exploration(s) en cours</div>
      <Exploration v-for="exploration in explorations" :key="exploration.id" :exploration="exploration"/>
    </div>
  </div>
</template>

<script>
  import Exploration from './Exploration'
  import _ from 'lodash'

  export default {
    name: 'ExplorationType',
    components: { Exploration },
    props: {
      explorationType: {
        type: Object,
        default: function() {
          return {}
        }
      },
    },
    data() {
      return {
        explorations: [],
      }
    },
    created() {
      this.getExplorations()
    },
    methods: {
      async getExplorations() {
        let response = await this.$api.explorations.getList({
          page: 1,
          numberPerPage: 1000,
          user_ids: [this.$store.state.auth.user.userID],
          exploration_type_ids: [this.explorationType.id]
        })
        const explorations = response.data || []

        response = await this.$api.fields.getList({
          page: 1,
          numberPerPage: 1000,
          parent_id: this.explorationType.id
        })
        const fields = response.data || []

        await this.asyncForEach(explorations, async (exploration) => {
          response = await this.$api.fieldLogs.count({
            user_ids: [this.$store.state.auth.user.userID],
            child_id: exploration.id,
            child_type: 'exploration',
            field_ids: _.map(fields, 'id')
          })
          exploration.isDone = (response.data.res > 0) || false
        })

        this.explorations = explorations
      }
    },
  }
</script>

<style scoped>
</style>

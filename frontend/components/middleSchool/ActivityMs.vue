<template>
  <div class="entity w-full bg-white z-10 rounded-lg">
    <div class="bg-white shadow-lg p-3 rounded-lg">
      <div class="min-w-100 border-ms-red border-opacity-50 bg-white min-h-100 border-solid border-1 rounded-t-lg relative">
        <div class="absolute img-container w-24 h-32">
          <div class="bg-white w-4 h-32 absolute bloc-white"/>
          <img :src="'/img/middle-school/step3.png'" class="absolute pt-5">
          <div class="relative">
            <div class="dot-left bg-ms-orange absolute rounded-full"/>
            <div class="dot-right bg-ms-orange absolute rounded-full"/>
          </div>
        </div>
        <div class="py-2 w-full">
          <div class="flex xl:flex-row flex-col items-center justify-between w-full px-6">
            <div class="flex flex-col xl:w-3/4 pl-16">
              <div class="flex flex-col pt-6">
                <div class="flex justify-start items-center pb-4">
                  <div class="dot mr-2 bg-ms-red"/>
                  <div class="ms-subtitle text-ms-red">Exploration</div>
                  <div class="dot ml-2 bg-ms-red"/>
                </div>
              </div>
              <p class="pt-2 text-xl text-ms-gray-dark leading-loose break-all text-justify">
                {{ explorationType.description }}
              </p>
            </div>
            <div class="flex flex-col xl:w-1/5 pr-6">
              <button class="button-ms text-ms-red bg-white border-solid border-1 border-ms-red hover:bg-ms-red hover:text-white" @click="createExploration">Créer</button>
            </div>
          </div>
        </div>
      </div>
      <div v-if="explorations.length" class="min-w-100 border-ms-red bg-white min-h-100 border-solid border-b-1 border-l-1 border-r-1 rounded-b-lg relative p-5">
        <div class="ms-subtitle-bis w-full pt-4">{{ explorations.length }} exploration(s) en cours</div>
        <Exploration v-for="exploration in explorations" :key="exploration.id" :exploration="exploration"/>
      </div>
    </div>
  </div>
</template>

<script>
import ProgressBar from '../middleSchool/ProgressBar'
import Button from '../../mixins/button'
import Exploration from './Exploration'
import _ from 'lodash'

export default {
  name: 'ActivityMs',
  components: { Exploration, Button, ProgressBar },
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
      explorations: []
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
        exploration_type_ids: [this.$route.params.id]
      })
      const explorations = response.data || []

      response = await this.$api.fields.getList({
        page: 1,
        numberPerPage: 1000,
        parent_id: this.$route.params.id
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
    },
    createExploration() {
      this.$emit('createExploration')
    }
  },
}
</script>

<style scoped>
.img-container{
  left: 0;
  top:50%;
  transform: translate(-50%, -50%);
}
.dot-left {
  left: 50%;
  top:0;
  transform: translate(-65%, 0%);
  width: 3px;
  height: 3px;
}
.dot-right {
  left: 50%;
  bottom: -100px;
  transform: translate(-65%, 0%);
  width: 3px;
  height: 3px;
}
.bloc-white {
  left: 50%;
  top:50%;
  transform: translate(-50%, -50%);
}
.exploration-name:first-letter {
  text-transform: capitalize !important;
}
</style>

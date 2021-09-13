<template>
  <div class="activity d-flex pa-0 ml-4 my-4 align-center justify-space-between center">
    <div v-if="showVersion" class="empty-bloc"></div>
    <img class="activity-img" :class="{'activity-img-big' : showVersion}" :src="'/img/autoportrait/' + activity.slug + '.svg'">
    <div class="pl-4 pr-2 py-3 activity-name" :class="{'activity-name-big' : showVersion, 'text-center' : !showVersion }">{{activity.name}}</div>
    <div v-if="showVersion">
      <v-select
        attach
      :items="activityVersions"
      :class="{'selectVersion' : showVersion }"
      class="select-version"
      item-value="id"
      item-text="name"
      v-model="activity.version_id"
      placeholder="version">
      </v-select>
    </div>
  </div>
</template>

<script>
  import _ from 'lodash'
  export default {
    props: {
      activity:  {
        type: Object,
        default: () => {
          return null
        }
      },
      showVersion:  {
        type: Boolean,
        default: () => {
          return false
        }
      },
      versions:  {
        type: Array,
        default: () => {
          return null
        }
      },
    },
    data() {
      return {
       selectedVersion: null,
       activityVersions: [],
      }
    },
    created() {
      this.activityVersions = _.filter(this.versions, {parent_id: this.activity.id})
      if(!this.activity.version_id) {
        this.activity.version_id = _.get(_.find(this.activityVersions, {isDefault : true}), 'id', '')
      }
    },
  }
</script>
<style lang="scss">
  .activity {
    background: white !important;
    border-radius: 4px;
    box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.15);
    position: relative;

  }
  .select-version.v-text-field>.v-input__control>.v-input__slot:before { border-style: none; } .select-version.v-text-field>.v-input__control>.v-input__slot:after { border-style: none; }
  .activity-img {
    position: absolute;
    width: 45px;
    height: 45px;
    margin: auto;
    top: 0; left: -20px; bottom: 0;
  }
  .activity-img-big {
    width: 60px;
    height: 60px;
    left: -30px;
  }
  .activity-name {
    font-weight: bold;
    height: 100%;
  }
  .activity-name-big {
    min-width: 60%;
  }
  .empty-bloc {
    width: 5%;
    max-width: 5%;
  }
</style>

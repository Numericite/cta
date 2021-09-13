<template>
  <div class="domain-card px-3 d-flex align-center justify-space-between">
    <v-layout>
      <v-flex wrap xs12 class=" d-flex align-center domain-name">
        {{domain.name}}
      </v-flex>
      <v-flex class="pb-3">
        <v-switch @change="saveDomain()"  class="d-inline-block switch-btn" depressed v-model="domain.isSelected"></v-switch>
      </v-flex>
    </v-layout>
  </div>
</template>

<script>
  import _ from 'lodash'
  export default {
    props: {
      domain:  {
        type: Object,
        default: () => {
          return null
        }
      },
    },
    methods: {
      async saveDomain() {
        this.$emit('saveDomain')
      }
    }
  }
</script>
<style lang="scss">
  .domain-card {
    box-shadow: 0 0 5px 0 rgba(0, 0, 0, 0.15);
    min-width: 500px;
    max-width: 500px;
    font-weight: bold;
    color: gray;
    max-height: 80px;
  }
  .domain-name {
    min-width: 80%;
  }
</style>

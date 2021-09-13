<template>
  <div v-if="item && item.nom">{{ item.nom }}</div>
  <div v-else-if="item">{{ item }}</div>
</template>

<script>
export default {
  name: 'CitiesItemTemplate',
  props: {
    item: {
      type: Object,
      default: function() {
        return { item: null }
      }
    }
  }
}
</script>

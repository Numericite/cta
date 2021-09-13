<template>
  <div>{{ item.name }}</div>
</template>

<script>
export default {
  name: 'SchoolItemTemplate',
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

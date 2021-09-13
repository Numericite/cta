<template>
  <div>aa {{ item.name }}</div>
</template>

<script>
  export default {
    name: 'NameItemTemplate',
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

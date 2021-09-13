<template>
  <div class="card">
    <div class="content">
      <div class="row d-flex">
        <div :class="{'col-xs-5': hasIcon, 'col-xs-7': !hasIcon}">
          <slot name="header"></slot>
        </div>
        <div :class="{'col-xs-7': hasIcon, 'col-xs-5': !hasIcon}">
          <slot name="content"></slot>
        </div>
      </div>
    </div>

  </div>
</template>
<script>
  export default {
    name: 'stats-card',
    props: {
      hasIcon: {
        type: Boolean,
        default: true
      }
    }
  }

</script>
<style>

</style>

<template>
  <div class="w-full h-full pt-8 pb-12 px-12 flex flex-col justify-center text-center">
    <div class="ms-title pb-6">{{ video.title }}</div>
    <div v-if="video.short_description" class="font-plex mb-8 text-ms-gray-dark leading-normal">
      {{ video.short_description }}
    </div>
    <div class="w-full h-full">
      <video-embed :src="video.path"/>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'CollegeVideoModalDashboard',
    layout: 'dashboard_ms_student',
    props: {
      video: {
        type: Object,
        default: function() {
          return {}
        }
      },
    },
  }
</script>

<style lang="scss">

.embed-responsive {
  height: 100%;
  iframe {
    width: 100%;
    height: 100%;
  }
}

</style>

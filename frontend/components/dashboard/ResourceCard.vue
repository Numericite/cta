<template>
  <div class="cardShadow bg-white rounded-xl p-6 px-8 sm:px-6">
    <div class="flex sm:flex-col">
      <div class="w-1/6 flex-none py-4 pr-8 sm:py-8 sm:pr-4 sm:pl-0 border-solid border-r border-blue-lightest sm:border-0 sm:w-1/2">
        <img :src="resource.img_path" class="w-full sm:mx-auto block">
      </div>
      <div class="left-part flex flex-col ml-8 justify-around sm:mr-0 sm:ml-0">
        <div class="font-semibold text-md text-blue-lighter sm:text-base">
          Ressource externe n°{{ index }}
        </div>
        <div class="font-bold text-3xl text-blue sm:text-xl mt-4">
          {{ resource.name }}
        </div>
        <div class="sm:text-xl mt-6">
          <p>{{ resource.description }}</p>
        </div>
        <div v-if="resource.embed_video_code" class="text-xl text-blue sm:text-xl mt-6">
          <div class="video-container" v-html="resource.embed_video_code"/>
        </div>
      </div>
    </div>
    <div class="text-right mt-12 mb-4">
      <a :href="resource.link" class="button button--blue sm:px-2 sm:py-2 sm:text-sm" target="_blank">
        ACCÉDER AU SITE
      </a>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ResourceCard',
  props: {
    resource: {
      type: Object,
      default() {
        return {
          name: ''
        }
      }
    },
    index: {
      type: Number,
      default() {
        return 0
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.cardShadow {
  box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
}

.italic {
  font-style: italic;
}

.video-container {
  iframe {
    margin: auto;
    display: block;
    max-width: 100%;
  }
}
</style>

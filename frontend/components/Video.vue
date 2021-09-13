<template>
  <div class="relative overflow-hidden video-container" @mouseover="iframeMouseOver = true" @mouseout="iframeMouseOver = false">
    <iframe :src="'https://player.vimeo.com/video/' + vimeoId + '?color&autopause=0&loop=0&muted=0&title=0&portrait=0&byline=0#t='" allowfullscreen="" class="w-full h-auto"/>
  </div>
</template>

<script>
export default {
  name: 'Video',
  props: {
    vimeoId: {
      type: String,
      default: function() {
        return { src: null }
      }
    }
  },
  data() {
    return {
      iframeMouseOver: false
    }
  },
  mounted() {
    var self = this
    window.addEventListener('blur',function(){
      if(self.iframeMouseOver){
        self.$emit('clicked')
      }
    });
  }
}
</script>

<style lang="scss" scoped>
  .video-container {
    padding-bottom:56.25%;
    padding-top:30px;
    height: 0;
  }

  .video-container iframe,
  .video-container object,
  .video-container embed {
    width:100%;
    height:100%;
    position:absolute;
    top:0;
    left:0;
  }
</style>

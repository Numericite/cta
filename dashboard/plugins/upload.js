import Vue from 'vue'
import VuejsClipper from 'vuejs-clipper'

Vue.use(VuejsClipper,{
  components: {
    clipperBasic: true,
    clipperPreview: true,
    clipperUpload: true
  }
})

import Vue from 'vue'

// require styles
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

// if used in nuxt.js/ssr, you should keep require it only in browser build environment
if (process.browser) {
  const VueQuillEditor = require('vue-quill-editor/dist/ssr')
  Vue.use(VueQuillEditor, {
    modules: {
      toolbar: [
        [{ 'header': 1 }, { 'header': 2 }],
        ['bold', 'italic', 'underline', 'strike'],
        [{ 'list': 'ordered' }, { 'list': 'bullet' }],
        [{ 'indent': '-1' }, { 'indent': '+1' }],
        ['link', 'image', 'video']
      ]
    },
    placeholder: 'Votre contenu ici...'
  })
}


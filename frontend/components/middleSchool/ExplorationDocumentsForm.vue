<template>
  <div class="">
    <div v-if="documentsTmp.length === 0" class="text-3xl text-center p-4 pt-12 italic">
      Aucune ressource pour l'exploration...
    </div>
    <div v-for="document in documentsTmp" :key="document.id">

      <div class="flex items-end">
        <FormulateInput v-model="document.title"
                        :outerClass="['my-8 text-left w-4/5']"
                        :inputClass="['mt-4 py-5 px-6 focus:border-purple text-ms-gray-dark']"
                        :label="'Ressource ' + document.num"
                        :validationMessages="{
                          required: 'Le nom de la ressource document est obligatoire'
                        }"
                        validation="required"
                        name="title"
                        type="text"
                        placeholder="Nom de la ressource" />
        <button class="my-8 ml-auto p-4 border-1 border-solid px-6 py-5 text-white rounded-lg2 bg-ms-red-crisp focus:outline-none"
                @click="deleteDocument(document)">
          <img src="~/assets/img/TrashExploration.svg"
               class="h-4 w-5 font-extrabold ">
        </button>
      </div>
      <div class="flex">
        <FormulateInput v-model="document.file_kind"
                        :outerClass="['w-2/5 pr-8 text-left']"
                        :inputClass="['py-5 px-6 focus:border-ms-red']"
                        :options="fileKinds"
                        validation="required"
                        name="file_kind"
                        type="select"/>

        <button v-if="document.file_kind === 'document' && !document.file" type="file" class="outline-none border-1 border-solid p-5 rounded-lg bg-blue focus:outline-none" @click="$refs['file_input_' + document.num][0].$el.querySelector('input').click()">
          <img src="~/assets/img/FileExploration.svg"
               class="h-3 w-4 font-extrabold">
        </button>
        <div v-else-if="document.file_kind === 'document' && document.file" class="flex items-center w-2/5">
          <span>{{ document.file.name.substring(0, 70) + "..." }}</span>
        </div>
        <FormulateInput v-if="document.file_kind === 'document'"
                        :id="'file_input_' + document.num" :ref="'file_input_' + document.num"
                        :errorsClass="['file_input_hidden']" :fileNameClass="['file_name_hidden']"
                        :validationMessages="{
                          required: 'Le document est obligatoire'
                        }"
                        :validationName="'document_' + document.num" :validation="(document.file && !document.file.id) || !document.file ? 'required' : ''" type="file" errorBehavior="submit"
                        hidden @change="handleFile($event, document)" />

        <FormulateInput v-if="document.file_kind === 'link_video'" v-model="document.path"
                        :outerClass="['text-left w-2/5']"
                        :inputClass="['py-5 px-6 focus:border-purple text-ms-gray-dark']"
                        :validationRules="{
                          url_video: ({ value }) => value.includes('youtube', 'youtu', 'vimeo', 'dailymotion')
                        }"
                        :validationMessages="{
                          required: 'Le lien de la vidéo est obligatoire',
                          url: 'Le lien n\'est pas valide',
                          url_video: 'Le lien n\'est pas un lien vidéo'
                        }"
                        name="video_path"
                        validation="required|url|url_video"
                        validationName="video_path"
                        type="text"
                        placeholder="Lien de la vidéo" />

        <FormulateInput v-if="document.file_kind === 'link'" v-model="document.path"
                        :outerClass="['text-left w-2/5']"
                        :inputClass="['py-5 px-6 focus:border-purple text-ms-gray-dark']"
                        :validationMessages="{
                          required: 'Le lien de la ressource est obligatoire',
                          url: 'Le lien n\'est pas valide',
                        }"
                        name="link_path"
                        validation="required|url"
                        type="text"
                        validationName="link_path"
                        placeholder="Lien vers la ressource" />
      </div>
    </div>
    <div class="flex justify-center pt-12">
      <button class="bg-blue px-12 py-6 text-2xl text-white rounded-xl shadow-sm font-bold focus:outline-none"
              @click="addDocument()">
        Ajouter une ressource
      </button>
    </div>
  </div>
</template>

<script>
  import _ from 'lodash'

  export default {
    props: {
      exploration: {
        type: Object,
        default: function() {
          return {}
        }
      },
      documents: {
        type: Array,
        default: function() {
          return []
        }
      }
    },
    data() {
      return {
        documentsTmp: [],
        documentsToDelete: [],
        fileKinds: [
          { label: 'Document', value: 'document'},
          { label: 'Lien vidéo', value: 'link_video'},
          { label: 'Lien externe', value: 'link'}
        ],
        numDocument: 2,
      }
    },
    watch: {
      documents: {
        handler() {
          console.log('change !!!')
          this.documentsTmp = this.documents
          this.numDocument = Math.max(...this.documentsTmp.map(item => item.num))
          this.documentsToDelete = []
        }
      }
    },
    methods: {
      handleFile(e, document) {
        const files = e.target.files || e.dataTransfer.files;
        document.file = files[0]
        console.log('in there ' + this.documentsTmp)
        this.$emit('documentsChange', this.documentsTmp)
        this.$forceUpdate()
      },

      addDocument() {
        this.documentsTmp.push({
          title: '',
          file_kind: 'document',
          num: ++this.numDocument,
          path: '',
        })
      },

      deleteDocument(document) {
        if (document.id)
          this.documentsToDelete.push(document.id)

        this.documentsTmp = _.without(this.documentsTmp, document)
      },

      getDocuments() {
        return this.documentsTmp
      },

      getDeletedDocuments() {
        return this.documentsToDelete
      }
    }
  }
</script>

<style lang="scss">

.file_input_hidden {
  margin-top: 0.75em;
  margin-left: 1em;
}

.file_name_hidden {
  display: none;
}

</style>

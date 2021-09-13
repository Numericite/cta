<template>
  <div>
    <v-toolbar flat color="black">
      <v-toolbar-title class="numericite-toolbar-title">Documents de {{ parentType === 'moduleActivity' ? 'l\'activité' : parentType === 'explorationType' ? 'l\'exploration' : ''}} "{{ itemProp.name }}"</v-toolbar-title>
      <v-divider
        class="mx-2"
        inset
        vertical
      ></v-divider>
      <v-spacer></v-spacer>
      <v-dialog v-model="dialog" max-width="500px" persistent>
        <template v-slot:activator="{ on }">
          <v-btn color="primary" dark class="mb-2" v-on="on">Créer</v-btn>
        </template>
        <v-card>
          <v-card-title>
            <span class="headline">{{ formTitle }}</span>
          </v-card-title>
          <v-card-text>
            <v-form v-model="formData">
              <v-container grid-list-md>
                <v-layout wrap>
                  <v-flex xs12>
                    <v-text-field
                      v-model="editedItem.title"
                      v-validate="'required'"
                      :error-messages="errors.collect('title')"
                      label="Titre *"
                      data-vv-name="title"
                      required></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-textarea
                      v-model="editedItem.short_description"
                      :error-messages="errors.collect('short_description')"
                      label="Description"
                      data-vv-name="short_description"
                      required></v-textarea>
                  </v-flex>
                  <v-flex xs12>
                    <v-select
                      v-model="editedItem.file_kind"
                      :items="selectFileKinds"
                      v-validate="'required'"
                      :error-messages="errors.collect('file_kind')"
                      @change="changeFileKind"
                      label="Type de fichiers *"
                      data-vv-name="file_kind"
                      required
                    ></v-select>
                  </v-flex>
                  <v-flex xs12 v-if="editedItem.file_kind === 'document'">
                    <v-text-field
                      label="Document *"
                      v-validate="{required: editedItem.file_kind === 'document'}"
                      key="document"
                      :error-messages="errors.collect('document')"
                      @click="pickDocument"
                      placeholder="Cliquez pour sélectionner un document..."
                      v-model="editedItem.description"
                      data-vv-name="document"></v-text-field>
                    <input
                      type="file"
                      style="display: none"
                      ref="document"
                      accept="application/pdf"
                      @change="onDocumentPicked"
                    >
                  </v-flex>
                  <v-flex xs12 v-if="editedItem.file_kind === 'link'">
                    <v-text-field
                      v-model="editedItem.path"
                      v-validate="{required: editedItem.file_kind === 'link', url: {require_protocol: true}}"
                      key="link"
                      :error-messages="errors.collect('path')"
                      label="Lien *"
                      data-vv-name="path"
                    ></v-text-field>
                  </v-flex>
                  <v-flex xs12 v-if="editedItem.file_kind === 'link_video'">
                    <v-text-field
                      v-model="editedItem.path"
                      v-validate="{required: editedItem.file_kind === 'link_video', url: {require_protocol: true}, regex: '(youtube|youtu|vimeo|dailymotion)'}"
                      key="link_video"
                      :error-messages="errors.collect('path')"
                      label="Lien *"
                      data-vv-name="path"
                    ></v-text-field>
                  </v-flex>
                </v-layout>
              </v-container>
            </v-form>
          </v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click="close">Annuler</v-btn>
            <v-btn color="blue darken-1" flat @click="save">Sauvegarder</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-toolbar>
    <v-data-table
      :headers="headers"
      :items="items"
      :pagination.sync="pagination"
      :total-items="total"
      :loading="loading"
      :rows-per-page-items="$store.state.dataTable.rowsPerPageItems"
      class="elevation-1"
    >
      <v-progress-linear v-slot:progress color="blue" indeterminate></v-progress-linear>
      <template slot="items" slot-scope="props">
        <tr class="sortableRow" :key="props.item.id">
          <td class="dragButton">
            <v-btn class="sortHandle" :ripple="false" flat><v-icon small> drag_indicator </v-icon>{{ props.item.num }}</v-btn>
          </td>
          <td>{{ props.item.title }}</td>
          <td>{{ getFileKindTitle(props.item.file_kind || 'document') }}</td>
          <td>{{ props.item.description }}</td>
          <td>
            <a v-if="props.item.file_kind === 'document'" :href="props.item.path" target="_blank">
              <v-btn round depressed small>
                <v-icon
                  small
                >
                  ti-import
                </v-icon>
              </v-btn>
            </a>
            <a v-if="props.item.file_kind === 'link'" :href="props.item.path" target="_blank">
              <v-btn round depressed small>
                <v-icon
                  small
                >
                  ti-link
                </v-icon>
              </v-btn>
            </a>
            <v-btn round depressed small @click="editItem(props.item)">
              <v-icon
                small
              >
                edit
              </v-icon>
            </v-btn>
            <confirm-delete @confirmedClick="deleteItem(props.item)"></confirm-delete>
          </td>
        </tr>
      </template>
    </v-data-table>
  </div>
</template>

<script>
import utils from '~/assets/js/utils'
import ConfirmDelete from "../ConfirmDelete";
import Sortable from "sortablejs";

export default {
  $_veeValidate: {
    validator: 'new'
  },
  components: { ConfirmDelete },
  props: ['parentType', 'parentId', 'itemProp'],
  data() {
    return {
      items: [],
      itemsCopy: [],
      formData: null,
      editedItem: {
        title: '',
        short_description: '',
        user_id: '',
        file_kind: '',
        description: '',
        path: '',
      },
      editedIndex: -1,
      defaultItem: {
        title: '',
        short_description: '',
        user_id: '',
        file_kind: '',
        description: '',
        path: '',
      },
      dialog: false,
      loading: true,
      firstLoad: true,
      total: 0,
      pagination: {
        page: 1,
        rowsPerPage: 100
      },
      headers: [
        {
          text: 'Numéro',
          align: 'left',
          sortable: false,
          value: 'num'
        },
        {
          text: 'Titre',
          align: 'left',
          sortable: false,
          value: 'text'
        },
        {
          text: 'Type',
          align: 'left',
          sortable: false,
          value: 'text'
        },
        {
          text: 'Nom',
          align: 'left',
          sortable: false,
          value: 'text'
        },
        { text: 'Actions', sortable: false }
      ],
      selectFileKinds: [
        { text: 'Document', value: 'document'},
        { text: 'Lien vidéo', value: 'link_video'},
        { text: 'Lien externe', value: 'link'}
      ]
    }
  },
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'Créer un document' : 'Éditer un document'
    },
    dragOptions() {
      return {
        animation: 250,
        group: "description",
        disabled: false,
        ghostClass: "ghost"
      }
    }
  },
  watch: {
    params: {
      handler() {
        if (!this.firstLoad) {
          this.getRows().then(this.bindData)
        }
      },
      deep: true
    },
    pagination: {
      handler () {
        if (!this.firstLoad) {
          this.getRows().then(this.bindData)
        }
      },
      deep: true
    },
    dialog (val) {
      val || this.close()
    }
  },
  mounted() {
    this.$validator.localize('fr', this.dictionary)

    new Sortable(
      document.querySelector(".v-datatable tbody"),
      {
        animation: 250,
        group: 'description',
        disabled: false,
        ghostClass: 'ghost',
        draggable: '.sortableRow',
        handle: '.sortHandle',
        onEnd: this.dragReorder
      }
    )

    this.getRows().then((data) => {
      this.firstLoad = false
      this.bindData(data)
    })
  },
  methods: {
    dragReorder ({oldIndex, newIndex}) {
      const movedItem = this.items.splice(oldIndex, 1)[0]
      this.items.splice(newIndex, 0, movedItem)
      this.updateNum()
    },

    updateNum () {
      let pageIndexHandler = ((this.pagination.page - 1 ) * this.pagination.rowsPerPage)
      this.items.map( (item, index) => item.num = index + 1 + pageIndexHandler)

      this.items.map( async item => {
        if (!_.isEqual(item, _.find(this.itemsCopy, { id: item.id }))) {
          //EDIT
          try {
            await this.$api.documents.update(item)
          } catch (e) {

          }
        }
      })

      if (!_.isEqual(this.items, this.itemsCopy))
        this.getRows().then(this.bindData)
    },

    getFileKindTitle(file_kind) {
      return _.get(_.find(this.selectFileKinds, {value: file_kind}), 'text', '')
    },

    bindData(data) {
      this.total = data.count || 0
      this.items = data.items || []
      this.itemsCopy = JSON.parse(JSON.stringify(data.items || []))
      this.loading = false
    },

    async getRows() {
      this.loading = true;
      return new Promise(async (resolve, reject) => {
        try {
          let request = await this.$api.documents.count({
            parent_type: this.parentType,
            parent_ids: [this.parentId]
          })
          const count = request.data.res || 0

          request = await this.$api.documents.getList({
            page: this.pagination.page,
            numberPerPage: this.pagination.rowsPerPage,
            parent_type: this.parentType,
            parent_ids: [this.parentId]
          })
          const items = request.data || []

          items.sort( (a, b) => a.num - b.num)

          resolve({
            count,
            items
          })
        } catch (e) {
          reject(e)
        }
      })
    },

    editItem (item) {
      this.editedIndex = this.items.indexOf(item)
      this.editedItem = Object.assign({}, item)
      if (!this.editedItem.file_kind) this.editedItem.file_kind = 'document'
      this.dialog = true
    },

    close () {
      this.dialog = false
      setTimeout(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
        requestAnimationFrame(() => {
          this.$validator.reset();
        });
      }, 300)
    },

    async deleteItem (item) {
      try {
        await this.$api.documents.drop([item.id])
        this.items = this.items.filter(document => document.id !== item.id)
        this.updateNum()
        this.getRows().then(this.bindData)
      } catch (e) {
        console.log(e)
      }
    },

    async save () {
      this.$validator.validate().then(async valid => {
        if (valid) {
          this.editedItem.user_id = this.$store.state.auth.user.userID
          this.editedItem.parent_type = this.parentType
          this.editedItem.parent_id = this.parentId
          if (this.editedIndex > -1) {
            //EDIT
            try {

              if (this.editedItem.file_kind === 'document') {
                let response = await this.$api.documents.upload(this.editedItem.parent_type, this.editedItem.file, this.editedItem.grades, this.editedItem.school_kind, true)
                this.editedItem.path = response.data.res.path
              }

              this.editedItem.file_kind === 'link' || this.editedItem.file_kind === 'link_video' ? this.editedItem.description = this.editedItem.title : ''
              await this.$api.documents.update(this.editedItem)
              this.getRows().then(this.bindData)
              this.close()
            } catch (e) {

            }
          } else {
            //CREATE
            if (this.editedItem.file_kind === 'document') {
              try {
                await this.$api.documents.upload(this.editedItem.parent_type, this.editedItem.file)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e.response)
              }
            } else if (this.editedItem.file_kind === 'link' || this.editedItem.file_kind === 'link_video') {
              try {
                this.editedItem.num = this.total + 1
                this.editedItem.description = this.editedItem.title
                await this.$api.documents.create(this.editedItem)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e.response)
              }
            }

          }
        }
      })
    },

    pickDocument() {
      this.$refs.document.click()
    },

    onDocumentPicked(e) {
      const files = e.target.files
      const formData = new FormData()
      if(files[0] !== undefined) {
        this.editedItem.description = files[0].name
        formData.append('file', files[0], utils.toValidFileName(files[0].name))
        formData.append('user_id', this.$store.state.auth.user.userID)
        formData.append('description', this.editedItem.description)
        formData.append('title', this.editedItem.title)
        formData.append('short_description', this.editedItem.short_description)
        formData.append('file_kind', this.editedItem.file_kind)
        formData.append('parent_id', this.parentId)
        formData.append('num', this.total + 1)
        this.editedItem.file = formData
      } else {
        this.editedItem.description = ''
      }
    },

    changeFileKind() {
      this.editedItem.description = ''
      this.editedItem.path = ''
    },

  }
}

</script>
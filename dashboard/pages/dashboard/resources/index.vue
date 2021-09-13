<template>
<div>
  <v-app>
    <v-toolbar flat color="black">
      <v-toolbar-title class="numericite-toolbar-title">Ressource externes</v-toolbar-title>
      <v-divider
        class="mx-2"
        inset
        vertical
      ></v-divider>
      <v-spacer></v-spacer>
      <v-select
        v-model="searchType"
        label="Chercher par type de destination"
        :items="types"
        clearable
        single-line
        hide-details
      ></v-select>
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
                      v-model="editedItem.name"
                      v-validate="'required'"
                      :error-messages="errors.collect('name')"
                      label="Nom *"
                      data-vv-name="name"
                      required></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-text-field
                      v-model="editedItem.link"
                      v-validate="'required'"
                      :error-messages="errors.collect('link')"
                      label="Lien *"
                      data-vv-name="link"
                      required></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-select
                      v-model="editedItem.parent_type"
                      :items="types"
                      v-validate="'required'"
                      :error-messages="errors.collect('parent_type')"
                      label="À destination de *"
                      data-vv-name="parent_type"
                      required
                    ></v-select>
                  </v-flex>
                  <v-flex xs12>
                    <v-textarea
                      v-model="editedItem.description"
                      v-validate="'required'"
                      :error-messages="errors.collect('description')"
                      label="Description *"
                      data-vv-name="description"
                      required></v-textarea>
                  </v-flex>
                  <v-flex xs12>
                    <v-textarea
                      v-model="editedItem.embed_video_code"
                      :error-messages="errors.collect('embed')"
                      label="Code HTML du lecteur vidéo"
                      data-vv-name="embed">
                    </v-textarea>
                  </v-flex>
                  <v-flex xs12>
                    <label :class="{'text-danger': missingImg && !tmpImg}" class="fake-label">Image *</label>
                  </v-flex>
                  <v-flex v-if="editedItem.img_path" class="text-center" xs12>
                    <button type="button" class="btn btn-primary btn-modified" @click="deleteImage()"><i class="fa fa-pencil"></i> Modifier</button>
                    <img class="img img-preview" :src="editedItem.img_path">
                  </v-flex>
                  <v-flex v-if="!editedItem.img_path" class="text-center" xs12>
                    <clipper-upload ref="upload" :check="true" accept="image/jpeg,image/png" v-model="tmpImg"><button type="button" class="btn btn-primary"><i class="fa fa-folder"></i> Parcourir...</button></clipper-upload>
                  </v-flex>
                  <v-flex v-show="tmpImg" xs12 md6>
                    <clipper-basic
                      ref="clipper"
                      preview="my-preview"
                      :ratio="3/4"
                      :src="tmpImg"></clipper-basic>
                  </v-flex>
                  <v-flex v-show="tmpImg" xs12 md6>
                    <clipper-preview name="my-preview" class="my-clipper"></clipper-preview>
                  </v-flex>
                  <v-flex v-if="missingImg && !tmpImg" xs12>
                    <div class="custom-error">L'image est obligatoire</div>
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
      <template v-slot:items="props">
        <td>{{ props.item.name }}</td>
        <td><img class="table-logo" :src="props.item.img_path"></td>
        <td>{{ displayedType(props.item.parent_type) }}</td>
        <td class="justify-center">
          <v-btn round depressed small @click="editItem(props.item)">
            <v-icon
              small
            >
              edit
            </v-icon>
          </v-btn>
          <confirm-delete @confirmedClick="deleteItem(props.item)"></confirm-delete>
        </td>
      </template>
    </v-data-table>
  </v-app>
</div>
</template>
<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import utils from '~/assets/js/utils'
  import ConfirmDelete from '~/components/ConfirmDelete'
  import { ACCOUNT_TYPES } from '~/config'
  import _ from 'lodash'

  export default {
    name: 'DashboardResources',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    components: { ConfirmDelete },
    routePerimeterAction: 'accessDashboard',
    $_veeValidate: {
      validator: 'new'
    },
    data () {
      return {
        items: [],
        formData: null,
        editedItem: {
          name: '',
          link: '',
          description: '',
          parent_type: '',
          embed_video_code: '',
          img_path: ''
        },
        editedIndex: -1,
        defaultItem: {
          name: '',
          link: '',
          description: '',
          parent_type: '',
          embed_video_code: '',
          img_path: ''
        },
        dialog: false,
        loading: true,
        firstLoad: true,
        total: 0,
        searchType: '',
        pagination: {
          page: 1,
          rowsPerPage: this.$store.state.dataTable.rowsPerPageItems[0]
        },
        headers: [
          {
            text: 'Nom',
            align: 'left',
            sortable: false,
            value: 'name'
          },
          {
            text: 'Logo',
            align: 'left',
            sortable: false,
            value: 'name'
          },
          {
            text: 'À destination de',
            align: 'left',
            sortable: false,
            value: 'text'
          },
          { text: 'Actions', sortable: false }
        ],
        deleteDialog: false,
        tmpImg: null,
        resultURL: null,
        missingImg: false,
        types: [{text: ACCOUNT_TYPES.student + ' - Domaines', value: 'student-domain'}, {text: ACCOUNT_TYPES.student + ' - Filières', value: 'student-stream'}, {text: ACCOUNT_TYPES.teacher + ' - Lycée Général', value: 'teacher-high-school'}, {text: ACCOUNT_TYPES.teacher + ' - Lycée Pro', value: 'teacher-high-school-pro'}]
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer une ressource externe' : 'Éditer une ressource externe'
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
      searchType: {
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
      this.getRows().then((data) => {
        this.firstLoad = false
        this.bindData(data)
      })
    },
    methods: {
      bindData(data) {
        this.total = data.count || 0
        this.items = data.items || []
        this.loading = false
      },

      async getRows() {
        this.loading = true;
        return new Promise(async (resolve, reject) => {
          try {
            let request = await this.$api.resources.count({
              parent_type: this.searchType ? this.searchType : undefined
            })
            const count = request.data.res || 0

            request = await this.$api.resources.getList({
              page: this.pagination.page,
              numberPerPage: this.pagination.rowsPerPage,
              parent_type: this.searchType ? this.searchType : undefined
            })
            const items = request.data || []

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
        this.dialog = true
      },

      close () {
        this.dialog = false
        this.$data.tmpImg = null
        this.missingImg = false
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
          await this.$api.resources.drop([item.id])
          this.getRows().then(this.bindData)
        } catch (e) {
          console.log(e)
        }
      },

      async save () {
        this.$validator.validate().then(async valid => {
          if (valid && (this.tmpImg || this.editedItem.img_path)) {
            if (this.editedIndex > -1) {
              //EDIT
              try {
                await this.$api.resources.update(this.editedItem)

                const file = utils.dataURItoFormData(this.getImgResult(), this.editedItem.id + '_' + new Date().getTime() + '.' + this.getExtension(this.$refs.upload.file.name))
                await this.$api.resources.uploadLogo(this.editedItem.id, file)

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            } else {
              //CREATE
              try {
                let response = await this.$api.resources.create(this.editedItem)
                const id = response.data.res.id || ''

                const file = utils.dataURItoFormData(this.getImgResult(), id + '_' + new Date().getTime() + '.' + this.getExtension(this.$refs.upload.file.name))
                await this.$api.resources.uploadLogo(id, file)

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            }
          } else if (!this.editedItem.img_path && !this.tmpImg) {
            this.missingImg = true
          }
        })
      },

      getExtension(name) {
        return name.substring(name.lastIndexOf('.') + 1)
      },

      getImgResult() {
        const canvas = this.$refs.clipper.clip();//call component's clip method
        return canvas.toDataURL("image/" + this.getExtension(this.$refs.upload.file.name), 1);//canvas->image
      },

      deleteImage() {
        this.editedItem.img_path = ''
      },

      displayedType(type) {
        return _.get(_.find(this.types, {value: type}), 'text', '')
      }
    }
  }
</script>
<style lang="scss">
.btn-modified {
  margin-bottom: 1em;
}
</style>

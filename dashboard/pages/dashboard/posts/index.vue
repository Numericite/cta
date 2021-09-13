<template>
  <div>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Articles</v-toolbar-title>
        <v-divider
          class="mx-2"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-dialog v-model="dialog" max-width="800px" persistent>
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
                    <v-flex xs4>
                      <v-menu
                        ref="menu1"
                        v-model="openDateTool"
                        :close-on-content-click="false"
                        :nudge-right="40"
                        lazy
                        transition="scale-transition"
                        offset-y
                        full-width
                        max-width="290px"
                        min-width="290px"
                      >
                        <template v-slot:activator="{ on }">
                          <v-text-field
                            v-model="computedDateFormatted"
                            label="Date *"
                            persistent-hint
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-date-picker v-model="tmpDate" no-title @input="openDateTool = false"></v-date-picker>
                      </v-menu>
                    </v-flex>
                    <v-flex xs8>
                      <v-select
                        v-model="editedItem.status_name"
                        :items="status"
                        v-validate="'required'"
                        :error-messages="errors.collect('status')"
                        label="Status *"
                        data-vv-name="status"
                        required
                      ></v-select>
                    </v-flex>
                    <v-flex xs12 class="quill-container">
                      <label :class="{'text-danger': missingContent && !editedItem.content}" class="fake-label">Contenu *</label>
                      <div class="quill-editor"
                           :content="editedItem.content"
                           v-quill:myQuillEditor
                           @change="onEditorChange($event)">
                      </div>
                    </v-flex>
                    <v-flex v-if="missingContent && !editedItem.content" xs12>
                      <div class="custom-error">Veuillez ajouter du contenu</div>
                    </v-flex>
                    <!--<v-flex xs12>-->
                      <!--<label :class="{'text-danger': missingImg && !tmpImg}" class="fake-label">Image *</label>-->
                    <!--</v-flex>-->
                    <!--<v-flex v-if="editedItem.img_path" class="text-center" xs12>-->
                      <!--<button type="button" class="btn btn-primary btn-modified" @click="deleteImage()"><i class="fa fa-pencil"></i> Modifier</button>-->
                      <!--<img class="img img-preview" :src="editedItem.img_path">-->
                    <!--</v-flex>-->
                    <!--<v-flex v-if="!editedItem.img_path" class="text-center" xs12>-->
                      <!--<clipper-upload v-model="tmpImg"><button type="button" class="btn btn-primary"><i class="fa fa-folder"></i> Parcourir...</button></clipper-upload>-->
                    <!--</v-flex>-->
                    <!--<v-flex v-show="tmpImg" xs12 md6>-->
                      <!--<clipper-basic-->
                        <!--ref="clipper"-->
                        <!--preview="my-preview"-->
                        <!--:ratio="4/3"-->
                        <!--:src="tmpImg"></clipper-basic>-->
                    <!--</v-flex>-->
                    <!--<v-flex v-show="tmpImg" xs12 md6>-->
                      <!--<clipper-preview name="my-preview" class="my-clipper"></clipper-preview>-->
                    <!--</v-flex>-->
                    <!--<v-flex v-if="missingImg && !tmpImg" xs12>-->
                      <!--<div class="custom-error">L'image est obligatoire</div>-->
                    <!--</v-flex>-->
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
          <td>{{ props.item.date | displayDate }}</td>
          <td>{{ props.item.title }}</td>
          <td v-if="props.item.status_name === 'open'"><i class="fa fa-check fa-highlight"></i></td>
          <td v-if="props.item.status_name === 'standby'"><i class="fa fa-times fa-highlight"></i></td>
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

  export default {
    name: 'DashboardPosts',
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
          title: '',
          date: null,
          img_path: '',
          content: ''
        },
        editedIndex: -1,
        defaultItem: {
          title: '',
          date: null,
          img_path: '',
          content: ''
        },
        tmpDate: new Date().toISOString().substr(0, 10),
        dialog: false,
        loading: true,
        firstLoad: true,
        total: 0,
        pagination: {
          page: 1,
          rowsPerPage: this.$store.state.dataTable.rowsPerPageItems[0]
        },
        headers: [
          {
            text: 'Date de publication',
            align: 'left',
            sortable: false,
            value: 'name'
          },
          {
            text: 'Titre',
            align: 'left',
            sortable: false,
            value: 'name'
          },
          {
            text: 'Publié',
            align: 'left',
            sortable: false
          },
          { text: 'Actions', sortable: false }
        ],
        deleteDialog: false,
        tmpImg: null,
        resultURL: null,
        missingImg: false,
        missingContent: false,
        openDateTool: false,
        status: [{text: 'Publié', value: 'open'}, {text: 'Brouillon', value: 'standby'}]
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer un article' : 'Éditer un article'
      },
      computedDateFormatted () {
        return this.formatDate(this.tmpDate)
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
            let request = await this.$api.posts.count()
            const count = request.data.res || 0

            request = await this.$api.posts.getList({
              page: this.pagination.page,
              numberPerPage: this.pagination.rowsPerPage
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
        this.tmpDate = new Date(this.editedItem.date).toISOString().substr(0, 10)
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
          await this.$api.posts.drop([item.id])
          this.getRows().then(this.bindData)
        } catch (e) {
          console.log(e)
        }
      },

      async save () {
        this.$validator.validate().then(async valid => {
          if (valid && this.editedItem.content) {
            this.editedItem.date = new Date(this.tmpDate).getTime()
            if (this.editedIndex > -1) {
              //EDIT
              try {
                await this.$api.posts.update(this.editedItem)

                // const file = utils.dataURItoFormData(this.getImgResult(), this.editedItem.id + '_' + new Date().getTime() + '.jpg')
                // await this.$api.posts.uploadLogo(this.editedItem.id, file)

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            } else {
              //CREATE
              try {
                await this.$api.posts.create(this.editedItem)

                // const id = response.data.res.id || ''
                // const file = utils.dataURItoFormData(this.getImgResult(), id + '_' + new Date().getTime() + '.jpg')
                // await this.$api.posts.uploadLogo(id, file)

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            }
          } else if (!this.editedItem.content) {
            this.missingContent = true
          }
        })
      },

      getImgResult() {
        const canvas = this.$refs.clipper.clip();//call component's clip method
        return canvas.toDataURL("image/jpg", 1);//canvas->image
      },

      deleteImage() {
        this.editedItem.img_path = ''
      },

      onEditorChange(event) {
        this.editedItem.content = event.html
        this.missingContent = this.editedItem.content.length > 0
      },

      formatDate (date) {
        if (!date) return null

        const [year, month, day] = new Date(date).toISOString().substr(0, 10).split('-')
        return `${day}/${month}/${year}`
      }
    }
  }
</script>
<style lang="scss">
  .btn-modified {
    margin-bottom: 1em;
  }
</style>

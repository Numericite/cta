<template>
  <div>
    <nuxt-link :to="'/dashboard/activities/' + $route.params.id + '/versions'" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux versions</nuxt-link>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Sélections pour "{{ activity.name }}" ({{ version.name }})</v-toolbar-title>
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
                        v-model="editedItem.text"
                        v-validate="'required'"
                        :error-messages="errors.collect('text')"
                        label="Text *"
                        data-vv-name="text"
                        required></v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-textarea
                        v-model="editedItem.description"
                        label="Description"
                        required></v-textarea>
                    </v-flex>
                    <v-flex xs12>
                      <label class="fake-label">Image</label>
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
                        :src="tmpImg"></clipper-basic>
                    </v-flex>
                    <v-flex v-show="tmpImg" xs12 md6>
                      <clipper-preview name="my-preview" class="my-clipper"></clipper-preview>
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
          <td>{{ props.item.text }}</td>
          <td><img class="table-logo" :src="props.item.img_path"></td>
          <td class="justify-center">
            <v-btn round depressed small color="primary" @click="$router.push('/dashboard/activities/' + props.item.activity_id + '/versions/' + props.item.version_id + '/selections/' + props.item.id + '/choices')"><i class="fa fa-bullseye"></i> Choix</v-btn>
            <v-btn v-if="activity.num === 6" round depressed small color="warning" @click="$router.push('/dashboard/activities/' + props.item.activity_id + '/versions/' + props.item.version_id + '/selections/' + props.item.id + '/selectors')"><i class="fa fa-question-circle"></i> Questions</v-btn>
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
    name: 'DashboardActivitySelections',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    components: { ConfirmDelete },
    routePerimeterAction: 'accessDashboard',
    $_veeValidate: {
      validator: 'new'
    },
    validate ({ params }) {
      return utils.testUuid(params.id)
    },
    async asyncData({ app, route }) {
      try {
        let request = await app.$api.activities.getByIds([route.params.id])
        const activity = request.data[0] || {}

        request = await app.$api.activities.getVersionByIds([route.params.versionId])
        const version = request.data[0] || {}

        return  { activity, version }
      } catch (e) {
        console.log(e)
      }
    },
    data () {
      return {
        items: [],
        formData: null,
        editedItem: {
          name: '',
          activity_id: null,
          version_id: null,
          img_path: ''
        },
        editedIndex: -1,
        defaultItem: {
          name: '',
          activity_id: null,
          version_id: null,
          img_path: ''
        },
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
            text: 'Nom',
            align: 'left',
            sortable: false,
            value: 'name'
          },
          {
            text: 'Image',
            align: 'left',
            sortable: false,
            value: 'name'
          },
          { text: 'Actions', sortable: false }
        ],
        deleteDialog: false,
        tmpImg: null,
        resultURL: null
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer une selection' : 'Éditer une selection'
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
    created() {
      this.defaultItem.activity_id = this.$route.params.id
      this.editedItem.activity_id = this.$route.params.id
      this.defaultItem.version_id = this.$route.params.versionId
      this.editedItem.version_id = this.$route.params.versionId
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
            let request = await this.$api.activities.countSelections({
              activity_ids: [this.$route.params.id],
              version_ids: [this.$route.params.versionId]
            })
            const count = request.data.res || 0

            request = await this.$api.activities.getSelections({
              activity_ids: [this.$route.params.id],
              version_ids: [this.$route.params.versionId],
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
        this.dialog = true
      },

      close () {
        this.dialog = false
        this.$data.tmpImg = null
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
          await this.$api.activities.dropSelections([item.id])
          this.getRows().then(this.bindData)
        } catch (e) {
          console.log(e)
        }
      },

      async save () {
        this.$validator.validate().then(async valid => {
          if (valid) {
            if (this.editedIndex > -1) {
              //EDIT
              try {
                await this.$api.activities.updateSelection(this.editedItem)

                if (this.tmpImg) {
                  const file = utils.dataURItoFormData(this.getImgResult(), this.editedItem.id + '_' + new Date().getTime() + '.' + this.getExtension(this.$refs.upload.file.name))
                  await this.$api.activities.uploadSelectionLogo(this.editedItem.id, file)
                }

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            } else {
              //CREATE
              try {
                this.editedItem.activity_id = this.$route.params.id
                this.editedItem.version_id = this.$route.params.versionId
                let response = await this.$api.activities.createSelection(this.editedItem)
                if (this.tmpImg) {
                  const id = response.data.res.id || ''
                  const file = utils.dataURItoFormData(this.getImgResult(), id + '_' + new Date().getTime() + '.' + this.getExtension(this.$refs.upload.file.name))
                  await this.$api.activities.uploadSelectionLogo(id, file)
                }

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            }
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
      }
    }
  }
</script>
<style lang="scss">
  .btn-modified {
    margin-bottom: 1em;
  }

  .img-preview, .table-logo {
    background-color: $main-color;
  }
</style>

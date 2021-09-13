<template>
  <div>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Domaines</v-toolbar-title>
        <v-divider
          class="mx-2"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-select
          v-model="searchType"
          label="Chercher par domaine"
          :items="selectDomainTypes"
          clearable
          attach
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
                      <v-textarea
                        v-model="editedItem.description"
                        :error-messages="errors.collect('description')"
                        label="Description"
                        data-vv-name="description"
                        required></v-textarea>
                    </v-flex>
                    <v-flex xs12>
                      <v-select
                        v-model="editedItem.kind"
                        :items="selectDomainTypes"
                        v-validate="'required'"
                        :error-messages="errors.collect('kind')"
                        label="Type *"
                        data-vv-name="kind"
                        required
                      ></v-select>
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
          <td>{{ props.item.name }}</td>
          <td>{{ domainTypes[props.item.kind] }}</td>
          <td>
            <v-btn round depressed small color="primary" @click="$router.push('/dashboard/domains/' + props.item.id + '/details')"><i class="fa fa-bullseye"></i> Details</v-btn>
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
  import ConfirmDelete from '~/components/ConfirmDelete'
  import { DOMAIN_TYPES } from '~/config'
  import utils from '~/assets/js/utils'

  export default {
    name: 'DashboardDomains',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    components: { ConfirmDelete },
    $_veeValidate: {
      validator: 'new'
    },
    data () {
      return {
        items: [],
        formData: null,
        editedItem: {
          name: '',
          description: '',
          kind: '',
          img_path: ''
        },
        editedIndex: -1,
        defaultItem: {
          name: '',
          description: '',
          kind: '',
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
            value: 'text'
          },
          {
            text: 'Type',
            align: 'left',
            sortable: false,
            value: 'text'
          },
          { text: 'Actions', sortable: false }
        ],
        domainTypes: DOMAIN_TYPES,
        selectDomainTypes: [{text: DOMAIN_TYPES['activity-area'], value: 'activity-area'}, {text: DOMAIN_TYPES['action-verb'], value: 'action-verb'}, {text: DOMAIN_TYPES['stream'], value: 'stream'}],
        tmpImg: null,
        resultURL: null,
        searchType: undefined
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer un domaine' : 'Éditer un domaine'
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
            let request = await this.$api.domains.count({
              kind: this.searchType ? this.searchType : undefined
            })
            const count = request.data.res || 0

            request = await this.$api.domains.getList({
              page: this.pagination.page,
              numberPerPage: this.pagination.rowsPerPage,
              kind: this.searchType ? this.searchType : undefined
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
          await this.$api.domains.drop([item.id])
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
                await this.$api.domains.update(this.editedItem)

                if (this.tmpImg) {
                  const file = utils.dataURItoFormData(this.getImgResult(), this.editedItem.id + '_' + new Date().getTime() + '.' + this.getExtension(this.$refs.upload.file.name))
                  await this.$api.domains.uploadLogo(this.editedItem.id, file)
                }

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {

              }
            } else {
              //CREATE
              try {
                let response = await this.$api.domains.create(this.editedItem)

                if (this.tmpImg) {
                  const id = response.data.res.id || ''
                  const file = utils.dataURItoFormData(this.getImgResult(), id + '_' + new Date().getTime() + '.' + this.getExtension(this.$refs.upload.file.name))
                  await this.$api.domains.uploadLogo(id, file)
                }

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e.response)
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

</style>

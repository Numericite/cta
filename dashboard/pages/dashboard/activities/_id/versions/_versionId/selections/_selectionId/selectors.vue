<template>
  <div>
    <nuxt-link :to="'/dashboard/activities/' + $route.params.id + '/versions/' + $route.params.versionId + '/selections'" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux selections</nuxt-link>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Questions pour "{{ selection.text }}" ({{ activity.name }} / {{ version.name }})</v-toolbar-title>
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
                    <v-flex xs3>
                      <v-text-field
                        type="number"
                        v-model="editedItem.num"
                        v-validate="'required'"
                        :error-messages="errors.collect('num')"
                        label="Numéro *"
                        data-vv-name="num"
                        required></v-text-field>
                    </v-flex>
                    <v-flex xs9>
                      <v-text-field
                        v-model="editedItem.text"
                        v-validate="'required'"
                        :error-messages="errors.collect('text')"
                        label="Text *"
                        data-vv-name="text"
                        required></v-text-field>
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
          <td>{{ props.item.num }}</td>
          <td>{{ props.item.text }}</td>
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
    name: 'DashboardActivitySelectionSelectors',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    components: { ConfirmDelete },
    routePerimeterAction: 'accessDashboard',
    $_veeValidate: {
      validator: 'new'
    },
    validate ({ params }) {
      return utils.testUuid(params.selectionId)
    },
    async asyncData({ app, route }) {
      try {
        let request = await app.$api.activities.getByIds([route.params.id])
        const activity = request.data[0] || {}

        request = await app.$api.activities.getVersionByIds([route.params.versionId])
        const version = request.data[0] || {}

        request = await app.$api.activities.getSelectionByIds([route.params.selectionId])
        const selection = request.data[0] || {}

        return  { activity, version, selection }
      } catch (e) {
        console.log(e)
      }
    },
    data () {
      return {
        items: [],
        formData: null,
        editedItem: {
          text: '',
          domain_ids: []
        },
        editedIndex: -1,
        defaultItem: {
          text: '',
          domain_ids: []
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
            text: 'Numéro',
            align: 'left',
            sortable: false
          },
          {
            text: 'Nom',
            align: 'left',
            sortable: false
          },
          { text: 'Actions', sortable: false }
        ],
        deleteDialog: false
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer une question' : 'Éditer une question'
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
      this.defaultItem.selection_id = this.$route.params.selectionId
      this.editedItem.selection_id = this.$route.params.selectionId
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
            let request = await this.$api.activities.countSelectors([this.$route.params.selectionId])
            const count = request.data.res || 0

            request = await this.$api.activities.getSelectors({
              selection_ids: [this.$route.params.selectionId],
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
          await this.$api.activities.dropSelectors([item.id])
          this.getRows().then(this.bindData)
        } catch (e) {
          console.log(e)
        }
      },

      async save () {
        this.$validator.validate().then(async valid => {
          if (valid) {
            this.editedItem.num = parseInt(this.editedItem.num)
            if (this.editedIndex > -1) {
              //EDIT
              try {
                await this.$api.activities.updateSelector(this.editedItem)

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            } else {
              //CREATE
              try {
                await this.$api.activities.createSelector(this.editedItem)

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            }
          }
        })
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

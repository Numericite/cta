<template>
  <div>
    <nuxt-link to="/dashboard/activities" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux activités</nuxt-link>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Versions pour "{{ activity.name }}"</v-toolbar-title>
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
                        v-model="editedItem.name"
                        v-validate="'required'"
                        :error-messages="errors.collect('name')"
                        label="Nom *"
                        data-vv-name="name"
                        required></v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-select
                        :items="versions"
                        item-value="id"
                        item-text="name"
                        v-model="editedItem.model_id"
                        label="Modèle"
                      ></v-select>
                    </v-flex>
                    <v-flex xs12>
                      <v-switch
                        v-model="editedItem.isDefault"
                        v-validate="'required'"
                        :error-messages="errors.collect('isDefault')"
                        label="Version par défaut"
                        data-vv-name="isDefault"
                        required>
                      </v-switch>
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
          <td v-if="props.item.isDefault"><i class="fa fa-check fa-highlight"></i></td>
          <td v-if="!props.item.isDefault"><i class="fa fa-times fa-highlight"></i></td>
          <td class="justify-center">
            <v-btn round depressed small color="primary" @click="$router.push('/dashboard/activities/' + props.item.parent_id + '/versions/' + props.item.id + '/selections')"><i class="fa fa-bullseye"></i> Sélections</v-btn>
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
    name: 'DashboardActivityVersions',
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

        request = await app.$api.activities.getVersions({page: 1, numberPerPage: 1000, parent_ids: activity.id})
        const versions = request.data || []
        
        return  { activity, versions }
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
          parent_id: null,
          kind: 'activity',
          isDefault: false,
          model_id: null
        },
        editedIndex: -1,
        defaultItem: {
          name: '',
          parent_id: null,
          kind: 'activity',
          isDefault: false,
          model_id: null
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
            text: 'Défaut',
            align: 'left',
            sortable: false,
            value: 'isDefault'
          },
          { text: 'Actions', sortable: false }
        ],
        deleteDialog: false,
        resultURL: null
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer une version' : 'Éditer une version'
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
      this.defaultItem.parent_id = this.$route.params.id
      this.editedItem.parent_id = this.$route.params.id
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
            let request = await this.$api.activities.countVersions([this.$route.params.id])
            const count = request.data.res || 0

            request = await this.$api.activities.getVersions({
              parent_ids: [this.$route.params.id],
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
          await this.$api.activities.dropVersions([item.id])
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
                await this.$api.activities.updateVersion(this.editedItem)

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            } else {
              //CREATE
              try {
                this.editedItem.parent_id = this.$route.params.id
                let request = await this.$api.activities.createVersion(this.editedItem)
                const version_id = request.data.res.id

                if (this.editedItem.model_id) {
                  await this.createDataFromModel(this.editedItem.model_id, version_id)
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

      async createDataFromModel(model_id, version_id) {
        let request = await this.$api.activities.getSelections({
          activity_ids: [this.$route.params.id],
          version_ids: [model_id],
          page: 1,
          numberPerPage: 1000
        })
        const selections = request.data || []

        const selection_ids = _.map(selections, 'id')

        request = await this.$api.activities.getChoices({
          selection_ids: [selection_ids],
          page: 1,
          numberPerPage: 1000
        })
        const choices = request.data || [];

        request = await this.$api.activities.getSelectors({
          selection_ids: [selection_ids],
          page: 1,
          numberPerPage: 1000
        })
        const selectors = request.data || []

        _.forEach(selections, async (selection) => {
          selection.version_id = version_id
          request = await this.$api.activities.createSelection(selection)
          const selection_id = request.data.res.id

          const selection_choices = _.filter(choices, {selection_id: selection.id})
          _.forEach(selection_choices, async (choice) => {
            choice.selection_id = selection_id
            await this.$api.activities.createChoice(choice)
          })

          const selection_selectors = _.filter(selectors, {selection_id: selection.id})
          _.forEach(selection_selectors, async (selector) => {
            selector.selection_id = selection_id
            await this.$api.activities.createSelector(selector)
          })
        })
      }
    }
  }
</script>
<style lang="scss">
  .btn-modified {
    margin-bottom: 1em;
  }

</style>

<template>
  <div>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Réunions d'informaton</v-toolbar-title>
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
                        v-model="editedItem.kind"
                        :items="meetingKinds"
                        v-validate="'required'"
                        :error-messages="errors.collect('kind')"
                        label="Type *"
                        data-vv-name="kind"
                        required
                      ></v-select>
                    </v-flex>
                    <v-flex v-if="editedItem.kind === 'school'" xs12>
                      <v-select
                        v-model="editedItem.school_id"
                        :items="schools"
                        item-value="id"
                        item-text="name"
                        v-validate="'required'"
                        :error-messages="errors.collect('school')"
                        label="École *"
                        data-vv-name="school"
                        required
                      ></v-select>
                    </v-flex>
                    <v-flex v-if="editedItem.kind === 'school'" xs12>
                      <v-text-field
                        type="number"
                        v-model.number="editedItem.nb_parent_informed"
                        v-validate="'required|decimal'"
                        :error-messages="errors.collect('nbParentInformed')"
                        label="Nombre de parents d'élève informés *"
                        data-vv-name="nbParentInformed"
                        required></v-text-field>
                    </v-flex>
                    <v-flex v-if="editedItem.kind === 'structure'" xs12>
                      <v-text-field
                        type="number"
                        v-model.number="editedItem.nb_structure_aware"
                        v-validate="'required|decimal'"
                        :error-messages="errors.collect('nbStructureAware')"
                        label="Nombre de structures jeunesses sensibilisées *"
                        data-vv-name="nbStructureAware"
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
          <td>{{ getKindName(props.item.kind) }}</td>
          <td>
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
  import { MEETING_KINDS } from '~/config'
  import _ from 'lodash'

  export default {
    name: 'DashboardMeeting',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    components: { ConfirmDelete },
    $_veeValidate: {
      validator: 'new'
    },
    async asyncData({app}) {
      try {
        let response = await app.$api.schools.getList({page: 1, numberPerPage: 1000})
        const schools = response.data || []

        return { schools }
      } catch (e) {
        console.log(e)
      }
    },
    data () {
      return {
        items: [],
        schools: [],
        formData: null,
        editedItem: {
          name: '',
          description: ''
        },
        editedIndex: -1,
        defaultItem: {
          name: '',
          description: ''
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
        meetingKinds: MEETING_KINDS
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer une réunion d\'informaton' : 'Éditer une réunion d\'informaton'
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
            let request = await this.$api.meetings.count()
            const count = request.data.res || 0

            request = await this.$api.meetings.getList({
              page: this.pagination.page,
              numberPerPage: this.pagination.rowsPerPage
            })
            const items = request.data || []

            items.forEach((item) => {
              item.school = _.find(this.schools, {id: item.school_id}) || {}
            })

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
          await this.$api.meetings.drop([item.id])
          this.getRows().then(this.bindData)
        } catch (e) {
          console.log(e)
        }
      },

      async save () {
        this.$validator.validate().then(async valid => {
          if (valid) {

            if (this.editedItem.kind === 'school') {
              delete this.editedItem.nb_structure_aware
            } else if (this.editedItem.kind === 'structure') {
              delete this.editedItem.nb_parent_informed
              delete this.editedItem.school_id
            }

            if (this.editedIndex > -1) {
              //EDIT
              try {
                await this.$api.meetings.update(this.editedItem)

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {

              }
            } else {
              //CREATE
              try {
                await this.$api.meetings.create(this.editedItem)

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

      getKindName(kind) {
        return _.get(_.find(MEETING_KINDS, {value: kind}), 'text', '')
      }
    }
  }
</script>
<style lang="scss">

</style>

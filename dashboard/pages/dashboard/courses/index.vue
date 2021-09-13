<template>
  <div>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Parcours</v-toolbar-title>
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
                        label="Course *"
                        data-vv-name="name"
                        required></v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-select
                        :items="versionNames"
                        v-model="editedItem.text_version_name"
                        label="Version de texte"
                      ></v-select>
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
          <td><nuxt-link :to="'/dashboard/courses/' + props.item.id + '/chapters'">{{ props.item.name }}</nuxt-link></td>
          <td>{{ props.item.text_version_name }}</td>
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
  import { COURSE_DEFAULT_CHAPTERS } from '~/config'

  export default {
    name: 'DashboardCourses',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    components: { ConfirmDelete },
    $_veeValidate: {
      validator: 'new'
    },
    async asyncData({ app }) {
      try {
        let request = await app.$api.versions.getAvailableNames()
        const versionNames = request.data || []

        return  { versionNames }
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
          chapters: COURSE_DEFAULT_CHAPTERS
        },
        editedIndex: -1,
        defaultItem: {
          name: '',
          chapters: COURSE_DEFAULT_CHAPTERS
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
            text: 'Version de texte',
            align: 'left',
            sortable: false,
            value: 'text'
          },
          { text: 'Actions', sortable: false }
        ]
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer un parcours' : 'Éditer un parcours'
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
            let request = await this.$api.courses.count()
            const count = request.data.res || 0

            request = await this.$api.courses.getList({
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
          await this.$api.courses.drop([item.id])
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
                await this.$api.courses.update(this.editedItem)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {

              }
            } else {
              //CREATE
              try {
                await this.$api.courses.create(this.editedItem)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e.response)
              }
            }
          }
        })
      },
    }
  }
</script>
<style lang="scss">

</style>

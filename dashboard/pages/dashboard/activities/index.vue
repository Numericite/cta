<template>
<div>
  <v-app>
    <v-toolbar flat color="black">
      <v-toolbar-title class="numericite-toolbar-title">Activités</v-toolbar-title>
      <v-divider
        class="mx-2"
        inset
        vertical
      ></v-divider>
      <v-spacer></v-spacer>
      <v-dialog v-model="dialog" max-width="500px" persistent>
        <!--<template v-slot:activator="{ on }">-->
          <!--<v-btn color="primary" dark class="mb-2" v-on="on">Créer</v-btn>-->
        <!--</template>-->
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
                  <v-flex xs12 sm4>
                    <v-text-field
                      v-model="editedItem.num"
                      type="number"
                      v-validate="'required'"
                      :error-messages="errors.collect('num')"
                      label="Numéro *"
                      data-vv-name="num"
                      required></v-text-field>
                  </v-flex>
                  <v-flex xs12 sm8>
                    <v-text-field
                      v-model="editedItem.slug"
                      v-validate="'required'"
                      :error-messages="errors.collect('slug')"
                      label="Slug *"
                      data-vv-name="slug"
                      required></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-textarea
                      v-model="editedItem.desc"
                      v-validate="'required'"
                      :error-messages="errors.collect('desc')"
                      label="Description *"
                      data-vv-name="desc"
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
        <td>{{ props.item.num }}</td>
        <td>{{ props.item.name }}</td>
        <td>
          <v-btn round depressed small color="primary" @click="$router.push('/dashboard/activities/' + props.item.id + '/versions')"><i class="fa fa-file"></i> Versions</v-btn>
          <v-btn round depressed small @click="editItem(props.item)">
            <v-icon
              small
            >
              edit
            </v-icon>
          </v-btn>
        </td>
      </template>
    </v-data-table>
  </v-app>
</div>
</template>
<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

  export default {
    name: 'DashboardActivities',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
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
          slug: '',
          num: null,
          desc: ''
        },
        editedIndex: -1,
        defaultItem: {
          name: '',
          slug: '',
          num: null,
          desc: ''
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
            sortable: false,
            value: 'num'
          },
          {
            text: 'Nom',
            align: 'left',
            sortable: false,
            value: 'name'
          },
          { text: 'Actions', sortable: false }
        ]
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer une activité' : 'Éditer une activité'
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
            let request = await this.$api.activities.count()
            const count = request.data.res || 0

            request = await this.$api.activities.getList({
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

      async save () {
        this.$validator.validate().then(async valid => {
          if (valid) {
            if (this.editedIndex > -1) {
              //EDIT
              try {
                this.editedItem.num = parseInt(this.editedItem.num)
                await this.$api.activities.update(this.editedItem)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {

              }
            } else {
              //CREATE
              try {
                this.editedItem.num = parseInt(this.editedItem.num)
                await this.$api.activities.create(this.editedItem)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e.response)
              }
            }
          }
        })
      }
    }
  }
</script>
<style lang="scss">

</style>

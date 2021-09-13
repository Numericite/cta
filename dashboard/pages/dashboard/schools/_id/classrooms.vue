<template>
  <div>
    <nuxt-link to="/dashboard/schools" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux établissements</nuxt-link>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Classes pour l'établissement "{{ school.name }}"</v-toolbar-title>
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
                    <v-flex xs12 d-flex>
                      <v-select
                        v-model="editedItem.grade"
                        :items="grades"
                        v-validate="'required'"
                        :error-messages="errors.collect('grade')"
                        label="Grade *"
                        data-vv-name="grade"
                        required
                      ></v-select>
                    </v-flex>
                    <v-flex xs12>
                      <v-select
                        v-model="editedItem.course_id"
                        :items="courses"
                        item-text="name"
                        item-value="id"
                        label="Parcours"
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
          <td>{{ props.item.name }}</td>
          <td>{{ props.item.grade_display }}</td>
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
  import _ from 'lodash'
  import {STUDENT_GRADES} from "../../../../config";

  export default {
    name: 'DashboardSchoolClassrooms',
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
        let request = await app.$api.schools.getByIds([route.params.id])
        const school = request.data[0] || {}

        request = await app.$api.courses.getList({page: 1, numberPerPage: 1000})
        const courses = request.data

        return  { school, courses }
      } catch (e) {
        console.log(e)
      }
    },
    data () {
      return {
        items: [],
        formData: null,
        courses: [],
        editedItem: {
          name: '',
          grade: null
        },
        editedIndex: -1,
        defaultItem: {
          name: '',
          grade: null
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
            text: 'Grade',
            align: 'left',
            sortable: false,
            value: 'name'
          },
          { text: 'Actions', sortable: false }
        ],
        grades: STUDENT_GRADES,
        deleteDialog: false
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer une classe' : 'Éditer une classe'
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
      this.defaultItem.school_id = this.$route.params.id
      this.editedItem.school_id = this.$route.params.id
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
            let request = await this.$api.schools.countClassrooms([this.$route.params.id])
            const count = request.data.res || 0

            request = await this.$api.schools.getClassrooms({
              school_ids: [this.$route.params.id],
              page: this.pagination.page,
              numberPerPage: this.pagination.rowsPerPage
            })

            const items = request.data || []

            const grades = this.grades
            items.forEach(function (item) {
              item.grade_display = _.get(_.find(grades, {value: item.grade}), 'text', '')
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
          await this.$api.schools.dropClassrooms([item.id])
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
                await this.$api.schools.updateClassroom(this.editedItem)

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            } else {
              //CREATE
              try {
                this.editedItem.school_id = this.$route.params.id
                await this.$api.schools.createClassroom(this.editedItem)

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

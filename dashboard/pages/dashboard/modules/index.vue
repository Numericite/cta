<template>
  <div>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Modules</v-toolbar-title>
        <v-divider
          class="mx-2"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-select
          v-model="searchType"
          label="Chercher par niveau"
          :items="grades"
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
                      <v-textarea
                        v-model="editedItem.description"
                        v-validate="'required'"
                        :error-messages="errors.collect('desc')"
                        label="Description *"
                        data-vv-name="desc"
                        required></v-textarea>
                    </v-flex>
                    <v-flex xs12>
                      <v-textarea
                        v-model="editedItem.help"
                        :error-messages="errors.collect('help')"
                        label="Zone d'aide"
                        data-vv-name="help"></v-textarea>
                    </v-flex>
                    <v-flex xs12 d-flex>
                      <v-select
                        v-model="editedItem.grade"
                        @change="delete editedItem.school_type"
                        :items="grades"
                        v-validate="'required'"
                        :error-messages="errors.collect('grade')"
                        label="Grade *"
                        data-vv-name="grade"
                        required
                      ></v-select>
                    </v-flex>
                    <v-flex v-if="editedItem.grade >= 0 && editedItem.grade <= 2" xs12 d-flex>
                      <v-select
                        v-model="editedItem.school_type"
                        :items="school_types"
                        v-validate="'required'"
                        :error-messages="errors.collect('school_type')"
                        label="Type d'établissement *"
                        data-vv-name="school_type"
                        required
                      ></v-select>
                    </v-flex>
                    <v-flex>
                      <v-autocomplete :items="explorationTypes"
                                      v-model="editedItem.exploration_type_ids"
                                      chips
                                      multiple
                                      item-value="id"
                                      item-text="name"
                                      :wait="0"
                                      label="Explorations proposées en fin de module"
                                      data-vv-name="explorationTypes"
                                      :error-messages="errors.collect('explorationTypes')"></v-autocomplete>
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
        <template slot="items" slot-scope="props">
          <tr class="sortableRow" :key="props.item.id">
            <td class="dragButton">
              <v-btn class="sortHandle" :ripple="false" flat><v-icon small> drag_indicator </v-icon>{{ props.item.num }}</v-btn>
            </td>
            <td>{{ props.item.name }}</td>
            <td>{{ props.item.grade_display }}</td>
            <td>
              <v-btn round depressed small color="primary" @click="$router.push('/dashboard/modules/' + props.item.id + '/activities')"><i class="ti-agenda mr-1"></i> Activités</v-btn>
              <v-btn round depressed small color="primary" @click="$router.push('/dashboard/modules/' + props.item.id + '/form')"><i class="fa fa-file"></i> Formulaire</v-btn>
              <v-btn round depressed small @click="editItem(props.item)">
                <v-icon
                  small
                >
                  edit
                </v-icon>
              </v-btn>
              <confirm-delete @confirmedClick="deleteItem(props.item)"></confirm-delete>
            </td>
          </tr>
        </template>
      </v-data-table>
    </v-app>
  </div>
</template>

<script>

import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import ConfirmDelete from '~/components/ConfirmDelete'
import {STUDENT_GRADES, SCHOOL_KINDS} from "../../../config";
import Sortable from 'sortablejs'

export default {
  name: 'DashboardModules',
  layout: "dashboard",
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessDashboard',
  components: { ConfirmDelete },
  $_veeValidate: {
    validator: 'new'
  },
  async asyncData({app, store}) {
    try {
      let response = await app.$api.explorations.getList({page: 1, numberPerPage: 1000})
      const explorationTypes = response.data || []

      return { explorationTypes }
    } catch (e) {
      console.log(e)
    }
  },
  data() {
    return {
      items: [],
      itemsCopy: [],
      explorationTypes: [],
      formData: null,
      searchInput: '',
      searchType: '',
      editedItem: {
        name: '',
        description: '',
      },
      editedIndex: -1,
      defaultItem: {
        name: '',
        description: '',
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
        {
          text: 'Grade',
          align: 'left',
          sortable: false,
          value: 'grade'
        },
        { text: 'Actions', sortable: false }
      ],
      grades: STUDENT_GRADES,
      school_types: SCHOOL_KINDS.filter(school => school.value === 'high-school-new' || school.value === 'high-school-pro-new')
    }
  },
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'Créer un module' : 'Éditer un module'
    },
    dragOptions() {
      return {
        animation: 250,
        group: "description",
        disabled: false,
        ghostClass: "ghost"
      }
    },
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

    new Sortable(
      document.querySelector(".v-datatable tbody"),
      {
        animation: 250,
        group: 'description',
        disabled: false,
        ghostClass: 'ghost',
        draggable: '.sortableRow',
        handle: '.sortHandle',
        onEnd: this.dragReorder
      }
    )

    this.getRows().then((data) => {
      this.firstLoad = false
      this.bindData(data)
    })
  },
  methods: {
    dragReorder ({oldIndex, newIndex}) {
      const movedItem = this.items.splice(oldIndex, 1)[0]
      this.items.splice(newIndex, 0, movedItem)
      this.updateNum()
    },

    updateNum () {
      let pageIndexHandler = ((this.pagination.page - 1 ) * this.pagination.rowsPerPage)
      this.items.map( (item, index) => item.num = index + 1 + pageIndexHandler)

      this.items.map( async item => {
        if (!_.isEqual(item, _.find(this.itemsCopy, { id: item.id }))) {
          //EDIT
          try {
            await this.$api.modules.update(item)
          } catch (e) {

          }
        }
      })

      if (!_.isEqual(this.items, this.itemsCopy))
        this.getRows().then(this.bindData)
    },

    bindData(data) {
      this.total = data.count || 0
      this.items = data.items || []
      this.itemsCopy = JSON.parse(JSON.stringify(data.items || []))
      this.loading = false
    },

    async getRows() {
      this.loading = true;
      return new Promise(async (resolve, reject) => {
        try {
          let request = await this.$api.modules.count({
            grade: this.searchType ? this.searchType : undefined
          })
          const count = request.data.res || 0

          request = await this.$api.modules.getList({
            page: this.pagination.page,
            numberPerPage: this.pagination.rowsPerPage,
            grade: this.searchType ? this.searchType : undefined
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
        await this.$api.modules.drop([item.id])
        this.items = this.items.filter(module => module.id !== item.id)
        this.updateNum()

        let request = await this.$api.fields.getList({ parent_id: item.id,  page: 1, numberPerPage: 100 })
        let fieldsIdsToRemove = request.data.map(item => item.id)
        if (fieldsIdsToRemove)
          await this.$api.fields.drop(fieldsIdsToRemove)

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
              await this.$api.modules.update(this.editedItem)
              this.getRows().then(this.bindData)
              this.close()
            } catch (e) {

            }
          } else {
            //CREATE
            try {
              this.editedItem.num = this.total + 1
              await this.$api.modules.create(this.editedItem)
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

<style lang="scss" scoped>

.dragButton {
  .v-btn {
    margin-left: 0;
    padding: 0;
  }

  .v-btn:hover:before, .v-btn:focus:before {
    background-color: transparent !important;
  }
}

.ghost {
  opacity: 0.4;
  background: #d79d84;
  border-radius: 5px;
}

</style>

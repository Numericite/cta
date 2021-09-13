<template>
  <div>
    <nuxt-link to="/dashboard/modules" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux modules</nuxt-link>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Activités pour "{{ module.name }}"</v-toolbar-title>
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
                      <v-textarea
                        v-model="editedItem.description"
                        :error-messages="errors.collect('desc')"
                        label="Description "
                        data-vv-name="desc"
                        required></v-textarea>
                    </v-flex>
                    <v-flex xs12>
                      <v-text-field
                        label="Document *"
                        v-validate="'required'"
                        :error-messages="errors.collect('document')"
                        @click="pickDocument"
                        placeholder="Cliquez pour sélectionner un document..."
                        v-model="editedItem.file_name"
                        data-vv-name="document"></v-text-field>
                      <input
                        type="file"
                        style="display: none"
                        ref="document"
                        accept="application/pdf"
                        @change="onDocumentPicked"
                      >
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
            <td v-if="props.item.status_name === 'open'"><i class="fa fa-check fa-highlight"></i></td>
            <td v-if="props.item.status_name === 'standby'"><i class="fa fa-times fa-highlight"></i></td>
            <td class="justify-center">
              <v-btn round depressed small color="primary" @click="$router.push('/dashboard/modules/' + $route.params.id + /activities/ + props.item.id + '/documents')"><i class="ti-files mx-1"></i> Documents</v-btn>
              <v-btn round depressed small color="primary" @click="$router.push('/dashboard/modules/' + $route.params.id + /activities/ + props.item.id + '/form')"><i class="fa fa-file"></i> Formulaire</v-btn>
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
  import utils from '~/assets/js/utils'
  import ConfirmDelete from '~/components/ConfirmDelete'
  import Sortable from "sortablejs";

  export default {
    name: 'DashboardModuleActivities',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    components: { ConfirmDelete },
    routePerimeterAction: 'accessDashboard',
    $_veeValidate: {
      validator: 'new'
    },
    async asyncData({ app, route }) {
      try {
        let requestModule = await app.$api.modules.getByIds([route.params.id])
        const module = requestModule.data[0] || {}

        let requestModuleForm = await app.$api.fields.getByIds()
        const moduleForm = requestModuleForm.data[0] || {}

        return  { module, moduleForm }
      } catch (e) {
        console.log(e)
      }
    },
    data () {
      return {
        items: [],
        itemsCopy: [],
        loading: true,
        firstLoad: true,
        formData: null,
        editedItem: {
          name: '',
          description: '',
          file_name: '',
        },
        editedIndex: -1,
        defaultItem: {
          name: '',
          description: '',
          file_name: '',
        },
        dialog: false,
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
        ],
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
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer une activité' : 'Éditer une activité'
      },
      itemHasId () {
        return this.editedItem.id !== undefined
      },
      dragOptions() {
        return {
          animation: 250,
          group: "description",
          disabled: false,
          ghostClass: "ghost"
        }
      }
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
              await this.$api.moduleActivities.update(item)
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
            let request = await this.$api.moduleActivities.count({ module_ids: [this.$route.params.id] })
            const count = request.data.res || 0

            request = await this.$api.moduleActivities.getList({
              module_ids: [this.$route.params.id],
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
        if (!this.editedItem.file_name) this.editedItem.file_name = item.path
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
          await this.$api.moduleActivities.drop([item.id])
          this.items = this.items.filter(moduleActivity => moduleActivity.id !== item.id)
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
            this.editedItem.module_id = this.$route.params.id
            this.editedItem.parent_type = 'moduleActivityDocument'
            if (this.editedIndex > -1) {
              //EDIT
              try {
                await this.$api.moduleActivities.update(this.editedItem)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            } else {
              //CREATE
              try {
                this.editedItem.num = this.total + 1
                let requestModuleActivity = await this.$api.moduleActivities.create(this.editedItem)
                await this.$api.moduleActivities.uploadDocument(requestModuleActivity.data.res.id, this.editedItem.file)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e)
              }
            }
          }
        })
      },

      pickDocument() {
        this.$refs.document.click()
      },

      onDocumentPicked(e) {
        const files = e.target.files
        const formData = new FormData()
        if(files[0] !== undefined) {
          this.editedItem.file_name = files[0].name
          formData.append('file', files[0], utils.toValidFileName(files[0].name))
          formData.append('user_id', this.$store.state.auth.user.userID)
          formData.append('description', this.editedItem.description)
          formData.append('file_name', this.editedItem.file_name)
          formData.append('title', this.editedItem.name)
          this.editedItem.file = formData
        } else {
          this.editedItem.file_name = ''
        }
      },

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

<template>
  <div>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Explorations</v-toolbar-title>
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
                        v-model="editedItem.partner_slug"
                        item-text="name"
                        item-value="slug"
                        :items="partners"
                        :error-messages="errors.collect('partner_slug')"
                        label="Partenaire"
                        data-vv-name="slug"></v-select>
                    </v-flex>
                    <v-flex xs12>
                      <v-textarea
                        v-model="editedItem.description"
                        :error-messages="errors.collect('description')"
                        label="Description"
                        data-vv-name="description"></v-textarea>
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
            <td v-if="props.item.partner_slug && props.item.partner_slug.length > 0"> {{ partnerTitle(props.item.partner_slug) }}</td>
            <td v-if="!props.item.partner_slug"><i class="fa fa-times fa-highlight"></i></td>
            <td>
              <v-btn round depressed small color="primary" @click="$router.push('/dashboard/explorations/' + props.item.id + '/documents/')"><i class="ti-files mx-1"></i> Documents</v-btn>
              <v-btn round depressed small color="primary" @click="$router.push('/dashboard/explorations/' + props.item.id + '/form')"><i class="fa fa-file"></i> Formulaire</v-btn>
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
  import Sortable from 'sortablejs'
  import _ from 'lodash'
  import utils from '~/assets/js/utils'

  export default {
    name: 'DashboardExplorations',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    components: { ConfirmDelete },
    $_veeValidate: {
      validator: 'new'
    },
    async asyncData({app}) {
      try {
        let response = await app.$api.partners.getList({page: 1, numberPerPage: 1000})
        const partners = response.data || []

        return { partners }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        items: [],
        itemsCopy: [],
        formData: null,
        editedItem: {
          name: '',
          partner_slug: null,
          description: '',
        },
        editedIndex: -1,
        defaultItem: {
          name: '',
          partner_slug: null,
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
            value: 'text'
          },
          {
            text: 'Partenaire',
            align: 'left',
            sortable: false
          },
          { text: 'Actions', sortable: false }
        ],
        partners: [],
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer une exploration' : 'Éditer une exploration'
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
              await this.$api.explorations.update(item)
            } catch (e) {

            }
          }
        })

        if (!_.isEqual(this.items, this.itemsCopy))
          this.getRows().then(this.bindData)
      },

      partnerTitle (partner_slug) {
        return _.get(_.find(this.partners, {slug: partner_slug}), 'name', '')
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
            let request = await this.$api.explorations.count()
            const count = request.data.res || 0

            request = await this.$api.explorations.getList({
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
          await this.$api.explorations.drop([item.id])
          this.items = this.items.filter(exploration => exploration.id !== item.id)
          this.updateNum()

          let request = await this.$api.fields.getList({ parent_id: item.id,  page: 1, numberPerPage: 100 })
          let fieldsIdsToRemove = request.data.map(item => item.id)
          if (fieldsIdsToRemove)
            await this.$api.fields.drop(fieldsIdsToRemove)

          request = await this.$api.restrictions.getRestrictionList({ exploration_type_id: item.id })
          let restrictionsIdsToRemove = request.data.map(item => item.id)
          if (restrictionsIdsToRemove)
            await this.$api.restrictions.drop(restrictionsIdsToRemove)

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
                await this.$api.explorations.update(this.editedItem)
                if (this.editedItem.partner_slug) await this.checkRestriction(this.editedItem.id)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {

              }
            } else {
              //CREATE
              try {
                this.editedItem.num = this.total + 1
                let response = await this.$api.explorations.create(this.editedItem)
                if (this.editedItem.partner_slug) await this.checkRestriction(response.data.res.id)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e.response)
              }
            }
          }
        })
      },

      async checkRestriction(exploration_type_id) {
        let response = await this.$api.restrictions.getRestrictionList({ exploration_type_id: exploration_type_id})
        const restriction = response.data || []

        if (!exploration_type_id || !restriction.length) {
          await this.$api.restrictions.create({ exploration_type_id: exploration_type_id, classroom_id: "", school_id: "" })
        }
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

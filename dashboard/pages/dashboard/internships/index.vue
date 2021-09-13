<template>
  <div>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Stages</v-toolbar-title>
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
                        v-model="editedItem.company_id"
                        :items="companies"
                        item-value="id"
                        item-text="name"
                        v-validate="'required'"
                        :error-messages="errors.collect('company')"
                        label="Entreprise *"
                        data-vv-name="company"
                        required
                      ></v-select>
                    </v-flex>
                    <v-flex xs12>
                      <v-autocomplete :items="schools"
                                      v-model="editedItem.school_ids"
                                      chips
                                      multiple
                                      item-value="id"
                                      item-text="name"
                                      :component-item="nameItemTemplate"
                                      :wait="0"
                                      label="Établissements *"
                                      v-validate="'required'"
                                      data-vv-name="schools"
                                      :error-messages="errors.collect('schools')"
                                      required></v-autocomplete>
                    </v-flex>
                    <v-flex xs12>
                      <v-text-field
                        type="number"
                        v-model.number="editedItem.nb_participant_boy"
                        v-validate="'required|decimal'"
                        :error-messages="errors.collect('nbParticipantBoy')"
                        label="Nombre de garçons ayant réalisés le stage *"
                        data-vv-name="nbParticipantBoy"
                        required></v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-text-field
                        type="number"
                        v-model.number="editedItem.nb_participant_girl"
                        v-validate="'required|decimal'"
                        :error-messages="errors.collect('nbParticipantGirl')"
                        label="Nombre de filles ayant réalisés le stage *"
                        data-vv-name="nbParticipantGirl"
                        required></v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-select
                        v-model="editedItem.kind"
                        :items="internshipKinds"
                        v-validate="'required'"
                        :error-messages="errors.collect('kind')"
                        label="Type *"
                        data-vv-name="kind"
                        required
                      ></v-select>
                    </v-flex>
                    <v-flex v-if="editedItem.kind === 'dt'" xs12>
                      <v-text-field
                        type="number"
                        v-model.number="editedItem.nb_immersions"
                        v-validate="'decimal'"
                        :error-messages="errors.collect('nbImmersions')"
                        label="Nombre d'immersions"
                        data-vv-name="nbImmersions">
                      </v-text-field>
                    </v-flex>
                    <v-flex v-if="editedItem.kind === 'dt'" xs12>
                      <v-text-field
                        type="number"
                        v-model.number="editedItem.nb_visits"
                        v-validate="'decimal'"
                        :error-messages="errors.collect('nbVisits')"
                        label="Nombre de visites"
                        data-vv-name="nbVisits">
                      </v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-text-field
                        type="number"
                        v-model.number="editedItem.satisfaction_rate"
                        v-validate="'decimal'"
                        :error-messages="errors.collect('satisfactionRate')"
                        label="Taux de satisfaction des jeunes ayant réalisé le stage (en %)"
                        data-vv-name="satisfactionRate">
                      </v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-text-field
                        type="number"
                        v-model.number="editedItem.acquisition_operation_rate"
                        v-validate="'decimal'"
                        :error-messages="errors.collect('acquisitionOperationRate')"
                        label="Taux d'acquisition des codes et fonctionnement de l'entreprise (en %)"
                        data-vv-name="acquisitionOperationRate">
                      </v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-text-field
                        type="number"
                        v-model.number="editedItem.acquisition_skills_rate"
                        v-validate="'decimal'"
                        :error-messages="errors.collect('acquisitionSkillsRate')"
                        label="Taux d'acquisition des compétences (en %)"
                        data-vv-name="acquisitionSkillsRate">
                      </v-text-field>
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
          <td>{{ props.item.company.name }}</td>
          <td>
            <span v-for="(school, index) in props.item.schools" :key="school.id">
              {{ school.name }}<span v-if="index < (props.item.schools.length - 1)">, </span>
            </span>
          </td>
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
  import TemplateItem from '~/components/autocomplete/NameItemTemplate'
  import { INTERNSHIP_KINDS } from "~/config";
  import _ from 'lodash'

  export default {
    name: 'DashboardInternship',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    components: { ConfirmDelete },
    $_veeValidate: {
      validator: 'new'
    },
    async asyncData({app}) {
      try {
        let response = await app.$api.companies.getList({page: 1, numberPerPage: 1000})
        const companies = response.data || []

        response = await app.$api.schools.getList({page: 1, numberPerPage: 1000})
        const schools = response.data || []

        return { companies, schools }
      } catch (e) {
        console.log(e)
      }
    },
    data () {
      return {
        items: [],
        companies: [],
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
            text: 'Entreprise',
            align: 'left',
            sortable: false,
            value: 'text'
          },
          {
            text: 'Écoles',
            align: 'left',
            sortable: false,
            value: 'text'
          },
          { text: 'Actions', sortable: false }
        ],
        internshipKinds: INTERNSHIP_KINDS,
        nameItemTemplate: TemplateItem
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer un stage' : 'Éditer un stage'
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
            let request = await this.$api.internships.count()
            const count = request.data.res || 0

            request = await this.$api.internships.getList({
              page: this.pagination.page,
              numberPerPage: this.pagination.rowsPerPage
            })
            const items = request.data || []

            items.forEach((item) => {
              item.company = _.find(this.companies, {id: item.company_id}) || {}
              item.schools = _.filter(this.schools, (school) => {
                return _.includes(item.school_ids, school.id)
              }) || {}
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
          await this.$api.internships.drop([item.id])
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
                await this.$api.internships.update(this.editedItem)

                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {

              }
            } else {
              //CREATE
              try {
                await this.$api.internships.create(this.editedItem)

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
    }
  }
</script>
<style lang="scss">

</style>

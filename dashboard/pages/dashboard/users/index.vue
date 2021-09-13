<template>
<div>
  <v-app>
    <v-toolbar flat color="black">
      <v-toolbar-title class="numericite-toolbar-title">Utilisateurs</v-toolbar-title>
      <v-divider
        class="mx-2"
        inset
        vertical
      ></v-divider>
      <v-spacer></v-spacer>
      <v-text-field
        v-model="searchInput"
        append-icon="search"
        label="Chercher par nom, prénom, email"
        single-line
        hide-details
      ></v-text-field>
      <v-spacer></v-spacer>
      <v-select
        v-model="searchType"
        label="Chercher par rôle"
        :items="roles"
        item-value="value.accountType"
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
                  <v-flex xs12 sm6>
                    <v-text-field
                      v-model="editedItem.firstName"
                      v-validate="'required'"
                      :error-messages="errors.collect('firstName')"
                      label="Prénom *"
                      data-vv-name="firstName"
                      required></v-text-field>
                  </v-flex>
                  <v-flex xs12 sm6>
                    <v-text-field v-model="editedItem.lastName"
                                  v-validate="'required'"
                                  :error-messages="errors.collect('lastName')"
                                  label="Nom *"
                                  data-vv-name="lastName"
                                  required>></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-select
                      v-model="editedItem.sex"
                      :items="sexes"
                      :error-messages="errors.collect('sex')"
                      label="Sexe"
                      data-vv-name="sex"
                    ></v-select>
                  </v-flex>
                  <v-flex xs12 >
                    <v-text-field v-model="editedItem.email"
                                  v-validate="'required|email'"
                                  :error-messages="errors.collect('email')"
                                  label="Email *"
                                  data-vv-name="email"
                                  @change="conflictEmail = false"
                                  required>></v-text-field>
                    <div v-if="conflictEmail" class="custom-error">
                      Cet email existe déjà dans notre base de donnée
                    </div>
                  </v-flex>
                  <v-flex xs12 d-flex>
                    <v-select
                      v-model="editedItem.kind"
                      @change="resetConfig()"
                      :items="roles"
                      v-validate="'required'"
                      :error-messages="errors.collect('role')"
                      label="Role *"
                      data-vv-name="role"
                      required
                    ></v-select>
                  </v-flex>
                  <v-flex v-show="editedItem.kind && (editedItem.kind.accountType === 'collectivity' || editedItem.kind.accountType === 'operator')">
                    <v-autocomplete :items="schools"
                                    v-model="editedItem.config.school_ids"
                                    chips
                                    multiple
                                    item-value="id"
                                    item-text="name"
                                    :component-item="nameItemTemplate"
                                    :wait="0"
                                    label="Établissements *"
                                    v-validate="editedItem.kind && (editedItem.kind.accountType === 'collectivity' || editedItem.kind.accountType === 'operator') ? 'required' : ''"
                                    data-vv-name="schools"
                                    :error-messages="errors.collect('schools')"
                                    :required="editedItem.kind && (editedItem.kind.accountType === 'collectivity' || editedItem.kind.accountType === 'operator')"></v-autocomplete>
                  </v-flex>
                  <v-flex v-show="editedItem.kind && (editedItem.kind.accountType === 'student' || editedItem.kind.accountType === 'teacher' || editedItem.kind.accountType === 'school')">
                    <v-autocomplete :items="schools"
                                    v-model="editedItem.config.school_id"
                                    item-value="id"
                                    item-text="name"
                                    :component-item="nameItemTemplate"
                                    :wait="0"
                                    label="École *"
                                    v-validate="editedItem.kind && (editedItem.kind.accountType === 'student' || editedItem.kind.accountType === 'teacher' || editedItem.kind.accountType === 'school') ? 'required' : ''"
                                    data-vv-name="school"
                                    :error-messages="errors.collect('school')"
                                    :required="editedItem.kind && (editedItem.kind.accountType === 'student' || editedItem.kind.accountType === 'teacher' || editedItem.kind.accountType === 'school')"
                                    @change="getClassrooms(editedItem.config.school_id)"></v-autocomplete>
                  </v-flex>
                  <v-flex v-show="editedItem.kind && editedItem.kind.accountType === 'student' && editedItem.config.school_id">
                    <v-autocomplete :items="classrooms"
                                    v-model="editedItem.config.classroom_id"
                                    item-value="id"
                                    item-text="name"
                                    :component-item="nameItemTemplate"
                                    :wait="0"
                                    label="Classe *"
                                    v-validate="editedItem.kind && editedItem.kind.accountType === 'student' ? 'required' : ''"
                                    data-vv-name="classroom"
                                    :error-messages="errors.collect('classroom')"
                                    :required="editedItem.kind && editedItem.kind.accountType === 'student'"></v-autocomplete>
                  </v-flex>
                  <v-flex v-show="editedItem.kind && editedItem.kind.accountType === 'student'">
                    <v-autocomplete :items="parents"
                                    v-model="editedItem.config.parent_id"
                                    item-value="userID"
                                    item-text="fullName"
                                    :component-item="nameItemTemplate"
                                    :wait="0"
                                    label="Parent"></v-autocomplete>
                  </v-flex>
                  <v-flex v-show="editedItem.kind && editedItem.kind.accountType === 'student'">
                    <v-autocomplete :items="mentors"
                                    v-model="editedItem.config.mentor_id"
                                    item-value="userID"
                                    item-text="fullName"
                                    :component-item="nameItemTemplate"
                                    :wait="0"
                                    label="Mentor"></v-autocomplete>
                  </v-flex>
                  <v-flex v-show="editedItem.kind && editedItem.kind.accountType === 'teacher' && editedItem.config.school_id">
                    <v-autocomplete :items="classrooms"
                                    v-model="editedItem.config.classroom_ids"
                                    chips
                                    multiple
                                    item-value="id"
                                    item-text="name"
                                    :component-item="nameItemTemplate"
                                    :wait="0"
                                    label="Classes *"
                                    v-validate="editedItem.kind && editedItem.kind.accountType === 'teacher' ? 'required' : ''"
                                    data-vv-name="classrooms"
                                    :error-messages="errors.collect('classrooms')"
                                    :required="editedItem.kind && editedItem.kind.accountType === 'teacher'"></v-autocomplete>
                  </v-flex>
                  <v-flex v-show="editedItem.kind && editedItem.kind.accountType === 'partner'">
                    <v-autocomplete :items="partners"
                                    v-model="editedItem.config.partner_id"
                                    item-value="id"
                                    item-text="name"
                                    :component-item="nameItemTemplate"
                                    :wait='0'
                                    label="Partenaire"
                                    v-validate="editedItem.kind && editedItem.kind.accountType === 'partner' ? 'required' : ''"
                                    data-vv-name="partners"
                                    :error-messages="errors.collect('partners')"
                                    :required="editedItem.kind && editedItem.kind.accountType === 'partner'"></v-autocomplete>
                  </v-flex>
                  <v-flex v-if="editedIndex === -1" xs12>
                    <v-text-field
                      v-model="editedItem.password"
                      :append-icon="showPassword ? 'visibility' : 'visibility_off'"
                      :type="showPassword ? 'text' : 'password'"
                      :disabled="editedItem.generated"
                      v-validate="!editedItem.generated ? 'required|min:8' : ''"
                      :error-messages="errors.collect('password')"
                      :label="!editedItem.generated ? 'Mot de passe *' : 'Mot de passe'"
                      data-vv-name="password"
                      :required="!editedItem.generated"
                      hint="Au moins 8 caractères"
                      counter
                      @click:append="showPassword = !showPassword"
                    ></v-text-field>
                  </v-flex>
                  <v-flex v-if="editedIndex === -1" xs12>
                    <v-checkbox v-model="editedItem.generated" label="Générer automatiquement le mot de passe"></v-checkbox>
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
      :search="searchType"
      class="elevation-1"
    >
      <v-progress-linear v-slot:progress color="blue" indeterminate></v-progress-linear>
      <template v-slot:items="props">
        <td>{{ props.item.lastName}}</td>
        <td>{{ props.item.firstName }}</td>
        <td>{{ props.item.email}}</td>
        <td>{{ accountTypes[props.item.config.accountType] }}</td>
        <td class="justify-center">
          <v-btn round depressed small @click="editItem(props.item)">
            <v-icon
              small
            >
              edit
            </v-icon>
          </v-btn>
          <v-btn round depressed small @click="deleteItem(props.item)">
            <v-icon
              small
            >
              delete
            </v-icon>
          </v-btn>
          <v-btn v-if="props.item.config.accountType === 'student' || props.item.config.accountType === 'teacher'" round depressed small @click="reInitUser(props.item)">
            <v-icon
              small
            >
              refresh
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
  import TemplateItem from '~/components/autocomplete/NameItemTemplate'
  import { ROLES, ACCOUNT_TYPES } from '~/config'
  import _ from 'lodash'

  export default {
    name: 'DashboardUsers',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    components: { TemplateItem },
    $_veeValidate: {
      validator: 'new'
    },
    async asyncData({app}) {
      try {
        let response = await app.$api.schools.getList({page: 1, numberPerPage: 1000})
        const schools = response.data || []

        response = await app.$api.users.getParents({page: 1, numberPerPage: 1000})
        const parents = response.data.res || []

        parents.forEach(function (parent) {
          parent.fullName = parent.firstName + ' ' + parent.lastName
        })

        response = await app.$api.users.getMentors({page: 1, numberPerPage: 1000})
        const mentors = response.data.res || []

        mentors.forEach(function (mentor) {
          mentor.fullName = mentor.firstName + ' ' + mentor.lastName
        })

        response = await app.$api.partners.getList({page: 1, numberPerPage: 1000})
        const partners = response.data ||  []

        return { schools, parents, mentors, partners }
      } catch (e) {
        console.log('error : ', e)
      }
    },
    data () {
      return {
        items: [],
        formData: null,
        searchInput: '',
        editedItem: {
          firstName: '',
          lastName: '',
          email: '',
          config: {
            school_id: null,
            school_ids: null,
            classroom_id: null,
            classroom_ids: null,
            parent_id: null,
            partner_id: null
          },
          generated: false
        },
        editedIndex: -1,
        defaultItem: {
          firstName: '',
          lastName: '',
          email: '',
          config: {
            school_id: null,
            school_ids: null,
            classroom_id: null,
            classroom_ids: null,
            parent_id: null,
            partner_id: null
          },
          generated: false
        },
        conflictEmail: false,
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
            sortable: true,
            value: 'lastName'
          },
          {
            text: 'Prénom',
            align: 'left',
            sortable: true,
            value: 'firstName'
          },
          {
            text: 'Email',
            align: 'left',
            sortable: false,
            value: 'email'
          },
          {
            text: 'Type',
            align: 'left',
            sortable: false,
            value: 'email'
          },
          { text: 'Actions', sortable: false }
        ],
        showPassword: false,
        roles: [
          {'text': 'Étudiant', value: ROLES.student},
          {'text': 'Professeur', value: ROLES.teacher},
          {'text': 'Parent', value: ROLES.parent},
          {'text': 'Mentor', value: ROLES.mentor},
          {'text': 'École', value: ROLES.school},
          {'text': 'Collectivité', value: ROLES.collectivity},
          {'text': 'Administrateur', value: ROLES.admin},
          {'text': "Partenaire", value: ROLES.partner},
          {'text': "Chargé des opérations", value: ROLES.operator}
        ],
        sexes: [
          { 'text': 'Homme', value: 'man'},
          { 'text': 'Femme', value: 'woman'}
        ],
        accountTypes: ACCOUNT_TYPES,
        classrooms: [],
        parents: [],
        mentors: [],
        schools: [],
        partners: [],
        nameItemTemplate: TemplateItem,
        searchType: ''
      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'Créer un utilisateur' : 'Éditer un utilisateur'
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
      searchType: {
        handler () {
          if (!this.firstLoad) {
            this.getRows().then(this.bindData)
          }
        },
        deep: true
      },
      searchInput: {
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
            let request = await this.$api.users.count()
            const count = request.data.res || 0

            const requestParams = {
              page: this.pagination.page,
              numberByPage: this.pagination.rowsPerPage,
            }

            if (this.pagination.sortBy) {
              requestParams['sortBy' + this.capitalize(this.pagination.sortBy)] = this.pagination.descending ? -1 : 1
            }

            if (this.searchInput && this.searchInput !== '')  {
              requestParams.lastName = this.searchInput
              requestParams.email = this.searchInput
              requestParams.firstName = this.searchInput
            }

            if (this.searchType && this.searchType !== '')  {
              requestParams.accountType = this.searchType  
            }

            request = await this.$api.users.getList(requestParams)
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
        this.editedItem.kind = _.find(ROLES, {'accountType': this.editedItem.config.accountType})
        this.getClassrooms(this.editedItem.config.school_id)
        this.dialog = true
      },

      resetConfig() {
        this.editedItem.config = {}
        this.$forceUpdate()
      },

      async deleteItem (item) {
        const index = this.items.indexOf(item)
        if (confirm('Êtes vous sur de vouloir supprimer cet utilisateur')) {
          try {
            await this.$api.users.drop({email: item.email, providerID: item.userID})
            this.getRows().then(this.bindData)
          } catch (e) {

          }
        }
      },

      async reInitUser(item) {
        if (confirm('Êtes vous sur de vouloir remettre à zéro cet élève ?')) {
          try {
            await this.$api.users.reInit({id: item.userID, accountType: item.config.accountType})
            this.getRows().then(this.bindData)
          } catch (e) {

          }
        }
      },

      close () {
        this.dialog = false
        this.conflictEmail = false
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
          console.log(this.editedItem.config);
          if (valid) {
            this.editedItem.config.accountType = this.editedItem.kind.accountType
            this.editedItem.roles = this.editedItem.kind.roles
            if (this.editedIndex > -1) {
              //EDIT
              try {
                await this.$api.users.update(this.editedItem)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {

              }
            } else {
              //CREATE
              try {
                await this.$api.users.signUp(this.editedItem)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e.response)
                if (e.response.status === 409) {
                  this.conflictEmail = true
                }
              }
            }
          }
        })
      },

      async getClassrooms(id) {
        try  {
          let response = await this.$api.schools.getClassrooms({page: 1, numberPerPage: 1000, school_ids: [id]})
          this.classrooms = response.data || []
        } catch (e) {
          console.log('error : ', e)
        }
      }

    }
  }
</script>
<style lang="scss">

</style>

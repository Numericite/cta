<template>
  <div>
    <v-app>
      <v-tabs class="tabs-cta" color="#F4F3EF" flat>
        <v-tabs-slider class="tab-slider-cta" color="primary"></v-tabs-slider>
        <v-tab v-for="(type, key) in types" :key="key" :to="'/dashboard/documents?slug=' + type.value"
               class="tab-cta" exact-active-class="tab-active" :ripple="false" exact nuxt dark>
          {{ type.text }}
        </v-tab>
      </v-tabs>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Documents ({{ getTitleType }}) </v-toolbar-title>
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
                        v-model="editedItem.title"
                        v-validate="'required'"
                        :error-messages="errors.collect('title')"
                        label="Titre *"
                        data-vv-name="title"
                        required
                        @change="documentFieldChange"></v-text-field>
                    </v-flex>
                    <v-flex xs12>
                      <v-select
                        v-model="editedItem.parent_type"
                        :items="types"
                        @change="removeEditedItem"
                        v-validate="'required'"
                        :error-messages="errors.collect('parent_type')"
                        label="À destination de *"
                        data-vv-name="parent_type"
                        required
                      ></v-select>
                    </v-flex>
                    <v-flex v-if="editedItem.parent_type === 'teacher'" xs12>
                      <v-select
                        v-model="editedItem.school_kind"
                        :items="schoolKinds"
                        v-validate="'required'"
                        :error-messages="errors.collect('school_kind')"
                        label="Type d'établissement *"
                        data-vv-name="school_kind"
                        required
                        @change="documentFieldChange"
                      ></v-select>
                    </v-flex>
                    <v-flex v-if="editedItem.parent_type.includes('teacher')" xs12>
                      <v-autocomplete
                        v-model="editedItem.grades"
                        :items="gradesFilter(editedItem.parent_type)"
                        chips
                        multiple
                        label="Classes *"
                        v-validate="{required: editedItem.parent_type.includes('teacher')}"
                        key="grades"
                        data-vv-name="grades"
                        :error-messages="errors.collect('grades')"
                        required
                      ></v-autocomplete>
                    </v-flex>
                    <v-flex v-if="editedItem.parent_type.includes('teacher')" xs12>
                      <v-select
                        v-model="editedItem.file_kind"
                        :items="selectFileKinds"
                        v-validate="{required: editedItem.parent_type.includes('teacher')}"
                        key="file_kind"
                        :error-messages="errors.collect('file_kind')"
                        @change="changeFileKind"
                        label="Type de fichiers *"
                        data-vv-name="file_kind"
                        required
                      ></v-select>
                    </v-flex>
                    <v-flex xs12 v-if="editedItem.file_kind === 'document' || !editedItem.file_kind">
                      <v-text-field
                        label="Document *"
                        v-validate="{required: editedItem.file_kind === 'document'}"
                        :error-messages="errors.collect('document')"
                        key="document"
                        @click="pickDocument"
                        placeholder="Cliquez pour sélectionner un document..."
                        v-model="editedItem.description"
                        data-vv-name="document"></v-text-field>
                      <input
                        type="file"
                        style="display: none"
                        ref="document"
                        accept="application/pdf"
                        @change="onDocumentPicked"
                      >
                    </v-flex>
                    <v-flex xs12 v-if="editedItem.file_kind === 'link' && editedItem.parent_type.includes('teacher')">
                      <v-text-field
                        v-model="editedItem.path"
                        v-validate="{required: editedItem.file_kind === 'link' && editedItem.parent_type.includes('teacher'), url: {require_protocol: true}}"
                        :error-messages="errors.collect('path')"
                        key="link"
                        label="Lien *"
                        data-vv-name="path"
                      ></v-text-field>
                    </v-flex>
                    <v-flex xs12 v-if="editedItem.file_kind === 'link_video' && editedItem.parent_type.includes('teacher')">
                      <v-text-field
                        v-model="editedItem.path"
                        v-validate="{required: editedItem.file_kind === 'link_video' && editedItem.parent_type.includes('teacher'), url: {require_protocol: true}, regex: '(youtube|youtu|vimeo|dailymotion)'}"
                        key="link_video"
                        :error-messages="errors.collect('path')"
                        label="Lien *"
                        data-vv-name="path"
                      ></v-text-field>
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
            <td>{{ props.item.title }}</td>
            <td>{{ props.item.description }}</td>
            <td>{{ displayedType(props.item.parent_type) }}</td>
            <td>
              <a v-if="props.item.file_kind === 'document' || props.item.file_kind === undefined" :href="props.item.path" target="_blank">
                <v-btn round depressed small>
                  <v-icon
                    small
                  >
                    ti-import
                  </v-icon>
                </v-btn>
              </a>
              <a v-if="props.item.file_kind === 'link'" :href="props.item.path" target="_blank">
                <v-btn round depressed small>
                  <v-icon
                    small
                  >
                    ti-link
                  </v-icon>
                </v-btn>
              </a>
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
import utils from '~/assets/js/utils'
import { ACCOUNT_TYPES, SCHOOL_KINDS } from '~/config'
import _ from 'lodash'
import {STUDENT_GRADES} from "../../../config"
import Sortable from "sortablejs"

export default {
  name: 'DashboardQuestions',
  layout: "dashboard",
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessDashboard',
  components: { ConfirmDelete },
  $_veeValidate: {
    validator: 'new'
  },
  asyncData({ redirect, route }) {
    try {
      
      let searchType = route.query.slug

      const types = [
        {text: ACCOUNT_TYPES.teacher + ' - Lycée', value: 'teacher'},
        {text: ACCOUNT_TYPES.teacher + ' - Collège', value: 'teacher-ms'},
        {text: ACCOUNT_TYPES.parent, value: 'parent'},
        {text: ACCOUNT_TYPES.mentor, value: 'mentor'},
        {text: ACCOUNT_TYPES.school, value: 'school'},
        {text: ACCOUNT_TYPES.collectivity, value: 'collectivity'},
        {text: ACCOUNT_TYPES.student + ' - Domaines', value: 'student-domain'},
        {text: ACCOUNT_TYPES.student + ' - Filières', value: 'student-stream'}
      ]

      let slugValid = types.map(item => item.value).includes(searchType)

      if (!searchType || !slugValid) {
        redirect('/dashboard/documents?slug=' + types[0].value)
      }

      return { searchType, types }
    } catch (e) {
      console.log(e)
    }
  },
  data () {
    return {
      items: [],
      formData: null,
      editedItem: {
        title: '',
        user_id: '',
        description: '',
        parent_type: '',
        file_kind: '',
        path: '',
        grades: [],
        school_kind: null
      },
      editedIndex: -1,
      defaultItem: {
        title: '',
        user_id: '',
        description: '',
        parent_type: '',
        file_kind: '',
        path: '',
        grades: [],
        school_kind: null
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
        },
        {
          text: 'Titre',
          align: 'left',
          sortable: false,
          value: 'text'
        },
        {
          text: 'Fichier',
          align: 'left',
          sortable: false,
          value: 'text'
        },
        {
          text: 'À destination de',
          align: 'left',
          sortable: false,
          value: 'text'
        },
        { text: 'Actions', sortable: false }
      ],
      types: [
        {text: ACCOUNT_TYPES.teacher + ' - Lycée', value: 'teacher'},
        {text: ACCOUNT_TYPES.teacher + ' - Collège', value: 'teacher-ms'},
        {text: ACCOUNT_TYPES.parent, value: 'parent'},
        {text: ACCOUNT_TYPES.mentor, value: 'mentor'},
        {text: ACCOUNT_TYPES.school, value: 'school'},
        {text: ACCOUNT_TYPES.collectivity, value: 'collectivity'},
        {text: ACCOUNT_TYPES.student + ' - Domaines', value: 'student-domain'},
        {text: ACCOUNT_TYPES.student + ' - Filières', value: 'student-stream'}
      ],
      selectFileKinds: [
        { text: 'Document', value: 'document'},
        { text: 'Lien vidéo', value: 'link_video'},
        { text: 'Lien externe', value: 'link'}
      ],
      schoolKinds: _.concat([{text: 'Tous', value: 'all'}], SCHOOL_KINDS)
    }
  },
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'Créer un document' : 'Éditer un document'
    },
    itemHasId () {
      return this.editedItem.id ? true : false
    },
    getTitleType() {
      return this.types.find(item => item.value === this.searchType).text
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
    '$route.query': {
      handler () {
        if (!this.firstLoad) {
          this.searchType = this.$route.query.slug
          this.getRows().then(this.bindData)
        }
      },
      deep: true
    },
    dialog (val) {
      if (val) this.editedItem.parent_type = this.searchType
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
            await this.$api.documents.update(item)
          } catch (e) {

          }
        }
      })

      if (!_.isEqual(this.items, this.itemsCopy))
        this.getRows().then(this.bindData)
    },

    gradesFilter (teacher) {
      if (teacher === 'teacher') {
        return STUDENT_GRADES.filter(item => item.group === 'high-school')
      } else if (teacher === 'teacher-ms') {
        return STUDENT_GRADES.filter(item => item.group === 'college')
      }
    },

    removeEditedItem () {
      this.editedItem.file_kind = 'document'
      this.editedItem.path = ''
      this.editedItem.grades.length ? this.editedItem.grades = [] : ''
    },

    bindData(data) {
      this.total = data.count || 0
      this.items = data.items || []
      this.loading = false
    },

    async getRows() {
      this.loading = true;
      return new Promise(async (resolve, reject) => {
        try {

          let request = await this.$api.documents.count({ parent_type: this.searchType })
          const count = request.data.res || 0

          request = await this.$api.documents.getList({
            page: this.pagination.page,
            numberPerPage: this.pagination.rowsPerPage,
            parent_type: this.searchType
          })

          const items = request.data || []

          items.sort((a, b) => a.num - b.num)

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
        await this.$api.documents.drop([item.id])
        this.getRows().then(this.bindData)
      } catch (e) {
        console.log(e)
      }
    },

    async save () {
      this.$validator.validate().then(async valid => {

        if (this.editedItem.file_kind === 'link' || this.editedItem.file_kind === 'link_video')
          this.editedItem.description = this.editedItem.title

        if (valid) {
          this.editedItem.user_id = this.$store.state.auth.user.userID
          if (this.editedIndex > -1) {
            //EDIT
            try {

              if (this.editedItem.file_kind === 'document') {
                let response = await this.$api.documents.upload(this.editedItem.parent_type, this.editedItem.file, this.editedItem.grades, this.editedItem.school_kind, true)
                this.editedItem.path = response.data.res.path
              }

              this.editedItem.file_kind === 'link' || this.editedItem.file_kind === 'link_video' ? this.editedItem.description = this.editedItem.title : ''
              await this.$api.documents.update(this.editedItem)
              this.getRows().then(this.bindData)
              this.close()
            } catch (e) {
              console.log(e.response)
            }
          } else {
            //CREATE
            if (this.editedItem.file_kind === 'document') {
              try {
                await this.$api.documents.upload(this.editedItem.parent_type, this.editedItem.file, this.editedItem.grades, this.editedItem.school_kind, false)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e.response)
              }
            } else if (this.editedItem.file_kind === 'link' || this.editedItem.file_kind === 'link_video') {
              try {
                await this.$api.documents.create(this.editedItem)
                this.getRows().then(this.bindData)
                this.close()
              } catch (e) {
                console.log(e.response)
              }
            }
          }
        }
      })
    },

    documentFieldChange() {
      if (this.editedItem.file) {
        this.editedItem.file.set('description', this.editedItem.description)
        this.editedItem.file.set('title', this.editedItem.title)

        if (this.editedItem.school_kind) {
          this.editedItem.file.set('school_kind', this.editedItem.school_kind)
        }
      }
    },

    pickDocument() {
      this.$refs.document.click()
    },

    onDocumentPicked(e) {
      const files = e.target.files
      const formData = new FormData()
      if(files[0] !== undefined) {
        this.editedItem.description = files[0].name
        formData.append('file', files[0], utils.toValidFileName(files[0].name))
        formData.append('user_id', this.$store.state.auth.user.userID)
        formData.append('description', this.editedItem.description)
        formData.append('title', this.editedItem.title)
        formData.append('file_kind', this.editedItem.file_kind)
        this.editedItem.file = formData
      } else {
        this.editedItem.description = ''
      }
    },

    changeFileKind() {
      this.editedItem.description = ''
      this.editedItem.path = ''
    },

    displayedType(type) {
      return _.get(_.find(this.types, {value: type}), 'text', '')
    }
  }
}
</script>

<style lang="scss">

  .tabs-cta {
    background-color: #F4F3EF;

    .v-tabs__slider-wrapper {
      bottom: unset;
      z-index: 10;

      .tab-slider-cta {
        height: 3px;
      }
    }

    .v-tabs__item {
      font-weight: 500;
      position: static!important;

      &:hover {
        color: $main-color!important;
      }

      &.tab-active {
        background-color: black!important;
        color: white!important;
      }
    }

  }

</style>

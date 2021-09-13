<template>
  <div>

    <v-dialog v-model="dialogSettings" max-width="500px">
      <v-card>
        <v-card-title>
          <span class="headline">Réglage du champ {{ editedSettings.sort_id + 1 }}</span>
        </v-card-title>

        <v-card-text>
          <v-container grid-list-md>
            <v-layout wrap>
              <v-flex xs12>
                <v-checkbox
                  v-model="editedSettings.validation"
                  label="Champ obligatoire ?"
                />
              </v-flex>
              <v-flex v-if="editedSettings.type !== 'radio' && editedSettings.type !== 'checkbox' && editedSettings.type !== 'date'" xs12>
                <v-text-field
                  v-model="editedSettings.placeholder"
                  label="Placeholder *"
                  class="label-no-margin"
                />
              </v-flex>
              <v-flex v-if="editedSettings.type === 'checkbox'" xs12>
                <v-select
                  v-model="editedSettings.validationMin"
                  label="Minimum de case a cocher"
                  :items="checkboxSettings"
                />
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" flat @click="dialogSettings = false">Fermer</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-toolbar flat color="black">
      <v-toolbar-title class="numericite-toolbar-title">Générateur de formulaire</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-card-actions>
        <v-btn color="grey darken-1" @click="addComponent" depressed dark>
          <v-icon class="mr-2"> add</v-icon> Ajouter un composant
        </v-btn>
        <v-btn color="grey darken-1" class="ml-3" @click="addField" depressed dark>
          <v-icon class="mr-2"> add</v-icon> Ajouter un champ
        </v-btn>
        <v-btn class="primary ml-3" @click="save" depressed dark>
          <v-icon class="mr-2"> save</v-icon> Enregistrer
        </v-btn>
      </v-card-actions>

    </v-toolbar>
    <v-container class="pt-3 pb-5" white fluid>
      <v-layout justify-center wrap>
        <v-flex xs12 sm6>
          <draggable v-model="schema" handle=".handle" @change="updateSortId" v-bind="dragOptions" group="field">
            <v-layout class="mt-3" v-for="(input, index) in schema" :key="input.lastId">
              <v-flex class="borderDrag my-2 mr-2 handle text-xs-left" d-flex align-center justify-center xs2 lg1>
                <v-btn :ripple="false" flat>
                  <v-icon color="grey darken-2" class="textOpacity" small> drag_indicator</v-icon><span class="textOpacity">{{ (index + 1) }}</span>
                </v-btn>
              </v-flex>
              <v-flex xs10 lg11>
                <v-layout class="borderField px-3 pt-2" column>
                  <v-layout wrap>

                    <v-flex  xs10 offset-xs1 lg5 offset-lg0>
                      <v-text-field  v-if="input.type != null"
                       v-model="input.label"
                       label="Label *"
                       :data-vv-name="'inputLabel-' + input.lastId"
                       v-validate="'required'"
                       :error-messages="errors.collect('inputLabel-' + input.lastId)"
                       required
                      />
                      <v-text-field v-if="input.component != null"
                        v-model="input.children"
                        label="Label *"
                        :data-vv-name="'inputChildren-' + input.lastId"
                        v-validate="'required'"
                        :error-messages="errors.collect('inputChildren-' + input.lastId)"
                        required
                      />
                    </v-flex>

                    <v-flex offset-xs1 xs10 lg3>
                      <v-select v-if="input.type != null"
                        v-model="input.type"
                        :items="inputTypes"
                        label="Champs possible"
                        @change="changeField(input, index)"
                        attach
                      />
                      <v-select v-if="input.component != null"
                        v-model="input.component"
                        :items="componentTypes"
                        label="Composants possible"
                        @change="changeField(input, index)"
                        attach
                      />
                    </v-flex>

                    <v-flex xs10 offset-xs1 lg3 offset-lg0>
                      <v-card-actions class="text-xs-center mt-1 justify-center align-center">
                        <v-btn v-if="input.type != null" color="grey darken-1" @click.stop="startDialogSettings(input)" class="pa-2" depressed round dark>
                          <v-icon> settings</v-icon>
                        </v-btn>

                        <v-btn color="red darken-1" @click="deleteField(index)" class="pa-2" depressed round dark>
                          <v-icon> clear</v-icon>
                        </v-btn>

                      </v-card-actions>
                    </v-flex>

                  </v-layout>
                  <v-layout v-for="(option, indexOption) in input.options" :key="option.value" d-flex align-center row wrap>

                    <v-flex class="text-lg-center" xs10 offset-xs1 lg3 offset-lg0>
                      <v-label>{{ input.type + ' ' + (indexOption + 1) + ' : ' }}</v-label>
                    </v-flex>

                    <v-flex xs10 offset-xs1 lg6 offset-lg0>
                      <v-text-field
                        v-model="option.label"
                        :label="'Label ' + input.type + ' ' + (indexOption + 1) + ' *'"
                        :data-vv-name="'optionLabel-' + option.id"
                        v-validate="'required'"
                        :error-messages="errors.collect('optionLabel-' + option.id)"
                        required
                      />
                    </v-flex>

                    <v-flex xs10 offset-xs1 lg3 offset-lg0>
                      <v-card-actions class="text-xs-center justify-center">
                        <v-btn color="red darken-1" @click="deleteOption(input, indexOption)" class="pa-2" depressed round dark>
                          <v-icon> clear</v-icon>
                        </v-btn>
                      </v-card-actions>
                    </v-flex>

                  </v-layout>

                  <v-layout v-if="input.options != null && input.options.length" d-flex align-center row wrap>
                    <v-flex class="text-lg-center" xs10 offset-xs1 lg3 offset-lg0>
                      <v-label disabled><span class="textOpacity">{{ input.type + ' ' + (input.options.length + 1) + ' : ' }}</span></v-label>
                    </v-flex>
                    <v-flex xs10 offset-xs1 lg6 offset-lg0>
                      <v-text-field class="textOpacity" :label="'Label ' + input.type + ' ' + (input.options.length + 1) + ' *'" disabled/>
                    </v-flex>
                    <v-flex xs10 offset-xs1 lg3 offset-lg0>
                      <v-card-actions class="text-xs-center justify-center">
                        <v-btn class="primary pa-2" @click="createOption(index)" depressed round dark>
                          <v-icon> add</v-icon>
                        </v-btn>
                      </v-card-actions>
                    </v-flex>
                  </v-layout>

                </v-layout>
              </v-flex>
            </v-layout>
          </draggable>
        </v-flex>
        <v-flex xs12 offset-sm1 sm5>
          <h3 class="mt-2">Formulaire généré : </h3>
          <v-layout class="mt-3 pa-4 white borderField" column>
            <v-flex class="d-flex pt-1 px-1" >
              <FormulateForm :schema="schema"/>
            </v-flex>
            <v-flex class="d-flex pt-3 px-1">
              <FormulateInput v-bind="{type: 'submit', label : 'Valider'}" disabled/>
            </v-flex>
          </v-layout>
        </v-flex>
      </v-layout>
    </v-container>

  </div>
</template>

<script>

  import '@braid/vue-formulate/themes/snow/snow.scss';
  import draggable from 'vuedraggable'
  import _ from 'lodash'

  export default {
    components: {
      draggable,
    },
    $_veeValidate: {
      validator: 'new'
    },
    props: ['parentId'],
    data() {
      return {
        dialogSettings: false,
        indexSettings: 0,
        editedSettings: {},
        checkboxSettings: [1, 2, 3, 4, 5],
        loading: true,
        lastId: 0,
        lastOptionId: 0,
        valid: false,
        requiredRule: [
          v => !!v || ''
        ],
        inputTypes: [
          { value: 'text', text: 'Texte'},
          { value: 'textarea', text: 'Zone de texte'},
          { value: 'number', text: 'Nombre'},
          { value: 'date', text: 'Date'},
          { value: 'checkbox', text: 'Cases à cocher'},
          { value: 'radio', text: 'Boutons radios'},
        ],
        componentTypes: [
          { value: 'h3', text: 'Titre'}
        ],
        schema: [],
        schemaCopy: [],
      }
    },
    computed: {
      dragOptions() {
        return {
          animation: 250,
          group: "description",
          disabled: false,
          ghostClass: "ghost"
        };
      }
    },
    mounted() {
      this.$validator.localize('fr', this.dictionary)
      this.getFields().then((data) => {
        this.bindData(data)
      })
    },
    methods: {

      // Field CRUD

      bindData(data) {

        data.items.sort((a, b) => a.sort_id - b.sort_id)

        data.items.map(item => {

          item.lastId = this.lastId++

          if (item.kind != null) {
            item.type = item.kind
            item.validation != null ? '' : item.validation = false
          }

          item.disabled = true
          item.type === 'checkbox' ? item.validationMin ? '' : item.validationMin = 1 : delete item.validationMin

          if (item.optionsField && item.optionsField.length) {
            item.options = item.optionsField.map(option => {return { label: option.label, id: option.id, value: option.value}})
          } else {
            item.options = []
          }
        })

        this.schema = data.items.length ? data.items : []
        this.schemaCopy = JSON.parse(JSON.stringify(data.items || []))
        this.loading = false
      },

      async getFields() {
        this.loading = true;
        return new Promise(async (resolve, reject) => {
          try {

            let request = await this.$api.fields.getList({
              page: 1,
              numberPerPage: 100,
              parent_id: this.parentId
            })

            const items = request.data || []

            resolve({
              items
            })
          } catch (e) {
            reject(e)
          }
        })
      },

      async save () {
        this.$validator.validate().then(async valid => {
          if (valid) {

            this.schema.map( async field => {

              if (field.options.length) {
                field.optionsField = field.options.map(option => option)
              } else {
                delete field.optionsField
              }

              field.parent_id = this.parentId

              if (field.type != null)
                field.kind = field.type

              field.type === 'checkbox' && field.validationMin !== 1 ? '' : delete field.validationMin

              if ((field.type === 'radio' || field.type === 'checkbox') && field.optionsField.length > 0) {
                field.optionsField = field.optionsField.map((option) => {
                  return {
                    id: option.id,
                    value: option.value ? option.value : Math.random().toString(),
                    label: option.label
                  }
                })
              }


              if (field.id) {
                if (!_.isEqual(field, _.find(this.schemaCopy, { id: field.id }))) {
                  //EDIT
                  try {
                    await this.$api.fields.update(field)
                    this.getFields().then(this.bindData)
                  } catch (e) {

                  }
                }
              } else {
                //CREATE
                try {
                  await this.$api.fields.create(field)
                  this.getFields().then(this.bindData)
                } catch (e) {
                  console.log(e.response)
                }
              }
            })

            let fieldIdsToRemove = _.difference(this.schemaCopy.map(item => item.id), this.schema.map(item => item.id))

            if (fieldIdsToRemove.length > 0) {
              await this.$api.fields.drop(fieldIdsToRemove)
              this.getFields().then(this.bindData)
            }

          }
        })
      },

      // Methods component Form Generator

      updateSortId() {
        this.schema.map( (item, index) => item.sort_id = index)
      },

      addComponent() {
        this.schema.push({ component: 'h3', children: '', lastId: this.lastId++, options: [], sort_id: this.schema.length })
      },

      addField() {
        this.schema.push({ type: 'text', label: '', lastId: this.lastId++, options: [], disabled: true, sort_id: this.schema.length })
      },

      changeField(input, index) {
        if (input.type === 'radio' || input.type === 'checkbox') {
          input.type === 'checkbox' ? input.validationMin = 1 : delete input.validationMin
          if (!input.options.length)
            this.createOption(index)
        } else {
          input.options = []
          delete input.validationMin
        }
      },

      deleteField(indexField) {
        this.schema.splice(indexField, 1)
        this.updateSortId()
      },

      createOption(index) {
        this.schema[index].options.push({label: '', id: this.lastId++})
      },

      deleteOption(input, indexOption) {
        if (input.options.length !== 1)
          input.options.splice(indexOption, 1)
      },

      startDialogSettings(input) {
        this.dialogSettings = true
        this.editedSettings = input
      },

    }
  }
</script>

<style lang="scss">

.borderField {
  border: solid 2px grey !important;
  border-radius: 5px;
  background-color: #fff;
}

.ghost {
  opacity: 0.4;
  background: #d79d84;
  border-radius: 5px;
}

.borderDrag {
  border-right: 2px solid rgba(158, 158, 158, .3);
}

.textOpacity {
  opacity: 0.5;
}

.v-btn:hover:before, .v-btn:focus:before {
  background-color: transparent !important;
}


// Vue Formulate Css

.v-label, .formulate-input-element-decorator {
  margin-bottom: 0;
}

h3 {
  margin-top: 0;
}

</style>

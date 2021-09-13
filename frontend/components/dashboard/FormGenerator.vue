<template>
  <div class="pt-4">
    <div class="text-right">

    </div>
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
    props: {
      parentId: {
        type: String,
        default: ''
      }
    },
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
                  console.log(option.value)
                  return {
                    id: option.id,
                    value: option.value ? option.value : Math.random().toString(),
                    label: option.label
                  }
                })
                console.log(field.optionsField)
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

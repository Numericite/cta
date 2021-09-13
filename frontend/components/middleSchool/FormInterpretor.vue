<template>
  <div>
    <FormulateForm ref="myForm" v-model="values" :schema="schema" class="formulate-form-custom"/>
    <button v-if="!previewForm" class="px-8 py-4 rounded-lg border-1 border-ms-red bg-ms-red text-white hover:text-ms-red hover:bg-white" @click="submit">Continuer</button>
  </div>
</template>

<script>
  import _ from 'lodash'

  export default {
    name: 'FormInterpretor',
    props: {
      fields: {
        type: Array,
        default: function() {
          return []
        }
      },
      fieldLogs: {
        type: Array,
        default: function() {
          return []
        }
      },
      previewForm: {
        type: Boolean,
        default: function() {
          return false
        }
      }
    },
    data() {
      return {
        schema: [],
        values: {}
      }
    },
    created() {
      this.schema = this.getSchemaFromFields(this.fields)
    },
    methods: {
      async submit() {
        const hasErrors = await this.$refs['myForm'].hasValidationErrors()

        if (!hasErrors) {
          const fieldLogs = []
          _.forEach(this.values, (value, key) => {
            const field = _.find(this.schema, {id: key})
            const fieldLog = _.find(this.fieldLogs, {field_id: key})

            fieldLogs.push({
              id: _.get(fieldLog, 'id', undefined),
              field_id: field.id,
              values: field.type === 'checkbox' ? value : [value],
              user_id: this.$store.state.auth.user.userID,
              child_id: this.$route.query.exploration ? this.$route.query.exploration : this.$store.state.auth.user.userID,
              child_type: this.$route.query.exploration ? 'exploration' : 'user',
              sort_id: field.sort_id,
              created_date: _.get(fieldLog, 'created_date', undefined),
              updated_date: _.get(fieldLog, 'updated_date', undefined)
            })
          })
          this.$emit('submit', fieldLogs)
        } else {
          this.$refs['myForm'].showErrors()
        }
      }
    }
  }
</script>

<style scoped>
</style>

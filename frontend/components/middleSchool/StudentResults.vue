<template>
  <div class="w-full pt-12">
    <div class="flex mb-4">
      <div class="flex w-1/3 justify-start">
        <button class="button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue hover:text-white" @click="printResults()">Imprimer</button>
      </div>
      <div class="flex w-2/3 justify-end">
        <button :class="filterKinds.length === 0 ? 'border-ms-green text-ms-green': 'text-ms-gray-light'"
                class="bg-white focus:outline-none mb-2 xl:mb-0 border-1 border-solid rounded-lg py-2 px-4 ml-4" @click="resetFilters()">Tous</button>
        <button v-for="kind in kinds" :class="(filterKinds.indexOf(kind.value) > -1) ? 'border-ms-red text-ms-red': 'text-ms-gray-light'" :key="kind.value"
                class="bg-white focus:outline-none mb-2 xl:mb-0 border-1 border-solid rounded-lg py-2 px-4 ml-4" @click="filterResults(kind.value, !(filterKinds.indexOf(kind.value) > -1))">{{ kind.text }}</button>
      </div>
    </div>
    <div id="printable-part">
      <Result v-for="result in results" :key="result.date" :data="result" />
      <div v-if="results.length === 0" class="bg-white p-4 rounded-lg shadow-lg">
        <div class="px-4 py-12 text-center border-1 border-solid border-ms-gray text-3xl rounded-lg italic">
          <span v-if="loading">
            <img src="~/assets/img/loader.gif" class="block mx-auto w-16 h-16">
            <div class="italic text-normal mt-4 text-ms-gray">Chargement...</div>
          </span>
          <span v-else>Aucun résultat à afficher pour le moment...</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import _ from 'lodash'
  import Result from './Result'

  export default {
    name: 'StudentResults',
    components: { Result },
    props: {
      user_id: {
        type: String,
        default: ''
      },
      fullname: {
        type: String,
        default: null
      }
    },
    data () {
      return {
        results: [],
        fullResults: [],
        filterKinds: [],
        kinds: [
          {text: 'Séquence', value: 'module'},
          {text: 'Explorations', value: 'exploration'},
          {text: 'Choix', value: 'lead'},
        ],
        loading: true
      }
    },
    async created() {
      await this.getResults()
    },
    methods: {
      async getResults() {
        try {
          let response = await this.$api.user.getStudentResults({user_id: this.user_id})
          const results = response.data || []

          this.results = _.orderBy(
            _.map(results, (result) => {
              if (result.fields) {
                result.fields.forEach((field) => {
                  const log = _.find(result.logs, { field_id: field.id })

                  if (log) {
                    field.value = (field.kind === 'checkbox') ? log.values : (log.values[0] || '')

                    if (field.optionsField) {
                      field.optionsField = field.optionsField.filter((option) => {
                        return _.includes(field.value, option.value) || (field.value === option.value)
                      })
                    }
                  }
                })

                result.fields = _.orderBy(result.fields, ['sort_id'], ['ASC'])
              }

              if (result.children) {
                result.children.filter((child) => {
                  child.fields.forEach((field) => {
                    const log = _.find(child.logs, { field_id: field.id })

                    if (log) {
                      field.value = (field.kind === 'checkbox') ? log.values : (log.values[0] || '')

                      if (field.optionsField) {
                        field.optionsField = field.optionsField.filter((option) => {
                          return _.includes(field.value, option.value) || (field.value === option.value)
                        })
                      }
                    }
                  })
                })
              }

              return result
            }),
            ['date'], ['asc']
          )

          this.fullResults = this.results

          this.loading = false
        } catch (e) {
          console.log(e)
        }
      },

      filterResults(kind, isAdd) {
        if (isAdd) {
          this.filterKinds.push(kind)
        } else {
          this.filterKinds = _.without(this.filterKinds, kind)
        }

        if (this.filterKinds.length > 0) {
          this.results = _.filter(this.fullResults, (result) => {
            return _.includes(this.filterKinds, result.kind)
          })
        } else {
          this.resetFilters()
        }
      },

      resetFilters() {
        this.filterKinds = []
        this.results = this.fullResults
      },

      printResults() {
        const printContents = this.$store.state.auth.user.config.accountType === 'teacher' ?
          '<h1 class="ms-title w-full text-center" style="margin-top: 50px; margin-bottom: 25px;">' + this.fullname + '</h1>' + document.getElementById('printable-part').innerHTML
          : document.getElementById('printable-part').innerHTML;

        const css = '@page { size: landscape; margin: 0; }',
          head = document.head || document.getElementsByTagName('head')[0],
          style = document.createElement('style');

        if (style.styleSheet){
          style.styleSheet.cssText = css;
        } else {
          style.appendChild(document.createTextNode(css));
        }

        head.appendChild(style);
        document.body.innerHTML = head.innerHTML + printContents;

        setTimeout(() => {
          window.print();
          this.$router.go()
        })
      }
    }
  }
</script>

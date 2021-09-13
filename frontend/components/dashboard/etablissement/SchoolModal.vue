<template>
  <div class="final fixed pin-l pint-t pin-b w-screen h-screen z-index-modal flex justify-center items-center px-3 sm:py-3">

    <div class="layer" @click="closeModal"/>

    <div class="onboardingCard h-3/4 w-1/2 sm:w-full bg-white pt-8 pb-4 rounded-lg flex flex-col items-center sm:my-3 relative">
      <div class="flex w-full px-10">
        <input v-model="schoolName" type="text" placeholder="nom de l'école" class="outline-none border w-full border-blue border-2 px-4 py-4 mr-4 rounded-lg"
               @keyup="getSchools" >
      </div>
      <div class="overflow-y-auto w-full pb-24">
        <div v-for="school in schoolsFiltered" :key="school.code_uai" class="w-full px-10 pt-10">
          <school-info :school="school" @reloadOnisepFeedback="reloadOnisepFeedback"/>
        </div>
        <div v-if="!schoolsFiltered.length && showError" class="flex w-full text-red text-xl pt-10 pl-10">
          Aucun résultat.
        </div>
      </div>
      <div class="w-full flex p-10 absolute pin-b bg-white pb-6 mb-2">
        <button class="bg-blue button" @click="closeModal">Fermer</button>
      </div>
    </div>
  </div>
</template>

<script>
  import Button from '../../../mixins/button'
  import SchoolInfo from './SchoolInfo'
  import _ from 'lodash'
  export default {
    name: 'Modal',
    components: { SchoolInfo, Button },
    props: {
      onisepFeedbacks: {
        type: Array,
        default: function() {
          return []
        }
      }
    },
    data() {
      return {
        showError: false,
        school: {},
        schoolName: '',
        schoolsFiltered: [],
        schoolSelected: '',
        schoolListSelected: [],
      }
    },
    // watch: {
    //   schoolName: function() {
    //     this.getSchools()
    //   }
    // },
    methods: {
      closeModal() {
        this.$emit("closeModal")
      },
      async getSchools() {

        if(this.schoolName.length) {
          const code_uais = _.map(this.onisepFeedbacks, 'code_uai')
          let response = await this.$api.onisep.getOnisepSchool({size: 20, query: this.schoolName})
          this.schoolsFiltered = _.map(response.data.results, (school) => {
            school.isSelected = _.includes(code_uais, school.code_uai)
            return school
          })
          this.$forceUpdate()
          if(!this.schoolsFiltered.length) {
            this.showError = true
          }
        }

      },
      reloadOnisepFeedback() {
        this.$emit('reloadOnisepFeedback')
      },
    },
  }
</script>


<style lang="scss" scoped>
  .v-autocomplete {
    .v-autocomplete-input-group {
      .v-autocomplete-input {
        padding: 20px !important;
        border-bottom: 2px solid blue !important;
      }
    }
  }
</style>

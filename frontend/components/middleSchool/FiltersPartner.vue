<template>
  <div class="flex flex-col mdmin:flex-row w-full justify-between items-start pb-10">
    <div class="flex flex-col mdmin:flex-row items-start">
      <div v-if="account.accountType !== 'school'" class="relative flex">
        <FormulateInput v-model="schoolFilter"
                        :outerClass="['my-2 mdmin:mx-0 no-arrow']"
                        :inputClass="['ms-text focus:outline-none mb-2 xl:mb-0 border-1 border-solid rounded-lg py-3 pl-4 pr-7', schoolFilter ? 'text-ms-red border-ms-red' : 'text-ms-green border-ms-green']"
                        :options="createOptionsArray(schools, 'Tous les établissements')"
                        type="select" name="schoolFilter" @change="changeFilter(filters, 'school', 'partner', data, app)">
          <template #suffix="">
            <div :class="schoolFilter ? 'text-ms-red' : 'text-ms-green'" class="absolute chevron-down flex items-center mr-4">
              <svg width="6" height="5" viewBox="0 0 6 5" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path fill="currentColor" d="M0.143658 1.54844C0.0645627 1.47017 0.0645626 1.34239 0.143658 1.26412L0.64883 0.764212C0.726762 0.687092 0.852255 0.687092 0.930187 0.764212L3 2.81244L5.06981 0.764212C5.14775 0.687092 5.27324 0.687092 5.35117 0.764212L5.85634 1.26412C5.93544 1.34239 5.93544 1.47017 5.85634 1.54844L3.14068 4.23579C3.06275 4.31291 2.93725 4.31291 2.85932 4.23579L0.143658 1.54844Z"/>
              </svg>
            </div>
          </template>
        </FormulateInput>
      </div>
      <div class="relative flex">
        <FormulateInput v-model="gradeFilter"
                        :outerClass="['my-2 mdmin:mx-0 no-arrow', account.accountType !== 'school' ? 'mdmin:ml-4' : '']"
                        :inputClass="['ms-text focus:outline-none mb-2 xl:mb-0 border-1 border-solid rounded-lg py-3 pl-4 pr-7', gradeFilter ? 'text-ms-red border-ms-red' : 'text-ms-green border-ms-green']"
                        :options="createOptionsArray(data.grades.length ? data.grades : gradesAll, 'Tous les niveaux')"
                        type="select" name="gradeFilter" @change="changeFilter(filters, 'grade', 'partner', data, app)">
          <template #suffix="">
            <div :class="gradeFilter ? 'text-ms-red' : 'text-ms-green'" class="absolute chevron-down flex items-center mr-4">
              <svg width="6" height="5" viewBox="0 0 6 5" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path fill="currentColor" d="M0.143658 1.54844C0.0645627 1.47017 0.0645626 1.34239 0.143658 1.26412L0.64883 0.764212C0.726762 0.687092 0.852255 0.687092 0.930187 0.764212L3 2.81244L5.06981 0.764212C5.14775 0.687092 5.27324 0.687092 5.35117 0.764212L5.85634 1.26412C5.93544 1.34239 5.93544 1.47017 5.85634 1.54844L3.14068 4.23579C3.06275 4.31291 2.93725 4.31291 2.85932 4.23579L0.143658 1.54844Z"/>
              </svg>
            </div>
          </template>   
        </FormulateInput>
      </div>
      <div class="relative flex">
        <FormulateInput v-model="classFilter"
                        :outerClass="['my-2 mdmin:mx-0 mdmin:ml-4 no-arrow']"
                        :inputClass="['ms-text focus:outline-none mb-2 xl:mb-0 border-1 border-solid rounded-lg py-3 pl-4 pr-7', classFilter ? 'text-ms-red border-ms-red' : '', !schoolFilter && !gradeFilter && account.accountType !== 'school' ? 'text-ms-gray-light border-ms-gray' : 'text-ms-green border-ms-green']"
                        :options="createOptionsArray(data.classes, 'Toutes les classes')" :disabled="!schoolFilter && !gradeFilter && account.accountType !== 'school'"
                        type="select" name="classFilter" @change="emitAll()">
          <template #suffix="">
            <div :class="[classFilter ? 'text-ms-red' : '', !schoolFilter && !gradeFilter && account.accountType !== 'school' ? 'text-ms-gray-light' : 'text-ms-green']" class="absolute chevron-down flex items-center mr-4">
              <svg width="6" height="5" viewBox="0 0 6 5" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path fill="currentColor" d="M0.143658 1.54844C0.0645627 1.47017 0.0645626 1.34239 0.143658 1.26412L0.64883 0.764212C0.726762 0.687092 0.852255 0.687092 0.930187 0.764212L3 2.81244L5.06981 0.764212C5.14775 0.687092 5.27324 0.687092 5.35117 0.764212L5.85634 1.26412C5.93544 1.34239 5.93544 1.47017 5.85634 1.54844L3.14068 4.23579C3.06275 4.31291 2.93725 4.31291 2.85932 4.23579L0.143658 1.54844Z"/>
              </svg>
            </div>
          </template>
        </FormulateInput>
      </div>
    </div>
    <FormulateInput v-if="typePage !== 'stats'"
                    v-model="fullName"
                    :outerClass="['my-2 sm:ml-0 ml-4']"
                    :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-ms-green text-ms-gray-dark']"
                    type="text"
                    placeholder="Rechercher..."
                    @change="emitAll()" />
  </div>
</template>

<script>

import { useOptionsArray, useClassrooms, useEmitAll, useChangeFilter } from '../../composition/FilterPartner'
import { reactive, toRefs, useContext, onMounted, onBeforeMount, useAsync } from '@nuxtjs/composition-api'

export default {
  props: {
    account: {
      type: Object,
      default: function() {
        return {}
      }
    },
    gradesAll: {
      type: Array,
      default: function() {
        return []
      }
    },
    schools: {
      type: Array,
      default: function() {
        return []
      }
    },
    typePage: {
      type: String,
      default: function() {
        return ''
      }
    }
  },
  setup(props, context) {

    const { app } =  useContext()

    const filters = reactive({
      schoolFilter: '',
      gradeFilter: '',
      classFilter: '',
      fullName: ''
    })

    const data = reactive({
      grades: [],
      classes: [],
      props: props
    })

    const emitAll = () => useEmitAll(filters, data, context)
    const getClassrooms = () => useClassrooms(filters, data, app)

    const updateFilters = async () => {
      data.classes = await getClassrooms()
      emitAll()
    }

    useAsync(  () => {
      updateFilters()
    })

    return {
      ...toRefs(filters),
      filters,
      data,
      ...useOptionsArray(),
      ...useChangeFilter(context),
      emitAll,
      updateFilters,
      app
    }
  },
}


</script>

<style lang="scss">

option {
  @apply text-ms-gray-dark;
}

.chevron-down {
  top: 0;
  bottom: 0;
  right: 0;
  pointer-events: none;
}

</style>

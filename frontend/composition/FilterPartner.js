import { reactive, isReactive } from '@nuxtjs/composition-api'
import { GRADES } from '~/config'
import { useRestrictions } from './ExplorationModal'

export const useOptionsArray = () => {

  const arrOptions = reactive([])

  const createOptionsArray = (options, firstValue = null) => {

    if (typeof options[0] === 'object') {
      arrOptions.value = options.map(item => { return { id: item.id, value: item.id, label: item.name }})
    } else {
      arrOptions.value = options.map(item => { return { id: item, value: item.toString(), label: firstValue.includes('niveaux') ? GRADES[item] : item } })
    }

    if (firstValue) arrOptions.value.unshift({ value: '', label: firstValue })

    return arrOptions.value
  }

  return { createOptionsArray }
}

export const useChangeFilter = (context) => {


  const changeFilter = async (filters, type, typeData, data, app) => {

    filters.classFilter = ''

    if (type === 'school') {

      filters.gradeFilter = ''

      if (filters.schoolFilter) {
        try {
          let response = await app.$api.schools.getGradesBySchools([filters.schoolFilter])
          data.grades = response.data.sort() || []
        } catch (e) {
          console.log(e)
        }
      } else {
        data.grades = []
      }

    }

    data.classes = await useClassrooms(filters, data, app)

    await useEmitAll(filters, data, context)

  }

  return { changeFilter }

}

export async function useClassrooms({ schoolFilter, gradeFilter }, data, app) {

  try {
    let response = await app.$api.schools.getClassrooms({ school_ids: [schoolFilter || data.props.account?.school_ids], grade: gradeFilter || undefined, page: 1, numberPerPage: 1000 })
    return response.data
  } catch (e) {
    console.log(e)
  }


}

export function useEmitAll({ schoolFilter, gradeFilter, classFilter, fullName }, { classes, props }, context) {

  context.emit('getDataFilters', {
    school_ids: schoolFilter || props.account?.school_ids,
    grade: gradeFilter || undefined,
    class_id: classFilter || undefined,
    full_name: fullName || undefined,
    classes
  })

}

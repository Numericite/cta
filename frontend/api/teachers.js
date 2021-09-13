import BaseApi from './base'
import qs from 'qs'

export default class TeachersApi extends BaseApi {

  count(params) {
    return this.api.get('teacher/count', {
      params: {
        school_ids: params.school_ids,
        grade: params.grade,
        classroom_id: params.classroom_id
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getList(params) {
    return this.api.get('teacher/list', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        school_ids: params.school_ids,
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getTeachers(params) {
    return this.api.get('teacher/list/filters', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        school_ids: params.school_ids,
        grade: params.grade,
        classroom_id: params.classroom_id
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  countStudents(params) {
    return this.api.get('teacher/count/students', {
      params: {
        classroom_ids: params.classroom_ids
      },
      paramsSerializer: params => {
        return qs.stringify( params, { arrayFormat: 'repeat' } )
      }
    } )
  }

  getStudents( params ) {
    return this.api.get( 'teacher/list/students', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        classroom_ids: params.classroom_ids
      },
      paramsSerializer: params => {
        return qs.stringify( params, { arrayFormat: 'repeat' } )
      }
    } )
  }

  getStudentsByFullName(params) {
    return this.api.get('teacher/search/students/' + params.fullName, {
      params: {
        classroom_ids: params.classroom_ids
      },
      paramsSerializer: params => {
        return qs.stringify( params, { arrayFormat: 'repeat' } )
      }
    } )
  }

  searchByFullName(params) {
    return this.api.get('teacher/search/' + params.fullName)
  }
}

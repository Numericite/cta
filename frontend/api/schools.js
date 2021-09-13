import BaseApi from './base'
import qs from 'qs'

export default class SchoolsApi extends BaseApi {
  getSchools() {
    return this.api.get( 'school/list?page=1&numberPerPage=1000' )
  }

  getSchoolsByIds( ids ) {
    return this.api.get( 'school/list/ids', {
      params: {
        page: 1,
        numberPerPage: 1000,
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify( params, { arrayFormat: 'repeat' } )
      }
    } )
  }

  getGradesBySchools( schoolIds ) {
    return this.api.get( '/school/list/grades', {
      params: {
        school_ids: schoolIds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  countClassrooms(params) {
    return this.api.get('schoolclassroom/count', {
      params: {
        school_ids: params.school_ids,
        grade: params.grade
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getClassrooms(params) {
    return this.api.get('schoolclassroom/list', {
      params: {
        school_ids: params.school_ids,
        grade: params.grade,
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify( params, { arrayFormat: 'repeat' } )
      }
    } )
  }

  getClassroomsByIds( ids ) {
    return this.api.get( 'schoolclassroom/list/ids', {
      params: {
        page: 1,
        numberPerPage: 1000,
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify( params, { arrayFormat: 'repeat' } )
      }
    } )
  }
}

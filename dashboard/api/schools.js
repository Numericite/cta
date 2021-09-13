import BaseApi from './base'
import qs from 'qs'

export default class SchoolsApi extends BaseApi {
  count(params) {
    return this.api.get('school/count', {
      params: {
        kinds: params.kinds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getList(params) {
    return this.api.get('school/list', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        kinds: params.kinds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getByIds(ids) {
    return this.api.get('school/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('school', params)
  }

  update(params) {
    return this.api.post('school/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('school', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  uploadLogo(id, file) {
    return this.api.post('school/logos/' + id, file)
  }

  // -------------------------------- //
  // ------ CLASSROOMS SECTION ------ //
  // -------------------------------- //

  createClassroom(classroom) {
    return this.api.post('schoolclassroom', classroom)
  }

  updateClassroom(classroom) {
    return this.api.post('schoolclassroom/' + classroom.id, classroom)
  }

  getClassroomByIds(ids) {
    return this.api.get('schoolclassroom/list/ids', {
      params: {
        ids: ids
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
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  countClassrooms(schoolIds) {
    return this.api.get('schoolclassroom/count', {
      params: {
        school_ids: schoolIds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  dropClassrooms(classroomIds) {
    return this.api.delete('schoolclassroom', {
      params: {
        ids: classroomIds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

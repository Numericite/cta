import BaseApi from './base'
import qs from 'qs'

export default class DocumentsApi extends BaseApi {

  create(params) {
    return this.api.post('document/create', params)
  }

  update(params) {
    return this.api.post('/document/update/' + params.id, params)
  }

  getList(params) {
    return this.api.get('/document/list', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        parent_type: params.parent_type,
        parent_ids: params.parent_ids,
        school_kinds: params.school_kinds,
        grades: params.grades
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  count(params) {
    return this.api.get('/document/count', {
      params: {
        parent_type: params.parent_type,
        school_kinds: params.school_kinds,
        grades: params.grades
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  upload(parent_type, file) {
    return this.api.post('/document/' + parent_type, file)
  }

  drop( ids ) {
    return this.api.delete( '/document', {
      params: {ids: ids},
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

}

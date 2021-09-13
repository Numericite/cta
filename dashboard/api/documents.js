import BaseApi from './base'
import qs from 'qs'

export default class DocumentsApi extends BaseApi {

  create(params) {
    return this.api.post('document/create', params)
  }

  upload(parent_type, file, grades, school_kind, only_upload) {
    if (grades)
      file.append("grades", JSON.stringify(grades))
    if (school_kind)
      file.append("school_kind", school_kind)
    file.append("only_upload", only_upload ? only_upload : false)
    return this.api.post('document/' + parent_type, file)
  }

  update(params) {
    return this.api.post('/document/update/' + params.id, params)
  }

  getList(params) {
    return this.api.get('document/list', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        parent_type: params.parent_type,
        parent_types: params.parent_types,
        parent_ids: params.parent_ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  count(params) {
    return this.api.get('document/count', {
      params: {
        parent_type: params.parent_type,
        parent_ids: params.parent_ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  drop(ids) {
    return this.api.delete('document', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

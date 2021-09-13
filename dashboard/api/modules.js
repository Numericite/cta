import BaseApi from './base'
import qs from 'qs'

export default class ModuleApi extends BaseApi {

  count(params) {
    return this.api.get('module/count', {
      params: {
        grade: params.grade
      }
    })
  }

  getList(params) {
    return this.api.get('module/list', {
      params: params
    })
  }

  getByIds(ids) {
    return this.api.get('module/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('module', params)
  }

  update(params) {
    return this.api.post('module/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('module', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

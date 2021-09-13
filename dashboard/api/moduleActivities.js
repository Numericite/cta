import BaseApi from './base'
import qs from 'qs'

export default class ModuleActivityApi extends BaseApi {
  count(params) {
    return this.api.get('moduleactivity/count', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getList(params) {
    return this.api.get('moduleactivity/list', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getByIds(ids) {
    return this.api.get('moduleactivity/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('moduleactivity', params)
  }

  uploadDocument(id, file) {
    return this.api.post('moduleactivity/documents/' + id, file)
  }

  update(params) {
    return this.api.post('moduleactivity/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('moduleactivity', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

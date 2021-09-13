import BaseApi from './base'
import qs from 'qs'

export default class domainsApi extends BaseApi {

  count(params) {
    return this.api.get('domain/count', {
      params: {
        kind: params.kind
      }
    })
  }

  getList(params) {
    return this.api.get('domain/list', {
      params: params
    })
  }

  getByIds(ids) {
    return this.api.get('domain/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('domain', params)
  }

  update(params) {
    return this.api.post('domain/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('domain', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  uploadLogo(id, file) {
    return this.api.post('domain/logos/' + id, file)
  }
}

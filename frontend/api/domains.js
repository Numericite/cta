import BaseApi from './base'
import qs from 'qs'

export default class DomainsApi extends BaseApi {
  count() {
    return this.api.get('domain/count')
  }

  getList(params) {
    return this.api.get('domain/list', {
      params: params
    })
  }

  getByIds(params) {
    return this.api.get('domain/list/ids', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

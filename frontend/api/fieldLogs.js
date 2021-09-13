import BaseApi from './base'
import qs from 'qs'

export default class FieldLogApi extends BaseApi {
  create(params) {
    return this.api.post('/fieldlog', params)
  }

  update(params) {
    return this.api.post('/fieldlog/' + params.id, params)
  }

  getList(params) {
    return this.api.get('fieldlog/list', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  count(params) {
    return this.api.get('fieldlog/count', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

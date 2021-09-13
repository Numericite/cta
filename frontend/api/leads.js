import BaseApi from './base'
import qs from 'qs'

export default class LeadsApi extends BaseApi {

  count(params) {
    return this.api.get('lead/count', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  get(params) {
    return this.api.get('lead/list', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  update(params) {
    return this.api.post('/lead/' + params.id, params)
  }

  create(params) {
    return this.api.post('/lead', params)
  }

  getByIds(params) {
    return this.api.get('lead/list/ids', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

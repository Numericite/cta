import BaseApi from './base'
import qs from 'qs'

export default class BookingsApi extends BaseApi {
  create(params) {
    return this.api.post('/booking', params)
  }

  update(params) {
    return this.api.post('/booking/' + params.id, params)
  }

  count(params) {
    return this.api.get('booking/count', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getList(params) {
    return this.api.get('booking/list', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  delete(params) {
    return this.api.delete('/booking', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  removeByParentIds(params) {
    return this.api.delete('/booking/parents', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

import BaseApi from './base'
import qs from 'qs'

export default class EventsApi extends BaseApi {
  create(params) {
    return this.api.post('/event', params)
  }

  update(params) {
    return this.api.post('/event/' + params.id, params)
  }

  count(params) {
    return this.api.get('event/count', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getList(params) {
    return this.api.get('event/list', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getByIds(params) {
    return this.api.get('event/list/ids', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getByUserIds(params) {
    return this.api.get('event/list/users', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  delete(params) {
    return this.api.delete('/event', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

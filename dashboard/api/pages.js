import BaseApi from './base'
import qs from 'qs'

export default class PageApi extends BaseApi {
  count() {
    return this.api.get('page/count')
  }

  getList(params) {
    return this.api.get('page/list', {
      params: params
    })
  }

  getByIds(ids) {
    return this.api.get('page/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('page', params)
  }

  update(params) {
    return this.api.post('page/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('page', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

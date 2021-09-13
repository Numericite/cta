import BaseApi from './base'
import qs from 'qs'

export default class explorationsApi extends BaseApi {

  count(params) {
    return this.api.get('exploration/count', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getList(params) {
    return this.api.get('exploration/list', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
  getExplorationByIds(ids) {
    return this.api.get('/exploration/list/ids', {
      params: {
        ids: ids,
        page: 1,
        numberPerPage: 1000
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
  create(params) {
    return this.api.post('/exploration', params)
  }
}

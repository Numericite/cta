import BaseApi from './base'
import qs from 'qs'

export default class VersionApi extends BaseApi {
  count() {
    return this.api.get('version/count')
  }

  getList(params) {
    return this.api.get('version/list', {
      params: params
    })
  }

  getByIds(ids) {
    return this.api.get('version/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getAvailableNames() {
    return this.api.get('/version/file/names')
  }

  create(params) {
    return this.api.post('version', params)
  }

  update(params) {
    return this.api.post('version/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('version', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

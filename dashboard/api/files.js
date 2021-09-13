import BaseApi from './base'
import qs from 'qs'

export default class FileApi extends BaseApi {
  count() {
    return this.api.get('file/count')
  }

  getList(params) {
    return this.api.get('file/list', {
      params: params
    })
  }

  getTree() {
    return this.api.get('file/tree')
  }

  getByIds(ids) {
    return this.api.get('file/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('file', params)
  }

  update(params) {
    return this.api.post('file/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('file', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

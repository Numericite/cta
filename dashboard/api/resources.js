import BaseApi from './base'
import qs from 'qs'

export default class ResourceApi extends BaseApi {

  count(params) {
    return this.api.get('resource/count', {
      params: {
        parent_type: params.parent_type
      }
    })
  }

  getList(params) {
    return this.api.get('resource/list', {
      params: params
    })
  }

  create(params) {
    return this.api.post('resource', params)
  }

  update(params) {
    return this.api.post('resource/' + params.id, params)
  }

  uploadLogo(id, file) {
    return this.api.post('resource/logos/' + id, file)
  }

  drop(ids) {
    return this.api.delete('resource', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

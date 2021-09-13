import BaseApi from './base'
import qs from 'qs'

export default class PostApi extends BaseApi {
  count() {
    return this.api.get('post/count')
  }

  getList(params) {
    return this.api.get('post/list', {
      params: params
    })
  }

  create(params) {
    return this.api.post('post', params)
  }

  update(params) {
    return this.api.post('post/' + params.id, params)
  }

  uploadLogo(id, file) {
    return this.api.post('post/logos/' + id, file)
  }

  drop(ids) {
    return this.api.delete('post', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

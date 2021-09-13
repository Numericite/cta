import BaseApi from './base'
import qs from 'qs'

export default class InternshipApi extends BaseApi {
  count() {
    return this.api.get('internship/count')
  }

  getList(params) {
    return this.api.get('internship/list', {
      params: params
    })
  }

  create(params) {
    return this.api.post('internship', params)
  }

  update(params) {
    return this.api.post('internship/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('internship', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

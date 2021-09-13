import BaseApi from './base'
import qs from 'qs'

export default class CompanyApi extends BaseApi {
  count() {
    return this.api.get('company/count')
  }

  getList(params) {
    return this.api.get('company/list', {
      params: params
    })
  }

  create(params) {
    return this.api.post('company', params)
  }

  update(params) {
    return this.api.post('company/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('company', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

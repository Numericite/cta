import BaseApi from './base'
import qs from 'qs'

export default class ExperiencesApi extends BaseApi {
  getList(params) {
    return this.api.get('experience/list', {
      params: params
    })
  }

  updateMine(params) {
    return this.api.post('/experience/mine/' + params.id + '/' + params.user_id, params)
  }

  create(params) {
    return this.api.post('/experience', params)
  }

  deleteMine(params) {
    return this.api.delete('/experience/mine/' + params.user_id, {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

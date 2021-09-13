import BaseApi from './base'
import qs from 'qs'

export default class OnisepFeedBack extends BaseApi {
  get(params) {
    return this.api.get('onisepfeedback/list', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage
      },
    })
  }
  // GET BY IDS
  getByIds(params) {
    return this.api.get('onisepfeedback/list/ids', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  delete(params) {
    return this.api.delete('/onisepfeedback', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
  // DELETE BY CODE_UAI
  deleteByCodeUais(params) {
    return this.api.delete('/onisepfeedback/uais', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
  update(params) {
    return this.api.post('/onisepfeedback/' + params.id, params)
  }
  create(params) {
    return this.api.post('/onisepfeedback', params)
  }
}

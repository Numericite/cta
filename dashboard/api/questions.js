import BaseApi from './base'
import qs from 'qs'

export default class QuestionsApi extends BaseApi {
  count() {
    return this.api.get('faq/count')
  }

  getList(params) {
    return this.api.get('faq/list', {
      params: params
    })
  }

  create(params) {
    return this.api.post('faq', params)
  }

  update(params) {
    return this.api.post('faq/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('faq', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

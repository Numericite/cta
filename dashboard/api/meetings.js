import BaseApi from './base'
import qs from 'qs'

export default class MeetingApi extends BaseApi {
  count() {
    return this.api.get('meeting/count')
  }

  getList(params) {
    return this.api.get('meeting/list', {
      params: params
    })
  }

  create(params) {
    return this.api.post('meeting', params)
  }

  update(params) {
    return this.api.post('meeting/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('meeting', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

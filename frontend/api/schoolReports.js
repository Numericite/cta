import BaseApi from './base'
import qs from 'qs'

export default class SchoolReportsApi extends BaseApi {
  create(params) {
    return this.api.post('/schoolreport', params)
  }

  update(params) {
    return this.api.post('/schoolreport/' + params.id, params)
  }

  getList(params) {
    return this.api.get('schoolreport/list', {
      params: {
        user_ids: params.user_ids,
        stream_ids: params.stream_ids,
        page: 1,
        numberPerPage: 1000
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

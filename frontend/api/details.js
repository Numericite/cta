import BaseApi from './base'
import qs from 'qs'

export default class DetailsApi extends BaseApi {
  count(params) {
    return this.api.get('detail/count', {
      params: {
        domain_ids: params.domain_ids,
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getList(params) {
    return this.api.get('detail/list', {
      params: {
        domain_ids: params.domain_ids,
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

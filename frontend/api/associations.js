import BaseApi from './base'
import qs from 'qs'

export default class AssociationsApi extends BaseApi {
  create( params ) {
    return this.api.post( '/association', params )
  }

  count(params) {
    return this.api.get('association/count', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getList(params) {
    return this.api.get('association/list', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

import BaseApi from './base'
import qs from 'qs'

export default class RestrictionsApi extends BaseApi {
  create( params ) {
    return this.api.post( '/restriction', params )
  }

  getRestrictionList( params ) {
    return this.api.get( '/restriction/list', {
      params: {
        page: 1,
        numberPerPage: 1000,
        exploration_type_id: params.exploration_type_id
      }
    } )
  }

  updateRestriction( params ) {
    return this.api.post( '/restriction/' + params.id, params )
  }

  drop( ids ) {
    return this.api.delete( '/restriction', {
      params: {ids: ids},
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

}

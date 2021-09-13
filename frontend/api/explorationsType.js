import BaseApi from './base'
import qs from 'qs'

export default class explorationsApi extends BaseApi {

  createExplorationType( params ) {
    return this.api.post( '/explorationtype', params )
  }

  count( params ) {
    return this.api.get( '/explorationtype/count', params )
  }

  getList( params ) {
    return this.api.get( 'explorationtype/list', {
      params: {
        partner_slug: params.partner_slug,
        page: params.page,
        numberPerPage: params.numberPerPage,
        exclude_partner: params.exclude_partner
      },
      paramsSerializer: params => {
        return qs.stringify( params, { arrayFormat: 'repeat' } )
      }
    } )
  }

  getListByRestriction( params ) {
    return this.api.get('explorationtype/list/restriction', {
      params: {
        classroom_id: params.classroom_id
      }
    })
  }

  updateExplorationType( params ) {
    return this.api.post( 'explorationtype/' + params.id, params )
  }

  getExplorationTypeByIds( ids ) {
    return this.api.get( '/explorationtype/list/ids', {
      params: {
        ids: ids,
        page: 1,
        numberPerPage: 1000
      },
      paramsSerializer: params => {
        return qs.stringify( params, { arrayFormat: 'repeat' } )
      }
    } )
  }

  delete( params ) {
    return this.api.delete( '/explorationtype', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify( params, { arrayFormat: 'repeat' } )
      }
    } )
  }
}

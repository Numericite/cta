import BaseApi from './base'
import qs from 'qs'

export default class PartnersApi extends BaseApi {


  getList(params) {
    return this.api.get('partner/list', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        school_ids: params.school_ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getPartnersByIds(ids) {
    return this.api.get('partner/list/ids', {
      params: {
        ids: ids,
        page: 1,
        numberPerPage: 1000
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  countExplorationsType(params) {
    return this.api.get('partner/count/explorationstype', {
      params: {
        partner_slug: params.partner_slug,
        school_ids: params.school_ids,
        grade: params.grade,
        name: params.name,
        classroom_id: params.classroom_id,
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getExplorationsType(params) {
    return this.api.get('partner/list/explorationstype', {
      params: {
        partner_slug: params.partner_slug,
        school_ids: params.school_ids,
        grade: params.grade,
        classroom_id: params.classroom_id,
        name: params.name,
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getPartnerStats(params) {
    return this.api.get('partner/stats', {
      params: {
        partner_slug: params.partner_slug,
        classroom_id: params.classroom_id,
        school_ids: params.school_ids,
        grade: params.grade
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }


}

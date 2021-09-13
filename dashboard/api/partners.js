import BaseApi from './base'
import qs from 'qs'

export default class PartnerApi extends BaseApi {
  count() {
    return this.api.get('partner/count')
  }

  getList(params) {
    return this.api.get('partner/list', {
      params: params
    })
  }

  getPartnerStats(params) {
    return this.api.get('partner/stats', {
      params: {
        partner_slug: params.partner_slug,
        classroom_id: params.classroom_id,
        school_ids: params.school_ids,
        grade: params.grade,
        kind: params.kind
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('partner', params)
  }

  update(params) {
    return this.api.post('partner/' + params.id, params)
  }

  uploadLogo(id, file) {
    return this.api.post('partner/logos/' + id, file)
  }

  drop(ids) {
    return this.api.delete('partner', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

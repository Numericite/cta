import BaseApi from './base'
import qs from 'qs'

export default class DataApi extends BaseApi {
  getData(params) {
    return this.api.get('data/admin', {
      params: {
        from_date: 0,
        to_date: 0,
        school_ids: params.school_ids,
        classroom_ids: params.classroom_ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

import BaseApi from './base'
import qs from 'qs'

export default class DataApi extends BaseApi {
  getData(accountType, schoolType = 'high-school', params) {
    if (accountType === 'collectivity')
      return this.api.get('data/collectivity', {
        params : {
          from_date: 0,
          to_date: 0,
          school_ids: params.school_ids,
          classroom_ids: params.classroom_ids
        },
        paramsSerializer: params => {
          return qs.stringify(params, {arrayFormat: 'repeat'})
        }
      })
    else
      return this.api.get('data/' + schoolType, {
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

  getDataStats(params) {
    return this.api.get('data/stats', {
      params: {
        school_ids: params.school_ids,
        grade: params.grade,
        classroom_id: params.classroom_id
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

}

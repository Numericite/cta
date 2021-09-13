import BaseApi from './base'
import qs from 'qs'

export default class CoursesApi extends BaseApi {
  getCourses(params) {
    return this.api.get('course/list', {
      params: params
    })
  }

  getByIds(ids) {
    return this.api.get('course/list/ids', {
      params: {
        page: 1,
        numberPerPage: 1000,
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

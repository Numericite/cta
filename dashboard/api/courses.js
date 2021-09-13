import BaseApi from './base'
import qs from 'qs'

export default class CourseApi extends BaseApi {
  count() {
    return this.api.get('course/count')
  }

  getList(params) {
    return this.api.get('course/list', {
      params: params
    })
  }

  getByIds(ids) {
    return this.api.get('course/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('course', params)
  }

  update(params) {
    return this.api.post('course/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('course', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

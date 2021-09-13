import BaseApi from './base'
import qs from 'qs'

export default class FieldApi extends BaseApi {
  count(params) {
    return this.api.get('field/count', {
      params: params
    })
  }

  getList(params) {
    return this.api.get('field/list', {
      params: params
    })
  }

  getByIds(ids) {
    return this.api.get('field/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('field', params)
  }

  update(params) {
    return this.api.post('field/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('field', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

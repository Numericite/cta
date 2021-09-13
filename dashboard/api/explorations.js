import BaseApi from './base'
import qs from 'qs'

export default class ExplorationApi extends BaseApi {
  count(params) {
    return this.api.get('explorationtype/count', {
      params: params
    })
  }

  getList(params) {
    return this.api.get('explorationtype/list', {
      params: params
    })
  }

  getByIds(ids) {
    return this.api.get('explorationtype/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('explorationtype', params)
  }

  update(params) {
    return this.api.post('explorationtype/' + params.id, params)
  }

  drop(ids) {
    return this.api.delete('explorationtype', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

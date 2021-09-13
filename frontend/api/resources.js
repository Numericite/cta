import BaseApi from './base'
import qs from 'qs'

export default class resourcessApi extends BaseApi {

  getList(params) {
    return this.api.get('resource/list', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        parent_type: params.parent_type
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  count(params) {
    return this.api.get('resource/count', {
      params: {
        parent_type: params.parent_type
      }
    })
  }
}

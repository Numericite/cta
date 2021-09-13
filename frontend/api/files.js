import BaseApi from './base'
import qs from 'qs'

export default class FilesApi extends BaseApi {
  getFront(params) {
    return this.api.get('file/front', {
      params: params
    })
  }
}

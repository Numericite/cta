import BaseApi from './base'
import qs from 'qs'

export default class LoginLogApi extends BaseApi {

  create(params) {
    return this.api.post('/loginLog', params)
  }

}

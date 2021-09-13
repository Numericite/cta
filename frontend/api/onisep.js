import BaseApi from './base'

export default class OnisepApi extends BaseApi {
  getOnisepSchool(params) {
    return this.api.get('onisep/schools', {
      params: params
    })
  }
}

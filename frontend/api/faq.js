import BaseApi from './base'

export default class FaqApi extends BaseApi {
  getFaq() {
    return this.api.get('faq/list?page=1&numberPerPage=1000')
  }
}

import BaseApi from './base'

export default class ParentsApi extends BaseApi {
  getStudents(params) {
    return this.api.get('parent/list/students', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        parent_id: params.parent_id
      }
    })
  }
}

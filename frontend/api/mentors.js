import BaseApi from './base'

export default class MentorsApi extends BaseApi {
  getStudents(params) {
    return this.api.get('mentor/list/students', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        mentor_id: params.mentor_id
      }
    })
  }
}

import BaseApi from './base'

export default class BlogApi extends BaseApi {
  getAllPosts() {
    return this.api.get('post/list', {
      params: {
        page: 1,
        numberPerPage: 1000,
        status_names: ['open']
      }
    })
  }

  getFirstPosts(limit, exclude) {
    if (exclude)
      return this.api.get(
        'post/list?numberPerPage=' + limit + '&page=1&status_names=open&exclude_ids=' + exclude
      )
    else return this.api.get('post/list?numberPerPage=' + limit + '&page=1&status_names=open').then(function(response) {
      return response
    })
  }

  getPost(id) {
    return this.api.get('post/list/ids?ids=' + id)
  }
}

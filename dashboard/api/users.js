import BaseApi from './base'

export default class UserApi extends BaseApi {
  login(user) {
    return this.api.post('auth/login/validated', { email: user.email.toLowerCase(), password: user.password })
  }

  getList(params) {
    return this.api.get('mercure/user', {
      params: params
    })
  }

  getParents(params) {
    return this.api.get('parent/list/all', {
      params: params
    })
  }

  getMentors(params) {
    return this.api.get('mentor/list/all', {
      params: params
    })
  }

  count(params) {
    return this.api.get('mercure/user/count', params)
  }

  update(params) {
    return this.api.post('mercure/user/update/admin/' + params.userID, params)
  }

  drop(params) {
    return this.api.delete('mercure/auth/close/account/' + params.email, { providerID: params.id })
  }

  reInit(params) {
    return this.api.post('user/reinit/' + params.id + '/' + params.accountType)
  }

  signUp(params) {
    return this.api.post('mercure/auth/signup', {
        roles: params.roles,
        lastName: params.lastName,
        firstName: params.firstName,
        email: params.email.toLowerCase(),
        active: true,
        password: params.password || '',
        sex: params.sex || 'undefined',
        avatar_path: '/img/profile.webp',
        config: {
            accountType: params.config.accountType,
            school_id: params.config.school_id,
            school_ids: params.config.school_ids,
            classroom_id: params.config.classroom_id,
            classroom_ids: params.config.classroom_ids,
            parent_id: params.config.parent_id
        }
      },
      {
        params: {
          generated: params.generated
        }
      }
    )
  }
}

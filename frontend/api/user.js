import BaseApi from './base'
import qs from 'qs'

export default class UserApi extends BaseApi {
  register( user ) {
    // eslint-disable-next-line
    if ( user.config.accountType === 'teacher' ) {
      return this.api.post( 'auth/signup/validated', {
        email: user.email.toLowerCase(),
        password: user.password,
        sex: user.sex,
        firstName: user.firstName,
        lastName: user.lastName,
        avatar_path: user.avatar_path,
        roles: user.roles,
        config: {
          city: user.config.city,
          department: user.department,
          school_id: user.school.id,
          classroom_id: user.config.classroom_id,
          classroom_ids: user.config.classroom_ids,
          phoneNumber: user.config.phoneNumber,
          accountType: user.config.accountType
        }
      } )
    }
    return this.api.post( 'auth/signup/validated', {
      email: user.email.toLowerCase(),
      password: user.password,
      sex: user.sex,
      firstName: user.firstName,
      lastName: user.lastName,
      avatar_path: user.avatar_path,
      roles: user.roles,
      config: {
        city: user.config.city,
        postal_code: user.config.postal_code,
        department: user.department,
        school_id: user.school.id,
        classroom_id: user.config.classroom_id,
        phoneNumber: user.config.phoneNumber,
        accountType: user.config.accountType
      }
    } )
  }

  login( user ) {
    return this.api.post( 'auth/login/validated', {
      email: user.email.toLowerCase(),
      password: user.password
    } )
  }

  updateUser( user ) {
    return this.api.post( 'mercure/user/update/normal/' + user.email, user )
  }

  validateAccount( token ) {
    return this.api.get( 'user/validation/' + token )
  }

  forgotPassword(email) {
    return this.api.get('mercure/auth/password', {
      params: {
        email: email
      }
    })
  }

  resetPassword(token, password) {
    return this.api.post( 'mercure/auth/password/reset', {
      token: token,
      password: password
    })
  }

  getUserInfos( userIds ) {
    return this.api.get( 'user/list-free/ids', {
      params: {
        ids: userIds
      },
      paramsSerializer: params => {
        return qs.stringify( params, {
          arrayFormat: 'repeat'
        } )
      }
    } )
  }

  getStudentNextActions(params) {
    return this.api.get('/student/next-actions', {
      params: params
    })
  }

  getStudentResults(params) {
    return this.api.get('/student/results', {
      params: params
    })
  }

  countStudents(params) {
    return this.api.get('student/count', {
      params: {
        classroom_id: params.classroom_id,
        school_ids: params.school_ids,
        grade: params.grade
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getStudents(params) {
    return this.api.get('student/list', {
      params: {
        classroom_id: params.classroom_id,
        school_ids: params.school_ids,
        grade: params.grade,
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  searchByFullName(params) {
    return this.api.get('student/search/' + params.full_name, {
      params: {
        classroom_id: params.classroom_id,
        school_ids: params.school_ids,
        grade: params.grade,
        fullName: params.full_name
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  uploadAvatar(id, file) {
    return this.api.post( '/mercure/user/logos/' + id, file)
  }
}

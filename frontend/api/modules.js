import BaseApi from './base'
import qs from 'qs'

export default class modulesApi extends BaseApi {

  count(params) {
    return this.api.get('module/count', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getList(params) {
    return this.api.get('module/list', {
      params: {
        page: params.page,
        numberPerPage: params.numberPerPage,
        grade: params.grade,
        school_type: params.school_type
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
  getModuleByIds(ids) {
    return this.api.get('/module/list/ids', {
      params: {
        ids: ids,
        page: 1,
        numberPerPage: 1000
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getModuleActivities(params) {
    return this.api.get('/moduleactivity/list', {
      params: params,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getModuleActivityByIds(ids) {
    return this.api.get('/moduleactivity/list/ids', {
      params: {
        ids: ids,
        page: 1,
        numberPerPage: 1000
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getProgress(user_id, module_id = undefined, grade, school_type) {
    return this.api.get('module/progress', {
      params: {
        user_id: user_id,
        module_id: module_id,
        grade: grade,
        school_type: school_type
      }
    })
  }

  getMaxNum(grade) {
    return this.api.get('module/maxnum', {
      params: {
        grade: grade
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

}

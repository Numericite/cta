import BaseApi from './base'
import qs from 'qs'

export default class ActivitiesApi extends BaseApi {
  getActivities() {
    return this.api.get( 'activity/list' )
  }

  getSelections(params) {
    return this.api.get('selection/list', {
      params: {
        activity_ids: params.activity_ids,
        version_ids: params.version_ids,
        page: 1,
        numberPerPage: 1000
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getSelectors(selectionIds) {
    return this.api.get('selector/list', {
      params: {
        selection_ids: selectionIds,
        page: 1,
        numberPerPage: 1000
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  // ------- CHOICES ------- //
  getChoicesByIds(choicesIds) {
    return this.api.get('choice/list/ids', {
      params: {
        ids: choicesIds,
        page: 1,
        numberPerPage: choicesIds.length
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getChoices(selectionIds) {
    return this.api.get('choice/list', {
      params: {
        selection_ids: selectionIds,
        page: 1,
        numberPerPage: 1000
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  // ------- RESULTS ------- //
  createResults(userId, choiceIds) {
    return this.api.post('results/' + userId, {choice_ids: choiceIds,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  // ------- TOKENS ------- //
  createToken(activityToken) {
    return this.api.post('activitytoken', activityToken)
  }

  getUserTokens(userId, activityId) {
    return this.api.get('activitytoken/user/' + userId, {
      params: {
        activity_id: activityId
      }
    })
  }

  checkToken(tokenId, activityId) {
    return this.api.get('activitytoken/check/' + tokenId, {
      params: {
        activity_id: activityId
      }
    })
  }

  validateToken(tokenId, choiceIds) {
    return this.api.post('activitytoken/validate/' + tokenId, {
      choice_ids: choiceIds,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  // ------- LOGS ------- //
  createLog(activityLog) {
    return this.api.post('activitylog', activityLog)
  }

  getUserLogs(userId, activityId) {
    return this.api.get('activitylog/user/' + userId, {
      params: {
        activity_id: activityId
      }
    })
  }

  getUserHistory(userId) {
    return this.api.get('activitylog/history/' + userId)
  }

  getUserUndoneActivities(userId, params) {
    return this.api.get('activitylog/undone/' + userId, {
      params: params
    })
  }

  getUserAdvancedLogs(userId) {
    return this.api.get('activitylog/advanced/' + userId)
  }

  // ------- VERSIONS ------- //
  getVersions(params) {
    return this.api.get('version/list', {
      params: {
        parent_ids: params.parent_ids,
        kind: 'activity',
        isDefault: params.isDefault,
        page: 1,
        numberPerPage: 1000
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getVersionsByIds(ids) {
    return this.api.get('version/list/ids', {
      params: {
        page: 1,
        numberPerPage: ids.length,
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

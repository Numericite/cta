import BaseApi from './base'
import qs from 'qs'

export default class ActivityApi extends BaseApi {
  count() {
    return this.api.get('activity/count')
  }

  getList(params) {
    return this.api.get('activity/list', {
      params: params
    })
  }

  getByIds(ids) {
    return this.api.get('activity/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  create(params) {
    return this.api.post('activity', params)
  }

  update(params) {
    return this.api.post('activity/' + params.id, params)
  }

  // -------------------------------- //
  // ------ VERSIONS SECTION ------ //
  // -------------------------------- //

  createVersion(version) {
    return this.api.post('version', version)
  }

  updateVersion(version) {
    return this.api.post('version/' + version.id, version)
  }

  uploadVersionLogo(id, file) {
    return this.api.post('version/logos/' + id, file)
  }

  getVersionByIds(ids) {
    return this.api.get('version/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getVersions(params) {
    return this.api.get('version/list', {
      params: {
        parent_ids: params.parent_ids,
        kind: 'activity',
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  countVersions(parentIds) {
    return this.api.get('version/count', {
      params: {
        parent_ids: parentIds,
        kind: 'activity'
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  dropVersions(versionIds) {
    return this.api.delete('version', {
      params: {
        ids: versionIds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  // -------------------------------- //
  // ------ SELECTIONS SECTION ------ //
  // -------------------------------- //

  createSelection(selection) {
    return this.api.post('selection', selection)
  }

  updateSelection(selection) {
    return this.api.post('selection/' + selection.id, selection)
  }

  uploadSelectionLogo(id, file) {
    return this.api.post('selection/logos/' + id, file)
  }

  getSelectionByIds(ids) {
    return this.api.get('selection/list/ids', {
      params: {
        ids: ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  getSelections(params) {
    return this.api.get('selection/list', {
      params: {
        activity_ids: params.activity_ids,
        version_ids: params.version_ids,
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  countSelections(params) {
    return this.api.get('selection/count', {
      params: {
        activity_ids: params.activity_ids,
        version_ids: params.version_ids
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  dropSelections(selectionIds) {
    return this.api.delete('selection', {
      params: {
        ids: selectionIds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  // -------------------------------- //
  // ------ CHOICES SECTION ------ //
  // -------------------------------- //

  createChoice(choice) {
    return this.api.post('choice', choice)
  }

  updateChoice(choice) {
    return this.api.post('choice/' + choice.id, choice)
  }

  uploadChoiceLogo(id, file) {
    return this.api.post('choice/logos/' + id, file)
  }

  getChoices(params) {
    return this.api.get('choice/list', {
      params: {
        selection_ids: params.selection_ids,
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  countChoices(selectionIds) {
    return this.api.get('choice/count', {
      params: {
        selection_ids: selectionIds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  dropChoices(choiceIds) {
    return this.api.delete('choice', {
      params: {
        ids: choiceIds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  // -------------------------------- //
  // ------ SELECTORS SECTION ------ //
  // -------------------------------- //

  createSelector(selector) {
    return this.api.post('selector', selector)
  }

  updateSelector(selector) {
    return this.api.post('selector/' + selector.id, selector)
  }

  uploadSelectorLogo(id, file) {
    return this.api.post('selector/logos/' + id, file)
  }

  getSelectors(params) {
    return this.api.get('selector/list', {
      params: {
        selection_ids: params.selection_ids,
        page: params.page,
        numberPerPage: params.numberPerPage
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  countSelectors(selectionIds) {
    return this.api.get('selector/count', {
      params: {
        selection_ids: selectionIds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }

  dropSelectors(selectorIds) {
    return this.api.delete('selector', {
      params: {
        ids: selectorIds
      },
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'})
      }
    })
  }
}

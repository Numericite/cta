import Cookies from 'js-cookie'

const state = () => {}

const mutations = {
  setUser(state, user) {
    state.user = user
  },
  setUserMatrix(state, userMatrix) {
    if (state.user) {
      state.user.matrix = userMatrix
    }
  },
  setSyncMatrix(state, sync) {
    if (state.user && state.user.matrix) {
      state.user.matrix.sync = sync
    }
  },
  setInitialSyncMatrix(state, initialSync) {
    if (state.user && state.user.matrix) {
      state.user.matrix.initialSync = initialSync
    }
  },
  setClassroom(state, classroom) {
    if (state.user) {
      state.user.classroom = classroom
    }
  },
  setSchool(state, school) {
    if (state.user) {
      state.user.school = school
    }
  },
  setCourse(state, course) {
    if (state.user) {
      state.user.course = course
    }
  },
  setUserAvatar(state, avatar_path) {
    if (state.user) {
      state.user.avatar_path = avatar_path
    }
  },
  setPartner(state, partner) {
    if (state.user) {
      state.user.partner = partner
    }
  },
  setUserDomains(state, domain_ids) {
    if (state.user) {
      state.user.config.domain_ids = domain_ids
    }
  },
  setUserStreams(state, stream_ids) {
    if (state.user) {
      state.user.config.stream_ids = stream_ids
    }
  },
  setUserSchools(state, school_uais) {
    if (state.user) {
      state.user.config.school_uais = school_uais
    }
  },
  setUserCriteria(state, criterion_ids) {
    if (state.user) {
      state.user.config.criterion_ids = criterion_ids
    }
  },
  setUserDetails(state, detail_ids) {
    if (state.user) {
      state.user.config.detail_ids = detail_ids
    }
  },
  setUserIntrospectionStreams(state, introspection_stream_ids) {
    if (state.user) {
      state.user.config.introspection_stream_ids = introspection_stream_ids
    }
  },
  setIntrospectionSchoolUais(state, school_uais) {
    if (state.user) {
      state.user.config.introspection_school_uais = school_uais
    }
  },
  setUserPartner(state, partner) {
    if (state.user) {
      state.user.config.partner= partner
    }
  },
  setCurrentStep(state, currentStep) {
    if (state.user) {
      state.user.config.currentStep = currentStep
    }
  },
  setSawFinalPopup(state, saw) {
    if (state.user) {
      state.user.config.sawFinalPopup = saw
    }
  },
  setHasChooseRank3(state, has) {
    if (state.user) {
      state.user.config.hasChooseRank3 = has
    }
  },
  setHasChooseFaculty(state, has) {
    if (state.user) {
      state.user.config.hasChooseFaculty = has
    }
  },
  setToken(state, token) {
    state.token = token
    Cookies.remove('token')
    Cookies.set('token', token, { expires: 7 })
  },
  destroy(state) {
    state.user = null
    state.token = null
    Cookies.remove('token')
  }
}

const getters = {
  username: state => {
    return state.user && state.user.username
  },
  getMatrix: (state) => {
    return state.user.matrix
  },
  getSyncMatrix: (state) => {
    return state.user.matrix.sync
  },
}

const auth = {
  namespaced: true,
  state,
  mutations,
  getters
}
export default auth

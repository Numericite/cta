import Cookies from 'js-cookie'

const state = () => {}

const mutations = {
  setUser(state, user) {
    state.user = user
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
  }
}

const auth = {
  namespaced: true,
  state,
  mutations,
  getters
}
export default auth

import axios from 'axios'
import Vuex from 'vuex'
import { API_ROOT } from '~/config'
import cookieparser from 'cookieparser'
import auth from '~/store/auth.js'
import dataTable from '~/store/data-table.js'
import { ROLES } from '~/config'

const store = () => {
  return new Vuex.Store({
    modules: {
      auth: auth,
      dataTable: dataTable
    },
    state: () => ({
      partners: null
    }),
    mutations: {
      setPartners: function(state, data) {
        state.partners = data
      }
    },
    actions: {
      async nuxtServerInit({ commit, state }, { req }) {

        //DATA TABLES
        commit('dataTable/initParams')

        //Auth
        let token = null
        if (req && req.headers && req.headers.cookie) {
          const parsed = cookieparser.parse(req.headers.cookie)
          token = parsed.token || null
        }

        //RETRIEVE USER
        if (token) {
          try {
            let response = await axios.get(API_ROOT + 'mercure', { headers: {'X-Auth-Token': token}})
            const user = response.data

            if (user.roles[0] !== ROLES.admin.roles[0]) {
              commit('auth/destroy')
            } else {
              commit('auth/setToken', token)
              commit('auth/setUser', user)
            }
          } catch (e) {
            if (e.response.status === 401) {
              commit('auth/destroy')
            }
          }
        }
      }
    }
  })
}

export default store

import axios from 'axios'
import Vuex from 'vuex'
import { API_ROOT, API_MATRIX } from '~/config'
import cookieparser from 'cookieparser'
import auth from '~/store/auth.js'
import _ from 'lodash'

const store = () => {
  return new Vuex.Store({
    modules: {
      auth: auth
    },
    state: () => ({
      partners: null
    }),
    mutations: {
      setPartners: function(state, data) {
        state.partners = data
      },
      setPages: function(state, data) {
        state.pages = data
      }
    },
    actions: {
      async nuxtServerInit({ commit, state }, { req }) {
        // Partners
        try {
          let response = await axios.get(API_ROOT + 'partner/list?page=1&numberPerPage=1000')
          const partners = response.data
          commit('setPartners', partners)

          response = await axios.get(API_ROOT + 'file/front')
          const pages = response.data
          commit('setPages', pages)
        } catch (e) {
          console.log(e)
        }
        //Auth
        let token = null
        if (req && req.headers && req.headers.cookie) {
          const parsed = cookieparser.parse(req.headers.cookie)
          token = parsed.token || null
        }

        //RETRIEVE USER
        if (token) {
          try {
            let response = await axios.get(API_ROOT + 'current', { headers: {'X-Auth-Token': token}})
            const user = response.data

            // CLASSROOM
            response = await axios.get(API_ROOT + 'schoolclassroom/list/ids?ids=' + _.get(user.config, 'classroom_id', ''), { headers: {'X-Auth-Token': token}})
            const classroom = response.data[0] || {}

            // SCHOOL
            if (user.config && user.config.school_id) {
              response = await axios.get(API_ROOT + 'school/list/ids?ids=' + user.config.school_id, { headers: { 'X-Auth-Token': token } })
            } else {
              response = await axios.get(API_ROOT + 'school/list/ids?ids=' + _.get(classroom, 'school_id', ''), { headers: { 'X-Auth-Token': token } })
            }
            const school = response.data[0] || {}

            // COURSE
            const course_id = classroom.course_id ? classroom.course_id : school.course_id ? school.course_id : ''
            if (course_id !== '') {
              response = await axios.get(API_ROOT + 'course/list/ids?ids=' + course_id, { headers: { 'X-Auth-Token': token } })
            } else {
              response = await axios.get(API_ROOT + 'course/list?page=1&numberPerPage=1000', { headers: { 'X-Auth-Token': token } })
            }

            const course = response.data[0]

            response = await axios.get(API_ROOT + 'file/front?version_name=' + course.text_version_name)
            const pages = response.data
            commit('setPages', pages)

            commit('auth/setToken', token)
            commit('auth/setUser', user)
            commit('auth/setSchool', school)
            commit('auth/setClassroom', classroom)
            commit('auth/setCourse', course)

            // PARTNER
            if (user.config && user.config.partner_id) {
              response = await axios.get(API_ROOT + 'partner/list/ids?ids=' + user.config.partner_id)
              commit('auth/setPartner', (response.data[0] || {}))
            } else if (user.config && user.config.accountType === 'student') {
              let studentPartners = (_.find(state.partners, (partner) => partner?.school_ids?.includes(user.config.school_id)) || {})
              if (!_.isEmpty(studentPartners)) commit('auth/setPartner', studentPartners)
            }

            // MATRIX AUTH FOR TEACHER

            if (user.config && (user.config.accountType === 'teacher' || user.config.accountType === 'operator')) {
              let response = (await this.$api.matrix.usernameAvailable({ 
                username: user.userID 
              })) || {}
    
              if (response?.available) {
    
                var user_matrix = (await this.$api.matrix.register({ 
                  auth: { type: 'm.login.dummy' },
                  username: user.userID,
                  password: user.userID
                })) || {}
    
                await this.$api.matrix.setDisplayName({ 
                  access_token: user_matrix.access_token, 
                  user_id: user_matrix.user_id, 
                  display_name: `${user.firstName} ${user.lastName}`
                })
    
                await this.$api.matrix.setAvatarUrl({ 
                  access_token: user_matrix.access_token, 
                  user_id: user_matrix.user_id, 
                  avatar_url: user.avatar_path
                })
    
              } else {
    
                var user_matrix = (await this.$api.matrix.login({ 
                  type: 'm.login.password', 
                  user: user.userID, 
                  password: user.userID
                })) || {}
    
                let profileMatrix = (await this.$api.matrix.getProfileMatrix({ 
                  user_id: user_matrix.user_id
                })) || {}
    
                if (profileMatrix.displayname != `${user.firstName} ${user.lastName}`) {
    
                  await this.$api.matrix.setDisplayName({ 
                    access_token: user_matrix.access_token, 
                    user_id: user_matrix.user_id, 
                    display_name: `${user.firstName} ${user.lastName}`
                  })
    
                }
    
                if (profileMatrix.avatar_url != user.avatar_path) {
                  await this.$api.matrix.setAvatarUrl({ 
                    access_token: user_matrix.access_token, 
                    user_id: user_matrix.user_id, 
                    avatar_url: user.avatar_path
                  })
                }
    
              }
    
              commit('auth/setUserMatrix', user_matrix)
              commit('auth/setInitialSyncMatrix', {})
              commit('auth/setSyncMatrix', {})
            }

          } catch (e) {
            console.log(e)
            if (e.response.status === 401) {
              commit('auth/destroy')
            }
          }
        }
      },

      async matrixInitialSync({ commit }, user_matrix) {

        let state_matrix = (await this.$api.matrix.retrieveState({ 
          access_token: user_matrix.access_token,
          timeout: 0 
        })) || {}

        user_matrix.next_batch = state_matrix.next_batch 

        commit('auth/setInitialSyncMatrix', state_matrix)

        return user_matrix
      },

      async matrixSync({ commit, dispatch }, user_matrix) {

        let state_matrix = (await this.$api.matrix.retrieveState({ 
          access_token: user_matrix.access_token,
          next_batch: user_matrix.next_batch,
          timeout: 30000
        })) || {}

        user_matrix.next_batch = state_matrix.next_batch

        commit('auth/setSyncMatrix', state_matrix)

        dispatch('matrixSync', user_matrix)
      },

    }
  })
}

export default store

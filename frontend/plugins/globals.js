import Vue from 'vue'
import _ from 'lodash'
import moment from 'moment'

Vue.mixin({
  methods: {
    getPageTexts(slug) {
      return _.map(
        _.get(_.find(this.$store.state.pages, {slug: slug}), 'texts', []),
        (text) =>
          {
            return text.replace(/\n/g, "<br>")
              .replace(/<a /g, '$& target="_blank" ')
              .replace(/<\/p><p>/, '<br>')
              .replace(/<p[^>]*>/g, '')
              .replace(/<\/p>/g, '<br>')
              .replace(/<br>([^<br>]*)$/, '$1')
          }
      )
    },
    arrayRemoveUndefined(array) {
      return _.filter(array, (item) => !!item)
    },
    unwind(array, field, unwindField){
      const unwindArray = []
      _.forEach(array, (o) => {
        _.forEach(o[field], (val) => {
          const cloned = _.clone(o)
          cloned[unwindField] = val
          unwindArray.push(cloned)
        })
      })
      return unwindArray
    },
    async asyncForEach(array, callback) {
      for (let index = 0; index < array.length; index++) {
        await callback(array[index], index, array);
      }
    },
    formatDate: function(date) {
      return moment(date).locale('fr').format('D MMMM YYYY')
    },
    getSchemaFromFields(fields) {
      return fields.map((field) => {
        const tmpField = Object.assign({}, field)
        tmpField.type = tmpField.type ? tmpField.type : tmpField.kind
        tmpField.name = tmpField.id

        if (tmpField.validation) {
          tmpField.validation = 'required'
          tmpField['validation-messages'] = {
            'required': 'Tu dois renseigner ce champ'
          }
        }

        // Add css for Formulate Form
        tmpField.outerClass = ['my-8']
        tmpField.inputClass = ['mt-8 py-5 px-6 text-ms-gray-dark']

        const log = _.find(this.fieldLogs, {field_id: tmpField.id})
        if (log) {
          tmpField.value = _.get(log, tmpField.type === 'checkbox' ? 'values' : 'values[0]', undefined)
        }

        if (tmpField.component === 'h3') {
          tmpField.class = 'ms-title-medium py-4 leading-tight text-5.2xl'
        }

        if (tmpField.optionsField && tmpField.optionsField.length) {
          tmpField.options = tmpField.optionsField.map((option) => {
            option.id = Math.random().toString()
            return option
          })
        }

        return tmpField
      })
    },
    stepCalculator(step) {
      const chapters = this.$store.state.auth.user.course.chapters
      let response = step
      chapters.forEach(function(chapter) {
        if (!chapter.display) {
          if (chapter.slug === 'activity-area') {
            response += 4
          } else if (chapter.slug === 'stream') {
            response += 4
          }
        } else {
          chapter.children.forEach(function (child) {
            if (!child.display) {
              if (chapter.slug === 'activity-area') {
                if (child.slug === 'introspection') {
                  response += 1
                } else if (child.slug === 'exploration') {
                  response += 2
                }
              } else if (chapter.slug === 'stream') {
                if (child.slug === 'introspection') {
                  response += 1
                } else if (child.slug === 'exploration') {
                  response += 2
                }
              }
            }
          })
        }
      })

      return response
    },
    formatDateMessage(date) {
      return moment().locale('fr').subtract(date).format('DD MMMM')
    },
    async authUserToMatrix(user) {

      let changeDisplayName = async (userMatrix, user) => {
        await this.$api.matrix.setDisplayName({ 
          access_token: userMatrix.access_token, 
          user_id: userMatrix.user_id, 
          display_name: `${user.firstName} ${user.lastName}`
        })
      }
  
      let changeAvatarUrl = async (userMatrix, user) => {
        await this.$api.matrix.setAvatarUrl({ 
          access_token: userMatrix.access_token, 
          user_id: userMatrix.user_id, 
          avatar_url: user.avatar_path
        })
      }

      let response = (await this.$api.matrix.usernameAvailable({ 
        username: user.userID 
      })) || {}

      if (response?.available) {

        var userMatrix = (await this.$api.matrix.register({ 
          auth: { type: 'm.login.dummy' },
          username: user.userID,
          password: user.userID
        })) || {}

        await changeDisplayName(userMatrix, user)
        await changeAvatarUrl(userMatrix, user)

      } else {

        var userMatrix = (await this.$api.matrix.login({ 
          type: 'm.login.password', 
          user: user.userID, 
          password: user.userID
        })) || {}

        let profileMatrix = (await this.$api.matrix.getProfileMatrix({ 
          user_id: userMatrix.user_id
        })) || {}

        if (profileMatrix.displayname != `${user.firstName} ${user.lastName}`) await changeDisplayName(userMatrix, user)
        if (profileMatrix.avatar_url != user.avatar_path) await changeAvatarUrl(userMatrix, user)
      }

      return userMatrix

    },
    async newConversationMatrix(userMatrix, member) {

      let response = await this.$api.matrix.createRoom({ 
        access_token: userMatrix.access_token
      })
      let room_id = response.room_id

      let memberRoomMatrix = await this.authUserToMatrix(member)
      
      response = await this.$api.matrix.inviteUserToRoom({ 
        access_token: userMatrix.access_token,
        member_user_id: memberRoomMatrix.user_id,
        room_id
      });

      response = await this.$api.matrix.joinRoom({
        access_token: memberRoomMatrix.access_token,
        user_id: memberRoomMatrix.user_id,
        room_id
      })

    },
  }
})

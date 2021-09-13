<template>
  <div :class="{'bg-white p-3 rounded-xl' : !typeComponentHisDropdown}">
    <div :style="typeComponentHisDropdown ? 'width: 300px' : 'height: 750px'" 
         :class="{'border-solid border rounded-xl border-blue': !typeComponentHisDropdown}" class="flex">
      <Rooms :ref="'room_' + typeComponent" :rooms="!typeComponentHisDropdown ? rooms : roomsDropdown" :roomActive="!typeComponentHisDropdown ? roomActive : {}" 
             :presenceUsers="presenceUsers" :typeComponent="typeComponent" :class="typeComponentHisDropdown ? 'w-full rounded-b-xl' : 'w-1/3 rounded-l-xl'" 
             @emitHideDropdownMessage="emitHideDropdownMessage" @changeRoom="changeRoom" />
      <Conversation v-if="roomActive && typeComponent != 'dropdown'" :ref="'conversation_' + typeComponent" 
                    :key="componentConversationKey" :roomActive="roomActive" class="w-2/3" />
    </div>
  </div>
</template>

<script>

  import Rooms from './Rooms.vue'
  import Conversation from './Conversation.vue'
  import { mapGetters } from 'vuex'
  import _ from 'lodash'

  export default {
    name: 'Chat',
    components: { Rooms, Conversation },
    props: {
      typeComponent: {
        type: String,
        default: function() {
          return ''
        }
      },
    },
    data() {
      return {
        rooms: [],
        roomActive: null,
        presenceUsers: [],
        componentConversationKey: 0
      }
    },
    computed: {
      ...mapGetters({
        userMatrix: 'auth/getMatrix'
      }),
      typeComponentHisDropdown() {
        return this.typeComponent === 'dropdown'
      },
      roomsDropdown() {
        return this.rooms.slice(0, 3)
      }
    },
    watch: {
      async 'userMatrix.sync'() {
        await this.logEventStream()
      },
      '$route.query.roomId'() {
        if (this.$route.query.roomId && this.typeComponent != 'dropdown') {
          let newRoom = this.rooms.find(room => room.id == this.$route.query.roomId)
          this.changeRoom(newRoom)
          this.$router.replace({ query: null })
        }
      },
      '$route.query.newMessage'() {
        if (this.$route.query.newMessage && this.typeComponent != 'dropdown') {
          this.$refs['room_base'].menuNewDiscussion = true
          this.$router.replace({ query: null })
        }
      }
    },
    async mounted() {

      if (this.typeComponent == 'base') {
        await this.$store.dispatch('matrixInitialSync', this.$store.state.auth.user.matrix)
      }

      let state_matrix = this.userMatrix.initialSync

      this.presenceUsers = state_matrix.presence.events

      this.rooms = Object.entries({ ...state_matrix.rooms.join }).map( ([key, value]) => {
        value.id = key
        return value
      })

      this.rooms.sort((a, b) => a.timeline.events[a.timeline.events.length - 1].origin_server_ts - b.timeline.events[b.timeline.events.length - 1].origin_server_ts).reverse()
      
      if (this.$route.query.roomId) {
        this.roomActive = this.rooms.find(item => item.id == this.$route.query.roomId)
        this.roomActive.memberHisTyping = false
        this.$router.replace({ query: null })
      } else if (this.rooms.length) {
        this.roomActive = this.rooms[0]
        this.roomActive.memberHisTyping = false
      } else {
        this.roomActive = {}
      }

    },
    methods: {

      async logEventStream() {

        let state_matrix = this.userMatrix.sync

        let rooms = Object.entries({ ...state_matrix.rooms.join }).map( ([key, value]) => {
          value.id = key
          return value
        })

        // Presence events
        let presenceEvents = state_matrix.presence.events

        if (presenceEvents.length) {
          presenceEvents.forEach(presence => {
            if (this.presenceUsers.some(item => item.sender == presence.sender)) {
              this.presenceUsers.map(item => { if(item.sender == presence.sender) { item = presence } })
            } else {
              this.presenceUsers.push(presence)
            }
          })
          this.roomActive = Object.assign({}, this.roomActive, {})
        }

        // Leave rooms events (problem with multiple delete at the same time)
        if (Object.keys({ ...state_matrix.rooms.leave }).length) {
          let leave_room_ids = Object.keys({ ...state_matrix.rooms.leave })
          leave_room_ids.forEach(leave_room_id => {
            this.rooms = this.rooms.filter(room => room.id != leave_room_id)
            if (!this.rooms.length) {
              this.roomActive = {}
              this.componentConversationKey += 1
            } else if (this.roomActive.id == leave_room_id) {
              this.changeRoom(this.rooms[0])
            } 
          })
        }

        if (rooms.length) {

          let newRoom = rooms.filter(room => !this.rooms.map(item => item.id).includes(room.id))
          if (newRoom.length) this.rooms.unshift(newRoom[0])

          rooms.map(room => {

            let timelineEvents = room.timeline.events
            let ephemeralEvents = room.ephemeral.events
            let accountDataEvents = room.account_data.events

            // New message
            if (timelineEvents.length && timelineEvents[0].type == 'm.room.message') {
              if (this.roomActive.id == room.id && this.typeComponent !== 'dropdown') this.$refs['conversation_base'].roomMessages.push(timelineEvents[0]) // To push new message for component Conversation
              this.rooms.find(item => item.id == room.id).timeline.events.push(timelineEvents[0]) // For rooms last message
        
              this.rooms.sort((a, b) => a.timeline.events[a.timeline.events.length - 1].origin_server_ts - b.timeline.events[b.timeline.events.length - 1].origin_server_ts).reverse()
            }

            // New events within room creation
            if (timelineEvents.length && timelineEvents[0].type != 'm.room.message') {
              this.rooms.find(item => item.id == room.id).timeline.events.push(timelineEvents[0])
            }

            // Typing events
            if (ephemeralEvents.length && ephemeralEvents[0].type == 'm.typing') {

              if (!ephemeralEvents[0].content.user_ids.length 
                 || ephemeralEvents[0].content.user_ids[0] == this.userMatrix.user_id) {
                if (this.roomActive.id == room.id) this.roomActive = Object.assign({}, this.roomActive, { memberHisTyping: false })
                this.rooms.find(item => item.id == room.id).memberHisTyping = false
              }

              if (ephemeralEvents[0].content.user_ids.length == 1 
                 && ephemeralEvents[0].content.user_ids[0] != this.userMatrix.user_id
                 || ephemeralEvents[0].content.user_ids.length > 1) {
                   if (this.roomActive.id == room.id) this.roomActive = Object.assign({}, this.roomActive, { memberHisTyping: true })
                   this.rooms.find(item => item.id == room.id).memberHisTyping = true
              }
            }

            // Notification events
            if (room.unread_notifications.notification_count != this.rooms.find(item => item.id == room.id).unread_notifications.notification_count) {
              this.rooms.find(item => item.id == room.id).unread_notifications.notification_count = room.unread_notifications.notification_count
            }

            // Read markers events
            if (ephemeralEvents.length && ephemeralEvents[0].type == 'm.receipt') {

              let eventIds = Object.entries({ ...ephemeralEvents[0].content }).map( ([key, value]) => {
                value.id = key
                value.user_ids = Object.keys({ ...value['m.read'] }).filter(item => item != this.userMatrix.user_id) // Change this line to get event date
                return value
              })

              const newInterlocutorReadState = _.get(eventIds.filter(item => {
                return item.user_ids.length
              }).map(item => item.id), '0', null)

              if (newInterlocutorReadState) {
                if (this.roomActive.id == room.id && this.typeComponent != 'dropdown') this.$refs['conversation_base'].interlocutorReadState = newInterlocutorReadState
                this.rooms.find(item => item.id == room.id).interlocutorReadState = newInterlocutorReadState
              }

            }

          })
        }

      },

      changeRoom(newRoom) {
        if (this.roomActive.id != newRoom.id) {
          this.roomActive = newRoom
          this.roomActive.memberHisTyping = newRoom.memberHisTyping ? newRoom.memberHisTyping : false
          this.componentConversationKey += 1
        }
      },

      emitHideDropdownMessage() {
        this.$emit('hideDropdownMessage')
      }

    }
  }

</script>
<template>
  <div :class="menuNewDiscussion 
         ? 'bg-white border-solid border-r-1 border-blue' : typeComponent == 'dropdown'
       ? 'bg-blue' : 'bg-ms-purple-light-2'" 
       class="relative">

    <div v-if="menuNewDiscussion" class="flex flex-col h-full">
      <div class="flex flex-col bg-ms-purple-light-2 rounded-tl-xl pt-7 px-7 pb-5">
        <div class="flex items-center justify-between mb-5">
          <div class="flex justify-start items-center ml-2">
            <div class="dot bg-white"/>
            <div class="ms-subtitle mx-4 text-white">Nouveau message</div>
            <div class="dot bg-white"/>
          </div>
          <img class="h-4 w-4 ml-3 cursor-pointer" src="~/assets/img/middle-school/close.svg" 
               @click="changeMenuDiscussion(false)">
        </div>
        <FormulateInput v-model="fullName" :outerClass="['w-full']" 
                        :inputClass="['px-5 py-3 focus:border-purple text-ms-gray-dark']"
                        type="text" placeholder="Prénom et nom" autocomplete="off"
                        @change="fullName ? searchByFullName() : getTeachers()" />
      </div>
      
      <div class="px-7 py-2 overflow-auto">
        <div v-for="(teacher, index) in teachers" :key="index" class="flex items-center mt-4">
          <div class="flex flex-grow items-center rounded-lg shadow-card-teacher">
            <div class="flex flex-grow items-center rounded-lg p-3 cursor-pointer hover:active-room"
                 @click="startNewDiscussionLoading ? '' : startNewConversation(teacher)">
              <img :src="teacher.avatar_path" class="w-12 h-12 rounded-lg mr-3" >
              <span class="font-bold">{{ teacher.firstName + ' ' + teacher.lastName }}</span>
            </div>
          </div>
        </div>
      </div>
      
    </div>

    <div v-else class="pt-7 h-full">
      <div :class="{ 'overflow-auto pb-24 h-full' : !typeComponentHisDropdown && rooms.length }" class="pl-7">
        <div class="flex justify-start items-center mb-5 ml-2">
          <div class="dot bg-white"/>
          <div class="ms-subtitle mx-4 text-white">{{ !typeComponentHisDropdown ? 'Nouveaux' : '' }} Messages</div>
          <div class="dot bg-white"/>
        </div>
        <div v-if="!rooms.length" class="text-center mr-6 pt-6 pb-4">
          <span class="text-white font-medium">Aucune conversation en cours</span>
        </div>
        <div v-for="room in rooms" :key="room.id" class="flex items-center mb-5">
          <div :class="roomActive.id === room.id ? 'active-room' : 'bg-white'" 
               class="flex flex-grow rounded-lg p-3 cursor-pointer hover:active-room" 
               @click="changeRoom(room) ">
            <div class="relative">
              <img :src="room.avatar_url" class="w-12 h-12 rounded-lg mr-3" >
              <div :class="room.presence ? 'bg-ms-green' : 'bg-red'"
                   class="w-4 h-4 border-2 border-solid border-white rounded-full absolute" style="right: 4px; top: 0;" />
            </div>
            <div :class="room.lastMessage ? 'py-1 justify-end' : 'justify-center'" class="flex flex-grow flex-col">
              <div :class="{'pb-3' : room.lastMessage}" class="flex justify-between">
                <span class="font-bold">{{ room.name }}</span>
                <span v-if="room.lastMessage" class="self-start">{{ formatDateMessage(room.lastMessage.unsigned.age) }}</span>
              </div>
              <span v-if="room.lastMessage" :class="{'font-semibold' : room.lastMessage.sender != userMatrix.user_id && room.unread_notifications.notification_count}" 
                    class="text-ms-gray-dark">
                {{ room.lastMessage.sender == userMatrix.user_id ? 'Vous: ' : '' }} 
                {{ room.lastMessage.content.body | str_limit(20) }}
              </span>
            </div>
          </div>
          <div class="flex w-6 items-center justify-center">
            <div v-show="roomActive.id === room.id" class="dot w-2.5 h-2.5 active-room"/>
          </div>
        </div>
      </div>


      <div v-if="accountType == 'operator'" :class="typeComponentHisDropdown || !rooms.length ? 'py-3' : 'absolute bg-ms-purple-light-1 shadow-new-message h-24'" 
           class="flex justify-center items-center w-full rounded-bl-xl" style="bottom: 0;">
        <div :class="typeComponentHisDropdown ? 'border border-white border-solid' : 'bg-white'" 
             class="flex items-stretch rounded-lg px-6 py-5 cursor-pointer" @click="changeMenuDiscussion(true)">
          <span :class="typeComponentHisDropdown ? 'text-white' : 'text-blue'" class="font-medium">
            Nouveau message
          </span>
          <div v-show="rooms.length" :class="typeComponentHisDropdown ? 'text-white' : 'text-blue'" class="flex items-center h-full mt-1 ml-2">
            <svg width="11" height="11" viewBox="0 0 11 11" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
              <path clip-rule="evenodd" d="M1.47637 1.3097C1.79206 0.994016 2.22022 0.816666 2.66667 0.816666H5.08334C5.35948 0.816666 5.58334 1.04052 5.58334 1.31667V2.01667C5.58334 2.29281 5.35948 2.51667 5.08334 2.51667H3.18334C2.9072 2.51667 2.68334 2.74052 2.68334 3.01667V7.81667C2.68334 8.09281 2.90719 8.31667 3.18334 8.31667H7.98334C8.25948 8.31667 8.48334 8.09281 8.48334 7.81667V5.91667C8.48334 5.64052 8.70719 5.41667 8.98334 5.41667H9.68334C9.95948 5.41667 10.1833 5.64052 10.1833 5.91667V8.33333C10.1833 8.77978 10.006 9.20794 9.6903 9.52363C9.37461 9.83931 8.94645 10.0167 8.5 10.0167H2.66667C2.22022 10.0167 1.79206 9.83931 1.47637 9.52363C1.16069 9.20794 0.983337 8.77978 0.983337 8.33333V2.5C0.983337 2.05355 1.16069 1.62539 1.47637 1.3097Z" fill="currentColor"/>
              <path d="M6.12129 6.36396L4.50005 6.5L4.70708 4.94975L9.4447 0.21213C9.56185 0.0949723 9.7518 0.0949723 9.86896 0.21213L10.8589 1.20208C10.9761 1.31924 10.9761 1.50919 10.8589 1.62634L6.12129 6.36396Z" fill="currentColor"/>
            </svg>
          </div>
        </div>
      </div>

      <div v-if="typeComponentHisDropdown" :class="accountType == 'operator' ? 'mt-3' : 'mt-6'" class="mb-8">
        <nuxt-link :to="'/dashboard/college/' + accountType + '/messages'" class="flex flex-col items-center justify-center" @click.native="emitHideDropdownMessage">
          <a class=" font-plex underline text-white hover:text-white cursor-pointer">Accéder à la messagerie</a>
        </nuxt-link>
      </div>
    </div>

  </div>
</template>

<script>

  import moment from 'moment'

  export default {
    name: 'Rooms',
    props: {
      rooms: {
        type: Array,
        default: function() {
          return []
        }
      },
      presenceUsers: {
        type: Array,
        default: function() {
          return []
        }
      },
      roomActive: {
        type: Object,
        default: function() {
          return {}
        }
      },
      typeComponent: {
        type: String,
        default: function() {
          return ''
        }
      }
    },
    data () {
      return {
        menuNewDiscussion: false,
        teachers: [],
        fullName: '',
        userMatrix: this.$store.state.auth.user.matrix,
        accountType: this.$store.state.auth.user.config.accountType !== 'teacher' ? this.$store.state.auth.user.config.accountType : 'manager',
        startNewDiscussionLoading: false
      }
    },
    computed: {
      typeComponentHisDropdown() {
        return this.typeComponent === 'dropdown'
      },
    },
    watch: {
      rooms: {
        handler() {
          this.updateRooms()
          this.$forceUpdate()
        },
        deep: true
      },
      presenceUsers() {
        this.updateRooms()
        this.$forceUpdate()
      }
    },
    async mounted() {
      if (this.$route.query.newMessage) {
        this.menuNewDiscussion = true
        this.$router.replace({ query: null })
      }
      if (this.rooms.length) this.updateRooms()
      if (this.accountType == 'operator') await this.getTeachers()
      this.$forceUpdate()
    },
    methods: {

      updateRooms() {
        this.rooms.forEach(room => {
          var events = room.timeline.events.filter(event => event.content.membership == 'join' && event.type == 'm.room.member' && event.state_key != this.userMatrix.user_id)
          if (!events.length) events = room.state.events.filter(event => event.content.membership == 'join' && event.type == 'm.room.member' && event.state_key != this.userMatrix.user_id)
          let memberRoom = events.slice(-1)[0]
          if (memberRoom) {
            room.user_id = memberRoom.state_key
            room.name = memberRoom.content.displayname
            room.avatar_url = memberRoom.content.avatar_url
            room.presence = this.presenceUsers.some(item => item.sender == memberRoom.state_key && item.content.presence == 'online')
            room.lastMessage = room.timeline.events.filter(event => event.type == 'm.room.message').slice(-1)[0]
          }
        })
      },

      changeRoom(room) {
        if (this.typeComponent == 'base') {
          this.$emit('changeRoom', room)
        } else if (this.typeComponent == 'dropdown') {
          this.emitHideDropdownMessage()
          this.$router.push({ path: '/dashboard/college/' + this.accountType + '/messages', query: { roomId: room.id } })
        } 
      },

      async changeMenuDiscussion(state) {
        if (this.typeComponent == 'base') {
          if (state) {
            this.fullName = ''
            await this.getTeachers()
          }
          this.menuNewDiscussion = state
        } else if (this.typeComponent == 'dropdown') {
          this.emitHideDropdownMessage()
          this.$router.push({ path: '/dashboard/college/' + this.accountType + '/messages', query: { newMessage: true } })
        }
      },

      async getTeachers() {
        let response = await this.$api.teachers.getList({ page: 1, numberPerPage: 100 })
        this.teachers = response.data.res || []
        this.filterTeachers()
      },

      async searchByFullName() {
        let response = await this.$api.teachers.searchByFullName({ fullName: this.fullName })
        this.teachers = response.data.result || []
        this.filterTeachers()
      },

      filterTeachers() {
        this.teachers = this.teachers.filter(teacher => this.$store.state.auth.user.config.school_ids.includes(teacher.config.school_id))
        let user_ids = this.rooms.map(room => room.user_id.split(':')[0].replace('@', ''))
        user_ids.push(this.$store.state.auth.user.userID)
        this.teachers = this.teachers.filter(item => !user_ids.includes(item.userID))
        this.teachers.forEach(item => item.cardActive = false)
      },

      changeStateCardNewMessage(index) {
        this.teachers[index].cardActive = !this.teachers[index].cardActive
        this.$forceUpdate()
      },

      async startNewConversation(teacher) {
        this.startNewDiscussionLoading = true
        await this.newConversationMatrix(this.userMatrix, teacher)
        this.menuNewDiscussion = false
        this.$emit('changeRoom', this.rooms[0])
        this.startNewDiscussionLoading = false
      },

      emitHideDropdownMessage() {
        this.$emit('emitHideDropdownMessage')
      }

    }
  }

</script>


<style lang="scss">

.active-room {
  background-color: #E8E6F1;
}

.shadow-new-message {
  box-shadow: 0px -5px 15px 0px #00000033;
}

.shadow-card-teacher {
  box-shadow: 0px 7.5px 15px rgba(124, 124, 128, 0.1);
  transition: box-shadow 0.3s ease-out;

  &:hover {
    box-shadow: 0px 20px 40px rgba(124, 124, 128, 0.2);
  }

}

</style>
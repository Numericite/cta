<template>
  <div v-if="!loading" class="flex relative flex-col">
    <div class="flex items-center h-24 w-full px-6 py-4 bg-ms-purple-light-5 rounded-tr-xl">
      <template v-if="roomMember">
        <img :src="roomMember.content.avatar_url" class="h-16 w-16 p-2 bg-white rounded-lg">
        <span class="font-semibold text-2xl ml-5">{{ roomMember.content.displayname }}</span>
        <span v-if="roomMember.status.presence" class="ml-3">
          {{ roomMember.status.presence === 'online' ? 'en ligne' : 'hors ligne' }}
        </span>
        <nuxt-link v-if="accountType == 'operator'" :to="`/dashboard/college/${accountType}/teachers/${getUserIdFromUserMatrix}`" 
                   tag="div" class="flex items-center bg-blue rounded-lg px-4 py-3 ml-auto cursor-pointer">
          <img src="~/assets/img/middle-school/planning_white.svg" class="ml-1 w-8 h-8 block">
        </nuxt-link>
      </template>
      <template v-else>
        <div class="h-16 w-16 p-2 bg-ms-gray rounded-lg" />
        <div class="flex items-end">
          <div class="h-6 w-36 bg-ms-gray opacity-45 ml-5" />
          <div class="h-5 w-24 bg-ms-gray opacity-25 ml-3" />
        </div>
      </template>
    </div>
    <div ref="scrollToBottom" class="flex flex-col mt-auto mb-3 mx-5 overflow-hidden overflow-y-scroll">
      <div v-for="(message, index) in roomMessages" :key="message.event_id" class="w-full">
        <div v-if="differenceBetweenMessages(message, index)" class="flex justify-center py-5">
          <span class="text-ms-gray-dark">{{ differenceBetweenMessages(message, index) }}</span>
        </div>
        <div :class="messageLayout(message, index, 'space')" class="flex-grow items-end">
          <div class="flex flex-col items-start my-1">
            <div :class="[userMatrix.user_id == message.sender 
                            ? 'bg-ms-purple-light-1 self-end text-white' 
                            : 'bg-chat-member text-black', 
                          messageLayout(message, index, 'layout')]" 
                 class="w-3/5 px-6 py-5">
              {{ message.content.body }}
            </div>
            <div v-show="message.event_id === interlocutorReadState" class="self-end mt-4">
              <img :src="roomMember.content.avatar_url" class="h-8 w-8 rounded-md">
            </div>
          </div>
        </div>
      </div>
      <span v-if="memberHisTyping" class="mb-1">{{ roomMember.content.displayname }} est en train de taper</span>
    </div>
    <div class="flex items-center justify-between h-24 px-6 py-4 bg-blue rounded-br-xl">
      <FormulateInput v-model="newMessage" :outerClass="['w-full']" :disabled="Object.keys(roomActive).length == 0"
                      :inputClass="['py-4 px-5 focus:border-purple text-ms-gray-dark']"
                      type="text" placeholder="Votre message..." autocomplete="off" @keyup.enter="sendMessage" />
      <div v-if="newMessage.length" class="flex justify-center items-center w-16 h-full ml-8 bg-white rounded-lg" 
           @click="sendMessage">
        <img src="~/assets/img/middle-school/send.svg" class="flex justify-center items-center cursor-pointer">
      </div>
    </div>
  </div>
</template>


<script>

  import moment from 'moment'

  export default {
    name: 'Conversation',
    props: {
      roomActive: {
        type: Object,
        default: function() {
          return {}
        }
      }
    },
    data() {
      return {
        roomMessages: [],
        roomMember: null,
        newMessage: '',
        interlocutorReadState: null,
        loading: true,
        tokenMessage: undefined,
        limitNumberMessages: 100,
        userMatrix: this.$store.state.auth.user.matrix,
        accountType: this.$store.state.auth.user.config.accountType !== 'teacher' ? this.$store.state.auth.user.config.accountType : 'manager',
      }
    },
    computed: {
      memberHisTyping() {
        return this.roomActive.memberHisTyping
      },
      getUserIdFromUserMatrix() {
        return this.roomMember.user_id.split(':')[0].replace('@', '')
      }
    },
    watch: {
      roomActive: {
        async handler() {
          if (this.roomMember) {
            await this.retrieveMemberStatus()
            if (this.roomMessages.length) await this.setRoomReadMarkers()
          }
        },
        deep: true
      },
      async newMessage(oldValue, newValue) {
        if (!oldValue.length && newValue.length) {
          await this.sendUserTyping(false)
        } else if (oldValue.length && !newValue.length) {
          await this.sendUserTyping(true)
        }
      }
    },
    async mounted() {
      if (Object.keys(this.roomActive).length) {
        await this.retrieveMembers()
        await this.retrieveMemberStatus()
        await this.retrieveMessages()
        if (this.roomMessages.length) await this.setRoomReadMarkers()
        this.setMemberReadState()
      }
      this.loading = false
    },
    updated() {
      if (!this.loading) this.scrollToElement()
    },
    methods: {

      async sendMessage() {
        await this.$api.matrix.sendMessage({ 
          access_token: this.userMatrix.access_token, 
          message: this.newMessage, 
          room_id: this.roomActive.id 
        })
        this.newMessage = ''

        if (this.roomMessages.length) await this.setRoomReadMarkers()
      },

      async retrieveMessages() {
        let response = (await this.$api.matrix.retrieveMessages({ 
          access_token: this.userMatrix.access_token, 
          room_id: this.roomActive.id, 
          token: this.tokenMessage ? this.tokenMessage : 'END',
          limit: this.limitNumberMessages,
          filter: JSON.stringify({ types: ['m.room.message'] }),
        })) || {}

        this.tokenMessage = response.end

        response.chunk.reverse()

        this.roomMessages.unshift(...response.chunk)

        if (response.chunk.length === this.limitNumberMessages) await this.retrieveMessages()
      },

      async retrieveMembers() {

        let response = (await this.$api.matrix.retrieveMembers({
          access_token: this.userMatrix.access_token,
          room_id: this.roomActive.id
        })) || {}

        response.chunk = response.chunk.filter(member => member.user_id !== this.userMatrix.user_id)

        this.roomMember = response.chunk[0]
        this.roomMember.isTyping = false
      },

      async retrieveMemberStatus() {
        
        let response = (await this.$api.matrix.retrieveUserPresence({
          access_token: this.userMatrix.access_token,
          user_id: this.roomMember.state_key
        })) || {}

        this.roomMember.status = response
      },


      async setRoomReadMarkers() {
        await this.$api.matrix.setRoomReadMarkers({
          access_token: this.userMatrix.access_token,
          room_id: this.roomActive.id,
          body: { 
            'm.fully_read': this.roomMessages[this.roomMessages.length - 1].event_id,
            'm.read': this.roomMessages[this.roomMessages.length - 1].event_id
          }
        })
      },

      async sendUserTyping(state) {
        await this.$api.matrix.userStartedTyping({ 
          access_token: this.userMatrix.access_token, 
          user_id: this.userMatrix.user_id, 
          room_id: this.roomActive.id,
          body: { timeout: 30000, typing: state }
        }) 
      },

      setMemberReadState() {

        if (this.roomActive.interlocutorReadState) {
          this.interlocutorReadState = this.roomActive.interlocutorReadState
        } else if (this.roomActive.ephemeral.events.length) {
          let roomReceipt = this.roomActive.ephemeral.events.find(item => item.type == 'm.receipt')
          let eventIds = Object.entries({ ...roomReceipt.content }).map( ([key, value]) => {
            value.id = key
            value.user_ids = Object.keys({ ...value['m.read'] }).filter(item => item != this.userMatrix.user_id)
            return value
          })
          this.interlocutorReadState = _.get(eventIds.filter(item => item.user_ids.includes(this.roomMember.user_id)).map(item => item.id), '0', null)
        }

      },

      scrollToElement() {
        this.$refs.scrollToBottom.scrollTop = this.$refs.scrollToBottom.scrollHeight 
      },

      messageLayout(message, index, type) {

        let messageDate = moment().locale('fr').subtract(message.unsigned.age)
        let lastMessageDate = moment().locale('fr').subtract(this.roomMessages[index - 1]?.unsigned?.age)
        let nextMessageDate = moment().locale('fr').subtract(this.roomMessages[index + 1]?.unsigned?.age)

        if (this.roomMessages[index - 1]?.sender != message.sender && this.roomMessages[index + 1]?.sender != message.sender
           || this.roomMessages[index + 1]?.sender != message.sender && messageDate.diff(lastMessageDate, 'hours') > 12) {
          return type == 'space' ? 'my-3' : 'rounded-lg'
        }

        if (this.roomMessages[index - 1]?.sender == message.sender && this.roomMessages[index + 1]?.sender != message.sender
           || this.roomMessages[index + 1]?.sender == message.sender && nextMessageDate.diff(messageDate, 'hours') > 12) {
          return type == 'space' ? 'mb-3' : 'rounded-b-lg rounded-t'
        }

        if (this.roomMessages[index - 1]?.sender != message.sender && this.roomMessages[index + 1]?.sender == message.sender
           || this.roomMessages[index + 1]?.sender == message.sender && messageDate.diff(lastMessageDate, 'hours') > 12) {
          return type == 'space' ? 'mt-3' : 'rounded-t-lg rounded-b'
        }

        if (this.roomMessages[index - 1]?.sender == message.sender && this.roomMessages[index + 1]?.sender == message.sender) {
          return type == 'space' ? '' : 'rounded'
        }
      },

      differenceBetweenMessages(message, index) {

        let messageDate = moment().locale('fr').subtract(message.unsigned.age)
        let lastMessageDate = moment().locale('fr').subtract(this.roomMessages[index - 1]?.unsigned?.age)

        if (!this.roomMessages[index - 1] || messageDate.diff(lastMessageDate, 'hours') > 12)
          return messageDate.format("dddd D MMMM - HH:mm").replace(':', 'h')
      }

    }
  }

</script>

<style lang="scss">

.bg-chat-member {
  background-color: #E0DCF24C;
}

</style>
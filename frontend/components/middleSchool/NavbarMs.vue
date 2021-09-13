<template>
  <div class="flex flex-wrap w-full relative shadow-md min-h-60 py-2 bg-white">
    <img src="~/assets/img/ms-logo-cta.png" class="absolute pin-l pin-t mt-2 py-2 pl-5 h-16 cursor-pointer" @click="$router.push('/dashboard/college/student/parcours')">
    <div class="ms-container flex items-center justify-start relative">
      <div class="md:flex lg:flex xl:flex mr-auto ms-menu-item hidden">
        <div v-for="(item, index) in menuList" :key="index" class="flex flex-col justify-center items-center content-center pr-12">
          <nuxt-link :to="'/dashboard/college/' + accountType + '/' + item.slug" class="flex flex-col items-center justify-center">
            <a class="font-plex uppercase text-lg text-ms-gray-dark cursor-pointer">{{ item.name }}</a>
            <div class="dot hidden bg-black mt-2"/>
          </nuxt-link>
        </div>
      </div>
      <div class="flex md:hidden lg:hidden xl:hidden justify-end w-full pr-4"
           @click="showContent = !showContent">
        <img src="~/static/icons/Hamburger.png">
      </div>
      <div class="flex relative items-center ml-auto">
        <div v-if="accountType === 'manager' || accountType === 'operator'" class="flex flex-col items-center cursor-pointer mr-12" @click="$router.push('/dashboard/college/' + accountType + '/planning')">
          <img src="~/assets/img/middle-school/planning.svg" class="w-10 h-10 block">
          <div v-if="dropdownPlanning || getNameRoute == 'planning'" class="dot bg-ms-purple-light-1 mt-2"/>
        </div>
        <div v-click-outside="hideDropdownMessage" v-if="accountType === 'manager' || accountType === 'operator'" class="flex flex-col items-center relative mr-12">
          <img src="~/assets/img/middle-school/message.svg" class="w-10 h-10 cursor-pointer block" @click="changeStateDropdownMessage">
          <div v-if="Object.keys(userMatrix.initialSync).length && numberOfUnreadNotifications" 
               class="absolute flex justify-center items-center bg-ms-orange text-white h-5 w-5 rounded-full p-4" style="top: -5px; right: -10px;">
            {{ numberOfUnreadNotifications }}
          </div>
          <div v-if="dropdownMessage || getNameRoute == 'messages'" class="dot bg-ms-purple-light-1 mt-2"/>
          <div v-show="dropdownMessage" class="absolute" style="margin-top: 52px;">
            <Chat v-if="Object.keys(userMatrix.initialSync).length" ref="chat_dropdown"
                  typeComponent="dropdown" @hideDropdownMessage="hideDropdownMessage" />
          </div>
        </div>
        <img v-if="$store.state.auth.user.partner"
             :src="$store.state.auth.user.partner.img_path"
             class="w-18 h-12 mr-10 hidden md:block lg:block xl:block">
        <img :src="$store.state.auth.user.avatar_path || '~/static/img/profile.png'"
             class="w-10 h-10 border-2 hidden md:block lg:block xl:block border-ms-red border-solid rounded-full cursor-pointer"
             @mouseenter="dropdown = true"
             @mouseleave="dropdown = false">
        <div v-show="dropdown"
             class="dropdown absolute pin-r pin-t mt-12 bg-white w-48 px-4 py-6 rounded-lg border-1 border-solid border-ms-gray"
             @mouseenter="dropdown = true"
             @mouseleave="dropdown = false">
          <ul>
            <nuxt-link :to="'/dashboard/college/' + accountType + '/profil'"
                       tag="li"
                       class="font-plex uppercase text-base text-ms-gray-dark cursor-pointer"
                       @click.native="dropdown = false"><a>Mon profil</a></nuxt-link>
            <hr class="hr my-4">
            <li @click="logout"><a class="font-plex uppercase text-base text-ms-gray-dark cursor-pointer">Se déconnecter</a></li>
          </ul>
        </div>
      </div>
    </div>
    <div v-show="showContent" class="bg-white w-full absolute menu-mobile py-4 shadow-md">
      <div v-for="(item, index) in menuList" :key="index" class="flex flex-col justify-center items-center content-center py-2" @click="showContent = false">
        <nuxt-link :to="'/dashboard/college/' + accountType + '/' + item.slug" class="flex flex-col items-center justify-center">
          <a class=" font-plex uppercase text-lg text-ms-gray-dark cursor-pointer">{{ item.name }}</a>
          <div class="dot hidden bg-black mt-2"/>
        </nuxt-link>
      </div>
      <img src="~/static/img/profile.png" class=" w-10 h-10 border-1 border-purple border-solid rounded-full">
    </div>
  </div>
</template>

<script>

import clickOutside from '~/directives/click-outside-directive.js'
import Chat from '../middleSchool/Conversations/Chat.vue'
import { mapGetters } from 'vuex'

export default {
  name: "NavbarMs",
  components: { Chat },
  props: {
    menuList: {
      type: Array,
      default: function() {
        return []
      }
    },
    accountType: {
      type: String,
      default: function() {
        return ''
      }
    }
  },
  data() {
    return {
      dropdown: false,
      showContent: false,
      dropdownMessage: false,
      dropdownPlanning: false,
      componentChatKey: 0,
      numberOfUnreadNotifications: 0
    }
  },
  computed: {
    ...mapGetters({
      userMatrix: 'auth/getMatrix'
    }),
    getNameRoute() {
      return this.$route.path.split('/')[this.$route.path.split('/').length - 1]
    }
  },
  watch: {
    userMatrix: {
      deep: true,
      handler() {
        let typeSync = Object.keys(this.userMatrix.sync).length ? 'sync' : 'initialSync'
        let rooms = Object.values({ ...this.userMatrix[typeSync].rooms.join })
        if (rooms.length) {
          this.numberOfUnreadNotifications = rooms.map(item => item.unread_notifications.notification_count).reduce((a, b) => a + b, 0)
        }
      }
    }
  },
  async mounted() {
    if (this.accountType == 'manager' || this.accountType == 'operator') {
      let user_matrix = await this.$store.dispatch('matrixInitialSync', this.$store.state.auth.user.matrix)
      this.$store.dispatch('matrixSync', user_matrix)
    }
  },
  methods: {
    logout: function() {
      this.$store
        .commit( 'auth/destroy' )
      this.$router.push( '/' )
    },
    hideDropdownMessage() {
      this.dropdownMessage = false
    },
    changeStateDropdownMessage() {
      this.dropdownMessage = !this.dropdownMessage
    },
  }
}
</script>

<style lang="scss" scoped>
.menu-mobile {
  margin-top: 3.3rem;
}

.triangle {
  width: 0;
  height: 0;
  position: absolute;
  transform: translate(100%, -80%);

  top: 0;
  border-style: solid;
  border-width: 0 7.5px 15px 7.5px;
  border-radius: 0.5rem;
  border-color: transparent transparent #fff transparent;
}
</style>

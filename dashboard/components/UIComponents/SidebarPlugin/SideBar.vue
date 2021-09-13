<template>
  <div :class="sidebarClasses"
       :data-background-color="backgroundColor"
       :data-active-color="activeColor">
    <div class="sidebar-wrapper">
      <div class="logo">
        <a href="#" class="simple-text">
          <div class="logo-img">
            <img src="~static/img/logo-cta.png" alt="logo-dashboard">
          </div>
          {{title}}
        </a>
      </div>

      <slot name="content"></slot>
      <ul :class="navClasses">
        <!--By default vue-router adds an active class to each route link. This way the links are colored when clicked-->
        <slot>
          <sidebar-link v-for="(link,index) in sidebarLinks"
                        :key="link.name + index"
                        :to="link.path"
                        @click="closeNavbar"
                        :link="link">
            <p>{{link.name}}</p>
          </sidebar-link>
        </slot>
      </ul>
      <!--<moving-arrow :move-y="arrowMovePx">-->

      <!--</moving-arrow>-->
    </div>
  </div>
</template>
<script>
  import SidebarLink from './SidebarLink.vue'
  import SidebarDropdown from './SidebarDropdown.vue'
  import MovingArrow from './MovingArrow.vue'

  export default {
    components: {
      SidebarLink,
      SidebarDropdown,
      MovingArrow
    },
    props: {
      title: {
        type: String,
        default: 'Crée ton avenir'
      },
      type: {
        type: String,
        default: 'sidebar',
        validator: (value) => {
          let acceptedValues = ['sidebar', 'navbar']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      backgroundColor: {
        type: String,
        default: 'black',
        validator: (value) => {
          let acceptedValues = ['white', 'black', 'darkblue']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      backgroundImage: {
        type: String,
        default: 'static/img/sidebar-5.jpg'
      },
      activeColor: {
        type: String,
        default: 'success',
        validator: (value) => {
          let acceptedValues = ['primary', 'info', 'success', 'warning', 'danger']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      sidebarLinks: {
        type: Array,
        default: () => []
      },
      autoClose: {
        type: Boolean,
        default: true
      }
    },
    provide () {
      return {
        autoClose: this.autoClose
      }
    },
    computed: {
      sidebarClasses () {
        if (this.type === 'sidebar') {
          return 'sidebar'
        } else {
          return 'collapse navbar-collapse off-canvas-sidebar'
        }
      },
      navClasses () {
        if (this.type === 'sidebar') {
          return 'nav'
        } else {
          return 'nav navbar-nav'
        }
      },
      /**
       * Styles to animate the arrow near the current active sidebar link
       * @returns {{transform: string}}
       */
      arrowMovePx () {
        return this.linkHeight * this.activeLinkIndex
      }
    },
    data () {
      return {
        linkHeight: 60,
        activeLinkIndex: 0,
        windowWidth: 0,
        isWindows: false,
        hasAutoHeight: false
      }
    },
    methods: {
      findActiveLink () {
        this.sidebarLinks.find((element, index) => {
          let found = element.path === this.$route.path
          if (found) {
            this.activeLinkIndex = index
          }
          return found
        })
      }
    },
    mounted () {
      this.findActiveLink()
    },
    watch: {
      $route: function (newRoute, oldRoute) {
        this.findActiveLink()
      }
    }
  }

</script>
<style>

</style>

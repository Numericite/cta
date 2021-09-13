import Sidebar from './SideBar.vue'
import SidebarLink from './SidebarLink.vue'
import SidebarDropdown from './SidebarDropdown.vue'

const SidebarStore = {
  showSidebar: false,
  sidebarLinks: [
    {
      name: 'Dashboard',
      icon: 'ti-panel',
      path: '/admin/overview'
    }
  ],
  displaySidebar (value) {
    this.showSidebar = value
  }
}

const SidebarPlugin = {

  install (Vue) {
    Vue.mixin({
      data () {
        return {
          sidebarStore: SidebarStore
        }
      }
    })

    if(process.client){
      Object.defineProperty(Vue.prototype, '$sidebar', {
        get () {
          return this.$root.sidebarStore
        }
      })
    }
    Vue.component('side-bar', Sidebar)
    Vue.component('sidebar-dropdown', SidebarDropdown)
    Vue.component('sidebar-link', SidebarLink)
  }
}

export default SidebarPlugin

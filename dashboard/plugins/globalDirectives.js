import Vue from 'vue'
import { directive as vClickOutside } from 'vue-clickaway'
import { SUPER_ADMIN_EMAILS } from '~/config'
import _ from 'lodash'

/**
 * You can register global directives here and use them as a plugin in your main Vue instance
 */


Vue.directive('click-outside', vClickOutside);

Vue.directive('show-numericite', {
  bind(el, binding, vnode) {
    if( !_.includes(SUPER_ADMIN_EMAILS, vnode.context.$store.state.auth.user.email )) {
      el.style = 'display:none'
    }
  }
})

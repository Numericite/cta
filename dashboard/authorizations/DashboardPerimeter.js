import { createPerimeter } from 'vue-kindergarten'
import { ROLES } from '~/config'

export default createPerimeter({
  purpose: 'student',
  can: {
    accessLogin() {
      return !this.isAdmin()
    },
    accessDashboard() {
      return this.isAdmin()
    }
  },
  hasRole(role) {
    return (
      this.child &&
      this.child.roles.includes(role)
    )
  },
  isAdmin() {
    return this.child && this.child && this.hasRole(ROLES.admin.roles[0])
  }
})

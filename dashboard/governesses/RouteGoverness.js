import { HeadGoverness } from 'vue-kindergarten'

export default class RouteGoverness extends HeadGoverness {
  guard(action, { redirect }) {
    if (this.isNotAllowed(action)) {
      if (this.isAllowed('accessDashboard')) {
        redirect('/dashboard/stats')
      } else if (this.isAllowed('accessLogin')) {
        redirect('/login')
      } else {
        redirect('/')
      }
    }
  }
}

import { HeadGoverness } from 'vue-kindergarten'

export default class RouteGoverness extends HeadGoverness {
  guard(action, { redirect }) {
    if (this.isNotAllowed(action)) {
      if (this.isAllowed('accessMiddleSchoolStudentDashboard')) {
        redirect('/dashboard/college/student/parcours')
      } else if (this.isAllowed('accessHighSchoolStudentDashboard')) {
        redirect('/dashboard/student')
      } else if (this.isAllowed('accessMiddleSchoolManagerDashboard')) {
        redirect('/dashboard/college/manager')
      } else if (this.isAllowed('accessMiddleSchoolPartnerDashboard')) {
        redirect('/dashboard/college/partner')
      } else if (this.isAllowed('accessMiddleSchoolOperatorDashboard')) {
        redirect('/dashboard/college/operator')
      } else if (this.isAllowed('accessHighSchoolManagerDashboard')) {
        redirect('/dashboard/manager')
      } else if (this.isAllowed('accessDataDashboard')) {
        redirect('/dashboard/college/facility')
      } else if (this.isAllowed('accessDashboardCta')) {
        redirect(process.env.DASHBOARD_URL)
      } else {
        redirect( '/' )
      }
    }
  }
}

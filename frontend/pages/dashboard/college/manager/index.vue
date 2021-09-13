<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'DashboardManager',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessMiddleSchoolManagerDashboard',
  async asyncData({ redirect }) {
    redirect('/dashboard/college/manager/eleves')
  }
}
</script>

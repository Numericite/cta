<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'DashboardFacility',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessDataDashboard',
  async asyncData( { redirect } ) {
    redirect( '/dashboard/college/facility/stats' )
  }
}
</script>

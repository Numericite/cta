<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'DashboardOperator',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessMiddleSchoolOperatorDashboard',
  async asyncData( { redirect } ) {
    redirect( '/dashboard/college/operator/stats' )
  }
}
</script>
  
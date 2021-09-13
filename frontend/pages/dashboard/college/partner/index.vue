<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'DashboardPartner',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessMiddleSchoolPartnerDashboard',
  async asyncData( { redirect } ) {
    redirect( '/dashboard/college/partner/stats' )
  }
}
</script>

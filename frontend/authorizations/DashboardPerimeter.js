import { createPerimeter } from 'vue-kindergarten'
import { ROLES, SCHOOL_KINDS } from '~/config'

export default createPerimeter( {
  purpose: 'student',
  can: {
    accessAllDashboard() {
      return (
        this.isParent() || this.isHighSchoolTeacher() || this.isMentor() || this.isHighSchoolStudent() || this.isMiddleSchoolStudent() || this.isSchool() || this.isCollectivity()
      )
    },
    accessDocumentsPage() {
      return (
        this.isParent() || this.isHighSchoolTeacher() || this.isMentor() || this.isSchool() || this.isCollectivity()
      )
    },
    accessHighSchoolManagerDashboard() {
      return (
        this.isParent() || this.isHighSchoolTeacher() || this.isMentor()
      )
    },
    accessMiddleSchoolManagerDashboard() {
      return (
        this.isMiddleSchoolTeacher()
      )
    },
    accessMiddleSchoolPartnerDashboard() {
      return (
        this.isMiddleSchoolPartner()
      )
    },
    accessMiddleSchoolOperatorDashboard() {
      return (
        this.isMiddleSchoolOperator()
      )
    },
    accessDataDashboard() {
      return (
        this.isSchool() || this.isCollectivity()
      )
    },
    accessHighSchoolStudentDashboard() {
      return (
        this.isHighSchoolStudent()
      )
    },
    accessMiddleSchoolStudentDashboard() {
      return (
        this.isMiddleSchoolStudent()
      )
    },
    accessDashboardCta() {
      return (
        this.isAdmin()
      )
    },
    accessLanding() {
      return (
        !this.isAdmin() &&
        !this.isParent() &&
        !this.isHighSchoolStudent() &&
        !this.isMiddleSchoolStudent() &&
        !this.isHighSchoolTeacher() &&
        !this.isMiddleSchoolTeacher() &&
        !this.isMiddleSchoolPartner() &&
        !this.isMiddleSchoolOperator() &&
        !this.isMentor() &&
        !this.isSchool() &&
        !this.isCollectivity()
      )
    },
  },
  hasRole( role ) {
    return (
      this.child &&
      this.child.config.accountType &&
      this.child.config.accountType === role
    )
  },
  hasSchool( school_kind ) {
    return (
      this.child &&
      this.child.school &&
      this.child.school.kind &&
      this.child.school.kind === school_kind
    )
  },
  isAdmin() {
    return this.hasRole( ROLES.admin )
  },
  isHighSchoolStudent() {
    return this.hasRole( ROLES.student ) &&
      ( this.hasSchool( SCHOOL_KINDS.highSchool ) || this.hasSchool( SCHOOL_KINDS.highSchoolPro ) || this.hasSchool( SCHOOL_KINDS.ime ) || this.hasSchool( SCHOOL_KINDS.esat ) )
  },
  isMiddleSchoolStudent() {
    return this.hasRole( ROLES.student ) && (this.hasSchool( SCHOOL_KINDS.middleSchool ) || this.hasSchool( SCHOOL_KINDS.highSchoolNew ) || this.hasSchool( SCHOOL_KINDS.highSchoolProNew ))
  },
  isParent() {
    return this.hasRole( ROLES.parent )
  },
  isHighSchoolTeacher() {
    return this.hasRole( ROLES.teacher ) &&
      ( this.hasSchool( SCHOOL_KINDS.highSchool ) || this.hasSchool( SCHOOL_KINDS.highSchoolPro ) || this.hasSchool( SCHOOL_KINDS.ime ) || this.hasSchool( SCHOOL_KINDS.esat ) )
  },
  isMiddleSchoolTeacher() {
    return this.hasRole( ROLES.teacher ) && (this.hasSchool( SCHOOL_KINDS.middleSchool ) || this.hasSchool( SCHOOL_KINDS.highSchoolNew ) || this.hasSchool( SCHOOL_KINDS.highSchoolProNew ))
  },
  isMiddleSchoolPartner() {
    return this.hasRole( ROLES.partner )
  },
  isMiddleSchoolOperator() {
    return this.hasRole( ROLES.operator )
  },
  isMentor() {
    return this.hasRole( ROLES.mentor )
  },
  isSchool() {
    return this.hasRole( ROLES.school )
  },
  isCollectivity() {
    return this.hasRole( ROLES.collectivity )
  },
} )

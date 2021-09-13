import BlogApi from './blog'
import UserApi from './user'
import PartnersApi from './partners'
import SchoolsApi from './schools'
import ActivitiesApi from './activities'
import GeoGouvApi from './geo-gouv'
import FaqApi from './faq'
import TeachersApi from './teachers'
import ParentsApi from './parents'
import DocumentsApi from './documents'
import ExperiencesApi from './experiences'
import DomainsApi from './domains'
import ResourceApi from './resources'
import DetailsApi from './details'
import MentorsApi from './mentors'
import DataApi from './data'
import CoursesApi from './courses'
import FilesApi from './files'
import OnisepApi from './onisep'
import OnisepFeedBack from './onisepFeedBack'
import ModulesApi from './modules'
import ExplorationsTypeApi from './explorationsType'
import ExplorationsApi from './explorations'
import FieldsApi from './fields'
import FieldLogsApi from './fieldLogs'
import LeadsApi from './leads'
import SchoolReports from './schoolReports'
import RestrictionsApi from './restrictions'
import AssociationsApi from './associations'
import LoginLogApi from './loginLog'
import MatrixNumericiteApi from './matrix-numericite'
import EventApi from './events'
import BookingApi from './bookings'

export default class Api {
  constructor( $axios ) {
    this.axios = $axios
    this.blog = new BlogApi( $axios )
    this.user = new UserApi( $axios )
    this.partners = new PartnersApi( $axios )
    this.schools = new SchoolsApi( $axios )
    this.activities = new ActivitiesApi( $axios )
    this.geogouv = new GeoGouvApi( $axios )
    this.faq = new FaqApi( $axios )
    this.teachers = new TeachersApi( $axios )
    this.parents = new ParentsApi( $axios )
    this.documents = new DocumentsApi( $axios )
    this.experiences = new ExperiencesApi( $axios )
    this.domains = new DomainsApi( $axios )
    this.resources = new ResourceApi( $axios )
    this.details = new DetailsApi( $axios )
    this.mentors = new MentorsApi( $axios )
    this.data = new DataApi( $axios )
    this.courses = new CoursesApi( $axios )
    this.files = new FilesApi( $axios )
    this.onisep = new OnisepApi( $axios )
    this.onisepFeedback = new OnisepFeedBack( $axios )
    this.modules = new ModulesApi( $axios )
    this.explorationsType = new ExplorationsTypeApi( $axios )
    this.explorations = new ExplorationsApi( $axios )
    this.fields = new FieldsApi( $axios )
    this.fieldLogs = new FieldLogsApi( $axios )
    this.leads = new LeadsApi( $axios )
    this.schoolReports = new SchoolReports( $axios )
    this.restrictions = new RestrictionsApi( $axios )
    this.associations = new AssociationsApi( $axios )
    this.loginLog = new LoginLogApi( $axios )
    this.matrix = new MatrixNumericiteApi( $axios )
    this.events = new EventApi( $axios )
    this.bookings = new BookingApi( $axios )
  }
}

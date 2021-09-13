import UserApi from './users'
import ActivityApi from './activities'
import PartnerApi from './partners'
import QuestionsApi from "./questions"
import SchoolsApi from "./schools"
import PostsApi from "./posts"
import DomainsApi from "./domains"
import DocumentsApi from "./documents"
import ResourcesApi from "./resources"
import DetailsApi from "./details"
import InternshipApi from './internships'
import CompanyApi from './companies'
import MeetingApi from './meetings'
import FileApi from './files'
import DataApi from './data'
import VersionApi from './versions'
import PageApi from './pages'
import CourseApi from './courses'
import ModuleApi from './modules'
import FieldApi from './fields'
import ModuleActivityApi from './moduleActivities'
import ExplorationApi from './explorations'
import RestrictionsApi from "./restrictions";

export default class Api {
  constructor($axios) {
    this.axios = $axios
    this.users = new UserApi($axios)
    this.activities = new ActivityApi($axios)
    this.partners = new PartnerApi($axios)
    this.questions = new QuestionsApi($axios)
    this.schools = new SchoolsApi($axios)
    this.posts = new PostsApi($axios)
    this.domains = new DomainsApi($axios)
    this.documents = new DocumentsApi($axios)
    this.resources = new ResourcesApi($axios)
    this.details = new DetailsApi($axios)
    this.internships = new InternshipApi($axios)
    this.companies = new CompanyApi($axios)
    this.meetings = new MeetingApi($axios)
    this.files = new FileApi($axios)
    this.data = new DataApi($axios)
    this.versions = new VersionApi($axios)
    this.pages = new PageApi($axios)
    this.courses = new CourseApi($axios)
    this.modules = new ModuleApi($axios)
    this.fields = new FieldApi($axios)
    this.moduleActivities = new ModuleActivityApi($axios)
    this.explorations = new ExplorationApi($axios)
    this.restrictions = new RestrictionsApi($axios)
  }
}

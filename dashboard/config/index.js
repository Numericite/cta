export const API_ROOT = 'http://localhost:7000/'
export const API_ROOT_PROD = 'https://parcours-orientation.cree-ton-avenir.fr/api/'
export const WEBSITE_ROOT = 'http://localhost:3000/'
export const WEBSITE_ROOT_PROD = 'https://parcours-orientation.cree-ton-avenir.fr/'

export const REQUEST = {
  TIMEOUT: 20000
}

export const ROLES = {
  admin: {accountType: 'admin', roles: ['Administrator']},
  teacher: {accountType: 'teacher', roles: ['NormalUser', 'Teacher']},
  parent: {accountType: 'parent', roles: ['NormalUser', 'Parent']},
  mentor: {accountType: 'mentor', roles: ['NormalUser', 'Mentor']},
  school: {accountType: 'school', roles: ['NormalUser', 'School']},
  collectivity: {accountType: 'collectivity', roles: ['NormalUser', 'Collectivity']},
  student: {accountType: 'student', roles: ['NormalUser']},
  partner: {accountType: 'partner', roles: ['NormalUser', 'Partner']},
  operator: {accountType: 'operator', roles: ['NormalUser', 'Operator']}
}

export const DOMAIN_TYPES = {
  'activity-area': 'Domaine d\'activité',
  'action-verb': 'Verbe d\'action',
  'stream': 'Filière'
}

export const ACCOUNT_TYPES = {
  teacher: 'Professeur',
  parent: 'Parent',
  mentor: 'Mentor',
  student: 'Élève',
  school: 'École',
  collectivity: 'Collectivité',
  admin: 'Administrateur',
  partner: 'Partenaire',
  operator: 'Chargé des opérations'
}

export const GRADES = {
  0: 'Terminal',
  1: '1ère',
  2: '2nd',
  3: '3ème',
  4: '4ème',
  5: '5ème',
  6: '6ème',
}

export const EXPLORATION_TYPE_CATEGORIES = {
  sf: 'Savoir-faire',
  se: 'Savoir-être'
}

export const SCHOOL_KINDS = [
  {text: 'Lycée Général', value: 'high-school'},
  {text: 'Lycée Pro', value: 'high-school-pro'},
  {text: 'Lycée Général Nouveau', value: 'high-school-new'},
  {text: 'Lycée Pro Nouveau', value: 'high-school-pro-new'},
  {text: 'Collège', value: 'middle-school'},
  {text: 'IME', value: 'ime'},
  {text: 'ESAT', value: 'esat'}
]

export const SCHOOL_SPECIFICITIES = [
  {text: 'Collèges', value: 'base'},
  {text: 'REP', value: 'rep'},
  {text: 'REP+', value: 'repPlus'},
  {text: 'QPV', value: 'qpv'},
]

export const SCHOOL_TERRITORIES = [
  {text: 'Territoire urbain', value: 'urban'},
  {text: 'Cité éducative', value: 'schoolCity'},
  {text: 'Territoire ruraux (/isolé)', value: 'rural'},
  {text: 'PRE', value: 'pre'},
]

export const COMPANY_KINDS = [
  { text: 'Entreprise', value: 'company' },
  { text: 'Association', value: 'association' },
  { text: 'Collectivity', value: 'collectivity' },
  { text: 'Mairie', value: 'town-hall' },
]

export const INTERNSHIP_KINDS = [
  { text: 'DT', value: 'dt' },
  { text: 'DME', value: 'dme' }
]

export const MEETING_KINDS = [
  { text: 'École', value: 'school' },
  { text: 'Structures jeunesse', value: 'structure' }
]

export const FILE_KINDS = [
  { text: 'Fichier', value: 'file' },
  { text: 'Dossier', value: 'folder' }
]

export const SUPER_ADMIN_EMAILS = [ 'frederic.selva1@gmail.com','lelong.clement0@outlook.com']

export const STUDENT_GRADES = [
  {text: '6ème', value: 6, group: 'college'},
  {text: '5ème', value: 5, group: 'college'},
  {text: '4ème', value: 4, group: 'college'},
  {text: '3ème', value: 3, group: 'college'},
  {text: '2nd', value: 2, group: 'high-school'},
  {text: '1ère', value: 1, group: 'high-school'},
  {text: 'Terminal', value: 0, group: 'high-school'}
]

export const COURSE_DEFAULT_CHAPTERS = [
  {
    name: 'Domaines',
    slug: 'activity-area',
    display: false,
    adminDisplay: false,
    domain_ids: [],
    children: [
      {
        name: 'Connaissance de soi',
        slug: 'introspection',
        kind: 'activities',
        display: false,
        adminDisplay: false
      },
      {
        name: 'Exploration',
        slug: 'exploration',
        kind: 'tabs',
        display: false,
        adminDisplay: false
      }
    ]
  },
  {
    name: 'Filières',
    slug: 'stream',
    display: false,
    adminDisplay: false,
    domain_ids: [],
    children: [
      {
        name: 'Connaissance de soi',
        slug: 'introspection',
        kind: 'activities',
        display: false,
        adminDisplay: false
      },
      {
        name: 'Exploration',
        slug: 'exploration',
        kind: 'tabs',
        display: false,
        adminDisplay: false
      }
    ]
  },
  {
    name: 'Établissements',
    slug: 'school',
    display: false,
    adminDisplay: false,
    domain_ids: [],
    children: [
      {
        name: 'Connaissance de soi',
        slug: 'introspection',
        kind: 'activities',
        display: false,
        adminDisplay: false
      },
      {
        name: 'Exploration',
        slug: 'exploration',
        kind: 'tabs',
        display: false,
        adminDisplay: false
      }
    ]
  }
]

export default {
  API_ROOT,
  API_ROOT_PROD,
  WEBSITE_ROOT,
  WEBSITE_ROOT_PROD,
  REQUEST
}

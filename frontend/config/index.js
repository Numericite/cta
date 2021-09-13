export const API_ROOT = 'http://localhost:7000/'
export const API_ROOT_PROD = 'https://parcours-orientation.cree-ton-avenir.fr/api/'
export const API_MATRIX = 'https://matrix.numericite.eu/_matrix/client/r0'

export const REQUEST = {
  TIMEOUT: 20000
}

export const ROLES = {
  student: 'student',
  parent: 'parent',
  teacher: 'teacher',
  mentor: 'mentor',
  school: 'school',
  collectivity: 'collectivity',
  admin: 'admin',
  partner: 'partner',
  operator: 'operator'
}

export const SCHOOL_KINDS = {
  highSchool: 'high-school',
  highSchoolPro: 'high-school-pro',
  highSchoolNew: 'high-school-new',
  highSchoolProNew: 'high-school-pro-new',
  middleSchool: 'middle-school',
  ime: 'ime',
  esat: 'esat'
}

export const SCHOOL_SPECIFICITIES = {
  base: 'Collèges',
  rep: 'REP',
  repPlus: 'REP+',
  qpv: 'QPV'
}

export const SCHOOL_TERRITORIES = {
  urban: 'Territoire urbain',
  schoolCity: 'Cité éducative',
  rural: 'Territoire ruraux (/isolé)',
  pre: 'PRE',
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

export const STATUS = {
  open: 'Publié',
  standby: 'Brouillon'
}

export const EVENTS = {
  planning: {
    interview: 'Entretien de mi-parcours',
    sequence: 'Séquence',
  }
}

export const CRITERIA = [
  { id: 0, value: false, text: 'Avoir des cours qui approfondissent la théorie' },
  { id: 1, value: false, text: 'Commencer par des études courtes quitte à continuer ensuite' },
  { id: 2, value: false, text: 'Avoir l’opportunité de faire beaucoup de stages tout au long du parcours' },
  { id: 3, value: false, text: 'Etudier en alternance dès la première année' },
  { id: 4, value: false, text: 'Faire beaucoup de travaux en groupe' },
  { id: 5, value: false, text: 'Avoir beaucoup de cours magistraux en amphithéâtre' },
  { id: 6, value: false, text: 'Faire une partie de mes études à l’étranger' },
  { id: 7, value: false, text: 'Être motivé(e) par le fait se mettre en concurrence avec les autres élèves' },
  { id: 8, value: false, text: 'Pouvoir intégrer le monde du travail rapidement avec un diplôme ou bien obtenir d’autres diplômes si je veux' },
  { id: 9, value: false, text: 'M’investir à 300% dans mes études, je sais gérer une quantité de travail très importante' },
  { id: 10, value: false, text: 'Bénéficier d’un encadrement pédagogique régulier et organisé' },
  { id: 11, value: false, text: 'Faire une formation avec le minimum d’obligations financières pour moi' },
  { id: 12, value: false, text: 'Continuer à apprendre plusieurs langues vivantes' },
  { id: 13, value: false, text: 'Faire un double cursus par exemple langues et économie, sciences et sciences sociales...' },
  { id: 14, value: false, text: 'Intégrer une formation éloignée de mon domicile (changer de ville, de pays)' },
  { id: 15, value: false, text: 'Avoir des cours qui s’appuient sur la pratique' },
  { id: 16, value: false, text: 'M’investir dans des études longues (au moins un bac+ 5)' },
  { id: 17, value: false, text: 'D’abord étudier, peut-être même faire de la recherche' },
  { id: 18, value: false, text: 'Ne pas faire de l\'alternance de suite, plus tard peut être' },
  { id: 19, value: false, text: 'Faire plutôt des travaux personnels' },
  { id: 20, value: false, text: 'Avoir plutôt des cours en groupes de taille moyenne (TD/TP/...)' },
  { id: 21, value: false, text: 'Rester plutôt en France voire même pas trop loin de ma famille' },
  { id: 22, value: false, text: 'Ne pas avoir envie d’être dans une ambiance de compétition' },
  { id: 23, value: false, text: 'Choisir une formation longue avec l’intention d’aller jusqu’à la fin du cursus et intégrer le monde du travail après' },
  { id: 24, value: false, text: 'Avoir un emploi du temps qui me permette de continuer à faire d’autres activités' },
  { id: 25, value: false, text: 'Avoir plutôt beaucoup d’autonomie' },
  { id: 26, value: false, text: 'Intégrer une formation qui engendre des frais importants' },
  { id: 27, value: false, text: 'Ne pas me spécialiser dès le début pour me laisser le temps de choisir' },
  { id: 28, value: false, text: 'Être intégré(e) dans une promo où je pourrais connaitre mes camarades' },
  { id: 29, value: false, text: 'Intégrer une formation dans ma ville' }
]

export const MODAL_AUTOPORTRAIT = {
  modalRencontre: ['rencontres'],
  modalSimple: ['traits-de-personnalite'],
  modalTriple: ['matieres-preferees', 'centres-dinterets', 'competences', 'intelligences-multiples', 'contributions'],
}

export default {
  API_ROOT,
  API_ROOT_PROD,
  API_MATRIX,
  REQUEST,
  ROLES
}

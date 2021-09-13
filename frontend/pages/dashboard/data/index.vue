<template>
  <div class="dashboard flexContainer-dash bg-blue-lightest ml-32 sm:ml-0 relative">

    <div v-if="$store.state.auth.user.config.accountType === 'collectivity'" class="w-full">
      <div :class="{'bg-orange text-white': !showHighSchool, 'bg-disabled-grey text-disabled-blue': showHighSchool}"
           class="inline-block py-4 px-6 rounded-b-lg text-lg font-bold uppercase cursor-pointer hover:bg-orange hover:text-white"
           @click="changeTab(false)">
        Collèges
      </div>
      <div :class="{'bg-orange text-white': showHighSchool, 'bg-disabled-grey text-disabled-blue': !showHighSchool}"
           class="inline-block ml-2 py-4 px-6 rounded-b-lg text-lg font-bold uppercase cursor-pointer hover:bg-orange hover:text-white"
           @click="changeTab(true)">
        Lycées
      </div>
    </div>

    <!--<pre>{{ data }}</pre>-->

    <div v-if="$store.state.auth.user.config.accountType === 'school'" class="mb-16">
      <h2 class="title title--dash text-6xl pt-16 sm:text-4xl">Statistiques de l'établissement {{ school.name }}</h2>
    </div>
    <div v-else class="mb-16">
      <h2 class="title title--dash text-6xl pt-16 sm:text-4xl">Statistiques de la collectivité</h2>
    </div>
    <div class="my-12 flex flex-wrap">
      <div v-if="$store.state.auth.user.config.accountType === 'collectivity'" class="data-select-container">
        <select v-model="school" class="data-select" @change="getData()">
          <option :value="{}">Tous les établissements</option>
          <option v-for="school in filteredSchools()" :key="school.id" :value="school">{{ school.name }}</option>
        </select>
      </div>
      <div v-if="showHighSchool" :class="{'disabled': gradeDisabled}" class="data-select-container">
        <select v-model.number="filters.grade" class="data-select" @change="getData(filters.grade === -1)">
          <option :value="-1">Tous les grades</option>
          <option v-for="(grade, index) in grades" :key="index" :value="grade">{{ getGradeName(grade) }}</option>
        </select>
      </div>
      <div v-if="!classroomDisabled && showHighSchool" class="data-select-container">
        <select v-model="filters.classroom_ids" class="data-select" @change="getData()">
          <option :value="[]">Toutes les classes</option>
          <option v-for="classroom in classrooms" :key="classroom.id" :value="[classroom.id]">{{ classroom.name }}</option>
        </select>
      </div>
      <div v-else-if="school.id && showHighSchool" class="data-select-container disabled">
        <select class="data-select"><option :selected="true">Toutes les classes</option></select>
      </div>
    </div>

    <h2 class="title title--dash text-5.8xl pt-10 sm:text-4xl">Indicateurs d'activité</h2>

    <div v-if="(data.middleSchool && !showHighSchool)">
      <div class="data-container bg-white p-4 mt-8 rounded-lg flex flex-wrap">
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/nombre-stage-realise.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.middleSchool.nbInternships }}</span>
            stage réalisés
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/nombre-eleves-stages-femme.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.middleSchool.nbParticipantsGirl }}</span>
            filles ayant réalisé un stage
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/nombre-eleves-stages-homme.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.middleSchool.nbParticipantsBoy }}</span>
            garçons ayant réalisé un stage
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/nombre-etablissements-concernes.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.middleSchool.nbSchools }}</span>
            établissements scolaires concernés
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/nombre-visites-realises.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.middleSchool.nbVisits }}</span>
            visites DT réalisées
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/nombre-immersions-realises.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.middleSchool.nbImmersions }}</span>
            immersions DT réalisées
          </p>
        </div>
      </div>

      <h2 class="title title--dash mt-8 text-5.8xl pt-10 sm:text-4xl">Détails des stages</h2>

      <div class="questions w-full flex flex-col mt-8">
        <accordion :items="data.middleSchool.internships">
          <template v-slot:item="{ item }">
            <div class="flex flex-wrap">
              <div class="w-1/2 sm:w-full pr-4 sm:pr-0">
                <h3 class="w-full font-bold text-blue text-2xl">Description</h3>
                <p class="text-medium font-bold mt-4">{{ item.description }}</p>
              </div>
              <div class="w-1/2 sm:w-full pl-4 sm:pl-0">
                <h3 class="w-full font-bold text-blue text-2xl">Entreprise</h3>
                <p class="text-medium font-bold mt-4 sm:pr-2">{{ getCompanyName(item.company_id) }}</p>
                <h3 class="w-full font-bold text-blue text-2xl mt-16 sm:mt-0">École<span v-if="item.school_ids.length > 0">s</span></h3>
                <p class="text-medium font-bold mt-4 sm:pr-2">
                  <span v-for="(school_id, index) in item.school_ids" :key="school_id">{{ getSchoolName(school_id) }}<span v-if="index < (item.school_ids.length - 1)">, </span></span>
                </p>
              </div>
            </div>
            <div class="flex flex-wrap mt-8">
              <div class="w-full">
                <h3 class="w-full font-bold text-blue text-2xl">Statistiques</h3>
              </div>
              <div class="flex flex-wrap w-1/2 sm:w-full items-center">
                <img class="w-42 sm:w-1/2" src="~/assets/img/data/nombre-eleves-stages-femme.svg">
                <p class="data-text text-medium font-bold px-4 sm:w-1/2">
                  <span class="block -mb-2 text-7xl font-bold text-blue">{{ item.nb_participant_girl || 0 }}</span>
                  filles ayant réalisé un stage
                </p>
              </div>
              <div v-if="item.kind === 'dt'" class="flex flex-wrap w-1/2 sm:w-full items-center">
                <img class="w-42 sm:w-1/2" src="~/assets/img/data/nombre-immersions-realises.svg">
                <p class="data-text text-medium font-bold px-4 sm:w-1/2">
                  <span class="block -mb-2 text-7xl font-bold text-blue">{{ item.nb_immersions || 0 }}</span>
                  immersions DT réalisées
                </p>
              </div>
              <div class="flex flex-wrap w-1/2 sm:w-full items-center">
                <img class="w-42 sm:w-1/2" src="~/assets/img/data/nombre-eleves-stages-homme.svg">
                <p class="data-text text-medium font-bold px-4 sm:w-1/2">
                  <span class="block -mb-2 text-7xl font-bold text-blue">{{ item.nb_participant_boy || 0 }}</span>
                  garçons ayant réalisé un stage
                </p>
              </div>
              <div v-if="item.kind === 'dt'" class="flex flex-wrap w-1/2 sm:w-full items-center">
                <img class="w-42 sm:w-1/2" src="~/assets/img/data/nombre-visites-realises.svg">
                <p class="data-text text-medium font-bold px-4 sm:w-1/2">
                  <span class="block -mb-2 text-7xl font-bold text-blue">{{ item.nb_visits || 0 }}</span>
                  visites DT réalisées
                </p>
              </div>
            </div>
            <div class="flex flex-wrap mt-8">
              <h3 class="w-full font-bold text-blue text-2xl mb-6">Indicateurs d'impact</h3>
              <div class="flex flex-wrap items-center w-1/3 sm:w-full">
                <radial-progress-bar :diameter="120"
                                     :strokeWidth="15"
                                     :completed-steps="item.satisfaction_rate" :total-steps="100"
                                     startColor="#ffc600"
                                     stopColor="#ffc600"
                                     innerStrokeColor="rgba(255, 198, 0, 0.2)" />
                <p class="w-1/2 text-xl font-bold px-4">
                  <span class="block -mb-2 text-7xl font-bold text-blue">{{ item.satisfaction_rate || 0 }}%</span>
                  de satisfaction des jeunes
                </p>
              </div>
              <div class="flex flex-wrap items-center w-1/3 sm:w-full">
                <radial-progress-bar :diameter="120"
                                     :strokeWidth="15"
                                     :completed-steps="item.acquisition_operation_rate" :total-steps="100"
                                     startColor="#27aaff"
                                     stopColor="#27aaff"
                                     innerStrokeColor="rgba(39, 170, 255, 0.2)" />
                <p class="w-1/2 text-xl font-bold px-4">
                  <span class="block -mb-2 text-7xl font-bold text-blue">{{ item.acquisition_operation_rate || 0 }}%</span>
                  d'acquisition des codes de l'entreprise
                </p>
              </div>
              <div class="flex flex-wrap items-center w-1/3 sm:w-full">
                <radial-progress-bar :diameter="120"
                                     :strokeWidth="15"
                                     :completed-steps="item.acquisition_skills_rate" :total-steps="100"
                                     startColor="#ff9c9c"
                                     stopColor="#ff9c9c"
                                     innerStrokeColor="rgba(255, 156, 156, 0.2)" />
                <p class="w-1/2 text-xl font-bold px-4">
                  <span class="block -mb-2 text-7xl font-bold text-blue">{{ item.acquisition_skills_rate || 0 }}%</span>
                  d'acquisition des compétences
                </p>
              </div>
            </div>
          </template>
        </accordion>
      </div>
    </div>

    <div v-if="(data.highSchool && showHighSchool)">
      <div class="data-container bg-white p-4 mt-8 rounded-lg flex flex-wrap">
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/eleves.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.highSchool.nbStudents }}</span>
            élèves sont accompagnés
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/commune.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.highSchool.nbStudents }}</span>
            jeunes de la commune accompagnés
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/prof.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.highSchool.nbTeachers }}</span>
            professeurs formés
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/activites.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.highSchool.nbActivitiesCompleted }}</span>
            activités complétés
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/professionnels.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.highSchool.nbExperiences }}</span>
            rencontres avec les professionnels
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/mentor.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.highSchool.nbMentors }}</span>
            mentors
          </p>
        </div>
      </div>
      <div class="data-container bg-white p-4 mt-8 rounded-lg flex flex-wrap">
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/classes.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.highSchool.nbClassrooms }}</span>
            classes accompagnées
          </p>
        </div>
        <div v-if="data.collectivity && data.collectivity.nbStructureAware" class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/structure-jeunesse.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.collectivity.nbStructureAware }}</span>
            structures jeunesses (CIO, BIJ …) sensibilisées
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <img class="w-42 sm:w-1/2" src="~/assets/img/data/parents.svg">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.highSchool.nbParentInformed }}</span>
            parents d’élèves informés
          </p>
        </div>
      </div>
      <h2 class="title title--dash text-5.8xl pt-16 sm:text-4xl">Indicateurs d'impact</h2>

      <h2 v-if="data.highSchool.nbSchools === 1" class="title title--dash text-5.5xl mt-8">Généraux</h2>
      <div v-if="data.highSchool.nbSchools === 1" class="data-container bg-white p-4 mt-4 rounded-lg flex flex-wrap">
        <div v-if="data.highSchool.secondYearAccessRate" class="flex flex-wrap items-center">
          <radial-progress-bar :diameter="120"
                               :strokeWidth="15"
                               :completed-steps="data.highSchool.secondYearAccessRate"
                               :total-steps="100"
                               startColor="#ffc600"
                               stopColor="#ffc600"
                               innerStrokeColor="rgba(255, 198, 0, 0.2)" />
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ data.highSchool.secondYearAccessRate }}%</span>
            d'accès en 1ère
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <radial-progress-bar :diameter="120"
                               :strokeWidth="15"
                               :completed-steps="72"
                               :total-steps="100"
                               startColor="#27aaff"
                               stopColor="#27aaff"
                               innerStrokeColor="rgba(39, 170, 255, 0.2)" />
          <p class="data-text text-xl font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">72%</span>
            d’acquisition des différentes connaissances et compétences
          </p>
        </div>
        <div class="flex flex-wrap items-center">
          <radial-progress-bar :diameter="120"
                               :strokeWidth="15"
                               :completed-steps="90" :total-steps="100"
                               startColor="#ff9c9c"
                               stopColor="#ff9c9c"
                               innerStrokeColor="rgba(255, 156, 156, 0.2)" />
          <p class="data-text text-xl font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">90%</span>
            de satisfaction de chaque partie prenante
          </p>
        </div>
      </div>


      <h2 class="title title--dash text-5.5xl mt-8">Domaines</h2>
      <div class="data-container bg-white rounded-t-lg p-4 mt-4 flex flex-wrap">
        <div v-for="domain in displayedDomains" :key="domain.id" class="flex flex-wrap items-center">
          <img :src="domain.img_path" class="w-42 sm:w-1/2">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ getDomainPercentage(domain.nbOccurrences) }}%</span>
            {{ domain.name }}
          </p>
        </div>
      </div>
      <p class="text-right w-full p-4 bg-white rounded-b-lg">
        <a v-if="!moreDomains" class="cursor-pointer text-xl" @click="moreDomains = true">+ Voir plus de domaines</a>
        <a v-else class="cursor-pointer text-xl" @click="moreDomains = false">- Voir moins de domaines</a>
      </p>

      <h2 class="title title--dash text-5.5xl mt-8">Filières</h2>
      <div class="data-container bg-white p-4 mt-4 rounded-lg flex flex-wrap">
        <div v-for="stream in streams" :key="stream.id" class="flex flex-wrap items-center">
          <img :src="stream.img_path" class="w-42 sm:w-1/2">
          <p class="data-text text-medium font-bold px-4">
            <span class="block -mb-2 text-7xl font-bold text-blue">{{ getStreamPercentage(stream.nbOccurrences) }}%</span>
            {{ stream.name }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import Accordion from '~/components/dashboard/Accordion'
import heads from '~/config/meta.json'
import { GRADES } from '~/config'
import _ from 'lodash'

export default {
  name: 'DataDashboard',
  layout: 'dashboard_data',
  components: { Accordion },
  head() {
    if ( heads[this.$options.name] ) return heads[this.$options.name]
    return {
      title: 'Crée ton avenir - Dashboard data'
    }
  },
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessDataDashboard',
  async asyncData({app}) {
    try {
      let response = await app.$api.domains.getList({page: 1, numberPerPage: 1000, kind: 'activity-area'})
      const domains = response.data || []

      response = await app.$api.domains.getList({page: 1, numberPerPage: 1000, kind: 'stream'})
      const streams = response.data || []

      return { domains, streams }

    } catch (e) {
      console.log(e)
    }
  },
  data() {
    return {
      data: {},
      domains: [],
      streams: [],
      schools: [],
      classrooms: [],
      grades: [],
      filters: {
        school_ids: [],
        classroom_ids: [],
        grade: -1
      },
      school: {},
      totalDomainsOccurrences: 0,
      totalStreamsOccurrences: 0,
      indeterminate: false,
      moreDomains: false,
      progress: .5,
      showHighSchool: true,
      activeInternship: null
    }
  },
  computed: {
    gradeDisabled() {
      return this.filters.classroom_ids.length > 0 && (this.filters.classroom_ids.length === 0 || (this.filters.grade === -1))
    },
    classroomDisabled() {
      return !this.school.id || this.classrooms.length === 0 || (this.filters.grade > -1)
    },
    displayedDomains() {
      return this.moreDomains ? this.domains : this.domains.slice(0, 6)
    },
    formIsFilled() {
      return this.school.id || this.filters.grade > 0 || this.filters.classroom_ids.length > 0
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    async getData(reset) {
      try {
        let response
        const accountType = this.$store.state.auth.user.config.accountType

        if (reset) {
          this.filters.classroom_ids = []
        }

        if (accountType === 'school') {
          response = await this.$api.schools.getSchoolsByIds([this.$store.state.auth.user.config.school_id])
          this.school = response.data[0] || {}
          this.showHighSchool = (this.school.kind === 'high-school')
        } else if (!this.school.id) {
          response = await this.$api.schools.getSchoolsByIds(this.$store.state.auth.user.config.school_ids)
          this.schools = response.data || []
        }

        const school_ids = this.school.id ? [this.school.id] : _.map(this.schools, 'id')
        response = await this.$api.schools.getClassrooms({ page: 1, numberPerPage: 1000, school_ids: school_ids})
        this.classrooms = _.orderBy(response.data || [], ['grade', 'name'], ['desc', 'asc'])

        this.grades = _.orderBy(_.uniq(_.map(this.classrooms, 'grade')), [], ['desc'])

        this.filters.school_ids = (this.school && this.school.id) ? [this.school.id] : _.map(this.schools, 'id')

        if (this.filters.grade > -1) {
          this.filters.classroom_ids = _.map(_.filter(this.classrooms, {grade: this.filters.grade}), 'id')
        }

        response = await this.$api.data.getData(this.$store.state.auth.user.config.accountType, this.school.kind, this.filters)
        this.data = response.data || {}

        this.domains.forEach((domain) => {
          domain.nbOccurrences = _.find(_.get(this.data, 'highSchool.domainsOccurrences', _.get(this.data, 'middle-school.domainsOccurrences', [])), (value, key) => {
            if (key === domain.id) return value
          })
          if (!domain.nbOccurrences) domain.nbOccurrences = 0
        })
        this.domains = _.orderBy(this.domains, ['nbOccurrences'], ['desc'])
        this.totalDomainsOccurrences = _.sumBy(['nbOccurrences'], _.partial(_.sumBy, this.domains))

        this.streams.forEach((stream) => {
          stream.nbOccurrences = _.find(_.get(this.data, 'highSchool.streamsOccurrences', _.get(this.data, 'middle-school.streamsOccurrences', [])), (value, key) => {
            if (key === stream.id) return value
          })
          if (!stream.nbOccurrences) stream.nbOccurrences = 0
        })
        this.streams = _.orderBy(this.streams, ['nbOccurrences'], ['desc'])
        this.totalStreamsOccurrences = _.sumBy(['nbOccurrences'], _.partial(_.sumBy, this.streams))

      } catch (e) {
        console.log(e)
      }
    },

    resetForm() {
      this.school = {}
      this.filters.grade = -1
      this.filters.classroom_ids = []
    },

    changeTab(toHighSchool) {
      this.showHighSchool = toHighSchool

      if (this.formIsFilled) {
        this.resetForm()
        this.getData(true)
      }
    },

    filteredSchools() {
      return _.filter(this.schools, (school) => {
        return this.showHighSchool ? (school.kind === 'high-school') : (school.kind === 'middle-school')
      })
    },

    getDomainPercentage(nbOccurrences) {
      return nbOccurrences ? Math.round(nbOccurrences / this.totalDomainsOccurrences * 100) : 0
    },

    getStreamPercentage(nbOccurrences) {
      return nbOccurrences ? Math.round(nbOccurrences / this.totalStreamsOccurrences * 100) : 0
    },

    getGradeName(grade) {
      return GRADES[grade]
    },

    getCompanyName(company_id) {
      return _.get(_.find(_.get(this.data, 'middle-school.companies', []), {id: company_id}), 'name', '')
    },

    getSchoolName(school_id) {
      return _.get(_.find(_.get(this.data, 'middle-school.internshipSchools', []), {id: school_id}), 'name', '')
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  width: calc(100vw - 9.1rem);
  @screen sm {
    width: 100vw;
  }

  .data-container {

    .data-text {
      width: 55%;
    }

    & > div {
      width: 30%;

      &:nth-child(n + 4) {
        @apply mt-8;
      }

      &:nth-child(3n - 1) {
        width : 40%;
      }
    }

    @screen sm {
      .data-text {
        @apply w-1/2;
      }

      & > div {
        @apply w-full;


        &:not(:first-of-type) {
          @apply mt-8;
        }

        &:nth-child(3n - 1) {
          @apply w-full;
        }
      }
    }
  }

  .radial-progress-container {
    transform: rotate(-90deg);
  }

  .data-select-container {
    @apply relative cursor-pointer mr-8;

    &:before, &:after {
      content: '';
      top: 50%;
      @apply w-4 h-4 border-solid border-blue absolute pin-r mr-6 pointer-events-none;
    }

    &:before {
      @apply border-l-3;
      transform: translateY(-70%)rotate(-45deg);
    }

    &:after {
      @apply border-r-3;
      transform: translateY(-70%)rotate(45deg);
    }

    .data-select {
      @apply px-8 py-6 pr-20 bg-white rounded-xl text-blue text-2xl font-bold shadow-md cursor-pointer;

      &:focus {
        @apply outline-none;
      }
    }

    &.disabled {

      &:before, &:after {
        @apply border-disabled-blue;
      }

      .data-select {
        @apply pointer-events-none bg-disabled-grey text-disabled-blue shadow-none;
      }
    }
  }
}
</style>

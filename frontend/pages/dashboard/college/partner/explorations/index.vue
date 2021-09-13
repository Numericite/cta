<template>
  <div class="pt-16 px-5 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <CollegeModal v-show="showVideoExplorationsPartner" :count="1" @handleClose="closeVideoExplorationsPartnerModal">
      <VideoModal :video="videoExplorationsPartner"/>
    </CollegeModal>
    <CollegeModal v-if="showCreateModal"
                  :count="2"
                  @handleClose="handleCloseModal">
      <ExplorationPartnerModal :schools="schools"
                               :gradesAll="grades"
                               :partner="partner"
                               :explorationType="selectedExplorationType"
                               @closeModal="handleCloseModal" />
    </CollegeModal>
    <div class="flex items-center justify-between">
      <div class="text-6xl">Bonjour <span class="ms-title">{{ $store.state.auth.user.firstName }} </span></div>
      <button class="center-vertical button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue p-4" @click="showVideoExplorationsPartner = true">
        <img src="~/assets/img/play.svg" class="h-3 w-4"> Revoir la vidéo de l'espace partenaire
      </button>
    </div>
    <div class="flex items-center pt-11 pb-6">
      <div class="dot mr-4 bg-black" />
      <div class="ms-subtitle">{{ explorationsTypes.length }} EXPLORATIONS</div>
      <div class="dot ml-4 bg-black" />
    </div>
    <FiltersPartner :account="partner"
                    :schools="schools"
                    :gradesAll="grades"
                    :typePage="'explorations'"
                    @getDataFilters="getPartnerExplorations" />
    <div class="text-4xl font-bold">Explication d'une exploration</div>
    <div class="pt-2 pb-7 ms-subtitle-small text-justify text-ms-gray-dark">
      Les explorations correspondent aux ateliers des associations Défi Jeunesse.
      Pour chaque atelier vous allez créer une exploration et l'affecter à un collège/niveau de classe/classe.
      Exemple : Si tous les 4ème de Monod ont 4 ateliers avec S'Orienter Ensemble, vous allez créer 4 explorations,
      une par atelier, et les affecter à l'ensemble des classes du niveau.
    </div>
    <div class="relative flex justify-center items-center mb-12 h-24 rounded-lg border border-dashed border-ms-red cursor-pointer"
         @click="openModalCreate({})">
      <img class="bg-white absolute illustration-row focus:outline-none"
           src="~/assets/img/middle-school/bubble_plus.svg">
      <span class="text-5.2xl text-ms-red font-medium"> Créer une nouvelle exploration </span>
    </div>
    <div v-for="explorationType in explorationsTypes" :key="explorationType.id" class="flex justify-between items-center px-6 py-8 mb-5 border-1 shadow-md bg-white rounded-xl">
      <div class="flex flex-col ml-2">
        <h1 class="font-plex font-semibold text-5.2xl">{{ explorationType.name }}</h1>
        <div class="flex items-center pt-4">
          <span class="font-plex text-ms-gray-dark"> Créée le {{ new Date(explorationType.created_date).toLocaleDateString('fr-Fr', {year: 'numeric', month: 'numeric', day: 'numeric'}) }} </span>
          <div class="dot mx-4 bg-black" />
          <span :class="[explorationType.status_name === 'open' ? 'text-ms-green' : 'text-ms-red-crisp']" class="font-plex">
            {{ STATUS[explorationType.status_name] }}
          </span>
        </div>
      </div>
      <div class="flex items-end relative">
        <a :href="'/dashboard/college/partner/explorations/' + explorationType.id + '/formulaire'" class="outline-none border-1 border-solid p-5 mr-2 rounded-lg text-white bg-ms-blue focus:outline-none hover:border-0 hover:text-white">
          <img src="~/assets/img/FormExploration.svg" class="h-3 w-4 text-white font-extrabold"> Formulaire
        </a>
        <button class="outline-none border-1 border-solid p-5 mx-2 rounded-lg text-white bg-ms-red focus:outline-none" @click="openModalCreate(explorationType)">
          <img src="~/assets/img/EditExploration.svg" class="h-3 w-4 text-white font-extrabold">
        </button>
        <button class="outline-none border-1 border-solid p-5 ml-2 rounded-lg bg-ms-red-crisp focus:outline-none" @click.prevent="handleDelete(explorationType.id)">
          <img src="~/assets/img/TrashExploration.svg" class="h-3 w-4 font-extrabold">
        </button>
      </div>
    </div>
  </div>
</template>

<script>

import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import CollegeModal from '../../../../../components/middleSchool/CollegeModal'
import VideoModal from '../../../../../components/middleSchool/modals/VideoModal'
import ExplorationPartnerModal from '../../../../../components/middleSchool/modals/ExplorationPartnerModal'
import FiltersPartner from '../../../../../components/middleSchool/FiltersPartner'
import Button from '../../../../../mixins/button'
import { STATUS } from '../../../../../config/index'

export default {
  layout: 'dashboard_ms_partner',
  name: 'DashboardPartnerExploration',
  components: { Button, CollegeModal, ExplorationPartnerModal, FiltersPartner, VideoModal },
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessMiddleSchoolPartnerDashboard',
  async asyncData( { app } ) {
    try {

      let response = await app.$api.partners.getPartnersByIds( [app.store.state.auth.user.config.partner_id] )
      let partner = response.data[0] || {}

      partner.accountType = app.store.state.auth.user.config.accountType

      response = await app.$api.schools.getSchoolsByIds( [partner.school_ids] )
      const schools = response.data

      response = await app.$api.schools.getGradesBySchools( [partner.school_ids] )
      const grades = response.data.sort()

      return { partner, schools, grades }
    }
    catch ( e ) {
      console.log( e )
    }
  },
  data() {
    return {
      explorationsTypes: [],
      selectedExplorationType: {},
      showCreateModal: false,
      showVideoExplorationsPartner: !this.$store.state.auth.user.config.sawVideoExplorationsPartnerMs,
      videoExplorationsPartner: {
        title: 'Vidéo de présentation de l\'espace exploration',
        path: 'https://vimeo.com/524290815'
      },
      dataFilters: {},
      count: 0,
      STATUS: STATUS,
      pagination: {
        page: 1,
        numberPerPage: 1000,
        numberOfPages: 0
      },
    }
  },
  watch: {
    selectedExplorationType() {
      console.log('ROOT CHANGE')
    },
  },
  methods: {
    handleCloseModal() {
      this.getPartnerExplorations( this.dataFilters )
      this.showCreateModal = false
    },
    async handleDelete( id ) {
      await this.$api.explorationsType.delete( { ids: [id] } )
      this.getPartnerExplorations( this.dataFilters )
    },
    openModalCreate( explorationType ) {
      this.selectedExplorationType = explorationType
      this.showCreateModal = true
    },

    showVideoExplorationsPartnerModal() {
      this.showVideoExplorationsPartner = true
    },

    async closeVideoExplorationsPartnerModal() {
      const user = this.$store.state.auth.user
      user.config.sawVideoExplorationsPartnerMs = true
      this.$store.commit('auth/setUser', user)
      await this.$api.user.updateUser(this.$store.state.auth.user)
      this.showVideoExplorationsPartner = !this.$store.state.auth.user.config.sawVideoExplorationsPartnerMs
    },

    async getPartnerExplorations( dataFilters ) {

      this.dataFilters = dataFilters

      let response = await this.$api.partners.countExplorationsType( {
        partner_slug: this.partner.slug,
        school_ids: this.dataFilters.school_ids,
        grade: this.dataFilters.grade,
        classroom_id: this.dataFilters.class_id,
        name: this.dataFilters.full_name
      } )
      this.count = response.data.count || 0

      this.pagination.numberOfPages = Math.trunc( ( this.count + this.pagination.numberPerPage - 1 ) / this.pagination.numberPerPage )

      response = await this.$api.partners.getExplorationsType( {
        partner_slug: this.partner.slug,
        school_ids: this.dataFilters.school_ids,
        grade: this.dataFilters.grade,
        classroom_id: this.dataFilters.class_id,
        name: this.dataFilters.full_name,
        page: this.pagination.page,
        numberPerPage: this.pagination.numberPerPage
      } )
      this.explorationsTypes = response.data || []

    }
  },
}
</script>

<style lang="scss">
.illustration-row {
  top: 50%;
  left: 0;
  transform: translate(-50%, -50%);
}

.btn-grp {
  top: 0%;
  right: -65%;
  transform: translate(-50%, -50%);
}
</style>

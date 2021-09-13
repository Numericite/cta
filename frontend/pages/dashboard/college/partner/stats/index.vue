<template>
  <div class="pt-16 px-5 xl:px-0 xl:pt-32 flex flex-col">
    <CollegeModal v-show="showVideoStats" :count="1" @handleClose="closeVideoStatsModal">
      <VideoModal :video="videoStats"/>
    </CollegeModal>
    <div class="flex items-center justify-between pb-8">
      <div class="text-6xl">Bonjour <span class="ms-title">{{ $store.state.auth.user.firstName }} </span></div>
      <button class="center-vertical button-ms text-white bg-ms-blue border-solid border-1 border-ms-blue hover:bg-ms-blue p-4" @click="showVideoStats = true">
        <img src="~/assets/img/play.svg" class="h-3 w-4"> Revoir la vidéo de présentation
      </button>
    </div>
    <FiltersPartner :account="partner" :schools="schools" :gradesAll="grades" :typePage="'stats'" @getDataFilters="getPartnerStats" />
    <div class="bg-white rounded-lg mt-4">
      <div v-if="partnerStats" class="flex flex-wrap border-1 border-solid rounded-lg border-ms-red m-3">
        <div v-for="(studentStat, index) in Object.values(partnerStats.countStudents)" :key="index" class="flex flex-1 py-6 items-center">
          <div class="flex flex-col flex-1 px-20 items-center">
            <h1 class="ms-title text-ms-red">{{ studentStat }}</h1>
            <span class="text-ms-gray-dark text-center text-3xl mt-2"> {{ textStudentStats[index] }} </span>
          </div>
          <div :class="[ index !== Object.keys(partnerStats.countStudents).length - 1 ? 'border-stats' : '' ]" class="h-12 border-ms-red"/>
        </div>
      </div>
    </div>
    <div class="bg-white rounded-lg mt-8">
      <div v-if="partnerStats" class="flex flex-wrap border-1 border-solid rounded-lg border-ms-red m-3">
        <div v-for="(explorationStat, index) in Object.values(partnerStats.countExplorations)" :key="index" class="flex flex-1 py-6 items-center">
          <div class="flex flex-col flex-1 px-20 items-center">
            <h1 class="ms-title text-ms-red">{{ explorationStat }}</h1>
            <span class="text-ms-gray-dark text-center text-3xl mt-2"> {{ textExplorationStats[index] }} </span>
          </div>
          <div :class="[ index !== Object.keys(partnerStats.countExplorations).length - 1 ? 'border-stats' : '' ]" class="h-12 border-ms-red"/>
        </div>
      </div>
    </div>
    <div v-if="partnerStats" class="flex flex-wrap">
      <div class="sm:w-full w-1/2 bg-white rounded-lg mt-8">
        <div class="flex flex-col items-center border-1 border-solid rounded-lg border-ms-red m-3 p-5">
          <h1 class="ms-title-medium mt-3 mb-5">Genres</h1>
          <bar-chart v-if="partnerStats.countUsersSexes.length" :chartData="chartsdata.sexUsers" :options="options" :width="200" :height="270"/>
          <div v-if="!partnerStats.countUsersSexes.length" class="flex justify-center items-center doughnut-dimension">
            <h1 class="ms-gray-dark text-5xl">Aucune donnée</h1>
          </div>
        </div>
      </div>
      <div class="sm:w-full w-1/2 bg-white rounded-lg mt-8">
        <div class="flex flex-col items-center border-1 border-solid rounded-lg border-ms-red m-3 p-5">
          <h1 class="ms-title-medium mt-3 mb-5">Classes</h1>
          <bar-chart v-if="partnerStats.countClassrooms.length" :chartData="chartsdata.classrooms" :options="options" :width="200" :height="270"/>
          <div v-if="!partnerStats.countClassrooms.length" class="flex justify-center items-center doughnut-dimension">
            <h1 class="ms-gray-dark text-5xl">Aucune donnée</h1>
          </div>
        </div>
      </div>
      <div class="sm:w-full w-1/2 bg-white rounded-lg mt-8">
        <div class="flex flex-col items-center border-1 border-solid rounded-lg border-ms-red m-3 p-5">
          <h1 class="ms-title-medium mt-3 mb-5">Aide aux choix</h1>
          <bar-chart v-if="partnerStats.countLeads.length" :chartData="chartsdata.leads" :options="options" :width="200" :height="270"/>
          <div v-if="!partnerStats.countLeads.length" class="flex justify-center items-center doughnut-dimension">
            <h1 class="ms-gray-dark text-5xl">Aucune donnée</h1>
          </div>
        </div>
      </div>
      <div class="sm:w-full w-1/2 bg-white rounded-lg mt-8">
        <div class="flex flex-col items-center border-1 border-solid rounded-lg border-ms-red m-3 p-5">
          <h1 class="ms-title-medium mt-3 mb-5">Etablissements</h1>
          <bar-chart v-if="partnerStats.countSchoolsSpecificities.length" :chartData="chartsdata.specificitySchools" :options="options" :width="200" :height="270"/>
          <div v-if="!partnerStats.countSchoolsSpecificities.length" class="flex justify-center items-center doughnut-dimension">
            <h1 class="ms-gray-dark text-5xl">Aucune donnée</h1>
          </div>
        </div>
      </div>
      <div class="sm:w-full w-1/2 bg-white rounded-lg mt-8">
        <div class="flex flex-col items-center border-1 border-solid rounded-lg border-ms-red m-3 p-5">
          <h1 class="ms-title-medium mt-3 mb-5">Territoires</h1>
          <bar-chart v-if="partnerStats.countSchoolsTerritories.length" :chartData="chartsdata.territorySchools" :options="options" :width="200" :height="270"/>
          <div v-if="!partnerStats.countSchoolsTerritories.length" class="flex justify-center items-center doughnut-dimension">
            <h1 class="ms-gray-dark text-5xl">Aucune donnée</h1>
          </div>
        </div>
      </div>
      <div class="sm:w-full w-1/2 bg-white rounded-lg mt-8">
        <div class="flex flex-col items-center border-1 border-solid rounded-lg border-ms-red m-3 p-5">
          <h1 class="ms-title-medium mt-3 mb-5">Catégories</h1>
          <bar-chart v-if="partnerStats.countExplorationCategories.length" :chartData="chartsdata.categoryExplorations" :options="options" :width="200" :height="270"/>
          <div v-if="!partnerStats.countExplorationCategories.length" class="flex justify-center items-center doughnut-dimension">
            <h1 class="ms-gray-dark text-5xl">Aucune donnée</h1>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>

import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import FiltersPartner from '../../../../../components/middleSchool/FiltersPartner'
import BarChart from '../../../../../components/middleSchool/Charts/Chart'
import VideoModal from '../../../../../components/middleSchool/modals/VideoModal'
import CollegeModal from '../../../../../components/middleSchool/CollegeModal'
import { colors } from '~/tailwind'
import { GRADES, SCHOOL_SPECIFICITIES, SCHOOL_TERRITORIES, EXPLORATION_TYPE_CATEGORIES } from '~/config'

export default {
  layout: 'dashboard_ms_partner',
  name: 'DashboardPartnerStats',
  components: { FiltersPartner, BarChart, CollegeModal, VideoModal },
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessMiddleSchoolPartnerDashboard',
  data() {
    return {
      dataFilters: null,
      partnerStats: null,
      showVideoStats: !this.$store.state.auth.user.config.sawVideoStatsMs,
      videoStats: {
        title: 'Vidéo de présentation de l\'espace partenaire',
        path: 'https://vimeo.com/524287838'
      },
      chartsdata: {
        classrooms: {},
        leads: {},
        sexUsers: {},
        specificitySchools: {},
        territorySchools: {},
        categoryExplorations: {}
      },
      chartsdataColors: [colors['ms-red'], colors['ms-red-light-1'], colors['ms-red-light-2'], colors['ms-red-light-3']],
      textStudentStats: ['élèves suivis', 'établissements partenaires', 'classes', 'associations'],
      textExplorationStats: ['explorations créées', 'explorations complétées', 'connexions'],
      options: {
        responsive: true,
        maintainAspectRatio: false,
        cutoutPercentage: 78,
        legend: {
          display: true,
          position: 'bottom',
          labels: {
            usePointStyle: true,
            generateLabels: function(chart) {
              var labels = [];

              var total = 0;
              chart.config.data.datasets[0].data.forEach(function (number) {
                total += number;
              });

              chart.config.data.labels.map(function (label, index) {
                var labelNew = {
                  text: (label.length > 30 ? label.substr(0, 30) + '...' : label) + ' ' + Math.round(chart.config.data.datasets[0].data[index] * 100 / total) + '%',
                  fillStyle: chart.config.data.datasets[0].backgroundColor[index],
                  strokeStyle: 'transparent',
                  pointStyle: 'rectRounded',
                };
                labels.push(labelNew);
              });
              return labels;
            }
          }
        },
        tooltips: {
          callbacks: {
            title: function(tooltipItem, data) {
              return data['labels'][tooltipItem[0]['index']];
            },
            label: function(tooltipItem, data) {
              return data['datasets'][0]['data'][tooltipItem['index']];
            },
          },
          xPadding: 14,
          yPadding: 10,
          backgroundColor: '#FFF',
          titleFontSize: 16,
          titleAlign: 'center',
          titleFontColor: colors['ms-red'],
          bodyAlign: 'center',
          bodyFontColor: '#000',
          bodyFontSize: 14,
          borderColor: colors['ms-gray-dark'],
          borderWidth: 2,
          displayColors: false
        },
        hover: {
          mode: null
        },
        animation: {
          duration: 1500,
        }
      },
    }
  },
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
  methods: {

    showVideoStatsModal() {
      this.showVideoStats = true
    },

    async closeVideoStatsModal() {
      const user = this.$store.state.auth.user
      user.config.sawVideoStatsMs = true
      this.$store.commit('auth/setUser', user)
      await this.$api.user.updateUser(this.$store.state.auth.user)
      this.showVideoStats = !this.$store.state.auth.user.config.sawVideoStatsMs
    },

    async getPartnerStats(dataFilters) {

      this.dataFilters = dataFilters

      try {
        let response = await this.$api.partners.getPartnerStats({
          partner_slug: this.partner.slug,
          school_ids: this.dataFilters.school_ids,
          grade: this.dataFilters.grade,
          classroom_id: this.dataFilters.class_id,
        })

        this.partnerStats = response.data || {}
      } catch (e) {
        console.log(e)
      }

      if (!this.dataFilters.grade && !this.dataFilters.class_id) {

        this.chartsdata.classrooms = {
          labels: this.partnerStats.countClassrooms.map(item => GRADES[item.name]),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.partnerStats.countClassrooms.map(item => item.length),
            borderWidth: 0
          }]
        }

        this.chartsdata.leads = {
          labels: this.partnerStats.countLeads.map(item => item.name),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.partnerStats.countLeads.map(item => item.length),
            borderWidth: 0
          }]
        }

        this.chartsdata.sexUsers = {
          labels: this.partnerStats.countUsersSexes.map(item => item.name === 'man' ? 'Garçons' : 'Filles'),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.partnerStats.countUsersSexes.map(item => item.length),
            borderWidth: 0
          }]
        }

        this.chartsdata.specificitySchools = {
          labels: this.partnerStats.countSchoolsSpecificities.map(item => SCHOOL_SPECIFICITIES[item.name]),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.partnerStats.countSchoolsSpecificities.map(item => item.length),
            borderWidth: 0
          }]
        }

        this.chartsdata.territorySchools = {
          labels: this.partnerStats.countSchoolsTerritories.map(item => SCHOOL_TERRITORIES[item.name]),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.partnerStats.countSchoolsTerritories.map(item => item.length),
            borderWidth: 0
          }]
        }
        
        this.chartsdata.categoryExplorations = {
          labels: this.partnerStats.countExplorationCategories.map(item => EXPLORATION_TYPE_CATEGORIES[item.name]),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.partnerStats.countExplorationCategories.map(item => item.length),
            borderWidth: 0
          }]
        }

      }

    },
  }
}

</script>

<style lang="scss">

.border-stats {
  border-right-width: 1px;
  border-right-style: solid;
  opacity: 0.5;
}

.doughnut-dimension {
  width: 200px;
  height: 270px;
}

</style>

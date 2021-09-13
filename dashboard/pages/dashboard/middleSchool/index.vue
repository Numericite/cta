<template>
  <v-app class="no-style">
    <div>
      <h2 class="mt-2 mb-4">Statistiques d'usage - Collège</h2>
      <v-container grid-lis-md class="card z-10 mb-4">
        <v-layout wrap>
          <v-flex>
            <v-autocomplete :items="schools"
                            v-model="filters.school_ids"
                            multiple
                            chips
                            item-value="id"
                            item-text="name"
                            :component-item="nameItemTemplate"
                            :wait="0"
                            attach
                            label="Établissements *"
                            @change="getData()"></v-autocomplete>
          </v-flex>
        </v-layout>
      </v-container>
      <v-container v-if="msStats" class="card border-card d-flex align-center z-10 mb-4">
        <div class="col-6 d-flex align-center" style="flex: 1 1!important;" v-for="(studentStat, index) in Object.values(msStats.countStudents)" :key="index">
          <v-layout class="d-flex items-center" align-center justify-center column>
            <h1 class="number mt-0 mb-0">{{ studentStat }}</h1>
            <span class="text-number mt-1"> {{ textStudentStats[index] }} </span>
          </v-layout>
          <div :class="[ index !== Object.keys(msStats.countStudents).length - 1 ? 'border-stats' : '' ]" style="flex-grow: 0!important;" />
        </div>
      </v-container>
      <v-container v-if="msStats" class="card border-card d-flex align-center z-10 mb-4">
        <div class="d-flex align-center" style="flex: 1 1!important;" v-for="(explorationStats, index) in Object.values(msStats.countExplorations)" :key="index">
          <v-layout class="d-flex items-center" align-center justify-center column>
            <h1 class="number mt-0 mb-0">{{ explorationStats }}</h1>
            <span class="text-number mt-1"> {{ textExplorationStats[index] }} </span>
          </v-layout>
          <div :class="[ index !== Object.keys(msStats.countExplorations).length - 1 ? 'border-stats' : '' ]" style="flex-grow: 0!important;" />
        </div>
      </v-container>
      <v-layout v-if="msStats" class="d-flex" wrap>
        <v-flex class="wrapper-card-stats mt-8">
          <v-layout class="d-flex column card card-stats border-card align-center ml-0">
            <h1 class="ms-title-medium mt-3">Genres</h1>
            <bar-chart v-if="msStats.countUsersSexes.length" :chartData="chartsdata.sexUsers" :options="options" :width="200" :height="270"/>
            <div v-if="!msStats.countUsersSexes.length" class="d-flex justify-center align-center text-center doughnut-dimension">
              <h1 class="mb-0 mt-0">Aucune donnée</h1>
            </div>
          </v-layout>
        </v-flex>
        <v-flex class="wrapper-card-stats mt-8">
          <v-layout class="d-flex column card card-stats border-card align-center mr-0">
            <h1 class="ms-title-medium mt-3">Classes</h1>
            <bar-chart v-if="msStats.countClassrooms.length" :chartData="chartsdata.classrooms" :options="options" :width="200" :height="270"/>
            <div v-if="!msStats.countClassrooms.length" class="d-flex justify-center items-center doughnut-dimension">
              <h1 class="mb-0 mt-0">Aucune donnée</h1>
            </div>
          </v-layout>
        </v-flex>
        <v-flex class="wrapper-card-stats mt-8">
          <v-layout class="d-flex column card card-stats border-card align-center ml-0">
            <h1 class="ms-title-medium mt-3">Aide aux choix</h1>
            <bar-chart v-if="msStats.countLeads.length" :chartData="chartsdata.leads" :options="options" :width="200" :height="270"/>
            <div v-if="!msStats.countLeads.length" class="d-flex justify-center align-center doughnut-dimension">
              <h1 class="mb-0 mt-0">Aucune donnée</h1>
            </div>
          </v-layout>
        </v-flex>
        <v-flex class="wrapper-card-stats mt-8">
          <v-layout class="d-flex column card card-stats border-card align-center mr-0">
            <h1 class="ms-title-medium mt-3">Etablissements</h1>
            <bar-chart v-if="msStats.countSchoolsSpecificities.length" :chartData="chartsdata.specificitySchools" :options="options" :width="200" :height="270"/>
            <div v-if="!msStats.countSchoolsSpecificities.length" class="d-flex justify-center align-center doughnut-dimension">
              <h1 class="mb-0 mt-0">Aucune donnée</h1>
            </div>
          </v-layout>
        </v-flex>
        <v-flex class="wrapper-card-stats mt-8">
          <v-layout class="d-flex column card card-stats border-card align-center ml-0">
            <h1 class="ms-title-medium mt-3">Territoires</h1>
            <bar-chart v-if="msStats.countSchoolsTerritories.length" :chartData="chartsdata.territorySchools" :options="options" :width="200" :height="270"/>
            <div v-if="!msStats.countSchoolsTerritories.length" class="d-flex justify-center align-center doughnut-dimension">
              <h1 class="ms-gray-dark text-5xl">Aucune donnée</h1>
            </div>
          </v-layout>
        </v-flex>
        <v-flex class="wrapper-card-stats mt-8">
          <v-layout class="d-flex column card card-stats border-card align-center mr-0">
            <h1 class="ms-title-medium mt-3">Catégories</h1>
            <bar-chart v-if="msStats.countExplorationCategories.length" :chartData="chartsdata.categoryExplorations" :options="options" :width="200" :height="270"/>
            <div v-if="!msStats.countExplorationCategories.length" class="d-flex justify-center align-center doughnut-dimension">
              <h1 class="mb-0 mt-0">Aucune donnée</h1>
            </div>
          </v-layout>
        </v-flex>
      </v-layout>
    </div>
  </v-app>
</template>

<script>

  import StatsCard from '~/components/UIComponents/Cards/StatsCard.vue'
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import TemplateItem from '~/components/autocomplete/NameItemTemplate'
  import BarChart from '~/components/charts/Chart'
  import { GRADES, SCHOOL_SPECIFICITIES, SCHOOL_TERRITORIES, EXPLORATION_TYPE_CATEGORIES } from '~/config'
  import _ from 'lodash'

  export default {
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    components: { TemplateItem, StatsCard, BarChart },
    async asyncData({app}) {
      try {
        
        let response = await app.$api.schools.getList({page: 1, numberPerPage: 10000, kinds: 'middle-school'})
        const schools = response.data || []

        return { schools }

      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        filters: {},
        msStats: null,
        chartsdata: {
          classrooms: {},
          leads: {},
          sexUsers: {},
          specificitySchools: {},
          territorySchools: {},
          categoryExplorations: {}
        },
        chartsdataColors: ['#ff8f5e', '#FFB798', '#FFDACA', '#FFE7DD'],
        textStudentStats: ['élèves', 'établissements', 'classes', 'associations'],
        textExplorationStats: ['explorations créées', 'explorations complétées', 'connexions'],
        nameItemTemplate: TemplateItem,
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
                  if (label) {
                    var labelNew = {
                      text: (label.length > 30 ? label.substr(0, 30) + '...' : label) + ' ' + Math.round(chart.config.data.datasets[0].data[index] * 100 / total) + '%',
                      fillStyle: chart.config.data.datasets[0].backgroundColor[index],
                      strokeStyle: 'transparent',
                      pointStyle: 'rectRounded',
                    };
                    labels.push(labelNew);
                  }
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
            titleFontColor: '#ff8f5e',
            bodyAlign: 'center',
            bodyFontColor: '#000',
            bodyFontSize: 14,
            borderColor: '#66615b',
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
    created() {
      this.getData()
    },
    methods: {
      async getData() {

        let response = await this.$api.partners.getPartnerStats({ kind: 'middle-school', school_ids: this.filters.school_ids })
        this.msStats = response.data || {}

        this.chartsdata.classrooms = {
          labels: this.msStats.countClassrooms.map(item => GRADES[item.name]),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.msStats.countClassrooms.map(item => item.length),
            borderWidth: 0
          }]
        }

        this.chartsdata.leads = {
          labels: this.msStats.countLeads.map(item => item.name),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.msStats.countLeads.map(item => item.length),
            borderWidth: 0
          }]
        }

        this.chartsdata.sexUsers = {
          labels: this.msStats.countUsersSexes.map(item => item.name === 'man' ? 'Garçons' : 'Filles'),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.msStats.countUsersSexes.map(item => item.length),
            borderWidth: 0
          }]
        }

        this.chartsdata.specificitySchools = {
          labels: this.msStats.countSchoolsSpecificities.map(item => SCHOOL_SPECIFICITIES.filter(specificity => specificity.value === item.name).map(obj => obj.text)),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.msStats.countSchoolsSpecificities.map(item => item.length),
            borderWidth: 0
          }]
        }

        this.chartsdata.territorySchools = {
          labels: this.msStats.countSchoolsTerritories.map(item => SCHOOL_TERRITORIES.filter(territory => territory.value === item.name).map(obj => obj.text)),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.msStats.countSchoolsTerritories.map(item => item.length),
            borderWidth: 0
          }]
        }
        
        this.chartsdata.categoryExplorations = {
          labels: this.msStats.countExplorationCategories.map(item => EXPLORATION_TYPE_CATEGORIES[item.name]),
          datasets: [{
            backgroundColor: this.chartsdataColors,
            data: this.msStats.countExplorationCategories.map(item => item.length),
            borderWidth: 0
          }]
        }

      }
    }
  }

</script>

<style lang="scss" scoped>

  .v-input--selection-controls:not(.v-input--hide-details) .v-input__slot {
    margin-bottom: 0;
  }

  .number {
    color: $main-color;
  }

  .text-number {
    font-size: 16px;
  }

  .border-card {
    border: 1px solid $main-color;
  }

  .border-stats {
    height: 3rem;
    border-color: $main-color;
    border-right-width: 1px;
    border-right-style: solid;
    opacity: 0.5;
  }

  .wrapper-card-stats {
    width: 50%;
    max-width: 50%;

    .card-stats {
      padding: 1rem;
      margin: .55rem;
    }

    .doughnut-dimension {
      // width: 200px; 
      height: 270px;
    }
  }

  @media (max-width: 992px) {
    .wrapper-card-stats {
      width: 100%;
      max-width: 100%;

      .card-stats {
        margin-right: 0;
        margin-left: 0;
      }

    }
  }

</style>
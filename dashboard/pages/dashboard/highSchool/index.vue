<template>
  <v-app class="no-style">
    <div>
      <h2 class="mt-2 mb-4">Statistiques d'usage - Lycée</h2>
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
      <div class="mb-4">
        <div class="row">
          <div class="col-lg-3 col-sm-6" v-for="stats in statsCards" :key="stats.title">
            <stats-card :hasIcon="true">
              <div class="icon-big text-center" :class="`icon-${stats.type}`" slot="header">
                <i :class="stats.icon"></i>
              </div>
              <div class="numbers" slot="content">
                <p>{{stats.title}}</p>
                {{stats.value}}
              </div>
            </stats-card>
            <stats-card v-if="stats.children" v-for="child in stats.children" :key="child.title" :hasIcon="false">
              <div slot="header">
                <p>{{child.title}}</p>
              </div>
              <div class="numbers" slot="content">
                {{child.value}}
              </div>
            </stats-card>
          </div>
        </div>
      </div>
      <div class="stats-container card">
        <h3 class="text-center font-weight-bold">Statistique de parcours</h3>
        <hr/>
        <v-container grid-list-md>
          <v-layout wrap>
            <v-flex xs12>
              <progress-bar v-if="loadSteps" :userSteps="userSteps" />
            </v-flex>
          </v-layout>
        </v-container>
      </div>
      <div class="stats-container card">
        <h3 class="text-center font-weight-bold">Domaines sélectionnés par les élèves</h3>
        <hr/>
        <v-container grid-list-md>
          <v-layout wrap>
            <v-flex v-for="domain in displayedDomains" :key="domain.id" xs12 sm4>
              <v-layout wrap>
                <v-flex xs5>
                  <img :src="domain.img_path" class="domain-img">
                </v-flex>
                <v-flex xs7>
                  <span class="domain-number">{{ getDomainPercentage(domain.nbOccurrences) }}%</span>
                  <p class="domain-text">
                    {{ domain.name }}
                  </p>
                </v-flex>
              </v-layout>
            </v-flex>
          </v-layout>
        </v-container>
        <p class="text-right w-full">
          <a v-if="!moreDomains" class="cursor-pointer" @click="moreDomains = true">+ Voir plus de domaines</a>
          <a v-else class="cursor-pointer" @click="moreDomains = false">- Voir moins de domaines</a>
        </p>
      </div>
      <div class="stats-container card">
        <h3 class="text-center font-weight-bold">Filières sélectionnées par les élèves</h3>
        <hr/>
        <v-container grid-list-md>
          <v-layout wrap>
            <v-flex v-for="stream in streams" :key="stream.id" xs12 sm4>
              <v-layout wrap>
                <v-flex xs5>
                  <img :src="stream.img_path" class="domain-img">
                </v-flex>
                <v-flex xs7>
                  <span class="domain-number">{{ getStreamPercentage(stream.nbOccurrences) }}%</span>
                  <p class="domain-text">
                    {{ stream.name }}
                  </p>
                </v-flex>
              </v-layout>
            </v-flex>
          </v-layout>
        </v-container>
      </div>
    </div>
  </v-app>
</template>

<script>

  import StatsCard from '~/components/UIComponents/Cards/StatsCard.vue'
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import TemplateItem from '~/components/autocomplete/NameItemTemplate'
  import ProgressBar from '~/components/UIComponents/ProgressBar'
  import { SCHOOL_KINDS } from '~/config'
  import _ from 'lodash'

  export default {
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    components: {ProgressBar, StatsCard, TemplateItem },
    async asyncData({app}) {
      try {
        let response = await app.$api.domains.getList({page: 1, numberPerPage: 1000, kind: 'activity-area'})
        const domains = response.data || []

        response = await app.$api.domains.getList({page: 1, numberPerPage: 1000, kind: 'stream'})
        const streams = response.data || []

        response = await app.$api.schools.getList({page: 1, numberPerPage: 10000, kinds: 'high-school'})
        const schools = response.data || []

        return { domains, streams, schools }

      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        statsCards: [],
        filters: {},
        data: {},
        schools: [],
        domains: [] ,
        streams: [],
        totalDomainsOccurrences: 0,
        totalStreamsOccurrences: 0,
        userSteps: {},
        moreDomains: false,
        loadSteps: false,
        school_kinds: SCHOOL_KINDS,
        nameItemTemplate: TemplateItem
      }
    },
    computed: {
      displayedDomains() {
        return this.moreDomains ? this.domains : this.domains.slice(0, 6)
      },
    },
    created() {
      this.getData()
    },
    methods: {
      async getData() {
        try {
          this.userSteps = {}
          this.loadSteps = false

          let request = await this.$api.data.getData(this.filters)
          this.data = request.data || {}

          this.statsCards = [
            {
              type: 'success',
              icon: 'ti-user',
              title: 'Élèves',
              value: this.data.admin.nbStudents,
              children: [
                { title: 'Lycée général', value: this.data.admin.schoolKindsStudentOccurrences.highSchool },
                { title: 'Lycée pro', value: this.data.admin.schoolKindsStudentOccurrences.highSchoolPro },
                { title: 'Collège', value: this.data.admin.schoolKindsStudentOccurrences.middleSchool },
                { title: 'IME', value: this.data.admin.schoolKindsStudentOccurrences.ime },
                { title: 'ESAT', value: this.data.admin.schoolKindsStudentOccurrences.esat }
              ]
            },
            {
              type: 'warning',
              icon: 'ti-user',
              title: 'Parents',
              value: this.data.admin.nbParents,
            },
            {
              type: 'danger',
              icon: 'ti-user',
              title: 'Mentors',
              value: this.data.admin.nbParents,
            },
            {
              type: 'info',
              icon: 'ti-user',
              title: 'Professeurs',
              value: this.data.admin.nbTeachers,
            }
          ]

          this.domains.forEach((domain) => {
            domain.nbOccurrences = _.find(_.get(this.data, 'highSchool.domainsOccurrences', _.get(this.data, 'middleSchool.domainsOccurrences', [])), (value, key) => {
              if (key === domain.id) return value
            })
            if (!domain.nbOccurrences) domain.nbOccurrences = 0
          })
          this.domains = _.orderBy(this.domains, ['nbOccurrences'], ['desc'])
          this.totalDomainsOccurrences = _.sumBy(['nbOccurrences'], _.partial(_.sumBy, this.domains))

          this.streams.forEach((stream) => {
            stream.nbOccurrences = _.find(_.get(this.data, 'highSchool.streamsOccurrences', _.get(this.data, 'middleSchool.streamsOccurrences', [])), (value, key) => {
              if (key === stream.id) return value
            })
            if (!stream.nbOccurrences) stream.nbOccurrences = 0
          })
          this.streams = _.orderBy(this.streams, ['nbOccurrences'], ['desc'])
          this.totalStreamsOccurrences = _.sumBy(['nbOccurrences'], _.partial(_.sumBy, this.streams))

          for (const key in this.data.admin.studentStepsOccurrences) {
            switch (key) {
              case '0':
              case '1':
              case '2':
                this.userSteps['0'] = this.data.admin.studentStepsOccurrences[key]
                break
              case '3':
              case '4':
                this.userSteps['1'] = this.data.admin.studentStepsOccurrences[key]
                break
              case '5':
                this.userSteps['2'] = this.data.admin.studentStepsOccurrences[key]
                break
              case '6':
              case '7':
                this.userSteps['4'] = this.data.admin.studentStepsOccurrences[key]
                break
              case '8':
                this.userSteps['5'] = this.data.admin.studentStepsOccurrences[key]
                break
            }
          }
          this.loadSteps = true

        } catch (e) {
          console.log(e)
        }
      },

      getDomainPercentage(nbOccurrences) {
        return nbOccurrences ? Math.round(nbOccurrences / this.totalDomainsOccurrences * 100) : 0
      },

      getStreamPercentage(nbOccurrences) {
        return nbOccurrences ? Math.round(nbOccurrences / this.totalStreamsOccurrences * 100) : 0
      },
    }
  }

</script>


<style lang="scss">

  .domain-number {
    color: $main-color;
    font-size: 40px;
    font-weight: bold;
    width: 100%;
    display: block;
  }

  .stats-container {
    padding: 15px;
  }

  .v-input--selection-controls:not(.v-input--hide-details) .v-input__slot {
    margin-bottom: 0;
  }

</style>
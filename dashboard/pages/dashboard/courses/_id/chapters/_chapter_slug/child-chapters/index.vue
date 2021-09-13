<template>
  <div>
    <nuxt-link :to="'/dashboard/courses/' + course.id + '/chapters'" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux chapitres</nuxt-link>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Sous Chapitres - <span class="chapter-name">{{chapter.name}}/{{course.name}}</span></v-toolbar-title>
        <v-divider
          class="mx-2"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-btn v-if="!showVisibility" color="primary" dark class="mb-2" @click="showVisibility = true">Modifier</v-btn>
      </v-toolbar>
      <v-data-table
        :headers="showVisibility ? headersUpdate : headers"
        :items="childChapters"
        hide-actions
        class="elevation-1"
      >
        <v-progress-linear v-slot:progress color="blue" indeterminate></v-progress-linear>
        <template v-slot:items="props">
          <td :class="{'disable' : props.item.name !== 'Connaissance de soi' || chapter.slug !== 'activity-area'}">
            <nuxt-link :to="'/dashboard/courses/' + course.id + '/chapters/' + chapter.slug + '/child-chapters/' + props.item.slug">{{ props.item.name }}</nuxt-link>
          </td>
          <td v-if="!showVisibility" class="second-col">
            <i v-if="props.item.display" class="fa fa-check fa-highlight"></i>
            <i v-if="!props.item.display" class="fa fa-times fa-highlight"></i>
            <div v-if="!props.item.display" class="admin-display-actions pull-right disabled">
              <i v-if="props.item.adminDisplay" class="fa fa-eye"></i>
              <i v-if="!props.item.adminDisplay" class="fa fa-eye-slash"></i>
            </div>
          </td>
          <td v-else class="second-col">
            <v-layout wrap>
              <v-flex xs12>
                <v-switch class="d-inline-block" depressed v-model="props.item.display"></v-switch>
                <div v-if="!props.item.display" class="admin-display-actions d-inline-block">
                  <i v-if="props.item.adminDisplay" class="fa fa-eye" @click="props.item.adminDisplay = false"></i>
                  <i v-if="!props.item.adminDisplay" class="fa fa-eye-slash" @click="props.item.adminDisplay = false"></i>
                </div>
              </v-flex>
            </v-layout>
          </td>
        </template>
      </v-data-table>
      <v-layout wrap>
        <v-flex xs12 class="text-xs-right mt-2">
          <v-btn v-if="showVisibility" color="gray" dark @click="showVisibility = false"><i class="fa fa-undo"></i> Annuler</v-btn>
          <v-btn v-if="showVisibility" color="black" dark @click="save()"><i class="fa fa-save"></i> Enregistrer</v-btn>
        </v-flex>
      </v-layout>
      <template>
        <v-layout class="mt-5 d-flex wrap align-content-start">
          <div v-for="domain in domains" :key="domain.id" class="domain-list">
            <DomainCard :domain="domain" @saveDomain="saveDomain" class="domain-card"></DomainCard>
          </div>
        </v-layout>
      </template>
    </v-app>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import _ from 'lodash'
  import DomainCard from "../../../../../../../components/domainCard/DomainCard";

  export default {
    name: 'DashboardCourseChapterChildChapters',
    components: {DomainCard},
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    async asyncData({ app, route }) {
      try {
        let request = await app.$api.courses.getByIds([route.params.id])
        const course = request.data[0] || {}
        const chapter = _.find(_.get(course, 'chapters', []), {slug: route.params.chapter_slug})
        const childChapters = _.get(chapter, 'children', [])

        let response = await app.$api.domains.getList({page: 1, numberPerPage: 1000, kind: route.params.chapter_slug })
        const domains = response.data || []
        domains.forEach(function (domain) {
          domain.isSelected = _.includes(chapter.domain_ids, domain.id)
        })
        return  { course, chapter, childChapters, domains }
      } catch (e) {
        console.log(e)
      }
    },
    data () {
      return {
        domains: [],
        selectedDomains: [],
        items: [],
        headers: [
          {
            text: 'Nom',
            align: 'left',
            sortable: false,
            value: 'text'
          },
          {
            text: 'Activé',
            align: 'left',
            sortable: false,
            value: 'text'
          }
        ],
        headersUpdate: [
          {
            text: 'Nom',
            align: 'left',
            sortable: false,
            value: 'text'
          },
          { text: 'Actions', sortable: false }
        ],
        showVisibility: false
      }
    },
    methods: {
      async save() {
        await this.$api.courses.update(this.course)
        this.showVisibility = false
      },
      async saveDomain() {

        this.chapter.domain_ids = _.map(_.filter(this.domains, {isSelected: true}), 'id')
        await this.$api.courses.update(this.course)
      }
    },
    components: {
      DomainCard,
    }
  }
</script>

<style lang="scss">
  .disable {
    pointer-events: none;
  }
  .v-input--selection-controls__input {
    margin-top: 50%;
  }

  .fa-highlight {
    font-size: 22px;
  }

  .admin-display-actions {
    font-size: 22px;
    cursor: pointer;
    margin-left: 20px;

    &.disabled {
      color: grey;
      cursor: default;
    }
  }

  .second-col {
    width: 40%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .chapter-name {
    color: grey;
  }
  .domain-card {
    margin-bottom: 2px;
  }
  .domain-list {
    max-width: 500px;
  }
</style>

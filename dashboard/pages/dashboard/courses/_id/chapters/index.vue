<template>
  <div>
    <nuxt-link to="/dashboard/courses" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux parcours</nuxt-link>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Chapitres - <span class="course-name">{{ course.name }}</span></v-toolbar-title>
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
        :items="chapters"
        hide-actions
        class="elevation-1"
      >
        <v-progress-linear v-slot:progress color="blue" indeterminate></v-progress-linear>
        <template v-slot:items="props">
          <td><nuxt-link :to="'/dashboard/courses/' + course.id + '/chapters/' + props.item.slug + '/child-chapters'">{{ props.item.name }}</nuxt-link></td>
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
        </v-flex>-
      </v-layout>
    </v-app>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import _ from 'lodash'
  import { COURSE_DEFAULT_CHAPTERS } from '~/config'

  export default {
    name: 'DashboardCourseChapters',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',
    async asyncData({ app, route }) {
      try {
        let request = await app.$api.courses.getByIds([route.params.id])
        const course = request.data[0] || {}
        const chapters = _.get(course, 'chapters', [])

        return  { course, chapters }
      } catch (e) {
        console.log(e)
      }
    },
    data () {
      return {
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
      }
    }
  }
</script>

<style lang="scss">
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
  .course-name {
    color: grey;
  }
</style>

<template>
  <div>
    <nuxt-link :to="'/dashboard/courses/' + course.id + '/chapters/' + chapter.slug + '/child-chapters/'" class="btn btn-default btn-back"><i class="fa fa-arrow-left"></i> Retour aux sous chapitres</nuxt-link>
    <v-app>
      <v-toolbar flat color="black">
        <v-toolbar-title class="numericite-toolbar-title">Activités - <span class="chapter-name"> {{childChapter.name}}/{{chapter.name}}</span> </v-toolbar-title>
        <v-divider
          class="mx-2"
          inset
          vertical
        ></v-divider>
      </v-toolbar>
      <template>
        <v-container fluid class="container pt-0">
          <v-layout row wrap justify-center fill-height>
            <v-flex wrap xs12 md6 class="first-col slide-bg" :class="{'no-bg' : selectedActivities.length > 0}">
              <draggable
                class="dragArea list-group"
                :list="selectedActivities"
                group="activity"
                @end="onEnd"
              >
                <div class="list-group-item" v-for="(activity, index) in selectedActivities" :key="index">
                  <activityCard :versions="versions" :showVersion = true :activity="activity"/>
                </div>
              </draggable>
            </v-flex>
            <v-flex wrap fill-height xs12 md6 class="second-column-activity pa-3 d-flex">
              <draggable class=" dragArea d-flex" :list="activities" group="activity">
                <transition-group>
                  <div v-for="activity in activities" :key="activity.name" class="list-group-item">
                    <activityCard :activity="activity"  class="activityList"/>
                  </div>
                </transition-group>
              </draggable>
            </v-flex>
          </v-layout>
        </v-container>
      </template>
      <v-layout wrap>
        <v-flex xs12 class="text-xs-right mt-2">
          <v-btn color="grey" dark @click="save()">Enregistrer</v-btn>
        </v-flex>
      </v-layout>
    </v-app>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import _ from 'lodash'
  import draggable from 'vuedraggable'
  import ActivityCard from '~/components/activityCard/ActivityCard'
  export default {
    display: "Two Lists",
    order: 1,
    name: 'DashboardCourseChapterChildChapters',
    layout: "dashboard",
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessDashboard',

    async asyncData({ app, route }) {
      try {
        let request = await app.$api.courses.getByIds([route.params.id])
        const course = request.data[0] || {}
        const chapter = _.find(_.get(course, 'chapters', []), {slug: route.params.chapter_slug})
        const childChapter = _.find(_.get(chapter, 'children', []), {slug: route.params.child_chapter_slug})

        request = await app.$api.activities.getList()
        let activities = request.data || {}

        request = await app.$api.activities.getVersions({page: 1, numberPerPage: 1000})
        const versions = request.data || []

        const selectedActivities = _.orderBy(_.filter(_.map(activities, activity => {
          if(_.includes(_.map(childChapter.activities, 'id'), activity.id)) {
            activity.version_id = _.get(_.find(childChapter.activities, {id: activity.id}), 'version_id', '')
            activity.index = _.map(childChapter.activities, 'id').indexOf(activity.id)
            return activity
          }
        }), function (activity) {
          return !!activity
        }), 'index', 'asc')
        activities = _.filter(activities, activity => {
          return !(_.includes(selectedActivities, activity))
        })

        return  { course, chapter, childChapter, activities, versions, selectedActivities }
      } catch (e) {
        console.log(e)
      }
    },
    data () {
      return {
        showVisibility: false,
        selectedActivities: [],
        activities: [],
      }
    },
    created() {
    },
    methods: {
      async save() {
        this.childChapter.activities = _.map(this.selectedActivities, function (activity) {
            return {
              id : activity.id,
              version_id: activity.version_id
            }
        })
        console.log(this.childChapter.activities)
        await this.$api.courses.update(this.course)
        this.showVisibility = false
      },
      onEnd() {

        },
    },
    components: {
      draggable,
      ActivityCard,
    }
  }
</script>

<style lang="scss">
  .container {
    padding: 15px;
    .second-column-activity .d-flex span {
      display: flex !important;
      flex-wrap: wrap;
      max-height: 70px !important;
    }
    .list-group-item {
      background-color: transparent !important;
      border: none !important;
      padding-top: 0 !important;
      padding-bottom: 0 !important;
      margin: 0 !important;

    }
  }
  .second-column-activity {
    background-color: lightgray !important;
    box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.15);
    min-height: 100vh;
    overflow: auto;
  }
  .first-col {
    background-color: white !important;
    box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.15);
    position: relative;
  }
  .slide-bg {
    background-image: url("/img/slide_activities_transparent.png");
    background-size: 40%;
    background-position: 50% 10%;
  }
  .chapter-name {
    color: grey;
  }
  .activityArea {
    border: 2px solid grey;
    border-style: dashed solid;
    margin: 10px;
    position: relative;
  }
  .slide-text {
    color: gray;
    font-weight: bold;
    padding: 10px;
  }
  .activityList {
    width: 100%;
  }
  .dragArea { min-height: 100vh; }
  .no-bg {
    background: none;
  }
</style>

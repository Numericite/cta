<template>
  <div>
    <h2 class="title title--dash pt-16 sm:text-4xl sm:pt-12">{{ title }}</h2>

    <div class="flex mt-6 sm:overflow-x-scroll scroll--mobile sm:py-5 sm:mt-1">
      <score :completedSteps="nbActivityDone"
             :totalSteps="nbActivities"
             :desc="'Activités <br> terminées'"
             :color="'#ff9c9c'"
             :innerColor="'#FFEBEB'"
             class="w-1/3 ml-6 sm:w-2/5" />
      <score :completedSteps="nbDomains"
             :totalSteps="nbDomains"
             :desc="'Domaines <br> débloqués'"
             :color="'#1dd4b6'"
             :innerColor="'#D2F6F0'"
             class="w-1/3 ml-6 sm:w-2/5" />
      <score :completedSteps="nbStreams"
             :totalSteps="nbStreams"
             :desc="'Filières <br> débloqués'"
             :color="'#ffc600'"
             :innerColor="'#FFF4CC'"
             class="w-1/3 ml-6 sm:w-2/5" />
    </div>
  </div>
</template>

<script>
import Score from '~/components/dashboard/Score'
import _ from 'lodash'

export default {
  name: 'Scores',
  components: { Score },
  props: {
    nbActivityDone: {
      type: Number,
      default: function() {
        return 0
      }
    },
    nbDomains: {
      type: Number,
      default: function() {
        return 0
      }
    },
    nbStreams: {
      type: Number,
      default: function() {
        return 0
      }
    },
    title: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      nbActivities: 0
    }
  },
  created() {
    const chapters = this.$store.state.auth.user.course.chapters
    let domain = _.find(chapters, { slug: 'activity-area' })
    this.nbActivities = _.get(_.find(domain.children, { slug: 'introspection' }), 'activity_ids.length', 0)
  }
}
</script>

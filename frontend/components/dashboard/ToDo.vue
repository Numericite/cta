<template>
  <div class="ToDo">
    <h2 class="title title--dash pt-16 sm:text-4xl">{{ title }}</h2>

    <div class="flex mt-6 sm:flex-col">
      <activity-card v-for="(activity, key) in activities" :key="key" :activity="activity"
                     class="w-full mr-6 sm:mt-4 sm:w-full" />
    </div>

  </div>
</template>

<script>
import Card from '~/components/dashboard/ToDo-card'
import ActivityCard from './introspection/ActivityCard'

export default {
  name: 'ToDo',
  components: { ActivityCard, Card },
  props: {
    activities: {
      type: Array,
      default: function() {
        return []
      }
    },
    title: {
      type: String,
      default: ''
    }
  }
}
</script>

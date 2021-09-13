<template>
  <div>
    <h2 class="title title--dash pt-16 sm:text-4xl sm:pt-12">{{ title }}</h2>

    <div class="mt-6">
      <history-single
        v-for="(historyLine, key) in userHistory"
        :key="key"
        :historyLine="historyLine"
        class="mb-4" />
    </div>
  </div>
</template>

<script>
import HistorySingle from '~/components/dashboard/HistorySingle'

export default {
  name: 'History',
  components: { HistorySingle },
  props: {
    userHistory: {
      type: Array,
      default: function() {
        return []
      }
    },
    title: {
      type: String,
      default: ''
    }
  },
  data() {
    return {}
  }
}
</script>

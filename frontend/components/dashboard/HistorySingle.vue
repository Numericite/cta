<template>
  <div class="activity bg-white w-full p-10 flex justify-between sm:p-4 sm:flex-col sm:relative">
    <div class="flex sm:items-center sm:pb-8">
      <img src="~/static/icons/activity.svg"
           alt="illustration d'une activité"
           class="sm:h-10 sm:w-10">
      <div class="ml-16 flex flex-col justify-center sm:ml-4 sm:pl-4 sm:border-solid sm:border-blue-lightest sm:border-l-2">
        <h4 class="text-blue font-extrabold text-3xl mb-4 sm:text-xl">{{ historyLine.activity_name }}</h4>
        <h4 class="text-orange text-extrabold text-2xl">Terminée</h4>
      </div>
    </div>
    <div class="flex items-center sm:mt-3 sm:absolute sm:pin-b sm:mb-3">
      <span class="text-blue-lighter mr-3 font-semibold sm:text-xs sm:ml-16 sm:pl-3">{{ formatDate(historyLine.created_date) }} {{ formatTime(historyLine.created_date) }}</span>
      <img src="~/static/icons/clock.svg"
           alt="horloge">
    </div>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    name: 'HistorySingle',
    props: {
      historyLine: {
        type: Object,
        default: function() {
          return {}
        }
      }
    },
    methods: {
      formatDate: function(date) {
        return moment(date).format('D MMMM YYYY')
      },
      formatTime: function(date) {
        return moment(date).format('HH:mm')
      }
    }
  }
</script>

<style lang="scss" scoped>
.activity {
  border-radius: 12px;
  box-shadow: 0 2px 15px 0 rgba(40, 21, 134, 0.09);
}
</style>

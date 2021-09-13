<template>
  <div>
    <div class="datepicker-trigger p-4 rounded-xlg items-center bg-blue-lightest flex">
      <div :class="{'bg-green': deadline > 7, 'bg-orange': (deadline && deadline < 7 && deadline >= 0), 'bg-red': deadline < 0, 'bg-white': !deadline}" class="w-4 h-4 rounded-full flex"/>
      <input
        :id="'datepicker-trigger-' + pickerIndex"
        :disabled="isDisabled"
        :value="formatDates(date)"
        class="bg-blue-lightest w-5/6 ml-2 text-blue flex cursor-pointer focus:outline-none"
        type="text"
        placeholder="Date du rendez-vous"
      >
      <img src="~/assets/img/calendar.svg" class="flex">

      <AirbnbStyleDatepicker
        v-if="!isDisabled"
        :triggerElementId="'datepicker-trigger-' + pickerIndex"
        :mode="'single'"
        :fullscreenMobile="true"
        :dateOne="date"
        @date-one-selected="val => { date = val }"
      />
    </div>
  </div>
</template>

<script>
  import format from 'date-fns/format'

  export default {
    name: 'DatePicker',
    props: {
      pickerIndex: {
        type: String,
        default: function() {
          return '0'
        }
      },
      value: {
        type: Number,
        default: function() {
          return null
        }
      },
      isDisabled: {
        type: Boolean,
        default: function() {
          return false
        }
      }
    },
    data() {
      return {
        dateFormat: 'D / MM / YYYY',
        date: '',
        deadline: null
      }
    },
    created() {
      if (this.value) {
        this.date = new Date(this.value)
      }
    },
    methods: {
      formatDates(date) {
        let formattedDates = ''
        if (date) {
          this.setDeadline(date)
          formattedDates = format(date, this.dateFormat)
          this.$emit('dateChanged', date)
        }
        return formattedDates
      },
      setDeadline(date) {
        const tmpDate = new Date(date)
        const today = new Date()
        this.deadline = this.daysDiff(today, tmpDate)
      },
      daysDiff(first, second) {
        return Math.round((second-first)/(1000*60*60*24));
      }
    }
  }
</script>

<style lang="scss">
  .asd__wrapper--datepicker-open {
    width: 300px !important;
  }
</style>

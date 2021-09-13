<template>
  <div class="activity__right relative min-h-screen bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:pl-16 px-6 xl:pr-32 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Environnements Favorables</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4 sm:text-3xl">6.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal xl:mt-4 sm:text-xl">
        Dis-nous comment tu es habillé(e) ?
      </h1>
    </div>
    <!-- End Title -->
    <div class="flex flex-wrap">
      <div v-for="(choice, key) in choices" :key="key" class="p-4 w-1/2 sm:w-full">
        <div :class="selected == (key + 1) ? 'border-2 border-orange' : selected != 0 ? 'border-blue-grayish border disabled' : 'border-blue-grayish border'"
             class="relative card structure border-solid cursor-pointer flex flex-wrap rounded-xlg py-8 overflow-hidden"
             @click="selected = selected == (key + 1) ? 0 : (key + 1)">
          <div class="icon text-orange w-full h-1/2 mb-4 flex items-center justify-center">
            <picture>
              <img :src="choice.img_path">
            </picture>
          </div>
          <div class="desc justify-center flex flex-wrap w-full text-center">
            <div class="title text-blue font-semibold text-2xl w-full justify-center">{{ choice.text }}</div>
            <div class="subtitle text-blue-lighter font-semibold text-xl mt-1 w-full justify-center">{{ choice.description }}</div>
          </div>
          <div v-if="selected != (key + 1) && selected != 0"
               class="opacity-60 absolute pin-t pin-l w-full h-full bg-blue-lightest" />
        </div>
      </div>
    </div>
    <button v-scroll-to="'#actTitle'"
            :disabled="!selected"
            class="button button--blue mt-8 self-start ml-6 mt-3 sm:w-full sm:ml-0"
            @click="nextPage">
      Je valide
    </button>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'EnvironnementsFavorables',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    actions: {
      type: Array,
      default: function() {
        return null
      }
    },
    activityLogs: {
      type: Object,
      default: function() {
        return {}
      }
    }
  },
  data() {
    return {
      selected: false,
      choices: []
    }
  },
  watch: {
    selected(value) {
      if (value)
        this.$emit( 'setStep', 7)
      else
        this.$emit( 'setStep', 6)
    }
  },
  created() {
    this.choices = this.actions.filter(function(action) {
      return action.selection_id === '8d3fda73-17fe-40da-93fa-8448ff5e6c71'
    })

    if (this.activityLogs.choice_ids) {
      const choice_ids = this.activityLogs.choice_ids
      var selected_index = 0
      this.choices.forEach(function (choice, index) {
        choice.value = choice_ids.indexOf(choice.id) !== -1

        if (choice.value) {
          selected_index = index + 1
        }
      })
      this.selected = selected_index
    }
  },
  mounted() {
    this.$emit( 'setStep', 6 )
  },
  methods: {
    nextPage() {
      this.choices[(this.selected - 1)].value = true
      this.$router.push( '/dashboard/student/domaines/introspection/environnements-favorables/etape-7' )
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .structure {
    transition: all 0.2s;
    .selected {
    }
    &:not(.disabled):hover {
      box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
    }
  }

  .cursor-grab {
    cursor: grab;
  }
}
</style>

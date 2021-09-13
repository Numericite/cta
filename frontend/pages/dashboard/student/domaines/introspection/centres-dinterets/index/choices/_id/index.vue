<template>
  <div class="action__right bg-blue-lightest xl:min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center h-full">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes centres d'intérêts</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4 ml-8">
        <span v-html="texts[0]" /> <span class="uppercase">{{ selection.text }}</span>.
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex flex-wrap items-end  sm:h-auto my-4">
      <div v-for="(action, key) in actions"
           :key="key"
           :class="action.value ? 'border-yellow' : 'border-transparent'"
           class="bg-white border-solid border-3 cardShadow m-3 text-sm pl-6 pr-2 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center action cursor-pointer action-label"
           @click="action.value = !action.value">
        {{ action.text }}
        <div class="heart ml-3">
          <svg :class="action.value ? 'opacity-45 text-yellow' : 'text-blue-lighter'"
               xmlns="http://www.w3.org/2000/svg"
               width="11"
               height="10"
               viewBox="0 0 11 10"
               class="mr-2 h-6 w-6 fill-current">
            <path d="M5.9 1.514C6.449.556 7.093.01 8.258.01a2.594 2.594 0 0 1 2.593 2.593c0 2.62-2.893 5.347-4.784 7.239a.237.237 0 0 1-.334 0C3.842 7.95.95 5.222.95 2.602.95.33 3.6-.686 5.09.505c.309.247.567.584.811 1.01z" />
          </svg>
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            class="button button--blue mt-8 self-start xs:justify-center"
            @click="goBack">
      Je valide
    </button>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'IntelligencesMultiples',
  layout: 'action',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    results: {
      type: Array,
      default: function() {
        return null
      }
    }
  },
  data() {
    return {
      actions: [],
      selection: {},
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    if (!this.results)
      this.$router.push('../../centres-dinterets')

    this.$emit( 'setStep', 5 )
    const selection_id = this.$route.params.id
    this.selection = this.results.find(function(result) {
      return result.id === selection_id
    })
    this.actions = this.selection.actions || []
  },
  methods: {
    goBack() {
      this.$router.push( '../etape-2' )
    }
  }
}
</script>

<style lang="scss" scoped>
.action__right {

  @screen xl {
    width: 70vw;
  }

  @screen xxl {
    width: 65vw;
  }

  .card-head {
    height: 103px;
  }

  .action-label {
    max-width: 90vw;
  }

  .button.action {
    transform: scale(0.8);
  }

  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }
  .heart {
    transform: translateY(2px);
  }
}
</style>

<template>
  <div class="activity__right bg-blue-lightest min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes traits de personnalités</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">2.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="questionCard card rounded-lg mt-12 w-full xl:mx-6 mx-2 xl:p-8 p-4">
      <div class="flex flex-wrap -mx-1  lg:-mx-1">
        <template v-for="(personnalite, key) in results">
          <div :key="key"
               class="w-1/3 my-2 px-2  flex lg:my-1 lg:px-1 lg:w-1/3 relative cursor-pointer border-b-2 border-blue-lightest border-solid"
               @click="personnalite.value = (!allChecked ? !personnalite.value : false)">
            <div :class="!personnalite.value && allChecked ? 'allChecked' : ''"
                 class="checkbox checkbox-static w-4 h-6 xl:mr-6 mr-3"
                 checked>
              <!-- {{ key + (personnalitesStep - 1) * 27 }} -->
              <input :id="key"
                     checked
                     disabled
                     class="fake-disabled"
                     name="donnees"
                     type="checkbox">
              <label :for="key" />
            </div>
            <div class="">
              <p :class="!personnalite.value && allChecked ? 'text-blue-lighter' : 'text-blue'"
                 class="xl:text-lg text-base pt-4 sm:mb-2">
                {{ personnalite.text }}
              </p>
              <svg v-if="personnalite.value"
                   xmlns="http://www.w3.org/2000/svg"
                   width="106"
                   height="38"
                   viewBox="0 0 106 38"
                   class="select-pers fill-current text-topaze absolute ">
                <path fill-rule="evenodd"
                      d="M88.306 3.217C71.356.13 53.984-1.031 36.841 1.05c-8.386 1.018-16.705 2.771-24.726 5.46C7.203 8.154.685 11.152.05 17.088c-.685 6.405 5.754 10.26 10.745 12.412 8.057 3.474 16.789 5.053 25.419 6.288A212.272 212.272 0 0 0 62.9 37.912c8.56.137 17.38.086 25.832-1.446 6.107-1.107 17.03-3.805 16.723-11.949-.305-8.151-8.844-11.106-15.355-12.8-1.007-.26-1.436 1.31-.43 1.571 5.834 1.517 16.167 4.972 13.972 13.208-1.018 3.818-5.552 5.602-8.864 6.746-3.56 1.229-7.287 1.893-11.02 2.294-14.787 1.59-30.029.93-44.75-.994-7.43-.971-14.936-2.19-22.093-4.485C11.472 28.311.33 24.575 1.702 16.842c1.135-6.397 9.932-8.586 15.088-10.086 7.19-2.092 14.57-3.505 22.01-4.3C55.168.703 71.714 1.843 87.878 4.786c1.016.185 1.452-1.384.43-1.57" />
              </svg>
            </div>
          </div>
        </template>

      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            :disabled="!allChecked"
            class="button button--blue mt-8 self-start mt-3"
            @click="nextStep">
      Je Valide
    </button>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'IntelligencesMultiples',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    results: {
      type: Array,
      default: function() {
        return { results: null }
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
      texts: this.getPageTexts(this.$route.name)
    }
  },
  computed: {
    allChecked() {
      let i = 0;

      this.results.forEach( element => {
        i += element.value ? 1 : 0
      } );
      if ( i > 7 )
        this.$emit( 'setStep', 4 )
      else
        this.$emit( 'setStep', 3 )
      return i > 7
    }
  },
  mounted() {
    if ( !this.results || this.results.length === 0)
      this.$router.push( '../traits-de-personnalite' )

    if (this.activityLogs.choice_ids) {
      const choice_ids = this.activityLogs.choice_ids
      this.results.forEach(function(result) {
        result.value = choice_ids.indexOf(result.id) !== -1
      })
    }

    this.$emit( 'setStep', 3 )
  },
  methods: {
    nextStep: function() {
      this.activityLogs.choice_ids = this.results.filter(function(result) {
        return result.value
      }).map(function (result) {
        return result.id
      })
      this.$emit( 'setActivityLogs', this.activityLogs )

      this.$emit( 'setEndResults', this.cat )

      this.$emit('createLogs', () => {
        this.$router.push( 'etape-3' )
      })
    }
  },
}
</script>

<style lang="scss" scoped>
.activity__right {
  .intelligence {
    &:nth-child(1) {
      .intelligence__header {
        @apply bg-yellow;
      }
    }
    &:nth-child(2) {
      .intelligence__header {
        @apply bg-peach;
      }
    }
    &:nth-child(3) {
      .intelligence__header {
        @apply bg-topaze;
      }
    }
    h4 {
      font-size: 2.5rem;
    }
    .content {
      span {
        @apply flex items-center font-bold uppercase text-xs mr-2 mb-2 border-yellow border-solid border rounded-full p-2 cursor-pointer;
      }
    }
  }
  .card {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }
  .select-pers {
    transform: translate(-0.7rem, -3.6rem);
    z-index: 500;
  }
}
</style>

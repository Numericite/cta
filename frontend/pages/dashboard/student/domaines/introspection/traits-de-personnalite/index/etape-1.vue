<template>
  <div class="activity__right relative bg-blue-lightest min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:pl-16 px-6 xl:pr-32 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes traits de personnalités</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4 sm:text-3xl">{{ personnalitesStep }}<span class="text-5xl">/{{ getMaxStep }}</span>.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal xl:mt-4 sm:text-xl">
        {{ texts[0] }}
      </h1>
    </div>
    <!-- End Title -->

    <div class="questionCard card rounded-lg mt-12 w-full xl:mx-6 mx-2 xl:p-8 p-4">

      <div v-if="(getLikesCount < 8) && showError" class="text-center">
        <p class="error">Sélectionne au moins 8 termes!</p>
      </div>
      <div class="flex flex-wrap -mx-1 overflow-hidden lg:-mx-1">
        <template v-for="(personnalite, key) in personnalites.slice(getFirstPersonnalite(), personnalitesStep * 27)">
          <div :key="key"
               :class=" key < 24 ? 'border-b-2 border-blue-lightest border-solid' : ''"
               class="w-1/3 my-2 px-2 overflow-hidden flex lg:my-1 lg:px-1 lg:w-1/3 cursor-pointer"
               @click="changeValue(personnalite, key)">
            <div class="checkbox w-4 h-6 xl:mr-6 mr-3">
              <!-- {{ key + (personnalitesStep - 1) * 27 }} -->
              <input :id="key + (personnalitesStep - 1) * 27"
                     v-model="personnalites[key + (personnalitesStep - 1) * 27].like"
                     name="donnees"
                     type="checkbox"
                     @click="changeValue(personnalite, key)">
              <label :for="key + (personnalitesStep - 1) * 27" />
            </div>
            <div class="">
              <p class="text-blue xl:text-lg text-base sm:mb-2 mt-2">
                {{ personnalite.text }}
              </p>
            </div>
          </div>
        </template>

      </div>
    </div>
    <div class="relative">
      <div class="flex sm:flex-col xl:flex-row-reverse">
        <button v-scroll-to="'#actTitle'"
                v-if="personnalitesStep === getMaxStep"
                class="button button--orange mt-8 self-start ml-6 mt-3 sm:mx-auto"
                @click="nextStep">
          Valider mes réponses
        </button>
      </div>
      <div class="flex flex-row sm:justify-center xl:pl-3 xl:pin-t">
        <button v-scroll-to="'#actTitle'"
                :disabled="personnalitesStep === 1"
                class="button button--blue mt-8 self-start mx-3 mt-3"
                @click="previousPersonnalitesStep">
          Précédent
        </button>
        <button v-scroll-to="'#actTitle'"
                :disabled="personnalitesStep === getMaxStep"
                class="button button--blue mt-8 self-start mx-3 mt-3"
                @click="nextPersonnalitesStep">
          Suivant
        </button>
      </div>
    </div>
    <div class="xl:fixed pin-b pin-r xl:pr-12 pb-12 text-4xl text-blue-lighter font-bold sm:text-right sm:py-8">
      <span class="text-blue text-6xl">{{ personnalitesStep }}</span>
      <span>/</span>
      <span>{{ getMaxStep }}</span>
    </div>

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
    personnalites: {
      type: Array,
      default: function () {
        return { personnalites: null }
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
      personnalitesStep: 1,
      showError: false,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  computed: {
    getLikesCount: function() {
      return this.personnalites.filter(function (personnalite) {
        return personnalite.like
      }).length;
    },
    getMaxStep: function() {
      return Math.trunc(this.personnalites.length / 27.1) + 1
    }
  },
  created() {
    this.$emit( 'setStep', 1 )
    if (this.activityLogs.config.firsts_choices_ids) {
      const firsts_choices_ids = this.activityLogs.config.firsts_choices_ids
      this.personnalites.forEach(function (personnalite) {
        personnalite.like = firsts_choices_ids.indexOf(personnalite.id) !== -1
      });
    }
  },
  methods: {
    previousPersonnalitesStep: function () {
      if ( this.personnalitesStep > 1 ) {
        this.personnalitesStep--;
      }
    },
    nextPersonnalitesStep: function () {
      if ( this.personnalitesStep < this.personnalites.length / 27 ) {
        this.personnalitesStep++
        this.$emit( 'setStep', 2 )
      }
      this.$emit( 'setQuestionStep', this.personnalitesStep )
    },
    nextStep: function () {
      const results = this.personnalites.filter(function (personnalite) {
        return personnalite.like
      });

      if (results.length >= 8) {
        this.activityLogs.config.firsts_choices_ids = results.map(function (result) {
          return result.id
        })
        this.$emit('setActivityLogs', this.activityLogs)

        this.$emit('sendResults', results)
        this.$router.push('etape-2')
      } else {
        this.showError = true;
      }
    },
    getFirstPersonnalite: function () {
      if ( this.personnalitesStep == 1 ) {
        return 0
      } else {
        return ( this.personnalitesStep - 1 ) * 27
      }
    },
    changeValue: function(personnalite, key) {
      personnalite.like = !personnalite.like
      document.getElementById(key + (this.personnalitesStep - 1) * 27).checked = personnalite.like
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .questionCard {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }

  .error {
    color: #cc0000;
  }
}
</style>

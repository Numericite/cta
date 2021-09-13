<template>
  <div class="activity__right min-h-screen bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex competences-center underline">Mes compétences primaires</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">4.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex xl:flex-no-wrap sm:flex-wrap xl:h-3/4 mt-4">
      <div class="xl:w-1/3 sm:w-full xl:h-full flex flex-col flex-none">
        <div class="xl:h-80 sm:w-full p-4">
          <div class="flex flex-col bg-white cardShadow rounded-xlg xl:h-full w-full justify-center">
            <div class="font-bold text-blue xl:text-xl sm:text-xs text-center my-4">
              <span v-html="texts[1]" />
            </div>
            <div class="xl:h-2/3 sm:h-1/5 px-16">
              <img class="xl:max-h-full m-auto block" style="display: block"
                   src="../../../../../../../assets/img/competences/exemple-concret/1.png"
                   alt="">
            </div>
            <div class="flex justify-center">
              <button v-scroll-to="'#actTitle'"
                      class="button text-blue hover:text-white border-blue border-2 px-3 py-3 reduce-size my-2"
                      @click="goBack">
                Je redécouvre
              </button>
            </div>
          </div>
        </div>
        <div v-for="n in 2"
             :key="n"
             class="xl:h-32 sm:w-full p-4">
          <div :class="!competences[n - 1].value && count === 3 ? '' : 'cursor-pointer competences cardShadow'"
               class="bg-white rounded-xlg h-full pt-4 pl-5 relative overflow-hidden"
               @click="selectCard(competences[n - 1])">
            <div class="flex flex-no-wrap relative">
              <div class="text-blue font-bold uppercase text-base leading-tight mr-10">
                {{ competences[n - 1].text }}
              </div>
              <input :id="competences[n - 1].id"
                     v-model="competences[n - 1].value"
                     name="competence"
                     type="checkbox"
                     @click="selectCard(competences[n - 1])">
              <label :for="competences[n - 1].id"
                     class="competence-checkbox-position" />
            </div>
            <div class="text-blue-lighter mt-4 text-xs font-semibold leading-tight mr-8 mb-6">
              {{ competences[n - 1].description }}
            </div>
            <div v-show="!competences[n - 1].value && count === 3"
                 class="bg-white opacity-60 absolute w-full h-full pin-t pin-l" />
          </div>
        </div>
      </div>
      <div class="xl:w-1/3 sm:w-full xl:h-full flex flex-col">
        <div v-for="n in 4"
             :key="n"
             class="xl:h-32 sm:w-full p-4">
          <div :class="!competences[n + 1].value && count === 3 ? '' : 'cursor-pointer competences cardShadow'"
               class="bg-white rounded-xlg h-full pt-4 pl-5 relative overflow-hidden"
               @click="selectCard(competences[n + 1])">
            <div class="flex flex-no-wrap relative">
              <div class="text-blue font-bold uppercase text-base leading-tight mr-10">
                {{ competences[n + 1].text }}
              </div>
              <input :id="competences[n + 1].id"
                     v-model="competences[n + 1].value"
                     name="competence"
                     type="checkbox"
                     @click="selectCard(competences[n + 1])">
              <label :for="competences[n + 1].id"
                     class="competence-checkbox-position" />
            </div>
            <div class="text-blue-lighter mt-4 text-xs font-semibold leading-tight mr-8 mb-6">
              {{ competences[n + 1].description }}
            </div>
            <div v-show="!competences[n + 1].value && count === 3"
                 class="bg-white opacity-60 absolute w-full h-full pin-t pin-l" />
          </div>
        </div>
      </div>
      <div class="xl:w-1/3 sm:w-full xl:h-full flex flex-col">
        <div v-for="n in 4"
             :key="n"
             class="h-32 p-4">
          <div :class="!competences[n + 5].value && count === 3 ? '' : 'cursor-pointer competences cardShadow'"
               class="bg-white rounded-xlg h-full pt-4 pl-5 relative overflow-hidden"
               @click="selectCard(competences[n + 5])">
            <div class="flex flex-no-wrap relative">
              <div class="text-blue font-bold uppercase text-base leading-tight mr-10">
                {{ competences[n + 5].text }}
              </div>
              <input :id="competences[n + 5].id"
                     v-model="competences[n + 5].value"
                     name="competence"
                     type="checkbox"
                     @click="selectCard(competences[n + 5])">
              <label :for="competences[n + 5].id"
                     class="competence-checkbox-position" />
            </div>
            <div class="text-blue-lighter mt-4 text-xs font-semibold leading-tight mr-8 mb-6">
              {{ competences[n + 5].description }}
            </div>
            <div v-show="!competences[n + 5].value && count === 3"
                 class="bg-white opacity-60 absolute w-full h-full pin-t pin-l" />
          </div>
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            :disabled="count !== 3"
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
    passedQuizz: {
      type: Boolean,
      default: function() {
        return false
      }
    },
    competences: {
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
      count: 0,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  created() {
    if (!this.passedQuizz)
      this.$router.push('../competences')

    if (this.activityLogs.selection_ids) {
      const selection_ids = this.activityLogs.selection_ids
      this.competences.forEach(function (competence) {
        competence.value = selection_ids.indexOf(competence.id) !== -1
      })
    }

    this.count = this.competences.filter( function( result ) {
      return result.value;
    } ).length;
  },
  mounted() {
    this.$emit( 'setStep', 1 )
  },
  methods: {
    nextStep: function() {
      const results = this.competences.filter( function( competence ) {
        return competence.value;
      } )
      this.$emit( 'sendResults', results )

      this.activityLogs.selection_ids = results.map(function(result) {
        return result.id
      })
      this.$emit( 'setActivityLogs', this.activityLogs )

      this.$router.push( 'etape-5' )
    },
    goBack() {
      this.$router.push( 'etape-3' )
    },
    selectCard( item ) {
      if ( !item.value && this.count < 3 ) {
        item.value = true
        this.count++
        this.$emit( 'setStep', 1 + this.count )
      } else if ( item.value ) {
        item.value = false
        this.count--
        this.$emit( 'setStep', 1 + this.count )
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.activity__right {
  .competence-checkbox-position {
    position: absolute;
    top: -5px;
    right: 0px;
  }

  .card-head {
    height: 106px;
  }

  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }

  .cursor-grab {
    cursor: grab;
  }

  .reduce-size {
    transform: scale(0.8);
  }

  .competences {
    transition: all 0.3s;
    &:hover {
      box-shadow: 0 10px 25px 5px rgba(14, 7, 51, 0.1);
    }
  }
}
</style>

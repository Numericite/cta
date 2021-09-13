<template>
  <div class="activity__right relative bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:pl-16 px-6 xl:pr-32 flex flex-col justify-center min-h-screen">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes premières rencontres professionnelles</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4 sm:text-3xl">1.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal xl:mt-4 sm:text-xl">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->



    <div class="card rounded-lg mt-12 xl:w-5/6 xl:mx-6 mx-2 xl:p-8 p-4">
      <!-- Affirmations -->
      <div v-for="(affirmation, key) in basicAffirmations"
           :key="affirmation.id">
        <div class="affirmation w-full py-2 flex cursor-pointer"
             @click="changeValue(affirmation, true)">

          <!-- checkbox -->
          <div class="checkbox w-8 h-8 xl:mr-10 mr-3">
            <input :id="affirmation.id"
                   :disabled="(hasNoExperience === 2)"
                   v-model="affirmation.like"
                   name="donnees"
                   type="checkbox"
                   @click="changeValue(affirmation, true)">
            <label :for="affirmation.id" />
          </div>
          <!-- End checkbox -->

          <!-- affirmation -->
          <div class="content flex">
            <span class="font-bold xl:text-4xl text-2xl text-blue xl:mr-8 mr-3 sm:mt-1">{{ affirmation.num }}</span>
            <p :class="{'mb-0': key === (affirmations.length - 2)}" class="text-blue xl:text-lg text-base sm:mb-2">
              {{ affirmation.description }}
            </p>
          </div>
          <!-- End affirmation -->

        </div>
        <hr v-if="key !== (affirmations.length - 2)"
            class="hr xl:mb-4 mb-2">
      </div>

      <!-- End affirmation -->
    </div>

    <div class="card rounded-lg mt-12 xl:w-5/6 xl:mx-6 mx-2 xl:px-8 px-4 py-2">

      <div class="lastAffirmation w-full py-2 flex cursor-pointer"
           @click="changeValue(lastAffirmation, false)">

        <!-- checkbox -->
        <div class="checkbox w-8 h-8 xl:mr-10 mr-3">
          <input :id="lastAffirmation.id"
                 :disabled="(hasNoExperience === 1)"
                 v-model="lastAffirmation.like"
                 name="donnees"
                 type="checkbox"
                 @click="changeValue(lastAffirmation, false)">
          <label :for="lastAffirmation.id" />
        </div>
        <!-- End checkbox -->

        <!-- lastAffirmation -->
        <div class="content flex">
          <span class="font-bold xl:text-4xl text-2xl text-blue xl:mr-8 mr-3 sm:mt-1">{{ lastAffirmation.num }}</span>
          <p class="text-blue xl:text-lg text-base mb-0">
            {{ lastAffirmation.description }}
          </p>
        </div>
        <!-- End lastAffirmation -->

      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            class="button button--blue mt-8 self-start ml-6 mt-3"
            @click="nextPage">
      Je continue
    </button>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import _ from 'lodash'


export default {
  name: 'MesRencontres',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    affirmations: {
      type: Array,
      default: function() {
        return []
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
      hasNoExperience: 0,
      basicAffirmations: [],
      texts: this.getPageTexts(this.$route.name)
    }
  },
  created() {
    this.affirmations.forEach(function(affirmation) {
      affirmation.num = parseInt(affirmation.text)
    })

    this.affirmations = _.orderBy(this.affirmations, ['num'], ['asc'])

    this.basicAffirmations = this.affirmations.slice(0, this.affirmations.length - 1)
    this.lastAffirmation = this.affirmations[this.affirmations.length - 1]
  },
  mounted() {
    this.$emit( 'setStep', 1 )

    if (this.activityLogs.selection_ids) {
      const selection_ids = this.activityLogs.selection_ids
      const affirmations = this.affirmations
      const changeValue = this.changeValue
      this.affirmations.forEach(function(affirmation, index) {
        if (selection_ids.indexOf(affirmation.id) !== -1) {
          changeValue(affirmation, index !== (affirmations.length - 1))
        }
      })
    }
  },
  methods: {
    nextPage() {
      const results = this.affirmations.filter(function(affirmation) {
        return affirmation.like
      })

      this.$emit('sendResults', results)

      this.activityLogs.selection_ids = results.map(function(result) {
        return result.id
      })
      this.$emit('setActivityLogs', this.activityLogs)

      if (results.length === 1 && results[0].id === this.lastAffirmation.id) {
        delete this.activityLogs.config
        this.$emit('setActivityLogs', this.activityLogs)
        this.$router.push( '/dashboard/student/domaines/introspection/rencontres/etape-3' )
      } else {
        this.$router.push( '/dashboard/student/domaines/introspection/rencontres/etape-2' )
      }
    },
    changeValue: function(affirmation, isBasic) {
      if ((isBasic && this.hasNoExperience !== 2) || (!isBasic && this.hasNoExperience !== 1))
        affirmation.like = !affirmation.like

      const basicAffirmations = this.basicAffirmations
      const basicAffirmationsLiked = basicAffirmations.filter(function(affirmation) {
        return affirmation.like
      })
      const noExperienceLiked = !!this.lastAffirmation.like

      if (basicAffirmationsLiked.length > 0) {
        this.hasNoExperience = 1
      } else if (noExperienceLiked) {
        this.hasNoExperience = 2
      } else {
        this.hasNoExperience = 0
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }

  .cursor-grab {
    cursor: grab;
  }
}
</style>

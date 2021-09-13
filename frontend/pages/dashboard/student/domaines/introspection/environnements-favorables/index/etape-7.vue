<template>
  <div class="activity__right min-h-screen bg-white xl:w-3/5 w-full xl:pb-12 pb-16 pt-6 xl:pl-16 xl:pr-32 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes matières préférées</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-5.5xl mr-4">3.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        Félicitations ! Tes matières préférées ont bien été enregistrées.
        Tu peux maintenant valider ton activité !
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex w-full py-12 sm:flex-wrap items-center">
      <div class="xl:w-2/3 w-full">
        <div class="flex flex-col justify-around h-full">
          <div v-for="(action, key) in validatedActions" :key="key" class="result flex items-center my-5">
            <div :style="{backgroundColor: action.color}" class="p-2 w-16 h-16 rounded-full flex items-center">
              <img :src="action.icon_path"
                   class="max-w-80 m-auto"
                   alt="illustration d'environnement favorable">
            </div>
            <div class="flex items-center">
              <span :style="{color: action.color}" class="text-5.5xl font-bold ml-8 mr-5 titleNumber">{{ key + 1 }}</span>
              <span class="text-blue font-bold text-xl leading-normal">
                {{ textPrefix[key] }}
                <br>{{ action.text.toLowerCase() }}
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="xl:w-1/2 w-full flex justify-end sm:mt-16 ">
        <picture>
          <img src="~/assets/img/environnements-favorables/illu.png"
               alt="Activité numéro 1">
        </picture>
      </div>
    </div>

    <div class="flex sm:flex-col">
      <button v-scroll-to="'#actTitle'"
              class="button button--white mt-8 self-start mt-3 border-blue border-2 border-solid sm:w-full"
              @click="restart">
        Je recommence
      </button>
      <button v-scroll-to="'#actTitle'"
              class="button button--blue mt-8 self-start mt-3 ml-6 sm:w-full sm:ml-0"
              @click="validate">
        Je termine mon activité
      </button>
    </div>

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
        return { actions: null }
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
      questionStep: 1,
      validatedActions: [],
      textPrefix: [
        'Je serai dans',
        'Je travaillerai',
        'Je serai',
        'Je travaillerai',
        'J\'utiliserai',
        'J\'aurai un habit'
      ]
    }
  },
  created() {
    const tmpValidatedActions = this.actions.filter(function(action) {
      return action.value
    });
    this.validatedActions = [
      tmpValidatedActions.find(this.getBySelectionId('1689ccba-0283-48d8-9ea2-99e2559064a7')),
      tmpValidatedActions.find(this.getBySelectionId('13e61d71-2f1d-4ed3-b2bd-d1168f77ac3d')),
      tmpValidatedActions.find(this.getBySelectionId('c1570dc0-22db-4b33-8f3c-bcbeb6606276')),
      tmpValidatedActions.find(this.getBySelectionId('4c0c813f-e655-4128-b508-6c2127e2c244')),
      tmpValidatedActions.find(this.getBySelectionId('b45d87c4-ac27-4685-9b3f-2178053b9a71')),
      tmpValidatedActions.find(this.getBySelectionId('8d3fda73-17fe-40da-93fa-8448ff5e6c71'))
    ]
  },
  mounted() {
    this.$emit( 'setStep', 8 )
  },
  methods: {
    validate: function() {
      const endResults = this.validatedActions.map(function (validatedAction) {
        return validatedAction.id
      })

      this.activityLogs.choice_ids = endResults
      this.$emit('createResults', endResults)
      this.$emit('createLogs', () => {
        this.$router.push( '/dashboard/student/domaines/introspection' )
      })
    },
    restart: function() {
      this.$router.push( '/dashboard/student/domaines/introspection/environnements-favorables' )
    },
    getBySelectionId: function(selection_id) {
      return function (action) {
        return action.selection_id === selection_id
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .titleNumber {
    transform: translateY(4px);
  }
  .logo {
    transform: translateY(-4px);
  }
}
</style>

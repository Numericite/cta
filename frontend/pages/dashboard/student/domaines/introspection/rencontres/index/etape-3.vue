<template>
  <div class="activity__right min-h-screen bg-white xl:w-3/5 w-full xl:pb-12 pb-16 pt-6 xl:pl-16 xl:pr-32 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes premières rencontres professionnelles</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4 ml-3">
        <span v-html="texts[0]"/>
      </h1>
    </div>
    <!-- End Title -->

    <div v-if="activityLogs.config && activityLogs.config.domainsLiked" class="flex w-full  h-1/2 py-12 sm:flex-wrap">
      <div class="w-full">
        <div class="flex flex-col flex-no-shrink justify-around h-full">

          <div v-for="n in [1, 2]" :key="n" class=" flex flex-no-shrink items-center my-4">
            <div :class="{'bg-topaze': n === 1, 'bg-yellow': n === 2}" class="rounded-full flex-no-shrink h-20 w-20">
              <img src="~/assets/img/activity-6-loupe.svg"
                   alt="illustration loupe"
                   class="m-2 h-10 w-10 logo mt-5 ml-4">
            </div>
            <div class="flex items-center">
              <span :class="{'text-topaze': n === 1, 'text-yellow': n === 2}" class="text-6xl font-bold ml-8 mr-5 titleNumber">{{ n }}</span>
              <span v-if="n === 1" class="text-blue font-bold text-xl leading-normal">
                <span v-html="texts[1]" /><br>
                {{ activityLogs.config.domainsLiked.text }}
              </span>
              <span v-if="n === 2" class="text-blue font-bold text-xl leading-normal">
                <span v-html="texts[2]" /><br>
                {{ activityLogs.config.jobsLiked.text }}
              </span>
            </div>
          </div>

        </div>
      </div>
      <div class="xl:w-2/3 xl:ml-3 flex justify-end sm:mt-16 ">
        <picture class="flex-no-grow w-full h-1/2">
          <img src="~/assets/img/activity-6.png"
               alt="Activité numéro 7">
        </picture>
      </div>
    </div>
    <div v-else class="flex w-full  h-1/2 py-12 sm:flex-wrap">
      <p class="text-3xl"><img src="~/assets/img/domain_ban.svg" class="w-4 h-4"> Je n'ai pas eu de contact avec le monde du travail</p>
    </div>

    <div class="flex sm:flex-col">
      <button v-scroll-to="'#actTitle'"
              class="button button--white mt-8 self-start mt-3 border-blue border-2 border-solid sm:w-full"
              @click="restart">
        Je recommence
      </button>
      <button v-scroll-to="'#actTitle'"
              class="button button--blue mt-8 self-start ml-6 mt-3 sm:w-full sm:ml-0"
              @click="validate">
        Je termine mon activité
      </button>
    </div>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'MesRencontres',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
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
  mounted() {
    if (!this.activityLogs.selection_ids)
      this.$router.push( '../rencontres' )

    this.$emit( 'setStep', 8 )
  },
  methods: {
    validate: function() {
      this.$emit('createResults')
      this.$emit('createLogs', () => {
        this.$router.push( {path: '/dashboard/student/domaines/introspection', hash: '#rencontres' })
      })
    },
    restart: function() {
      this.$router.push( '/dashboard/student/domaines/introspection/rencontres' )
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

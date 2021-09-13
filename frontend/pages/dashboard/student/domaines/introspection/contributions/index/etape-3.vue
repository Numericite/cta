<template>
  <div class="activity__right min-h-screen bg-white xl:w-3/5 w-full xl:pb-12 pb-16 pt-6 xl:pl-16 xl:pr-32 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes contributions</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">2.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4 ml-3">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex w-full  h-1/2 py-12 sm:flex-wrap">
      <div class="w-full">
        <div class="flex flex-col flex-no-shrink justify-around h-full">

          <div v-for="(result, key) in results" :key="key" class=" flex flex-no-shrink items-center my-4">
            <div :class="{'bg-yellow': key === 0, 'bg-peach': key === 1, 'bg-topaze': key === 2}" class="rounded-full flex-no-shrink h-20 w-20">
              <img :src="result.img_path"
                   alt="illustration d'une activité"
                   class="m-2 h-10 w-10 logo mt-5 ml-4">
            </div>
            <div class="flex items-center">
              <span :class="{'text-yellow': key === 0, 'text-peach': key === 1, 'text-topaze': key === 2}" class="text-6xl font-bold ml-8 mr-5 titleNumber">{{ key + 1 }}</span>
              <span class="text-blue font-bold text-2xl mt-4 leading-normal">{{ result.text }}</span>
            </div>
          </div>

        </div>
      </div>
      <div class="xl:w-2/3 xl:ml-3 flex justify-end sm:mt-16 ">
        <picture class="flex-no-grow  h-1/2">
          <img src="~/assets/img/activity-7.png"
               alt="Activité numéro 7">
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
  name: 'CentresDinterets',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    results: {
      type: Array,
      default: function() {
        return []
      }
    }
  },
  data() {
    return {
      questionStep: 1,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    if (!this.results)
      this.$router.push('../contributions')
    this.$emit( 'setStep', 8 )
  },
  methods: {
    validate: function() {
      this.$emit('createResults')
      this.$emit('createLogs', () => {
        this.$router.push( {path: '/dashboard/student/domaines/introspection', hash: '#contributions' })
      })
    },
    restart: function() {
      this.$router.push( '/dashboard/student/domaines/introspection/contributions' )
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

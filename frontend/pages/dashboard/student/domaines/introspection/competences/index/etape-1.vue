<template>
  <div class="activity__right min-h-screen relative bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:pl-16 px-6 xl:pr-32 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes compétences primaires</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4 sm:text-3xl">1.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal xl:mt-4 sm:text-xl">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <videoComp vimeoId="357031661"
               class="w-full mt-8" />

    <button v-scroll-to="'#actTitle'"
            class="button button--blue mt-8 self-start ml-6 mt-3"
            @click="nextPage">
      Je continue
    </button>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import VideoComp from '~/components/Video'


export default {
  name: 'MatieresPreferees',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  components: {
    VideoComp
  },
  data() {
    return {
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    this.$emit( 'setStep', 1 )
  },
  methods: {
    nextPage() {
      this.$router.push( '/dashboard/student/domaines/introspection/competences/etape-2' )
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

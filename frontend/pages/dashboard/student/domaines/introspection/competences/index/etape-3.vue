<template>
  <div class="activity__right min-h-screen bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes compétences primaires</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">3.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        {{ texts[0] }}
      </h1>
    </div>
    <!-- End Title -->

    <div v-show="step == 1"
         class="bg-white rounded-xlg cardShadow p-10 mt-12">
      <div class="flex flex-no-wrap">
        <div class="uppercase font-bold text-sm w-1/2">
          <span class="text-blue">Étape 1</span> <span class="text-orange">sur 4</span>
        </div>
        <div class="flex justify-end w-1/2">
          <span class="rounded-full h-3 w-3 bg-orange mx-1" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
        </div>
      </div>
      <div class="flex flex-col mt-6 justify-center">
        <div class="text-blue uppercase text-xl font-bold justify-center flex">
          <span v-html="texts[1]" />
        </div>
        <div class="text-center">
          <img class="w-48 mt-6"
               src="../../../../../../../assets/img/competences/exemple-concret/1.png"
               alt="">
        </div>
        <div class="flex flex-col justify-center mt-6">
          <div class="text-2xl text-blue font-bold text-center">
            <span v-html="texts[2]" />
          </div>
          <div class="text-base text-blue text-center xl:px-56 mt-2 leading-tight font-semibold">
            <span v-html="texts[3]" />
          </div>
        </div>
        <div class="flex justify-center">
          <button v-scroll-to="'#actTitle'"
                  class="button button--orange mt-8 self-start mt-3"
                  @click="nextStep">
            Je découvre
          </button>
        </div>
      </div>
    </div>
    <div v-show="step == 2"
         class="bg-white rounded-xlg cardShadow p-10 mt-12">
      <div class="flex flex-no-wrap">
        <div class="uppercase font-bold text-sm w-1/2">
          <span class="text-blue">Étape 2</span> <span class="text-orange">sur 4</span>
        </div>
        <div class="flex justify-end w-1/2">
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
        </div>
      </div>
      <div class="flex flex-col mt-6 justify-center">
        <div class="text-blue uppercase text-xl font-bold justify-center flex">
          <span v-html="texts[2]" />
        </div>
        <div class="text-center">
          <img class="w-96 mt-6"
               src="../../../../../../../assets/img/competences/exemple-concret/2.png"
               alt="">
        </div>
        <div class="flex flex-col justify-center mt-12">
          <div class="text-2xl text-blue font-bold text-center">
            <span class="text-orange"><span v-html="texts[4]" /></span><br><span v-html="texts[5]" />
          </div>
          <div class="text-base text-blue text-center xl:px-56 mt-2 leading-tight font-semibold">
            <span v-html="texts[6]" />
          </div>
        </div>
        <div class="flex justify-center">
          <button v-scroll-to="'#actTitle'"
                  class="button button--orange mt-8 self-start mt-3"
                  @click="nextStep">
            Je continue
          </button>
        </div>
      </div>
    </div>
    <div v-show="step == 3"
         class="bg-white rounded-xlg cardShadow p-10 mt-12">
      <div class="flex flex-no-wrap">
        <div class="uppercase font-bold text-sm w-1/2">
          <span class="text-blue">Étape 3</span> <span class="text-orange">sur 4</span>
        </div>
        <div class="flex justify-end w-1/2">
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
        </div>
      </div>
      <div class="flex flex-col mt-6 justify-center">
        <div class="text-blue uppercase text-xl font-bold justify-center flex">
          <span v-html="texts[2]" />
        </div>
        <div class="text-center">
          <img class="w-80 mt-6"
               src="../../../../../../../assets/img/competences/exemple-concret/3.png"
               alt="">
        </div>
        <div class="flex flex-col justify-center mt-6">
          <div class="text-2xl text-blue font-bold text-center">
            <span class="text-orange"><span v-html="texts[7]" /></span><br><span v-html="texts[8]" />
          </div>
          <div class="text-base text-blue text-center xl:px-56 mt-2 leading-tight font-semibold">
            <span v-html="texts[9]" />
          </div>
        </div>
        <div class="flex justify-center">
          <button v-scroll-to="'#actTitle'"
                  class="button button--orange mt-8 self-start mt-3"
                  @click="nextStep">
            Je continue
          </button>
        </div>
      </div>
    </div>
    <div v-show="step == 4"
         class="bg-white rounded-xlg cardShadow p-10 mt-12">
      <div class="flex flex-no-wrap">
        <div class="uppercase font-bold text-sm w-1/2">
          <span class="text-blue">Étape 4</span> <span class="text-orange">sur 4</span>
        </div>
        <div class="flex justify-end w-1/2">
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1 opacity-25" />
          <span class="rounded-full h-3 w-3 bg-orange mx-1" />
        </div>
      </div>
      <div class="flex flex-col mt-6 justify-center">
        <div class="text-blue uppercase text-xl font-bold justify-center flex">
          <span v-html="texts[2]" />
        </div>
        <div class="text-center">
          <img class="w-96 mt-6"
               src="../../../../../../../assets/img/competences/exemple-concret/4.png"
               alt="">
        </div>
        <div class="flex flex-col justify-center mt-6">
          <div class="text-2xl text-blue font-bold text-center">
            <span v-html="texts[10]" />
          </div>
          <div class="text-base text-blue text-center xl:px-56 mt-2 leading-tight font-semibold">
            <span v-html="texts[11]" />
          </div>
        </div>
        <div class="flex justify-center">
          <button v-scroll-to="'#actTitle'"
                  class="button button--blue mt-8 self-start mt-3"
                  @click="nextStep">
            J'ai compris
          </button>
        </div>
      </div>
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
    passedQuizz: {
      type: Boolean,
      default: function() {
        return false
      }
    }
  },
  data() {
    return {
      step: 1,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    if (!this.passedQuizz)
      this.$router.push('../competences')

    this.$emit( 'setStep', 1 )
  },
  methods: {
    nextStep: function() {
      if ( this.step != 4 ) {
        this.step++
      } else {
        this.$router.push( 'etape-4' )
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.activity__right {
  .card-head {
    height: 106px;
  }

  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }

  .cursor-grab {
    cursor: grab;
  }
}
</style>

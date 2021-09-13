<template>
  <div class="activity__right relative bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:pl-16 px-6 xl:pr-32 flex flex-col justify-center min-h-screen h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Intelligences multiples</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4 sm:text-3xl">1.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal xl:mt-4 sm:text-xl">
        <span v-if="questionStep === 1" ><span v-html="texts[0]" /><br></span>
        <span v-html="texts[1]" /> <span v-if="questionStep === 7"><span v-html="texts[2]" /></span> <span v-if="questionStep === 8"><span v-html="texts[3]" /></span> {{ questionStep }}/9
      </h1>
    </div>
    <!-- End Title -->

    <div class="questionCard card rounded-lg mt-12 xl:w-5/6 xl:mx-6 mx-2 xl:p-8 p-4">

      <!-- Questions -->
      <div v-for="question in questions.slice(getFirstQuestion(), questionStep * 8)"
           :key="question.id">
        <div v-if="questions && questions[question.num -1]"
             class="question w-full py-2 flex cursor-pointer"
             @click="changeValue(question)">

          <!-- checkbox -->
          <div class="checkbox w-8 h-8 xl:mr-10 mr-3">
            <input :id="question.num"
                   v-model="question.value"
                   name="donnees"
                   type="checkbox"
                   @click="changeValue(question)">
            <label :for="question.num" />
          </div>
          <!-- End checkbox -->

          <!-- question -->
          <div class="content flex">
            <span class="font-bold xl:text-4xl text-2xl text-blue xl:mr-8 mr-3 sm:mt-1">{{ question.num }}</span>
            <p class="text-blue xl:text-lg text-base sm:mb-2">
              {{ question.text }}
            </p>
          </div>
          <!-- End question -->

        </div>
        <hr v-if="question.num != 8 && 16 && 24 && 32 && 40 && 48 && 56 && 64 && 72 && 80"
            class="hr xl:mb-4 mb-2">
      </div>

      <!-- End question -->
    </div>

    <button v-scroll-to="'#actTitle'"
            class="button button--blue mt-8 self-start ml-6 mt-3"
            @click="nextQuestionStep">
      Je continue
    </button>

    <div class="xl:fixed pin-b pin-r pr-12 pb-12 text-4xl text-blue-lighter font-bold sm:text-right sm:p-12 sm:absolute">
      <span class="text-blue text-6xl">{{ questionStep }}</span>
      <span>/</span>
      <span>{{ questions.length / 8 }}</span>
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
    questions: {
      type: Array,
      default: function() {
        return { questions: null }
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
      texts: this.getPageTexts(this.$route.name)
    }
  },
  created() {
    this.$emit('setFirstStep')
    if (this.activityLogs.selector_ids) {
      const selector_ids = this.activityLogs.selector_ids
      this.questions.forEach(function(question) {
        question.value = (selector_ids.indexOf(question.id) !== -1)
      })
    }
  },
  methods: {
    nextQuestionStep: function() {
      if (this.questionStep < this.questions.length / 8) {
        this.questionStep++
      } else {
        this.$emit('sendResults', this.questions)

        this.activityLogs.selector_ids = this.questions.filter(function (question) {
          return question.value === true
        }).map(function(question) {
          return question.id
        })
        this.$emit('setActivityLogs', this.activityLogs)

        this.$router.push('etape-2')
      }
      this.$emit('setQuestionStep', this.questionStep)
    },
    getFirstQuestion: function() {
      if (this.questionStep == 1) {
        return 0
      } else {
        return (this.questionStep - 1) * 8
      }
    },
    changeValue: function(question) {
      question.value = !question.value
      document.getElementById(question.num).checked = question.value
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .questionCard {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }
}
</style>

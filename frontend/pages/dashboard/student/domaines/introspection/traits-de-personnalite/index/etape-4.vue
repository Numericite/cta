<template>
  <div class="activity__right bg-blue-lightest min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes traits de personnalités</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">5.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="questionCard card rounded-xlg overflow-hidden mt-12 xl:w-5/6 relative xl:mx-16 mx-2 xl:p-8 p-4">
      <div class="flex flex-wrap -mx-1  lg:-mx-1">
        <template v-for="(personnalite, key) in results">
          <div :key="key"
               :class=" key < 12 ? 'border-b-2 border-blue-lightest border-solid' : ''"
               class="w-1/3 my-2 px-2 sm:w-1/2 flex lg:my-1 lg:px-1 lg:w-1/3 relative cursor-pointer"
               @click="personnalite.value = !personnalite.value">
            <div class="checkbox w-4 h-6 xl:mr-6 mr-3"
                 disabled
                 checked>
              <!-- {{ key + (personnalitesStep - 1) * 27 }} -->
              <input :id="key"
                     checked
                     name="donnees"
                     type="checkbox">
              <label :for="key" />
            </div>
            <div class="">
              <p class="text-blue xl:text-lg text-base sm:mb-2">
                {{ personnalite.desc }}
              </p>
              <svg v-if="personnalite.value0"
                   xmlns="http://www.w3.org/2000/svg"
                   width="106"
                   height="38"
                   viewBox="0 0 106 38"
                   class="select-pers0 fill-current absolute text-topaze">
                <path fill-rule="evenodd"
                      d="M88.306 3.217C71.356.13 53.984-1.031 36.841 1.05c-8.386 1.018-16.705 2.771-24.726 5.46C7.203 8.154.685 11.152.05 17.088c-.685 6.405 5.754 10.26 10.745 12.412 8.057 3.474 16.789 5.053 25.419 6.288A212.272 212.272 0 0 0 62.9 37.912c8.56.137 17.38.086 25.832-1.446 6.107-1.107 17.03-3.805 16.723-11.949-.305-8.151-8.844-11.106-15.355-12.8-1.007-.26-1.436 1.31-.43 1.571 5.834 1.517 16.167 4.972 13.972 13.208-1.018 3.818-5.552 5.602-8.864 6.746-3.56 1.229-7.287 1.893-11.02 2.294-14.787 1.59-30.029.93-44.75-.994-7.43-.971-14.936-2.19-22.093-4.485C11.472 28.311.33 24.575 1.702 16.842c1.135-6.397 9.932-8.586 15.088-10.086 7.19-2.092 14.57-3.505 22.01-4.3C55.168.703 71.714 1.843 87.878 4.786c1.016.185 1.452-1.384.43-1.57" />
              </svg>
              <svg v-if="personnalite.value1"
                   xmlns="http://www.w3.org/2000/svg"
                   width="106"
                   height="38"
                   viewBox="0 0 106 38"
                   class="select-pers1 fill-current absolute text-peach">
                <path fill-rule="evenodd"
                      d="M88.306 3.217C71.356.13 53.984-1.031 36.841 1.05c-8.386 1.018-16.705 2.771-24.726 5.46C7.203 8.154.685 11.152.05 17.088c-.685 6.405 5.754 10.26 10.745 12.412 8.057 3.474 16.789 5.053 25.419 6.288A212.272 212.272 0 0 0 62.9 37.912c8.56.137 17.38.086 25.832-1.446 6.107-1.107 17.03-3.805 16.723-11.949-.305-8.151-8.844-11.106-15.355-12.8-1.007-.26-1.436 1.31-.43 1.571 5.834 1.517 16.167 4.972 13.972 13.208-1.018 3.818-5.552 5.602-8.864 6.746-3.56 1.229-7.287 1.893-11.02 2.294-14.787 1.59-30.029.93-44.75-.994-7.43-.971-14.936-2.19-22.093-4.485C11.472 28.311.33 24.575 1.702 16.842c1.135-6.397 9.932-8.586 15.088-10.086 7.19-2.092 14.57-3.505 22.01-4.3C55.168.703 71.714 1.843 87.878 4.786c1.016.185 1.452-1.384.43-1.57" />
              </svg>
              <svg v-if="personnalite.value2"
                   xmlns="http://www.w3.org/2000/svg"
                   width="106"
                   height="38"
                   viewBox="0 0 106 38"
                   class="select-pers2 fill-current absolute text-yellow">
                <path fill-rule="evenodd"
                      d="M88.306 3.217C71.356.13 53.984-1.031 36.841 1.05c-8.386 1.018-16.705 2.771-24.726 5.46C7.203 8.154.685 11.152.05 17.088c-.685 6.405 5.754 10.26 10.745 12.412 8.057 3.474 16.789 5.053 25.419 6.288A212.272 212.272 0 0 0 62.9 37.912c8.56.137 17.38.086 25.832-1.446 6.107-1.107 17.03-3.805 16.723-11.949-.305-8.151-8.844-11.106-15.355-12.8-1.007-.26-1.436 1.31-.43 1.571 5.834 1.517 16.167 4.972 13.972 13.208-1.018 3.818-5.552 5.602-8.864 6.746-3.56 1.229-7.287 1.893-11.02 2.294-14.787 1.59-30.029.93-44.75-.994-7.43-.971-14.936-2.19-22.093-4.485C11.472 28.311.33 24.575 1.702 16.842c1.135-6.397 9.932-8.586 15.088-10.086 7.19-2.092 14.57-3.505 22.01-4.3C55.168.703 71.714 1.843 87.878 4.786c1.016.185 1.452-1.384.43-1.57" />
              </svg>
            </div>
          </div>
        </template>

      </div>
    </div>
    <div class="questionCard card rounded-xlg overflow-hidden mt-6 xl:w-5/6 relative xl:mx-16 mx-2 xl:p-8 p-4">
      <div class="flex sm:flex-wrap -mx-1  lg:-mx-1">
        <div v-for="(endResult, key) in endResults" :key="key" :class="{'bg-topaze': key === 0, 'bg-peach': key === 1, 'bg-yellow': key === 2, 'sm:my-6': key === 1}" class="card flex relative mx-2 rounded-xlg w-1/3 sm:w-full">
          <div class="text-center w-full mx-4 my-5">
            <span class="text-white font-bold tracking-wide text-5.5xl">
              <span>{{ endResult.count }}/3</span>
              <svg class="m-2 mt-3 mr-4 absolute pin-t pin-r"
                   xmlns="http://www.w3.org/2000/svg"
                   width="17"
                   height="13"
                   viewBox="0 0 17 13">
                <g fill="none"
                   fill-rule="nonzero">
                  <circle cx="6.5"
                          cy="6.5"
                          r="6.5"
                          fill="#FFF"
                          opacity=".381" />
                  <path stroke="#FFF"
                        stroke-linecap="round"
                        stroke-width="2"
                        d="M3.28 5l4.36 4 7.99-7" />
                </g>
              </svg>
            </span>
            <div class="text-white font-bold mt-1">{{ endResult.desc }}</div>
          </div>
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            class="button button--blue mt-8 self-start ml-16"
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
    personnalites: {
      type: Array,
      default: function () {
        return []
      }
    },
    tokens: {
      type: Array,
      default: function () {
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
      results: [],
      endResults: [],
      texts: this.getPageTexts(this.$route.name)
    }
  },
  created() {
    this.$emit( 'setStep', 11 )

    const firsts_choice_ids = this.activityLogs.config.firsts_choices_ids
    const choice_ids = this.activityLogs.choice_ids
    const joker1_choice_ids = this.tokens[0] ? this.tokens[0].config.choice_ids : []
    const joker2_choice_ids = this.tokens[1] ? this.tokens[1].config.choice_ids : []
    const results = []

    this.personnalites.forEach(function (personnalite) {
      if (firsts_choice_ids.indexOf(personnalite.id) !== -1) {
        var result = {
          id: personnalite.id,
          desc: personnalite.text,
          value0: choice_ids.indexOf(personnalite.id) !== -1,
          value1: joker1_choice_ids.indexOf(personnalite.id) !== -1,
          value2: joker2_choice_ids.indexOf(personnalite.id) !== -1
        }

        result.count = 0

        if (result.value0) ++result.count
        if (result.value1) ++result.count
        if (result.value2) ++result.count

        results.push(result)
      }
    })

    this.results = results

    this.endResults = Array.prototype.slice.call(results).sort(function ( a, b ) {
      return b.count - a.count
    }).slice(0, 3)
  },
  methods: {
    nextStep: function () {
      this.$emit( 'sendResults', this.results)
      this.$router.push( 'etape-5' )
    }
  }
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

  .select-pers0 {
    transform: translate(-0.7rem, -3.6rem);
  }

  .select-pers1 {
    transform: translate(-0.8rem, -3.3rem);
  }

  .select-pers2 {
    transform: translate(-0.5rem, -3.8rem);
  }

  .bandeau {
    transform: translate(45%, 24px) rotate(40deg);
  }
}
</style>

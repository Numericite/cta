<template>
  <div class="activity__right bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center min-h-screen h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Intelligences multiples</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">2.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <!-- Results -->
    <div v-show="intelligences"
         class="flex mt-12 sm:w-auto">

      <!-- Intelligence -->
      <no-ssr>
        <swiper v-show="intelligences.length > 0"
                :options="swiperOption"
                class="flex w-full">
          <swiper-slide v-for="(intelligence, key) in intelligences.slice(0,3)"
                        :key="key"
                        class="xl:w-1/3 w-2/3 card intelligence rounded-lg overflow-hidden">

            <!-- Header -->
            <div class="p-4 intelligence__header flex">
              <div class="w-3/5">
                <span class="flex items-start">
                  <h4 class="font-bold text-white mr-2">{{ intelligence.weight }}</h4>
                  <input id="t"
                         class="cursor-default"
                         type="checkbox"
                         disabled
                         checked>
                  <label for="t"
                         class="cursor-default cursor-intelligence cursor-intelligence--firsts mt-2 ml-2" />
                </span>
                <p class="text-lg text-white font-bold leading-tight mt-2 mb-0 text-3xl break-words">{{ intelligence.text }}</p>
              </div>
              <div class="w-1/2">
                <radial-progress-bar :diameter="120"
                                     :completed-steps="intelligence.weight / 8"
                                     :startColor="'#fff'"
                                     :stopColor="'#fff'"
                                     :strokeWidth="8"
                                     :innerStrokeColor="'#ffffff59'"
                                     :total-steps="9">
                  <img :src="intelligence.img_path" class="w-1/2">
                </radial-progress-bar>
              </div>
            </div>
            <!-- End Header -->

            <!-- content -->
            <div class="content p-4">
              <div class="flex items-center flex-wrap mb-2">
                <span v-for="action in intelligence.actions"
                      :key="action.id"
                      :class="{'text-yellow border-yellow': key === 0, 'text-peach border-peach': key === 1, 'text-topaze border-topaze': key === 2}"
                      @click="action.value = !action.value">
                  <svg width="12"
                       height="15"
                       class="mr-3"
                       viewBox="0 0 100 125"
                       x="0px"
                       y="0px">
                    <path :fill="[action.value ? (key === 0 ? '#ffc600' : (key === 1 ? '#ff9c9c' : (key === 2 ? '#1dd4b6' : '#fff'))) : '#fff']"
                          :stroke="key === 0 ? '#ffc600' : (key === 1 ? '#ff9c9c' : (key === 2 ? '#1dd4b6' : '#fff'))"
                          d="M50,94,9.3,53.3A27.71,27.71,0,0,1,48.49,14.11L50,15.63l1.51-1.52A27.71,27.71,0,0,1,90.7,53.3Z"
                          y="115"
                          stroke-width="10" />
                  </svg>
                  {{ action.text }}
                </span>
              </div>
              <textarea v-model="intelligence.studentAnswer"
                        name=""
                        cols="30"
                        rows="10"
                        placeholder="Exemples : J’aime lire, Je parle facilement, j’aime raconter des histoires, j’aime en entendre, j ‘aime les jeux avec des mots (mots croisés, Scrabble, etc.), j’aime les jeux de mots…" /></div>

          </swiper-slide>
        </swiper>
      </no-ssr>

      <!-- End Intelligence -->

    </div>

    <div class="my-8 self-start flex flex-wrap w-full">
      <div v-for="intelligence in intelligences.slice(3)"
           :key="intelligence.name" class="card flex items-center rounded-full self-start p-3 px-6 mr-3 mt-3">
        <h4 class="font-bold text-3xl text-blue mr-3">{{ intelligence.weight }}</h4>
        <p class="text-lg font-semibold text-blue mb-0">{{ intelligence.text }}</p>
        <input id="t"
               class="cursor-default"
               type="checkbox"
               disabled
               checked>
        <label for="t"
               class="cursor-default cursor-intelligence cursor-intelligence--others ml-2 mb-6 ml-3" />
      </div>
    </div>
    <!-- End Results -->

    <button v-scroll-to="'#actTitle'"
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
    intelligences: {
      type: Array,
      default: function () {
        return { intelligences: null }
      }
    },
    actions: {
      type: Array,
      default: function () {
        return { actions: null }
      }
    },
    results: {
      type: Array,
      default: function () {
        return { results: null }
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
      swiperOption: {
        slidesPerView: 3,
        centeredSlides: false,
        spaceBetween: 10,
        navigation: {
          nextEl: '.next',
          prevEl: '.prev'
        },
        breakpoints: {
          1024: {
            slidesPerView: 3,
            spaceBetween: 30
          },
          768: {
            slidesPerView: 2,
            spaceBetween: 20
          },
          640: {
            slidesPerView: 1.1,
            spaceBetween: 10
          },
          320: {
            slidesPerView: 1,
            spaceBetween: 5
          }
        }
      },
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    if ( !this.results || !this.intelligences )
      this.$router.push( '../intelligences-multiples' )

    const groupedResults = this.groupBy(this.results.filter(function (result) {
      return result.value === true
    }), 'selection_id')

    const groupedActions = this.groupBy(this.actions, 'selection_id')

    const weights = []
    this.intelligences.forEach(function (intelligence) {
      const intelligenceResults = groupedResults[intelligence.id] || []
      intelligence.weight = intelligenceResults.length
      weights[intelligence.id] = intelligence.weight
      intelligence.actions = groupedActions[intelligence.id] || []
    })
    this.activityLogs.config.weights = Object.assign({}, weights)

    // sort the intelligences by number of answers
    this.intelligences.sort( function ( a, b ) {
      return b.weight - a.weight
    } )

    if (this.activityLogs.choice_ids) {
      const choice_ids = this.activityLogs.choice_ids
      this.intelligences.slice(0, 3).forEach(function(intelligence) {
        intelligence.actions.forEach(function(action) {
          action.value = (choice_ids.indexOf(action.id) !== -1)
        })
      })
    }
  },
  created() {
    this.$emit( 'setSecondStep' )
  },
  methods: {
    groupBy: function (xs, key) {
      return xs.reduce(function(rv, x) {
        (rv[x[key]] = rv[x[key]] || []).push(x);
        return rv;
      }, {});
    },
    nextStep: function () {
      const endResults = []
      this.intelligences.slice(0, 3).forEach(function(intelligence) {
          intelligence.actions.forEach(function(action) {
            if (action.value) {
              endResults.push(action.id)
            }
          })
      })

      this.activityLogs.selection_ids = this.intelligences.slice(0, 3).map(function(intelligence) {
        return intelligence.id
      })
      this.activityLogs.choice_ids = endResults
      this.$emit('setActivityLogs', this.activityLogs)

      this.$emit( 'setEndResults', endResults )
      this.$router.push( 'etape-3' )
    }
  }
}
</script>

<style lang="scss" scoped>
.activity__right {
  .radial-progress-container {
    margin-left: auto;
  }

  .radial-progress-inner {
    width: 125px;
  }
  .radial-progress-inner img{
    width: 60px;
  }

  .cursor-intelligence {
    top: -4px;

    &--firsts {
      &:after {
        color: #fff;
      }

      &:before {
        background-color: #ffffff61;
      }
    }

    &--others {

    }

    &:after {
      top: 0.1rem;
      left: 0.2rem;
      font-size: 2.5rem;
    }
  }

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
        @apply flex items-center font-bold uppercase text-xs mr-2 mb-2 border-solid border rounded-full p-2 cursor-pointer;
      }
    }
  }
  .card {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }
}
</style>

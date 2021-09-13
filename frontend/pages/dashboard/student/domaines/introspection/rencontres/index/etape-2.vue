<template>
  <div class="activity__right relative bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:pl-16 px-6 xl:pr-32 flex flex-col justify-center min-h-screen">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes premières rencontres professionnelles</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4 sm:text-3xl">2.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal xl:mt-4 sm:text-xl">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <!-- STEP 1 -->
    <div v-show="currentStep === 1" class="px-12">
      <span class="rounded-lg p-2 bg-topaze text-white text-3xl font-bold mt-12 mb-8 w-auto inline-block">
        <span v-html="texts[1]" />
      </span>
      <div class="card rounded-lg shadow-md flex">
        <div class="w-1/2 sm:w-full px-8 py-12 border-r border-blue-lightest border-solid">
          <div class="text-center text-blue text-2xl font-bold">
            <span v-html="texts[2]" />
          </div>
          <div class="relative mt-8 px-6">
            <div v-if="!config.domainsLiked.textBlur" class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
              <textarea v-model="config.domainsLiked.text"
                        placeholder="Citer les domaines"
                        class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal h-24"
                        @focus="config.domainsLiked.textBlur = false"
                        @blur="config.domainsLiked.text ? config.domainsLiked.textBlur = true : config.domainsLiked.textBlur = false"/>
            </div>
            <div v-if="config.domainsLiked.textBlur && config.domainsLiked.text" class="bg-blue-lightest h-32 text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
              {{ config.domainsLiked.text.substring(0, 100) + '...' }}
              <img src="~/assets/img/pencil_2.svg" class="p-3 absolute pin-b pin-r cursor-pointer" @click="config.domainsLiked.textBlur = false">
            </div>
          </div>
          <div class="relative mt-8 px-6">
            <div v-if="!config.domainsLiked.whyBlur" class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
              <textarea v-model="config.domainsLiked.why"
                        placeholder="Expliquer pourquoi"
                        class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal h-24"
                        @focus="config.domainsLiked.whyBlur = false"
                        @blur="config.domainsLiked.why ? config.domainsLiked.whyBlur = true : config.domainsLiked.whyBlur = false"/>
            </div>
            <div v-if="config.domainsLiked.whyBlur && config.domainsLiked.why" class="bg-blue-lightest h-32 text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
              {{ config.domainsLiked.why.substring(0, 100) + '...' }}
              <img src="~/assets/img/pencil_2.svg" class="p-3 absolute pin-b pin-r cursor-pointer" @click="config.domainsLiked.whyBlur = false">
            </div>
          </div>
        </div>
        <div class="w-1/2 sm:w-full px-8 py-12">
          <div class="text-center text-blue text-2xl font-bold">
            <span v-html="texts[3]" />
          </div>
          <div class="relative mt-8 px-6">
            <div v-if="!config.domainsDisliked.textBlur" class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
              <textarea v-model="config.domainsDisliked.text"
                        placeholder="Citer les domaines"
                        class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal h-24"
                        @focus="config.domainsDisliked.textBlur = false"
                        @blur="config.domainsDisliked.text ? config.domainsDisliked.textBlur = true : config.domainsDisliked.textBlur = false"/>
            </div>
            <div v-if="config.domainsDisliked.textBlur && config.domainsDisliked.text" class="bg-blue-lightest h-32 text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
              {{ config.domainsDisliked.text.substring(0, 100) + '...' }}
              <img src="~/assets/img/pencil_2.svg" class="p-3 absolute pin-b pin-r cursor-pointer" @click="config.domainsDisliked.textBlur = false">
            </div>
          </div>
          <div class="relative mt-8 px-6">
            <div v-if="!config.domainsDisliked.whyBlur" class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
              <textarea v-model="config.domainsDisliked.why"
                        placeholder="Expliquer pourquoi"
                        class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal h-24"
                        @focus="config.domainsDisliked.whyBlur = false"
                        @blur="config.domainsDisliked.why ? config.domainsDisliked.whyBlur = true : config.domainsDisliked.whyBlur = false"/>
            </div>
            <div v-if="config.domainsDisliked.whyBlur && config.domainsDisliked.why" class="bg-blue-lightest h-32 text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
              {{ config.domainsDisliked.why.substring(0, 100) + '...' }}
              <img src="~/assets/img/pencil_2.svg" class="p-3 absolute pin-b pin-r cursor-pointer" @click="config.domainsDisliked.whyBlur = false">
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- END STEP 1 -->

    <!-- STEP 2 -->
    <div v-show="currentStep === 2" class="px-12">
      <span class="rounded-lg p-2 bg-yellow text-white text-3xl font-bold mt-12 mb-8 w-auto inline-block">
        <span v-html="texts[4]" />
      </span>
      <div class="card rounded-lg shadow-md flex">
        <div class="w-1/2 sm:w-full px-8 py-12 border-r border-blue-lightest border-solid">
          <div class="text-center text-blue text-2xl font-bold">
            <span v-html="texts[5]" />
          </div>
          <div class="relative mt-8 px-6">
            <div v-if="!config.jobsLiked.textBlur" class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
              <textarea v-model="config.jobsLiked.text"
                        placeholder="Citer les métiers"
                        class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal h-24"
                        @focus="config.jobsLiked.textBlur = false"
                        @blur="config.jobsLiked.text ? config.jobsLiked.textBlur = true : config.jobsLiked.textBlur = false"/>
            </div>
            <div v-if="config.jobsLiked.textBlur && config.jobsLiked.text" class="bg-blue-lightest h-32 text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
              {{ config.jobsLiked.text.substring(0, 100) + '...' }}
              <img src="~/assets/img/pencil_2.svg" class="p-3 absolute pin-b pin-r cursor-pointer" @click="config.jobsLiked.textBlur = false">
            </div>
          </div>
          <div class="relative mt-8 px-6">
            <div v-if="!config.jobsLiked.whyBlur" class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
              <textarea v-model="config.jobsLiked.why"
                        placeholder="Expliquer pourquoi"
                        class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal h-24"
                        @focus="config.jobsLiked.whyBlur = false"
                        @blur="config.jobsLiked.why ? config.jobsLiked.whyBlur = true : config.jobsLiked.whyBlur = false"/>
            </div>
            <div v-if="config.jobsLiked.whyBlur && config.jobsLiked.why" class="bg-blue-lightest h-32 text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
              {{ config.jobsLiked.why.substring(0, 100) + '...' }}
              <img src="~/assets/img/pencil_2.svg" class="p-3 absolute pin-b pin-r cursor-pointer" @click="config.jobsLiked.whyBlur = false">
            </div>
          </div>
        </div>
        <div class="w-1/2 sm:w-full px-8 py-12">
          <div class="text-center text-blue text-2xl font-bold">
            <span v-html="texts[6]" />
          </div>
          <div class="relative mt-8 px-6">
            <div v-if="!config.jobsDisliked.textBlur" class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
              <textarea v-model="config.jobsDisliked.text"
                        placeholder="Citer les métiers"
                        class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal h-24"
                        @focus="config.jobsDisliked.textBlur = false"
                        @blur="config.jobsDisliked.text ? config.jobsDisliked.textBlur = true : config.jobsDisliked.textBlur = false"/>
            </div>
            <div v-if="config.jobsDisliked.textBlur && config.jobsDisliked.text" class="bg-blue-lightest h-32 text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
              {{ config.jobsDisliked.text.substring(0, 100) + '...' }}
              <img src="~/assets/img/pencil_2.svg" class="p-3 absolute pin-b pin-r cursor-pointer" @click="config.jobsDisliked.textBlur = false">
            </div>
          </div>
          <div class="relative mt-8 px-6">
            <div v-if="!config.jobsDisliked.whyBlur" class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
              <textarea v-model="config.jobsDisliked.why"
                        placeholder="Expliquer pourquoi"
                        class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal h-24"
                        @focus="config.jobsDisliked.whyBlur = false"
                        @blur="config.jobsDisliked.why ? config.jobsDisliked.whyBlur = true : config.jobsDisliked.whyBlur = false"/>
            </div>
            <div v-if="config.jobsDisliked.whyBlur && config.jobsDisliked.why" class="bg-blue-lightest h-32 text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
              {{ config.jobsDisliked.why.substring(0, 100) + '...' }}
              <img src="~/assets/img/pencil_2.svg" class="p-3 absolute pin-b pin-r cursor-pointer" @click="config.jobsDisliked.whyBlur = false">
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- END STEP 2 -->


    <div class="xl:fixed pin-b pin-r pr-12 pb-12 text-4xl text-blue-lighter font-bold sm:text-right sm:p-12 sm:absolute">
      <span class="text-blue text-6xl">{{ currentStep }}</span>
      <span>/</span>
      <span>2</span>
    </div>

    <div class="flex mt-8">
      <button v-if="currentStep > 1" class="button button--blue ml-6 mt-6"
              @click="previousStep">
        Retour
      </button>

      <button v-scroll-to="'#actTitle'"
              :disabled="currentStep === 1 ? !config.domainsLiked.text || !config.domainsLiked.why || !config.domainsDisliked.text || !config.domainsDisliked.why : !config.jobsLiked.text || !config.jobsLiked.why || !config.jobsDisliked.text || !config.jobsDisliked.why"
              class="button button--blue ml-6 mt-6"
              @click="nextPage">
        Je continue
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
      currentStep: 1,
      config: {
        domainsLiked: {
          text: '',
          why: '',
          textBlur: false,
          whyBlur: false
        },
        domainsDisliked: {
          text: '',
          why: '',
          textBlur: false,
          whyBlur: false
        },
        jobsLiked: {
          text: '',
          why: '',
          textBlur: false,
          whyBlur: false
        },
        jobsDisliked: {
          text: '',
          why: '',
          textBlur: false,
          whyBlur: false
        }
      },
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    this.$emit( 'setStep', 1 )

    if (this.activityLogs.config) {
      this.config = this.activityLogs.config
    }
  },
  methods: {
    nextPage() {
      if (this.currentStep < 2) {
        this.currentStep++
      } else {
        this.activityLogs.config = this.config
        this.$emit('setActivityLogs', this.activityLogs)
        this.$router.push('/dashboard/student/domaines/introspection/rencontres/etape-3')
      }
    },
    previousStep() {
      if (this.currentStep > 1) {
        this.currentStep--;
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

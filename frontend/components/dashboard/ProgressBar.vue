<template>
  <div class="ProgressBar">
    <h2 v-if="!isManager" class="title title--dash pt-16 sm:text-4xl">{{ title }}</h2>

    <div :class="{'mt-12': isLightVersion, 'mt-16': !isLightVersion, 'bg-white': !isManager, 'bg-blue-lightest': isManager}" class="flex w-full bg-white h-2 rounded-full relative">

      <div :class="step >= 1 ? 'bg-orange' : 'bg-transparent'"
           :id="'activity-' + nbActivitiesUndone"
           class="step step1 h-full rounded-full relative z-0">
        <div :class="step >= 1 ? 'bg-orange border-orange' : step == 0 ? isManager ? 'bg-blue-lightest border-orange' : 'bg-white border-orange' : isManager ? 'bg-blue-lightest border-blue-lightest' : 'bg-white border-white'"
             class="circle absolute pin-r w-5 h-5 bg-white border-solid border-2 border-white rounded-full" />
        <span v-if="!isLightVersion" class="right   sm:text-center sm:hidden text-smallest text-sm absolute pin-r uppercase text-center text-blue-lighter font-bold z-10 sm:text-xs">Connaissance de soi</span>
      </div>

      <div :class="step >= 1 ? 'bg-orange' : 'bg-transparent'"
           class="step step2 h-full rounded-full relative z-0">
        <div :class="step >= 1 ? 'bg-orange border-orange' : step == 2 ? isManager ? 'bg-blue-lightest border-orange' : 'bg-white border-orange' : isManager ? 'bg-blue-lightest border-blue-lightest' : 'bg-white border-white'"
             class="circle absolute pin-r w-5 h-5 bg-white border-solid border-2 border-white rounded-full" />
        <span v-if="!isLightVersion" class="right   sm:text-center sm:hidden text-smallest text-sm absolute pin-r uppercase text-center text-blue-lighter font-bold sm:text-xs">Exploration</span>
      </div>

      <div :class="step >= 2 ? 'bg-orange' : 'bg-transparent'"
           class="step step0 h-full rounded-full relative z-10">
        <div :class="step >= 2 ? 'bg-orange border-orange' : isManager ? 'bg-blue-lightest border-blue-lightest' : 'bg-white border-white'"
             class="bigCircle absolute pin-r w-8 h-8 border-solid border-2 rounded-full" />
        <span v-if="!isLightVersion"
              :class="step >= 2 ? 'text-orange' : 'text-blue-lighter'"
              class="category pin-r text-xl font-bold absolute uppercase text-center font-bold z-20 sm:text-xs">DOMAINES</span>
      </div>

      <div :class="step >= 3 ? 'bg-orange' : 'bg-transparent'"
           class="step step4 h-full rounded-full relative z-0">
        <div :class="step >= 3 ? 'bg-orange border-orange' : step == 2 ? isManager ? 'bg-blue-lightest border-orange' : 'bg-white border-orange' : isManager ? 'bg-blue-lightest border-blue-lightest' : 'bg-white border-white'"
             class="circle absolute pin-r w-5 h-5 bg-white border-solid border-2 border-white rounded-full" />
        <span v-if="!isLightVersion" class="right   sm:text-center sm:hidden text-smallest text-sm absolute pin-r uppercase text-center text-blue-lighter font-bold z-10 sm:text-xs">Connaissance de soi</span>
      </div>

      <div :class="step >= 4 ? 'bg-orange' : 'bg-transparent'"
           class="step step5 h-full rounded-full relative z-0">
        <div :class="step >= 4 ? 'bg-orange border-orange' : step == 3 ? isManager ? 'bg-blue-lightest border-orange' : 'bg-white border-orange' : isManager ? 'bg-blue-lightest border-blue-lightest' : 'bg-white border-white'"
             class="circle absolute pin-r w-5 h-5 bg-white border-solid border-2 border-white rounded-full" />
        <span v-if="!isLightVersion" class="right sm:text-center sm:hidden text-smallest absolute pin-r uppercase text-center text-blue-lighter font-bold sm:text-xs">Exploration</span>
      </div>

      <div :class="step >= 5 ? 'bg-orange' : 'bg-transparent'"
           class="step step3 h-full rounded-full relative z-0">
        <div :class="step >= 5 ? 'bg-orange border-orange' : isManager ? 'bg-blue-lightest border-blue-lightest' : 'bg-white border-white'"
             class="bigCircle absolute pin-r w-8 h-8 bg-white border-solid border-2 border-white rounded-full" />
        <span v-if="!isLightVersion"
              :class="step >= 5 ? 'text-orange' : 'text-blue-lighter'"
              class="category pin-r text-xl font-bold absolute uppercase text-center text-blue-lighter font-bold z-20 sm:text-xs">FILIÈRES</span>
      </div>

      <div :class="step >= 6 ? 'bg-orange' : 'bg-transparent'"
           class="step step4 h-full rounded-full relative z-0">
        <div :class="step >= 6 ? 'bg-orange border-orange' : isManager ? 'bg-blue-lightest border-blue-lightest' : 'bg-white border-white'"
             class="circle absolute pin-r w-5 h-5 bg-white border-solid border-2 border-white rounded-full" />
        <span v-if="!isLightVersion" class="right   sm:text-center sm:hidden text-smallest text-sm absolute pin-r uppercase text-center text-blue-lighter font-bold z-10 sm:text-xs">Connaissance de soi</span>
      </div>

      <div :class="step >= 7 ? 'bg-orange' : 'bg-transparent'"
           class="step step5 h-full rounded-full relative z-0">
        <div :class="step >= 7 ? 'bg-orange border-orange' : step == 6 ? isManager ? 'bg-blue-lightest border-orange' : 'bg-white border-orange' : isManager ? 'bg-blue-lightest border-blue-lightest' : 'bg-white border-white'"
             class="circle absolute pin-r w-5 h-5 bg-white border-solid border-2 border-white rounded-full" />
        <span v-if="!isLightVersion" class="right   sm:text-center sm:hidden text-smallest text-sm absolute pin-r uppercase text-center text-blue-lighter font-bold sm:text-xs">Exploration</span>
      </div>

      <div :class="step >= 8 ? 'bg-orange' : 'bg-transparent'"
           class="step step3 h-full rounded-full relative z-0">
        <div :class="step >= 8 ? 'bg-orange border-orange' : isManager ? 'bg-blue-lightest border-blue-lightest' : 'bg-white border-white'"
             class="bigCircle absolute pin-r w-8 h-8 bg-white border-solid border-2 border-white rounded-full" />
        <span v-if="!isLightVersion"
              :class="step >= 8 ? 'text-orange' : 'text-blue-lighter'"
              class="category pin-r text-xl font-bold absolute uppercase text-center text-blue-lighter font-bold z-20 sm:text-xs">Établissements</span>
      </div>

      <div :class="step >= 9 ? 'bg-orange' : 'bg-transparent'"
           class="step final-step h-full rounded-full relative z-0">
        <div :class="step >= 9 ? 'bg-orange border-orange' : isManager ? 'bg-blue-lightest border-blue-lightest' : 'bg-white border-white'"
             class="bigCircle absolute pin-r w-8 h-8 bg-white border-solid border-2 border-white rounded-full" />
        <span v-if="!isLightVersion"
              :class="step >= 9 ? 'text-orange' : 'text-blue-lighter'"
              class="category pin-r text-xl font-bold absolute uppercase text-center text-blue-lighter font-bold z-20 sm:text-xs">Parcours Sup.</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProgressBar',
  props: {
    step: {
      default: 1,
      type: Number
    },
    nbActivitiesUndone: {
      default: 8,
      type: Number
    },
    isLightVersion: {
      default: false,
      type: Boolean
    },
    isManager: {
      default: false,
      type: Boolean
    },
    title: {
      type: String,
      default: ''
    }
  }
}
</script>

<style lang="scss" scoped>
.step {
  .circle {
    transform: translate(25%, -30%);
  }
  .bigCircle {
    transform: translate(25%, -40%);
  }
  span {
    transform: translateY(2rem);
  }
  .right {
    transform: translate(50%, 2rem);
  }
  .category {
    transform: translate(45%, -250%);
  }
}
.step0 {
  width: 10%;
}
.step1,
.step2,
.step3,
.step4,
.step5 {
  width: 10%;
}

.final-step {
  width: 17%;
}

  .text-smallest {
    font-size: 10px;
  }

#activity-0 {
  background-color: #ff8f5e;
}

#activity-1 {
  background: linear-gradient(to left, #fff 2%, #ff8f5e 98%);
}

#activity-2 {
  background: linear-gradient(to left, #fff 16.6%, #ff8f5e 83.4%);
}

#activity-3 {
  background: linear-gradient(to left, #fff 33.3%, #ff8f5e 66.6%);
}
#activity-4 {
  background: linear-gradient(to left, #fff 50%, #ff8f5e 50%);
}
#activity-5 {
  background: linear-gradient(to left, #fff 66.6%, #ff8f5e 33.3%);
}
#activity-6 {
  background: linear-gradient(to left, #fff 83.4%, #ff8f5e 16.6%);
}
#activity-7 {
  background: linear-gradient(to left, #fff 98%, #ff8f5e 2%);
}
</style>

<template>
  <div class="flex flex-col xl:flex-row w-full xl:justify-between xl:items-center pb-4">
    <div class="xl:w-1/2">
      <div class="flex flex-col">
        <div class="flex pt-4 pb-3">
          <svg class="pr-2 w-8" width="12" height="14" viewBox="0 0 12 10" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3.39844 4.35156C3.375 4.32812 3.30469 4.30469 3.25781 4.30469C3.21094 4.30469 3.16406 4.32812 3.14062 4.35156L1.47656 6.01562L0.5625 5.10156C0.515625 5.07812 0.46875 5.05469 0.421875 5.05469C0.375 5.05469 0.304688 5.07812 0.28125 5.10156L0.0234375 5.38281C0 5.40625 -0.0234375 5.47656 -0.0234375 5.5C-0.0234375 5.54688 0 5.61719 0.0234375 5.64062L1.33594 6.95312C1.35938 6.97656 1.42969 7.02344 1.47656 7.02344C1.5 7.02344 1.57031 6.97656 1.59375 6.95312L3.65625 4.89062C3.67969 4.86719 3.72656 4.79688 3.72656 4.75C3.72656 4.72656 3.67969 4.65625 3.65625 4.63281L3.39844 4.35156ZM1.45312 8.5C1.05469 8.5 0.703125 8.85156 0.703125 9.25C0.703125 9.67188 1.05469 10 1.45312 10C1.875 10 2.20312 9.67188 2.20312 9.25C2.20312 8.85156 1.875 8.5 1.45312 8.5ZM3.39844 0.601562C3.375 0.578125 3.30469 0.554688 3.25781 0.554688C3.21094 0.554688 3.16406 0.578125 3.14062 0.601562L1.47656 2.26562L0.5625 1.35156C0.515625 1.32812 0.46875 1.30469 0.421875 1.30469C0.375 1.30469 0.304688 1.32812 0.28125 1.35156L0.0234375 1.63281C0 1.65625 -0.0234375 1.72656 -0.0234375 1.75C-0.0234375 1.79688 0 1.86719 0.0234375 1.89062L1.33594 3.20312C1.35938 3.22656 1.42969 3.27344 1.47656 3.27344C1.5 3.27344 1.57031 3.22656 1.59375 3.20312L3.65625 1.14062C3.67969 1.11719 3.72656 1.04688 3.72656 1C3.72656 0.976562 3.67969 0.90625 3.65625 0.882812L3.39844 0.601562ZM11.7891 8.875H4.66406C4.54688 8.875 4.47656 8.96875 4.47656 9.0625V9.4375C4.47656 9.55469 4.54688 9.625 4.66406 9.625H11.7891C11.8828 9.625 11.9766 9.55469 11.9766 9.4375V9.0625C11.9766 8.96875 11.8828 8.875 11.7891 8.875ZM11.7891 1.375H4.66406C4.54688 1.375 4.47656 1.46875 4.47656 1.5625V1.9375C4.47656 2.05469 4.54688 2.125 4.66406 2.125H11.7891C11.8828 2.125 11.9766 2.05469 11.9766 1.9375V1.5625C11.9766 1.46875 11.8828 1.375 11.7891 1.375ZM11.7891 5.125H4.66406C4.54688 5.125 4.47656 5.21875 4.47656 5.3125V5.6875C4.47656 5.80469 4.54688 5.875 4.66406 5.875H11.7891C11.8828 5.875 11.9766 5.80469 11.9766 5.6875V5.3125C11.9766 5.21875 11.8828 5.125 11.7891 5.125Z" fill="#FF9C9C"/>
          </svg>
          <div class="font-semibold lowercase exploration-name">{{ exploration.name }}</div>
        </div>
        <div class="flex flex-col xl:flex-row w-full items-center ms-small text-sm">
          <ProgressBar :currentStepExploration="currentStep" :exploration="true" :bgColor="'red'" class="ml-8 mr-2"/>
          <div v-if="!exploration.isDone && (new Date() < new Date(exploration.date))" class="w-full text-ms-gray-dark pt-3 xl:pt-0">à compléter le <span class="text-ms-red">{{ new Date(exploration.date).toLocaleDateString() }}</span></div>
          <div v-else-if="!exploration.isDone && (new Date() > new Date(exploration.date))" class="w-full text-ms-gray-dark pt-3 xl:pt-0">en retard de <span class="text-ms-red">{{ dayDiff }} {{ dayString || 'jours' }}</span></div>
          <div v-else class="w-full text-ms-gray-dark pt-3 xl:pt-0">
            complétée
          </div>
        </div>
      </div>
    </div>
    <div class="xl:w-1/2 pt-4 xl:pt-0 flex xl:justify-end">
      <button v-if="!exploration.isDone" class="button-outline-ms border-ms-red text-ms-red" @click="$router.push('/dashboard/college/student/formulaire/' + exploration.exploration_type_id + '?exploration=' + exploration.id)"> Compléter l'exploration</button>
      <button v-else class="button-outline-ms border-ms-red text-ms-red" @click="$router.push('/dashboard/college/student/formulaire/' + exploration.exploration_type_id + '?exploration=' + exploration.id)"> Revoir mes réponses</button>
    </div>
  </div>
</template>

<script>
import ProgressBar from '../middleSchool/ProgressBar'
import Button from '../../mixins/button'
export default {
  name: 'Exploration',
  components: { Button, ProgressBar },
  props: {
    exploration: {
      type: Object,
      default: function() {
        return {}
      }
    },
  },
  data() {
    return {
      currentStep: 1
    }
  },
  computed: {
    dayString() {
      if (this.dayDiff === 1) {
        return 'jour'
      }
    },
    dayDiff(d1, d2) {
      d1 = new Date(this.exploration.date)
      d2 = new Date()
      const one_day = 1000 * 60 * 60 * 24
      return Math.floor((d2 - d1) / one_day)
    }
  },
  created() {
    if (this.dayDiff >= 1 || this.exploration.isDone) {
      this.currentStep = 5
    } else if (this.dayDiff >= -20 && this.dayDiff < 0) {
      this.currentStep = 4
    } else if (this.dayDiff >= -40 && this.dayDiff <= -20) {
      this.currentStep = 3
    } else if (this.dayDiff >= -60 && this.dayDiff <= -40) {
      this.currentStep = 2
    } else if (this.dayDiff <= -80 && this.dayDiff <= -60) {
      this.currentStep = 1
    } else if (this.dayDiff < -80) {
      this.currentStep = 0
    }
  }
}
</script>

<template>
  <div id="activityId" class="flex flex-wrap relative border border-1 border-solid border-blue py-6 px-6 my-6 text-4xl font-bold text-blue ml-12">
    <div class="ml-8">{{ activity.name }}</div>
    <img :src="'/img/autoportrait/' + activity.slug + '.svg'" class="absolute pin-l pin-t w-24 h-24 illustration">
    <modal-print-simple v-if="activity.modal == 'simple'" :activity="activity" :userLog="userLog" class="flex w-full justify-center overflow-hidden"/>
    <modal-print-rencontre v-if="activity.modal == 'rencontres'" :activity="activity" :userLog="userLog" class="flex w-full justify-center overflow-hidden"/>
    <modal-print-triple v-if="activity.modal == 'triple'" :activity="activity" :userLog="userLog" class="flex w-full justify-center overflow-hidden"/>
  </div>
</template>

<script>
import ModalPrintSimple from './modals/ModalPrintSimple'
import ModalPrintRencontre from './modals/ModalPrintRencontre'
import ModalPrintTriple from './modals/ModalPrintTriple'
import { MODAL_AUTOPORTRAIT } from '~/config'
import _ from 'lodash'

export default {
  components: {
    ModalPrintSimple,
    ModalPrintRencontre,
    ModalPrintTriple
  },
  props: {
    activity: {
      type: Object,
      default: () => {
        return null
      }
    },
    userLog:  {
      type: Object,
      default: () => {
        return {}
      }
    },
  },
  data() {
    return {
      modalsAutoportrait: MODAL_AUTOPORTRAIT,
    }
  },
  created() {
      if(_.includes(this.modalsAutoportrait.modalRencontre, this.activity.slug)) {
        this.activity.modal = 'rencontres'
      }
      if(_.includes(this.modalsAutoportrait.modalTriple, this.activity.slug)) {
        this.activity.modal = 'triple'
      }
      if(_.includes(this.modalsAutoportrait.modalSimple, this.activity.slug)) {
        this.activity.modal = 'simple'
      }
  }
}
</script>

<style lang="scss" scoped>
.illustration {
  transform: translate(-30px, -30px);
}
</style>

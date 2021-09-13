<template>
  <div class="final fixed pin-l pint-t pin-b w-screen h-screen z-index-modal flex justify-center items-center px-3 sm:py-3">

    <div class="layer"/>

    <div class="onboardingCard h-56 w-1/4 sm:w-full bg-white pt-8 pb-4 rounded-lg flex flex-col items-center sm:my-3 relative">
      <div class="flex flex-col justify-center items-center w-full">
        <div class="text-blue">{{ text }}</div>
        <div class="font-bold text-blue pt-4 px-20 flex justify-center w-full text-center">{{ item[nameField] }}</div>
        <div class="flex flex-wrap w-full justify-center items-center pt-6 pb-2">
          <button class="button bg-blue" @click="deleteFeedback">Oui</button>
          <button class="button bg-red ml-4" @click="closeModal">Non</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Button from '../../../mixins/button'
  import SchoolInfo from './SchoolInfo'
  import _ from 'lodash'
  export default {
    name: 'ConfirmDialogModal',
    components: { Button },
    props: {
      item: {
        type: Object,
        default: function() {
          return {}
        }
      },
      nameField: {
        type: String,
        default: ''
      },
      text: {
        type: String,
        default: 'Êtes-vous certain de vouloir supprimer cet élement ?'
      },
      serviceName: {
        type: String,
        default: 'onisepFeedback'
      }
    },
    data() {
      return {}
    },
    methods: {
      closeModal() {
        this.$emit("closeModal")
      },
      async deleteFeedback() {
        await this.$api[this.serviceName].delete({ids: [this.item.id]})
        this.$emit('reloadFeedbacks')
        this.closeModal()
      }
    },
  }
</script>


<style lang="scss" scoped>
</style>

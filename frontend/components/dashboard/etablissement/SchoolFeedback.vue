<template>
  <div class="flex flex-col w-full">
    <div class="flex bg-blue rounded-t-lg justify-between items-center">
      <div class="text-3xl uppercase text-white font-bold p-10 pl-16">{{ feedback.school_name }}</div>
      <button v-if="!$store.state.auth.user.config.introspection_school_uais" class="bg-white button m-6 text-blue" @click="openConfirmModal">Supprimer</button>
    </div>
    <table class="table-auto shadow-md">
      <thead>
        <tr class="font-bold uppercase">
          <th class="w-1/4 px-4 py-6 bg-blue-dark text-blue-lightest border-solid border-r-2 border-black">Caractéristiques</th>
          <th class="w-1/4 px-4 py-2 bg-blue-dark text-blue-lightest border-solid border-r-2 border-black">Informations</th>
          <th class="w-1/4 px-4 py-2 bg-blue-dark text-blue-lightest">Mon avis</th>
        </tr>
      </thead>
      <tbody class="bg-blue-lightestBis">
        <tr>
          <td class="px-4 align-middle font-bold text-blue-dark border-solid border-r-1 border-gray-lighter border-opacity-25">Les contenus des enseignements et leur organisation</td>
          <td class="px-4 py-4 border-solid border-r-1 border-gray-lighter border-opacity-25">
            <textarea v-model="feedback.school_content.information" placeholder="Cet établissement ..." rows="8" class="w-full text-xl text-black placeholder-opacity-50 placeholder-gray-light" @blur="saveFeedback"/>
          </td>
          <td class="px-4 align-middle">
            <textarea v-model="feedback.school_content.feedback" placeholder="Je trouve que ..." rows="8" class="w-full text-xl text-black placeholder-opacity-50 placeholder-gray-light" @blur="saveFeedback"/>
          </td>
        </tr>
        <tr class="bg-gray-100">
          <td class="px-4 py-4 align-middle font-bold text-blue-dark border-solid border-r-1 border-gray-lighter border-opacity-25">Les compétences et connaissances attendues, communiquées par le ministère pour ce type de formation</td>
          <td class="px-4 py-4 align-middle border-solid border-r-1 border-gray-lighter border-opacity-25">
            <textarea v-model="feedback.skills_required.information" placeholder="Cet établissement ..." rows="8" class="w-full text-xl text-black placeholder-opacity-50 placeholder-gray-light" @blur="saveFeedback"/>
          </td>
          <td class="px-4 align-middle">
            <textarea v-model="feedback.skills_required.feedback" placeholder="Je trouve que ..." rows="8" class="w-full text-xl text-black placeholder-opacity-50 placeholder-gray-light" @blur="saveFeedback"/>
          </td>
        </tr>
        <tr>
          <td class="px-4 py-4 align-middle font-bold text-blue-dark border-solid border-r-1 border-gray-lighter border-opacity-25">Les perspectives en termes de poursuite d’études ou d’insertion professionnelle</td>
          <td class="px-4 py-4 align-middle border-solid border-r-1 border-gray-lighter border-opacity-25">
            <textarea v-model="feedback.perspectives.information" placeholder="Cet établissement ..." rows="8" class="w-full text-xl text-black placeholder-opacity-50 placeholder-gray-light" @blur="saveFeedback"/>
          </td>
          <td class="px-4 py-4">
            <textarea v-model="feedback.perspectives.feedback" placeholder="Je trouve que ..." rows="8" class="w-full  text-xl text-black placeholder-opacity-50 placeholder-gray-light" @blur="saveFeedback"/>
          </td>
        </tr>
      </tbody>
    </table>
    <confirm-dialog-modal v-if="modalOpen"
                          :item="feedback"
                          serviceName="onisepFeedback"
                          nameField="school_name"
                          text="Êtes-vous certain de vouloir supprimer cette école ?"
                          @reloadFeedbacks="reloadFeedbacks"
                          @closeModal="closeModal"/>
  </div>
</template>

<script>

  import Button from '../../../mixins/button'
  import ConfirmDialogModal from './confirmDialogModal'
  export default {
    name: 'SchoolFeedback',
    components: { ConfirmDialogModal, Button },
    props: {
      feedback: {
        type: Object,
        default: function() {
          return {}
        }
      }
    },
    data() {
      return {
        modalOpen: false,
      }
    },
    methods: {
      async saveFeedback() {
        await this.$api.onisepFeedback.update(this.feedback)
      },
      openConfirmModal() {
        this.modalOpen = true
      },
      closeModal() {
        this.modalOpen = false
      },
      reloadFeedbacks() {
        this.$emit('reloadFeedbacks')
      }
    }
  }
</script>

<style lang="scss" scoped>
  textarea::-webkit-input-placeholder {
    color: #B0B0B0;
  }

  textarea:-moz-placeholder { /* Firefox 18- */
    color: #B0B0B0;
  }

  textarea::-moz-placeholder {  /* Firefox 19+ */
    color: #B0B0B0;
  }

  textarea:-ms-input-placeholder {
    color: #B0B0B0;
  }

  textarea::placeholder {
    color: #B0B0B0;
  }
</style>

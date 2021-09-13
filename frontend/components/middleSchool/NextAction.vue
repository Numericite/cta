<template>
  <div class="nextAction w-full z-10">
    <div class="bg-white shadow-lg p-3 rounded-lg">
      <div :class="{
        'border-ms-green-light': entity === 'lead',
        'border-ms-red-light' : entity === 'exploration',
        'border-ms-blue-light': entity === 'module',
        'rounded-b-lg': (!action.explorations || action.explorations.length === 0)
      }" class="border-solid border-1 rounded-t-lg relative px-4 py-6 border-opacity-25">
        <div class="flex justify-start items-center pb-2">
          <div :class="{
            'bg-ms-green' : entity === 'lead',
            'bg-ms-red' : entity === 'exploration',
            'bg-ms-blue' : entity === 'module'
          }" class="w-1 h-1 rounded-full mx-2"/>
          <div :class="{
            'text-ms-green' : entity === 'lead',
            'text-ms-red' : entity === 'exploration',
            'text-ms-blue' : entity === 'module'
          }" class="action ms-subtitle">{{ entity === 'lead' ? 'Piste' : entity === 'module' ? 'Séquence' : entity }}</div>
          <div :class="{
            'bg-ms-green' : entity === 'lead',
            'bg-ms-red' : entity === 'exploration',
            'bg-ms-blue' : entity === 'module'
          }" class="w-1 h-1 rounded-full mx-2"/>
          <div v-if="entity === 'exploration' || entity === 'module'" :class="{
            'text-ms-green' : entity === 'lead',
            'text-ms-red' : entity === 'exploration',
            'text-ms-blue' : entity === 'module'
          }" class="mx-1 ms-subtitle">{{ action.countActions }} </div>
          <div v-if="entity === 'module'" class="text-ms-blue  ms-small">activités à compléter</div>
          <!--<div v-if="entity === 'exploration'" class="text-ms-red ms-small">À faire aprèe le module</div>-->
          <!--<div v-if="entity === 'lead'" class="text-ms-green ms-small">Complète ton hypothèse</div>-->
        </div>
        <div class="flex justify-between pl-2">
          <div class="flex flex-col min-w-5/6">
            <div class="ms-title-medium py-4 leading-tight text-5.2xl">{{ action.name || action.description }}</div>
            <div class="text-ms-gray-dark description text-2xl pt-2 leading-normal">{{ action.name ? action.description : 'Complète ta piste du ' + formatDate(action.created_date) | str_limit(200) }}</div>
          </div>
          <div class="flex flex-col min-w-1/6 justify-center text-right pr-4">
            <div class="flex w-full items-center justify-end">
              <div class="">
                <button :class="{'bg-ms-green': nextAction && entity === 'lead',
                                 'bg-ms-red' : nextAction && entity === 'exploration',
                                 'bg-ms-blue' : nextAction && entity === 'module',
                                 'border-ms-blue' : !nextAction && entity === 'module',
                                 'border-ms-red' : !nextAction && entity === 'exploration',
                                 'border-ms-green' : !nextAction && entity === 'lead'}" class="bg-white outline-none border-1 border-solid px-7 py-6 rounded-lg" @click="startActivity">
                  <svg :class="{'text-white' : nextAction, 'text-ms-blue' : !nextAction && entity === 'module', 'text-ms-red' : !nextAction && entity === 'exploration', 'text-ms-green' : !nextAction && entity === 'lead' }" width="12" height="10" viewBox="0 0 9 7" fill="none" xmlns="http://www.w3.org/2000/svg" class="fill-current">
                    <path d="M5.08594 1.27734L6.375 2.54688H0.59375C0.320312 2.54688 0.125 2.76172 0.125 3.01562V3.48438C0.125 3.75781 0.320312 3.95312 0.59375 3.95312H6.375L5.08594 5.24219C4.91016 5.41797 4.91016 5.73047 5.08594 5.90625L5.41797 6.23828C5.59375 6.41406 5.90625 6.41406 6.08203 6.23828L8.71875 3.58203C8.91406 3.40625 8.91406 3.11328 8.71875 2.9375L6.08203 0.28125C5.90625 0.105469 5.59375 0.105469 5.41797 0.28125L5.08594 0.613281C4.91016 0.789062 4.91016 1.10156 5.08594 1.27734Z"/>
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-if="action.explorations && action.explorations.length" class="min-w-100 border-ms-red-light bg-white min-h-100 border-solid border-b-1 border-l-1 border-r-1 rounded-b-lg relative p-5">
        <div class="ms-subtitle-bis w-full pt-4">{{ action.explorations.length }} exploration(s) en cours</div>
        <Exploration v-for="exploration in action.explorations" :key="exploration.id" :exploration="exploration"/>
      </div>
    </div>
  </div>
</template>

<script>
  import ProgressBar from '../middleSchool/ProgressBar'
  import Button from '../../mixins/button'
  import Exploration from '../../components/middleSchool/Exploration'
  export default {
    name: 'NextAction',
    components: { Button, ProgressBar, Exploration },
    props: {
      action: {
        type: Object,
        default: function() {
          return {}
        }
      },
      entity: {
        type: String,
        default: 'module'
      },
      nextAction: {
        type: Boolean,
        default: function() {
          return false
        }
      }
    },
    methods: {
      startActivity() {
        let link = ''

        switch (this.entity) {
          case 'module':
            link = '/dashboard/college/student/programme/module/' + this.action.id
            break
          case 'lead':
            link = '/dashboard/college/student/choix/' + this.action.id
            break
          case 'exploration':
            link = '/dashboard/college/student/programme/exploration/' + this.action.id
            break;
        }

        this.$router.push(link)
      }
    }
  }
</script>

<style scoped>
</style>

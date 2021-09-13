<template>
  <div :class="{'w-full' : row}" class="entity">
    <div class="bg-white p-3 rounded-lg">
      <div :class="[(entity.name === 'lead' ? 'border-ms-green-light' : entity.name === 'exploration' ? 'border-ms-red-light' : 'border-ms-blue-light '), row ? 'w-full flex items-center max-w-full' : 'max-w-xs' ]" class="min-w-100 bg-white min-h-100 border-solid border-1 rounded-lg relative">
        <div v-if="!row" class="bg-white absolute bloc-white w-32 p-10"/>
        <div v-if="!row" :class="[entity.name === 'lead' ? 'bg-ms-green' : entity.name === 'exploration' ? 'bg-ms-red' : 'bg-ms-blue']" class="dot-left absolute w-1 h-1 rounded-full"/>
        <img v-if="!row" :src="'/img/middle-school/' + entity.illustration + '.png'" class="absolute w-24 illustration">
        <img v-if="row" :src="'/img/middle-school/' + entity.illustration + '.png'" class="absolute w-24 illustration-row">
        <div v-if="!row" :class="[entity.name === 'lead' ? 'bg-ms-green' : entity.name === 'exploration' ? 'bg-ms-red' : 'bg-ms-blue']" class="dot-right bg-ms-green absolute w-1 h-1 rounded-full"/>
        <div class="px-6 w-full">
          <div :class="[row ? 'flex-row items-center justify-between w-full pl-20 pr-4' : 'flex-col pt-20']" class="flex">
            <div :class="{'w-2/3' : row}" class="flex flex-col">
              <div :class="[row ? 'justify-start' : 'justify-center']" class="flex items-center pt-6">
                <div :class="[entity.name === 'lead' ? 'bg-ms-green' : entity.name === 'exploration' ? 'bg-ms-red' : 'bg-ms-blue']" class="dot mr-2"/>
                <div :class="[entity.name === 'lead' ? 'text-ms-green' : entity.name === 'exploration' ? 'text-ms-red' : 'text-ms-blue']" class="ms-subtitle">{{ entity.title }}</div>
                <div :class="[entity.name === 'lead' ? 'bg-ms-green' : entity.name === 'exploration' ? 'bg-ms-red' : 'bg-ms-blue']" class="dot ml-2"/>
              </div>
              <p :class="[row ? 'py-2 text-left' : 'py-10 text-center']" class="font-plex text-xl text-ms-gray-dark leading-tight">
                {{ entity.description }}
              </p>
            </div>
            <div :class="{'w-1/3' : row}" class="flex flex-col">
              <div v-if="!newExploration && !noIndicator">
                <div :class="[row ? 'justify-end' : 'pb-6 justify-center']" class="flex text-center text-xl items-center text-ms-gray font-bold">
                  <div :class="{'text-ms-green': entity.name === 'lead', 'text-ms-red' : entity.name === 'exploration', 'text-ms-blue': entity.name === 'module'}" class="pr-2"> {{ entity.count }}</div>
                  <div class="text-ms-gray-dark">{{ entity.info }}</div>
                </div>
              </div>
              <div v-else-if="!noIndicator" class="w-full flex justify-center pb-4">
                <button v-if="entity.name === 'lead'" class="button-ms bg-ms-green">C'est parti !</button>
                <button v-if="entity.name === 'exploration'" class="button-ms bg-ms-red">Choisir une exploration</button>
                <button v-if="entity.name === 'module'" class="button-ms bg-ms-blue">C'est parti !</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ProgressBar from '../middleSchool/ProgressBar'
  import Button from '../../mixins/button'
  export default {
    name: 'Entity',
    components: { Button, ProgressBar },
    props: {
      entity: {
        type: Object,
        default: function() {
          return {}
        }
      },
      row: {
        type: Boolean,
        default: function() {
          return false
        }
      },
      noIndicator: {
        type: Boolean,
        default: function() {
          return false
        }
      },
      onboarding: {
        type: Boolean,
        default: function() {
          return false
        }
      },
      newExploration: {
        type: Boolean,
        default: function() {
          return false
        }
      }
    },
    data() {
      return {
      }
    },
  }
</script>

<style scoped>
  .bloc-white, .illustration {
    left: 50%;
    transform: translate(-50%, -50%);
  }
  .illustration-row {
    top: 50%;
    left: 0;
    transform: translate(-50%, -50%);
  }
  .dot-left {
    left: 33%;
    transform: translate(-50%, -60%);
  }
  .dot-right {
    right: 31%;
    transform: translate(-50%, -60%);
  }
</style>

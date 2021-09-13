<template>
  <div class="modal relative">
    <div class="relative h-full w-full">
      <div class="z-10 relative min-h-full w-full p-10 sm:pl-0 sm:h-full">
        <div class="relative min-h-full w-full sm:h-full">
          <div class="sm:flex sm:flex-col z-10 bg-white rounded-xl cardShadow modal-content min-h-full sm:h-full w-full p-10 sm:p-5 sm:pl-8 sm:pl-8 overflow-hidden">
            <div class="w-full text-blue text-center sm:text-left text-4xl font-extrabold sm:mt-12">
              {{ activity.name }}
            </div>
            <div class="flex sm:flex-wrap justify-around mt-12 sm:overflow-scroll sm:h-full">
              <div v-for="(selection, selectionKey) in activity.selections"
                   :key="selectionKey"
                   class="flex flex-col sm:flex-row sm:flex-wrap w-1/3 sm:w-full px-10 sm:px-0 sm:mb-5">
                <div class="flex justify-center sm:justify-start sm:w-16 sm:h-16 sm:mr-4">
                  <div v-if="!activity.config || !activity.config.weights || !activity.config.weights[selection.id]"
                       :class="{'bg-yellow': selectionKey === 0, 'bg-peach': selectionKey === 1, 'bg-topaze': selectionKey === 2}"
                       class=" flex justify-center items-center flex-none rounded-full sm:p-3 selection-icon">
                    <img :src="selection.img_path">
                  </div>

                  <radial-progress-bar v-if="activity.config && activity.config.weights && activity.config.weights[selection.id]"
                                       :diameter="90"
                                       :completed-steps="activity.config.weights[selection.id] / 8"
                                       :startColor="'#fff'"
                                       :stopColor="'#fff'"
                                       :strokeWidth="6"
                                       :innerStrokeColor="selectionKey === 0 ? '#ffc600' : selectionKey === 1 ? '#ff9c9c' : selectionKey === 2 ? '#1dd4b6' : ''"
                                       :total-steps="9">
                    <img :src="selection.img_path"
                         :class="{'bg-yellow': selectionKey === 0, 'bg-peach': selectionKey === 1, 'bg-topaze': selectionKey === 2}"
                         class="rounded-full p-4 selection-icon">

                  </radial-progress-bar>
                </div>
                <div class="flex items-center justify-center mt-5 sm:mt-2 sm:items-start sm:justify-start">
                  <span :class="{'text-center': !activity.config || !activity.config.weights || !activity.config.weights[selection.id]}"
                        class="text-blue font-bold sm:mt-1 sm:ml-8">{{ selection.text }}</span>
                </div>
                <div class="tags flex flex-wrap mt-4 sm:w-2/3 sm:mt-0 sm:ml-6">
                  <div v-for="(choice, key) in selection.choices"
                       :key="key"
                       :class="{'border-yellow text-yellow': selectionKey === 0, 'border-peach text-peach': selectionKey === 1, 'border-topaze text-topaze': selectionKey === 2}"
                       class="flex items-center justify-center rounded-full m-1 border border-solid uppercase text-xs py-1 px-2 font-semibold">
                    <svg v-if="selectionKey === 0"
                         width="12"
                         height="15"
                         class="stroke-current fill-current mr-1"
                         viewBox="0 0 100 100"
                         x="0px"
                         y="0px">
                      <path fill="#FFFFFF"
                            d="M50,94,9.3,53.3A27.71,27.71,0,0,1,48.49,14.11L50,15.63l1.51-1.52A27.71,27.71,0,0,1,90.7,53.3Z"
                            y="115"
                            stroke="#ffc600"
                            stroke-width="10" />
                    </svg>
                    <svg v-if="selectionKey === 1"
                         width="12"
                         height="15"
                         class="stroke-current fill-current mr-1"
                         viewBox="0 0 100 100"
                         x="0px"
                         y="0px">
                      <path fill="#FFFFFF"
                            d="M50,94,9.3,53.3A27.71,27.71,0,0,1,48.49,14.11L50,15.63l1.51-1.52A27.71,27.71,0,0,1,90.7,53.3Z"
                            y="115"
                            stroke="#ff9c9c"
                            stroke-width="10" />
                    </svg>
                    <svg v-if="selectionKey === 2"
                         width="12"
                         height="15"
                         class="stroke-current fill-current mr-1"
                         viewBox="0 0 100 100"
                         x="0px"
                         y="0px">
                      <path fill="#FFFFFF"
                            d="M50,94,9.3,53.3A27.71,27.71,0,0,1,48.49,14.11L50,15.63l1.51-1.52A27.71,27.71,0,0,1,90.7,53.3Z"
                            y="115"
                            stroke="#1dd4b6"
                            stroke-width="10" />
                    </svg>
                    {{ choice.text }}
                  </div>
                </div>
              </div>
            </div>
            <div v-if="!isManager"
                 class="flex sm:flex-row text-right sm:text-center w-full mt-12">
              <nuxt-link :to="link"
                         class="ml-auto">
                <button class="button button--blue mt-4 ">Recommencer l'activité</button>
              </nuxt-link>
            </div>
            <div class="absolute close p-4 pin-t pin-r cursor-pointer"
                 @click="$emit('close', 'activity' + activity.num)">

              <svg xmlns="http://www.w3.org/2000/svg"
                   width="32"
                   height="32"
                   viewBox="0 0 32 32">
                <g fill="none"
                   fill-rule="nonzero"
                   stroke="#432CB4"
                   transform="translate(1 1)">
                  <circle cx="15"
                          cy="15"
                          r="15"
                          stroke-width=".6" />
                  <g stroke-linecap="round"
                     stroke-width="1.2">
                    <path d="M9.6 9.6l11.02 10.835M20.62 9.6L9.6 20.435" />
                  </g>
                </g>
              </svg>

            </div>
          </div>
        </div>
      </div>
      <div :class="'bg-' + bgColor"
           class="z-0 absolute rounded-xl cardShadow h-4/5 w-4/5 pin-t pin-r sm:w-screen sm:fixed" />
    </div>
  </div>
</template>

<script>

import _ from 'lodash'

export default {
  name: 'AutoportraitModalTriple',
  props: {
    isManager: {
      type: Boolean,
      default: () => {
        return false
      }
    },
    activity: {
      type: Object,
      default: () => {
        return {
          name: 'Name'
        }
      }
    },
    link: {
      type: String,
      default: '/'
    },
    bgColor: {
      type: String,
      default: 'yellow'
    }
  },
  created() {
    if ( this.activity.selections ) {
      const activity = this.activity
      if (activity.choices) {
        this.activity.selections.forEach(function(selection, index) {
          selection.choices = activity.choices.filter(function(choice) {
            return choice.selection_id === selection.id
          })
        })
      }

      const orderedArray = []
      this.activity.selection_ids.forEach( function( selection_id, index ) {
        orderedArray[index] = _.find( activity.selections, { id: selection_id } )
      } )
      this.activity.selections = orderedArray
    }
  }
}
</script>

<style lang="scss" scoped>
.cardShadow {
  box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
}

.selection-icon {
  @media (min-width: 640px) {
    width: 70px;
    height: 70px;
  }
  @media (max-width: 640px) {
    width: 55px;
    height: 55px;
  }
}

.modal {
  @media (min-width: 640px) {
    z-index: 20;
    width: 100%;
  }
  @media (max-width: 640px) {
    z-index: 99999;
    position: fixed;
    top: 11%;
    bottom: 14%;
    width: 100%;
    height: auto;

    .modal-content {
      height: 64vh;
      overflow-y: scroll;
    }
  }
  .tags {
    @media (max-width: 640px) {
      transform: translateX(70px);
    }
  }
}
</style>

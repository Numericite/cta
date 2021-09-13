<template>
  <div class="modal relative">
    <div class="relative h-full w-full">
      <div class="z-10 relative min-h-full w-full p-10 sm:pl-0 ">
        <div class="relative min-h-full w-full">
          <div class="z-10 bg-white rounded-xl cardShadow modal-content min-h-full w-full p-10 sm:p-5">
            <div class="w-full text-blue text-center sm:text-left text-4xl font-extrabold sm:mt-12">
              {{ activity.name }}
            </div>
            <div class="flex sm:flex-row flex-wrap mt-12 sm:overflow-scroll">
              <div v-for="(choice, choiceKey) in activity.choices"
                   :key="choiceKey"
                   :class="{'w-1/4': activity.choices.length % 4 === 0, 'w-1/3': activity.choices.length % 3 === 0}"
                   class="flex-initial mb-12 px-10  sm:px-0 sm:mb-5 sm:w-1/2">
                <div class="flex justify-center sm:justify-start sm:w-16 sm:h-16 sm:mr-4">
                  <img v-if="choice.icon_path || choice.img_path"
                       :src="choice.icon_path || choice.img_path"
                       :class="{'bg-orange': choiceKey === 0, 'bg-purple': choiceKey === 1, 'bg-blue-sky': choiceKey === 2, 'bg-yellow': choiceKey === 3, 'bg-peach': choiceKey === 4, 'bg-topaze': choiceKey === 5, 'bg-emerald': choiceKey === 6, 'bg-green': choiceKey === 7}"
                       class="rounded-full p-4 choice-icon sm:mx-auto">
                  <img v-if="!choice.img_path"
                       :class="{'bg-orange': choiceKey === 0, 'bg-purple': choiceKey === 1, 'bg-blue-sky': choiceKey === 2, 'bg-yellow': choiceKey === 3, 'bg-peach': choiceKey === 4, 'bg-topaze': choiceKey === 5, 'bg-emerald': choiceKey === 6, 'bg-green': choiceKey === 7}"
                       src="~/static/icons/check.svg"
                       class="rounded-full p-4 choice-icon sm:mx-auto">
                </div>
                <div class="flex items-center justify-center mt-5 sm:mt-2 sm:items-start sm:justify-start">
                  <span class="text-blue text-center font-bold sm:mt-1 sm:mx-auto">{{ choice.text }}</span>
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
export default {
  name: 'AutoportraitModalSimple',
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
  }
}
</script>

<style lang="scss" scoped>
.cardShadow {
  box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
}

.choice-icon {
  width: 70px;
  height: 70px;
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

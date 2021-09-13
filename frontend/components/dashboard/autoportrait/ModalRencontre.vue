<template>
  <div class="modal relative">
    <div class="relative h-full w-full">
      <div class="z-10 relative min-h-full w-full p-10 sm:pl-0 ">
        <div class="relative min-h-full w-full">
          <div class="z-10 bg-white rounded-xl cardShadow modal-content min-h-full w-full p-10 sm:p-5">
            <div class="w-full text-blue text-center sm:text-left text-4xl font-extrabold sm:mt-12">
              {{ activity.name }}
            </div>
            <div v-if="activity.config && activity.config.domainsLiked" class="flex sm:flex-wrap justify-around mt-12 sm:overflow-scroll sm:h-full">
              <div class="flex flex-col sm:flex-row sm:flex-wrap w-1/2 sm:w-full px-10 sm:px-0 sm:mb-5">
                <div class="flex justify-center sm:justify-start sm:w-16 sm:h-16 sm:mr-4">
                  <div class="flex bg-topaze justify-center items-center flex-none rounded-full p-4 sm:p-3 selection-icon">
                    <img src="~/assets/img/activity-6-loupe.svg">
                  </div>
                </div>
                <div class="w-full text-center mt-6">
                  <span class="text-blue font-bold text-xl leading-normal">
                    Les secteurs qui m'intéressent :<br>
                    <p>{{ activity.config.domainsLiked.text }}</p>
                  </span>
                </div>
              </div>
              <div class="flex flex-col sm:flex-row sm:flex-wrap w-1/2 sm:w-full px-10 sm:px-0 sm:mb-5">
                <div class="flex justify-center sm:justify-start sm:w-16 sm:h-16 sm:mr-4">
                  <div class="flex bg-yellow justify-center items-center flex-none rounded-full p-4 sm:p-3 selection-icon">
                    <img src="~/assets/img/activity-6-loupe.svg">
                  </div>
                </div>
                <div class="w-full text-center mt-6">
                  <span class="text-blue font-bold text-xl leading-normal">
                    Les métiers qui m'intéressent :<br>
                    <p>{{ activity.config.jobsLiked.text }}</p>
                  </span>
                </div>
              </div>
            </div>
            <div v-else class="text-center mt-12">
              <p class="text-3xl"><img src="~/assets/img/domain_ban.svg" class="w-4 h-4"> Je n'ai pas eu de contact avec le monde du travail</p>
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
  name: 'AutoportraitModalRencontre',
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

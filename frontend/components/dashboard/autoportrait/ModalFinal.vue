<template>
  <div class="modal relative">
    <div class="relative h-full w-full">
      <div class="z-10 relative min-h-full w-full p-10 sm:pl-0 ">
        <div class="relative min-h-full w-full">
          <div class="z-10 bg-white rounded-xl cardShadow modal-content min-h-full w-full p-10 sm:p-5 sm:pl-8">
            <div class="w-full text-blue text-center sm:text-left text-4xl font-extrabold mb-8 sm:mt-12">
              {{ activity.name }}
            </div>
            <div class="w-1/5 inline-block sm:w-full">
              <div v-for="(choice, key) in activity.choices" :key="key" class="mb-8">
                <div class="inline-block">
                  <img :src="choice.img_path" :class="{'bg-yellow': key === 0, 'bg-peach': key === 1, 'bg-topaze': key === 2}" class="w-20 h-20 rounded-full p-4">
                </div>
                <div class="inline-block align-top ml-4">
                  <span :class="{'text-yellow': key === 0, 'text-peach': key === 1, 'text-topaze': key === 2}" class="align-top text-5xl font-bold mb-8">{{ key + 1 }}</span><br>
                  <span class="align-top text-blue font-bold leading-normal">
                    J'aime :<br>{{ choice.name }}
                  </span>
                </div>
              </div>
            </div>
            <div class="w-1/2 inline-block align-top sm:w-full">
              <div v-for="(domain, key) in activity.selections" :key="key" class="card inline-block items-center rounded-full self-start p-3 px-6 mr-3 mt-3 bg-blue-lightest pr-20 relative">
                <p class="text-3xl font-semibold text-blue mb-0">{{ domain.name }}</p>
                <input id="t"
                       class="cursor-default"
                       type="checkbox"
                       disabled
                       checked>
                <label for="t"
                       class="cursor-default cursor-custom ml-2 mb-6 ml-3" />
              </div>
            </div>
            <div class="w-1/5 inline-block align-bottom sm:hidden">
              <img class="w-full" src="~/assets/img/autoportrait/final.png">
            </div>
            <div class="absolute close p-4 pin-t pin-r cursor-pointer"
                 @click="$emit('close', 'final')">

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
            <div v-if="!isManager"
                 class="text-right sm:text-center w-full mt-12">
              <nuxt-link :to="link"
                         class="ml-auto">
                <button class="button button--blue mt-4 ">Recommencer l'activité</button>
              </nuxt-link>
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
    domains: {
      type: Array,
      default: () => {
        return []
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
    const chapters = this.$store.state.auth.user.course.chapters
    let domainStore = _.find(chapters, { slug: 'activity-area' })

    this.activity.selections = _.filter(this.domains, domain => {
      return _.includes(domainStore.domain_ids, domain.id)
    })
  }
}
</script>

<style lang="scss" scoped>
.cardShadow {
  box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
}

.selection-icon {
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

  .cursor-custom {
    position: absolute;
    top: 0.5em;
    transform: translateY(-50%);
    right: 0;

    &:before {
      background-color: #c8d0ff;
    }
  }
}
</style>

<template>
  <div class="flex flex-wrap">
    <div v-if="userLog" class="flex flex-wrap">
      <div class="flex justify-around mt-12">
        <div v-for="(selection, selectionKey) in userLog.selections"
             :key="selectionKey"
             class="flex flex-col items-center min-h-100 w-1/3 px-10 ">
          <div class="flex justify-center">
            <div v-if="!userLog.config || !userLog.config.weights || !userLog.config.weights[selection.id]"
                 :class="{'bg-yellow': selectionKey === 0, 'bg-peach': selectionKey === 1, 'bg-topaze': selectionKey === 2}"
                 class=" flex justify-center items-center flex-none rounded-full selection-icon w-16 h-16 p-2">
              <img :src="selection.img_path" class="p-2">
            </div>

            <radial-progress-bar v-if="userLog.config && userLog.config.weights && userLog.config.weights[selection.id]"
                                 :diameter="90"
                                 :completed-steps="userLog.config.weights[selection.id] / 8"
                                 :startColor="'#fff'"
                                 :stopColor="'#fff'"
                                 :strokeWidth="6"
                                 :innerStrokeColor="selectionKey === 0 ? '#ffc600' : selectionKey === 1 ? '#ff9c9c' : selectionKey === 2 ? '#1dd4b6' : ''"
                                 :total-steps="9">
              <img :src="selection.img_path"
                   :class="{'bg-yellow': selectionKey === 0, 'bg-peach': selectionKey === 1, 'bg-topaze': selectionKey === 2}"
                   class="rounded-full p-2 selection-icon">

            </radial-progress-bar>
          </div>
          <div class="flex items-center justify-center mt-5 ">
            <span :class="{'text-center': !userLog.config || !userLog.config.weights || !userLog.config.weights[selection.id]}"
                  class="text-blue text-base font-bold ">{{ selection.text }}</span>
          </div>
          <div class="tags flex flex-wrap mt-4">
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
    </div>
  </div>
</template>

<script>

export default {
  props: {
    activity:  {
      type: Object,
      default: () => {
        return null
      }
    },
    userLog: {
      type: Object,
      default: () => {
        return {}
      }
    },
  },
  created() {
  }
}
</script>

<style lang="scss" scoped>
</style>

<template>
  <div class="flex flex-wrap mt-6 w-full justify-center">
    <div v-if="userLog" class="flex flex-wrap">
      <div v-for="(choice, choiceKey) in userLog.choices"
           :key="choiceKey"
           :class="{'w-1/4': userLog.choices.length % 4 === 0, 'w-1/3': userLog.choices.length % 3 === 0, 'mb-8': choiceKey < 4, 'mb-2': choiceKey >= 4}"
           class="flex-initial px-10 ">
        <div class="flex justify-center">
          <img v-if="choice.icon_path || choice.img_path"
               :src="choice.icon_path || choice.img_path"
               :class="{'bg-orange': choiceKey === 0, 'bg-purple': choiceKey === 1, 'bg-blue-sky': choiceKey === 2, 'bg-yellow': choiceKey === 3, 'bg-peach': choiceKey === 4, 'bg-topaze': choiceKey === 5, 'bg-emerald': choiceKey === 6, 'bg-green': choiceKey === 7}"
               class="rounded-full w-12 min-w-50 min-h-50 h-12 p-4 choice-icon">
          <img v-if="!choice.img_path"
               :class="{'bg-orange': choiceKey === 0, 'bg-purple': choiceKey === 1, 'bg-blue-sky': choiceKey === 2, 'bg-yellow': choiceKey === 3, 'bg-peach': choiceKey === 4, 'bg-topaze': choiceKey === 5, 'bg-emerald': choiceKey === 6, 'bg-green': choiceKey === 7}"
               src="~/static/icons/check.svg"
               class="rounded-full w-12 min-w-50 min-h-50 h-12 p-4 choice-icon">
        </div>
        <div class="flex items-center justify-center mt-5">
          <span class="text-sm text-blue text-center font-bold">{{ choice.text }}</span>
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
    userLog:  {
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

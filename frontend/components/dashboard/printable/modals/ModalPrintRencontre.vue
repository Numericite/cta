<template>
  <div class="flex flex-wrap w-full">
    <div v-if="userLog" class="flex flex-wrap">
      <div v-if="userLog.config && userLog.config.domainsLiked" class="flex justify-around mt-12 ">
        <div class="flex flex-col pr-4 ">
          <div class="flex justify-center">
            <div class="flex bg-topaze justify-center items-center flex-none rounded-full p-4 selection-icon w-50 lg:w-auto">
              <img src="~/assets/img/activity-6-loupe.svg">
            </div>
          </div>
          <div class="text-center mt-6 min-w-110">
            <span class="text-blue font-bold text-xl leading-normal">
              Les secteurs qui m'intéressent :<br>
              <p>{{ userLog.config.domainsLiked.text }}</p>
            </span>
          </div>
        </div>
        <div class="flex flex-col">
          <div class="flex justify-center ">
            <div class="flex bg-yellow justify-center items-center flex-none rounded-full p-4 selection-icon w-50 lg:w-auto">
              <img src="~/assets/img/activity-6-loupe.svg">
            </div>
          </div>
          <div class="text-center mt-6 min-w-110">
            <span class="text-blue font-bold text-xl leading-normal">
              Les métiers qui m'intéressent :<br>
              <p>{{ userLog.config.jobsLiked.text }}</p>
            </span>
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

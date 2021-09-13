<template>
  <div class="todo-card flex bg-white">

    <div class="card__left w-1/4 flex items-center justify-center">
      <picture>
        <source :srcset="'../../img/activities/activity-' + activity.num + '.png'"
                type="image/png">
        <img :src="'../../img/activities/activity-' + activity.num + '.png'"
             alt="Activité numéro 1">
      </picture>
    </div>

    <div class="w-3/4 flex flex-col px-6 py-10">
      <h4 class="text-blue text-2xl font-extrabold sm:text-2xl">{{ activity.name }}</h4>
      <h5 class="text-blue text-lg mt-3 sm:text-base">Activité numéro {{ activity.num }}</h5>
      <p class="mt-2 sm:text-sm">
        {{ activity.desc }}
      </p>
      <span v-if="activity.estimated_time" class="text-blue-lighter text-sm font-bold sm:text-xs flex items-center">
        {{ activity.estimated_time }} min
        <img src="~/static/icons/timer.svg"
             alt="horloge"
             class="ml-2">
      </span>
    </div>

  </div>
</template>

<script>
export default {
  name: 'ToDoCard',
  props: {
    activity: {
      type: Object,
        default: function() {
        return { type: null }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.todo-card {
  border-radius: 12px;
  box-shadow: 0 2px 40px 0 rgba(40, 21, 134, 0.2);
  .card__left {
    border-top-left-radius: 12px;
    border-bottom-left-radius: 12px;
    g {
      fill: #fff;
    }
  }
}
</style>

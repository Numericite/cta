<template>
  <nuxt-link :to="activity.status_name === 'open' ? '/dashboard/student/domaines/introspection/' + activity.slug + '?id=' + activity.id : '#'"
             :class="{'bg-white': activity.id !== 'final', 'bg-blue': activity.id === 'final'}"
             class="activityCard p-10 flex flex-col items-center relative">

    <div v-if="activity.id === 'final' && activity.status_name === 'closed'" class="w-full h-full text-white font-bold text-xl text-center absolute pin-t pin-l p-10 disabled-activity">
      <span class="disabled-activity-text w-full p-10 leading-normal">Finis toutes les autres activités pour accéder à "Mes premières pistes"</span>
    </div>

    <picture v-if="activity.id !== 'final'">
      <source :srcset="'../../../img/activities/activity-' + activity.num + '.png'"
              class="img-activity"
              type="image/png">
      <img :src="'../../../img/activities/activity-' + activity.num + '.png'"
           :alt="'Activité numéro ' + activity.num"
           class="img-activity">
    </picture>

    <picture v-if="activity.id === 'final'">
      <source :srcset="'../../../img/activities/activity-final.png'"
              class="img-activity"
              type="image/png">
      <img :src="'../../../img/activities/activity-final.png'"
           :alt="'Activité finale'"
           class="img-activity">
    </picture>

    <div class="flex mt-4 text-center">
      <h4 :class="{'text-blue': activity.id !== 'final', 'text-white': activity.id === 'final'}" class="ml-3 text-xl font-extrabold">{{ activity.name }}</h4>
    </div>

    <p :class="{'text-blue': activity.id !== 'final', 'text-white': activity.id === 'final'}" class="mt-3 text-justify max-w-full"
       v-html="activity.desc" />

    <div v-if="activity.estimated_time" class="flex w-full mt-8 items-center justify-between">
      <div class="flex items-center">
        <span :class="{'text-blue-lighter': activity.id !== 'final', 'text-white': activity.id === 'final'}" class="text-sm">{{ activity.estimated_time }} min</span>
        <img src="~/static/icons/clock.svg"
             alt="horloge"
             class="ml-2">
      </div>
      <img src="~/static/icons/activity.svg"
           alt="illustration d'une activité"
           class="w-10 h-10">
    </div>

  </nuxt-link>
</template>

<script>
export default {
  name: 'ActivityCard',
  props: {
    activity: {
      type: Object,
      default: function () {
        return { activity: null }
      }
    }
  }
}
</script>


<style lang="scss">
  .activityCard {
    box-shadow: 0 2px 40px 0 rgba(40, 21, 134, 0.09);
    border-radius: 10px;

    & > picture {
      height: 20em;
      position: relative;
      width: 100%;

      img {
        position: relative;
        top: 50%;
        transform: translateY(-50%);
        height: 100%;
        width : auto;
        margin: auto;
        display: block;
      }
    }
  }

  .disabled-activity {
    border-radius: 10px;
    z-index: 101;
    background-color: rgba(0, 0, 0, 0.8);

    &-text {
      position: absolute;
      top: 50%;
      transform:translateY(-50%)translateX(-50%);
    }
  }
</style>

<template>
  <div class="entity w-full bg-white z-10">
    <div class="bg-white shadow-lg p-3 rounded-lg">
      <div class="min-w-100 border-ms-green-light border-opacity-50 bg-white border-solid border-1 rounded-t-lg relative">
        <div class="py-2 w-full">
          <div class="flex flex-row items-center justify-between w-full px-6 py-2">
            <div class="flex flex-col w-2/3">
              <div class="flex py-6">
                <div class="text-3xl font-bold">{{ lead.description }}</div>
              </div>
            </div>
            <div class="flex flex-col xl:w-1/5 pl-10">
              <button v-if="!lead.choice_ease" class="button-ms bg-ms-green" @click="$router.push('/dashboard/college/student/choix/' + lead.id)">Compléter</button>
              <div v-if="lead.choice_ease" class="flex flex-col text-right justify-end w-full items-end">
                <img src="~/static/icons/check.png" class="w-4 pb-3">
                <div class="text-ms-gray-dark font-bold text-sm">complété le <span class="text-ms-green">{{ new Date(lead.updated_date).toLocaleDateString() }}</span></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <ChoiceDetails v-if="lead.choice_ease" :lead="lead" class="border-ms-green-light bg-white border-solid border-b-1 border-l-1 border-r-1 rounded-b-lg relative p-5"/>
    </div>
  </div>
</template>

<script>
import Button from '../../mixins/button'
import ChoiceDetails from './ChoiceDetails'
export default {
  name: 'ChoiceReview',
  components: { ChoiceDetails, Button },
  props: {
    lead: {
      type: Object,
      default: function() {
        return {}
      }
    },
  },
  methods: {
    createExploration() {
      this.$emit('createExploration')
    }
  },
}
</script>

<style scoped>
.img-container{
  left: 0;
  top:50%;
  transform: translate(-50%, -50%);
}
.dot-left {
  left: 50%;
  top:0;
  transform: translate(-65%, 0%);
  width: 3px;
  height: 3px;
}
.dot-right {
  left: 50%;
  bottom: -100px;
  transform: translate(-65%, 0%);
  width: 3px;
  height: 3px;
}
.bloc-white {
  left: 50%;
  top:50%;
  transform: translate(-50%, -50%);
}
</style>

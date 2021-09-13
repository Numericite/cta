<template>
  <div class="entity w-full bg-white z-10">
    <div class="bg-white shadow-lg p-3 rounded-lg">
      <div class="min-w-100 border-ms-green-light border-opacity-50 bg-white min-h-100 border-solid border-1 rounded-t-lg relative">
        <div class="absolute img-container w-24 h-32">
          <div class="bg-white w-4 h-32 absolute bloc-white"/>
          <img :src="'/img/middle-school/step3.png'" class="absolute pt-5">
          <div class="relative">
            <div class="dot-left bg-ms-green-light absolute rounded-full"/>
            <div class="dot-right bg-ms-green-light absolute rounded-full"/>
          </div>
        </div>
        <div class="py-2 w-full">
          <div class="flex flex-col xl:flex-row items-center justify-between w-full px-6">
            <div class="flex flex-col xl:w-2/3 pl-16">
              <div class="flex flex-col pt-6">
                <div class="flex justify-start items-center pb-4">
                  <div class="dot mr-2 bg-ms-green"/>
                  <div class="ms-subtitle text-ms-green">Tes pistes</div>
                  <div class="dot ml-2 bg-ms-green"/>
                </div>
                <div class="text-3xl font-bold">{{ choice.title }}</div>
              </div>
              <p class=" pt-2 text-left text-xl text-ms-gray-dark leading-loose break-all">
                {{ choice.description }}
              </p>
            </div>
            <div class="flex flex-col xl:w-1/5 px-10">
              <button class="button-ms text-ms-green bg-white border-solid border-1 border-ms-green hover:bg-ms-green hover:text-white" @click="$router.push('/dashboard/college/student/choix/' + choice.id)">Commencer</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProgressBar from '../middleSchool/ProgressBar'
import Button from '../../mixins/button'
export default {
  name: 'ActivityMs',
  components: { Button, ProgressBar },
  props: {
    choice:  {
      type: Object,
      default: function() {
        return {}
      }
    }
  },
  data() {
    return {
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

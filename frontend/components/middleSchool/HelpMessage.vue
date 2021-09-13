<template>
  <div class="helpMessage w-full">
    <div class="min-w-224 border-solid border-1 border-ms-orange rounded-lg relative bg-white py-6 pl-24 pr-10 ">
      <div class="text-ms-orange font-bold text-2xl pb-4 font-plex">{{ helpMessage.title }}</div>
      <p class="text-ms-orange text-justify m-0 text-2xl leading-loose font-plex">{{ helpMessage.message }}</p>
      <div class="bg-white absolute bloc-white w-24 h-32">
        <img src="/img/middle-school/Bubble.png" class="absolute pt-5">
        <div class="relative">
          <div class="dot-left bg-ms-orange absolute w-1 h-1 rounded-full"/>
          <div class="dot-right bg-ms-orange absolute w-1 h-1 rounded-full"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'HelpMessage',
    props: {
      helpMessage: {
        type: Object,
        default: function() {
          return {}
        }
      }
    },
  }
</script>

<style scoped>
  .bloc-white {
    left: 0;
    top:50%;
    transform: translate(-50%, -50%);
  }
  .dot-left {
    left: 50%;
    top:0;
    transform: translate(-65%, 0%);
  }
  .dot-right {
    left: 50%;
    bottom: -100px;
    transform: translate(-65%, 0%);
  }
</style>

<template>
  <div :class="[row ? 'w-1/2' : 'flex-col']" class="badge flex justify-center items-center">
    <div :class="[row ? 'h-24 w-24' : 'h-40 w-42']" class="flex items-center justify-center rounded-full shadow-sm bg-white">
      <div :class="[(badge.illustration === 'appleBadge' ? 'border-ms-blue' : badge.illustration === 'earBadge' ? 'border-yellow' : 'border-ms-gray'), row ? 'items-center h-22 w-22' : 'h-38 w-38']" class="flex justify-center rounded-full border-solid border-1 relative">
        <div v-if="!row" class="absolute flex items-center justify-center rounded-full h-10 w-10 -mt-5 bg-white">
          <svg :class="[badge.illustration === 'appleBadge' ? 'text-ms-blue' : badge.illustration === 'earBadge' ? 'text-yellow' : 'text-ms-gray']" width="18" height="24" viewBox="0 0 18 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path class="stroke-current" d="M9.9602 7.2L4.2002 13.6M9.9602 7.2L14.4402 9.12L17.0002 14.24M9.9602 7.2L11.2402 12.96M4.2002 13.6L8.0402 21.92L12.5202 23.2M4.2002 13.6L9.3202 16.8M12.5202 23.2L17.0002 14.24M12.5202 23.2L9.3202 16.8M17.0002 14.24L11.2402 12.96M11.2402 12.96L9.3202 16.8" stroke="#AEAAC2" stroke-width="0.8" stroke-linejoin="round"/>
            <path class="stroke-current" d="M1 7.2L2.6 8.8" stroke="#AEAAC2" stroke-width="0.8" stroke-linecap="round"/>
            <path class="stroke-current" d="M5 2.4L5.8 5.6" stroke="#AEAAC2" stroke-width="0.8" stroke-linecap="round"/>
            <path class="stroke-current" d="M10.5998 0.800003L9.7998 4" stroke="#AEAAC2" stroke-width="0.8" stroke-linecap="round"/>
          </svg>
        </div>
        <div v-if="!row" :class="[badge.illustration === 'appleBadge' ? 'bg-ms-blue' : badge.illustration === 'earBadge' ? 'bg-yellow' : 'bg-ms-gray']" class="dot-left absolute w-1 h-1 mt-1 rounded-full"/>
        <div v-if="!row" :class="[badge.illustration === 'appleBadge' ? 'bg-ms-blue' : badge.illustration === 'earBadge' ? 'bg-yellow' : 'bg-ms-gray']" class="dot-right absolute w-1 h-1 mt-1 rounded-full"/>
        <div class="flex items-center">
          <img :src="'/img/middle-school/' + badge.illustration + '.png'" :class="[badge.illustration === 'lockBadge' ? 'h-4' : 'h-8']">
        </div>
      </div>
    </div>
    <div :class="[row ? 'flex-row pl-11' : 'flex-col text-center']" class="flex">
      <div :class="{'opacity-50' : badge.illustration === 'lockBadge'}" class="flex flex-col">
        <div :class="{'justify-center pt-11' : !row}" class="flex items-center">
          <div class="dot mr-3 bg-black"/>
          <div class="ms-subtitle">{{ badge.name }}</div>
          <div class="dot ml-3 bg-black"/>
        </div>
        <div class="pt-3 ms-description text-ms-gray-dark">{{ badge.description }}</div>
      </div>
    </div>
  </div>
</template>

<script>

  export default {
    name: 'Badge',
    props: {
      badge: {
        type: Object,
        default: function() {
          return {}
        }
      },
      row: {
        type: Boolean,
        default: function() {
          return false
        }
      }
    },
  }
</script>

<style lang="scss" scoped>

  .dot-left {
    left: 36%;
    transform: translate(-50%, -50%);
  }
  .dot-right {
    right: 33%;
    transform: translate(-50%, -50%);
  }

</style>

<template>
  <div>
    <div v-for="(item, index) in items"
         :key="item[itemId]"
         :class="{'mt-4': index > 0}"
         class="accordion w-full overflow-hidden bg-transparent">

      <!-- Question -->
      <div :class="{'rounded-b-lg': activeItem !== item[itemId]}"
           class="w-full relative rounded-t-lg bg-blue flex items-center justify-between py-8 px-8 cursor-pointer z-10"
           @click="toggleItem(item[itemId])">
        <h3 class="text-white font-bold text-4xl sm:text-xl leading-normal">{{ item[itemTitle] }}</h3>
        <svg :class="[activeItem === item[itemId] ? 'arrow--active' : '']"
             class="arrow"
             height="25px"
             width="25px"
             viewBox="0 0 240.823 240.823"
             fill="#FFFFFF">
          <g>
            <path d="M183.189,111.816L74.892,3.555c-4.752-4.74-12.451-4.74-17.215,0c-4.752,4.74-4.752,12.439,0,17.179 l99.707,99.671l-99.695,99.671c-4.752,4.74-4.752,12.439,0,17.191c4.752,4.74,12.463,4.74,17.215,0l108.297-108.261 C187.881,124.315,187.881,116.495,183.189,111.816z" />
          </g>
        </svg>
      </div>
      <!-- End Question -->

      <!-- Answer -->
      <transition name="slideBottom">
        <div v-if="activeItem === item[itemId]"
             class="px-10 py-12 bg-white rounded-b-lg relative z-0 overflow-hidden">
          <slot :item="item" name="item">
            <pre>{{ item }}</pre>
          </slot>
        </div>
      </transition>
      <!-- End Answer -->

    </div>
  </div>
</template>

<script>
  export default {
    name: 'Accordion',
    props: {
      items: {
        type: Array,
        default: () => {
          return []
        }
      },
      itemId: {
        type: String,
        default: 'id'
      },
      itemTitle: {
        type: String,
        default: 'name'
      },
    },
    data() {
      return {
        activeItem: null
      }
    },
    methods: {
      toggleItem: function(id) {
        this.activeItem !== id
          ? (this.activeItem = id)
          : (this.activeItem = null)
      }
    }
  }
</script>


<style lang="scss">
  .accordion {
    .arrow {
      @apply ml-3;
      transition: transform 0.3s ease;
      transform: rotate(90deg);
    }

    .arrow--active {
      transform: rotate(-90deg);
    }
  }
</style>

<template>
  <li>
    <div @click="handleClick" v-bind="$attrs">
      <a class="nav-link" :class="{'mb-0': open}">
        <i v-if="link.icon" :class="link.icon"></i>
        <p>{{link.name}}</p>
      </a>
    </div>
    <ul class="dropdown-sidelink" :class="{'h-0': !open}">
      <slot></slot>
    </ul>
  </li>
</template>
<script>
  import MovingArrow from "./MovingArrow";

  export default {
    components: { MovingArrow },
    data() {
      return {
        id: Math.random().toString(36).substring(7),
        open: this.initOpen
      }
    },
    props: {
      initOpen: {
        type: Boolean,
        default: false
      },
      link: {
        type: Object,
        default: () => {
          return {}
        }
      }
    },
    mounted() {
      this.$root.$on('sidebarDropDownOpening', id => {
        if (this.id !== id)
          this.open = false
      })
    },
    methods: {
      handleClick() {
        this.open = !this.open
        if (this.open) {
          this.$root.$emit('sidebarDropDownOpening', this.id);
        }
      }
    }
  }
</script>

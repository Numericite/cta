<template>
  <div class="Partners flexContainer mt-32 overflow-hidden sm:mt-16">

    <div class="flex items-start justify-between">
      <h3 class="text-6xl sm:text-5xl xsm:text-4xl title font-extrabold text-blue mb-12">
        <span v-html="title" />
      </h3>
      <div class="appear">
        <svg class="mr-16 cursor-pointer prev sm:w-10 sm:h-10 sm:mr-1"
             xmlns="http://www.w3.org/2000/svg"
             width="52"
             height="52"
             viewBox="0 0 52 52">
          <g fill="none"
             fill-rule="evenodd"
             stroke="#432CB4"
             transform="matrix(-1 0 0 1 51 1)">
            <circle cx="25"
                    cy="25"
                    r="25" />
            <path stroke-linecap="round"
                  stroke-width="2"
                  d="M21 18l7.95 8.441L21 33.36" />
          </g>
        </svg>
        <svg class="cursor-pointer next sm:w-10 sm:h-10"
             xmlns="http://www.w3.org/2000/svg"
             width="52"
             height="52"
             viewBox="0 0 52 52">
          <g fill="none"
             fill-rule="evenodd"
             stroke="#432CB4"
             transform="matrix(-1 0 0 1 51 1)">
            <circle cx="25"
                    cy="25"
                    r="25" />
            <path stroke-linecap="round"
                  stroke-width="2"
                  d="M21 18l7.95 8.441L21 33.36" />
          </g>
        </svg>
      </div>
    </div>

    <div class="mt-12 appear sm:mt-0">
      <no-ssr>
        <swiper :options="swiperOption"
                class="flex">
          <swiper-slide v-for="partner in partners"
                        :key="partner.name">
            <img :src="partner.img_path"
                 :alt="partner.name + 'logo, partenaire de crée ton avenir'">
          </swiper-slide>
        </swiper>
      </no-ssr>
    </div>

  </div>
</template>

<script>
import { API_ROOT } from '~/config'

export default {
  name: 'Partners',
  props: {
    title: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      API_ROOT: API_ROOT,
      swiperOption: {
        slidesPerView: 4,
        centeredSlides: false,
        spaceBetween: 25,
        navigation: {
          nextEl: '.next',
          prevEl: '.prev'
        },
        breakpoints: {
          1024: {
            slidesPerView: 3,
            spaceBetween: 25
          },
          768: {
            slidesPerView: 2,
            spaceBetween: 20
          },
          640: {
            slidesPerView: 1,
            spaceBetween: 10
          },
          320: {
            slidesPerView: 1,
            spaceBetween: 5
          }
        }
      }
    }
  },
  computed: {
    partners: function() {
      return this.$store.state.partners
    }
  }
}
</script>

<style lang="scss" scoped>
.swiper-slide {
  width: 60%;
  transition: 0.5s 0s ease;
  img {
    max-width: 12rem;
  }
}
.swiper-slide:nth-child(2n) {
  width: 40%;
}
.swiper-slide:nth-child(3n) {
  width: 20%;
}
.swiper-wrapper,
.swiper-container {
  overflow: visible;
}
.swiper-wrapper {
  width: auto;
}
.next {
  transform: rotate(180deg) !important;
}
</style>

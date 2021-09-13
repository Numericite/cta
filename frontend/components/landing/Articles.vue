<template>
  <div v-if="display"
       id="news"
       class="articles flexContainer mt-16 pb-6 overflow-x-hidden">

    <div class="flex items-start justify-between">
      <h3 class="text-6xl sm:text-5xl xsm:text-4xl title font-extrabold text-blue mb-12">
        Nos actualités
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

    <div>
      <no-ssr>
        <swiper v-if="posts.length > 0"
                :options="swiperOption"
                class="flex">
          <swiper-slide v-for="post in posts"
                        :key="post.title"
                        class="appear">
            <post-card v-if="post"
                       :bg="false"
                       :post="post" />
          </swiper-slide>
        </swiper>
      </no-ssr>
    </div>

    <nuxt-link to="blog"
               class="float-right text-blue-lighter uppercase text-sm underline mt-5 font-bold">
      Voir tous les articles
    </nuxt-link>

  </div>
</template>

<script>
import postCard from '~/components/PostCard'

export default {
  name: 'Articles',
  components: { postCard },
  props: {
    posts: {
      type: Array,
      default: function() {
        return { title: null }
      }
    }
  },
  data() {
    return {
      display: true,
      swiperOption: {
        slidesPerView: 3,
        centeredSlides: false,
        spaceBetween: 30,
        navigation: {
          nextEl: '.next',
          prevEl: '.prev'
        },
        breakpoints: {
          1024: {
            slidesPerView: 3,
            spaceBetween: 30
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
  }
}
</script>

<style lang="scss" scoped>
.swiper-slide {
  width: 60%;
  transition: 0.5s 0s ease;
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
  transition: 1s all ease;
}
.next {
  transform: rotate(180deg);
}
</style>

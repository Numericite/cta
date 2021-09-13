<template>
  <div class="single w-screen">

    <div class="toolbar fixed pin-r my-auto z-30 flex flex-col bg-orange items-center">

      <a href="https://www.facebook.com/Crée-Ton-Avenir-France-101833571164053/"
         target="_blank"
         class="w-16 h-16 flex justify-center items-center sm:w-10 sm:h-10">
        <span>
          <img src="~/assets/img/socials/facebook.svg"
               alt="Logo de Facebook">
        </span>
      </a>
      <hr class="separator">

      <a href="https://twitter.com/cta_france"
         target="_blank"
         class="w-16 h-16 flex justify-center items-center sm:w-10 sm:h-10">
        <span>
          <img src="~/assets/img/socials/twitter.svg"
               alt="Logo de Twitter">
        </span>
      </a>
      <hr class="separator">

      <a href="https://www.instagram.com/cta_france/"
         target="_blank"
         class="w-16 h-16 flex justify-center items-center sm:w-10 sm:h-10">
        <span>
          <img src="~/assets/img/socials/instagram.svg"
               alt="Logo d'Instagram">
        </span>
      </a>
      <hr class="separator">

      <a href="https://www.youtube.com/channel/UCzM4nULaj-h-SNI2rSwc7Lg"
         target="_blank"
         class="w-16 h-16 flex justify-center items-center sm:w-10 sm:h-10">
        <span>
          <svg xmlns="http://www.w3.org/2000/svg"
               xmlns:xlink="http://www.w3.org/1999/xlink"
               version="1.1"
               x="0px"
               y="0px"
               viewBox="0 0 334.623 334.623"
               style="enable-background:new 0 0 334.623 334.623;"
               xml:space="preserve"
               width="25px"
               height="25px">
            <g>
              <path d="M175.329,287.017C175.322,287.017,175.329,287.017,175.329,287.017c-38.053,0-79.481-1.202-123.125-3.554   c-25.784-1.414-45.785-21.292-48.632-48.349c-4.762-45.2-4.762-90.83,0-135.625c2.841-26.832,22.815-46.71,48.574-48.33   c75.271-4.737,152.721-4.737,230.312,0c26.061,1.581,45.586,20.99,48.6,48.304c4.749,43.246,4.756,88.87,0.006,135.632   c-2.744,27.038-22.25,46.472-48.535,48.356C249.45,285.815,213.382,287.017,175.329,287.017z M166.428,60.452   c-37.931,0-76.113,1.195-113.485,3.541c-19.685,1.234-34.39,16.048-36.594,36.851c-4.666,43.895-4.666,88.613,0,132.926   c2.204,20.971,16.89,35.778,36.543,36.858c43.413,2.339,84.609,3.535,122.425,3.535c37.751,0,73.51-1.189,106.287-3.528   c19.833-1.427,34.57-16.228,36.659-36.839c4.666-45.862,4.666-90.58,0.006-132.926c-2.294-20.868-17.005-35.688-36.594-36.877   C243.145,61.641,204.365,60.452,166.428,60.452z M134.576,121.783v88.208l84.706-43.805L134.576,121.783z"
                    fill="#FFFFFF" />
            </g>
          </svg>
        </span>
      </a>

    </div>

    <!-- Header -->
    <div class="header w-full flexContainer flex items-center bg-blue bg-fixed py-24 sm:py-12 fixed pin-t pin-l z-0">
      <h1 v-if="post"
          class="title text-white text-8xl w-1/2 sm:text-center sm:w-full sm:text-5xl xsm:text-4xl">{{ post.title }}</h1>
    </div>
    <!-- End Header -->

    <div :class="{'wrapper--scroll': scrollY > 1}"
         class="z-10 w-full bg-white wrapper bg-white relative">

      <!-- Topbar -->
      <div class="topbar relative w-full flexContainer flex justify-between py-4 sm:flex-col sm:items-center sm:pt-0">
        <nuxt-link to="/"
                   class="uppercase text-blue flex items-center font-bold sm:mt-4 sm:self-start">
          <svg width="20px"
               height="20px"
               viewBox="0 0 86 86"
               version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               xmlns:xlink="http://www.w3.org/1999/xlink">
            <g stroke="none"
               stroke-width="1"
               fill="none"
               fill-rule="evenodd">
              <g transform="translate(0.000000, -1.000000)"
                 fill="#000000">
                <g transform="translate(0.000000, 0.637820)">
                  <path d="M46.162425,22.27547 C46.5694179,22.2756387 46.9357147,22.5224264 47.0887334,22.8995584 C47.2417521,23.2766904 47.1509683,23.7089355 46.859155,23.99264 L27.489644,43.3622 L46.859155,62.7317 C47.11941,62.9827423 47.2238827,63.3547191 47.1323887,63.704553 C47.0408948,64.054387 46.7676855,64.3275865 46.4178484,64.4190681 C46.0680112,64.5105497 45.6960381,64.4060639 45.445005,64.1458 L25.368425,44.0692 C24.9780316,43.6786658 24.9780316,43.0456342 25.368425,42.6551 L45.445006,22.5785 C45.6333782,22.3847035 45.8921647,22.275395 46.162426,22.27547 L46.162425,22.27547 Z" />
                </g>
              </g>
            </g>
          </svg>
          Retour
        </nuxt-link>
        <picture>
          <source srcset="~/assets/img/eleve.webp"
                  type="image/webp">
          <img src="~/assets/img/eleve.png"
               class="logo xl:absolute pin-r pin-t flexContainer"
               alt="Illustration d'un élève">
        </picture>
      </div>
      <div class="flexContainer">
        <hr class="hr">
      </div>
      <!-- End Topbar -->

      <!-- content -->
      <div class="flexContainer text-content leading-normal whitespace-pre-wrap text-grey mt-16">
        <h4 class="text-emerald text-sm font-bold uppercase">{{ formatDate(post.date) }}</h4>
        <no-ssr>
          <div v-html="post.content" />
        </no-ssr>
      </div>
      <div class="flexContainer mt-12">
        <hr class="hr">
      </div>
      <!-- end content -->

      <!-- Other -->
      <div class="flexContainer mt-8">
        <h2 class="title text-blue text-6xl font-bold">Autres actualités</h2>
        <div class="flex mt-12 sm:flex-col sm:items-center">
          <post-card v-for="post in posts.slice(0,3)"
                     :key="post.id"
                     :post="post"
                     :bg="false"
                     class="w-1/3 sm:w-full sm:mt-4" />
        </div>
      </div>
      <!-- End Other -->
    </div>

  </div>
</template>

<script>
import postCard from '~/components/PostCard'
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import { createPerimeter } from 'vue-kindergarten'
import heads from '~/config/meta.json'
import moment from 'moment'

export default {
  name: 'Single',
  components: { postCard },
  perimeters: [DashboardPerimeter],
  head() {
    if (heads[this.$options.name]) return heads[this.$options.name]
    return {
      title: 'Crée ton avenir'
    }
  },
  async asyncData({ app, route, error }) {
    try {
      let response = await app.$api.blog.getPost(
        route.params.id
      )
      const post = response.data
      response = await app.$api.blog.getFirstPosts(3, post[0].id)
      let posts = response.data
      return { post: post[0], posts: posts }
    } catch (err) {
      console.log(err)
    }
  },
  data() {
    return {
      scrollY: 0
    }
  },
  mounted() {
    moment.locale('fr')
  },
  created() {
    if (process.browser) {
      window.addEventListener('scroll', this.scrollEvent)
    }
  },
  destroyed() {
    if (process.browser) {
      window.removeEventListener('scroll', this.scrollEvent)
    }
  },
  methods: {
    scrollEvent: function() {
      this.scrollY = window.scrollY
    },
    formatDate: function(date) {
      return moment(date).format('D MMMM YYYY')
    }
  }
}
</script>

<style lang="scss" scoped>
.header {
  height: 396px;
  background-image: url('/img/blog/blog-header.svg');
  background-position: right top;
  background-repeat: no-repeat;

  @screen sm {
    height: 150px;
  }
}

.wrapper {
  margin-top: 396px;
  @screen sm {
    margin-top: 150px;
  }
}

.wrapper--scroll {
  box-shadow: 3px -7px 35px 0 rgba(0, 0, 0, 0.3);
  @apply pb-20;
  @screen sm {
    margin-bottom: 0;
  }
}

.toolbar {
  border-top-left-radius: 1rem;
  border-bottom-left-radius: 1rem;
  a {
    transition: background 0.2s ease;
    &:first-child {
      border-top-left-radius: 1rem;
    }
    &:last-child {
      border-bottom-left-radius: 1rem;
    }
    &:hover {
      background: #e8723e;
    }
  }
  hr {
    width: 30px;
    height: 1px;
    background: #e8723e;
  }
}

.topbar path {
  fill: #432cb4;
}
.topbar .logo {
  transform: translate(-30%, -85%);
}
@screen allsm {
  .topbar .logo {
    transform: inherit;
  }
}
.postCard:nth-child(2) {
  @apply mx-12;
}
</style>

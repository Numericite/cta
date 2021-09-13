<template>
  <div>
    <Hero ref="appear" :title="texts[0]" :subTitle="texts[1]"/>
    <Articles v-if="posts.length > 0" ref="appear"
              :posts="posts" />
    <About ref="appear" :title="texts[2]" :text="texts[3]" />
    <Steps ref="appear"
           :title="texts[4]"
           :step1="{ title: texts[5], text: texts[6]}"
           :step2="{ title: texts[7], text: texts[8]}"
           :step3="{ title: texts[9], text: texts[10]}"/>
    <Partners ref="appear" :title="texts[11]"/>
    <Contact ref="appear"
             :title="texts[12]"
             :subTitle="texts[13]"
             :subText="texts[14]"
             :availabilityTitle="texts[15]"
             :availabilityText="texts[16]"
             :phone="texts[17]"
             :email="texts[18]"
    />
  </div>
</template>

<script>
import Hero from '~/components/landing/Hero'
import Articles from '~/components/landing/Articles'
import Contact from '~/components/landing/Contact'
import About from '~/components/landing/About'
import Steps from '~/components/landing/Steps'
import Partners from '~/components/landing/Partners'
import Scroll from '~/mixins/scroll.js'
import { createPerimeter } from 'vue-kindergarten'
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import heads from '~/config/meta.json'

export default {
  name: 'Landing',
  components: { Hero, Articles, Contact, About, Steps, Partners },
  mixins: [Scroll],
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessLanding',
  head() {
    if (heads[this.$options.name]) return heads[this.$options.name]
    return {
      title: 'Crée ton avenir'
    }
  },
  async asyncData({ app, route, error }) {
    try {
      let response = await app.$api.blog.getFirstPosts(10)
      const posts = response.data || []
      return { posts }
    } catch (e) {
      return 'error'
    }
  },
  data() {
    return {
      scrollNav: false,
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    // this.initEvents()
  },
  beforeDestroy() {
    this.removeEvents()
  },
  methods: {
    initEvents() {
      document.addEventListener('wheel', this.wheelEvent, {
        capture: true,
        passive: true
      })
      document.addEventListener('touchmove', this.wheelEvent, {
        capture: true,
        passive: true
      })
    },
    removeEvents() {
      document.removeEventListener('wheel', this.wheelEvent)
      document.removeEventListener('touchmove', this.wheelEvent)
    },
    wheelEvent(e) {
      if (e.type == 'wheel') {
        if (e.deltaY > 0) this.appearElms()
      } else if (e.type == 'touchmove') {
        if (e.touches[0].pageY > 0) this.appearElms()
      }
    }
  }
}
</script>

<template>
  <nuxt-link :to="{ name: 'blog-id-title', params: { id: post.id, title: post.title.replace(/ /g,'_') } }"
             :class="[bg ? 'bg-white' : 'bg-blue-lightest']"
             class="flex flex-col postCard hoverCard rounded-lg p-6 pr-16 pb-16 sm:pb-8">

    <!-- DATE -->
    <div class="flex justify-center items-center text-white font-extrabold text-base bg-emerald rounded-full relative w-16 h-16">
      <span class="day absolute pin-t pin-l">{{ dateDay(post.date) }}</span>
      <span class="separator absolute w-6 bg-white" />
      <span class="month absolute pin-b pin-r mb-2 mr-2">{{ dateMonth(post.date) }}</span>
    </div>
    <!-- End DATE -->

    <div class="postCard__title text-2xl text-blue font-extrabold pt-8 pb-4 leading-normal sm:text-3xl">{{ post.title }}</div>

    <div class="postCard__ext text-grey text-base leading-normal">
      {{ (post.content).substring(0,150) + '...' | withoutTags }}
    </div>
  </nuxt-link>
</template>

<script>
import moment from 'moment'

export default {
  name: 'Postcard',
  props: {
    post: {
      type: Object,
      default: function() {
        return { title: '' }
      }
    },
    bg: {
      type: Boolean,
      default: function() {
        return { bg: false }
      }
    }
  },
  mounted() {
    moment.locale('fr')
  },
  methods: {
    dateDay: function(date) {
      return moment(date).format('D')
    },
    dateMonth: function(date) {
      var fromatedMonth = moment(date).format('M')
      if (fromatedMonth < 10) {
        fromatedMonth = '0' + fromatedMonth
      }
      return fromatedMonth
    }
  }
}
</script>

<style scoped lang="scss">
.day {
  margin-top: 0.85rem;
  margin-left: 0.85rem;
}
.separator {
  height: 1px;
  transform: rotate(135deg);
}
.month {
  margin-bottom: 0.85rem;
  margin-right: 0.85rem;
}
.postCard__ext  {
  min-height: 5.267rem;
}
</style>

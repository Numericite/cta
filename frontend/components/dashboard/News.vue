<template>
  <div>
    <h2 class="title title--dash pt-16 sm:text-4xl sm:pt-12">Actualités</h2>

    <div class="flex mt-6 sm:hidden">
      <post-card v-for="(post, i) in posts.slice(0, 2)"
                 :key="i++"
                 :post="post"
                 :bg="true"
                 :class="[i == 1 || i == 3 ? 'mr-8' : '']"
                 class="w-1/2 article sm:w-4/5" />
    </div>
    <div class="flex sm:hidden">
      <post-card v-for="(post, i) in posts.slice(2, 4)"
                 :key="i++"
                 :post="post"
                 :bg="true"
                 :class="[i == 1 || i == 3 ? 'mr-8' : '']"
                 class="w-1/2 mt-8 article sm:w-4/5" />
    </div>
    <div class="scroll--mobile sm:flex hidden sm:visible overflow-x-scroll sm:pb-8">
      <post-card v-for="(post, i) in posts.slice(0, 4)"
                 :key="i++"
                 :post="post"
                 :bg="true"
                 :class="[i == 4 ? '' : 'mr-8']"
                 class="w-1/2 mt-8 article sm:w-4/5 hidden" />
    </div>
    <nuxt-link to="blog"
               class="float-right text-blue-lighter uppercase text-sm underline mt-5 font-bold">
      Voir tous les articles
    </nuxt-link>
  </div>
</template>

<script>
import PostCard from '~/components/PostCard'

export default {
  name: 'News',
  components: { PostCard },
  props: {
    posts: {
      type: Array,
      default: function() {
        return { title: null }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.article {
  box-shadow: 0 11px 30px 0 rgba(48, 30, 141, 0.22);
  @screen sm {
    min-width: 80%;
  }
}
</style>

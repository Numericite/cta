<template>
  <div class="activity__right xl:w-3/5 w-full xl:p-12 xl:pr-32 flex flex-col justify-center sm:pt-16 p-6">

    <h1 id="actTitle" class="title text-blue font-bold">
      <span v-html="texts[0]" />
    </h1>

    <div class="mt-12">
      <videoComp vimeoId="357031203"
                 class="w-full mb-8" />
    </div>
    <div class="mt-16">
      <h2 class="title text-4xl text-blue font-bold mb-6">
        <span v-html="texts[1]" />
      </h2>
      <p class="text-blue text-3xl text-justify">
        <span v-html="texts[2]" /><br><br>
        <span v-html="texts[3]" /><br>
        <span v-for="index in texts.length" :key="index"
              class="block"
              v-html="texts[index + 3]" />
      </p>
    </div>

    <button class="button button--blue mt-8 self-start"
            @click="$emit('startActivity')">
      Je commence l'activité
    </button>

  </div>
</template>

<script>
import VideoComp from '~/components/Video'

export default {
  components: { VideoComp },
  data() {
    return {
      texts: this.getPageTexts(this.$route.name)
    }
  },
  created() {
    this.$emit( 'stopActivity' )
  },
  mounted() {
    document.getElementById('actTitle').scrollIntoView({
      behavior: 'smooth'
    });
  }
}
</script>

<style lang="scss" scoped>
.activity__right {

  h1 {
    font-size: 2.5rem;
  }
}
</style>

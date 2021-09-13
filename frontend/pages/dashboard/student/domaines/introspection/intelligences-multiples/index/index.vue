<template>
  <div class="activity__right xl:w-3/5 w-full xl:p-12 xl:pr-32 flex flex-col justify-center sm:pt-16 p-6 overflow-x-hidden overflow-y-scroll">

    <h1 id="actTitle" class="title text-blue font-bold">
      <span v-html="texts[0]" />
    </h1>

    <div class="mt-12">
      <videoComp vimeoId="357032868"
                 class="w-full mb-8" />
      <h2 class="title text-4xl text-blue font-bold mb-6">
        <span v-html="texts[1]" />
      </h2>
      <ul class="text-blue text-3xl leading-normal">
        <li class="mt-4 text-justify"><span class="font-bold text-orange"><span v-html="texts[2]" /></span> <span v-html="texts[3]" /></li>
        <li class="mt-4 text-justify"><span class="font-bold text-orange"><span v-html="texts[4]" /></span> <span v-html="texts[5]" /></li>
        <li class="mt-4 text-justify"><span class="font-bold text-orange"><span v-html="texts[6]" /></span> <span v-html="texts[7]" /></li>
        <li class="mt-4 text-justify"><span class="font-bold text-orange"><span v-html="texts[8]" /></span> <span v-html="texts[9]" /></li>
        <li class="mt-4 text-justify"><span class="font-bold text-orange"><span v-html="texts[10]" /></span> <span v-html="texts[11]" /></li>
        <li class="mt-4 text-justify"><span class="font-bold text-orange"><span v-html="texts[12]" /></span> <span v-html="texts[13]" /></li>
        <li class="mt-4 text-justify"><span class="font-bold text-orange"><span v-html="texts[14]" /></span> <span v-html="texts[15]" /></li>
        <li class="mt-4 text-justify"><span class="font-bold text-orange"><span v-html="texts[16]" /></span> <span v-html="texts[17]" /></li>
      </ul>
    </div>

    <button v-scroll-to="'#actTitle'"
            class="button button--blue mt-8 self-start"
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
    this.$emit('stopActivity')
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

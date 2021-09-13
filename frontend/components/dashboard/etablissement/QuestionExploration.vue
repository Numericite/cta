<template>
  <div class="mb-4">
    <div class="text-white rounded-lg bg-blue">
      <a href="#" class="tab__link p-4 block hover:border-b-0 text-white flex justify-between" @click.prevent="active = !active">
        <strong>{{ question.title }}</strong>
        <span v-show="!active" class="down-Arrow">&#9660;</span>
        <span v-show="active" class="up-Arrow">&#9650;</span>
      </a>
    </div>
    <div v-for="(question, index) in question.questionList" v-show="active" :key="index" class="p-2 rounded-b-lg p-4 bg-white"> {{ question }}</div>
  </div>
</template>

<script>
  export default {
    name: 'FaqCard',
    props: {
      question: {
        type: Object,
        default: null
      }
    },
    data () {
      return {
        active: false
      }
    },
  }
</script>

<style lang="scss">
  .tab__link:hover {
    border-bottom: none;
  }
</style>

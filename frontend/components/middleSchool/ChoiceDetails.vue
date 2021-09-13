<template>
  <div class="flex w-full justify-between sm:block">
    <div class="flex flex-col items-center justify-center">
      <div class="ms-subtitle-bis text-center pb-6 sm:pb-4">Je me sens à l’aise avec ce choix</div>
      <img :src="'/icons/' + getRatingLikeSmiley.img" class="sm:pb-4">
    </div>
    <div class="flex flex-col items-center justify-center">
      <div class="ms-subtitle-bis text-center pb-6 sm:pb-4">Je sais que je peux faire ce choix sans difficulté ou sans obstacle</div>
      <img :src="'/icons/' + getRatingAbleSmiley.img" class="sm:pb-4">
    </div>
    <div class="flex flex-col items-center justify-center">
      <div class="ms-subtitle-bis text-center pb-6 sm:pb-4">Je suis prêt(e)  à m’investir dans la voie que j’ai choisie</div>
      <img :src="'/icons/' + getRatingWishSmiley.img" class="sm:pb-4">
    </div>
  </div>
</template>

<script>
  export default {
    name: 'ChoiceDetails',
    props: {
      lead: {
        type: Object,
        default() {
          return {}
        }
      }
    },
    data() {
      return {
        rateIcons: [
          {id: 1, img: 'verysad_smiley.png'},
          {id: 2, img: 'sad_smiley.png'},
          {id: 3, img: 'happy_smileybis.png'},
          {id: 4, img: 'love_smiley.png'}
        ]
      }
    },
    computed: {
      getRatingLikeSmiley() {
        const rate = this.rateIcons.find(i => i.id === this.lead.choice_ease)
        return rate
      },
      getRatingAbleSmiley() {
        const rate = this.rateIcons.find(i => i.id === this.lead.choice_able)
        return rate
      },
      getRatingWishSmiley() {
        const rate = this.rateIcons.find(i => i.id === this.lead.choice_ready)
        return rate
      },
    },
  }
</script>

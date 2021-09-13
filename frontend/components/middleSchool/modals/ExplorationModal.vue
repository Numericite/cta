<template>
  <div class="w-full flex justify-center">
    <CollegeModal :newExploration="true" class="flex w-full justify-center" @handleClose="$emit('handleClose')">
      <div v-if="step === 1" class="flex flex-col justify-start items-center content-center pt-10">
        <img src="~/static/img/middle-school/illustration-happy.png">
        <div class="text-center flex flex-col text-6xl pt-4"><span class="ms-title pb-2">Félicitation !</span> Tu as complété une exploration !</div>
        <div class="xl:max-w-1/2 text-center pt-6 text-ms-gray-dark leading-normal">{{ description }}</div>
        <div class="flex items-center py-20">
          <div class="dot mr-3 bg-black"/>
          <div class="ms-subtitle">CHOISIS TA PROCHAINE ACTION</div>
          <div class="dot ml-3 bg-black"/>
        </div>
        <div class="flex flex-wrap justify-center w-full pb-8">
          <Entity v-for="entity in entities" :key="entity.name" :entity="entity" :row="false" :newExploration="true" class="px-6 py-6"/>
        </div>
        <button class="button-outline-ms border-ms-red text-ms-red mb-8" @click="$router.push('/dashboard/college/student/programme/')">Retourner au programme</button>
      </div>
      <div v-if="step === 2" class="flex flex-col justify-start items-center content-center pt-20">
        <img src="~/static/img/middle-school/step3.png" class="w-32">
        <div class="text-center flex flex-col text-6xl pt-4"><span class="ms-title pb-2">Choisi le sujet </span> que tu veux explorer</div>
        <div class="max-w-1/2 text-center pt-6 text-ms-gray-dark leading-normal">{{ description }}</div>
        <div class="flex items-center pt-20 pb-10">
          <div class="dot mr-3 bg-black"/>
          <div class="ms-subtitle">CHOISIS TA PROCHAINE ACTION</div>
          <div class="dot ml-3 bg-black"/>
        </div>
        <div class="flex flex-col flex-wrap justify-center w-3/4 pb-8">
          <ExplorationType v-for="exploration in explorationTypeList" :key="exploration.id" :exploration="exploration" class="p-4 my-4 shadow-md rounded-lg"/>
        </div>
      </div>
      <div v-if="step === 3" class="flex flex-col justify-start items-center content-center pt-10">
        <img src="~/static/img/middle-school/step3.png" class="w-32">
        <div class="text-center flex flex-col text-6xl pt-4"><span class="ms-title pb-2">Créer <span class="font-normal">ton exploration</span></span></div>
        <div class="xl:max-w-1/2 text-center pt-6 text-ms-gray-dark leading-normal">
          Maintenant que tu as choisi ton exploration, tu vas devoir la programmer ! Pour cela tu dois lui
          donner un titre et définir une date limite pour l´effectuer.<br><br>
          Pour faciliter ton exploration, nous te proposons de te guider à l’aide de fiches d’exploration.
          Enfin, une fois que ton action d’exploration aura été réalisée, n´oublie pas de revenir faire ton retour
          pour la valider.
        </div>
        <div class="flex w-full flex-wrap justify-center pb-8 pt-8 px-20 ">
          <div class="flex flex-col xl:w-1/2 justify-center items-center">
            <div class="pt-11 w-full xl:max-w-2/3">
              <FormulateInput :outerClass="['w-full my-8']" :inputClass="['mt-8 py-5 px-6 focus:border-purple text-ms-gray-dark']" v-model="exploration.name" label="Titre" type="text" class="custom-exploration-input"/>
            </div>
            <div class="py-11 w-full xl:max-w-2/3">
              <FormulateInput :outerClass="['w-full my-8']" :inputClass="['mt-8 py-5 px-6 focus:border-purple text-ms-gray-dark']" v-model="exploration.date" label="Date" type="date" placeholder="Placeholder" class="custom-exploration-input"/>
            </div>
            <button :class="{'pointer-events-none opacity-50' : !exploration.name.length || !exploration.date.length }" class="button-outline-ms border-ms-red text-ms-red mb-4" @click="createExploration">Commencer l'exploration</button>
          </div>
        </div>
      </div>
    </CollegeModal>
  </div>
</template>

<script>
import CollegeModal from '../CollegeModal'
import Entity from '../Entity'
import ExplorationType from '../ExplorationType'
export default {
name: "ExplorationModal",
  components: { CollegeModal, Entity, ExplorationType },
  props: {
    explorationTypeList: {
      type: Array,
      default: function() {
        return []
      }
    },
    explorationType: {
      type: Object,
      default: function() {
        return {}
      }
    },
  },
  data() {
    return {
      step:1,
      entities: [
        {
          name: "s'orienter",
          description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pretium diam ipsumddd.',
          info: 'Prochaine échéance dans ',
          moreInfo: '1 mois',
          illustration: 'step2'
        },
        {
          name: "explorer",
          description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pretium diam ipsumddd.',
          count: 5,
          info: 'actions restantes',
          moreInfo: '',
          illustration: 'step3'
        },
        {
          name: "modules",
          description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pretium diam ipsumddd.',
          count: 7,
          info: 'modules restants',
          moreInfo: '',
          illustration: 'step1'
        },
      ],
      exploration: {
        user_id: this.$store.state.auth.user.userID,
        name: '',
        exploration_type_id: this.$route.params.id,
        date: ''
      },
      doneExploration: {
        user_id: this.$store.state.auth.user.userID,
        name: '',
        exploration_type_id: this.$route.params.id,
        date: ''
      },
      path: '/dashboard/college/student/parcours',
    }
},
  created() {
  if(this.$route.params.id) {
    this.step = 3
  }
},
  methods: {
  async createExploration() {
    if(this.exploration.name.length && this.exploration.date.length ) {
      await this.$api.explorations.create(this.exploration)
      await this.$emit('handleClose')
    }
  },
    // async completeExploration() {
    //   if(this.doneExploration.name.length) {
    //     delete this.doneExploration.date
    //     const response = await this.$api.explorations.create(this.doneExploration)
    //     this.doneExploration.id = response.data.res.id
    //     await this.$router.push('/dashboard/college/student/formulaire/' + this.explorationType.id + '?exploration=' + this.doneExploration.id)
    //     await this.$emit('handleClose')
    //   }
    // },
  },
}
</script>

<style scoped>
  .custom-exploration-input {
    margin: 0;
  }
</style>

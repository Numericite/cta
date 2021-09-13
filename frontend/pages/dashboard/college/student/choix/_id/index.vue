<template>
  <div class="w-full flex flex-col pt-16 xl:pt-32 xl:px-0 choice">
    <div v-if="!leadExists">
      <div v-if="canChooseRank3" class="flex flex-col z-10 formulate-form-custom">
        <div class="ms-title-medium">Fais ton choix de 3ème</div>
        <div class="text-ms-gray-dark text-2xl pt-10 pb-20 xl:max-w-2/3">Maintenant que tu as complété ton parcours de 4ème,  choisis vers  quel type de 3ème tu veux t'orienter</div>
        <FormulateInput
          :outerClass="['my-8']"
          :inputClass="['mt-8 py-5 px-6 text-ms-gray-dark']"
          :options="{
            '3ème générale': '3ème générale',
            '3ème technologique': '3ème technologique'
          }"
          v-model="lead.description"
          type="radio"
          label="Quelle 3ème pour l'année prochaine ?"
        />
      </div>
      <div v-if="canChooseFaculty" class="flex flex-col z-10 formulate-form-custom">
        <div class="ms-title-medium">Fais ton choix de fillière</div>
        <div class="text-ms-gray-dark text-2xl pt-10 pb-20 xl:max-w-2/3">Maintenant que tu as complété ton autoportrait, que tu as choisi tes domaines et que tu as pris conscience de tes représentations sur le monde du travail, nous allons te demander de choisir quel type de filière et de parcours tu souhaites faire</div>
        <FormulateInput
          :outerClass="['my-8']"
          :inputClass="['mt-8 py-5 px-6 text-ms-gray-dark']"
          :options="{
            'Filière professionnelle': 'Filière professionnelle',
            'Filière générale': 'Filière générale'
          }"
          v-model="lead.faculty"
          type="radio"
          label="Je coche la case que je préférerais pour ma filière :"
        />
        <FormulateInput
          :outerClass="['my-8']"
          :inputClass="['mt-8 py-5 px-6 text-ms-gray-dark']"
          :options="{
            'Un parcours plutôt court pour commencer': 'Un parcours plutôt court pour commencer',
            'Un parcours mi-long': 'Un parcours mi-long',
            'Un parcours plutôt long' : 'Un parcours plutôt long'
          }"
          v-model="lead.course"
          type="radio"
          label="Je coche la case qui me plait le plus pour mon parcours d’études :"
        />
      </div>
    <!--      <div v-else-if="!hasChooseDomains">-->
    <!--        <FormulateInput-->
    <!--          :outerClass="['my-8']"-->
    <!--          :inputClass="['mt-8 py-5 px-6 text-ms-gray-dark']"-->
    <!--          :options="domainOptions"-->
    <!--          type="radio"-->
    <!--          label="Quelle 3ème pour l'année prochaine ?"-->
    <!--        />-->
    <!--      </div>-->
    </div>
    <div v-else>
      <div class="ms-title-medium">Analyse ta piste</div>
      <p class="text-ms-gray-dark text-2xl pt-10 pb-20 max-w-2/3">{{ text_description }}</p>
      <!-- REVIEW -->
      <div class="flex flex-col w-full items-center">
        <div class="w-full">
          <div class="ms-subtitle-bis pb-6 w-full font-medium">Je me sens à l’aise avec ce choix</div>
          <div class="flex justify-between items-start w-full max-w-3/4">
            <div v-for="(review, index) in [1,2,3,4]" :key="index" class="flex flex-col pr-10 pretty">
              <p-radio v-model="choice_ease" :value="index + 1" color="primary-o" class="p-default p-round text-5xl mb-6 border-ms-green" name="radioFirst"/>
              <img :src="'/icons/' + rate_icon[index]" class="w-7 h-7">
            </div>
          </div>
        </div>
        <div class="w-full py-20">
          <div class="ms-subtitle-bis pb-6 w-full font-medium"> Je sais que je peux faire ce choix sans difficulté ou sans obstacle</div>
          <div class="flex justify-between items-start w-full max-w-3/4">
            <div v-for="(review, index) in [1,2,3,4]" :key="index" class="flex flex-col pr-10 pretty">
              <p-radio v-model="choice_able" :value="index +1" color="primary-o" class="p-default p-round text-5xl mb-6 border-ms-green" name="radioSecond"/>
              <img :src="'/icons/' + rate_icon[index]" class="w-7 h-7">
            </div>
          </div>
        </div>
        <div class="w-full pb-10">
          <div class="ms-subtitle-bis pb-6 w-full font-medium"> Je suis prêt(e)  à m’investir dans la voie que j’ai choisie</div>
          <div class="flex justify-between items-start w-full max-w-3/4">
            <div v-for="(review, index) in [1,2,3,4]" :key="index" class="flex flex-col pr-10 pretty">
              <p-radio v-model="choice_ready" :value="index +1" color="primary-o" class="p-default p-round text-5xl mb-6 border-ms-green" name="radioThird"/>
              <img :src="'/icons/' + rate_icon[index]" class="w-7 h-7">
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="w-1/4 py-10">
      <button v-if="!leadExists" class="button-ms bg-ms-green" @click="createLead">Continuer</button>
      <button v-if="leadExists" :class="{'pointer-events-none opacity-50' : (!choice_ready || !choice_able || !choice_ease) }" class="button-ms bg-ms-green" @click="updateLead">Continuer</button>
    </div>
  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import dashboard_ms from '../../../../../../layouts/dashboard_ms_student'
import Button from '../../../../../../mixins/button'
import ProgressBar from '../../../../../../components/middleSchool/ProgressBar'
import _ from 'lodash'

export default {
  name: 'Index',
  layout: 'dashboard_ms_student',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessMiddleSchoolStudentDashboard',
  components: { ProgressBar, Button },
  async asyncData({ app, store, route }) {
    try {
      let response = await app.$api.leads.getByIds({ids: route.params.id})
      const lead = response.data[0] || {}

      return { lead }
    } catch (e) {
      return 'error'
    }
  },
  data() {
    return {
      choice_ease: '',
      choice_able: '',
      choice_ready: '',
      description: '',
      text_description: 'Lis chaque phrase ; coche une case pour dire si tu es plus ou moins d´accord -\n' +
        'Si tu réponds plutôt négativement à l’une de ces phrases, cela signifie que ton choix n’est pas à  100% certain et c’est bien de le savoir !\n' +
        'Une bonne solution peut être d’en discuter avec quelqu’un qui te connait bien comme par exemple ton enseignant ou tes parents ou un camarade.',
      lead: {},
      step: 3,
      rate_icon: ['verysad_smiley.png', 'sad_smiley.png', 'happy_smileybis.png', 'love_smiley.png'],
      leadExists: false,
      domainOptions: {}
    }
  },
  computed: {
    canChooseRank3() {
      return !this.$store.state.auth.user.config.hasChooseRank3 && this.$route.params.id === 'rank3'
    },
    canChooseFaculty() {
      return this.$route.params.id === 'faculty'
    },
    hasChooseDomains() {
      return this.$store.state.auth.user.config.hasChooseMiddleSchoolDomains
    }
  },
  created() {
    // To display the right block
      if(this.lead.id === this.$route.params.id) {
        this.leadExists = true
      }
  },
  methods: {
    async createLead() {
      if(this.lead.description) {
        this.lead.user_id = this.$store.state.auth.user.userID
        await this.$api.leads.create(this.lead)

        //
        this.$store.commit('auth/setHasChooseRank3', true)
        await this.$api.user.updateUser(this.$store.state.auth.user)
        await this.$router.push('/dashboard/college/student/choix/')
      }
      if (this.lead.faculty && this.lead.course) {
        this.lead.user_id = this.$store.state.auth.user.userID
        this.lead.description = this.lead.faculty + ' / ' + this.lead.course
        await this.$api.leads.create(this.lead)
        this.$store.commit('auth/setHasChooseFaculty', true)
        await this.$api.user.updateUser(this.$store.state.auth.user)
        await this.$router.push('/dashboard/college/student/choix/')
      }

    },
    async updateLead() {
      const lead = {
          id: this.$route.params.id,
          choice_ease: this.choice_ease,
          choice_able: this.choice_able,
          choice_ready: this.choice_ready,
          user_id: this.lead.user_id,
          description: this.lead.description,
          updated_date: new Date().getTime()
        }
        await this.$api.leads.update(lead)
        await this.$router.push('/dashboard/college/student/choix/')
    }
  }
}
</script>

<style type="scss" scoped>

.back-btn {
  left: 0;
  transform: translateX(-120%);
}
.step {
  left: 0;
  transform: translateX(-100%);
}
.choice {
  ::placeholder {
    color: #686676;
    font-size: 14px;
    font-weight: normal;
  }
}

</style>

<template>
  <div class="activity__right bg-blue-lightest min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:px-16 px-6 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes traits de personnalités</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4">4.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div class="flex sm:flex-wrap mt-12 sm:w-auto -mx-4">
      <div class="questionCard card rounded-xlg relative overflow-hidden mt-12 my-4 mx-auto w-2/3 sm:w-full">
        <span :class="getBadgeClass(joker1.status_name)"
              class="card-badge border py-2 px-3 font-semibold rounded border-solid absolute mt-4 ml-3 mb ">
          <span v-if="joker1.status_name === 'open'">En cours</span>
          <span v-if="joker1.status_name === 'done'">Terminé</span>
          <span v-if="joker1.status_name === ''">À compléter</span>
        </span>
        <form name="jokerForm">
          <div :class="{'field--active': joker1.config.firstName.length > 0}"
               class="field mx-8 mt-24">
            <label for="firstName1"
                   class="pointer-events-none">Prénom *</label>
            <input id="firstName1"
                   :disabled="joker1.status_name !== ''"
                   v-model="joker1.config.firstName"
                   type="text"
                   required
                   class="w-full">
          </div>
          <div :class="{'field--active': joker1.config.lastName.length > 0}"
               class="field mx-8">
            <label for="lastName1"
                   class="pointer-events-none">Nom *</label>
            <input id="lastName1"
                   :disabled="joker1.status_name !== ''"
                   v-model="joker1.config.lastName"
                   type="text"
                   required
                   class="w-full">
          </div>
          <div :class="{'field--active': joker1.email.length > 0}"
               class="field mx-8">
            <label for="email1"
                   class="pointer-events-none">Email *</label>
            <input id="email1"
                   :disabled="joker1.status_name !== ''"
                   v-model="joker1.email"
                   type="email"
                   required
                   class="w-full">
          </div>
          <div v-if="joker1.errors.length > 0" class="text-center">
            <p v-for="(error, key) in joker1.errors" :key="key" class="error">{{ error }}</p>
          </div>
          <button v-if="!joker1.status_name"
                  type="submit"
                  class="button mt-8 self-start m-8 button--white border-b-1 rounded-full border-blue"
                  @click.prevent="useJoker(joker1)">
            Je valide
          </button>
          <button v-if="joker1.status_name === 'open'"
                  type="button"
                  class="button mt-8 self-start m-8 button--white border-b-1 rounded-full border-blue"
                  @click="goToJoker(joker1)">
            {{ joker1.config.firstName }} est avec moi
          </button>
          <div v-if="joker1.status_name === 'done'" class="mb-12"/>
          <div class="bandeau w-full h-8 bg-peach absolute flex pin-t pin-r justify-center"><span class="text-white uppercase font-bold text-sm mt-3">Mon joker</span></div>
        </form>
      </div>
    </div>


    <button v-scroll-to="'#actTitle'"
            v-if="jokersDone"
            class="button button--blue mt-8 self-start mt-3 mx-auto"
            @click="nextStep">
      Je compare les résultats
    </button>

    <button v-scroll-to="'#actTitle'"
            v-if="jokersCompleted"
            class="button button--blue mt-8 self-start mt-3 mx-auto"
            @click="endActivity">
      Je termine mon activité
    </button>

  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'

export default {
  name: 'IntelligencesMultiples',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    tokens: {
      type: Array,
      default: function () {
        return []
      }
    }
  },
  data() {
    return {
      activity_id: '52485617-002b-4863-9e58-721be716f399',
      joker1: {
        config: {
          firstName: '',
          lastName: '',
          number: 1
        },
        status_name: '',
        email: '',
        errors: [],
      },
      texts: this.getPageTexts(this.$route.name)
    }
  },
  computed: {
    jokersCompleted() {
      return this.joker1.status_name !== '' && this.joker1.status_name !== 'done'
    },
    jokersDone() {
      return this.joker1.status_name === 'done'
    }
  },
  created() {
    this.$emit( 'setStep', 6 )

    if (this.tokens[0]) {
      this.joker1 = Object.assign(this.joker1, this.tokens[0])
      this.joker1.route = '/joker/' + this.joker1.activity_id + '/' + this.joker1.id
    }
  },
  methods: {
    goToJoker: function (joker) {
      this.$router.push(joker.route)
    },
    nextStep: function () {
      this.$router.push( 'etape-4' )
    },
    endActivity: function () {
      this.$router.push( {path: '/dashboard/student/domaines/introspection', hash: '#traits-de-personnalite'})
    },
    async createJoker ( joker ) {
      joker.user_id = this.$store.state.auth.user.userID
      joker.status_name = 'open'
      joker.activity_id = this.activity_id
      const response = await this.$api.activities.createToken(joker)
      joker.id = response.data.res.id
      return joker
    },
    async useJoker( joker ) {
      if (joker.config.firstName && joker.config.lastName && joker.email) {
        const jokerResponse = await this.createJoker(joker)
        joker = jokerResponse
      } else {
        joker.errors = [];

        if (!joker.config.firstName) {
          joker.errors.push('Prénom obligatoire.');
        }
        if (!joker.config.lastName) {
          joker.errors.push('Nom obligatoire.');
        }

        if (!joker.email) {
          joker.errors.push('Email obligatoire.');
        }
      }
      this.$forceUpdate();
    },
    getBadgeClass( status ) {
      switch ( status ) {
        case 'open':
          return 'text-dark-peach border-dark-peach'
        case 'done':
          return 'text-green border-green'
        default:
          return 'text-orange border-orange'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.activity__right {
  .intelligence {
    &:nth-child(1) {
      .intelligence__header {
        @apply bg-yellow;
      }
    }
    &:nth-child(2) {
      .intelligence__header {
        @apply bg-peach;
      }
    }
    &:nth-child(3) {
      .intelligence__header {
        @apply bg-topaze;
      }
    }
    h4 {
      font-size: 2.5rem;
    }
    .content {
      span {
        @apply flex items-center font-bold uppercase text-xs mr-2 mb-2 border-yellow border-solid border rounded-full p-2 cursor-pointer;
      }
    }
  }
}
.card {
  box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
}
.select-pers {
  transform: translate(-0.7rem, -3.6rem);
  z-index: 500;
}

.bandeau {
  transform: translate(43%, 24px) rotate(40deg);
  top: 5px;
  right: 10px;
}

.card-badge {
  transform: translate(20%, 20%);
}

.error {
  margin: 0;
  color: #cc0000;

  &:first-of-type {
    margin-top: 1em;
  }
}
</style>

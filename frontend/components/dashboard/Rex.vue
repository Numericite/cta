<template>
  <div class="my-4">
    <div v-if="!loading">
      <div v-for="(group, index) in groupedExperiences"
           :key="index"
           :class="{'bg-white py-4 px-4 shadow-md rounded-xlg': group.collapse}"
           class="mb-8">
        <h1 :class="{'mb-6': !group.collapse, 'bg-orange': index % 3 === 2, 'bg-green': index % 3 === 1, 'bg-blue-sky': index % 3 === 0}"
            class="px-6 py-3 bg-blue-sky text-white text-3xl inline-block rounded-xlg sans font-bold">{{ index + 1 }}. {{ group.domain.name }}</h1>
        <span v-if="!group.collapse"
              class="float-right w-10 h-10 border-grey border-solid border-1 text-grey rounded-full text-center text-4xl cursor-pointer pt-1 pr-1 minus-custom hover:text-blue hover:border-blue"
              @click="collapseGroup(group, true)">
          ---
        </span>
        <span v-if="group.collapse"
              class="float-right w-10 h-10 border-grey border-solid border-1 text-grey rounded-full text-center text-4xl cursor-pointer hover:text-blue hover:border-blue"
              @click="collapseGroup(group, false)">
          ...
        </span>
        <span v-if="isStream ? $store.state.auth.user.config.stream_ids : $store.state.auth.user.config.domain_ids" class="float-right w-10 h-10 mt-2 mr-8">
          <img v-if="!isStream && hasValidateDomain(group.domain)" src="~/assets/img/domain_check.svg" class="max-w-full">
          <img v-else-if="isStream && hasValidateStream(group.domain)" src="~/assets/img/domain_check.svg" class="max-w-full">
          <img v-else src="~/assets/img/domain_ban.svg" class="max-w-full">
        </span>
        <transition name="fade">
          <div v-if="!group.collapse"
               class="px-4 py-12 bg-white rounded-xlg xl:shadow-md sm:bg-transparent sm:py-4">
            <div v-for="(experience, indexExp) in group.experiences"
                 :key="indexExp"
                 class="sm:bg-white sm:rounded-xlg">
              <div :class="{'flex': indexExp !== 0, 'hidden': indexExp !== 0, 'flex': indexExp === 0}"
                   class="flex-row sm:flex-col text-center font-bold sm:hidden">
                <div class="w-col-custom sm:w-full px-2 text-blue text-xl">Mes actions</div>
                <div class="w-col-custom sm:w-full px-2 text-blue text-xl">Mes étapes à suivre</div>
                <div class="w-col-custom sm:w-full px-2 text-blue text-xl">Mes rendez-vous</div>
                <div class="w-col-custom sm:w-full px-2 text-blue text-xl">Mes résultats</div>
              </div>
              <div class="flex flex-row sm:flex-col my-8">
                <div class="w-col-custom sm:w-full px-2 sm:px-12 sm:py-5">
                  <div class="md:hidden xl:hidden text-blue text-2xl font-bold w-full justify-center flex py-3"> Mes actions</div>
                  <div v-if="changeActions[experience.id] || !experience.actions"
                       class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
                    <textarea v-model="experience.actions"
                              :disabled="isStream ? $store.state.auth.user.config.stream_ids : $store.state.auth.user.config.domain_ids"
                              :placeholder="'Action ' + (indexExp + 1) + ' : Participer au salon domaine / Aller chercher des informations / Conférence sur ... / Trouver un stage dans domaine 2'"
                              debounce="500"
                              class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal"
                              @input="updateExperience(experience)"
                              @focus="enableChangeActions(true, experience)"
                              @blur="enableChangeActions(false, experience)" />
                  </div>
                  <div v-if="!changeActions[experience.id] && experience.actions"
                       class="bg-blue-lightest text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
                    {{ experience.actions.substring(0, 100) + '...' }}
                    <img v-if="isStream ? !$store.state.auth.user.config.stream_ids : !$store.state.auth.user.config.domain_ids"
                         src="~/assets/img/pencil_2.svg"
                         class="p-3 absolute pin-b pin-r cursor-pointer"
                         @click="enableChangeActions(true, experience)">
                  </div>
                </div>
                <div class="md:hidden xl:hidden h-px w-full bg-blue-lighter"/>
                <div class="w-col-custom sm:w-full px-2 sm:px-12 sm:py-5">
                  <div class="md:hidden xl:hidden text-blue text-2xl font-bold w-full justify-center flex py-3"> Mes étapes à suivre</div>
                  <div v-if="changeSteps[experience.id] || !experience.steps"
                       class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
                    <textarea v-model="experience.steps"
                              :disabled="isStream ? $store.state.auth.user.config.stream_ids : $store.state.auth.user.config.domain_ids"
                              placeholder="S’inscrire auprès de ... / Site Onisep à consulter Numéro spécial l’Etudiant à acheter / Inscription avant le ... / Voir avec mon mentor s’il a des contacts Candidatures spontanées dans l’entreprises X, Y, Z..."
                              class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal"
                              @input="updateExperience(experience)"
                              @focus="enableChangeSteps(true, experience)"
                              @blur="enableChangeSteps(false, experience)" />
                  </div>
                  <div v-if="!changeSteps[experience.id] && experience.steps" class="bg-blue-lightest text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
                    {{ experience.steps.substring(0, 100) + '...' }}
                    <img v-if="isStream ? !$store.state.auth.user.config.stream_ids : !$store.state.auth.user.config.domain_ids" src="~/assets/img/pencil_2.svg" class="p-3 absolute pin-b pin-r cursor-pointer" @click="enableChangeSteps(true, experience)">
                  </div>
                </div>
                <div class="md:hidden xl:hidden h-px w-full bg-blue-lighter"/>
                <div class="w-col-custom sm:w-full px-2 sm:px-12 sm:py-5">
                  <div class="md:hidden xl:hidden text-blue text-2xl font-bold w-full justify-center flex py-3"> Mes rendez-vous</div>
                  <date-picker :isDisabled="isStream ? !!$store.state.auth.user.config.stream_ids : !!$store.state.auth.user.config.domain_ids" :placeholder="'text'" :value="experience.date" :pickerIndex="index.toString() + '-' + indexExp.toString()" @dateChanged="updateDate(experience, $event)"/>
                </div>
                <div class="md:hidden xl:hidden h-px w-full bg-blue-lighter"/>
                <div class="w-col-custom sm:w-full px-2 sm:px-12 sm:py-5">
                  <div class="md:hidden xl:hidden text-blue text-2xl font-bold w-full justify-center flex py-3"> Mes résultats</div>
                  <div v-if="changeFeedback[experience.id] || !experience.feedback" class="border-blue-sky border-solid border-1 rounded-xlg py-6 px-2 block w-full">
                    <textarea v-model="experience.feedback"
                              :disabled="isStream ? $store.state.auth.user.config.stream_ids : $store.state.auth.user.config.domain_ids"
                              class="bg-white text-blue w-full border-transparent appearance-none leading-normal font-normal"
                              @input="updateExperience(experience)"
                              @focus="enableChangeFeedback(true, experience)"
                              @blur="enableChangeFeedback(false, experience)" />
                  </div>
                  <div v-if="!changeFeedback[experience.id] && experience.feedback" class="bg-blue-lightest text-blue summary-block rounded-xlg pb-12 pt-8 px-4 block w-full appearance-none leading-normal relative">
                    {{ experience.feedback.substring(0, 100) + '...' }}
                    <img v-if="isStream ? !$store.state.auth.user.config.stream_ids : !$store.state.auth.user.config.domain_ids" src="~/assets/img/pencil_2.svg" class="p-3 absolute pin-b pin-r cursor-pointer" @click="enableChangeFeedback(true, experience)">
                  </div>
                </div>
                <div v-if="isStream ? !$store.state.auth.user.config.stream_ids : !$store.state.auth.user.config.domain_ids" class="w-col-custom last sm:w-full px-2 text-center sm:py-4">
                  <span class="inline-block align-middle h-full"/><img class="align-middle cursor-pointer" src="~/assets/img/trash.svg" @click="openModalDelete(group, experience)">
                </div>
              </div>
            </div>
            <div v-if="isStream ? !$store.state.auth.user.config.stream_ids : !$store.state.auth.user.config.domain_ids" class="py-4 text-center">
              <span class="mx-auto w-12 h-12 font-bold text-blue border-solid border-blue border-2 rounded-full text-5xl text-center align-middle inline-block pt-2 cursor-pointer hover:text-orange hover:border-orange" @click="createExperience(group)">
                +
              </span>
            </div>
          </div>
        </transition>
        <div v-if="index !== (groupedExperiences.length - 1) && !group.collapse && !groupedExperiences[index + 1].collapse"
             class="w-full h-px bg-blue-lighter mt-16 mb-16 opacity-50"/>
      </div>
      <div v-if="(isStream ? !$store.state.auth.user.config.stream_ids : !$store.state.auth.user.config.domain_ids) && !isManager" class="mt-16">
        <button v-if="availableDomains.length > 0" class="button text-blue border-b-1 border-solid border-blue rounded-full hover:text-white" @click="openModalDomains()">
          <span v-if="isStream">J'ajoute une nouvelle filière</span>
          <span v-else>J'ajoute un nouveau domaine</span>
        </button>
      </div>
      <div v-if="!isManager" class="mt-4">
        <button v-if="groupedExperiences.length > 0 && (isStream ? !$store.state.auth.user.config.stream_ids : !$store.state.auth.user.config.domain_ids)" class="button button--blue" @click="openModalValidate()">Je valide mon plan d'action</button>
      </div>
      <div v-show="modalDelete.open || modalDomains.open || modalValidate.open"
           class="z-151 bg-blue-lighter opacity-75 fixed h-full w-full pin-t pin-l" />
      <div v-show="modalDelete.open"
           class="z-152 fixed custom-fixed-position">
        <div class="bg-white rounded-xlg flex flex-col opacity-1 p-10">
          <span class="text-blue text-xl">Êtes vous sur de vouloir supprimer cette ligne ?</span>
          <p class="italic text-sm">Cette action est irreversible</p>
          <div class="text-right h-16 mt-4">
            <button class="button button--blue" @click="deleteExperience(modalDelete.group, modalDelete.experience)">Supprimer</button>
            <button class="button button--white border-b-1 rounded-full border-blue" @click="modalDelete.open = false">Annuler</button>
          </div>
        </div>
      </div>
      <div v-show="modalValidate.open"
           class="z-152 fixed w-1/3 custom-fixed-position">
        <div class="bg-white rounded-xlg flex flex-col opacity-1 p-10 overflow-y-auto custom-modal-height">
          <div class="text-center text-5xl font-bold text-blue">Mes choix finaux</div>
          <p class="text-blue text-xl text-center mt-8">
            C'est le moment de <span class="font-bold">confirmer tes choix</span>.<br>Coche les domaines que tu souhaites garder.
          </p>
          <div v-for="(group, key) in groupedExperiences" :key="key">
            <div class="w-full h-px bg-blue-lighter mb-4 mt-4 opacity-50" />
            <div class="w-2/3 inline-block">
              <h1 :class="{'bg-orange': key % 3 === 2, 'bg-green': key % 3 === 1, 'bg-blue-sky': key % 3 === 0}"
                  class="px-4 py-2 bg-blue-sky text-white text-xl inline-block rounded-lg sans font-bold">{{ key + 1 }}. {{ group.domain.name }}</h1>
            </div>
            <div class="w-1/4 inline-block text-right">
              <div class="checkbox w-8 h-8 float-right">
                <input :id="group.domain.id"
                       v-model="group.domain.validated"
                       name="donnees"
                       type="checkbox"
                       @click="changeDomainValue(group.domain)">
                <label :for="group.domain.id" />
              </div>
            </div>
            <div v-if="key === (groupedExperiences.length - 1)" class="w-full h-px bg-blue-lighter mb-4 mt-4 opacity-50" />
          </div>
          <div class="text-center h-16 mt-4">
            <button :disabled="nbValidatedDomains < 1" class="button button--blue" @click="validateDomains()">Je confirme</button>
          </div>
        </div>
      </div>
      <div v-show="modalDomains.open"
           class="z-152 fixed custom-fixed-position">
        <div class="bg-white rounded-xlg flex flex-col opacity-1 p-10">
          <span class="text-blue text-xl">Choisissez <span v-if="isStream">la filière</span><span v-else>le domain</span> à ajouter</span>
          <select v-model="modalDomains.domain_id" class="w-full mt-4 px-2 py-4 rounded-xlg bg-blue-lightest text-blue border-1 border-blue border-solid">
            <option v-for="(domain, index) in availableDomains" :key="index" :value="domain.id">{{ domain.name }}</option>
          </select>
          <div class="text-right h-16 mt-4">
            <button class="button button--blue" @click="createDomain(modalDomains.domain_id)">Valider</button>
            <button class="button button--white border-b-1 rounded-full border-blue" @click="modalDomains.open = false">Annuler</button>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <img src="~/assets/img/loader.gif" class="block mx-auto mt-16 w-16 h-16">
    </div>
  </div>
</template>

<script>
import _ from 'lodash'
import DatePicker from '~/components/dashboard/DatePicker'

export default {
  name: 'Rex',
  components: { DatePicker },
  props: {
    user_id: {
      type: String,
      default: function() {
        return ''
      }
    },
    domains: {
      type: Array,
      default: function() {
        return []
      }
    },
    isStream: {
      type: Boolean,
      default: function() {
        return false
      }
    },
    isManager: {
      type: Boolean,
      default: function() {
        return false
      }
    }
  },
  data() {
    return {
      loading: true,
      groupedExperiences: [],
      changeActions: [],
      changeFeedback: [],
      changeSteps: [],
      debounceTimeout: null,
      modalDelete: {
        experience: null,
        group: null,
        open: false
      },
      modalDomains: {
        domain_id: null,
        open: false
      },
      modalValidate: {
        open: false
      },
      availableDomains: [],
      nbValidatedDomains: null
    }
  },
  created() {
    this.getExperiences()
  },
  methods: {
    async getExperiences() {
      let response = await this.$api.experiences.getList( {
        page: 1,
        numberPerPage: 1000,
        user_id: this.user_id || this.$store.state.auth.user.userID,
        kind: this.isStream ? 'stream' : 'domain'
      } )

      const experiences = response.data || []

      const groupedExperiences = this.groupedExperiences

      const usedDomains = _.filter( this.domains, function( domain ) {
        return _.includes( _.map( experiences, 'domain_id' ), domain.id )
      } );

      usedDomains.forEach( function( domain ) {
        groupedExperiences.push( {
          domain: domain,
          experiences: _.filter( experiences, { domain_id: domain.id } )
        } )
      } )

      this.groupedExperiences.forEach( function( groupedExperience ) {
        groupedExperience.experiences = _.orderBy( groupedExperience.experiences, ['updated_date'], ['DESC'] )
      } )

      this.groupedExperiences = _.orderBy( this.groupedExperiences, ['experiences[0].updated_date'], ['DESC'] )

      this.availableDomains = this.domains.filter( function( domain ) {
        return !_.includes( usedDomains, domain )
      } )

      if ((this.isStream && this.$store.state.auth.user.config.stream_ids) || (!this.isStream && this.$store.state.auth.user.config.domain_ids)) {
        this.collapseAllGroups()
      }

      this.loading = false

      this.$forceUpdate();
    },
    async createExperience( group ) {
      const experience = {
        domain_id: group.domain.id,
        user_id: this.$store.state.auth.user.userID,
        kind: this.isStream ? 'stream' : 'domain'
      }

      let response = await this.$api.experiences.create( experience )

      experience.id = response.data.res.id || ''

      group.experiences.push( experience )
    },
    createDomain() {
      const emptyExperience = {
        domain_id: this.modalDomains.domain_id,
        user_id: this.$store.state.auth.user.userID,
        kind: this.isStream ? 'stream' : 'domain'
      }

      this.groupedExperiences.push( {
        domain: _.find( this.domains, { id: emptyExperience.domain_id } ),
        experiences: []
      } )

      this.createExperience( this.groupedExperiences.slice( -1 ).pop() )
      this.createExperience( this.groupedExperiences.slice( -1 ).pop() )

      this.availableDomains = _.without( this.availableDomains, _.find( this.domains, { id: emptyExperience.domain_id } ) )

      this.modalDomains.open = false
    },
    updateExperience( experience ) {
      if ( this.debounceTimeout ) clearTimeout( this.debounceTimeout );
      this.debounceTimeout = setTimeout( () => {
        this.$api.experiences.updateMine( experience )
      }, 500 );
    },
    async deleteExperience( group, experience ) {
      await this.$api.experiences.deleteMine( { ids: [experience.id], user_id: this.$store.state.auth.user.userID } )
      group.experiences = _.without( group.experiences, experience )
      this.modalDelete.open = false

      if ( group.experiences.length === 0 ) {
        this.groupedExperiences = _.without( this.groupedExperiences, group )
        this.availableDomains.push( group.domain )
      }
    },
    enableChangeActions( value, experience ) {
      this.changeActions[experience.id] = value
      this.$forceUpdate()
    },
    enableChangeFeedback( value, experience ) {
      this.changeFeedback[experience.id] = value
      this.$forceUpdate()
    },
    enableChangeSteps( value, experience ) {
      this.changeSteps[experience.id] = value
      this.$forceUpdate()
    },
    collapseGroup( group, value ) {
      group.collapse = value
      this.$forceUpdate()
    },
    collapseAllGroups() {
      const collapseGroup = this.collapseGroup
      this.groupedExperiences.forEach(function(group) {
        collapseGroup(group, true)
      })
    },
    openModalDelete( group, experience ) {
      this.modalDelete = {
        experience: experience,
        group: group,
        open: true
      }
    },
    openModalDomains() {
      this.modalDomains = {
        domain_id: this.domains[0].id,
        open: true
      }
    },
    openModalValidate() {
      this.modalValidate = {
        open: true
      }
    },
    updateDate( experience, date ) {
      const dateTime = new Date( date ).getTime()
      if ( experience.date !== dateTime ) {
        experience.date = dateTime
        this.updateExperience( experience )
      }
    },
    changeDomainValue(domain) {
      domain.validated = !domain.validated

      this.nbValidatedDomains = _.filter(this.groupedExperiences, function(group) {
        return group.domain.validated
      }).length
    },
    async validateDomains() {
      const domain_ids = _.map(_.filter(this.groupedExperiences, function(group) {
        return group.domain.validated
      }), 'domain.id')

      try {
        this.$store.commit(this.isStream ? 'auth/setUserStreams' : 'auth/setUserDomains', domain_ids)
        await this.$api.user.updateUser(this.$store.state.auth.user)
        this.$emit('domainsValidated')

        this.$toast.success(this.isStream ? 'Filières validés' : 'Domaines validés', {
          position: 'bottom-right',
          duration: 3500
        })
        this.collapseAllGroups()

        const user = this.$store.state.auth.user
        if (!this.isStream && this.stepCalculator(user.config.currentStep) < 5) {
          this.$store.commit('auth/setCurrentStep', 5)
          await this.$api.user.updateUser(this.$store.state.auth.user)
        } else if (this.isStream && this.stepCalculator(user.config.currentStep) < 8) {
          this.$store.commit('auth/setCurrentStep', 8)
          await this.$api.user.updateUser(this.$store.state.auth.user)
        }

        this.modalValidate.open = false

        if (!this.isStream) {
          this.$router.push('/dashboard/student/filieres/introspection/explanations')
        }
      } catch (e) {
        return 'error'
      }
    },
    hasValidateDomain(domain) {
      return _.includes(this.$store.state.auth.user.config.domain_ids, domain.id)
    },
    hasValidateStream(domain) {
      return _.includes(this.$store.state.auth.user.config.stream_ids, domain.id)
    }
  }
}
</script>

<style lang="scss" scoped>
.w-col-custom {
  @media (min-width: 768px) {
    width: 23.5%;

    &.last {
      width: 6%;
    }
  }

}

.summary-block {
  @media (min-width: 768px) {
   height: 11em;
  }
}

textarea {
 @media (min-width: 768px) {
   height: 11em;
  }
  outline: none;

  &::-webkit-scrollbar {
    width: 3px;
  }

  &::-webkit-scrollbar-track {
    @apply bg-blue-lightest;
    border-radius: 50px;
  }

  &::-webkit-scrollbar-thumb {
    border-radius: 50px;
    @apply bg-emerald;
  }
}

.custom-fixed-position {
  top: 50%;
  left: 50%;
  transform: translateX(-50%) translateY(-50%);
}

.minus-custom {
  letter-spacing: -3px;
  padding-right: 1px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}

.custom-modal-height {
  max-height: 70vh;

  &::-webkit-scrollbar
  {
    width: 12px;;
    height: 14px;
  }

  &::-webkit-scrollbar-track
  {
    @apply bg-blue-lightest;
    border-radius: 50px;
  }

  &::-webkit-scrollbar-thumb {
    height: 6px;
    border: 4px solid rgba(0, 0, 0, 0);
    background-clip: padding-box;
    -webkit-border-radius: 7px;
    background-color: rgba(0, 0, 0, 0.15);
    -webkit-box-shadow: inset -1px -1px 0px rgba(0, 0, 0, 0.05), inset 1px 1px 0px rgba(0, 0, 0, 0.05);
    border-radius: 50px;
    @apply bg-yellow;
  }

  ::-webkit-scrollbar-button {
    width: 0;
    height: 0;
    display: none;
  }
  ::-webkit-scrollbar-corner {
    background-color: transparent;
  }
}
</style>

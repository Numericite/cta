<template>
  <div :class="{'isManager': isManager}"
       class="autoportrait relative h-full flex flex-col pt-12 justify-center">
    <div v-if="!isManager && canDoFinal" class="mb-24 text-right sm:text-left">
      <button class="button button--orange" @click="printAutoportrait()">Imprimer l'autoportrait</button>
    </div>
    <div class="activity-link dashed absolute border-dashed border-l-2 border-blue-lighter z-0 h-full sm:pin-l sm:ml-8" />
    <div id="premieres-pistes" />
    <modal-final v-if="finalUserLog"
                 v-show="openFinalModal"
                 :activity="finalUserLog"
                 :isManager="isManager"
                 :domains="domains"
                 link="/dashboard/student/domaines/introspection/premieres-pistes"
                 class="modal modal-8"
                 bgColor="blue"
                 @close="closeModal" />
    <div class="activity activity-6 z-10 flex w-full relative justify-center sm:justify-start items-center">
      <img v-if="!finalUserLog && canDoFinal"
           class="my-8 sm:my-16 sm:h-16 sm:w-16"
           width="170"
           height="170"
           src="~/assets/img/autoportrait/illu-final-d.svg">
      <div :class="{'activity-right': !finalUserLog, 'w-4/5': finalUserLog}" class="w-1/2 sm:w-full sm:w-full h-full sm:w-full">
        <div v-if="finalUserLog && !openFinalModal"
             class="cardShadow rounded-xl bg-white py-8 px-12 relative h-full sm:mr-0">
          <div class="w-1/2 sm:w-full inline-block align-top relative">
            <div class="flex">
              <div class="text-blue font-bold text-5xl sm:text-base">
                {{ finalUserLog.name }}
              </div>
            </div>
            <div class="font-semibold text-xl mt-4 text-blue-lighter">
              <span class="text-blue">{{ finalUserLog.selection_ids.length }}</span> verbes d'action
            </div>
            <div class="font-semibold text-xl mt-4 text-blue-lighter">
              <span class="text-blue">{{ finalUserLog.choice_ids.length }}</span> domaines
            </div>
            <div class="badges relative mt-6 sm:mt-6">
              <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
                +{{ finalUserLog.choice_ids.length }}
              </div>
              <img v-for="(choice, key) in finalUserLog.choices"
                   :key="key"
                   :class="{'bg-yellow': key === 2, 'ml-4 bg-topaze': key === 0, 'ml-2 bg-orange': key === 1}"
                   :src="choice.img_path"
                   class="absolute card-icon rounded-full p-2 border border-solid border-white">
            </div>
            <div class="badges relative mt-6 sm:mt-6 ml-20">
              <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
                +{{ finalUserLog.selection_ids.length }}
              </div>
              <img v-for="(selection, key) in [1,2,3]"
                   :key="key"
                   :class="{'border-yellow': key === 2, 'ml-4 border-topaze': key === 0, 'ml-2 border-orange': key === 1}"
                   src="~/assets/img/autoportrait/heart.svg"
                   class="absolute card-icon rounded-full p-4 border border-solid border-2 bg-white">

            </div>
            <div class="cursor-pointer"
                 @click="openFinalModal = true">
              <div class="mt-48 flex items-center text-blue uppercase leading-normal tracking-wide font-bold text-xl sm:text-sm">
                Tout voir
                <svg class="ml-3"
                     xmlns="http://www.w3.org/2000/svg"
                     width="9"
                     height="14"
                     viewBox="0 0 9 14">
                  <path fill="none"
                        fill-rule="nonzero"
                        stroke="#432CB4"
                        stroke-linecap="round"
                        stroke-width="1.6"
                        d="M1 1.123l6 5.595-6 6.405" />
                </svg>
              </div>
            </div>
          </div>
          <div class="w-1/3 sm:hidden inline-block pt-8 final-img-bloc">
            <img class="w-full" src="~/assets/img/autoportrait/final.png">
          </div>
        </div>
        <div v-else-if="!openFinalModal && !isManager && canDoFinal"
             class="text-left mt-24 sm:ml-2">
          <div class="text-blue text-3xl sm:text-lg font-extrabold">
            Mes premières pistes
          </div>
          <nuxt-link to="/dashboard/student/domaines/introspection/premieres-pistes">
            <button class="button button--blue mt-4 ">Demarrer l'activité</button>
          </nuxt-link>
        </div>
      </div>
    </div>

    <div id="activityList" />
    <div :class="{'mt-24': canDoFinal}"
         class="activity activity-6 flex-col z-10 mt-24 sm:mt-16 flex w-full relative sm:justify-start">

      <!-- BLOC ACTIVITE -->
      <div class="flex flex-col relative">
        <div v-for="(activity, index) in activities" :id="activity.slug" :key="index" :class="[ index%2 === 0 ? 'justify-end flex sm:pr-0 pr-80px' : 'flex flex-row-reverse justify-end pl-80px sm:pl-0']" class="w-full min-h-180 relative my-16 sm:justify-start items-center" >
          <div class="absolute activityImg">
            <img v-if="isDone(activity.id)"
                 :src="'/img/autoportrait/' + activity.slug + '.svg'"
                 class=" sm:my-16 sm:h-16 sm:w-16"
                 width="134"
                 height="135">
            <img v-else
                 :src="'/img/autoportrait/' + activity.slug + '-d' + '.svg'"
                 class="mx-4 sm:my-16 sm:h-16 sm:w-16 "
                 width="134"
                 height="135">
          </div>
          <div v-show="!activity.openModal" class="w-1/3 flex flex-col sm:w-full sm:ml-20 sm:w-full h-full">
            <div v-if="isDone(activity.id)"
                 class="cardShadow rounded-xl bg-white py-4 px-6 min-h-180 relative h-full sm:mr-0 sm:h-48">
              <div class="flex">
                <div class="text-blue font-extrabold text-2xl sm:text-base">
                  {{ activity.name }}
                </div>
              </div>
              <div v-if="logs[index].config && logs[index].config.domainsLiked" class="font-semibold text-xl sm:text-base mt-4 text-blue-lighter">
                Des domaines qui m'intéressent
              </div>
              <div v-if="logs[index].config && logs[index].config.jobsLiked" class="font-semibold text-xl sm:text-base mt-4 text-blue-lighter">
                Des métiers qui m'intéressent
              </div>
              <div v-if="!logs[index].config" class="font-semibold text-xl sm:text-base mt-4 text-blue-lighter">
                Pas de contact avec le monde du travail
              </div>
              <div v-if="logs[index].selection_ids"
                   class="font-semibold text-xl sm:text-base mt-4 text-blue-lighter">
                <span class="text-blue"> {{ logs[index].selection_ids.length }} </span> résultats dominants
              </div>
              <div v-if="logs[index].choice_ids"
                   class="font-semibold text-xl sm:text-base mt-4 text-blue-lighter">
                <span class="text-blue"> {{ logs[index].choice_ids.length }} </span> actions clés
              </div>
              <div class="flex relative my-8">
                <!--              1ER BADGE-->
                <div v-if="logs[index].selections && activities[index].slug !== 'rencontres'" class="badges relative sm:mt-6 ml-2 ">
                  <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
                    +{{ logs[index].selection_ids.length }}
                  </div>
                  <div class="relative">
                    <img v-for="(selection, key) in logs[index].selections" v-if="selection.img_path !== '' "
                         :key="key"
                         :class="{'bg-topaze ml-4': key === 2, 'absolute bg-orange ml-2 ': key === 0, 'absolute bg-yellow': key === 1}"
                         :src="selection.img_path"
                         class=" card-icon rounded-full p-2 border border-solid border-white">
                  </div>
                </div>
                <!--              2EME BADGE-->
                <div v-if="logs[index].choices" :class="[!logs[index].selections ? 'ml-0 sm:ml-2' : 'ml-4']" class="badges relative sm:mt-6">
                  <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
                    +{{ logs[index].choice_ids.length }}
                  </div>
                  <div class="relative">
                    <img v-for="(choice, key) in [1,2,3]"
                         :key="key"
                         :class="{'border-yellow ml-4': key === 2, 'absolute ml-2 border-topaze': key === 0, 'absolute border-orange': key === 1}"
                         src="~/assets/img/autoportrait/heart.svg"
                         class="card-icon rounded-full p-4 border border-solid border-2 bg-white">
                  </div>
                </div>
              </div>
              <div class=" absolute pin-r pin-b mr-4 mb-6 cursor-pointer"
                   @click="openModal(index)">
                <div class="flex items-center text-blue uppercase leading-normal tracking-wide font-bold text-xl sm:text-sm">
                  Tout voir
                  <svg class="ml-3"
                       xmlns="http://www.w3.org/2000/svg"
                       width="9"
                       height="14"
                       viewBox="0 0 9 14">
                    <path fill="none"
                          fill-rule="nonzero"
                          stroke="#432CB4"
                          stroke-linecap="round"
                          stroke-width="1.6"
                          d="M1 1.123l6 5.595-6 6.405" />
                  </svg>
                </div>
              </div>
            </div>

            <div v-else-if="!isManager" :class="[ index%2 === 0 ? '' : 'pl-24']"
                 class="my-8 sm:mt-6 sm:ml-2">
              <div class="text-blue text-3xl sm:text-lg font-extrabold">
                {{ activity.name }}
              </div>
              <nuxt-link :to="'/dashboard/student/domaines/introspection/' + activity.slug">
                <button class="button button--blue mt-4 ">Demarrer l'activité</button>
              </nuxt-link>
            </div>
          </div>
          <!--    MODAL RENCONTRE -->
          <modal-rencontre v-if="activity.modal === 'rencontres'"
                           v-show="activity.openModal"
                           :activity="getUserLog(activity.id)"
                           :link="'/dashboard/student/domaines/introspection/rencontres'"
                           :isManager="isManager"
                           class="modal modal-6"
                           bgColor="yellow"
                           @close="closeModal(index)" />
          <!--    MODAL SIMPLE -->
          <modal-simple v-if="activity.modal === 'simple'"
                        v-show="activity.openModal"
                        :link="'/dashboard/student/domaines/introspection/' + activity.slug"
                        :activity="getUserLog(activity.id)"
                        :isManager="isManager"
                        class="modal modal-2"
                        bgColor="blue-sky"
                        @close="closeModal(index)" />
          <!--    MODAL TRIPLE -->
          <modal-triple v-if="activity.modal === 'triple'"
                        v-show="activity.openModal"
                        :link="'/dashboard/student/domaines/introspection/' + activity.slug"
                        :activity="getUserLog(activity.id)"
                        :isManager="isManager"
                        class="modal modal-3"
                        bgColor="purple"
                        @close="closeModal(index)" />
        </div>
      </div>

      <!--    GO FINAL-->
      <div v-if="!isManager && canDoFinal" id="printable-part" class="printable-container hidden">
        <div class="pb-4 px-8 pt-8 bg-white" style="width: 1000.5px;">
          <div class="h-full">
            <div class="flex flex-wrap justify-center">
              <!-- A GARDER-->
              <div class="w-full">
                <h3 class="text-center text-blue font-bold text-4xl">MON AUTOPORTRAIT</h3>
                <p class="text-center italic mt-2">{{ currentDate }}</p>
                <div class="flex flex-no-wrap w-full flex-start justify-between px-12 mb-8">
                  <div class="text-3xl text-sm text-blue mb-4 font-bold">{{ $store.state.auth.user.firstName }} {{ $store.state.auth.user.lastName }}</div>
                  <div class="text-xl text-orange font-bold">{{ $store.state.auth.user.school.name }} ({{ $store.state.auth.user.classroom.name }})</div>
                </div>
              </div>
              <div id="printable-container-activities" class="block relative">
                <div v-for="(activity, index) in activities" :key="index" :class="[Object.values(modalsAutoportrait.modalTriple).indexOf(activity.slug) > -1 ? 'w-full': 'w-1/2 ']" class="relative inline-block">
                  <printable-container :activity="activity" :userLog="logs[index]" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--    GO FINAL MODAL-->
    <go-final v-if="finalPopup" @close="closeFinalPopup()" />
  </div>
</template>

<script>
  import ModalTriple from './autoportrait/ModalTriple'
  import ModalSimple from './autoportrait/ModalSimple'
  import ModalRencontre from './autoportrait/ModalRencontre'
  import ModalFinal from './autoportrait/ModalFinal'
  import PrintableContainer from './printable/PrintableContainer'
  import GoFinal from './GoFinal'
  import { MODAL_AUTOPORTRAIT } from '~/config'

  import _ from 'lodash'

  export default {
    name: 'AutoportraitComponent',
    components: {
      ModalTriple,
      ModalSimple,
      ModalRencontre,
      ModalFinal,
      GoFinal,
      PrintableContainer
    },
    props: {
      course: {
        type: Object,
        default: () => {
          return {}
        }
      },
      isManager: {
        type: Boolean,
        default: () => {
          return false
        }
      },
      activityList:  {
        type: Array,
        default: () => {
          return null
        }
      },
      userLogs:  {
        type: Array,
        default: () => {
          return null
        }
      },
      finalUserLog: {
        type: Object,
        default: () => {
          return null
        }
      },
      selections: {
        type: Array,
        default: () => {
          return []
        }
      },
      choices: {
        type: Array,
        default: () => {
          return []
        }
      },
      domains: {
        type: Array,
        default: () => {
          return []
        }
      }
    },
    data() {
      return {
        currentDate: new Date().toLocaleDateString(),
        activities: [],
        logs: [],
        openFinalModal: false,
        modalsAutoportrait: MODAL_AUTOPORTRAIT,
        finalPopup: false,
        canDoFinal: false,
        school: { name: '' },
        classroom: { name: '' }
      }
    },
    created() {
      const chapters = this.course.chapters ? this.course.chapters : this.$store.state.auth.user.course.chapters
      let domain = _.find(chapters, { slug: 'activity-area' })
      let introspection = _.find(domain.children, { slug: 'introspection' })

      this.activities = this.arrayRemoveUndefined(_.orderBy(_.map(this.activityList, activity => {
        if(_.includes(_.map(introspection.activities, 'id'), activity.id)) {
          activity.version_id = _.get(_.find(introspection.activities, {id: activity.id}), 'version_id', '')
          activity.index = _.map(introspection.activities, 'id').indexOf(activity.id)
          this.logs[activity.index] = _.find(this.userLogs, {activity_id: activity.id})
          return activity
        }
      }), 'index', 'asc'))

      this.activities.forEach((item) => {
        if(_.includes(this.modalsAutoportrait.modalRencontre, item.slug)) {
          item.modal = 'rencontres'
        }
        if(_.includes(this.modalsAutoportrait.modalTriple, item.slug)) {
          item.modal = 'triple'
        }
        if(_.includes(this.modalsAutoportrait.modalSimple, item.slug)) {
          item.modal = 'simple'
        }
      })
      // ADD SELECTIONS TO USERLOG ITEM
      if(this.logs) {
       this.logs.forEach((item, i) => {
         if(item && item.selection_ids) {
           item.selections = _.filter(this.selections, function( selection ) {
             return item.selection_ids.indexOf( selection.id ) !== -1
           } )
         }
         if (item && item.choice_ids ) {
           item.choices = _.filter( this.choices, function( choice ) {
             return item.choice_ids.indexOf( choice.id ) !== -1
           } )
         }
       })
      }
      // END ADD SELECTIONS

      if (this.finalUserLog) {
        const finalUserLog = this.finalUserLog
        if (this.finalUserLog.selection_ids) {
          finalUserLog.selections = _.filter(this.domains, function (domain) {
            return finalUserLog.selection_ids.indexOf(domain.id) !== -1
          })
        }

        if (this.finalUserLog.choice_ids) {
          finalUserLog.choices = _.filter(this.domains, function (domain) {
            return finalUserLog.choice_ids.indexOf(domain.id) !== -1
          })
        }
      }

      this.canDoFinal = this.arrayRemoveUndefined(this.logs).length >= this.activities.length

      if (this.canDoFinal && !this.finalUserLog) {
        this.openEveryModals()
      }
    },
    async mounted() {
      if (!this.isManager && this.canDoFinal && !this.$store.state.auth.user.config.sawFinalPopup) {
        this.finalPopup = true
      }
      const route = this.$route
      if (route.hash) {
        setTimeout(function() {
          const element = document.getElementById(route.hash.slice(1))
          if (element) {
            element.scrollIntoView();
            window.scrollBy(0, -100);
          } }, 350)
      }
    },
    methods: {
      getUserLog(id) {
        return _.find(this.logs, { 'activity_id': id})
      },
      isDone(activity_id) {
        return _.includes(_.map(this.logs, 'activity_id'), activity_id)
      },
      openModal(index) {
        this.activities[index].openModal = true
        this.$forceUpdate()
      },
      closeModal(index) {
        if (index === 'final') {
          this.openFinalModal = false
        } else {
          this.activities[index].openModal = false
        }
        this.$forceUpdate()
      },
      closeFinalPopup() {
        this.finalPopup = false
        document.getElementById('premieres-pistes').scrollIntoView()
        window.scrollBy(0, -130);
      },
      openEveryModals() {
        this.activities.forEach((activity) => {
          activity.openModal = true
        })
      },
      printAutoportrait() {
        const printContents = document.getElementById('printable-part').innerHTML;

        const css = '@page { size: landscape; margin: 0; }',
          head = document.head || document.getElementsByTagName('head')[0],
          style = document.createElement('style');

        if (style.styleSheet){
          style.styleSheet.cssText = css;
        } else {
          style.appendChild(document.createTextNode(css));
        }

        head.appendChild(style);
        document.body.innerHTML = head.innerHTML + printContents;

        setTimeout(() => {
          window.print();
          this.$router.go()
        }, 1000)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .autoportrait.isManager {
    z-index: 101;
  }

  .activityImg {
    left: 50%;
    transform: translate(-50%, 0);
  }

  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }

  .card-icon {
    width: 47px;
    height: 47px;
  }

  .modal {
    transition: all 0.3s ease;
    @media (min-width: 640px) {
      // &.modal-6 {
      //   transform: translateY(-590px);
      // }
      // &.modal-5 {
      //   transform: translateY(-320px);
      // }
      // &.modal-4 {
      //   transform: translateY(-30px);
      // }
      // &.modal-3 {
      //   transform: translateY(240px);
      // }
      // &.modal-2 {
      //   transform: translateY(510px);
      // }
      // &.modal-1 {
      //   transform: translateY(510px);
      // }
    }
  }

  .activityImg {
      @apply  absolute ;
      @media (max-width: 765px) {
        @apply pin-l ml-8
    }
  }

  .activity-link {
    right: 50%;
    @media (max-width: 640px) {
      transform: translateY(-20px);
      height: 86%;
    }
  }

  .activity {
    transform: translateY(-30px);
  }

  .badges {
    @media (max-width: 640px) {
      transform: scale(0.9) translateX(-20px);
    }
  }

  .activity-right {
    @media (min-width: 640px) {
      transform: translateX(75%);
      @apply absolute pin-t;
    }
  }

  .button--blue {
    @media (max-width: 640px) {
      transform: scale(0.9) translateX(-15px);
    }
  }

  .activity-left {
    @media (min-width: 640px) {
      transform: translateX(-75%);
      @apply absolute pin-t;
    }
  }

  .final-img-bloc {
    transform: translateX(-53%);
  }

</style>

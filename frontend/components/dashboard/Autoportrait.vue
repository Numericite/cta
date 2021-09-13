<template>
  <div :class="{'isManager': isManager}"
       class="autoportrait relative h-full flex flex-col pt-12 justify-center">
    <div v-if="!isManager && canDoFinal" class="mb-24 text-right">
      <button class="button button--orange" @click="printAutoportrait()">Imprimer l'autoportrait</button>
    </div>
    <div class="activity-link dashed absolute border-dashed border-l-2 border-blue-lighter z-0 h-full" />
    <div id="activityFinal" />
    <modal-final v-if="activityFinal"
                 v-show="modal['activityFinal']"
                 :activity="activityFinal"
                 :isManager="isManager"
                 link="/dashboard/student/domaines/introspection/premieres-pistes"
                 class="modal modal-8"
                 bgColor="blue"
                 @close="closeModal" />
    <div class="activity activity-6 z-10 flex w-full relative justify-center items-center">
      <img v-if="!activityFinal && canDoFinal"
           class="my-8"
           width="170"
           height="170"
           src="~/assets/img/autoportrait/illu-final-d.svg">
      <div :class="{'activity-right': !activityFinal, 'w-4/5': activityFinal}" class="w-1/2 h-full">
        <div v-if="activityFinal && !modal.activityFinal"
             class="cardShadow rounded-xl bg-white py-8 px-12 relative h-full">
          <div class="w-1/2 inline-block align-top relative">
            <div class="flex">
              <div class="text-blue font-bold text-5xl">
                {{ activityFinal.name }}
              </div>
            </div>
            <div class="font-semibold text-xl mt-4 text-blue-lighter">
              <span class="text-blue">{{ activityFinal.selection_ids.length }}</span> verbes d'action
            </div>
            <div class="font-semibold text-xl mt-4 text-blue-lighter">
              <span class="text-blue">{{ activityFinal.choice_ids.length }}</span> domaines
            </div>
            <div class="badges relative mt-6">
              <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
                +{{ activityFinal.choice_ids.length }}
              </div>
              <img v-for="(choice, key) in activityFinal.choices"
                   :key="key"
                   :class="{'bg-yellow': key === 2, 'ml-4 bg-topaze': key === 0, 'ml-2 bg-orange': key === 1}"
                   :src="choice.img_path"
                   class="absolute card-icon rounded-full p-2 border border-solid border-white">
            </div>
            <div class="badges relative mt-6 ml-20">
              <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
                +{{ activityFinal.selection_ids.length }}
              </div>
              <img v-for="(selection, key) in [1,2,3]"
                   :key="key"
                   :class="{'border-yellow': key === 2, 'ml-4 border-topaze': key === 0, 'ml-2 border-orange': key === 1}"
                   src="~/assets/img/autoportrait/heart.svg"
                   class="absolute card-icon rounded-full p-4 border border-solid border-2 bg-white">

            </div>
            <div class="cursor-pointer"
                 @click="modal.activityFinal = true">
              <div class="mt-48 flex items-center text-blue uppercase leading-normal tracking-wide font-bold text-xl">
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
          <div class="w-1/3 inline-block pt-8 final-img-bloc">
            <img class="w-full" src="~/assets/img/autoportrait/final.png">
          </div>
        </div>
        <div v-else-if="!modal.activityFinal && !isManager && canDoFinal"
             class="text-left mt-24">
          <div class="text-blue text-3xl font-extrabold">
            Mes premières pistes
          </div>
          <nuxt-link to="/dashboard/student/domaines/introspection/premieres-pistes">
            <button class="button button--blue mt-4 ">Demarrer l'activité</button>
          </nuxt-link>
        </div>
      </div>
    </div>
    <div id="rencontres" />
    <modal-rencontre v-if="activity1"
                     v-show="modal['activity1']"
                     :activity="activity1"
                     :isManager="isManager"
                     class="modal modal-6"
                     bgColor="yellow"
                     link="/dashboard/student/domaines/introspection/rencontres"
                     @close="closeModal" />
    <div v-show="!modal['activity1']"
         :class="{'mt-24': canDoFinal}"
         class="activity activity-6 z-10 mt-24 flex w-full relative justify-center items-center">
      <img v-if="activity1"
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-6.svg">
      <img v-else
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-6-d.svg">
      <div class="activity-right w-1/3 h-full">
        <div v-if="activity1"
             class="cardShadow rounded-xl bg-white p-4 relative h-full">
          <div class="flex">
            <div class="text-blue font-extrabold text-2xl">
              {{ activity1.name }}
            </div>
          </div>
          <div v-if="activity1.config && activity1.config.domainsLiked" class="font-semibold text-xl mt-4 text-blue-lighter">
            Des domaines qui m'intéressent
          </div>
          <div v-if="activity1.config && activity1.config.jobsLiked" class="font-semibold text-xl mt-4 text-blue-lighter">
            Des métiers qui m'intéressent
          </div>
          <div v-if="!activity1.config" class="font-semibold text-xl mt-8 text-blue-lighter">
            Pas de contact avec le monde du travail
          </div>
          <div class="absolute pin-r pin-b mr-4 mb-6 cursor-pointer"
               @click="modal.activity1 = true">
            <div class="flex items-center text-blue uppercase leading-normal tracking-wide font-bold text-xl">
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
        <div v-else-if="!modal.activity1 && !isManager"
             class="mt-20">
          <div class="text-blue text-3xl font-extrabold">
            Mes premières rencontres professionnelles
          </div>
          <nuxt-link to="/dashboard/student/domaines/introspection/rencontres">
            <button class="button button--blue mt-4 ">Demarrer l'activité</button>
          </nuxt-link>
        </div>
      </div>
    </div>
    <div id="matieres-preferees" />
    <modal-triple v-if="activity2"
                  v-show="modal['activity2']"
                  :activity="activity2"
                  :isManager="isManager"
                  link="/dashboard/student/domaines/introspection/matieres-preferees"
                  class="modal modal-1"
                  bgColor="orange"
                  @close="closeModal" />
    <div v-show="!modal['activity2']"
         class="activity activity-1 z-10 mt-24 flex w-full relative justify-center items-center">
      <img v-if="activity2"
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-1.svg">
      <img v-else
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-1-d.svg">

      <div class="activity-left w-1/3 h-full">
        <div v-if="activity2"
             class="cardShadow rounded-xl bg-white p-4 relative h-full">
          <div class="flex">
            <div class="text-blue font-extrabold text-2xl">
              {{ activity2.name }}
            </div>
          </div>
          <div v-if="activity2.selection_ids"
               class="font-semibold text-xl mt-4 text-blue-lighter">
            <span class="text-blue">{{ activity2.selection_ids.length }}</span> résultats dominants
          </div>
          <div v-if="activity2.choice_ids"
               class="font-semibold text-xl mt-4 text-blue-lighter">
            <span class="text-blue">{{ activity2.choice_ids.length }}</span> actions clés
          </div>
          <div class="badges relative mt-8 ml-2">
            <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
              +{{ activity2.selection_ids.length }}
            </div>
            <img v-for="(selection, key) in activity2.selections"
                 :key="key"
                 :class="{'bg-yellow': key === 2, 'ml-4 bg-topaze': key === 0, 'ml-2 bg-orange': key === 1}"
                 :src="selection.img_path"
                 class="absolute card-icon rounded-full p-2 border border-solid border-white">
          </div>
          <div class="badges relative mt-8 ml-24">
            <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
              +{{ activity2.choice_ids.length }}
            </div>
            <img v-for="(choice, key) in [1,2,3]"
                 :key="key"
                 :class="{'border-yellow': key === 2, 'ml-4 border-topaze': key === 0, 'ml-2 border-orange': key === 1}"
                 src="~/assets/img/autoportrait/heart.svg"
                 class="absolute card-icon rounded-full p-4 border border-solid border-2 bg-white">

          </div>
          <div class="absolute pin-r pin-b mr-4 mb-6 cursor-pointer"
               @click="modal.activity2 = true">
            <div class="flex items-center text-blue uppercase leading-normal tracking-wide font-bold text-xl">
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
        <div v-else-if="!modal.activity2 && !isManager"
             class="text-right mr-6 mt-20">
          <div class="text-blue text-3xl font-extrabold">
            Mes matières préférées
          </div>
          <nuxt-link to="/dashboard/student/domaines/introspection/matieres-preferees">
            <button class="button button--blue mt-4">Demarrer l'activité</button>
          </nuxt-link>
        </div>
      </div>
    </div>
    <div id="traits-de-personnalite" />
    <modal-simple v-if="activity3"
                  v-show="modal['activity3']"
                  :activity="activity3"
                  :isManager="isManager"
                  link="/dashboard/student/domaines/introspection/traits-de-personnalite"
                  class="modal modal-2"
                  bgColor="blue-sky"
                  @close="closeModal" />
    <div v-show="!modal['activity3']"
         class="activity activity-2 z-10 mt-24 flex w-full relative justify-center items-center">
      <img v-if="activity3"
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-2.svg">
      <img v-else
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-2-d.svg">
      <div class="activity-right w-1/3 h-full">
        <div v-if="activity3"
             class="cardShadow rounded-xl bg-white p-4 relative h-full">
          <div class="flex">
            <div class="text-blue font-extrabold text-2xl">
              {{ activity3.name }}
            </div>
          </div>
          <div class="font-semibold text-xl mt-4 text-blue-lighter">
            <span class="text-blue">{{ activity3.choice_ids.length }}</span> résultats dominants
          </div>
          <div class="badges relative mt-8 ml-2">
            <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
              +{{ activity3.choice_ids.length }}
            </div>
            <img v-for="(choice, key) in [1,2,3]"
                 :key="key"
                 :class="{'bg-topaze': key === 2, 'ml-4 bg-yellow': key === 0, 'ml-2 bg-orange': key === 1}"
                 src="~/static/icons/check.svg"
                 class="absolute card-icon rounded-full p-4 border border-solid border-white">
          </div>
          <div class="absolute pin-r pin-b mr-4 mb-6 cursor-pointer"
               @click="modal.activity3 = true">
            <div class="flex items-center text-blue uppercase leading-normal tracking-wide font-bold text-xl">
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
        <div v-else-if="!modal.activity3 && !isManager"
             class="mt-20">
          <div class="text-blue text-3xl font-extrabold">
            Mes traits de personnalités
          </div>
          <nuxt-link to="/dashboard/student/domaines/introspection/traits-de-personnalite">
            <button class="button button--blue mt-4">Demarrer l'activité</button>
          </nuxt-link>
        </div>
      </div>
    </div>
    <div id="centres-dinterets" />
    <modal-triple v-if="activity4"
                  v-show="modal['activity4']"
                  :activity="activity4"
                  :isManager="isManager"
                  link="/dashboard/student/domaines/introspection/centres-dinterets"
                  class="modal modal-3"
                  bgColor="purple"
                  @close="closeModal" />
    <div v-show="!modal['activity4']"
         class="activity activity-3 z-10 mt-24 flex w-full relative justify-center items-center">
      <img v-if="activity4"
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-3.svg">
      <img v-else
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-3-d.svg">

      <div class="activity-left w-1/3 h-full">
        <div v-if="activity4"
             class="cardShadow rounded-xl bg-white p-4 relative h-full">
          <div class="flex">
            <div class="text-blue font-extrabold text-2xl">
              {{ activity4.name }}
            </div>
          </div>
          <div v-if="activity4.selection_ids"
               class="font-semibold text-xl mt-4 text-blue-lighter">
            <span class="text-blue">{{ activity4.selection_ids.length }}</span> résultats dominants
          </div>
          <div v-if="activity4.choice_ids"
               class="font-semibold text-xl mt-4 text-blue-lighter">
            <span class="text-blue">{{ activity4.choice_ids.length }}</span> actions clés
          </div>
          <div class="badges relative mt-8 ml-2">
            <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
              +{{ activity4.selection_ids.length }}
            </div>
            <img v-for="(selection, key) in activity4.selections"
                 :key="key"
                 :class="{'bg-yellow': key === 2, 'ml-4 bg-topaze': key === 0, 'ml-2 bg-orange': key === 1}"
                 :src="selection.img_path"
                 class="absolute card-icon rounded-full p-2 border border-solid border-white">
          </div>
          <div class="badges relative mt-8 ml-24">
            <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
              +{{ activity4.choice_ids.length }}
            </div>
            <img v-for="(choice, key) in [1,2,3]"
                 :key="key"
                 :class="{'border-yellow': key === 2, 'ml-4 border-topaze': key === 0, 'ml-2 border-orange': key === 1}"
                 src="~/assets/img/autoportrait/heart.svg"
                 class="absolute card-icon rounded-full p-4 border border-solid border-2 bg-white">

          </div>
          <div class="absolute pin-r pin-b mr-4 mb-6 cursor-pointer"
               @click="modal.activity4 = true">
            <div class="flex items-center text-blue uppercase leading-normal tracking-wide font-bold text-xl">
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
        <div v-else-if="!modal.activity4 && !isManager"
             class="text-right mr-6 mt-20">
          <div class="text-blue text-3xl font-extrabold">
            Centres d'intérêts
          </div>
          <nuxt-link to="/dashboard/student/domaines/introspection/centres-dinterets">
            <button class="button button--blue mt-4">Demarrer l'activité</button>
          </nuxt-link>
        </div>
      </div>
    </div>
    <div id="competences" />
    <modal-triple v-if="activity5"
                  v-show="modal['activity5']"
                  :activity="activity5"
                  :isManager="isManager"
                  link="/dashboard/student/domaines/introspection/competences"
                  class="modal modal-5"
                  bgColor="topaze"
                  @close="closeModal" />
    <div v-show="!modal['activity5']"
         class="activity activity-4 z-10 mt-24 flex w-full relative justify-center items-center">
      <img v-if="activity5"
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-4.svg">
      <img v-else
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-4-d.svg">
      <div class="activity-right w-1/3 h-full">
        <div v-if="activity5"
             class="cardShadow rounded-xl bg-white p-4 relative h-full">
          <div class="flex">
            <div class="text-blue font-extrabold text-2xl">
              {{ activity5.name }}
            </div>
          </div>
          <div v-if="activity5.selection_ids"
               class="font-semibold text-xl mt-4 text-blue-lighter">
            <span class="text-blue">{{ activity5.selection_ids.length }}</span> résultats dominants
          </div>
          <div v-if="activity5.choice_ids"
               class="font-semibold text-xl mt-4 text-blue-lighter">
            <span class="text-blue">{{ activity5.choice_ids.length }}</span> actions clés
          </div>
          <div class="badges relative mt-8 ml-2">
            <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
              +{{ activity5.selection_ids.length }}
            </div>
            <img v-for="(selection, key) in activity5.selections"
                 :key="key"
                 :class="{'bg-yellow': key === 2, 'ml-4 bg-topaze': key === 0, 'ml-2 bg-orange': key === 1}"
                 :src="selection.img_path"
                 class="absolute card-icon rounded-full p-2 border border-solid border-white">
          </div>
          <div class="badges relative mt-8 ml-24">
            <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
              +{{ activity5.choice_ids.length }}
            </div>
            <img v-for="(choice, key) in [1,2,3]"
                 :key="key"
                 :class="{'border-yellow': key === 2, 'ml-4 border-topaze': key === 0, 'ml-2 border-orange': key === 1}"
                 src="~/assets/img/autoportrait/heart.svg"
                 class="absolute card-icon rounded-full p-4 border border-solid border-2 bg-white">

          </div>
          <div class="absolute pin-r pin-b mr-4 mb-6 cursor-pointer"
               @click="modal.activity5 = true">
            <div class="flex items-center text-blue uppercase leading-normal tracking-wide font-bold text-xl">
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
        <div v-else-if="!modal.activity5 && !isManager"
             class="mt-20">
          <div class="text-blue text-3xl font-extrabold">
            Mes compétences primaires
          </div>
          <nuxt-link to="/dashboard/student/domaines/introspection/competences">
            <button class="button button--blue mt-4">Demarrer l'activité</button>
          </nuxt-link>
        </div>
      </div>
    </div>
    <div id="intelligences-multiples" />
    <modal-triple v-if="activity6"
                  v-show="modal['activity6']"
                  :activity="activity6"
                  :isManager="isManager"
                  link="/dashboard/student/domaines/introspection/intelligences-multiples"
                  class="modal modal-5"
                  bgColor="peach"
                  @close="closeModal" />
    <div v-show="!modal['activity6']"
         class="activity activity-5 z-10 mt-24 flex w-full relative justify-center items-center">
      <img v-if="activity6"
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-5.svg">
      <img v-else
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-5-d.svg">

      <div class="activity-left w-1/3 h-full">
        <div v-if="activity6"
             class="cardShadow rounded-xl bg-white p-4 relative h-full">
          <div class="flex">
            <div class="text-blue font-extrabold text-2xl">
              {{ activity6.name }}
            </div>
          </div>
          <div v-if="activity6.selection_ids"
               class="font-semibold text-xl mt-4 text-blue-lighter">
            <span class="text-blue">{{ activity6.selection_ids.length }}</span> résultats dominants
          </div>
          <div v-if="activity6.choice_ids"
               class="font-semibold text-xl mt-4 text-blue-lighter">
            <span class="text-blue">{{ activity6.choice_ids.length }}</span> actions clés
          </div>
          <div class="badges relative mt-8 ml-2">
            <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
              +{{ activity6.selection_ids.length }}
            </div>
            <img v-for="(selection, key) in activity6.selections"
                 :key="key"
                 :class="{'bg-yellow': key === 2, 'ml-4 bg-topaze': key === 0, 'ml-2 bg-orange': key === 1}"
                 :src="selection.img_path"
                 class="absolute card-icon rounded-full p-2 border border-solid border-white">
          </div>
          <div class="badges relative mt-8 ml-24">
            <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
              +{{ activity6.choice_ids.length }}
            </div>
            <img v-for="(choice, key) in [1,2,3]"
                 :key="key"
                 :class="{'border-yellow': key === 2, 'ml-4 border-topaze': key === 0, 'ml-2 border-orange': key === 1}"
                 src="~/assets/img/autoportrait/heart.svg"
                 class="absolute card-icon rounded-full p-4 border border-solid border-2 bg-white">

          </div>
          <div class="absolute pin-r pin-b mr-4 mb-6 cursor-pointer"
               @click="modal.activity6 = true">
            <div class="flex items-center text-blue uppercase leading-normal tracking-wide font-bold text-xl">
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
        <div v-else-if="!modal.activity6 && !isManager"
             class="text-right mr-6 mt-20">
          <div class="text-blue text-3xl font-extrabold">
            Mes intelligences multiples
          </div>
          <nuxt-link to="/dashboard/student/domaines/introspection/intelligences-multiples">
            <button class="button button--blue mt-4">Demarrer l'activité</button>
          </nuxt-link>
        </div>
      </div>
    </div>
    <modal-triple v-if="activity7"
                  v-show="modal['activity7']"
                  :activity="activity7"
                  :isManager="isManager"
                  link="/dashboard/student/domaines/introspection/contributions"
                  class="modal modal-3"
                  bgColor="blue-extra-sky"
                  @close="closeModal" />
    <div id="contributions" />
    <div v-show="!modal['activity7']"
         class="activity activity-3 z-10 mt-24 flex w-full relative justify-center items-center">
      <img v-if="activity7"
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-7.svg">
      <img v-else
           class="my-8"
           width="134"
           height="135"
           src="~/assets/img/autoportrait/illu-7-d.svg">

      <div class="activity-right w-1/3 h-full">
        <div v-if="activity7"
             class="cardShadow rounded-xl bg-white p-4 relative h-full">
          <div class="flex">
            <div class="text-blue font-extrabold text-2xl">
              {{ activity7.name }}
            </div>
          </div>
          <div v-if="activity7.selection_ids"
               class="font-semibold text-xl mt-4 text-blue-lighter">
            <span class="text-blue">{{ activity7.selection_ids.length }}</span> résultats dominants
          </div>
          <div class="badges relative mt-8 ml-2">
            <div class="absolute z-10 mt-8 text-white bg-blue rounded-full p-1 text-xs h-6 w-6 items-center flex justify-center">
              +{{ activity7.selection_ids.length }}
            </div>
            <img v-for="(selection, key) in activity7.selections"
                 :key="key"
                 :class="{'bg-yellow': key === 2, 'ml-4 bg-topaze': key === 0, 'ml-2 bg-orange': key === 1}"
                 :src="selection.img_path"
                 class="absolute card-icon rounded-full p-2 border border-solid border-white">
          </div>
          <div class="absolute pin-r pin-b mr-4 mb-6 cursor-pointer"
               @click="modal.activity7 = true">
            <div class="flex items-center text-blue uppercase leading-normal tracking-wide font-bold text-xl">
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
        <div v-else-if="!modal.activity7 && !isManager"
             class="text-left mr-6 mt-20">
          <div class="text-blue text-3xl font-extrabold">
            Mes contributions
          </div>
          <nuxt-link to="/dashboard/student/domaines/introspection/contributions">
            <button class="button button--blue mt-4">Demarrer l'activité</button>
          </nuxt-link>
        </div>
      </div>
    </div>
    <go-final v-if="finalPopup" @close="closeFinalPopup()" />
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
                <div class="text-xl text-orange font-bold">{{ school.name }} ({{ classroom.name }})</div>
              </div>
            </div>
            <div id="printable-container-activities" class="block">
              <div v-for="(activity, index) in orderedActivity" :key="index" :class="[Object.values(modalsAutoportrait.modalTriple).indexOf(activity.slug) > -1 ? 'w-full': 'w-1/2 ']" class="relative inline-block">
                <printable-container :activity="activity" :userLog="activity.log" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ModalTriple from './autoportrait/ModalTriple'
import ModalSimple from './autoportrait/ModalSimple'
import ModalRencontre from './autoportrait/ModalRencontre'
import ModalFinal from './autoportrait/ModalFinal'
import PrintableContainer from './printable/PrintableContainer'
import GoFinal from './GoFinal'
import _ from 'lodash'
import { MODAL_AUTOPORTRAIT } from '~/config'
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
    activity2: {
      type: Object,
      default: () => {
        return null
      }
    },
    activity3: {
      type: Object,
      default: () => {
        return null
      }
    },
    activity4: {
      type: Object,
      default: () => {
        return null
      }
    },
    activity5: {
      type: Object,
      default: () => {
        return null
      }
    },
    activity6: {
      type: Object,
      default: () => {
        return null
      }
    },
    activity1: {
      type: Object,
      default: () => {
        return null
      }
    },
    activity7: {
      type: Object,
      default: () => {
        return null
      }
    },
    activityFinal: {
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
      modalsAutoportrait: MODAL_AUTOPORTRAIT,
      orderedActivity: [],
      count:0,
      modal: {
        activity2: false,
        activity3: false,
        activity4: false,
        activity5: false,
        activity6: false,
        activity1: false,
        activity7: false,
        activityFinal: false
      },
      finalPopup: false,
      school: { name: '' },
      classroom: { name: '' },
      currentDate: new Date().toLocaleDateString()
    }
  },
  computed: {
    canDoFinal() {
      return this.activity2 && this.activity3 && this.activity4 && this.activity5 && this.activity6 && this.activity1 && this.activity7
    },
  },
  created() {
    this.orderedActivityList()
    if ( this.activity2 ) {
      const activity2 = this.activity2
      if ( this.activity2.selection_ids ) {
        activity2.selections = _.filter( this.selections, function( selection ) {
          return activity2.selection_ids.indexOf( selection.id ) !== -1
        } )
      }

      if ( this.activity2.choice_ids ) {
        activity2.choices = _.filter( this.choices, function( choice ) {
          return activity2.choice_ids.indexOf( choice.id ) !== -1
        } )
      }
    }

    if ( this.activity3 ) {
      const activity3 = this.activity3

      if ( this.activity3.choice_ids ) {
        activity3.choices = _.filter( this.choices, function( choice ) {
          return activity3.choice_ids.indexOf( choice.id ) !== -1
        } )
      }
    }

    if ( this.activity4 ) {
      const activity4 = this.activity4
      if ( this.activity4.selection_ids ) {
        activity4.selections = _.filter( this.selections, function( selection ) {
          return activity4.selection_ids.indexOf( selection.id ) !== -1
        } )
      }

      if ( this.activity4.choice_ids ) {
        activity4.choices = _.filter( this.choices, function( choice ) {
          return activity4.choice_ids.indexOf( choice.id ) !== -1
        } )
      }
    }

    if ( this.activity5 ) {
      const activity5 = this.activity5
      if ( this.activity5.selection_ids ) {
        activity5.selections = _.filter( this.selections, function( selection ) {
          return activity5.selection_ids.indexOf( selection.id ) !== -1
        } )
      }

      if ( this.activity5.choice_ids ) {
        activity5.choices = _.filter( this.choices, function( choice ) {
          return activity5.choice_ids.indexOf( choice.id ) !== -1
        } )
      }
    }

    if ( this.activity6 ) {
      const activity6 = this.activity6
      if ( this.activity6.selection_ids ) {
        activity6.selections = _.filter( this.selections, function( selection ) {
          return activity6.selection_ids.indexOf( selection.id ) !== -1
        } )
      }

      if ( this.activity6.choice_ids ) {
        activity6.choices = _.filter( this.choices, function( choice ) {
          return activity6.choice_ids.indexOf( choice.id ) !== -1
        } )
      }
    }

    if ( this.activity1 ) {
      const activity1 = this.activity1

      if ( this.activity1.choice_ids ) {
        activity1.choices = _.filter( this.choices, function( choice ) {
          return activity1.choice_ids.indexOf( choice.id ) !== -1
        } )
        activity1.choices.forEach( function( choice ) {
          choice.icon_path = choice.img_path.substring( 0, choice.img_path.indexOf( '.svg' ) ) + '_icon.svg'
        } )
      }
    }

    if ( this.activity7 ) {
      const activity7 = this.activity7
      if ( this.activity7.selection_ids ) {
        activity7.selections = _.filter( this.selections, function( selection ) {
          return activity7.selection_ids.indexOf( selection.id ) !== -1
        } )
      }

      if ( this.activity7.choice_ids ) {
        activity7.choices = _.filter( this.choices, function( choice ) {
          return activity7.choice_ids.indexOf( choice.id ) !== -1
        } )
      }
    }

    if (this.activityFinal) {
      const activityFinal = this.activityFinal
      if (this.activityFinal.selection_ids) {
        activityFinal.selections = _.filter(this.domains, function (domain) {
          return activityFinal.selection_ids.indexOf(domain.id) !== -1
        })
      }

      if (this.activityFinal.choice_ids) {
        activityFinal.choices = _.filter(this.domains, function (domain) {
          return activityFinal.choice_ids.indexOf(domain.id) !== -1
        })
      }
    } else if (this.canDoFinal) {
      this.modal.activity2 = true
      this.modal.activity3 = true
      this.modal.activity4 = true
      this.modal.activity5 = true
      this.modal.activity6 = true
      this.modal.activity1 = true
      this.modal.activity7 = true
    }
  },
  async mounted() {
    if (!this.isManager && this.canDoFinal && !this.$store.state.auth.user.config.sawFinalPopup) {
      this.finalPopup = true
    }

    // this.$forceNextTick(() => {
    //   if (this.$route.hash) {
    //     setTimeout(() => {
    //       const element = document.getElementById(this.$route.hash.slice(1))
    //       if (element) {
    //         element.scrollIntoView();
    //         window.scrollBy(0, -100);
    //       }
    //     }, 350)
    //   }
    // })

    // const route = this.$route
    // this.$forceNextTick()
    // if (route.hash) {
    //   setTimeout(function() {
    //     const element = document.getElementById(route.hash.slice(1))
    //     if (element) {
    //       element.scrollIntoView();
    //       window.scrollBy(0, -100);
    //     } }, 350)
    // }


    try {
      let response = await this.$api.schools.getSchoolsByIds([this.$store.state.auth.user.config.school_id])
      this.school = response.data[0] || {}

      response = await this.$api.schools.getClassroomsByIds([this.$store.state.auth.user.config.classroom_id])
      this.classroom = response.data[0] || {}
    } catch (e) {
      console.log(e)
    }
  },
  methods: {
    orderedActivityList() {
      this.orderedActivity = _.map(_.sortBy(this.activityList, function(activity) {
        if(activity.modal === 'triple') {
          return 1
        }
        else {
          return parseInt(activity.modal)
        }
      }), (activity) => {
        activity.log = _.find(this.userLogs, {num: activity.num})
        return activity
      })
    },
    closeModal(slug) {
      this.modal[slug] = false
    },
    closeFinalPopup() {
      this.finalPopup = false
      document.getElementById('activityFinal').scrollIntoView()
      window.scrollBy(0, -130);
    },
    printAutoportrait() {
      const printContents = document.getElementById('printable-part').innerHTML;

      const css = '@page { size: portrait; margin: 0; }',
        head = document.head || document.getElementsByTagName('head')[0],
        style = document.createElement('style');

      if (style.styleSheet){
        style.styleSheet.cssText = css;
      } else {
        style.appendChild(document.createTextNode(css));
      }

      head.appendChild(style);
      document.body.innerHTML = head.innerHTML + printContents;

      const element = document.getElementById("printable-container-activities");
      NodeList.prototype.forEach = Array.prototype.forEach
      var children = element.childNodes;
      var foundItem = false
      children.forEach(function(item, index){
        if(!foundItem && item.offsetTop < 1415 && item.offsetTop + item.clientHeight > 1415) {
          item.classList.add("printOnly")
          const breakDivBefore = children[index -1]
          breakDivBefore.firstChild.style.pageBreakAfter = 'always'
          foundItem = true
        }
      });

      setTimeout(() => {
        window.print();
        this.$router.go()
      }, 1000)
    }
  }
}
</script>

<style lang="scss" scoped>
  @media print {
    .printOnly {
      margin-top: 50px;
    }
  }
.autoportrait.isManager {
  z-index: 101;
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
    /*transform: translateX(75%);*/
    right: 50px;
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
    /*transform: translateX(-75%);*/
    left: 50px;
    @apply absolute pin-t;
  }
}

.final-img-bloc {
transform: translateX(-53%);
}

</style>

<template>
  <div class="activity__right min-h-screen bg-blue-lightest xl:w-3/5 w-full pb-12 pt-6 xl:px-16 sm:px-2 flex flex-col justify-center h-auto relative">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes compétences primaires</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl  xl:mr-4">2.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal mt-4">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div v-drag-and-drop:options="options"
         class="flex mt-8">
      <div v-show="step === 1"
           class="step1 flex flex-col p-2">
        <div class="w-full flex flex-no-wrap items-center">
          <div class="xl:w-2/3 max-w-85 sm:w-full bg-white rounded-xlg cardShadow my-3 xl:py-8 sm:py-4 px-4 flex flex-no-wrap sm:flex-wrap items-center relative">
            <div class="number bg-yellow rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl xl:mr-4">
              1
            </div>
            <div class="flex items-center w-full flex-1 px-4 text-blue min-w-2/3">
              {{ questions[0] }}
            </div>
            <div class="correct-1 step-1 sm:flex sm:flex-no-wrap sm:w-full mr-2 flex-none dropzone sm:ml-12 sm:mt-4">
              <div class="sm:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold">
                Quelle compétence ?
              </div>
              <div class="xl:hidden md:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 sm:cursor-pointer h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold"
                   @click="openModal('yellow', 1)">
                Quelle compétence ?
              </div>
              <span v-show="error[0]"
                    class="sm:flex text-red sm:flex-shrink sm:w-20 text-xs xl:absolute sm:mx-2 xl:pin-b xl:pin-r xl:pb-4 xl:pr-6 sm:items-center sm:justify-center">Erreur ! Tente une autre compétence</span>
            </div>
          </div>
          <div class="w-1/3 h-full sm:hidden flex justify-center items-center">
            <div class="correct-4 step-1 w-56 justify-center competence bg-white border-dashed border-2 border-transparent cardShadow m-3 text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center cursor-grab">
              {{ answers[3] }}
            </div>
          </div>
        </div>
        <div class="w-full flex flex-no-wrap items-center">
          <div class="xl:w-2/3 max-w-85 sm:w-full bg-white rounded-xlg cardShadow my-3 xl:py-8 sm:py-4 px-4 flex flex-no-wrap sm:flex-wrap items-center relative">
            <div class="number bg-peach rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl xl:mr-4">
              2
            </div>
            <div class="flex items-center w-full flex-1 px-4 text-blue min-w-2/3">
              {{ questions[1] }}
            </div>
            <div class="correct-2 step-1 sm:flex sm:flex-no-wrap sm:w-full mr-2 flex-none dropzone sm:ml-12 sm:mt-4">
              <div class="sm:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold">
                Quelle compétence ?
              </div>
              <div class="xl:hidden md:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 sm:cursor-pointer h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold"
                   @click="openModal('peach', 2)">
                Quelle compétence ?
              </div>
              <span v-show="error[1]"
                    class="sm:flex text-red sm:flex-shrink sm:w-20 text-xs xl:absolute sm:mx-2 xl:pin-b xl:pin-r xl:pb-4 xl:pr-6 sm:items-center sm:justify-center">Erreur ! Tente une autre compétence</span>
            </div>
          </div>
          <div class="w-1/3 h-full sm:hidden flex justify-center items-center">
            <div class="correct-2 step-1 w-56 justify-center competence bg-white border-dashed border-2 border-transparent cardShadow m-3 text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center cursor-grab">
              {{ answers[1] }}
            </div>
          </div>
        </div>
        <div class="w-full flex flex-no-wrap items-center">
          <div class="xl:w-2/3 max-w-85 sm:w-full bg-white rounded-xlg cardShadow my-3 xl:py-8 sm:py-4 px-4 flex flex-no-wrap sm:flex-wrap items-center relative">
            <div class="number bg-topaze rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl xl:mr-4">
              3
            </div>
            <div class="flex items-center w-full flex-1 px-4 text-blue min-w-2/3">
              {{ questions[2] }}
            </div>
            <div class="correct-3 step-1 sm:flex sm:flex-no-wrap sm:w-full mr-2 flex-none dropzone sm:ml-12 sm:mt-4">
              <div class="sm:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold">
                Quelle compétence ?
              </div>
              <div class="xl:hidden md:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 sm:cursor-pointer h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold"
                   @click="openModal('topaze', 3)">
                Quelle compétence ?
              </div>
              <span v-show="error[2]"
                    class="sm:flex text-red sm:flex-shrink sm:w-20 text-xs xl:absolute sm:mx-2 xl:pin-b xl:pin-r xl:pb-4 xl:pr-6 sm:items-center sm:justify-center">Erreur ! Tente une autre compétence</span>
            </div>
          </div>
          <div class="w-1/3 h-full sm:hidden flex justify-center items-center">
            <div class="correct-5 step-1 w-56 justify-center competence bg-white border-dashed border-2 border-transparent cardShadow m-3 text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center cursor-grab">
              {{ answers[4] }}
            </div>
          </div>
        </div>
        <div class="w-full flex flex-no-wrap items-center">
          <div class="xl:w-2/3 max-w-85 sm:w-full bg-white rounded-xlg cardShadow my-3 xl:py-8 sm:py-4 px-4 flex flex-no-wrap sm:flex-wrap items-center relative">
            <div class="number bg-blue-sky rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl xl:mr-4">
              4
            </div>
            <div class="flex items-center w-full flex-1 px-4 text-blue min-w-2/3">
              {{ questions[3] }}
            </div>
            <div class="correct-4 step-1 sm:flex sm:flex-no-wrap sm:w-full mr-2 flex-none dropzone sm:ml-12 sm:mt-4">
              <div class="sm:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold">
                Quelle compétence ?
              </div>
              <div class="xl:hidden md:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 sm:cursor-pointer h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold"
                   @click="openModal('blue-sky', 4)">
                Quelle compétence ?
              </div>
              <span v-show="error[3]"
                    class="sm:flex text-red sm:flex-shrink sm:w-20 text-xs xl:absolute sm:mx-2 xl:pin-b xl:pin-r xl:pb-4 xl:pr-6 sm:items-center sm:justify-center">Erreur ! Tente une autre compétence</span>
            </div>
          </div>
          <div class="w-1/3 h-full sm:hidden flex justify-center items-center">
            <div class="correct-1 step-1 w-56 justify-center competence bg-white border-dashed border-2 border-transparent cardShadow m-3 text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center cursor-grab">
              {{ answers[0] }}
            </div>
          </div>
        </div>
        <div class="w-full flex flex-no-wrap items-center">
          <div class="xl:w-2/3 max-w-85 sm:w-full bg-white rounded-xlg cardShadow my-3 xl:py-8 sm:py-4 px-4 flex flex-no-wrap sm:flex-wrap items-center relative">
            <div class="number bg-purple rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl xl:mr-4">
              5
            </div>
            <div class="flex items-center w-full flex-1 px-4 text-blue min-w-2/3">
              {{ questions[4] }}
            </div>
            <div class="correct-5 step-1 sm:flex sm:flex-no-wrap sm:w-full mr-2 flex-none dropzone sm:ml-12 sm:mt-4">
              <div class="sm:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold">
                Quelle compétence ?
              </div>
              <div class="xl:hidden md:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 sm:cursor-pointer h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold"
                   @click="openModal('purple', 5)">
                Quelle compétence ?
              </div>
              <span v-show="error[4]"
                    class="sm:flex text-red sm:flex-shrink sm:w-20 text-xs xl:absolute sm:mx-2 xl:pin-b xl:pin-r xl:pb-4 xl:pr-6 sm:items-center sm:justify-center">Erreur ! Tente une autre compétence</span>
            </div>
          </div>
          <div class="w-1/3 h-full sm:hidden flex justify-center items-center">
            <div class="correct-3 step-1 w-56 justify-center competence bg-white border-dashed border-2 border-transparent cardShadow m-3 text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center cursor-grab">
              {{ answers[2] }}
            </div>
          </div>
        </div>
      </div>
      <div v-show="step === 2"
           class="step-2 flex flex-col p-2">
        <div class="w-full flex flex-no-wrap items-center">
          <div class="xl:w-2/3 max-w-85 sm:w-full bg-white rounded-xlg cardShadow my-3 xl:py-8 sm:py-4 px-4 flex flex-no-wrap sm:flex-wrap items-center relative">
            <div class="number bg-yellow rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl xl:mr-4">
              6
            </div>
            <div class="flex items-center w-full flex-1 px-4 text-blue min-w-2/3">
              {{ questions[5] }}
            </div>
            <div class="correct-1 step-2 sm:flex sm:flex-no-wrap sm:w-full mr-2 flex-none dropzone sm:ml-12 sm:mt-4">
              <div class="sm:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold">
                Quelle compétence ?
              </div>
              <div class="xl:hidden md:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 sm:cursor-pointer h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold"
                   @click="openModal('yellow', 6)">
                Quelle compétence ?
              </div>
              <span v-show="error[0]"
                    class="sm:flex text-red sm:flex-shrink sm:w-20 text-xs xl:absolute sm:mx-2 xl:pin-b xl:pin-r xl:pb-4 xl:pr-6 sm:items-center sm:justify-center">Erreur ! Tente une autre compétence</span>
            </div>
          </div>
          <div class="w-1/3 h-full sm:hidden flex justify-center items-center">
            <div class="correct-4 step-2 w-56 justify-center competence bg-white border-dashed border-2 border-transparent cardShadow m-3 text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center cursor-grab">
              {{ answers[8] }}
            </div>
          </div>
        </div>
        <div class="w-full flex flex-no-wrap items-center">
          <div class="xl:w-2/3 max-w-85 sm:w-full bg-white rounded-xlg cardShadow my-3 xl:py-8 sm:py-4 px-4 flex flex-no-wrap sm:flex-wrap items-center relative">
            <div class="number bg-peach rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl xl:mr-4">
              7
            </div>
            <div class="flex items-center w-full flex-1 px-4 text-blue min-w-2/3">
              {{ questions[6] }}
            </div>
            <div class="correct-2 step-2 sm:flex sm:flex-no-wrap sm:w-full mr-2 flex-none dropzone sm:ml-12 sm:mt-4">
              <div class="sm:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold">
                Quelle compétence ?
              </div>
              <div class="xl:hidden md:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 sm:cursor-pointer h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold"
                   @click="openModal('peach', 7)">
                Quelle compétence ?
              </div>
              <span v-show="error[1]"
                    class="sm:flex text-red sm:flex-shrink sm:w-20 text-xs xl:absolute sm:mx-2 xl:pin-b xl:pin-r xl:pb-4 xl:pr-6 sm:items-center sm:justify-center">Erreur ! Tente une autre compétence</span>
            </div>
          </div>
          <div class="w-1/3 h-full sm:hidden flex justify-center items-center">
            <div class="correct-2 step-2 w-56 justify-center competence bg-white border-dashed border-2 border-transparent cardShadow m-3 text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center cursor-grab">
              {{ answers[6] }}
            </div>
          </div>
        </div>
        <div class="w-full flex flex-no-wrap items-center">
          <div class="xl:w-2/3 max-w-85 sm:w-full bg-white rounded-xlg cardShadow my-3 xl:py-8 sm:py-4 px-4 flex flex-no-wrap sm:flex-wrap items-center relative">
            <div class="number bg-topaze rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl xl:mr-4">
              8
            </div>
            <div class="flex items-center w-full flex-1 px-4 text-blue min-w-2/3">
              {{ questions[7] }}
            </div>
            <div class="correct-3 step-2 sm:flex sm:flex-no-wrap sm:w-full mr-2 flex-none dropzone sm:ml-12 sm:mt-4">
              <div class="sm:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold">
                Quelle compétence ?
              </div>
              <div class="xl:hidden md:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 sm:cursor-pointer h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold"
                   @click="openModal('topaze', 8)">
                Quelle compétence ?
              </div>
              <span v-show="error[2]"
                    class="sm:flex text-red sm:flex-shrink sm:w-20 text-xs xl:absolute sm:mx-2 xl:pin-b xl:pin-r xl:pb-4 xl:pr-6 sm:items-center sm:justify-center">Erreur ! Tente une autre compétence</span>
            </div>
          </div>
          <div class="w-1/3 h-full sm:hidden flex justify-center items-center">
            <div class="correct-5 step-2 w-56 justify-center competence bg-white border-dashed border-2 border-transparent cardShadow m-3 text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center cursor-grab">
              {{ answers[9] }}
            </div>
          </div>
        </div>
        <div class="w-full flex flex-no-wrap items-center">
          <div class="xl:w-2/3 max-w-85 sm:w-full bg-white rounded-xlg cardShadow my-3 xl:py-8 sm:py-4 px-4 flex flex-no-wrap sm:flex-wrap items-center relative">
            <div class="number bg-blue-sky rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl xl:mr-4">
              9
            </div>
            <div class="flex items-center w-full flex-1 px-4 text-blue min-w-2/3">
              {{ questions[8] }}
            </div>
            <div class="correct-4 step-2 sm:flex sm:flex-no-wrap sm:w-full mr-2 flex-none dropzone sm:ml-12 sm:mt-4">
              <div class="sm:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold">
                Quelle compétence ?
              </div>
              <div class="xl:hidden md:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 sm:cursor-pointer h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold"
                   @click="openModal('blue-sky', 9)">
                Quelle compétence ?
              </div>
              <span v-show="error[3]"
                    class="sm:flex text-red sm:flex-shrink sm:w-20 text-xs xl:absolute sm:mx-2 xl:pin-b xl:pin-r xl:pb-4 xl:pr-6 sm:items-center sm:justify-center">Erreur ! Tente une autre compétence</span>
            </div>
          </div>
          <div class="w-1/3 h-full sm:hidden flex justify-center items-center">
            <div class="correct-1 step-2 w-56 justify-center competence bg-white border-dashed border-2 border-transparent cardShadow m-3 text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center cursor-grab">
              {{ answers[5] }}
            </div>
          </div>
        </div>
        <div class="w-full flex flex-no-wrap items-center">
          <div class="xl:w-2/3 max-w-85 sm:w-full bg-white rounded-xlg cardShadow my-3 xl:py-8 sm:py-4 px-4 flex flex-no-wrap sm:flex-wrap items-center relative">
            <div class="number bg-purple rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl xl:mr-4">
              10
            </div>
            <div class="flex items-center w-full flex-1 px-4 text-blue min-w-2/3">
              {{ questions[9] }}
            </div>
            <div class="correct-5 step-2 sm:flex sm:flex-no-wrap sm:w-full mr-2 flex-none dropzone sm:ml-12 sm:mt-4">
              <div class="sm:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold">
                Quelle compétence ?
              </div>
              <div class="xl:hidden md:hidden border-dashed border-blue-lighter xl:w-56 flex-none sm:w-48 sm:cursor-pointer h-12 border-2 rounded-full flex flex-no-wrap text-sm items-center justify-center text-blue-lighter font-semibold"
                   @click="openModal('purple', 10)">
                Quelle compétence ?
              </div>
              <span v-show="error[4]"
                    class="sm:flex text-red sm:flex-shrink sm:w-20 text-xs xl:absolute sm:mx-2 xl:pin-b xl:pin-r xl:pb-4 xl:pr-6 sm:items-center sm:justify-center">Erreur ! Tente une autre compétence</span>
            </div>
          </div>
          <div class="w-1/3 h-full sm:hidden flex justify-center items-center">
            <div class="correct-3 step-2 w-56 justify-center competence bg-white border-dashed border-2 border-transparent cardShadow m-3 text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center cursor-grab">
              {{ answers[7] }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            :disabled="valid !== 5"
            class="button button--blue mt-8 self-start mt-3"
            @click="nextStep">
      Je Valide
    </button>
    <div v-show="modal.open"
         class="z-40 bg-blue opacity-75 absolute h-full w-full pin-t pin-l" />
    <div v-show="modal.open"
         class="z-50 fixed h-full w-full pin-t pin-l py-16 px-8">
      <div class="bg-white rounded-xlg flex flex-col opacity-1 items-center justify-center">
        <div :class="'bg-' + modal.color"
             class="number rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl mt-8 mb-4">
          {{ modal.number + (step - 1) * 5 }}
        </div>
        <div :class="modal.selected === 4 ? 'text-white bg-' + modal.color : 'text-blue border-solid cardShadow border border-blue cardShadow'"
             class="correct-4 justify-center competence bg-white m-3 my-4 text-xs px-6 py-4 text-center flex-no-grow flex-no-shrink font-semibold rounded-full flex items-center cursor-pointer"
             @click="modal.selected = modal.selected === 4 ? null : 4">
          {{ step == 1 ? 'Sens de L’organisation' : 'Adaptabilité' }}
        </div>
        <div :class="modal.selected === 2 ? 'text-white bg-' + modal.color : 'text-blue border-solid cardShadow border border-blue cardShadow'"
             class="correct-2 justify-center competence bg-white m-3 my-4 text-xs px-6 py-4 text-center flex-no-grow flex-no-shrink font-semibold rounded-full flex items-center cursor-pointer"
             @click="modal.selected = modal.selected === 2 ? null : 2">
          {{ step == 1 ? 'Créativité' : 'Initiative' }}
        </div>
        <div :class="modal.selected === 5 ? 'text-white bg-' + modal.color : 'text-blue border-solid cardShadow border border-blue cardShadow'"
             class="correct-5 justify-center competence bg-white m-3 my-4 text-xs px-6 py-4 text-center flex-no-grow flex-no-shrink font-semibold rounded-full flex items-center cursor-pointer"
             @click="modal.selected = modal.selected === 5 ? null : 5">
          {{ step == 1 ? 'Sens de la communication' : 'Sens des responsabilités' }}
        </div>
        <div :class="modal.selected === 1 ? 'text-white bg-' + modal.color : 'text-blue border-solid cardShadow border border-blue cardShadow'"
             class="correct-1 justify-center competence bg-white m-3 my-4 text-xs px-6 py-4 text-center flex-no-grow flex-no-shrink font-semibold rounded-full flex items-center cursor-pointer"
             @click="modal.selected = modal.selected === 1 ? null : 1">
          {{ step == 1 ? 'Autonomie' : 'Confiance en soi' }}
        </div>
        <div :class="modal.selected === 3 ? 'text-white bg-' + modal.color : 'text-blue border-solid cardShadow border border-blue cardShadow'"
             class="correct-3 justify-center competence bg-white m-3 my-4 text-xs px-6 py-4 text-center flex-no-grow flex-no-shrink font-semibold rounded-full flex items-center cursor-pointer"
             @click="modal.selected = modal.selected === 3 ? null : 3">
          {{ step == 1 ? 'Esprit d’équipe' : 'Persévérance' }}
        </div>
        <button v-scroll-to="'#actTitle'"
                :disabled="!modal.selected"
                class="button button--blue mt-6 text-sm py-4 px-5"
                @click="selectModal">
          Je selectionne
        </button>
        <picture class="mt-6 mb-4 cursor-pointer"
                 @click="closeModal">
          <svg xmlns="http://www.w3.org/2000/svg"
               width="32"
               height="32"
               viewBox="0 0 32 32">
            <g fill="none"
               fill-rule="nonzero"
               stroke="#432CB4"
               transform="translate(1 1)">
              <circle cx="15"
                      cy="15"
                      r="15"
                      stroke-width=".6" />
              <g stroke-linecap="round"
                 stroke-width="1.2">
                <path d="M9.6 9.6l11.02 10.835M20.62 9.6L9.6 20.435" />
              </g>
            </g>
          </svg>

        </picture>
      </div>
    </div>
  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import 'vue-draggable/polyfills'

export default {
  name: 'IntelligencesMultiples',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  data() {
    var that = this
    return {
      options: {
        dropzoneSelector: '.dropzone',
        draggableSelector: '.competence',
        handlerSelector: null,
        reactivityEnabled: true,
        multipleDropzonesItemsDraggingEnabled: false,
        showDropzoneAreas: true,
        onDrop: function( event ) {
          that.onDrop( event )
        }
      },
      step: 1,
      valid: 0,
      modal: {
        open: false,
        selected: null,
        color: null,
        number: 1
      },
      questions: [
        `Liberté de comportement d'un individu, faculté d'agir avec indépendance`,
        `La capacité d'un individu à imaginer un nouvel objet, ou à trouver une solution aux problèmes`,
        `Etre en accord avec un groupe de personnes dans un objectif partagé en privilégiant la réussite collective.`,
        `Ordonner ses affaires, son travail, ses activités dans le but d'agir efficacement.`,
        `Savoir s’exprimer à l’oral, établir un contact avec ses interlocuteurs. Savoir écouter et partager.`,
        `Conscience que l’on a de sa propre valeur qui permet d’avoir une certaine assurance.`,
        `Action d'une personne qui propose, entreprend, organise en étant la première.`,
        `Mettre en œuvre sa volonté, user de patience pour poursuivre une action malgré les difficultés, pour rester ferme dans une résolution, une opinion, une attitude.`,
        `Être capable de s’informer, de découvrir, d’apprendre, de s’approprier les changements de façon à rétablir l’équilibre d’une situation.`,
        `Obligation ou nécessité morale de répondre, de se porter garant de ses actions ou de celles des autres.`
      ],
      answers: [
        'Autonomie',
        'Créativité',
        'Esprit d’équipe',
        'Sens de L’organisation',
        'Sens de la communication',
        'Confiance en soi',
        'Initiative',
        'Persévérance',
        'Adaptabilité',
        'Sens des responsabilités'
      ],
      error: [
        false,
        false,
        false,
        false,
        false,
      ],
      texts: this.getPageTexts(this.$route.name)
    }
  },
  mounted() {
    this.$emit( 'setStep', 1 )
  },
  methods: {
    nextStep: function() {
      if ( this.step == 1 ) {
        this.step = 2
        this.valid = 0
      } else {
        this.$emit('passQuizz')
        this.$router.push( 'etape-3' )
      }
    },
    onDrop( event ) {
      this.error = [
        false,
        false,
        false,
        false,
        false,
      ]
      let number = event.droptarget.classList[0][8]
      if ( event.items[0].classList[0] == event.droptarget.classList[0] ) {
        this.valid++;
        let clone = event.items[0].cloneNode( true )
        let color = event.droptarget.parentElement.childNodes[0].classList[1]
        clone.className = "w-56 justify-center text-sm px-6 h-12 text-center flex-no-grow flex-no-shrink text-white font-semibold rounded-full flex items-center " + color
        clone.setAttribute( 'aria-grabbed', 'true' )
        clone.setAttribute( 'draggable', 'false' )
        event.droptarget.replaceChild( clone, event.droptarget.childNodes[0] )
        let newNode = document.createElement( 'div' )
        newNode.className = "w-56 justify-center border-dashed border-2 border-blue text-sm px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center"
        newNode.appendChild( event.items[0].firstChild )
        event.items[0].parentElement.replaceChild( newNode, event.items[0] )
      } else {
        this.$set( this.error, number - 1, true )
      }
    },
    openModal( color, number ) {
      this.error = [
        false,
        false,
        false,
        false,
        false,
      ]
      this.modal.color = color
      this.modal.number = number
      this.modal.open = true
    },
    closeModal() {
      this.modal.color = null
      this.modal.color = null
      this.modal.selected = null
      this.modal.open = false
    },
    selectModal( selected ) {
      if ( this.modal.selected === this.modal.number - ( this.step - 1 ) * 5 ) {
        this.valid++;
        let node = document.createElement( 'div' )
        node.className = "correct-" + this.modal.selected + " justify-center text-sm h-12 text-center px-6 flex-no-grow flex-no-shrink text-white font-semibold rounded-full flex items-center bg-" + this.modal.color
        node.appendChild( document.createTextNode( this.answers[this.modal.selected + ( this.step - 1 ) * 5 - 1] ) )
        let target = document.querySelector( ".step-" + this.step + ".dropzone.correct-" + ( this.modal.number - ( this.step - 1 ) * 5 ) )
        target.replaceChild( node, target.childNodes[2] )
      } else {
        this.$set( this.error, this.modal.number - 1, true )
      }
      this.closeModal()
    }
  }
}
</script>

<style lang="scss" scoped>
.activity__right {
  .card-head {
    height: 106px;
  }

  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }

  .cursor-grab {
    cursor: grab;
  }

  .cursor-pointer {
    transition: all 0.2s;
    &:hover {
      transform: scale(1.1);
    }
  }
}
</style>

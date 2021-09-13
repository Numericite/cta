<template>
  <div class="activity__right relative bg-blue-lightest min-h-screen xl:w-3/5 w-full pb-12 pt-6 xl:pl-16 px-6 xl:pr-32 flex flex-col justify-center h-auto">

    <h2 id="actTitle"
        class="uppercase text-blue-lighter font-bold flex items-center">Mes matières préférées</h2>

    <!-- Title -->
    <div class="mt-6 flex">
      <span class="text-blue-lighter font-bold text-6xl mr-4 sm:text-3xl">1.</span>
      <h1 class="title text-blue font-bold text-3xl leading-normal xl:mt-4 sm:text-xl ">
        <span v-html="texts[0]" />
      </h1>
    </div>
    <!-- End Title -->

    <div v-drag-and-drop:options="options"
         class="flex sm:flex-wrap mt-16 sm:mt-6">
      <div class="sm:hidden flex flex-wrap justify-start w-1/2 sm:flex-no-wrap sm:whitespace-no-wrap sm:mb-6  sm:overflow-auto sm:w-full items-start matieres-list dropzone">
        <div v-for="(matiere, key) in matieres"
             ref="matieresList"
             :key="key"
             :id="key"
             :class="'matiere-' + key"
             class="bg-white border-dashed border-2 border-transparent cardShadow m-3 text-2xl px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center matiere cursor-grab">
          {{ matiere.text }}
        </div>
      </div>
      <div class="w-1/2 sm:w-full">
        <div class="card cardShadow rounded-xlg p-6">
          <h3 class="text-center font-bold text-blue text-lg tracking-wide uppercase mt-4 leading-normal">
            <span v-html="texts[1]" />
          </h3>
          <div id="1"
               class="bg-yellow rounded-full bestMatiere-1 my-8 p-3 flex matiere-preferee dropzone">
            <span class="bg-white rounded-full mx-2 pt-4 text-center text-yellow font-proza font-bold w-12 h-12 flex-no-shrink text-5xl">1</span>
            <div :class="bestMatieres[0] ? ' xl:hidden md:hidden mobile-matiere-pref-selected' : 'mobile-matiere-pref'"
                 class="xl:hidden md:hidden sm:flex"
                 @click="openModal('yellow', 1)">
              {{ bestMatieres[0] ? bestMatieres[0] : 'Selectionner une matière' }}
            </div>
            <div class="sm:hidden border-dashed w-full border-white h-12 p-4 ml-4 border-2 rounded-full dropchild" />
          </div>
          <div id="2"
               class="bg-peach rounded-full bestMatiere-2 my-8 p-3 flex matiere-preferee dropzone">
            <span class="bg-white rounded-full mx-2 pt-4 text-center text-peach font-proza font-bold w-12 h-12 flex-no-shrink text-5xl">2</span>
            <div :class="bestMatieres[1] ? 'mobile-matiere-pref-selected' : 'mobile-matiere-pref'"
                 class="xl:hidden md:hidden sm:flex"
                 @click="openModal('peach', 2)">
              {{ bestMatieres[1] ? bestMatieres[1] : 'Selectionner une matière' }}
            </div>
            <div class="sm:hidden border-dashed w-full border-white h-12 p-4 ml-4 border-2 rounded-full dropchild" />
          </div>
          <div id="3"
               class="bg-topaze rounded-full bestMatiere-3 my-6 p-3 flex matiere-preferee dropzone">
            <span class="bg-white rounded-full mx-2 pt-4 text-center text-topaze font-proza font-bold w-12 h-12 flex-no-shrink text-5xl">3</span>
            <div :class="bestMatieres[2] ? 'mobile-matiere-pref-selected' : 'mobile-matiere-pref'"
                 class="xl:hidden md:hidden sm:flex"
                 @click="openModal('topaze', 3)">
              {{ bestMatieres[2] ? bestMatieres[2] : 'Selectionner une matière' }}
            </div>
            <div class="sm:hidden border-dashed w-full border-white h-12 p-4 ml-4 border-2 rounded-full dropchild" />
          </div>
        </div>
      </div>
    </div>

    <button v-scroll-to="'#actTitle'"
            :disabled="!(matieresPref == 3)"
            class="button button--blue mt-8 self-start ml-6 mt-3"
            @click="nextPage">
      Je valide
    </button>

    <div v-show="modal.open"
         class="z-40 bg-blue opacity-75 fixed h-full w-full pin-t pin-l" />
    <div v-show="modal.open"
         class="z-50 fixed h-full w-full pin-t pin-l py-16 px-8">
      <div class="bg-white rounded-xlg flex flex-wrap opacity-1 items-center px-6">
        <div class="flex items-center justify-center w-full">
          <div :class="'bg-' + modal.color"
               class="number rounded-full flex flex-no-shrink h-10 w-10 text-white font-proza font-bold items-center justify-center text-2xl mt-8 mb-4">
            {{ modal.number }}
          </div>
        </div>

        <div v-for="(matiere, key) in matieres"
             :key="key"
             :class="matiere.selected ? 'text-blue bg-transparent border-dashed border-2 border-solid' : modal.selected === key ? 'text-white border border-solid cursor-pointer bg-' + modal.color + ' border-' + modal.color : 'text-blue  cursor-pointer border-solid cardShadow border border-blue cardShadow'"
             class="justify-center competence bg-white mx-1 my-2 text-xs px-6 py-4 text-center flex-no-grow flex-no-shrink font-semibold rounded-full flex items-center"
             @click="matiere.selected ? null : modal.selected = modal.selected === key ? null : key">
          {{ matiere.text }}
        </div>
        <div class="flex items-center justify-center w-full">
          <button v-scroll-to="'#actTitle'"
                  :disabled="modal.selected == null"
                  class="button button--blue mt-6 text-sm py-4 px-5"
                  @click="selectModal">
            Je selectionne
          </button>
        </div>
        <div class="flex items-center justify-center w-full">
          <picture class="mt-6 mb-4 cursor-pointer self-center"
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
  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
if(process.browser) {
  require('vue-draggable/polyfills')
}
export default {
  name: 'MatieresPreferees',
  layout: 'activity',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessHighSchoolStudentDashboard',
  props: {
    matieres: {
      type: Array,
      default: function() {
        return { questions: null }
      }
    },
    activityLogs: {
      type: Object,
      default: function() {
        return {}
      }
    }
  },
  data() {
    var that = this
    return {
      options: {
        dropzoneSelector: '.dropzone',
        draggableSelector: '.matiere',
        handlerSelector: null,
        reactivityEnabled: true,
        multipleDropzonesItemsDraggingEnabled: false,
        showDropzoneAreas: true,
        onDrop: function( event ) {
          that.onDrop( event )
        }
      },
      matieresPref: 0,
      bestMatieres: [
        null,
        null,
        null
      ],
      modal: {
        open: false,
        selected: null,
        color: null,
        number: 1
      },
      texts: this.getPageTexts(this.$route.name)
    }
  },
  created() {
    if ( this.activityLogs.selection_ids ) {
      const selection_ids = this.activityLogs.selection_ids
      this.matieres.forEach( function( matiere ) {
        matiere.value = ( selection_ids.indexOf( matiere.id ) !== -1 ) ? ( selection_ids.indexOf( matiere.id ) + 1 ) : 0
      } );
    } else {
      this.matieres.forEach( function( matiere ) {
        matiere.value = 0
      } );
    }
  },
  mounted() {
    this.$emit( 'setStep', 1 )
    if ( this.activityLogs.selection_ids && (this.matieres.filter((matiere) => matiere.value > 0).length === 3)) { // tab id matiere
      const podium = document.getElementsByClassName( 'dropchild border-dashed' )
      const matieresList = this.$refs.matieresList
      const matieresPref = this.matieres.filter( function( matiere, index ) {
        matiere.old_index = index
        return matiere.value !== 0
      } ).sort( this.compareValues )
      const matieresPrefText = matieresPref.map( function( matiere ) {
        return matiere.text
      } )

      let index = 0
      for ( let element of podium ) {
        let newElement = document.createElement( "div" );
        newElement.id = matieresPref[index].old_index
        newElement.className = 'bg-white text-2xl text-blue font-semibold text-center matiere h-12 w-full p-4 ml-4 rounded-full dropchild truncate matiere cursor-grab'
        newElement.innerHTML = matieresPref[index].text
        newElement.setAttribute( 'aria-grabbed', 'false' )
        newElement.setAttribute( 'draggable', 'true' )
        element.parentElement.childNodes[4].setAttribute( 'hidden', '' )
        element.parentElement.appendChild( newElement )
        index++
      }

      for ( let matiere of matieresList ) {
        if ( matieresPrefText.includes( matiere.innerText.trim() ) ) {
          matiere.className = 'border-dashed border-2 border-blue m-3 text-2xl px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center'
        }
      }

      this.matieresPref = matieresPref.length
    }
  },
  methods: {
    nextPage() {
      this.$emit( 'sendResults', this.matieres )
      this.$router.push( '/dashboard/student/domaines/introspection/matieres-preferees/etape-2' )
    },
    onDrop( event ) {
      if ( event.droptarget === event.items[0].parentElement )
        return

      if ( event.droptarget.classList.contains( 'matieres-list' ) ) {
        this.matieresPref--
        this.matieres[event.items[0].getAttribute( 'id' )].value = 0
        this.$emit( 'setStep', this.matieresPref + 1 )
        var dashed = null
        for ( var entry of event.droptarget.childNodes.entries() ) {
          if ( entry[1].innerText.trim() == event.items[0].innerText.trim() ) {
            dashed = entry[1]
          }

        }
        event.items[0].className = "bg-white border-dashed border-2 border-transparent cardShadow m-3 text-2xl px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center matiere cursor-grab"
        event.items[0].setAttribute( 'aria-grabbed', 'false' )
        event.items[0].parentElement.childNodes[4].removeAttribute( 'hidden' )
        // event.droptarget.replaceChild( event.items[0], dashed )
        event.droptarget.replaceChild( event.items[0], dashed )

      } else {
        let already = null
        for ( var entry of event.droptarget.childNodes.entries() ) {
          if ( entry[1].classList && entry[1].classList.contains( 'matiere' ) )
            already = entry[1]
        }
        if ( already ) {
          if ( event.items[0].parentElement.classList.contains( 'matiere-preferee' ) ) {
            let clone = already.cloneNode( true )
            let parent = event.items[0].parentElement
            event.droptarget.replaceChild( event.items[0], already )
            parent.appendChild( clone )
          }
        } else {
          this.matieresPref++
          this.matieres[event.items[0].getAttribute( 'id' )].value = parseInt( event.droptarget.getAttribute( 'id' ) )
          this.$emit( 'setStep', this.matieresPref + 1 )
          let clone = event.items[0].cloneNode( true )
          clone.className = 'bg-white text-2xl text-blue font-semibold text-center matiere h-12 w-full p-4 ml-4 rounded-full dropchild truncate matiere cursor-grab'
          clone.setAttribute( 'aria-grabbed', 'false' )
          event.droptarget.childNodes[4].setAttribute( 'hidden', '' )
          event.droptarget.appendChild( clone )
          event.items[0].removeAttribute( 'draggable' )

          if ( event.items[0].parentElement.classList.contains( 'matiere-preferee' ) ) {
            event.items[0].parentElement.childNodes[4].removeAttribute( 'hidden' )
            event.items[0].parentElement.removeChild( event.items[0] )
          } else {
            event.items[0].className = 'border-dashed border-2 border-blue m-3 text-2xl px-6 h-14 text-center flex-no-grow flex-no-shrink text-blue font-semibold rounded-full flex items-center'
            event.items[0].blur()
          }
        }
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
      if ( this.modal.selected != null ) {
        this.matieresPref++;
        this.bestMatieres[this.modal.number - 1] = this.matieres[this.modal.selected].text
        let that = this
        let unset = this.matieres.find( matiere => {
          return matiere.selected === that.modal.number
        } )
        if ( unset )
          unset.selected = undefined
        this.matieres[this.modal.selected].selected = this.modal.number
      } else {
        this.$set( this.error, this.modal.number - 1, true )
      }
      this.closeModal()
    },
    compareValues: function( a, b ) {
      const valueA = a.value;
      const valueB = b.value;

      let comparison = 0;
      if ( valueA > valueB ) {
        comparison = 1;
      } else if ( valueA < valueB ) {
        comparison = -1;
      }
      return comparison;
    }
  }
}
</script>

<style lang="scss" scoped>
.activity {
  .cardShadow {
    box-shadow: 0 2px 25px 0 rgba(14, 7, 51, 0.1);
  }

  .mobile-matiere-pref {
    @apply border-dashed text-2xl w-full border-white h-12 ml-4 border-2 rounded-full text-white justify-center text-center font-semibold items-center cursor-pointer;
  }

  .mobile-matiere-pref-selected {
    @apply bg-white text-2xl text-blue font-semibold text-center h-12 w-full p-4 ml-4 rounded-full  justify-center text-center font-semibold overflow-hidden items-center cursor-pointer;
  }

  .cursor-grab {
    cursor: grab;
  }
}
</style>

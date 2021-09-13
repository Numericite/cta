<template>
  <div class="w-32 h-screen flex flex-col items-center float-left bg-white sm:flex-row sm:h-32 sm:w-screen sm:z-10">

    <!-- DATA MENU -->
    <nuxt-link v-if="$store.state.auth.user.config.accountType === 'school' || $store.state.auth.user.config.accountType === 'collectivity'"
               to="/dashboard/data"
               class="toolbar__item block z-20 justify-center items-center w-32 text-center my-3 sm:mt-0 sm:flex-col sm:w-1/2 sm:justify-center relative">
      <svg class="mt-6 w-full sm:mt-0 sm:mb-3 sm:w-10 sm:h-10 z-10 bg-white"
           width="41"
           height="53"
           viewBox="0 0 41 53">
        <g fill="none"
           fill-rule="nonzero"
           class="stroke"
           stroke="#A2ACE3"
           stroke-width="2.7">
          <rect width="12.3"
                height="27.3"
                x="1.35"
                y="1.35"
                rx="3" />
          <rect width="14.3"
                height="12.3"
                x="19.35"
                y="1.35"
                rx="3" />
          <rect width="14.3"
                height="8.3"
                x="19.35"
                y="20.35"
                rx="3" />
        </g>
      </svg>
      <p class="inline-block px-1 mb-2 w-full text-blue-lighter uppercase mb-0 text-sm font-bold">Tableau de bord</p>
    </nuxt-link>

    <!-- MANAGER MENU -->
    <nuxt-link v-if="$store.state.auth.user.config.accountType === 'parent' || $store.state.auth.user.config.accountType === 'teacher' || $store.state.auth.user.config.accountType === 'mentor'"
               to="/dashboard/manager"
               class="toolbar__item block z-20 justify-center items-center w-32 text-center my-3 sm:mt-0 sm:flex-col sm:w-1/2 sm:justify-center relative">
      <svg class="mt-6 w-full sm:mt-0 sm:mb-3 sm:w-10 sm:h-10 z-10 bg-white"
           width="41"
           height="53"
           viewBox="0 0 41 53">
        <g fill="none"
           fill-rule="nonzero"
           class="stroke"
           stroke="#A2ACE3"
           stroke-width="2.7">
          <rect width="12.3"
                height="27.3"
                x="1.35"
                y="1.35"
                rx="3" />
          <rect width="14.3"
                height="12.3"
                x="19.35"
                y="1.35"
                rx="3" />
          <rect width="14.3"
                height="8.3"
                x="19.35"
                y="20.35"
                rx="3" />
        </g>
      </svg>
      <p class="inline-block px-1 mb-2 w-full text-blue-lighter uppercase mb-0 text-sm font-bold">Carnet de bord</p>
    </nuxt-link>

    <nuxt-link v-if="$store.state.auth.user.config.accountType === 'parent' || $store.state.auth.user.config.accountType === 'teacher' || $store.state.auth.user.config.accountType === 'mentor' || $store.state.auth.user.config.accountType === 'school' || $store.state.auth.user.config.accountType === 'collectivity'"
               to="/dashboard/documents"
               class="toolbar__item block z-20 justify-center items-center w-32 text-center my-3 sm:mt-0 sm:flex-col sm:w-1/2 sm:justify-center relative">
      <svg class="mt-6 w-full sm:mt-0 sm:mb-3 sm:w-10 sm:h-10 z-10 bg-white"
           width="41"
           height="53"
           viewBox="0 0 41 53">
        <path fill="#A2ACE3"
              class="path"
              fill-rule="evenodd"
              d="M9.776 4.753L4.77 9.743h5.006v-4.99zm-6.98 7.772v26.434h15.358c.77 0 1.396.623 1.396 1.392 0 .768-.626 1.391-1.396 1.391H1.399c-.77 0-1.396-.623-1.396-1.391V11.21c-.024-.37.1-.754.409-1.062l9.773-9.74c.309-.307.695-.43 1.066-.406H30.72c.77 0 1.396.623 1.396 1.391v24.348c0 .768-.626 1.391-1.396 1.391-.77 0-1.396-.623-1.396-1.39V2.785H12.569v8.347c0 .768-.626 1.392-1.396 1.392H2.795zm18.15 34.782c-.77 0-1.396-.623-1.396-1.391 0-.769.625-1.391 1.396-1.391h13.962c.771 0 1.397.622 1.397 1.391 0 .768-.626 1.391-1.397 1.391H20.946zm8.378-12.641v5.685c0 .768-.626 1.391-1.397 1.391-.77 0-1.396-.623-1.396-1.391v-5.685l-1.107 1.103a1.4 1.4 0 0 1-1.974 0 1.388 1.388 0 0 1 0-1.967l3.49-3.48a1.4 1.4 0 0 1 1.974 0l3.491 3.48a1.388 1.388 0 0 1 0 1.967 1.4 1.4 0 0 1-1.974 0l-1.107-1.103z" />
      </svg>
      <p class="inline-block px-1 mb-2 w-full text-blue-lighter uppercase mb-0 text-sm font-bold">Mes documents</p>
    </nuxt-link>
    <!-- MANAGER MENU END -->

    <!-- STUDENT MENU -->
    <nuxt-link v-if="$store.state.auth.user.config.accountType === 'student'"
               to="/dashboard/student"
               class="toolbar__item block z-20 justify-center items-center w-32 text-center my-3 sm:mt-0 sm:flex-col sm:w-1/3 sm:justify-center relative">
      <svg class="mt-6 mb-4 w-full sm:mt-0 sm:mb-3 sm:w-10 sm:h-10 z-10 bg-white"
           width="41"
           height="53"
           viewBox="0 0 41 53">
        <g fill="#A2ACE3"
           class="path"
           fill-rule="evenodd">
          <path fill-rule="nonzero"
                d="M38.053.366H3.128C1.638.366.425 1.59.425 3.092v46.966c0 1.503 1.213 2.727 2.703 2.727h34.925c1.49 0 2.702-1.224 2.702-2.727V3.092c0-1.503-1.212-2.726-2.702-2.726zm.624 49.692c0 .347-.28.629-.624.629H3.128a.627.627 0 0 1-.624-.63V3.092c0-.347.28-.629.624-.629h34.925c.344 0 .624.282.624.63v46.966z" />
          <path d="M33.084 29.382H8.097c-.689 0-1.248.564-1.248 1.258 0 .695.559 1.258 1.248 1.258h24.987c.688 0 1.246-.563 1.246-1.258 0-.694-.558-1.258-1.246-1.258zM33.084 36.092H8.097c-.689 0-1.248.564-1.248 1.258 0 .695.559 1.258 1.248 1.258h24.987c.688 0 1.246-.563 1.246-1.258 0-.694-.558-1.258-1.246-1.258zM33.084 42.801H8.097c-.689 0-1.248.564-1.248 1.258 0 .695.559 1.258 1.248 1.258h24.987c.688 0 1.246-.563 1.246-1.258 0-.694-.558-1.258-1.246-1.258zM13.229 23.3c2.728 1.228 7.07 1.3 7.07 1.3 4.393.094 7.527-1.113 7.527-1.113.465-.296.478-.528.478-.528.324-2.83-1.063-3.84-1.063-3.84-.417-.933-2.96-1.741-2.96-1.741-.95-.338-1.239-.674-1.327-.847l-.025-.056v-.007l-.014-.053v-.001c-.037-.134-.127-.473-.127-.557l-.016.075a1.902 1.902 0 0 1-.004-.265l-.022.024c.315-.408.571-.873.74-1.347.29-.291.438-1 .438-1 .193-.544.136-.82.048-.961.027-.082.488-1.521-.163-2.631 0 0-.232-1.05-1.457-1.516-1.132-.7-2.82-.28-2.82-.28-2.058.35-2.499 2.79-2.499 2.79-.122 1.116.174 1.812.186 1.841-.105.264.086.954.086.954.066.422.27.55.329.58a4.799 4.799 0 0 0 .695 1.468s.035.171.003.411l-.007-.039c0 .098-.118.456-.133.502l-.003.007a5.607 5.607 0 0 1-.285.353c-.767.597-2.61 1.16-2.61 1.16-.8.312-1.14.778-1.14.778-1.186 1.772-1.326 3.543-1.326 3.543.015.902.4.996.4.996z" />
        </g>
      </svg>
      <p class="inline-block px-1 mb-2 w-full text-blue-lighter uppercase mb-0 text-sm font-bold">Carnet de bord</p>
    </nuxt-link>
    <div v-if="$store.state.auth.user.config.accountType === 'student' && canAccess.domains.display && (canAccess.domains.displayIntrospection || canAccess.domains.displayExploration)"
         :class="{'active': $route.name === 'dashboard-student-domaines-introspection' || $route.name.substring(0, 'dashboard-student-domaines-exploration'.length) === 'dashboard-student-domaines-exploration', 'menu-active': activeDomaines}"
         class="toolbar__item block z-20 justify-center items-center w-32 text-center my-3 sm:mt-0 sm:flex-col sm:w-1/3 sm:justify-center relative"
         @click="setActiveDomaines">
      <svg class="mt-6 mb-4 w-full sm:mt-0 sm:mb-3 sm:w-10 sm:h-10 z-10 bg-white"
           width="42"
           height="31"
           viewBox="0 0 42 31">
        <g fill="none"
           fill-rule="evenodd">
          <path class="path"
                fill="#A2ACE3"
                fill-rule="nonzero"
                d="M22.476 18.406c4.313 0 7.81-3.47 7.81-7.75 0-4.28-3.497-7.75-7.81-7.75s-7.81 3.47-7.81 7.75c0 4.28 3.497 7.75 7.81 7.75zm8.152-.463l9.8 9.212c.761.754 1.462 1.565.694 2.327-.762.757-1.903-.05-2.662-.803l-9.624-9.304c-.33.601-3.216 1.988-5.493 1.988-6.47 0-11.605-4.286-11.605-10.707C11.738 4.236 16.007 0 22.476 0c6.47 0 11.067 5.655 10.738 11.836-.095 1.799-1.436 4.633-2.586 6.107z" />
          <path class="stroke"
                stroke="#A2ACE3"
                stroke-linecap="round"
                stroke-width="3"
                d="M2 1.5h6M2 8.5h6M2 15.5h6M2 22.5h11M2 29.5h27" />
        </g>
      </svg>
      <p class="inline-block px-1 mb-2 w-full text-blue-lighter uppercase mb-0 text-sm font-bold">Domaines</p>
      <nuxt-link v-if="canAccess.domains.displayIntrospection" to="/dashboard/student/domaines/introspection"
                 class="item__legend px-4 opacity-0 hidden z-0 absolute top--50 h-full items-center bg-white mb-0 text-sm font-bold text-blue-lighter uppercase"
                 @click="setActiveDomaines">
        Connaissance de soi
      </nuxt-link>
      <nuxt-link v-if="canAccess.domains.displayExploration" :to="stepCalculator($store.state.auth.user.config.currentStep) === 3 ? '/dashboard/student/domaines/exploration/explanations' : stepCalculator($store.state.auth.user.config.currentStep) > 3 ? '/dashboard/student/domaines/exploration/rex' : '#'"
                 :class="{'disabled': stepCalculator($store.state.auth.user.config.currentStep) < 3}"
                 class="item__legend px-4 opacity-0 hidden z-0 absolute top-50 h-full items-center bg-white mb-0 text-sm font-bold text-blue-lighter uppercase"
                 @click="setActiveDomaines">
        Exploration
      </nuxt-link>
    </div>
    <div v-if="$store.state.auth.user.config.accountType === 'student' && canAccess.streams.display && (canAccess.streams.displayIntrospection || canAccess.streams.displayExploration)"
         :class="{'active': $route.name.substring(0, 'dashboard-student-filieres-introspection'.length) === 'dashboard-student-filieres-introspection' || $route.name.substring(0, 'dashboard-student-filieres-exploration'.length) === 'dashboard-student-filieres-exploration', 'menu-active': activeFilieres, 'disabled': stepCalculator($store.state.auth.user.config.currentStep) < 5}"
         class="toolbar__item block z-20 justify-center items-center w-32 text-center my-3 sm:mt-0 sm:flex-col sm:w-1/3 sm:justify-center relative"
         @click="setActiveFilieres">
      <svg class="mt-6 mb-4 w-full sm:mt-0 sm:mb-3 sm:w-10 sm:h-10 z-10 bg-white"
           width="42"
           height="44"
           viewBox="0 0 42 44">
        <g fill="#A2ACE3"
           class="path"
           fill-rule="evenodd">
          <path fill-rule="nonzero"
                d="M39.904 28.228V4.582c0-2.253-1.781-4.087-3.97-4.087-2.188 0-3.968 1.833-3.968 4.087v10.461a3.84 3.84 0 0 0-1.63-.37c-1.69 0-3.127 1.099-3.698 2.635a3.798 3.798 0 0 0-1.9-.527c-1.621 0-3.014 1.02-3.63 2.474a4.119 4.119 0 0 0-1.968-.51c-2.189 0-3.97 1.671-3.97 3.726V34.1c0 4.026 2.894 8.925 11.051 9.484l.535.042a30.85 30.85 0 0 0 2.622.133c2.592 0 5.858-.492 7.965-3.03 1.943-2.34 2.894-3.58 3.843-5.638 1.12-2.426.339-5.145-1.011-6.576l-.271-.287zM34.305 4.582c0-.963.731-1.747 1.629-1.747.899 0 1.629.784 1.629 1.747v21.166l-3.066-3.25c-.06-.063-.13-.108-.192-.168V4.582zM28.706 18.76c0-.963.732-1.747 1.63-1.747.898 0 1.629.784 1.629 1.747v2.477c-1.127-.132-2.287.189-3.159 1.013-.038.035-.064.078-.1.116V18.76zm0 9.164c.007.007.011.015.018.021l2.355 2.497c-.23.128-.482.206-.743.206-.898 0-1.629-.784-1.629-1.747v-.977zm-5.598-7.01c0-.989.731-1.793 1.63-1.793.898 0 1.628.805 1.628 1.793v8.399c0 .988-.73 1.792-1.628 1.792-.898 0-1.63-.804-1.63-1.792v-8.4zm-5.599 1.556c0-.764.732-1.386 1.63-1.386.899 0 1.629.622 1.629 1.387V29.718c0 .764-.731 1.386-1.629 1.386s-1.63-.622-1.63-1.386V22.47zm21.553 11.642c-.793 1.718-1.537 2.736-3.518 5.122-2.154 2.593-6.506 2.233-8.595 2.06l-.569-.044c-8.23-.565-8.87-5.614-8.87-7.149v-.993a4.152 4.152 0 0 0 1.629.337 4.074 4.074 0 0 0 2.86-1.155c.713.711 1.675 1.154 2.739 1.154 1.21 0 2.282-.578 3.01-1.47.697.623 1.595 1.013 2.587 1.013a3.88 3.88 0 0 0 2.109-.623c.094-.061.172-.136.243-.217l.167.177a1.17 1.17 0 0 0 1.703-1.605l-4.131-4.379c-.661-.7-.667-1.773-.013-2.389.652-.617 1.723-.548 2.384.152l5.675 6.017c.802.851 1.242 2.577.59 3.991z" />
          <path d="M23.065 9.153a1.405 1.405 0 0 0 2.746-.587l-1.54-7.195a1.404 1.404 0 0 0-2.746.587l1.54 7.195zM15.678 13.107l-5.272-5.544A1.405 1.405 0 0 0 8.37 9.496l5.272 5.543a1.401 1.401 0 0 0 1.985.051 1.404 1.404 0 0 0 .05-1.984zM1.427 22.47l7.283 2.386a1.4 1.4 0 0 0 1.77-.898 1.404 1.404 0 0 0-.897-1.77L2.3 19.802a1.405 1.405 0 0 0-.873 2.669zM9 32.63l-7.457 1.747a1.404 1.404 0 1 0 .641 2.733l7.456-1.747A1.404 1.404 0 1 0 9 32.63z" />
        </g>
      </svg>
      <p class="inline-block px-1 mb-2 w-full text-blue-lighter uppercase mb-0 text-sm font-bold">Filières</p>
      <nuxt-link v-if="canAccess.streams.displayIntrospection" to="/dashboard/student/filieres/introspection/explanations"
                 class="item__legend px-4 opacity-0 hidden z-0 absolute top--50 h-full items-center bg-white mb-0 text-sm font-bold text-blue-lighter uppercase"
                 @click="setActiveFilieres">
        Connaissance de soi
      </nuxt-link>
      <nuxt-link v-if="canAccess.streams.displayExploration" :to="stepCalculator($store.state.auth.user.config.currentStep) === 6 ? '/dashboard/student/filieres/exploration/explanations' : stepCalculator($store.state.auth.user.config.currentStep) > 6 ? '/dashboard/student/filieres/exploration/rex' : '#'"
                 :class="{'disabled': stepCalculator($store.state.auth.user.config.currentStep) < 6}"
                 class="item__legend px-4 opacity-0 hidden z-0 absolute top-50 h-full items-center bg-white mb-0 text-sm font-bold text-blue-lighter uppercase"
                 @click="setActiveFilieres">
        Exploration
      </nuxt-link>
    </div>
    <div v-if="$store.state.auth.user.config.accountType === 'student' && canAccess.domains.display && (canAccess.domains.displayIntrospection || canAccess.domains.displayExploration)"
         :class="{'active': $route.name.substring(0, 'dashboard-student-etablissements-introspection'.length) === 'dashboard-student-etablissements-introspection' || $route.name.substring(0, 'dashboard-student-etablissements-exploration'.length) === 'dashboard-student-etablissements-exploration', 'menu-active': activeEtablissements, 'disabled': stepCalculator($store.state.auth.user.config.currentStep) < 8}"
         class="toolbar__item block z-20 justify-center items-center w-32 text-center my-3 sm:mt-0 sm:flex-col sm:w-1/3 sm:justify-center relative"
         @click="setActiveDomaines">
      <svg
        class="mt-6 mb-4 w-full sm:mt-0 sm:mb-3 sm:w-10 sm:h-10 z-10 bg-white"
        width="42"
        height="44"
        viewBox="0 0 42 44">
        <defs><style>.cls-1{fill:none}.cls-1,.cls-3,.cls-4,.cls-5{stroke:#a2ace3}.cls-1,.cls-3,.cls-5{stroke-linecap:round;stroke-linejoin:round}.cls-1,.cls-3,.cls-4,.cls-5{stroke-width:2px}.cls-3,.cls-4{fill:#a2ace3}.cls-4{stroke-miterlimit:10}</style>
        </defs>
        <g fill="#A2ACE3"
           class="path"
           fill-rule="evenodd">
          <path fill-rule="nonzero" class="cls-1" d="M31 14h10v17H1V14h10"/>
          <path class="cls-1" d="M31 31H11V11.44l10-4 10 4V31z"/>
          <circle class="circle" cx="21" cy="15.28" r="2.32" fill="#a2ace3"/>
          <path class="cls-1" d="M16.68 23.6h8.64V31h-8.64zM21 23.6V31"/>
          <path class="cls-3" d="M21 7.44V1"/>
          <path class="cls-4" d="M21 1h3a.62.62 0 01.62.62V3a.62.62 0 01-.62.62h-3V1z"/>
          <path class="cls-4" d="M24.13 2.1H27a.16.16 0 01.16.16v2.32a.16.16 0 01-.16.16h-2.87a.62.62 0 01-.62-.62V2.71a.62.62 0 01.62-.61z"/>
          <path class="cls-3" d="M31 13.44l-10-4-10 4v-2l10-4 10 4v2z"/>
          <path class="cls-5" d="M4.45 18.25h3.1M4.45 22.5h3.1M4.45 26.75h3.1M34.45 18.25h3.1M34.45 22.5h3.1M34.45 26.75h3.1"/>
        </g>
      </svg>
      <p class="inline-block px-1 mb-2 w-full text-blue-lighter uppercase mb-0 text-sm font-bold">Etablissements</p>
      <nuxt-link v-if="canAccess.schools.displayIntrospection"
                 :to="$store.state.auth.user.config.currentStep === 8 ? '/dashboard/student/etablissements/introspection/explanations' : stepCalculator($store.state.auth.user.config.currentStep) > 8 ? '/dashboard/student/etablissements/introspection/identifier' : '#'"
                 class="item__legend px-4 opacity-0 hidden z-0 absolute top--50 h-full items-center bg-white mb-0 text-sm font-bold text-blue-lighter uppercase"
                 @click="setActiveEtablissements">
        Connaissance de soi
      </nuxt-link>
      <nuxt-link v-if="canAccess.schools.displayExploration"
                 :to="$store.state.auth.user.config.currentStep === 10 ? '/dashboard/student/etablissements/exploration/explanations' : stepCalculator($store.state.auth.user.config.currentStep) > 10 && !$store.state.auth.user.config.school_uais ? '/dashboard/student/etablissements/exploration/rex' : $store.state.auth.user.config.school_uais ? '/dashboard/student/etablissements/exploration/project' : '#'"
                 :class="{'disabled': stepCalculator($store.state.auth.user.config.currentStep) < 10}"
                 class="item__legend px-4 opacity-0 hidden z-0 absolute top-50 h-full items-center bg-white mb-0 text-sm font-bold text-blue-lighter uppercase"
                 @click="setActiveEtablissements">
        Exploration
      </nuxt-link>
    </div>
  </div>
</template>

<script>
  import _ from 'lodash'
export default {
  name: 'Toolbar',
  data() {
    return {
      activeDomaines: false,
      activeFilieres: false,
      activeEtablissements: false,
      canAccess: {
        domains: {
          display: false,
          displayIntrospection: true,
          displayExploration: false
        },
        streams: {
          display: false,
          displayIntrospection: false,
          displayExploration: false
        },
        schools: {
          display: false,
          displayIntrospection: false,
          displayExploration: false
        }
      }
    }
  },
  created() {
    if(this.$store.state.auth.user.course) {
      // CHAPITRE
      const chapters = this.$store.state.auth.user.course.chapters || []
      // DOMAINES
      const domain = _.find(chapters, {slug: 'activity-area'}) || {}
      this.canAccess.domains.display = _.get(domain, 'display', false)
      // DOMAINES INTROSPECTION
      this.canAccess.domains.displayIntrospection = _.get(_.find(domain.children, {slug: 'introspection'}), 'display', false)
      // DOMAINES EXPLORATION
      this.canAccess.domains.displayExploration = _.get(_.find(domain.children, {slug: 'exploration'}), 'display', false)

      // FILIERES
      const stream = _.find(chapters, {slug: 'stream'}) || {}
      this.canAccess.streams.display = _.get(stream, 'display', false)
      // FILIERES INTROSPECTION
      this.canAccess.streams.displayIntrospection = _.get(_.find(stream.children, {slug: 'introspection'}), 'display', false)
      // FILIERES EXPLORATION
      this.canAccess.streams.displayExploration= _.get(_.find(stream.children, {slug: 'exploration'}), 'display', false)

      // ÉTABLISSEMENTS
      const school = _.find(chapters, {slug: 'school'}) || {}
      this.canAccess.schools.display = _.get(school, 'display', false)
      // ÉTABLISSEMENTS INTROSPECTION
      // this.canAccess.schools.displayIntrospection = _.get(_.find(school.children, {slug: 'introspection'}), 'display', false)
      // ÉTABLISSEMENTS EXPLORATION
      // this.canAccess.schools.displayExploration= _.get(_.find(school.children, {slug: 'exploration'}), 'display', false)
    }
  },
  methods: {
    setActiveDomaines() {
      this.activeDomaines = !this.activeDomaines;
      this.activeFilieres = false;
      this.activeEtablissements = false;
    },
    setActiveFilieres() {
      this.activeFilieres = !this.activeFilieres;
      this.activeDomaines = false;
      this.activeEtablissements = false;
    },
    setActiveEtablissements() {
      this.activeEtablissements = !this.activeEtablissements;
      this.activeDomaines = false;
      this.activeFilieres = false;
    }
  }
}
</script>


<style lang="scss" scoped>
.toolbar__item {
  border-right: 1px solid white;
  position: relative;
  z-index: 10;
  cursor: pointer;

  .item__legend {
    width: 120%;
    text-align: center;

    &.top--50 {
      top: -49%;
    }

    &.top-50 {
      top: 50%;
    }
  }

  @screen sm {
    border-right: 0;
  }
  .item__legend {
    -webkit-transition: all 0.25s;
    -moz-transition: all 0.25s;
    -o-transition: all 0.25s;
    transition: all 0.25s;
    transform: translateY(0%);
    &.disabled {
      color: #b2b4b9
    }
  }
  svg g,
  svg .stroke,
  svg .path {
    transition: all 0.3s ease;
  }

  &.disabled {
    p {
      color: #b2b4b9 !important;
    }

    svg {
      & .path {
        fill: #b2b4b9 !important;
      }
      & .stroke {
        stroke: #b2b4b9 !important;
      }
    }
  }
  @screen sm {
    &:not(.disabled).menu-active {
      border-color: transparent;
      .item__legend {
        display: flex;
        opacity: 1;
        transform: translateY(-160%);
      }
      svg .path {
        fill: #ff8f5e;
      }
      svg .stroke {
        stroke: #ff8f5e;
      }
      span,
      p {
        @apply text-orange;
      }
    }
  }

  &:not(.disabled):hover {
    @screen mdmin {
      border-color: transparent;
      .item__legend {
        opacity: 1;
        display: flex;
        transform: translateX(83%);
        border-right: 1px solid #ff8f5e;
        @screen sm {
          border-right: 0;
          transform: translate(0, -160%);
        }
      }
      svg .cls-1, .cls-3, .cls-4, .cls-5, .circle {
        stroke: #ff8f5e;
      }
      svg .circle {
        fill: #ff8f5e;
      }
      svg .path {
        fill: #ff8f5e;
      }
      svg .stroke {
        stroke: #ff8f5e;
      }
      span,
      p {
        @apply text-orange;
      }
    }
  }
}

.toolbar__item.active {
  p {
    @apply text-orange;
  }
  svg .cls-1, .cls-3, .cls-4, .cls-5, .circle {
    stroke: #ff8f5e;
  }
  svg .circle {
    fill: #ff8f5e;
  }
  svg .path {
    fill: #ff8f5e;
  }
  svg .stroke {
    stroke: #ff8f5e;
  }
}

.nuxt-link-exact-active {
  &.item__legend {
    @apply text-orange;
    border-right: 1px solid #ff8f5e;
  }
  @screen sm {
    border-right: 0;
  }
  svg .cls-1, .cls-3, .cls-4, .cls-5, .circle {
    stroke: #ff8f5e;
  }
  svg .circle {
    fill: #ff8f5e;
  }

  svg .path {
    fill: #ff8f5e;
  }
  svg .stroke {
    stroke: #ff8f5e;
  }
  span,
  p {
    @apply text-orange;
  }
}
</style>

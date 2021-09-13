<template>
  <div class="flexContainer-dash dashboard-exploration ml-32 sm:ml-0 mt-12">
    <h3 class="title font-bold text-4xl text-blue"><span v-html="texts[0]" /></h3>
    <div class="desc text-blue text-3xl sm:text-xl font-normal mt-4">
      <span v-html="texts[1]" />
    </div>
    <div class="desc text-blue text-3xl sm:text-xl font-normal mt-4 pb-10">
      Après avoir établi ta fiche établissement et fait tes premiers choix d'établissements,
      il est temps maintenant d’explorer en profondeur tous les champs des  possibles.
    </div>
    <videoComp :vimeoId="texts[2]"
               class="w-full mt-4"
               @clicked="incrementCurrentStep()"/>
    <div class="desc text-blue text-3xl sm:text-xl font-normal mt-4 pt-6">
      <div>
        Avant de te lancer il faut que tu élabores ton plan d’action.
        Tu trouveras à la rubrique fiche pédagogique, une fiche qui t’explique ce qu’est un plan d’action et
        comment le construire.Certains évènements sont des éléments incontournables de
        ton plan d’action. Nous allons en décrire quelques-uns.
      </div>
      <div class="font-bold py-6">
        Les deux semaines de l’orientation, c’est quoi ?
      </div>
      <div>
        Organisées par les lycées au cours de 1er et 2nd trimestres,
        ces deux semainesde l’orientation ont pour objectif de t’informer
        sur les différents métiers et lesétudes pour y accéder.
        Cela va ensuite t’aider à affiner ton projet et à finaliser tes
        choix de poursuite d’études. Au   cours   de   ces   deux   semaines,
        tu   peux   rencontrer   des   représentantsd’établissements d’enseignement supérieur,
        des psychologues de l’Éducationnationale,   des   acteurs   du   monde   économique
        et   social,   d’anciens   élèves,participer à des forums de l’orientation,
        à des salons, à des journées portesouvertes, à des journées d’immersion dans des formations supérieures...
      </div>
      <div class="font-bold py-6">
        Les journées portes ouvertes (JPO) c’est quoi ?
      </div>
      <div>
        Les   JPO   sont   organisées   pour   accueillir   les   futurs   bacheliers
        dans   tous   lesétablissements de ta Région offrant des formations post-bac.
        Ces journées sontl’occasion de présenter leurs programmes, leurs cursus et leurs quotidiens.
        Pendant les JPO, les futurs bacheliers ont aussi la possibilité
        de visiter leur futurétablissement, de rencontrer des étudiants et de conforter ainsi leur choix.
      </div>
      <div class="font-bold py-6">
        Les salons, c’est quoi ?
      </div>
      <div>
        Chaque année des évènements destinés aux lycéens sont organisés
        sous forme de salons dans de nombreuses villes en France.
        Ces salons réunissent dans unmême lieu et selon le thème du salon des représentants de l’enseignement
        supérieur, des spécialistes de l’orientation, des experts d’un secteur.
      </div>
      <div class="font-bold py-6">
        Que   faire   pendant   ces   évènements   où   tu   rencontres   des   acteurs   del’enseignement supérieur ?
      </div>
      <div>L’objectif est d’échanger avec les représentants des écoles sur les stands,
      departiciper à des conférences, de collecter de la documentation.
      Il est aussipossible de rencontrer des étudiants qui suivent les formations présentées
      lors deces évènements pour confronter tes choix ; n’hésite pas à aller vers eux car ils
      ont souvent une vision différente et complémentaire de celle des représentantsdes établissements.</div>
    </div>
    <nuxt-link to="/dashboard/student/etablissements/exploration/rex">
      <button class="button button--blue mt-8">Je continue</button>
    </nuxt-link>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import VideoComp from '~/components/Video'

  export default {
    name: 'ExplorationExplanations',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    scrollToTop: true,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    components: { VideoComp },
    data() {
      return {
        texts: this.getPageTexts(this.$route.name)
      }
    },
    methods: {
      async incrementCurrentStep() {
        const user = this.$store.state.auth.user
        if (this.stepCalculator(user.config.currentStep) < 11) {
          this.$store.commit('auth/setCurrentStep', 11)
          await this.$api.user.updateUser(this.$store.state.auth.user)
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>

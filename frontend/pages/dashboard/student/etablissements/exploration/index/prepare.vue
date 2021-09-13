<template>
  <div class="flexContainer-dash min-h-screen dashboard-exploration ml-32 sm:ml-0 mt-12">
    <div class="desc text-blue text-3xl sm:text-xl font-normal mt-4 pb-10">
      <div class="pb-6">Après avoir établi ta fiche établissement et fait tes premiers choix d'établissements,
      il est temps maintenant d’explorer en profondeur tous les champs des  possibles.</div>
      <videoComp :vimeoId="texts[2]"
                 class="w-full mt-4"
                 @clicked="incrementCurrentStep()"/>
      <div class="font-bold py-6">
        Les questions á explorer
      </div>
      <question-exploration v-for="question in questions" :key="question.title" :question="question"/>
      <div>
        Tu   devras   te   poser   ces   questions   pour
        chaque   établissement   proposé   lors   de   ta
        recherche, n’hésite pas pour cela à faire des fiches
        par établissement et à les classerensuite par préférence.
        Pour cela il faut que tu identifies les critères les plus
        importants pour   toi   :   cela   peut   être   le   fait   que   l’établissement
        a   des   partenariats   avec   des établissements étrangers, ou que les activités
        associatives proposées te correspondent... tu es le seul à pouvoir classer ces critères.
        Quand tu auras choisi tes établissements, reporte les dans ta Fiche Projet Etudes Sup.
      </div>
    </div>
    <nuxt-link to="/dashboard/student/etablissements/exploration/project">
      <button class="button button--blue mt-8">Je continue</button>
    </nuxt-link>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import VideoComp from '~/components/Video'
  import _ from 'lodash'
  import QuestionExploration from '../../../../../../components/dashboard/etablissement/QuestionExploration'

  export default {
    name: 'PrepareExploration',
    layout: 'dashboard_student',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessHighSchoolStudentDashboard',
    scrollToTop: true,
    components: {
      VideoComp, QuestionExploration
    },
    data() {
      return {
        texts: this.getPageTexts(this.$route.name),
        questions: [
          { title: 'Question localisation : ',
            questionList: [
            'L’établissement est-il facilement accessible ?',
            'Combien de temps mets-tu pour y accéder ?',
            'Est-ce compatible avec ta charge de travail ?',
            'Le campus donne-t-il envie ?',
            'Les abords sont-ils agréables ? '
            ]
          },
          { title: 'Question locaux : ',
            questionList: [
              'Sont-ils modernes, bien aménagés ?',
              'L’endroit est-il accueillant ?',
              'Les salles de classe sont-elles en bon état et bien équipées ?',
              'Y a-t-il des aménagements pour faciliter les études et la vie étudiante (logements à proximité par exemple)',
              'Quels sont les horaires/jours d’accès aux locaux ? '
            ]
          },
          { title: 'Question enseignants : ',
            questionList: [
              'Sont-ils accueillants et disponibles ?',
              'D’où viennent les enseignants et comment sont-ils impliqués dans l’école ?',
              'Encouragent-ils les initiatives des étudiants ?',
              'Des exemples ?',
              'Y a-t-il des réunions avec les parents ?'
            ]
          },
          { title: 'Question Formation : ',
            questionList: [
              'Le diplôme envisagé est-il reconnu ? ',
              'Quel est la place de l’établissement dans les différents classements ? ',
              'Quels sont les critères pris en compte pour ces classements ? ',
              'A combien s’élèvent les frais d’inscription et de scolarité ? Que comprennent-ils ? ',
              'Quels sont les effectifs en 1ère année et en 2ème année ? ',
              'Comment s’organisent les cursus ? ',
              'Quelles sont les méthodes de travail ? ',
              'Quelles sont les approches pédagogiques ? Cours magistraux ? Classe inversée ? Formation à distane ?',
              'Travaux en groupe ? Mémoires ?',
              'Comment se font les évaluations ?  ',
              'Y a-t-il des study-tours à l’étranger ? ',
              'Y a-t-il des partenariats avec des établissements étrangers ? ',
              'Quels sont les moyens pour accompagner les étudiants dans la recherche de stages ? ',
              'Quelles sont les possibilités de poursuite d’études ? ',
              'Quels sont les débouchés ? ',
              'Quel est le salaire d’embauche des diplômés ?',
            ]
          },
          { title: 'Questions élèves : ',
            questionList: [
              'Quel est le rythme de travail ?  ',
              'Est-ce qu’il y a du temps pour les loisirs / la vie personnelle ou un travail complémentaire ? ',
              'Font-ils des travaux en groupe ? ',
              'Quels sont les moyens dont ils disposent (informatique, licences logiciels à prix réduits, bases de don-nées, centre de langues, ' +
              'de documentation, restaurant scolaire, équipements sportifs, fablab, ...) ? ',
              'Participent-ils à des associations ? Lesquelles ? ',
              'Y-a-t-il une vie collective sur le campus ? ',
            ]
          },
          { title: 'Autres questions ? ',
            questionList: [
            ]
          }
          ]
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

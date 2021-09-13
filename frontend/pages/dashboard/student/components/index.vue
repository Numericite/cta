<template>
  <div class="ms-container min-h-screen">
    <div class="flex flex-col w-full justify-center">
      <h1 class="ms-title pb-32 text-center uppercase">COMPONENTS :</h1>
      <div class="flex flex-wrap mb-20 justify-center w-full">
        <Entity v-for="entity in entities" :key="entity.name" :entity="entity" :row="false" class="px-6 pb-12"/>
      </div>
      <div class="flex mb-20 justify-center w-full">
        <HelpMessage :helpMessage="helpMessage"/>
      </div>
      <div class="flex flex-wrap mb-20 justify-center w-full">
        <NextAction v-for="action in actions" :key="action.entity" :action="action" class="py-6"/>
      </div>
    </div>
  </div>
</template>

<script>
  import Entity from '../../../../components/middleSchool/Entity'
  import HelpMessage from '../../../../components/middleSchool/HelpMessage'
  import NextAction from '../../../../components/middleSchool/NextAction'
  export default {
    name: 'Components',
    layout: 'dashboard_ms_student',
    components: {
      NextAction,
      HelpMessage,
      Entity
    },
    data() {
      return {
        entities: [
          {
            name: 'pistes',
            description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pretium diam ipsumddd.',
            info: 'Prochaine échéance dans ',
            moreInfo: '1 mois',
            illustration: 'step2'
          },
          {
            name: 'explorer',
            description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pretium diam ipsumddd.',
            count: 5,
            info: 'actions restantes',
            moreInfo: '',
            illustration: 'step3'
          },
          {
            name: 'modules',
            description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pretium diam ipsumddd.',
            count: 7,
            info: 'modules restants',
            moreInfo: '',
            illustration: 'step1'
          },
        ],
        helpMessage: {
          title: 'On est là pour t’aider !',
          message: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pretium diam ipsum, a efficitur sapien viverra at. Ut porttitor ante tempus tincidunt accumsan. Maecenas sollicitudin, enim tempor sagittis tempus, dui est accumsan velit, laoreet pulvinar lectus purus eget urna. Integer sagittis elit at sapien ultrices, eu rhoncus lectus laoreet.'
        },
        actions: [
          {
          entity: 'module',
          countActions: 3,
          title: 'Le monde du travail est composé de structures de tailles différentes',
          description: 'À l’aide de notre outil d’aide à la décision, tu vas pouvoir avancer ta réflexion sur ton orientation professionnele et scolaire.',
        },
        {
          entity: 'explorer',
          countActions: 5,
          title: 'Réalise une exploration sur le terrain',
          description: 'À l’aide de notre outil d’aide à la décision, tu vas pouvoir avancer ta réflexion sur ton orientation professionnele et scolaire.',
        },
        {
          entity: 'pistes',
          title: 'Complète et affine ton projet d’orientation',
          description: 'À l’aide de notre outil d’aide à la décision, tu vas pouvoir avancer ta réflexion sur ton orientation professionnele et scolaire.',
        },
        ]
      }
    },
    created() {
    }
  }
</script>

<style scoped>
</style>

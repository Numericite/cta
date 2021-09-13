<template>
  <div class="pt-16 px-5 mx-0 lg:px-0 xl:px-0 xl:pt-32 flex flex-col">
    <div class="ms-title">Retrouve les badges que tu as collectés</div>
    <div class="pt-5 pb-11 ms-subtitle-small text-justify text-ms-gray-dark max-w-2/3">
      Tout au long de l’année, tu vas apprendre de nouvelles choses et acquérir de nouvelles compétences.
      Et pour toute nouvelle compétence acquise, tu auras un badge, que tu retrouveras dans cette
      rubrique.
    </div>
    <div class="flex justify-center items-center">
      <div class="dot mr-3 bg-black"/>
      <div class="ms-subtitle">LES PEPITES</div>
      <div class="dot ml-3 bg-black"/>
    </div>
    <div class="flex pt-16 justify-center w-full">
      <HelpMessage :helpMessage="helpMessage"/>
    </div>
    <div class="flex xl:flex-row md:flex-row flex-col pt-16">
      <Badge v-for="(badge, indexBadge) in badges" :key="indexBadge" :badge="badge" :row="false" class="px-12 pb-16"/>
    </div>
    <div class="flex justify-center items-center pt-16">
      <div class="dot mr-4 bg-black"/>
      <div class="ms-subtitle">LES AUTRES BADGES</div>
      <div class="dot ml-4 bg-black"/>
    </div>
    <div class="flex flex-wrap xl:flex-row md:flex-row flex-col pl-20 xl:pl-0 pt-16">
      <Badge v-for="(badge, indexBadge) in badgesRows" :key="indexBadge" :badge="badge" :row="true" class="xl:pr-20 pb-16"/>
    </div>
  </div>
</template>

<script>
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import HelpMessage from '../../../../../components/middleSchool/HelpMessage'
  import Badge from '../../../../../components/middleSchool/Badge'

  export default {
    name: 'Badges',
    layout: 'dashboard_ms_student',
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolStudentDashboard',
    components: { HelpMessage, Badge },
    data() {
      return {
        helpMessage: {
          title: 'C’est quoi une pépite ?',
          message: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pretium diam ipsum, a efficitur sapien viverra at. Ut porttitor ante tempus tincidunt accumsan. Maecenas sollicitudin, enim tempor sagittis tempus, dui est accumsan velit, laoreet pulvinar lectus purus eget urna. Integer sagittis elit at sapien ultrices, eu rhoncus lectus laoreet.'
        },
        badges: [
          {
            name: "LA POMME",
            description: 'Échanger avec des professionnels est la clé pour comprendre comment les métiers se vivent au jour le jour.',
            illustration: 'appleBadge'
          },
          {
            name: "L'OREILLE",
            description: 'Échanger avec des professionnels est la clé pour comprendre comment les métiers se vivent au jour le jour.',
            illustration: 'earBadge'
          },
          {
            name: "LA POMME",
            description: 'Échanger avec des professionnels est la clé pour comprendre comment les métiers se vivent au jour le jour.',
            illustration: 'lockBadge'
          },
          {
            name: "LA POMME",
            description: 'Échanger avec des professionnels est la clé pour comprendre comment les métiers se vivent au jour le jour.',
            illustration: 'lockBadge'
          },
        ],
        badgesRows: [
          {
            name: "LA POMME",
            description: 'Échanger avec des professionnels est la clé',
            illustration: 'appleBadge'
          },
          {
            name: "LA POMME",
            description: 'Échanger avec des professionnels est la clé',
            illustration: 'appleBadge'
          },
          {
            name: "LA POMME",
            description: 'Échanger avec des professionnels est la clé',
            illustration: 'appleBadge'
          },
          {
            name: "LA POMME",
            description: 'Échanger avec des professionnels est la clé',
            illustration: 'appleBadge'
          },
          {
            name: "LA POMME",
            description: 'Échanger avec des professionnels est la clé',
            illustration: 'appleBadge'
          },
          {
            name: "LA POMME",
            description: 'Échanger avec des professionnels est la clé',
            illustration: 'appleBadge'
          },
        ]
      }
    }
  }
</script>

<style scoped>

</style>

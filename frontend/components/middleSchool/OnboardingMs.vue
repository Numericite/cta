<template>
  <div>
    <div v-if="count === 0" class="flex flex-col justify-start items-center content-center pt-10 px-6 md:px-0">
      <img src="~/static/img/middle-school/illustration-happy.png">
      <div class="text-center flex flex-col text-6xl pt-4"><span class="ms-title pb-2">Bienvenue !</span> C'est parti, à toi de faire tes choix d'orientation !</div>
      <div class="xl:max-w-2/3 text-center pt-10 text-ms-gray-dark ms-subtitle-small leading-normal">
        {{ welcomeText }}
      </div>
      <div class="w-full h-110 sm:h-auto mt-12">
        <video-embed :src="videoIntro"/>
      </div>
    </div>
    <div v-if="count === 1" class="flex flex-col justify-center items-center content-center px-6 md:px-0">
      <img src="~/static/img/middle-school/illustration-happy.png">
      <div class="text-center flex flex-col text-6xl pt-4"><span class="ms-title pb-2 text-ms-blue">Séquence</span> Comment définir ton projet d'orientation</div>
      <div class="flex flex-wrap justify-center w-full pt-10">
        <Entity :entity="entityModule" :row="false" class="pt-20 pb-10"/>
      </div>
      <div class="flex flex-wrap justify-center xl:w-2/3 py-10 px-10 xl:px-0">
        <HelpMessage :helpMessage="helpMessage"/>
      </div>
    </div>
    <div v-if="count === 2" class="flex flex-col justify-center items-center content-center px-6 md:px-0">
      <img src="~/static/img/middle-school/illustration-happy.png">
      <div class="text-center flex flex-col text-6xl pt-4"><span class="ms-title pb-2 text-ms-red">Explorer</span> Comment définir ton projet d'orientation</div>
      <div class="flex flex-wrap justify-center w-full pt-10">
        <Entity :entity="entityExploration" :row="false" class="pt-20 pb-10"/>
      </div>
      <div class="flex flex-wrap justify-center xl:w-2/3 py-10 px-10 xl:px-0">
        <HelpMessage :helpMessage="helpMessage"/>
      </div>
    </div>
    <div v-if="count === 3" class="flex flex-col justify-center items-center content-center px-6 md:px-0">
      <img src="~/static/img/middle-school/illustration-happy.png">
      <div class="text-center flex flex-col text-6xl pt-4"><span class="ms-title pb-2 text-ms-green">Pistes</span> Comment définir ton projet d'orientation</div>
      <div class="flex flex-wrap justify-center w-full pt-10">
        <Entity :entity="entityLead" :row="false" class="pt-20 pb-10"/>
      </div>
      <div class="flex flex-wrap justify-center xl:w-2/3 py-10 px-10 xl:px-0">
        <HelpMessage :helpMessage="helpMessage"/>
      </div>
    </div>
    <div class="w-full relative pin-b mb-4 my-12">
      <button v-if="count < 3" :class="[count === 0 ? 'bg-ms-blue-dark' : count === 1 ? 'bg-ms-blue' : count === 2 ? 'bg-ms-red' : 'bg-ms-blue']" class="button-ms block mb-16 mx-auto" @click="count++">Continuer</button>
      <button v-if="count === 3" class="button-ms bg-ms-green block mb-16 mx-auto" @click="closeOnboarding()">Commencer</button>
      <div class="flex w-full justify-center pb-4">
        <div v-for="(slide, index) in [0,1,2,3]" :key="index" :class="[index === 0 ? 'border-ms-blue-dark' : index === 1 ? 'border-ms-blue' : index === 2 ? 'border-ms-red' : 'border-ms-green']" class="dot-out mx-1 relative cursor-pointer flex justify-center items-center border-solid border-1" @click="count = index">
          <div v-if="count === index" :class="[index === 0 ? 'bg-ms-blue-dark' : index === 1 ? 'bg-ms-blue' : index === 2 ? 'bg-ms-red' : 'bg-ms-green']" class="big-dot absolute"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Entity from './Entity'
  import HelpMessage from './HelpMessage'
  export default {
    name: 'OnboardingMs',
    components: { Entity, HelpMessage },
    data() {
      return {
        count: 0,
        showModal: !this.$store.state.auth.user.config.sawOnboardingMs,
        entityModule: {
          title: 'Séquences',
          name: 'module',
          description: 'Une série d\'activités pour t\'aider à mieux te connaître et connaître le monde qui t\'entoure.',
          illustration: 'step1'
        },
        entityExploration: {
          title: 'Explorations',
          name: 'exploration',
          description: 'Des actions pour échanger, pour t\'informer et pour découvrir des filières de formation et le monde professionnel.',
          illustration: 'step3'
        },
        entityLead: {
          title: 'Pistes',
          name: 'lead',
          description: 'Un outil qui va te permettre de formuler tes choix et de vérifier si ils te correspondent bien.',
          illustration: 'step2'
        },
        description: 'Aliquet nisi ante nunc, nulla malesuada massa cursus id. A mauris semper ante pulvinar vestibulum ac. Eu et urna, netus non adipiscing enim pharetra viverra. Commodo placerat eget pharetra metus eget pellentesque donec.',
        helpMessage: {
          title: 'On est là pour t’aider !',
          message: 'Tu retrouveras ce bloc à différents moments de ton parcours, pour te donner un petit coup de pouce, et ainsi mener à bien les actions que tu devras réaliser.'
        },
        welcomeText: `Bienvenue sur ton espace !
          Cette espace est là pour t´aider à faire tes choix ; pour cela, nous allons te proposer les actions á
          mener tout au long de l´année ; elles sont divisées en 3 grandes parties que tu retrouves ci-dessous.`,
        path: '/dashboard/college/student/parcours',
        videoIntro: 'https://vimeo.com/465415167'
      }
    },
  }
</script>

<style lang="scss">
  .slide-btn {
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .embed-responsive {
    height: 100%;
    iframe {
      width: 100%;
      height: 100%;
    }
  }

</style>

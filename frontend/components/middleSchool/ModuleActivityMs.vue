<template>
  <div class="w-full bg-white p-3 rounded-lg relative">
    <img v-if="displayIllustration" :src="'/img/middle-school/girl-activity.png'" class="absolute pin-r pin-t w-28 girl-activity">
    <div class="w-full border-1 border-solid border-ms-blue rounded-lg p-6">
      <div class="flex items-center w-full justify-start mb-6">
        <div class="dot mr-4 bg-ms-blue"/>
        <div class="ms-subtitle text-ms-blue">Activité</div>
        <div class="dot ml-4 bg-ms-blue"/>
      </div>
      <div class="text-3xl font-bold">{{ moduleActivity.name }}</div>
      <p class=" pt-2 text-left text-xl text-ms-gray-dark leading-loose break-all">
        {{ moduleActivity.description }}
      </p>
      <div class="text-xl uppercase font-bold text-ms-gray-dark mb-6">
        {{ moduleActivity.nb_ressources }} ressources
      </div>
      <ModuleActivityDocument v-if="moduleActivity.path" :document="{
        path: moduleActivity.path,
        title: moduleActivity.name
      }" kind="main-document" class="mb-16"/>
      <ModuleActivityDocument v-for="(document, index) in moduleActivity.documents"
                              :key="document.id"
                              :document="document"
                              :kind="document.file_kind"
                              :class="{'pt-0': index === 0 && !moduleActivity.path}" class="my-16"/>
      <ModuleActivityDocument v-if="moduleActivity.hasForm" :activity="moduleActivity" kind="form" class="mt-16 mb-4"/>
    </div>
  </div>
</template>

<script>
  import ModuleActivityDocument from './ModuleActivityDocument'
  export default {
    name: 'ModuleActivityMs',
    components: { ModuleActivityDocument },
    props: {
      moduleActivity: {
        type: Object,
        default() {
          return {}
        }
      },
      displayIllustration: {
        type: Boolean,
        default: false
      }
    }
  }
</script>

<style lang="scss" scoped>
  .girl-activity {
    transform: translateY(-64%)translateX(-20%);
  }
</style>

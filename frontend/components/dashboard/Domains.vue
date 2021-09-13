<template>
  <div>
    <h2 class="title title--dash pt-16 sm:text-4xl sm:pt-12">{{ title }}</h2>
    <div :class="{'card rounded-lg shadow p-8 mt-12': verbs.length > 0, 'mt-6': verbs.length === 0}" class="w-full">
      <div class="w-full">
        <div v-for="(verb, key) in verbs" :key="key" class="mb-8 w-1/3 inline-block text-center">
          <div class="block">
            <img :src="verb.img_path" :class="{'bg-yellow': key === 0, 'bg-peach': key === 1, 'bg-topaze': key === 2}" class="w-20 h-20 rounded-full p-4">
          </div>
          <div class="align-top mt-2">
            <span :class="{'text-yellow': key === 0, 'text-peach': key === 1, 'text-topaze': key === 2}" class="align-top text-5xl font-bold mb-8">{{ key + 1 }}</span><br>
            <span class="align-top text-blue font-bold leading-normal mt-2">
              J'aime :<br>{{ verb.name }}
            </span>
          </div>
        </div>
      </div>
      <div class="w-full">
        <div v-for="(domain, key) in domains" :key="key" class="card inline-block items-center rounded-full self-start p-3 px-6 mr-3 mt-3 bg-white border-blue-lighter border border-solid pr-16 relative">
          <p class="text-3xl font-semibold text-blue mb-0">{{ domain.name }}</p>
          <input id="t"
                 class="cursor-default"
                 type="checkbox"
                 disabled
                 checked>
          <label for="t"
                 class="cursor-default cursor-custom ml-2 mb-6 ml-3" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>

  export default {
    name: 'Domains',
    props: {
      domains: {
        type: Array,
        default: function() {
          return []
        }
      },
      verbs: {
        type: Array,
        default: function() {
          return []
        }
      },
      title: {
        type: String,
        default: ''
      }
    }
  }
</script>

<style lang="scss" scoped>
.cursor-custom {
  position: absolute !important;
  top: 0.5em;
  transform: translateY(-50%);
  right: 0;

  &:before {
    background-color: #c8d0ff;
  }
}
</style>

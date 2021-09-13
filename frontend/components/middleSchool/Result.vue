<template>
  <div class="flex w-full mb-8">
    <div class="bg-white p-4 rounded-lg shadow-lg w-full">
      <div :class="{
        'border-ms-green': data.kind === 'lead',
        'border-ms-red': data.kind === 'exploration',
        'border-ms-blue': data.kind === 'module'
      }" class="border-1 border-solid rounded-lg p-5">
        <div class="flex justify-start items-center pb-2">
          <div :class="{
            'bg-ms-green' : data.kind === 'lead',
            'bg-ms-red' : data.kind === 'exploration',
            'bg-ms-blue' : data.kind === 'module'
          }" class="w-1 h-1 rounded-full mx-2"/>
          <div :class="{
            'text-ms-green' : data.kind === 'lead',
            'text-ms-red' : data.kind === 'exploration',
            'text-ms-blue' : data.kind === 'module'
          }" class="action ms-subtitle">{{ data.kind === 'lead' ? 'Choix' : data.kind === 'module' ? 'Séquence' : data.kind }}</div>
          <div :class="{
            'bg-ms-green' : data.kind === 'lead',
            'bg-ms-red' : data.kind === 'exploration',
            'bg-ms-blue' : data.kind === 'module'
          }" class="w-1 h-1 rounded-full mx-2"/>
          <div :class="{
            'text-ms-green' : data.kind === 'lead',
            'text-ms-red' : data.kind === 'exploration',
            'text-ms-blue' : data.kind === 'module'
          }" class="ml-auto sm:ml-0 sm:w-full sm:block ms-subtitle">Finalisée le {{ formatDate(data.date) }}</div>
        </div>
        <!--<pre>{{ data }}</pre>-->
        <div class="text-5xl font-bold mt-4">{{ data.entity.name || data.entity.description }}</div>
        <div v-if="data.kind === 'exploration'" class="text-3xl italic mt-4">{{ data.exploration.name }}</div>
        <ChoiceDetails v-if="data.kind === 'lead'" :lead="data.entity" class="mt-8 mb-2"/>
        <div v-if="data.kind === 'module'" class="text-4xl mt-8 -mb-2 underline justify-text">Réponses de ce que je retiens </div>
        <FormulateForm v-if="data.kind === 'exploration' || data.kind === 'module'" :schema="getSchemaFromFields(data.fields)" class="formulate-form-custom formulate-disabled"/>
        <div v-for="child in data.children" :key="child.date">
          <div class="text-4xl mt-12 underline">Réponses à l'activité "{{ child.entity.name }}"</div>
          <div class="text-md text-ms-gray mt-3 -mb-2">Complétée le {{ formatDate(child.date) }}</div>
          <FormulateForm :schema="getSchemaFromFields(child.fields)" class="formulate-form-custom formulate-disabled"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ChoiceDetails from './ChoiceDetails'
  export default {
    name: 'Result',
    components: { ChoiceDetails },
    props: {
      data: {
        type: Object,
        default: () => {
          return  {}
        }
      }
    }
  }
</script>

<style lang="scss">
  .formulate-disabled {
    * {
      pointer-events: none;
    }

    input, textarea {
      margin-top: 5px;
    }

    div[data-type="radio"].formulate-input-group-item:first-of-type, div[data-type="checkbox"].formulate-input-group-item:first-of-type {
      margin-top: 10px;
    }
  }
</style>

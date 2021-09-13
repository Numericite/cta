<template>
  <div class="flex flex-wrap justify-between w-full shadow-md p-4 items-center rounded-lg">
    <div class="text-blue uppercase font-bold">{{ school.nom }}</div>
    <img v-show="!showContent" src="../../../static/icons/arrowdown.png" class="w-5 cursor-pointer" @click="showContent = !showContent">
    <img v-show="showContent" src="../../../static/icons/arrowup.png" class="w-5 cursor-pointer" @click="showContent = !showContent">
    <div v-show="showContent" class="flex flex-col w-full text-blue uppercase">
      <div class="flex flex-row w-full pt-6 justify-between relative items-start">
        <div class="pb-4 flex-wrap flex-col w-1/2">
          <div class="font-bold pb-4">Adresse :</div>
          <div> {{ school.adresse }}</div>
          <div class="py-2"> {{ school.cp }}</div>
          <div> {{ school.ville }}</div>
          <div class="font-bold pt-6 pb-4">Telephone :</div>
          <div v-if="!school.telephone.length" class="italic text-lg"> Non renseigné</div>
          <div v-else> {{ school.telephone }}</div>
          <div class="w-full absolute pin-b pin-l p-4">
            <button v-if="!showDelete" :class="{ 'bg-green': isSelected }" class="bg-orange font-bold text-white py-4 px-6 rounded-full" @mouseover="showDeleteBtn" @click="addSchool()">
              {{ isSelected ? 'Ajouté' : 'Ajouter' }}</button>
            <button v-if="showDelete && isSelected" class="bg-red button remove-btn" @click="deleteFeedback" @mouseleave="showDelete = false">Retirer</button>
          </div>
        </div>
        <div v-if="showContent" id="map-wrap" class="h-74 w-100">
          <no-ssr>
            <l-map :zoom="16" :center="[school._geoloc.lat,school._geoloc.lon]">
              <l-tile-layer url="http://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png"/>
              <l-marker :latLng="[school._geoloc.lat,school._geoloc.lon]"/>
            </l-map>
          </no-ssr>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

  import Button from '../../../mixins/button'
  export default {
    name: 'SchoolInfo',
    components: {
      Button},
    props: {
      school: {
        type: Object,
        default: function() {
          return {}
        }
      }
    },
    data() {
      return {
        showDelete: false,
        isSelected: false,
        showContent: false
      }
    },
    created() {
      this.isSelected = this.school.isSelected
    },
    methods: {
      async addSchool() {
          await this.$api.onisepFeedback.create({
            code_uai: this.school.code_uai,
            school_name: this.school.nom,
            user_id: this.$store.state.auth.user.userID,
            school_content: {information: '', feedback: ''},
            skills_required: {information: '', feedback: ''},
            perspectives: {information: '', feedback: ''}
          })
          this.isSelected = true
          this.showDelete = true
          this.$emit('reloadOnisepFeedback')

      },
      async deleteFeedback() {
          // TODO: DELETE BY CODE_UAI
          await this.$api.onisepFeedback.deleteByCodeUais({code_uais: [this.school.code_uai], user_id: this.$store.state.auth.user.userID})
          this.showDelete = false
          this.isSelected = false
          this.$emit('reloadOnisepFeedback')
      },
      showDeleteBtn() {
        if(!this.isSelected) {
          this.showDelete = false
        }
        else {
          this.showDelete = true
        }
      }
    },
  }
</script>

<style lang="scss" scoped>
  .remove-btn:hover {
    background: red !important;
  }
  .arrow {
    border: solid black;
    border-width: 0 3px 3px 0;
    display: flex;
    padding: 3px;
    color: white;
  }

  .up {
    transform: rotate(-135deg);
    -webkit-transform: rotate(-135deg);
  }

  .down {
    transform: rotate(45deg);
    -webkit-transform: rotate(45deg);
  }
</style>

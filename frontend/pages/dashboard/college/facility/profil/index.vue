<template>
  <div class="pt-24 flex flex-col px-10 xl:px-0">
    <div class="flex flex-row">
      <div>
        <div v-show="!tmpImg" class="w-2/3 text-center">
          <div class="h-48 w-48 relative rounded-full border-4 border-ms-red border-solid flex justify-center items-start">
            <img v-if="user.avatar_path"
                 :src="user.avatar_path"
                 class="w-full rounded-full">
            <!--        <clipper-upload v-model="tmpImg">-->
            <div @click="toggleShowCropper">
              <img src="~/assets/img/pencil.svg" class="bg-ms-red p-3 rounded-full absolute pin-b pin-r cursor-pointer">
            </div>
            <!--        </clipper-upload>-->
          </div>
          <no-ssr>
            <cropper
              v-model="showCropper"
              :value.sync="showCropper"
              :width="300"
              :height="300"
              :params="{
                token: '123456798',
                name: 'avatar'
              }"
              :headers="{
                smail: '*_~'
              }"
              :noSquare="true"
              imgBgc="blue"
              imgFormat="jpg"
              field="img"
              langType="fr"
              @crop-success="cropSuccess"/>
            <img :src="imgDataUrl">
          </no-ssr>
        </div>
        <div v-show="tmpImg" class="w-2/3 sm:w-full mt-2">
          <div class="w-2/3 m-auto">
            <div class="flex items-center justify-between mt-4">
              <div class="flex flex-col">
                <button class="button button--blue" @click="tmpImg = null">Annuler</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="flex flex-col justify-center pl-6">
        <div class="ms-title">{{ $store.state.auth.user.firstName }} {{ $store.state.auth.user.lastName }}</div>
        <div class="pt-5 ms-description text-ms-gray-dark">{{ $store.state.auth.user.school.name }}</div>
      </div>
    </div>
    <div class="flex flex-row">
      <div class="w-2/3">
        <div class="pt-11 flex justify-start items-center">
          <div class="dot mr-3 bg-black"/>
          <div class="ms-subtitle">TON PROFIL</div>
          <div class="dot ml-3 bg-black"/>
        </div>
        <div class="pt-4 flex">
          <FormulateInput v-model="user.firstName" :outerClass="['w-1/2 my-8']" :inputClass="['mt-8 py-5 px-6 focus:border-purple text-ms-gray-dark']" label="Prénom" type="text" placeholder="Prénom..."/>
          <FormulateInput v-model="user.lastName" :outerClass="['w-1/2 pl-6 my-8']" :inputClass="['mt-8 py-5 px-6 focus:border-purple text-ms-gray-dark']" label="Nom" type="text" placeholder="Nom..."/>
        </div>
        <div class="flex">
          <FormulateInput v-model="user.email" :disabled="true" :outerClass="['w-full my-8']" :inputClass="['mt-8 py-5 px-6 focus:border-purple text-ms-gray-dark']" label="Email" type="text"/>
        </div>
        <div class="pt-6 text-right mb-10">
          <button class="button-ms bg-ms-blue-dark" @click="save()">Enregistrer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import utils from '~/assets/js/utils'

export default {
  name: 'CollegeFacilityDashboardProfil',
  layout: 'dashboard_ms_facility',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessDataDashboard',
  data() {
    return {
      user: Object.assign({}, this.$store.state.auth.user),
      tmpImg: null,
      imgDataUrl: null,
      showCropper: false
    }
  },
  methods: {
    toggleShowCropper() {
      this.showCropper = !this.showCropper
    },
    async cropSuccess(imgDataUrl) {
      const file = utils.dataURItoFormData(imgDataUrl, 'avatar_' + this.user.userID + '_' + new Date().getTime() + '.jpg')

      const result = await this.$api.user.uploadAvatar(this.user.userID, file)

      this.$store.commit('auth/setUserAvatar', result.data.res.avatar_path)
      this.user.avatar_path = result.data.res.avatar_path

      this.tmpImg = null
      this.$toast.success('Avatar mis à jour', {
        position: 'bottom-right',
        duration: 3500
      })
    },
    async save() {
      try {
        await this.$api.user.updateUser(this.user)
        this.$store.commit('auth/setUser', this.user)
        this.$toast.success('Informations mises à jour', {
          position: 'bottom-right',
          duration: 3500
        })
        return 'success'
      } catch (e) {
        this.$toast.error(e, {
          position: 'bottom-right',
          duration: 3500
        })
        return e
      }
    }
  }
}
</script>

<style lang="scss">

</style>

<template>
  <div class="profil flexContainer-dash bg-blue-lightest ml-32 sm:ml-0 relative">
    <full-screen-loader :display="avatarLoader" />
    <picture>
      <source srcset="~/assets/img/dash-bg-1.webp"
              type="image/webp">
      <img src="~/assets/img/dash-bg-1.png"
           class="absolute pin-t pin-r z-0 sm:hidden"
           alt="introspection background image">
    </picture>
    <h2 class="title title--dash pt-16">Mon profil</h2>

    <!-- Avatar -->
    <h3 class="text-blue font-bold text-2xl mt-12">Avatar</h3>
    <div v-show="!tmpImg" class="w-2/3 text-center">
      <div class="h-48 w-48 bg-blue relative rounded-full border-8 border-blue-lighter border-solid flex justify-center items-start m-auto">
        <img v-if="user.avatar_path"
             :src="user.avatar_path"
             class="w-full rounded-full">
        <!--        <clipper-upload v-model="tmpImg">-->
        <div @click="toggleShow">
          <img src="~/assets/img/pencil.svg" class="bg-blue-lighter p-3 rounded-full absolute pin-b pin-r cursor-pointer">
        </div>
      <!--        </clipper-upload>-->
      </div>
      <no-ssr>
        <cropper
          v-model="show"
          :value.sync="show"
          :width="300"
          :height="300"
          :params="params"
          :headers="headers"
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

    <!-- Infos persos -->
    <h3 class="text-blue font-bold text-2xl mt-12">Informations personnelles</h3>

    <div class="w-2/3 flex sm:flex-col sm:w-full">
      <!-- firstName -->
      <div :class="{'field--active': user.firstName.length > 0, 'field--error': errors.has('scope2.prénom')}"
           class="field w-1/2 mr-4 sm:w-full">
        <label for="firstName"
               class="pointer-events-none">Prénom*</label>
        <input v-validate="'required'"
               id="firstName"
               v-model="user.firstName"
               data-vv-scope="scope2"
               name="prénom"
               type="text"
               class="w-full">
        <span v-if="errors.has('scope2.prénom')"
              class="error">
          {{ errors.first('scope2.prénom') }}
        </span>
      </div>
      <!-- End firstName -->
      <!-- lastName -->
      <div :class="{'field--active': user.lastName.length > 0, 'field--error': errors.has('scope2.nom')}"
           class="field w-1/2 ml-4 sm:w-full sm:ml-0">
        <label for="lastName"
               class="pointer-events-none">Nom*</label>
        <input v-validate="'required'"
               id="lastName"
               v-model="user.lastName"
               data-vv-scope="scope2"
               name="nom"
               type="text"
               class="w-full">
        <span v-if="errors.has('scope2.nom')"
              class="error">
          {{ errors.first('scope2.nom') }}
        </span>
      </div>
      <!-- End lastName -->
    </div>
    <div class="w-2/3 flex sm:flex-col sm:w-full">
      <!-- mail -->
      <div :class="{'field--active': user.email.length > 0, 'field--error': errors.has('scope2.email')}"
           class="field w-1/2 mr-4 sm:w-full">
        <label for="email"
               class="pointer-events-none">Adresse email*</label>
        <input v-validate="'required|email'"
               id="email"
               v-model="user.email"
               disabled="true"
               data-vv-scope="scope2"
               name="email"
               type="email"
               class="w-full">
        <span v-if="errors.has('scope2.email')"
              class="error">
          {{ errors.first('scope2.email') }}
        </span>
      </div>
      <!-- End mail -->
      <!-- phone -->
      <div :class="{'field--active': user.config.phoneNumber > 0, 'field--error': errors.has('scope2.telephone')}"
           class="field w-1/2 ml-4 sm:w-full sm:ml-0">
        <label for="phone"
               class="pointer-events-none">Numéro de téléphone</label>
        <input v-validate="'required|numeric'"
               id="phone"
               v-model="user.config.phoneNumber"
               data-vv-scope="scope2"
               name="telephone"
               type="tel"
               class="w-full">
        <span v-if="errors.has('scope2.telephone')"
              class="error">
          {{ errors.first('scope2.telephone') }}
        </span>
      </div>
      <!-- End phone -->
    </div>
    <div class="w-2/3 flex sm:flex-col sm:w-full">
      <!-- city -->
      <div :class="{'field--active': ((tempCity && tempCity.length > 0) || (user.config.city)), 'field--error': errors.has('scope2.ville')}"
           class="field w-full sm:w-full">
        <label for="city"
               class="pointer-events-none">Ville*</label>
        <no-ssr>
          <v-autocomplete :items="cities"
                          v-model="user.config.city"
                          :get-label="getCityLabel"
                          :component-item="templateCity"
                          :wait="0"
                          :input-attrs="{autocomplete: 'off', 'class': 'w-full', 'name': 'city', 'type': 'text', 'data-vv-scope': 'scope2', 'id': 'city', 'v-validate': 'required' }"
                          :min-len="0"
                          @update-items="updateCities"
                          @item-selected="updateCityField" />
        </no-ssr>
        <span v-if="cityError"
              class="error">
          Le champ Ville est obligatoire
        </span>
      </div>
      <!-- End city -->
    </div>
    <!-- End Infos persos -->

    <!-- School infos -->
    <h3 v-if="user.config.accountType === 'student'" class="text-blue font-bold text-2xl mt-12">Informations scolaires</h3>
    <div v-if="user.config.accountType === 'student'" class="w-2/3 flex sm:flex-col sm:w-full">
      <!-- school -->
      <div :class="{'field--active': ((tempSchool && tempSchool.name && tempSchool.name.length > 0) || (user.school && user.school.name && user.school.name.length > 0)), 'field--error': schoolError, 'w-1/2': user.school, 'w-full': !user.school}"
           class="field mr-4 sm:w-full">
        <label for="school"
               class="pointer-events-none">Établissement scolaire*</label>
        <no-ssr>
          <v-autocomplete :items="items"
                          v-model="user.school"
                          :get-label="getLabel"
                          :component-item="templateItem"
                          :wait="0"
                          :input-attrs="{autocomplete: 'off', 'class': 'w-full', 'name': 'etablissement', 'type': 'text', 'data-vv-scope': 'scope2', 'id': 'school', 'v-validate': 'required' }"
                          :min-len="0"
                          @input="getClassrooms"
                          @update-items="updateItems"
                          @item-selected="updateSchoolField" />
        </no-ssr>
        <span v-if="schoolError"
              class="error">
          Le champ établissement est obligatoire
        </span>
      </div>
      <!-- End school -->
      <!-- grade -->
      <div v-if="user.school" :class="{'field--active': user.config.classroom_id.length > 0, 'field--error': errors.has('scope2.classe')}"
           class="field w-1/2 ml-4 sm:w-full sm:ml-0">
        <label for="classroom"
               class="pointer-events-none">Classe*</label>
        <select v-validate="'required'"
                id="classroom"
                v-model="user.config.classroom_id"
                data-vv-scope="scope2"
                name="classe"
                class="w-full">
          <option v-for="classroom in classrooms"
                  :key="classroom.id"
                  :value="classroom.id">{{ classroom.name }}</option>
        </select>
        <span v-if="errors.has('scope2.classe')"
              class="error">
          {{ errors.first('scope2.classe') }}
        </span>
      </div>
      <!-- End grade -->
    </div>
    <!-- End School infos -->

    <button class="button button--orange mt-16"
            @click="handleSubmit">Sauvegarder</button>

  </div>
</template>

<script>
import TemplateItem from '~/components/user/register/SchoolItemTemplate'
import TemplateCity from '~/components/user/register/CityItemTemplate'
import heads from '~/config/meta.json'
import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
import AvatarInput from '~/components/user/UploadAvatar.vue'
import FullScreenLoader from '~/components/FullScreenLoader.vue'
import utils from '~/assets/js/utils'
import _ from 'lodash'
import myUpload from 'vue-image-crop-upload/';
// import 'babel-polyfill'; // es6 shim

export default {
  name: 'Profil',
  layout: 'dashboard_student',
  routePerimeter: DashboardPerimeter,
  routePerimeterAction: 'accessAllDashboard',
  components: {
    TemplateItem,
    TemplateCity,
    AvatarInput,
    FullScreenLoader,
    'cropper': myUpload
  },
  head() {
    if (heads[this.$options.name]) return heads[this.$options.name]
    return {
      title: 'Crée ton avenir'
    }
  },
  async asyncData({ app, route, error }) {
    try {
      const response = await app.$api.schools.getSchools()
      return { schoolsList: response.data, items: response.data }
    } catch (err) {
      console.log(err)
    }
  },
  data() {
    return {
      show: false,
      headers: {
        smail: '*_~'
      },
      imgDataUrl: '',
      params: {
        token: '123456798',
        name: 'avatar'
      },
      avatar: null,
      avatarLoader: false,
      saving: false,
      saved: false,
      tmpImg: null,
      item: {
        name: 'Lycée'
      },
      items: null,
      cities: [],
      classrooms: [],
      tempCity: '',
      tempSchool: null,
      isMounted: false,
      schoolError: false,
      cityError: false,
      checkbox: false,
      templateItem: TemplateItem,
      templateCity: TemplateCity,
      user: {
        userID: '',
        sex: '',
        firstName: '',
        lastName: '',
        email: '',
        avatar_path: '/img/profile.webp',
        roles: [],
        active: true,
        config: {
          accountType: 0,
          city: null,
          department: '',
          school_id: null,
          classroom_id: '',
          phoneNumber: ''
        }
      },
      confirmPassword: ''
    }
  },
  watch: {
    avatar: {
      handler: function() {
        this.saved = false
      },
      deep: true
    }
  },
  created() {
    if (this.$store.state.auth.user) {
      const userStore = this.$store.state.auth.user
      this.user = Object.assign(this.user, userStore)
      this.user.school = this.schoolsList.find(function(school) {
        return school.id === userStore.config.school_id
      })
    }
  },
  mounted() {
    this.isMounted = true
  },
  methods: {
    toggleShow() {
      this.show = !this.show;
    },
    async cropSuccess(imgDataUrl, field) {
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
    uploadImage() {
      this.saving = true
      setTimeout(() => this.savedAvatar(), 1000)
    },
    savedAvatar() {
      this.saving = false
      this.saved = true
    },
    getLabel(item) {
      if (item) return item.name
      return ''
    },
    getCityLabel(city) {
      if (city && city.nom) return city.nom
      else if (city) return city
      return ''
    },
    updateItems(text) {
      this.tempSchool = text
      if (text && this.schoolsList) {
        this.items = this.schoolsList.filter(item => {
          return new RegExp(text.toLowerCase()).test(item.name.toLowerCase())
        })
      } else this.items = this.schoolsList
    },
    updateCityField(text) {
      text & text.nom
        ? (this.user.config.city = text.nom + '(' + text.codesPostaux[0] + ')')
        : ''
    },
    updateSchoolField(text) {
      this.tempSchool = text
    },
    async updateCities(text) {
      this.tempCity = text
      let response = await this.$api.geogouv.getCities(text)
      this.cities = response.data.slice(0, 10)
    },
    select: function(item) {
      if (this.user.accountType != item) {
        this.user.accountType = item
      } else {
        this.user.accountType = 0
      }
    },
    async getClassrooms() {
      if (this.user.school) {
        let response = await this.$api.schools.getClassrooms([this.user.school.id])
        this.classrooms = response.data.sort((function(a, b) {
          return (a.grade > b.grade) ? -1 : (a.grade === b.grade) ? ((a.name > b.name) ? 1 : -1) : 1
        }))

        if (!_.includes(_.map(this.classrooms, 'id'), this.user.config.classroom_id)) {
          this.user.config.classroom_id = this.classrooms[0].id
        }
      }
    },
    async handleSubmit() {
      this.user.config.school_id = this.user.school.id
      try {
        const request = await this.$api.user.updateUser(this.user)
        this.$store.commit('auth/setUser', request.data.res)
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
.v-autocomplete {
  width: 100%;
  position: absolute;
  bottom: 0;
}
.v-autocomplete-list {
  width: 100%;
  background-color: white;
  z-index: 10;
  position: absolute;
  top: 0;
  max-height: 230px;
  overflow-y: scroll;
}
.vicp-wrap {
  .vicp-step1 {
    height: 95% !important;
    width: 100% !important;
  }

  .vicp-drop-area {
    height: 95% !important;
    width: 100% !important;
    display: flex !important;
    justify-content: center !important;
    flex-direction: column !important;
  }

  a {
    color: #ff8f5e !important;
  }

/*IE11*/
  input[type=range]::-ms-track {
    background: #ff8f5e !important;
    height: 5px !important;
    /*remove bg colour from the track, we'll use ms-fill-lower and ms-fill-upper instead */
    /*leave room for the larger thumb to overflow with a transparent border */
    border-color: transparent !important;
    border-width: 6px 0 !important;

    /*remove default tick marks*/
    color: #ff8f5e !important;
  }
  input[type=range]::-ms-fill-lower {
    background: #777 !important;
    border-radius: 10px !important;
  }
  input[type=range]::-ms-fill-upper {
    background: #ddd !important;
    border-radius: 10px !important;
  }
  input[type=range]::-ms-thumb {
    border: none !important;
    height: 50px !important;
    width: 50px !important;
    border-radius: 50% !important;
    background: #ff8f5e !important;
  }
  input[type=range]:focus::-ms-fill-lower {
    background: #888 !important;
  }
  input[type=range]:focus::-ms-fill-upper {
    background: #ccc !important;
  }

  /* CHROME / SAFARI */
  input[type=range]::-webkit-slider-runnable-track {
    background: #ff8f5e !important;
    color: #ff8f5e !important;
  }

  input[type=range]::-webkit-slider-thumb {
    color: #ff8f5e !important;
    background: #ff8f5e !important;
  }

  /*FIREFOX*/
  input[type=range]::-moz-range-track {
    background: #ff8f5e !important;
  }

  input[type=range]::-moz-range-thumb {
    background: #ff8f5e !important;
  }
  /*END FIREFOX*/

  .vicp-crop {
    display: flex !important;
    justify-content: space-around !important;
    align-items: center !important;
    justify-items: center !important;
  }

  .vicp-operate {
    bottom: 10px !important;
  }

  .vicp-preview-item-circle {
    width: 150px !important;
    height: 150px !important;

    img {
      width: 150px !important;
      height: 150px !important;
    }
  }
}
.v-autocomplete-list-item {
  @apply cursor-pointer p-4 text-grey border-blue;
  border: 1px solid;
  border-top: 0;
}
</style>

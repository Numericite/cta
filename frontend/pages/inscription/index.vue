<template>
  <div class="register w-full flex flex-col items-center flexContainer mt-12">

    <h1 class="text-blue text-4xl text-center title font-extrabold">
      Votre orientation est à portée de clics…
    </h1>

    <div class="relative w-full flex flex-col items-center">
      <h2 class="text-blue text-2xl mt-8">
        {{ currentTitle }}
      </h2>

      <div class="w-full flex justify-center mt-8">

        <!-- Form -->
        <form-wizard ref="wizard"
                     title=""
                     nextButtonText="Étape suivante"
                     backButtonText="Étape précédente"
                     finishButtonText="Valider"
                     subtitle=""
                     color="#ff8f5e"
                     class="w-3/5 allsm:w-full xl:px-12"
                     @on-complete="handleSubmit">
          <tab-content :before-change="validateStep2"
                       title="Étape 1 : À propos de vous..."
                       icon=" ">
            <div class="px-10 w-full flex flex-col items-center sm:p-0">

              <div class="w-full flex sm:flex-col mt-6">

                <!-- firstName -->
                <div :class="{'field--active': user.firstName && user.firstName.length > 0, 'field--error': errors.has('scope2.prénom')}"
                     class="field w-1/2 mr-4 sm:w-full">
                  <label for="firstname"
                         class="pointer-events-none">Prénom*</label>
                  <input v-validate="'required'"
                         id="firstname"
                         v-model="user.firstName"
                         data-vv-scope="scope2" autoComplete="off"
                         name="prénom"
                         type="text"
                         class="w-full">
                  <span v-if="errors.has('scope2.prénom')"
                        class="error">
                    {{ errors.first('scope2.prénom') }}
                  </span>
                </div>
                <!-- End firstName -->

                <!-- lastname -->
                <div :class="{'field--active': user.lastName && user.lastName.length > 0, 'field--error': errors.has('scope2.nom')}"
                     class="field w-1/2 ml-4 sm:w-full sm:ml-0">
                  <label for="lastname"
                         class="pointer-events-none">Nom*</label>
                  <input v-validate="'required'"
                         id="lastname"
                         v-model="user.lastName"
                         data-vv-scope="scope2" autoComplete="off"
                         name="nom"
                         type="text"
                         class="w-full">
                  <span v-if="errors.has('scope2.nom')"
                        class="error">
                    {{ errors.first('scope2.nom') }}
                  </span>
                </div>
                <!-- End lastname -->

              </div>

              <!-- mail -->
              <div :class="{'field--active': user.email && user.email.length > 0, 'field--error': errors.has('scope2.email')}"
                   class="field w-full">
                <label for="email"
                       class="pointer-events-none">Adresse email*</label>
                <input v-validate="'required|email'"
                       id="email"
                       v-model="user.email"
                       data-vv-scope="scope2" autoComplete="off"
                       name="email"
                       type="email"
                       class="w-full"
                       @input="emailError = null">
                <span v-if="errors.has('scope2.email')"
                      class="error">
                  {{ errors.first('scope2.email') }}
                </span>
              </div>
              <!-- End mail -->

              <div class="w-full flex sm:flex-col">

                <!-- city -->
                <div :class="{'field--active': ((tempCity && tempCity.length > 0) || (user.config.city && user.config.city.nom.length > 0)), 'field--error': errors.has('scope2.ville') || cityError}"
                     class="field w-1/2 mr-4 sm:w-full">
                  <label for="city"
                         class="pointer-events-none">Ville*</label>
                  <no-ssr>
                    <v-autocomplete :items="cities"
                                    :get-label="getCityLabel"
                                    :component-item="templateCity"
                                    :wait="0"
                                    :input-attrs="{autocomplete: 'off', 'class': 'w-full', 'name': 'city', 'type': 'text', 'data-vv-scope': 'scope2', 'id': 'city', 'v-validate': 'required' }"
                                    :min-len="0"
                                    :autoSelectOneItem="false"
                                    v-model="user.config.city"
                                    @update-items="updateCities"
                                    @item-selected="updateCityField" />
                  </no-ssr>
                  <span v-if="cityError"
                        class="error">
                    Merci de sélectionner une ville
                  </span>
                </div>
                <!-- End city -->
                <!-- postal code -->
                <div :class="{'field--active': (user.config.department && user.config.department.length > 0), 'field--error': errors.has('scope2.department') || postalCodeError }" class="field w-1/2 mr-4 sm:w-full">
                  <label for="department"
                         class="pointer-events-none">Code postal*</label>
                  <no-ssr>
                    <v-autocomplete :items="postalCodeList"
                                    :wait="0"
                                    :autoSelectOneItem="true"
                                    :input-attrs="{autocomplete: 'off', 'class': 'w-full', 'name': 'department', 'type': 'text', 'data-vv-scope': 'scope2', 'id': 'department', 'v-validate': 'required', readOnly: true }"
                                    :min-len="0"
                                    v-model="postalCode"
                                    class="postal-code"
                                    @change="updatePostalCodeField()"
                                    @item-clicked="updatePostalCodeField()"
                                    @update-items="updatePostalCodeField()"
                                    @item-selected="updatePostalCodeField()"/>
                  </no-ssr>
                  <span v-if="postalCodeError"
                        class="error">
                    Merci de sélectionner un code postal
                  </span>
                </div>
                <!-- End postal code -->
              </div>

              <!-- school -->
              <div v-if="user.config.accountType !== 'parent'" :class="{'field--active': ((tempSchool && tempSchool.length > 0) || (user.school && user.school.name.length > 0)), 'field--error': schoolError}"
                   class="field w-full sm:w-full">
                <label for="school"
                       class="pointer-events-none">Établissement scolaire*</label>
                <no-ssr>
                  <v-autocomplete :items="schoolItems"
                                  v-model="user.school"
                                  :get-label="getLabel"
                                  :component-item="templateItem"
                                  :wait="0"
                                  :input-attrs="{autocomplete: 'off', 'class': 'w-full', 'name': 'etablissement', 'type': 'text', 'data-vv-scope': 'scope2', 'id': 'school', 'v-validate': 'required' }"
                                  :min-len="0"
                                  @update-items="updateSchoolItems"
                                  @item-selected="updateSchoolField" />
                </no-ssr>
                <span v-if="schoolError"
                      class="error">
                  Merci de sélectionner un établissement
                </span>
              </div>
              <!-- End school -->

              <div class="w-full flex sm:flex-col">
                <!-- classroom -->
                <div v-if="user.config.accountType === 'student'" :class="{'field--active': user.config.classroom_id, 'field--error': errors.has('scope2.classe')}"
                     class="field w-1/2 mr-4 sm:w-full">
                  <label for="classroom"
                         class="pointer-events-none">Classes*</label>
                  <select v-validate="'required'"
                          id="classroom"
                          v-model="user.config.classroom_id"
                          :disabled="noClassroom"
                          data-vv-scope="scope2" autoComplete="off"
                          name="classe"
                          class="w-full">
                    <option v-for="classroom in classrooms"
                            :key="classroom.id"
                            :value="classroom.id">{{ classroom.name }}</option>
                  </select>
                  <span v-if="noClassroom_bis" class="error">
                    Il n'ya pas de classe pour cet établissement pour le moment.
                  </span>
                </div>
                <div v-if="user.config.accountType === 'teacher'" :class="{'field--active': user.config.classrooms && user.config.classrooms.length > 0, 'field--error': classError}"
                     class="field w-1/2 mr-4 sm:w-full"
                     @click="classError = false">
                  <label for="classroom"
                         class="pointer-events-none">Classes*</label>
                  <multiselect v-model="user.config.classrooms"
                               :placeholder="''"
                               :multiple="true"
                               :close-on-select="false"
                               :clear-on-select="true"
                               :searchable="false"
                               :options="classrooms"
                               label="name"
                               track-by="id"
                               class="w-full">
                    <template slot="tag"
                              slot-scope="props">
                      <span class="option__desc">
                        <span class="option__title">{{ props.option.name }}</span>
                      </span>
                    </template>
                    <template slot="option"
                              slot-scope="props">
                      <div class="option__desc">
                        <span class="option__title">{{ props.option.name }}</span>
                      </div>
                    </template>
                    <template slot="noOptions">
                      <div>
                        Selectionnez d'abord une école
                      </div>
                    </template>
                  </multiselect>
                  <span v-if="classError"
                        class="error">
                    Merci de sélectionner au moins une classe
                  </span>
                </div>
                <!-- End classroom -->

                <!-- phone -->
                <div :class="{'field--active': user.config.phoneNumber && user.config.phoneNumber.length > 0, 'field--error': errors.has('scope2.telephone')}"
                     class="field w-1/2 ml-4 sm:w-full sm:ml-0">
                  <label for="phone"
                         class="pointer-events-none">Numéro de téléphone*</label>
                  <input v-validate="'required|numeric'"
                         id="phone"
                         v-model="user.config.phoneNumber"
                         data-vv-scope="scope2" autoComplete="off"
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
            </div>
          </tab-content>
          <tab-content :before-change="validateStep3"
                       title="Étape 2 : Dernière ligne droite !"
                       icon=" ">
            <div class="px-10 w-full flex flex-col items-center sm:p-0"
                 data-vv-scope="scope3">

              <!-- password -->
              <div :class="{'field--active': user.password && user.password.length > 0, 'field--error': errors.has('scope3.password')}"
                   class="field w-full">
                <label for="password"
                       class="pointer-events-none">Mot de passe*</label>
                <input v-validate="'required|password'"
                       id="password"
                       v-model="user.password"
                       data-vv-scope="scope3"
                       name="password"
                       type="password"
                       class="w-full">
                <span v-if="errors.has('scope3.password')"
                      class="error pwd-error">
                  Votre mot de passe doit contenir au moins 8 caractères comprenant au moins une lettre et un nombre.
                </span>
              </div>
              <!-- End password -->

              <!-- confirmPassword -->
              <div :class="{'field--active': confirmPassword && confirmPassword.length > 0, 'field--error': errors.has('scope3.confirmpassword')}"
                   class="field w-full">
                <label for="confirmpassword"
                       class="pointer-events-none">Confirmation de mot de passe*</label>
                <input v-validate="'required|password'"
                       id="confirmpassword"
                       v-model="confirmPassword"
                       data-vv-scope="scope3"
                       name="confirmpassword"
                       type="password"
                       class="w-full">
                <span v-if="errors.has('scope3.confirmpassword')"
                      class="error">
                  Les mots de passe ne correspondent pas
                </span>
              </div>
              <!-- End confirmPassword -->

              <!-- checkbox -->
              <div :class="{'field--error': errors.has('scope3.donnees')}"
                   class="checkbox w-full flex mt-12">
                <input v-validate="'required'"
                       id="checkbox"
                       v-model="checkbox"
                       data-vv-scope="scope3"
                       name="donnees"
                       type="checkbox"
                       class="mr-4">
                <label for="checkbox"
                       class="cursor-pointer">
                  En cochant cette case, vous acceptez que vos données soient utilisées par « Crée ton avenir » afin de vous proposer les meilleurs résultats possibles. Ces données ne seront en aucun cas transmises à un tiers, à une personne de votre entourage, ou à que ce soit.
                </label>
              </div>
              <!-- End checkbox -->

            </div>
          </tab-content>
        </form-wizard>
        <!-- End form -->

      </div>

    </div>

  </div>
</template>

<script>
import TemplateItem from '~/components/user/register/SchoolItemTemplate'
import TemplateCity from '~/components/user/register/CityItemTemplate'
import heads from '~/config/meta.json'
import AvatarInput from '~/components/user/UploadAvatar.vue'
import Multiselect from 'vue-multiselect'
import _ from 'lodash'

export default {
  name: 'Register',
  layout: 'options',
  components: {
    TemplateItem,
    TemplateCity,
    AvatarInput,
    Multiselect
  },
  head() {
    if ( heads[this.$options.name] ) return heads[this.$options.name]
    return {
      title: 'Crée ton avenir'
    }
  },
  async asyncData( { app, route, error } ) {
    try {
      let response = await app.$api.schools.getSchools()
      return { schoolsList: response.data, items: response.data }
    } catch ( err ) {
      console.log( err )
    }
  },
  data() {
    return {
      noClassroom: true,
      noClassroom_bis: false,
      avatar: null,
      saving: false,
      saved: false,
      classrooms: [],
      postalCode: '',
      postalCodeBis: '',
      postalCodeList: [],
      item: {
        name: 'Lycée'
      },
      schoolItems: null,
      cities: [],
      tempCity: '',
      tempSchool: null,
      isMounted: false,
      schoolError: false,
      classError: false,
      postalCodeError: false,
      cityError: false,
      emailError: false,
      checkbox: false,
      templateItem: TemplateItem,
      templateCity: TemplateCity,
      user: {
        email: '',
        password: '',
        firstName: '',
        lastName: '',
        avatar_path: '/img/profile.webp',
        config: {
          city: null,
          department: '',
          school_id: null,
          classroom_id: '',
          classroom_ids: [],
          phoneNumber: '',
          accountType: 'student'
        }
      },
      confirmPassword: ''
    }
  },
  computed: {
    currentTitle: function() {
      if (!this.isMounted) return
      return this.$refs.wizard.tabs[this.$refs.wizard.activeTabIndex].title
    },
  },
  watch: {
    avatar: {
      handler: function() {
        this.saved = false
      },
      deep: true
    }
  },
  mounted() {
    this.isMounted = true
  },
  methods: {
    uploadImage() {
      this.saving = true
      setTimeout( () => this.savedAvatar(), 1000 )
    },
    savedAvatar() {
      this.saving = false
      this.saved = true
    },
    getLabel( item ) {
      if ( item ) return item.name
      return ''
    },
    getCityLabel( city ) {
      if ( city ) return city.nom
      return ''
    },
    getPostalCode(text) {
      this.postalCode = ''
      if(text.codesPostaux.length === 1) {
        this.postalCode = text.codesPostaux[0]
      }
        this.postalCodeList = text.codesPostaux
    },
    updateSchoolItems( text ) {
      this.tempSchool = text
      if ( text && this.schoolsList ) {
        this.schoolItems = this.schoolsList.filter( item => {
          return new RegExp( text.toLowerCase() ).test( item.name.toLowerCase() )
        } )
      } else this.schoolItems = this.schoolsList
    },
    updateCityField( text ) {
      this.user.config.city = text.nom
      this.getPostalCode(text)
      this.cityError = false
    },
    updatePostalCodeField() {
      this.postalCodeBis = this.postalCode
      if(!this.postalCode.length) {
        this.postalCodeError = true
      } else {
        this.postalCodeError = false
      }
    },
    async updateSchoolField( text ) {
      this.tempSchool = text
      let response = await this.$api.schools.getClassrooms( [this.tempSchool.id] )
      this.classrooms = response.data.sort( ( function( a, b ) {
        return ( a.grade > b.grade ) ? -1 : ( a.grade === b.grade ) ? ( ( a.name > b.name ) ? 1 : -1 ) : 1
      } ) )
      this.schoolError = false
      if(!this.classrooms.length) {
      this.noClassroom = true
      this.noClassroom_bis = true
      }
      else {
        this.noClassroom = false
      }
    },
    async updateCities( text ) {
      this.tempCity = text
      let response = await this.$api.geogouv.getCities( text )
      this.cities = response.data || ''
    },
    select: function( item ) {
      if ( this.user.config.accountType != item ) {
        this.user.config.accountType = item
      } else {
        this.user.config.accountType = 0
      }
    },
    validateStep1: function() {
      const user = this.user
      return new Promise( ( resolve, reject ) => {
        if ( user.config.accountType == 0 ) {
          this.$toast.error( 'Erreur : Veuillez sélectionner un profil', {
            position: 'bottom-right',
            duration: 3500
          } )
          reject( 'error during validation' )
        } else {
          resolve( true )
        }
      } )
    },
    validateStep2: function() {
      const user = this.user
      // Check if department is correct
      if (!this.postalCodeBis.length) {
        this.postalCodeError = true
      }
      // else {
      //   this.postalCodeError = false
      // }
      this.user.config.department = this.postalCode
      // Check if school is correct
      this.user.school ? ( this.schoolError = false ) : ( this.schoolError = true )
      if ( !this.schoolError )
        this.user.config.school_id = this.user.school.id
      this.user.config.accountType === 'teacher' && !this.user.config.classroom_ids.length ? ( this.classError = false ) : ( this.classError = false )
      if ( !this.classError && this.user.config.accountType === 'teacher' )
        this.user.config.classroom_ids = this.user.config.classrooms.map( item => item.id )
      if(!this.classrooms.length) {
        this.noClassroom_bis = true
      }
      // Check if city is correct
      this.user.config.city && this.user.config.city.nom
        ? ( this.cityError = false )
        : ( this.cityError = true )
      if ( !this.cityError )
        this.user.department = this.user.config.city.codeDepartement
      this.avatar_path ? ( this.user.avatar_path = this.avatar.imageURL ) : ''
      return new Promise( ( resolve, reject ) => {
        this.$validator.validateAll( 'scope2' ).then( result => {
          if ( this.schoolError || this.cityError || this.classError || this.postalCodeError )
            reject( 'error' )
          if ( result && user.config.school_id ) {
            if ( !this.emailError ) {
              this.emailError = false
              resolve( true )
            } else {
              reject( 'error' )
            }
          } else {
            if ( result && !user.config.school_id ) {
              this.$toast.error(
                'Erreur : Veuillez sélectionner un établissement',
                {
                  position: 'bottom-right',
                  duration: 3500
                }
              )
            } else {
              this.$toast.error(
                'Erreur : Veuillez compléter tous les champs requis',
                {
                  position: 'bottom-right',
                  duration: 3500
                }
              )
            }
            reject( 'error' )
          }
        } )
      } )
    },
    validateStep3: function() {
      const user = this.user
      const confirm = this.confirmPassword
      return new Promise( ( resolve, reject ) => {
        this.$validator.validateAll( 'scope3' ).then( result => {
          if ( result ) {
            if ( user.password == confirm ) {
              resolve( true )
            } else {
              this.$toast.error(
                'Erreur : Les mots de passe ne correspondent pas',
                {
                  position: 'bottom-right',
                  duration: 3500
                }
              )
              resolve( 'error' )
            }
          } else {
            this.$toast.error(
              'Erreur : Veuillez compléter tous les champs requis',
              {
                position: 'bottom-right',
                duration: 3500
              }
            )
            resolve( 'error' )
          }
        } )
      } )
    },
    async handleSubmit() {
      this.user.roles = ['NormalUser']
      if (this.user.config.accountType === 'teacher') { this.user.roles.push('Teacher') }
      if (this.user.config.accountType === 'parent') { this.user.roles.push('Parent') }
      this.user.sex = 'undefined'
      try {
        const request = await this.$api.user.register( this.user )
        this.$router.push( '/inscription/confirm' )
        return 'success'
      } catch ( e ) {
        if ( e.response.status === 409 ) {
          this.$toast.error( 'Cette adresse mail est déjà utilisée', {
            position: 'bottom-right',
            duration: 3500
          } )
          this.$refs.wizard.prevTab()
          this.emailError = this.user.email
        } else {
          this.$toast.error( e, {
            position: 'bottom-right',
            duration: 3500
          } )
        }
        return e
      }
    }
  }
}
</script>



<style lang="scss">
  .postal-code ::placeholder {
      color: #1E0A81 !important;
  }
.addButton {
  transform: translateY(75%);
}
  .single-select-wrapper {
    margin-bottom: 0 !important;
  }
  .single-box div {
    width: 100% !important;
  }
  .search-input {
    border: none !important;
    border-bottom: 1px solid #432cb4 !important;
    border-radius: 0 !important;
    font-size: 13px !important;
    color: #432cb4 !important;
  }
  .wizard-progress-bar {
    @apply rounded-lg;
  }
  .stepTitle {
    display: none;
  }
  .wizard-icon-circle {
    width: 20px !important;
    height: 20px !important;
  }
  .wizard-progress-with-circle {
    top: 15px !important;
  }
  .checked {
    background: #ff8f5e !important;
  }
  .active .wizard-icon-circle {
    background: #fff !important;
  }
  .active .wizard-icon-container {
    background: transparent !important;
  }
  .wizard-btn {
    @apply uppercase;
    border-radius: 500px !important;
    padding: 0.75rem 1.5rem !important;
    font-weight: 700 !important;
  }
  .wizard-card-footer {
    @apply flex flex-col flex-col-reverse justify-center items-center mt-12;
  }
  .wizard-footer-left {
    @apply mt-2;
  }
  .wizard-footer-left button {
    @apply text-sm underline;
    background: none !important;
    color: #a2ace3 !important;
    border: 0 !important;
  }
  .wizard-tab-content {
    padding-left: 0 !important;
    padding-right: 0 !important;
  }
  .item--active img {
    filter: drop-shadow(0 11px 30px rgba(40, 21, 134, 0.3));
  }
  .choice,
  .choice img,
  .choice h3 {
    transition: all 0.3s ease;
  }

  .multiselect__tags {
    .option__desc {
      @apply mx-1;
    }
  }

  .v-autocomplete,
  .multiselect__content-wrapper {
    width: 100%;
    position: absolute;
    bottom: 0;
  }
  .v-autocomplete-list,
  .multiselect__content {
    width: 100%;
    /* background-color: white; */
    z-index: 10;
    position: absolute;
    top: 0;
    max-height: 230px;
    overflow-y: scroll;
  }
  .v-autocomplete-list-item {
    background-color: white;
    @apply cursor-pointer p-4 text-grey border-blue;
    border: 1px solid;
    border-top: 0;

    &.v-autocomplete-item-active {
      background-color: #eee;
      color: #432cb4;
    }

    &:hover {
      background-color: #eee;
      color: #432cb4;
    }
  }

  .multiselect__element {
    \display: block;
    .multiselect__option {
      min-height: 5rem;
      padding: 1rem;
      display: block;
      background-color: white;
      @apply cursor-pointer text-grey border-blue items-center;
      border: 1px solid;
      border-top: 0;
      position: relative;
      &.multiselect__option--selected {
        color: #432cb4;
      }
      &.multiselect__option--highlight {
        background-color: #eee;
        color: #432cb4;
      }
    }
  }
  .pwd-error {
    bottom: -15px !important;
  }
</style>

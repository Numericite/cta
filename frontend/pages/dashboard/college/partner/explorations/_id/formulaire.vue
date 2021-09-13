<template>
  <div class="pt-16 px-5 xl:px-0 xl:pt-32 flex flex-col">

    <CollegeModal v-if="showPreviewForm" :count="2" @handleClose="showPreviewForm = false">
      <FormInterpretor :fields="schema" :fieldLogs="[]" :previewForm="true" class="p-5 m-5" />
    </CollegeModal>

    <CollegeModal v-if="showOptionInput" :count="2" :modalFullWidth="false" @handleClose="showOptionInput = false">
      <h2 class="pt-6 text-5xl text-center">Réglage du champ {{ optionInput.sort_id + 1 }}</h2>
      <div class="flex flex-col items-center formulate-form-custom pt-10">
        <FormulateInput v-model="optionInput.validation"
                        :outerClass="['w-4/5 my-2 ml-4 pt-6']"
                        :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-ms-red text-ms-gray-dark']"
                        type="checkbox" label="Champ obligatoire ?"/>
        <FormulateInput v-if="optionInput.type !== 'radio' && optionInput.type !== 'checkbox' && optionInput.type !== 'date'"
                        v-model="optionInput.placeholder"
                        :outerClass="['w-4/5 my-2 ml-4 pt-6']"
                        :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-ms-red text-ms-gray-dark']"
                        type="text" label="Placeholder *" />
        <div v-if="optionInput.type === 'checkbox' && optionInput.options.length > 1" class="w-4/5 mt-6 relative">
          <FormulateInput v-model="optionInput.validationMin"
                          :outerClass="['my-2 ml-4 no-arrow']"
                          :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-ms-red text-ms-gray-dark']"
                          :options="checkboxSettings(optionInput)" type="select" label="Minimum de case a cocher" />
          <div class="absolute chevron-down flex items-center mt-6 mr-4">
            <svg width="6" height="5" viewBox="0 0 6 5" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path fill="currentColor" d="M0.143658 1.54844C0.0645627 1.47017 0.0645626 1.34239 0.143658 1.26412L0.64883 0.764212C0.726762 0.687092 0.852255 0.687092 0.930187 0.764212L3 2.81244L5.06981 0.764212C5.14775 0.687092 5.27324 0.687092 5.35117 0.764212L5.85634 1.26412C5.93544 1.34239 5.93544 1.47017 5.85634 1.54844L3.14068 4.23579C3.06275 4.31291 2.93725 4.31291 2.85932 4.23579L0.143658 1.54844Z"/>
            </svg>
          </div>
        </div>
      </div>
    </CollegeModal>

    <div class="flex xl:ml-0 pb-6 xl:pb-8 items-center w-full relative">
      <button class="back-btn rounded-full w-8 h-8 bg-ms-red fill-current text-white flex items-center justify-center focus:outline-none" @click="$router.push('/dashboard/college/partner/explorations')">
        <svg width="12" height="9" viewBox="0 0 12 9" fill="none" xmlns="http://www.w3.org/2000/svg" class="fill-current">
          <path d="M5.52344 6.89062L3.97656 5.34375H10.9375C11.2422 5.34375 11.5 5.10938 11.5 4.78125V4.21875C11.5 3.91406 11.2422 3.65625 10.9375 3.65625H3.97656L5.52344 2.13281C5.73438 1.92188 5.73438 1.54688 5.52344 1.33594L5.125 0.9375C4.91406 0.726562 4.53906 0.726562 4.32812 0.9375L1.16406 4.125C0.929688 4.33594 0.929688 4.6875 1.16406 4.89844L4.32812 8.08594C4.53906 8.29688 4.91406 8.29688 5.125 8.08594L5.52344 7.6875C5.73438 7.47656 5.73438 7.10156 5.52344 6.89062Z" fill="white"/>
        </svg>
      </button>
      <div class="flex justify-start items-center ml-3">
        <div class="dot mr-2 bg-ms-red"/>
        <div class="ms-subtitle text-ms-red">Mes explorations</div>
        <div class="dot ml-2 bg-ms-red"/>
      </div>
    </div>

    <div class="text-6xl">Formulaire pour <span class="font-extrabold">"{{ exploration.name }}"</span></div>

    <div class="flex sm:flex-col flex-row pt-8">
      <button class="flex flex-shrink items-center text-white outline-none border-1 border-solid p-5 rounded-lg bg-ms-gray focus:outline-none" @click="addComponent">
        <img src="~/assets/img/middle-school/add-white.svg" class="h-7 mr-3">Ajouter un titre
      </button>
      <button class="flex items-center text-white outline-none border-1 border-solid p-5 rounded-lg bg-ms-gray focus:outline-none sm:ml-0 ml-4" @click="addField">
        <img src="~/assets/img/middle-school/add-white.svg" class="h-7 mr-3">Ajouter un type de réponse
      </button>
      <button class="flex items-center text-white align-middle outline-none border-1 border-solid p-5 rounded-lg bg-ms-red focus:outline-none sm:ml-0 ml-4" @click="submitsubmitHandler">
        <img src="~/assets/img/middle-school/save-black.svg" class="h-7 mr-3">Enregistrer
      </button>
      <button class="flex items-center text-white outline-none border-1 border-solid p-5 rounded-lg bg-ms-blue focus:outline-none sm:ml-0 ml-4" @click="showPreviewForm = true">
        <img src="~/assets/img/white-eye.svg" class="h-7 mr-3">Aperçu
      </button>
    </div>


    <FormulateForm ref="submitFormBuilder" class="flex pt-4">

      <draggable v-model="schema" v-bind="dragOptions" class="w-full pr-3 pt-5" handle=".handle" group="field" @change="updateSortId" >
        <div v-for="(input, index) in schema" :key="input.lastId" class="flex mt-4">

          <div class="flex items-center justify-center borderDrag my-2 mr-2 handle text-xs-left">
            <div class="flex items-center cursor-move">
              <img class="text-gray textOpacity" src="~/assets/img/middle-school/drag_indicator-black.svg">
              <span class="textOpacity pt-1 mr-3">{{ index + 1 }}</span>
            </div>
          </div>

          <div class="w-full flex flex-col borderField p-4">

            <div class="flex justify-between pb-3">

              <FormulateInput v-if="input.type != null" v-model="input.label"
                              :outerClass="['w-2/5 my-2 ml-4']"
                              :elementClass="['pt-2']"
                              :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-ms-red text-ms-gray-dark']"
                              :errorsClass="['mt-2']" :errorClass="['text-red']"
                              label="Intitulé de la question *" type="text" validation="required"/>
              <FormulateInput v-if="input.component != null" v-model="input.children"
                              :outerClass="['w-3/5 my-2 ml-4']"
                              :elementClass="['pt-2']"
                              :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-ms-red text-ms-gray-dark']"
                              :errorsClass="['mt-2']" :errorClass="['text-red']"
                              label="Intitulé du titre *" type="text" validation="required"/>

              <div v-if="input.type != null" class="w-2/5 h-full relative">
                <FormulateInput v-model="input.type"
                                :outerClass="['my-2 ml-4 no-arrow']"
                                :elementClass="['mt-2 relative']"
                                :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-ms-red text-ms-gray-dark']"
                                :errorsClass="['mt-2']" :errorClass="['text-red']"
                                :options="inputTypes" label="Type de réponse" type="select" validation="required" 
                                @change="changeField(input, index)">
                  <template #suffix="">
                    <div class="absolute chevron-down flex items-center mr-4">
                      <svg width="6" height="5" viewBox="0 0 6 5" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path fill="currentColor" d="M0.143658 1.54844C0.0645627 1.47017 0.0645626 1.34239 0.143658 1.26412L0.64883 0.764212C0.726762 0.687092 0.852255 0.687092 0.930187 0.764212L3 2.81244L5.06981 0.764212C5.14775 0.687092 5.27324 0.687092 5.35117 0.764212L5.85634 1.26412C5.93544 1.34239 5.93544 1.47017 5.85634 1.54844L3.14068 4.23579C3.06275 4.31291 2.93725 4.31291 2.85932 4.23579L0.143658 1.54844Z"/>
                      </svg>
                    </div>
                  </template>            
                </FormulateInput>
              </div>

              <!-- <div v-if="input.component != null" class="w-2/5 relative">
                <FormulateInput v-model="input.component"
                                :outerClass="['my-2 ml-4 no-arrow']"
                                :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-ms-red text-ms-gray-dark']"
                                :errorsClass="['mt-2']" :errorClass="['text-red']"
                                :options="componentTypes" label="Composants possible" type="select" validation="required" @change="changeField(input, index)"/>
                <div class="absolute chevron-down flex items-center mt-6 mr-4">
                  <svg width="6" height="5" viewBox="0 0 6 5" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path fill="currentColor" d="M0.143658 1.54844C0.0645627 1.47017 0.0645626 1.34239 0.143658 1.26412L0.64883 0.764212C0.726762 0.687092 0.852255 0.687092 0.930187 0.764212L3 2.81244L5.06981 0.764212C5.14775 0.687092 5.27324 0.687092 5.35117 0.764212L5.85634 1.26412C5.93544 1.34239 5.93544 1.47017 5.85634 1.54844L3.14068 4.23579C3.06275 4.31291 2.93725 4.31291 2.85932 4.23579L0.143658 1.54844Z"/>
                  </svg>
                </div>
              </div> -->

              <div class="w-1/5 flex justify-center pt-11 mt-1 height-fit">
                <button v-if="input.type != null" class="outline-none rounded-lg focus:outline-none" @click="startDialogSettings(input)">
                  <img src="~/assets/img/middle-school/settings-black.svg" class="h-7">
                </button>
                <button class="outline-none rounded-lg ml-4 focus:outline-none" @click="deleteField(index)">
                  <img src="~/assets/img/middle-school/close-black.svg" class="h-7">
                </button>
              </div>
            </div>

            <div v-for="(option, indexOption) in input.options" :key="option.value" class="flex py-5 pl-3">

              <div class="pt-11 mt-2 px-3">
                <span>{{ `${(input.type === 'checkbox' ? 'Choix multiple ' : 'Choix unique ') + (indexOption + 1)} : ` }}</span>
              </div>

              <div class="w-2/5 pr-4">
                <FormulateInput v-model="option.label"
                                :outerClass="['my-2 ml-4']"
                                :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-ms-red text-ms-gray-dark']"
                                :errorsClass="['mt-2']" :errorClass="['text-red']"
                                label="Intitulé du choix *" type="text" validation="required"/>
              </div>

              <div class="pt-11">
                <button class="outline-none rounded-lg ml-4 focus:outline-none" @click="deleteOption(input, indexOption)">
                  <img src="~/assets/img/middle-school/close-black.svg" class="h-7">
                </button>
              </div>

            </div>

            <div v-if="input.options != null && input.options.length" class="flex py-5 pl-3">

              <div class="pt-11 mt-2 px-3">
                <span class="textOpacity">{{ `${(input.type === 'checkbox' ? 'Choix multiple ' : 'Choix unique ') + (input.options.length + 1)} : ` }}</span>
              </div>

              <div class="w-2/5 pr-4">
                <FormulateInput :outerClass="['my-2 ml-4']"
                                :inputClass="['py-3 px-4 border-1 border-solid rounded-lg outline-none focus:border-ms-red text-ms-gray-dark']"
                                label="Intitulé du choix *"
                                class="textOpacity" disabled />
              </div>

              <div class="pt-11">
                <button class="outline-none rounded-lg ml-4 focus:outline-none" @click="createOption(index)">
                  <img src="~/assets/img/middle-school/add-black.svg" style="fill: red;" class="h-7">
                </button>
              </div>

            </div>

          </div>

        </div>
      </draggable>

      <div/>

    </FormulateForm>

  </div>
</template>

<script>

  import _ from 'lodash'
  import DashboardPerimeter from '~/authorizations/DashboardPerimeter'
  import draggable from 'vuedraggable'
  import Button from '../../../../../../mixins/button'
  import CollegeModal from '../../../../../../components/middleSchool/CollegeModal'
  import FormInterpretor from '../../../../../../components/middleSchool/FormInterpretor'

  export default {
    layout: 'dashboard_ms_partner',
    name: 'DashboardPartnerFormulaire',
    components: { Button, draggable, CollegeModal, FormInterpretor },
    $_veeValidate: {
      validator: 'new'
    },
    routePerimeter: DashboardPerimeter,
    routePerimeterAction: 'accessMiddleSchoolPartnerDashboard',
    async asyncData({ app, params }) {
      try {
        let response = await app.$api.explorationsType.getExplorationTypeByIds([params.id])
        const exploration = response.data[0] || {}

        response = await app.$api.fields.getList({
          page: 1,
          numberPerPage: 100,
          parent_id: params.id
        })
        const fields = response.data || []

        return { exploration, fields }
      } catch (e) {
        console.log(e)
      }
    },
    data() {
      return {
        showPreviewForm: false,
        showOptionInput: false,
        optionInput: {},
        loading: true,
        lastId: 0,
        lastOptionId: 0,
        valid: false,
        requiredRule: [
          v => !!v || ''
        ],
        inputTypes: [
          { value: 'text', label: 'Réponse texte'},
          { value: 'textarea', label: 'Réponse texte long'},
          { value: 'number', label: 'Réponse nombre'},
          { value: 'date', label: 'Réponse date'},
          { value: 'checkbox', label: 'Réponse choix multiple'},
          { value: 'radio', label: 'Réponse choix unique'},
        ],
        componentTypes: [
          { value: 'h3', label: 'Titre'}
        ],
        schema: [],
        schemaCopy: [],
      }
    },
    computed: {
      dragOptions() {
        return {
          animation: 250,
          group: "description",
          disabled: false,
          ghostClass: "ghost"
        };
      }
    },
    mounted() {
      this.getFields().then((data) => {
        this.bindData(data)
      })
    },
    methods: {

      // Field CRUD

      bindData(data) {

        data.items.sort((a, b) => a.sort_id - b.sort_id)

        data.items.map(item => {

          item.lastId = this.lastId++

          if (item.kind != null) {
            item.type = item.kind
            item.validation != null ? '' : item.validation = false
          }

          item.disabled = true
          item.type === 'checkbox' ? item.validationMin ? '' : item.validationMin = 1 : delete item.validationMin

          if (item.optionsField && item.optionsField.length) {
            item.options = item.optionsField.map(option => {return { label: option.label, id: option.id, value: option.value}})
          } else {
            item.options = []
          }
        })

        this.schema = data.items.length ? data.items : []
        this.schemaCopy = JSON.parse(JSON.stringify(data.items || []))
        this.loading = false
      },

      async getFields() {
        this.loading = true;
        return new Promise(async (resolve, reject) => {
          try {

            let request = await this.$api.fields.getList({
              page: 1,
              numberPerPage: 100,
              parent_id: this.$route.params.id
            })

            const items = request.data || []

            resolve({
              items
            })
          } catch (e) {
            reject(e)
          }
        })
      },

      async submitHandler () {

        const hasErrors = await this.$refs['submitFormBuilder'].hasValidationErrors()

        if (!hasErrors) {

          this.schema.map( async field => {

            if (field.options.length) {
              field.optionsField = field.options.map(option => option)
            } else {
              delete field.optionsField
            }

            field.parent_id = this.$route.params.id

            if (field.type != null)
              field.kind = field.type

            field.type === 'checkbox' && field.validationMin !== 1 ? '' : delete field.validationMin

            if ((field.type === 'radio' || field.type === 'checkbox') && field.optionsField.length > 0) {
              field.optionsField = field.optionsField.map((option) => {
                return {
                  id: option.id,
                  value: option.value ? option.value : Math.random().toString(),
                  label: option.label
                }
              })
            }

            if (field.id) {
              if (!_.isEqual(field, _.find(this.schemaCopy, { id: field.id }))) {
                //EDIT
                try {
                  await this.$api.fields.update(field)
                  this.getFields().then(this.bindData)
                } catch (e) {

                }
              }
            } else {
              //CREATE
              try {
                await this.$api.fields.create(field)
                this.getFields().then(this.bindData)
              } catch (e) {
                console.log(e.response)
              }
            }
          })

          let fieldIdsToRemove = _.difference(this.schemaCopy.map(item => item.id), this.schema.map(item => item.id))

          if (fieldIdsToRemove.length > 0) {
            await this.$api.fields.drop(fieldIdsToRemove)
            this.getFields().then(this.bindData)
          }

          this.$toast.success("Formulaire enregistré", {
            position: 'bottom-right',
            duration: 3500
          })

        } else {
          this.$refs['submitFormBuilder'].showErrors()
        }
      },

      // Methods component Form Generator

      updateSortId() {
        this.schema.map( (item, index) => item.sort_id = index)
      },

      addComponent() {
        this.schema.push({ component: 'h3', children: '', lastId: this.lastId++, options: [], sort_id: this.schema.length })
      },

      addField() {
        this.schema.push({ type: 'text', label: '', lastId: this.lastId++, options: [], disabled: true, sort_id: this.schema.length })
      },

      changeField(input, index) {
        if (input.type === 'radio' || input.type === 'checkbox') {
          input.type === 'checkbox' ? input.validationMin = 1 : delete input.validationMin
          if (!input.options.length)
            this.createOption(index)
        } else {
          input.options = []
          delete input.validationMin
        }
      },

      deleteField(indexField) {
        this.schema.splice(indexField, 1)
        this.updateSortId()
      },

      createOption(index) {
        this.schema[index].options.push({label: '', id: this.lastId++})
      },

      deleteOption(input, indexOption) {
        if (input.options.length !== 1)
          input.options.splice(indexOption, 1)
      },

      startDialogSettings(input) {
        this.showOptionInput = true
        this.optionInput = input
      },

      checkboxSettings(input) {

        return Array(input.options.length - 1).fill().map((_, i) => {
          return { label: (i + 1).toString(), value: (i + 1).toString() }
        });

      }

    }

  }

</script>

<style lang="scss">

.borderField {
  border: solid 2px grey !important;
  border-radius: 5px;
  background-color: #fff;
}

.ghost {
  opacity: 0.4;
  background: #d79d84;
  border-radius: 5px;
}

.borderDrag {
  border-right: 2px solid rgba(158, 158, 158, .3);
}

.textOpacity {
  opacity: 0.5;
}

.v-btn:hover:before, .v-btn:focus:before {
  background-color: transparent !important;
}


// Vue Formulate Css

.v-label, .formulate-input-element-decorator {
  margin-bottom: 0;

  &::before {
    margin-top: 0 !important;
  }
}

h3 {
  margin-top: 0;
}


.height-fit {
  height: fit-content;
}


</style>

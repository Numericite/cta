<template>
  <div class="w-3/4 h-auto mx-auto pb-12 px-12 flex flex-col justify-center text-center">
    <FormulateForm ref="explorationForm" class="formulate-form-custom">
      <h1 v-if="exploration.id"
          class="mt-16 ms-title text-center">Modification de l'exploration</h1>
      <h1 v-else
          class="mt-16 ms-title text-center">Création d'une exploration</h1>
      <div class="flex flex-col">
        <div class="pt-12 flex justify-start items-center">
          <div class="dot mr-4 bg-black" />
          <div class="ms-subtitle text-xl">DÉFINITION</div>
          <div class="dot ml-4 bg-black" />
        </div>

        <FormulateInput v-model="exploration.name"
                        :outerClass="['my-8 text-left']"
                        :inputClass="['mt-4 py-5 px-6 focus:border-purple text-ms-gray-dark']"
                        :validationMessages="{
                          required: 'Le nom de l\'exploration est obligatoire'
                        }"
                        validation="required"
                        label="Titre de l'atelier"
                        name="name"
                        type="text"
                        placeholder="Titre" />
        <FormulateInput v-model="exploration.description"
                        :outerClass="['my-8 text-left']"
                        :inputClass="['h-36 mt-4 py-5 px-6 focus:border-purple text-ms-gray-dark']"
                        :validationMessages="{
                          required: 'La description de l\'exploration est obligatoire'
                        }"
                        validation="required"
                        label="Description de l'atelier concerné"
                        name="description"
                        type="textarea"
                        placeholder="Description" />
        <FormulateInput v-model="exploration.status_name"
                        :outerClass="['w-full text-left']"
                        :inputClass="['mt-4 py-5 px-6 focus:border-ms-red text-ms-gray-dark']"
                        :options="createOptionsArray(exploration_status.items)"
                        :validationMessages="{
                          required: 'Le statut de l\'exploration est obligatoire'
                        }"
                        validation="required"
                        label="Statut de l'atelier"
                        placeholder="Statut"
                        name="association_status"
                        type="select"/>
      </div>

      <div class="flex flex-col pt-6">
        <div class="pt-12 flex justify-start items-center ">
          <div class="dot mr-4 bg-black" />
          <div class="ms-subtitle text-xl">ASSOCIATION</div>
          <div class="dot ml-4 bg-black" />
        </div>
        <div v-if="!associationCreateMode.on" class="flex flex-col pt-8">
          <FormulateInput v-model="exploration.association_id"
                          :outerClass="['w-full text-left']"
                          :inputClass="['mt-4 py-5 px-6 focus:border-ms-red']"
                          :options="createOptionsArray(associations.items, 'Aucune association')"
                          label="Sélectionnez l'association référente"
                          name="association_id"
                          type="select"/>
        </div>
        <div v-else class="flex pt-8">
          <FormulateInput ref="association_name"
                          v-model="association.name"
                          :outerClass="['w-2/3 text-left']"
                          :inputClass="['mt-4 py-5 px-6 focus:border-purple text-ms-gray-dark']"
                          :validationMessages="{
                            required: 'Le nom de l\'association est obligatoire'
                          }"
                          validation="required"
                          label="Nom"
                          name="name"
                          type="text"
                          placeholder="Nom de l'association" />

          <div v-if="!association.file" class="ml-8 pt-11 mt-1">
            <button type="file"
                    class="text-white ml-auto outline-none border-1 border-solid p-5 rounded-lg bg-blue focus:outline-none"
                    @click.prevent="$refs['file_input_association_logo'].$el.querySelector('input').click()">
              <img src="~/assets/img/FileExploration.svg"
                   class="h-3 w-4 font-extrabold">
              Logo de l'association
            </button>
            <FormulateInput :id="'file_input_association_logo'" :ref="'file_input_association_logo'"
                            :fileNameClass="['file_name_hidden']"
                            :validationMessages="{
                              required: 'Le logo est obligatoire'
                            }"
                            :validationName="'association_logo'" :validation="'required'" type="file" errorBehavior="submit"
                            hidden @change="handleAssociationLogo($event, association)" />

          </div>
          <div v-else
               :class="{'mb-6': $refs['association_name'] && $refs['association_name'].hasVisibleErrors }"
               class="text-left pl-8 relative">
            <img :src="association.imagePath" class="w-auto max-h-50 border border-solid border-ms-gray rounded-lg shadow-md">
            <img src="~/assets/img/times.svg" class="rounded-full border-solid border-ms-gray shadow-md absolute pin-l pin-t ml-6 -mt-2 w-6 p-2 bg-white cursor-pointer"
                 @click="removeAssociationLogo(association)">
          </div>
        </div>
        <div class="pt-12">
          <button v-if="!associationCreateMode.on" class="bg-blue px-12 py-6 text-2xl text-white rounded-xl shadow-sm font-bold focus:outline-none"
                  @click.prevent="associationCreateMode.on = true">
            Créer une association
          </button>
          <button v-else class="bg-blue px-12 py-6 text-2xl text-white rounded-xl shadow-sm font-bold focus:outline-none"
                  @click.prevent="associationCreateMode.on = false">
            Sélectionner une association existante
          </button>
        </div>
      </div>

      <div class="flex flex-col pt-6">
        <div class="pt-12 flex justify-start items-center ">
          <div class="dot mr-4 bg-black" />
          <div class="ms-subtitle text-xl">CATEGORIE</div>
          <div class="dot ml-4 bg-black" />
        </div>

        <div class="pt-8">
          <FormulateInput v-model="exploration.category"
                          :outerClass="['w-full text-left']"
                          :inputClass="['mt-4 py-5 px-6 focus:border-ms-red']"
                          :options="createOptionsArray(categories.items, 'Aucune catégorie')"
                          label="Sélectionnez une catégorie"
                          name="category"
                          type="select"/>
        </div>


      </div>

      <div class="flex flex-col pt-6">
        <div class="pt-12 flex justify-start items-center">
          <div class="dot mr-4 bg-black" />
          <div class="ms-subtitle text-xl">RESSOURCES</div>
          <div class="dot ml-4 bg-black" />
        </div>

        <ExplorationDocumentsForm ref="explorationDocumentForm" :documents="documents.items"/>
      </div>

      <div class="flex flex-col pt-6">
        <div class="pt-12 flex items-center">
          <div class="dot mr-4 bg-black" />
          <div class="ms-subtitle text-xl">RESTRICTIONS</div>
          <div class="dot ml-4 bg-black" />
        </div>

        <div class="text-4xl self-start pt-8 font-bold">Explication d'une restriction</div>
        <div class="pt-2 ms-subtitle-small text-justify text-ms-gray-dark">
          Les restrictions déterminent quels jeunes sont concernés par les explorations.
          Il y a trois échelons : le collège, le niveau de classe et la classe.
          Vous pouvez en ajouter autant que nécessaire par exploration.
          Par exemple il y aura autant de restrictions que de collèges concernés par les ateliers de S'orienter Ensemble.
        </div>

        <div v-for="restriction in restrictions.items"
             :key="restriction.num"
             class="flex flex-col pt-8">

          <div class="flex items-end">
            <FormulateInput v-model="restriction.school_id"
                            :outerClass="['w-4/5 text-left']"
                            :inputClass="['mt-4 py-5 px-6 focus:border-ms-red']"
                            :options="createOptionsArray(schools, 'Tous les établissements')"
                            :label="'Restriction ' + restriction.num"
                            name="school_id"
                            type="select"
                            @change="changeRestrictionFilter(restriction, 'school', props, app)" />

            <button class="ml-auto p-4 border-1 border-solid px-6 py-5 text-white rounded-lg2 bg-ms-red-crisp focus:outline-none"
                    @click="deleteRestriction(restriction)">
              <img src="~/assets/img/TrashExploration.svg"
                   class="h-4 w-5 font-extrabold ">
            </button>
          </div>

          <div class="flex pt-4">
            <FormulateInput v-model="restriction.grade"
                            :outerClass="['w-2/5 pr-8 text-left']"
                            :inputClass="['mt-4 py-5 px-6 focus:border-ms-red']"
                            :options="createOptionsArray( restriction.grades.items.length ? restriction.grades.items : gradesAll, 'Tous les niveaux')"
                            name="grade"
                            type="select"
                            @change="changeRestrictionFilter(restriction, 'grade', props, app)" />

            <FormulateInput v-model="restriction.classroom_id"
                            :outerClass="['w-2/5 text-left']"
                            :inputClass="['mt-4 py-5 px-6 focus:border-ms-red']"
                            :options="createOptionsArray(restriction.classes.items, 'Toutes les classes')"
                            :disabled="!(restriction.grade !== '' || restriction.school_id !== 'all')"
                            name="classroom_id"
                            type="select" />
          </div>

        </div>

        <div class="flex justify-center pt-12">
          <button class="bg-blue px-12 py-6 text-2xl text-white rounded-xl shadow-sm font-bold focus:outline-none"
                  @click="createRestriction(numRestriction++)">
            Ajouter une restriction
          </button>
        </div>
      </div>

      <div class="flex justify-center pt-12">
        <button class="bg-ms-orange px-12 py-6 mr-16 rounded-xl text-white text-2xl shadow-sm font-bold focus:outline-none"
                @click="handleClose">Annuler</button>

        <FormulateInput v-if="exploration.id"
                        :outerClass="['bg-blue rounded-xl shadow-sm font-bold focus:outline-none']"
                        :inputClass="['bg-blue px-12 py-6 text-white text-2xl font-bold border-none shadow-none focus:outline-none']"
                        type="submit"
                        name="Modifier l'exploration"
                        @click="validateForm(exploration)" />

        <FormulateInput v-else
                        :outerClass="['bg-blue rounded-xl shadow-sm font-bold focus:outline-none']"
                        :inputClass="['bg-blue px-12 py-6 text-white text-2xl font-bold border-none shadow-none focus:outline-none']"
                        type="submit"
                        name="Créer l'exploration"
                        @click="validateForm(exploration)" />
      </div>
    </FormulateForm>
  </div>
</template>

<script>
import RestrictionForm from "../RestrictionForm";
import ExplorationDocumentsForm from "../ExplorationDocumentsForm"
import { useContext, onMounted, reactive, ref, watch, isReactive } from '@nuxtjs/composition-api'
import { useChangeFilter, useOptionsArray } from '../../../composition/FilterPartner'
import { useRestrictions, useCreateExploration } from '../../../composition/ExplorationModal'

export default {
  components: { RestrictionForm, ExplorationDocumentsForm },
  props: {
    explorationType: {
      type: Object,
      default: function () {
        return {}
      }
    },
    schools: {
      type: Array,
      default: function () {
        return []
      }
    },
    gradesAll: {
      type: Array,
      default: function () {
        return []
      }
    },
    partner: {
      type: Object,
      default: function () {
        return {}
      }
    }
  },
  setup( props, context ) {

    const { app } = useContext();

    const associationCreateMode = reactive({on: false})

    const deletedRestrictions = reactive({
      ids: []
    })

    const deletedDocuments = reactive({
      ids: []
    })

    const associations = reactive({
      items: []
    })

    const categories = {
      items: [
        {
          name: 'Savoir-faire',
          id: 'sf',
        },
        {
          name: 'Savoir-être',
          id: 'se'
        }
      ]
    }

    const exploration_status = {
       items: [
        {
          name: 'Publié',
          id: 'open',
        },
        {
          name: 'Brouillon',
          id: 'standby'
        }
      ]
    }

    const association = reactive({
      name: '',
      partner_slug: props.partner.slug
    })

    const exploration = reactive(
      Object.assign({
        name: '',
        description: '',
        association_id: '',
        association_name: '',
        status_name: '',
        category: '',
        partner_slug: props.partner.slug,
        user_id: app.store.state.auth.user.userID
      }, props.explorationType)
    )

    const restrictions = reactive( {
      items: [{
        num: 1,
        school_id: '',
        grade: '',
        classroom_id: '',
        grades: { items: [] },
        classes: { items: [] }
      }]
    } )

    const documents = reactive({
      items: [{
        num: 1,
        file_kind: 'document',
        title: '',
        path: '',
      }]
    })

    let numRestriction = 2

    const useRestrictionTmp = useRestrictions( restrictions )

    const createExplorationType = async ( explorationType ) => {
      await useCreateExploration( explorationType, restrictions, documents, associationCreateMode, association, app, deletedRestrictions, deletedDocuments )
      handleClose()
    }

    const getRestrictions = async () => {
      restrictions.items = []
      numRestriction = 1

      if ( exploration.id ) {
        const response = await app.$api.restrictions.getRestrictionList( {
          exploration_type_id: exploration.id
        } )
        response.data.forEach( ( restriction ) => {
          const new_restriction = useRestrictionTmp.createRestriction( numRestriction++, restriction )
          useRestrictionTmp.changeRestrictionFilter( new_restriction, 'school', props, app, false )
        } )
      } else {
        useRestrictionTmp.createRestriction( numRestriction++ )
      }
    }

    const getDocuments = async () => {

      if ( exploration.id ) {
        const response = await app.$api.documents.getList( {
          page: 1,
          numberPerPage: 100,
          parent_type: 'exploration',
          parent_ids: [exploration.id]
        } )
        documents.items = (response.data || []).map((document) => {
          if (document.hasOwnProperty('short_description')) {
            document.file = {
              id: document.id,
              name: document.description
            }
          }
          return document
        }).sort( (a, b) => a.num - b.num)
      } else {
        documents.items = [
          {
            file_kind: 'document',
            num: 1,
            title: '',
            path: '',
          }
        ]
      }
    }

    const getAssociations = async () => {
      const response = await app.$api.associations.getList({
        page: 1,
        numberPerPage: 1000,
        partner_slug: props.partner.slug
      })

      associations.items = response.data || []
    }

    const deleteRestriction = ( restriction ) => {
      if ( restriction.id ) {
        deletedRestrictions.ids.push( restriction.id )
      }
      useRestrictionTmp.removeRestriction( restriction.num )
    }

    const handleClose = () => {
      context.emit( 'closeModal' )
    }

    onMounted(() => {
      getRestrictions()
      getDocuments()
      getAssociations()
    })

    return {
      ...useOptionsArray(),
      ...useRestrictions(restrictions),
      handleClose,
      deleteRestriction,
      createExplorationType,
      restrictions,
      documents,
      associations,
      categories,
      exploration_status,
      numRestriction,
      exploration,
      associationCreateMode,
      association,
      deletedRestrictions,
      deletedDocuments,
      app,
      props
    }

  },
  methods: {
    async validateForm(exploration) {
      const hasErrors = await this.$refs['explorationForm'].hasValidationErrors()

      if (!hasErrors) {
        this.documents.items = this.$refs['explorationDocumentForm'].getDocuments()
        this.deletedDocuments.ids = this.$refs['explorationDocumentForm'].getDeletedDocuments()

        await this.createExplorationType(exploration)
      } else {
        const firstError = document.querySelector("*[data-has-errors=\"true\"] .formulate-input-element > *")
        firstError.focus()
      }
    },
    handleAssociationLogo(e, association) {
        const files = e.target.files || e.dataTransfer.files;
        association.file = files[0]
        association.imagePath = URL.createObjectURL(association.file)
        this.$forceUpdate()
    },
    removeAssociationLogo(association) {
      delete association.file
      delete association.img_path
      this.$forceUpdate()
    }
  }
}
</script>

<style lang="scss" scoped>
</style>


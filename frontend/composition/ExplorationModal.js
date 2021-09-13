import { reactive, isReactive } from '@nuxtjs/composition-api'
import { useClassrooms } from './FilterPartner'

export const useRestrictions = ( restrictions ) => {

  const createRestriction = ( numRestriction, restriction ) => {

    const new_restriction = {
      num: numRestriction,
      school_id: restriction ? restriction.school_id : '',
      grade: restriction ? restriction.grade : '',
      classroom_id: restriction ? restriction.classroom_id : '',
      grades: { items: [] },
      classes: { items: [] }
    }

    if ( restriction ) {
      new_restriction.id = restriction.id
    }

    restrictions.items.push( new_restriction )

    return new_restriction
  }

  const removeRestriction = ( numRestriction ) => {

    if ( restrictions.items.length !== 1 ) {
      restrictions.items = restrictions.items.filter( item => item.num !== numRestriction )
    }

  }

  const changeRestrictionFilter = async ( restriction, type, props, app, clear = true ) => {

    if ( clear )
      restriction.classroom_id = ''

    if ( type === 'school' ) {

      if ( clear )
        restriction.grade = ''

      if ( restriction.school_id ) {

        try {
          let response = await app.$api.schools.getGradesBySchools( [restriction.school_id] )
          restriction.grades.items = response.data.sort() || []
        } catch ( e ) {
          console.log( e )
        }

        restriction.classes.items = await useClassrooms( { schoolFilter: restriction.school_id, gradeFilter: restriction.grade }, { props }, app )

      } else {
        restriction.grades.items = []
        restriction.classes.items = []
      }

    }

  }

  return { createRestriction, changeRestrictionFilter, removeRestriction }

}


function toValidFileName(filename) {
  var accent = [
    /[\300-\306]/g, /[\340-\346]/g, // A, a
    /[\310-\313]/g, /[\350-\353]/g, // E, e
    /[\314-\317]/g, /[\354-\357]/g, // I, i
    /[\322-\330]/g, /[\362-\370]/g, // O, o
    /[\331-\334]/g, /[\371-\374]/g, // U, u
    /[\321]/g, /[\361]/g, // N, n
    /[\307]/g, /[\347]/g, // C, c
  ];
  var noaccent = ['A','a','E','e','I','i','O','o','U','u','N','n','C','c'];

  filename = filename.replace(/ /g, '_');
  for(var i = 0; i < accent.length; i++){
    filename = filename.replace(accent[i], noaccent[i]);
  }

  filename = filename.replace(/[^A-Za-z0-9.]/g, '_');
  return filename;
}


export async function useCreateExploration( exploration, restrictions, documents, associationCreateMode, association, app, deletedRestrictions = {ids: []}, deletedDocuments = {ids: []} ) {
  let response

  if (associationCreateMode.on) {
    response = await app.$api.associations.create(association)
    exploration.association_id = response.data.res.id
  }

  response = await app.$api.explorationsType.count()
  exploration.num = (response.data.res + 1)

  let explorationId
  if ( exploration.id ) {
    await app.$api.explorationsType.updateExplorationType( exploration )
    explorationId = exploration.id
  } else {
    response = await app.$api.explorationsType.createExplorationType( exploration )
    explorationId = response.data.res.id
  }

  await Promise.all(
    restrictions.items.map(
      async ( restriction ) => {
        restriction.exploration_type_id = explorationId
        restriction.grade = parseInt( restriction.grade )
        if ( restriction.id ) {
          return await app.$api.restrictions.updateRestriction( restriction )
        } else {
          return await app.$api.restrictions.create( restriction )
        }
      }
    )
  )

  await Promise.all(
    documents.items.map(
      async ( document ) => {

        document.user_id = app.store.state.auth.user.userID
        document.parent_type = 'exploration'
        document.parent_id = explorationId

        if (document.id) {
          //EDIT
          document.file_kind === 'link' || document.file_kind === 'link_video' ? document.description = document.title : ''
          return await app.$api.documents.update(document)
        } else {
          //CREATE
          if (document.file_kind === 'document') {
            const formData = new FormData()
            formData.append('file', document.file, toValidFileName(document.file.name) + '_' + new Date().getTime())
            formData.append('user_id', exploration.user_id)
            formData.append('description', toValidFileName(document.file.name))
            formData.append('title', document.title)
            formData.append('file_kind', document.file_kind)
            formData.append('parent_id', document.parent_id)
            formData.append('num', document.num)
            formData.append("only_upload", false)

            return await app.$api.documents.upload(document.parent_type, formData)
          } else if (document.file_kind === 'link_video' || document.file_kind === 'link') {

            document.description = document.title
            return await app.$api.documents.create(document)

          }
        }

      }
    )
  )

  if ( deletedRestrictions.ids.length > 0 ) {
    await app.$api.restrictions.drop( deletedRestrictions.ids )
  }

  if ( deletedDocuments.ids.length > 0 ) {
    await app.$api.documents.drop( deletedDocuments.ids )
  }
}

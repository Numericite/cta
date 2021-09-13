<template>
  <div class="flex flex-row justify-between relative">
    <div class="w-5/6">
      <FormulateInput v-model="restriction.school_id"
                      :outerClass="['w-full my-8 text-left']"
                      :inputClass="['mt-8 py-5 px-6 focus:border-ms-red']"
                      :options="schoolsOptions"
                      :label="'Restriction' + ' ' + (index + 1)"
                      name="school_id"
                      type="select"
                      @change="getClassrooms(restriction.school_id)" />

      <div class="flex justify-between w-full">
        <FormulateInput v-model="restriction.grade"
                        :outerClass="['mb-8 mx-2 w-3/5']"
                        :inputClass="['mt-0 py-5 px-6 w-3/5 focus:border-ms-red']"
                        :options="gradeOptions"
                        name="grade"
                        type="select"
                        @change="getClassrooms(restriction.school_id, restriction.grade)" />

        <FormulateInput v-model="
                        restriction.classroom_id"
                        :outerClass="['mb-8 mx-2 w-2/5']"
                        :inputClass="['mt-0 py-5 px-6 w-2/5 focus:border-ms-red']"
                        :options="classroomsOptions"
                        :disabled="restriction.grade !== '' || restriction.school_id !== 'all' ? false : true"
                        name="classroom_id"
                        type="select" />
      </div>
    </div>
    <button type="button"
            class="absolute h-14 w-14 outline-none border-1 border-solid px-6 py-5 text-white rounded-lg bg-red btn-delete focus:outline-none"
            @click="handleDeleterestriction(restriction)">
      <img src="~/assets/img/TrashExploration.svg"
           class="h-5 w-7 font-extrabold ">
    </button>
  </div>
</template>

<script>

export default {

  props: {
    restriction: {
      type: Object,
      default: function () {
        return {}
      }
    },
    schoolsOptions: {
      type: Array,
      default: function () {
        return []
      }
    },
    gradeOptions: {
      type: Array,
      default: function () {
        return []
      }
    },
    index: {
      type: Number,
      default: function () {
        return 1
      }
    }
  },
  data() {
    return {
      classrooms: [],
      classroomsOptions: []
    }
  },
  methods: {
    async getClassrooms( school_ids, grade ) {
      let response = await this.$api.schools.getClassrooms( { school_ids: school_ids, grade: grade, page: 1, numberPerPage: 1000 } )
      this.classrooms = [...response.data]
      this.classroomsOptions = this.populateClassroomsOptions()
    },
    populateClassroomsOptions() {
      let classOptions = this.classrooms.map( classroom => {
        return { value: classroom.id, label: classroom.name }
      } )
      classOptions.unshift( { value: "all", label: "Toutes les classes" } )
      return classOptions
    },
    handleDeleterestriction( restriction ) {
      this.$emit( 'deleteRestriction', restriction )
    }
  }
}
</script>

<style lang="scss" scoped>
.btn-delete {
  top: 43%;
  right: 0%;
  transform: translate(-50%, -50%);
}
</style>
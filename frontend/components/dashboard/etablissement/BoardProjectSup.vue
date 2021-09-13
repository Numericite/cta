<template>
  <div class="mb-4">
    <table class="table-auto shadow-md w-full">
      <thead>
        <tr class="font-bold uppercase">
          <th class="w-1/4 px-4 py-6 bg-blue-dark text-blue-lightest border-solid border-r-2 border-black rounded-tl-lg">Mes fillières</th>
          <th class="w-1/4 px-4 py-6 bg-blue-dark text-blue-lightest border-solid border-r-2 border-black">Spécialités</th>
          <th class="w-1/4 px-4 py-2 bg-blue-dark text-blue-lightest border-solid border-r-2 border-black">Etablissements</th>
          <th class="w-1/4 px-4 py-2 bg-blue-dark text-blue-lightest rounded-tr-lg">Modalités d'entrée</th>
        </tr>
      </thead>
      <tbody class="bg-blue-lightestBis">
        <tr v-for="school_report in school_reports" :key="school_report.id">
          <td class="px-4 align-middle text-center font-bold text-blue-dark border-solid border-r-1 border-b-1 border-gray-lighter border-opacity-25">
            {{ school_report.stream_name }}
          </td>
          <td colspan="3">
            <table class="w-full">
              <tbody>
                <tr v-for="(school, index) in school_report.schools " :key="index" class="border-solid border-b-1 border-t-1 border-gray-lighter border-opacity-25">
                  <td class="w-1/3 px-4 py-4 align-middle text-center border-opacity-25">
                    <div v-for="(speciality, index) in school.specialities" :key="index" class="flex w-full justify-between pb-4 items-center">
                      <input v-model="school.specialities[index]" class="w-full text-xl text-blue p-3 rounded-lg text-black placeholder-opacity-50 placeholder-gray-light shadow-md mr-4" @blur="saveReport(school_report)">
                      <img src="~/static/icons/delete-icon.png" class="w-5 h-auto cursor-pointer mr-1" @click="deleteSpeciality(school_report, school.specialities, speciality)">
                    </div>
                    <div class="flex w-full justify-end">
                      <button class="bg-blue hover:bg-orange rounded-full w-7 py-1 text-white" @click="addSpeciality(school_report, school)">+</button>
                    </div>
                  </td>
                  <td class="w-1/3 px-4 align-middle">
                    <input v-model="school.school" class="w-full shadow-md px-3 text-blue  text-center uppercase text-2xl py-8 text-black placeholder-opacity-50 placeholder-gray-light" @blur="saveReport(school_report)">
                  </td>
                  <td class="w-1/3 px-4 align-middle relative">
                    <textarea v-model="school.entry_conditions" class="w-5/6 shadow-md rounded-none px-3 text-blue text-lg py-7 placeholder-opacity-50 placeholder-gray-light" @blur="saveReport(school_report)"/>
                    <div class="absolute delete-row w-6 justify-center align-middle">
                      <img src="~/static/icons/delete-icon.png" class="w-5 h-auto cursor-pointer" @click="deleteSchool(school_report, school)">
                    </div>
                  </td>
                </tr>
                <tr>
                  <td colspan="3" class="text-center py-4">
                    <button class="text-3xl bg-blue hover:bg-orange rounded-full w-7 py-1 text-white" @click="addSchool(school_report)">+</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  import _ from 'lodash'
  import Button from '../../../mixins/button'

  export default {
    name: 'BoardProjectSup',
    components: { Button },
    props: {
      user_id: {
        type: String,
        default: null
      },
      streams: {
        type: Array,
        default: () => {
          return []
        }
      }
    },
    data () {
      return {
        school_reports: [],
        empty_school: {
          specialities: [''],
          school: '',
          entry_conditions: ''
        }
      }
    },
    created() {
      this.getReports()
    },
    methods: {
      async getReports() {
        let response = await this.$api.schoolReports.getList({
          page: 1,
          numberPerPage: 1000,
          user_ids: [this.user_id || this.$store.state.auth.user.userID]
        })

        this.school_reports = _.map(response.data, (report) => {
          report.stream_name = _.get(_.find(this.streams, {'id': report.stream_id}), 'name', '')
          return report
        }) || []

        if (this.school_reports.length === 0) {
          this.createUserReports()
        }
      },
      async createUserReports() {
        const stream_ids = this.$store.state.auth.user.config.stream_ids || []

        if (stream_ids.length > 0) {
          await Promise.all(stream_ids.map(async (stream_id) => {
            await this.$api.schoolReports.create({
              stream_id: stream_id,
              user_id: this.user_id || this.$store.state.auth.user.userID,
              schools: [
                Object.assign({}, this.empty_school)
              ]
            })
          }))

          this.getReports()
        }
      },
      async saveReport(report) {
        await this.$api.schoolReports.update(report)
      },
      addSpeciality(report, school) {
        school.specialities.push('')
        this.saveReport(report)
      },
      deleteSpeciality(report, specialities, speciality) {
        const index = specialities.indexOf(speciality);
        if (index > -1) {
          specialities.splice(index, 1);
        }
        this.saveReport(report)
      },
      deleteSchool(report, school) {
        report.schools = _.without(report.schools, school)
        this.saveReport(report)
      },
      addSchool(report) {
        report.schools.push(Object.assign({}, this.empty_school))
        this.saveReport(report)
      }
    },
  }
</script>

<style lang="scss">
  .delete-row {
    right: 5%;
    top: 50%;
    transform: translateY(-50%);
  }
</style>
